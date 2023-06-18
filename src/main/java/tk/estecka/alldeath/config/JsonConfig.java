package tk.estecka.alldeath.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import tk.estecka.alldeath.AllDeathMessages;

public class JsonConfig 
{
	static public JsonElement	GetJsonFromFile(String filename) throws FileNotFoundException
	{
		Path path = FabricLoader.getInstance().getConfigDir().resolve(filename+".json");
		File file = path.toFile();

		FileInputStream cin = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(cin);
		JsonElement json = JsonParser.parseReader(reader);
		return json;
	}

	static public HashMap<String, String[]>	CreateConfigFromJson(JsonElement json)
	{
		var config = new HashMap<String, String[]>();

		Gson gson = new Gson();
		if (!json.isJsonObject()){
			AllDeathMessages.LOGGER.error("Config file root is not a Json Object");
			return null;
		}

		JsonObject root = json.getAsJsonObject();
		for (String ruleName : root.keySet()) {
			String[] rule = CreateRuleFromJson(ruleName, root.get(ruleName), gson);
			if (rule == null)
				AllDeathMessages.LOGGER.error("Invalid rule \"{}\"", ruleName);
			else if (rule.length <= 0)
				AllDeathMessages.LOGGER.warn("Rule \"{}\" contains no valid predicate and will be ignored", ruleName);
			else
				config.put(ruleName, rule);
		}
		return config;
	}

	static private String[]	CreateRuleFromJson(String ruleName, JsonElement json, Gson gson){
		var result = new HashSet<String>();

		if (!json.isJsonArray())
			return null;
		else for (JsonElement predicate : json.getAsJsonArray()){
			if (!predicate.isJsonPrimitive() || !predicate.getAsJsonPrimitive().isString())
				AllDeathMessages.LOGGER.error("Rule \"{}\" contains an invalid predicate", ruleName);
			else {
				String typeId = predicate.getAsString();
				if (result.contains(typeId))
					AllDeathMessages.LOGGER.warn("Rule \"{}\" contains a duplicate predicate ({})", ruleName, typeId);
				else{
					result.add(typeId);
					SpellCheck(typeId);
				}
			} 
		}

		return result.toArray(new String[result.size()]);
	}

	static private void	SpellCheck(String typeName){
		Identifier id = new Identifier(typeName);
		if(id.getNamespace().equals("minecraft") && !Registries.ENTITY_TYPE.containsId(id))
			AllDeathMessages.LOGGER.warn("The type \"{}\" does not exist in vanilla minecraft", id);
	}

}
