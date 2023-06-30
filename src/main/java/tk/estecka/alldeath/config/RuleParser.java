package tk.estecka.alldeath.config;

import java.util.HashMap;
import java.util.function.Predicate;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.TypeEntityPredicate;

public class RuleParser {
	static public HashMap<String, Predicate<Entity>>	CreateConfigFromJson(JsonElement json)
	{
		var config = new HashMap<String, Predicate<Entity>>();

		Gson gson = new Gson();
		if (!json.isJsonObject()){
			AllDeathMessages.LOGGER.error("Rule config root is not a Json Object");
			return null;
		}

		JsonObject root = json.getAsJsonObject();
		for (String ruleName : root.keySet()) {
			TypeEntityPredicate rule = PredicateParser.CreateTypePredicateFromJson(root.get(ruleName), gson);
			if (rule == null)
				AllDeathMessages.LOGGER.error("Invalid rule \"{}\"", ruleName);
			else if (rule.size() <= 0)
				AllDeathMessages.LOGGER.warn("Rule \"{}\" contains no valid predicate and will be ignored", ruleName);
			else
				config.put(ruleName, rule);
		}
		return config;
	}

}
