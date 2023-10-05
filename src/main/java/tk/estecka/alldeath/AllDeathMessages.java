package tk.estecka.alldeath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;

public class AllDeathMessages implements ModInitializer
{
	static public final String MODID = "alldeathmessages";
	static public final Logger LOGGER = LoggerFactory.getLogger("AllDeath");

	static public final GameRules.Key<BooleanRule> COORD_RULE = GameRuleRegistry.register("showDeathCoordinates", GameRules.Category.CHAT, GameRuleFactory.createBooleanRule(true));

	@Override
	public void	onInitialize(){
		DeathRules.initialize();
		DeathStyles.initialize();
	}
}
