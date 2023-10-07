package tk.estecka.alldeath;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.Collection;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.command.argument.EntityArgumentType.entities;
import static net.minecraft.command.argument.EntityArgumentType.getEntities;

public class Commands 
{
	static public final Identifier ID = new Identifier("alldeath", "command");
	static private final String ENTITY_ARG = "entity";

	static public void	Register(){
		CommandRegistrationCallback.EVENT.register(ID, Commands::RegisterWith);
	}

	static public void RegisterWith(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment env){
		var root = literal("alldeathmsg");

		root.then(literal("test")
			.then(argument(ENTITY_ARG, entities())
				.executes(Commands::TestEntities)
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
			context.getSource().sendFeedback(()->result, false);
		}

		return 0;
	}
}