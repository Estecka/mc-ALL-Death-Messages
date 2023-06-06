package net.fabricmc.alldeath;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;


public class DeathRules implements ModInitializer 
{
	static public class MobCategory 
	{
		public final GameRules.Key<GameRules.BooleanRule> death;
		public final GameRules.Key<GameRules.BooleanRule> kill;
		public MobCategory(String name, Boolean deathDefault, Boolean killDefault){
			this.death = BooleanRule(name+".death", deathDefault);
			this.kill  = BooleanRule(name+".kill",  killDefault );
		}
	}

	public static final MobCategory NAMED = new MobCategory("named", true, true);
	public static final MobCategory OTHER = new MobCategory("other", false, true);

	// public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(new Identifier("alldeath", "messages"), Text.of("Death Messages"));

	@Override
	public void onInitialize() {
		// Even though empty, it is required for fabric to initialize static variables, (and thus game rules), at the required time.
	}

	private static GameRules.Key<GameRules.BooleanRule>	BooleanRule(String name, boolean defaultValue){
		// AllDeathMessages.LOGGER.warn("Registering: {}", name);
		return GameRuleRegistry.register("showDeathMessages."+name, Category.CHAT, GameRuleFactory.createBooleanRule(defaultValue));
	}

	public static ArrayList<MobCategory>	GetCategories(Entity entity){
		ArrayList<MobCategory> result = new ArrayList<DeathRules.MobCategory>();
		if (entity.hasCustomName() || entity.isPlayer())
			result.add(NAMED);
		if (result.isEmpty())
			result.add(OTHER);
		return result;
	}
}
