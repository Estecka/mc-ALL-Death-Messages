package tk.estecka.alldeath;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import tk.estecka.alldeath.DeathRules.MobCategory;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.arguments.EntityArgument.entities;
import static net.minecraft.commands.arguments.EntityArgument.getEntities;
import static tk.estecka.alldeath.AllDeathMessages.ServersideTranslatable;

public class Commands 
{
	static public final Identifier ID = Identifier.fromNamespaceAndPath("alldeath", "command");
	static private final String ENTITY_ARG = "entity";
	static private final String RULENAME_ARG = "rule name";
	static private final String RULETYPE_ARG = "rule type";
	static private final String BOOL_ARG = "boolean";
	static private final String CONFIRM_ARG = "confirm";

	static public void	Register(){
		CommandRegistrationCallback.EVENT.register(ID, Commands::RegisterWith);
	}

	static public void RegisterWith(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, CommandSelection env){
		var root = literal("alldeathmsg").requires(s->s.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS)));

		root.then(literal("test")
			.then(argument(ENTITY_ARG, entities())
				.executes(Commands::TestEntities)
			)
		);

		root.then(literal("see-enabled")
			.executes(Commands::SeeEnabled)
		);

		root.then(literal("reload-styles")
			.executes(Commands::ReloadStyles)
		);

		root.then(literal("disable-all")
			.then(argument(CONFIRM_ARG, bool())
				.executes(Commands::DisableAll)
			)
		);

		root.then(literal("set")
			.then(argument(RULENAME_ARG, string())
				.suggests(Commands::RulenameAutofill)
				.then(argument(RULETYPE_ARG, string())
					.suggests(Commands::RuletypeAutofill)
					.then(argument(BOOL_ARG, bool())
						.executes(Commands::SetRule)
					)	
				)
			)
		);

		dispatcher.register(root);
	}

	static private CompletableFuture<Suggestions> RulenameAutofill(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder){
		for (var name : DeathRules.nameToRule.keySet())
			builder.suggest(name);
		return builder.buildFuture();
	}

	static private CompletableFuture<Suggestions> RuletypeAutofill(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder){
		builder.suggest("kill");
		builder.suggest("death");
		return builder.buildFuture();
	}

	static private int	TestEntities(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<? extends Entity> entities = getEntities(context, ENTITY_ARG);

		for (Entity e : entities)
		{
			MutableComponent result = Component.empty();
			result.append(DeathStyles.getStyledName(e)).append(": ");
			boolean first = true;
			for (var predicate : EntityPredicates.predicates.entrySet())
			if  (predicate.getValue().test(e)){
				if (first)
					first = false;
				else
					result.append(", ");
				result.append(predicate.getKey());
			}
			context.getSource().sendSuccess(()->result, false);
		}

		return 0;
	}

	static private int	ReloadStyles(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		DeathStyles.STYLES.clear();
		if (DeathStyles.initialize()) {
			context.getSource().sendSuccess(()->ServersideTranslatable("command.alldeathmsg.reload-styles.success"), true);
			return 1;
		}
		else {
			context.getSource().sendFailure(ServersideTranslatable("command.alldeathmsg.reload-styles.failure"));
			return -1;
		}
	}

	static private int	SeeEnabled(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		final var source = context.getSource();
		final GameRules gamerules = source.getLevel().getGameRules();

		boolean first = true;
		for (var rule : DeathRules.nameToRule.entrySet()) {
			boolean death = gamerules.get(rule.getValue().death);
			boolean kill  = gamerules.get(rule.getValue().kill);
			if (death || kill){
				if (first) {
					first = false;
					source.sendSuccess(()->ServersideTranslatable("command.alldeathmsg.see-enabled.success"), false);
				}
				MutableComponent text = Component.literal("- ").append(rule.getKey()).append(": ");
				if (death) text.append("Death");
				if (death && kill) text.append(", ");
				if (kill ) text.append("Kill");
				source.sendSuccess(()->text, false);
			}
		}

		if (first)
			source.sendSuccess(()->ServersideTranslatable("command.alldeathmsg.see-enabled.failure"), false);

		return 0;
	}

	static private int	DisableAll(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		final Level world = context.getSource().getLevel();
		final MinecraftServer server = world.getServer();
		final GameRules gamerules = context.getSource().getLevel().getGameRules();
		if (!getBool(context, CONFIRM_ARG)){
			context.getSource().sendFailure(ServersideTranslatable("command.alldeathmsg.disable-all.failure"));
			return -1;
		}

		for (var rule : DeathRules.nameToRule.values()) {
			gamerules.set(rule.death, false, server);
			gamerules.set(rule.kill,  false, server);
		}
		context.getSource().sendSuccess(()->ServersideTranslatable("command.alldeathmsg.disable-all.success"), true);
		return 1;
	}

	static private int	SetRuleFailure(CommandContext<CommandSourceStack> context, String ruleName, String ruleType){
		context.getSource().sendFailure(ServersideTranslatable("command.alldeathmsg.set.failure", ruleName, ruleType));
		return -1;
	}

	static private int	SetRule(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		final MinecraftServer server = context.getSource().getServer();
		final GameRules gamerules = context.getSource().getLevel().getGameRules();

		String ruleName = getString(context, RULENAME_ARG);
		String ruleType = getString(context, RULETYPE_ARG);
		boolean value = getBool(context, BOOL_ARG);

		MobCategory rules = DeathRules.nameToRule.get(getString(context, RULENAME_ARG));
		if (rules == null)
			return SetRuleFailure(context, ruleName, ruleType);

		GameRule<Boolean> ruleKey;
		switch (ruleType) {
			case "death": ruleKey=rules.death; break;
			case "kill" : ruleKey=rules.kill ; break;
			default: return SetRuleFailure(context, ruleName, ruleType);
		}

		gamerules.set(ruleKey, value, server);
		context.getSource().sendSuccess(()->Component.translatable("commands.gamerule.set", ruleKey.getIdentifier().toShortString(), String.valueOf(value)), true);
		return 1;
	}
}
