package tk.estecka.alldeath.config;

import java.util.HashSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
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
		Identifier id = new Identifier(typeName);
		if(id.getNamespace().equals("minecraft") && !Registries.ENTITY_TYPE.containsId(id))
			AllDeathMessages.LOGGER.warn("The type \"{}\" does not exist in vanilla minecraft", id);
	}
}
