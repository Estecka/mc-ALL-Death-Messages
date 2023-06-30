package tk.estecka.alldeath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;
import tk.estecka.alldeath.config.ConfigParser;
import tk.estecka.alldeath.config.JsonConfig;


public class DeathRules implements ModInitializer 
{
	static public class MobCategory 
	{
		public final Key<BooleanRule> death;
		public final Key<BooleanRule> kill;
		public MobCategory(String name, Boolean deathDefault, Boolean killDefault){
			this.death = BooleanRule(name+".death", deathDefault);
			this.kill  = BooleanRule(name+".kill",  killDefault );
		}
	}

	static public final String	CONFIG_FILE = "alldeath-rules.json";

	static public final MobCategory NAMED = new MobCategory("named", true, true);
	static public final MobCategory OTHER = new MobCategory("other", false, true);
	static private HashMap<MobCategory, Predicate<Entity>> customRules = new HashMap<MobCategory, Predicate<Entity>>();
	static public final Set<String> RESERVED_NAMES = new HashSet<String>(){{
		add("named");
		add("other");
	}};


	static private Key<BooleanRule>	BooleanRule(String name, boolean defaultValue){
		return GameRuleRegistry.register("showDeathMessages."+name, Category.CHAT, GameRuleFactory.createBooleanRule(defaultValue));
	}

	static public ArrayList<MobCategory>	GetCategories(Entity entity){
		ArrayList<MobCategory> result = new ArrayList<DeathRules.MobCategory>();

		for (Entry<MobCategory, Predicate<Entity>> entry : customRules.entrySet())
			if (entry.getValue().test(entity))
				result.add(entry.getKey());

		if (entity.hasCustomName() || entity.isPlayer())
			result.add(NAMED);
		if (result.isEmpty())
			result.add(OTHER);
		return result;
	}

	@Override
	public void onInitialize() 
	{
		JsonConfig configFile = new JsonConfig(CONFIG_FILE, AllDeathMessages.MODID, AllDeathMessages.LOGGER);
		JsonElement json;
		try {
			json = configFile.GetOrCreateJsonFile();
		} catch (IOException e){
			AllDeathMessages.LOGGER.error("Unable to load config file");
			return;
		}

		var config = ConfigParser.CreateConfigFromJson(json);
		for (String ruleName : config.keySet()){
			if (RESERVED_NAMES.contains(ruleName))
				AllDeathMessages.LOGGER.error("The rule name \"{}\" is reserved. The rule defined in the config will be ignored.", ruleName);
			else {
				Predicate<Entity> predicate = new TypeEntityPredicate(config.get(ruleName));
				EntityPredicates.predicates.put(ruleName, predicate);
				customRules.put(
					new MobCategory(ruleName, true, true),
					predicate
				);
			}
		}
	}

}
