package tk.estecka.alldeath;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleCategory;
import tk.estecka.alldeath.config.RuleParser;
import tk.estecka.alldeath.config.JsonConfig;
import static tk.estecka.alldeath.AllDeathMessages.MODID;


public class DeathRules
{
	static public class MobCategory
	{
		public final GameRule<Boolean> death;
		public final GameRule<Boolean> kill;
		public MobCategory(String name, Boolean deathDefault, Boolean killDefault){
			this.death = CreateBooleanRule(DEATH_CATEGORY, name, deathDefault);
			this.kill  = CreateBooleanRule(KILL_CATEGORY,  name, killDefault );
		}
	}

	static public final String	CONFIG_FILE = "alldeath-rules.json";
	static public final HashMap<String,MobCategory> nameToRule = new HashMap<>();
	static public final GameRuleCategory DEATH_CATEGORY = GameRuleCategory.register(Identifier.of(MODID, "death"));
	static public final GameRuleCategory KILL_CATEGORY  = GameRuleCategory.register(Identifier.of(MODID, "kill" ));

	static private GameRule<Boolean> CreateBooleanRule(GameRuleCategory category, String name, boolean defaultValue){
		return GameRuleBuilder.forBoolean(defaultValue)
			.category(category)
			.buildAndRegister(Identifier.of(MODID, "show_death_messages."+name+"."+category.id().getPath()))
			;
	}

	static private void	InitializeBuiltinRule(String ruleName, boolean death, boolean kill){
		nameToRule.put(ruleName, new MobCategory(ruleName, death, kill));
	}

	static public void initialize() 
	{
		InitializeBuiltinRule("all"       , false, false);
		InitializeBuiltinRule("player"    , true , false);
		InitializeBuiltinRule("named"     , true , true );
		InitializeBuiltinRule("tamed"     , true , true );
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

	public static boolean IsRuleEnabled(Entity entity, GameRule<Boolean> key){
		return ((ServerWorld)entity.getEntityWorld()).getGameRules().getValue(key);
	}

	@Nullable
	public static GameRule<Boolean> HasDeathRule(LivingEntity entity){
		for (var entry : nameToRule.entrySet())
			if (IsRuleEnabled(entity, entry.getValue().death) && EntityPredicates.getOrDefault(entry.getKey()).test(entity))
				return entry.getValue().death;
		return null;
	}

	@Nullable
	public static GameRule<Boolean> HasKillRule(Entity entity){
		for (var entry : nameToRule.entrySet())
			if (IsRuleEnabled(entity, entry.getValue().kill) && EntityPredicates.getOrDefault(entry.getKey()).test(entity))
				return entry.getValue().kill;
		return null;
	}

}
