package tk.estecka.alldeath;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;
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

	static public final MobCategory NAMED = new MobCategory("named", true, true);
	static public final MobCategory OTHER = new MobCategory("other", false, true);
	static private HashMap<MobCategory, String[]> customRules = new HashMap<MobCategory, String[]>();
	static public final Set<String> RESERVED_NAMES = new HashSet<String>(){{
		add("named");
		add("other");
	}};


	static private Key<BooleanRule>	BooleanRule(String name, boolean defaultValue){
		return GameRuleRegistry.register("showDeathMessages."+name, Category.CHAT, GameRuleFactory.createBooleanRule(defaultValue));
	}

	static public ArrayList<MobCategory>	GetCategories(Entity entity){
		ArrayList<MobCategory> result = new ArrayList<DeathRules.MobCategory>();
		
		String entityType = EntityType.getId(entity.getType()).toString();
		for (Entry<MobCategory, String[]> entry : customRules.entrySet()) {
			for (String type : entry.getValue())
			if (type.equals(entityType)) {
				entity.getType();
				result.add(entry.getKey());
				break;
			}
		}

		if (entity.hasCustomName() || entity.isPlayer())
			result.add(NAMED);
		if (result.isEmpty())
			result.add(OTHER);
		return result;
	}

	@Override
	public void onInitialize() 
	{
		JsonElement json;
		try {
			json = JsonConfig.GetJsonFromFile("AllDeathMessages-rules");
		} catch (FileNotFoundException e){
			AllDeathMessages.LOGGER.error("The config file does not exist");
			return;
		}

		var config = JsonConfig.CreateConfigFromJson(json);
		for (String ruleName : config.keySet()){
			if (!RESERVED_NAMES.contains(ruleName))
				customRules.put(
					new MobCategory(ruleName, true, true),
					config.get(ruleName)
				);
			else
				AllDeathMessages.LOGGER.error("The rule name \"{}\" is reserved. The rule defined in the config will be ignored.", ruleName);
		}
	}
}
