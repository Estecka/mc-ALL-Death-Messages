package net.fabricmc.alldeath;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;


public class DeathRules implements ModInitializer {
	public static final GameRules.Key<GameRules.BooleanRule> NAMED_DEATH = BooleanRule("Named", true);
	public static final GameRules.Key<GameRules.BooleanRule> NAMED_KILL  = BooleanRule("NamedKill", true);
	public static final GameRules.Key<GameRules.BooleanRule> OTHER_DEATH = BooleanRule("Other", false);
	public static final GameRules.Key<GameRules.BooleanRule> OTHER_KILL  = BooleanRule("OtherKill", true);

	// public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(new Identifier("alldeath", "messages"), Text.of("Death Messages"));

	@Override
	public void onInitialize() {
		// Even though empty, it is required for fabric to initialize static variables, (and thus game rules), at the required time.
	}

	private static GameRules.Key<GameRules.BooleanRule>	BooleanRule(String name, boolean defaultValue){
		AllDeathMessages.LOGGER.warn("Registering: {}", name);
		return GameRuleRegistry.register("showDeathMessages"+name, Category.CHAT, GameRuleFactory.createBooleanRule(defaultValue));
	}
}
