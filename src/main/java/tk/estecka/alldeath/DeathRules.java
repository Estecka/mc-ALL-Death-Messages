package tk.estecka.alldeath;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Key;
import tk.estecka.alldeath.config.RuleParser;
import tk.estecka.alldeath.config.JsonConfig;
import static tk.estecka.alldeath.AllDeathMessages.MODID;


public class DeathRules
{
	static public class MobCategory
	{
		public final Key<BooleanRule> death;
		public final Key<BooleanRule> kill;
		public MobCategory(String name, Boolean deathDefault, Boolean killDefault){
			this.death = CreateBooleanRule(DEATH_CATEGORY, name, deathDefault);
			this.kill  = CreateBooleanRule(KILL_CATEGORY,  name, killDefault );
		}
	}

	static public final String	CONFIG_FILE = "alldeath-rules.json";
	static public final HashMap<String,MobCategory> nameToRule = new HashMap<>();
	static public final CustomGameRuleCategory DEATH_CATEGORY = new CustomGameRuleCategory(new Identifier(MODID, "death"), Text.translatable("gamerule.category.alldeath.death").formatted(Formatting.BOLD, Formatting.YELLOW));
	static public final CustomGameRuleCategory KILL_CATEGORY  = new CustomGameRuleCategory(new Identifier(MODID, "kill" ), Text.translatable("gamerule.category.alldeath.kill" ).formatted(Formatting.BOLD, Formatting.YELLOW));

	static private Key<BooleanRule>	CreateBooleanRule(CustomGameRuleCategory category, String name, boolean defaultValue){
		return GameRuleRegistry.register("showDeathMessages."+name+"."+category.getId().getPath(), category, GameRuleFactory.createBooleanRule(defaultValue));
	}

	static private void	InitializeBuiltinRule(String ruleName, boolean death, boolean kill){
		nameToRule.put(ruleName, new MobCategory(ruleName, death, kill));
	}

	static public void initialize() 
	{
		InitializeBuiltinRule("all"       , false, true );
		InitializeBuiltinRule("named"     , true , true );
		InitializeBuiltinRule("persistent", true , false);
		InitializeBuiltinRule("ephemeral" , false, false);
		InitializeBuiltinRule("hostile"   , false, false);
		InitializeBuiltinRule("passive"   , false, false);

		JsonConfig configFile = new JsonConfig(CONFIG_FILE, MODID, AllDeathMessages.LOGGER);
		JsonElement json;
		try {
			json = configFile.GetOrCreateJsonFile();
		} catch (IOException e){
			AllDeathMessages.LOGGER.error("Unable to load config file: {}", CONFIG_FILE);
			return;
		}

		HashMap<String, Predicate<Entity>> config = RuleParser.CreateConfigFromJson(json);
		for (var entry : config.entrySet()){
			String ruleName = entry.getKey();
			if (EntityPredicates.predicates.containsKey(ruleName))
				AllDeathMessages.LOGGER.error("The rule name \"{}\" is reserved. The rule defined in the config will be ignored.", ruleName);
			else {
				EntityPredicates.put(ruleName, entry.getValue());
				nameToRule.put(ruleName, new MobCategory(ruleName, true, true));
			}
		}
	}

	public static boolean	IsRuleEnabled(World world, GameRules.Key<BooleanRule> key){
		BooleanRule rule = world.getGameRules().get(key);
		if (rule != null)
			return rule.get();
		else {
			AllDeathMessages.LOGGER.error("The rule {} doesn't exist", key.getName());
			return false;
		}
	}

	@Nullable
	public static GameRules.Key<BooleanRule>	HasDeathRule(LivingEntity entity){
		for (var entry : nameToRule.entrySet())
			if (IsRuleEnabled(entity.getWorld(), entry.getValue().death) && EntityPredicates.getOrDefault(entry.getKey()).test(entity))
				return entry.getValue().death;
		return null;
	}

	@Nullable
	public static GameRules.Key<BooleanRule>	HasKillRule(Entity entity){
		for (var entry : nameToRule.entrySet())
			if (IsRuleEnabled(entity.getWorld(), entry.getValue().kill) && EntityPredicates.getOrDefault(entry.getKey()).test(entity))
				return entry.getValue().kill;
		return null;
	}

}
