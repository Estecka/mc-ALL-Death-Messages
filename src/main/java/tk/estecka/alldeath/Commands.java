package tk.estecka.alldeath;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.BooleanRule;
import tk.estecka.alldeath.DeathRules.MobCategory;
import java.util.Collection;
import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.command.argument.EntityArgumentType.entities;
import static net.minecraft.command.argument.EntityArgumentType.getEntities;

public class Commands 
{
	static public final Identifier ID = new Identifier("alldeath", "command");
	static private final String ENTITY_ARG = "entity";
	static private final String RULENAME_ARG = "rule name";
	static private final String RULETYPE_ARG = "rule type";
	static private final String BOOL_ARG = "boolean";

	static public void	Register(){
		CommandRegistrationCallback.EVENT.register(ID, Commands::RegisterWith);
	}

	static public void RegisterWith(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment env){
		var root = literal("alldeathmsg").requires(s->s.hasPermissionLevel(2));

		root.then(literal("test")
			.then(argument(ENTITY_ARG, entities())
				.executes(Commands::TestEntities)
			)
		);

		root.then(literal("see-enabled")
			.executes(Commands::SeeEnabled)
		);

		root.then(literal("disable-all")
			.executes(Commands::DisableAll)
		);

		root.then(literal("set")
			.then(argument(RULENAME_ARG, string())
				.then(argument(RULETYPE_ARG, string())
					.then(argument(BOOL_ARG, bool())
						.executes(Commands::SetRule)
					)	
				)
			)
		);

		dispatcher.register(root);
	}

	static private int	TestEntities(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<? extends Entity> entities = getEntities(context, ENTITY_ARG);

		for (Entity e : entities)
		{
			MutableText result = Text.empty();
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
			context.getSource().sendFeedback(result, false);
		}

		return 0;
	}

	static private int	SeeEnabled(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final World world = context.getSource().getWorld();
		final GameRules gamerules = world.getGameRules();
		for (var rule : DeathRules.nameToRule.entrySet()) {
			boolean death = gamerules.getBoolean(rule.getValue().death);
			boolean kill  = gamerules.getBoolean(rule.getValue().kill);
			if (death || kill){
				MutableText text = Text.literal(rule.getKey()).append(": ").formatted(Formatting.BOLD);
				if (death) text.append("death");
				if (death && kill) text.append(", ");
				if (kill ) text.append("kill" );
				context.getSource().sendFeedback(text, false);
			}
		}
		return 0;
	}

	static private int	DisableAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final World world = context.getSource().getWorld();
		final GameRules gamerules = world.getGameRules();
		final MinecraftServer server = world.getServer();
		for (var rule : DeathRules.nameToRule.values()) {
			gamerules.get(rule.death).set(false, server);
			gamerules.get(rule.kill ).set(false, server);
		}
		context.getSource().sendFeedback(Text.translatableWithFallback("command.alldeathmsg.disable-all.success", "Disabled all death messages"), true);
		return 1;
	}

	static private int	SetRuleFailure(CommandContext<ServerCommandSource> context, String ruleName, String ruleType){
		context.getSource().sendError(Text.translatableWithFallback("command.alldeathmsg.set.failure", "Invalid rule name: %s.%s", ruleName, ruleType));
		return -1;
	}

	static private int	SetRule(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final World world = context.getSource().getWorld();
		final GameRules gamerules = world.getGameRules();

		String ruleName = getString(context, RULENAME_ARG);
		String ruleType = getString(context, RULETYPE_ARG);
		boolean value = getBool(context, BOOL_ARG);

		MobCategory rules = DeathRules.nameToRule.get(getString(context, RULENAME_ARG));
		if (rules == null)
			return SetRuleFailure(context, ruleName, ruleType);

		GameRules.Key<BooleanRule> ruleKey;
		switch (ruleType) {
			case "death": ruleKey=rules.death; break;
			case "kill" : ruleKey=rules.kill ; break;
			default: return SetRuleFailure(context, ruleName, ruleType);
		}

		BooleanRule rule = gamerules.get(ruleKey);
		rule.set(value, world.getServer());
		context.getSource().sendFeedback(Text.translatable("commands.gamerule.set", ruleKey.getName(), rule.toString()), true);
		return 1;
	}
}
