package tk.estecka.alldeath.config;

import java.util.HashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.TypeEntityPredicate;

public class PredicateParser {
	static public TypeEntityPredicate	CreateTypePredicateFromJson(JsonElement json, Gson gson){
		var result = new HashSet<String>();

		if (!json.isJsonArray())
			return null;
		else for (JsonElement predicate : json.getAsJsonArray()){
			if (!predicate.isJsonPrimitive() || !predicate.getAsJsonPrimitive().isString())
				AllDeathMessages.LOGGER.error("Invalide predicate: \"{}\"", predicate);
			else {
				String typeId = predicate.getAsString();
				if (result.contains(typeId))
					AllDeathMessages.LOGGER.warn("Duplicate predicate: \"{}\"", typeId);
				else{
					result.add(typeId);
					SpellCheck(typeId);
				}
			} 
		}

		return new TypeEntityPredicate( result.toArray(new String[result.size()]) );
	}

	static private void	SpellCheck(String typeName){
		Identifier id = Identifier.read(typeName).getOrThrow();
		if(id.getNamespace().equals("minecraft") && !BuiltInRegistries.ENTITY_TYPE.containsKey(id))
			AllDeathMessages.LOGGER.warn("The type \"{}\" does not exist in vanilla minecraft", id);
	}
}
