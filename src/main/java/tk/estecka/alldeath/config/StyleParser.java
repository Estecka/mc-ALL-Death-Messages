package tk.estecka.alldeath.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;
import net.minecraft.text.TextColor;
import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.EntityPredicates;
import tk.estecka.alldeath.DeathStyles.MobStyle;

public class StyleParser 
{
	static private final Gson gson = new Gson();

	static public List<MobStyle>	CreateConfigFromJson(JsonElement json)
	{
		var config = new ArrayList<MobStyle>();
		if (!json.isJsonArray()){
			AllDeathMessages.LOGGER.error("Style config root is not a Json Array");
			return null;
		}

		JsonArray root = json.getAsJsonArray();
		int i=0;
		for (JsonElement elt : root)
		{
			MobStyle style = CreateStyleFromJson(elt);
			if (style == null)
				AllDeathMessages.LOGGER.error("Invalid style at index {}", i);
			else if (style.IsEmpty())
				AllDeathMessages.LOGGER.warn("Style at index {} is empty and will eb ignored.", i);
			else
				config.add(style);

			++i;
		}

		return config;
	}

	static private MobStyle	CreateStyleFromJson(JsonElement json){
		if (!json.isJsonObject())
			return null;

		MobStyle style = new MobStyle();
		JsonObject jObj = json.getAsJsonObject();

		style.predicate = GetPredicate(jObj);
		if (style.predicate == null){
			AllDeathMessages.LOGGER.error("Invalid predicate.");
			return null;
		}

		style.color     = GetOptionalColor(jObj);
		style.bold      = GetOptionalBool(jObj, "bold");
		style.italic    = GetOptionalBool(jObj, "italic");
		style.underline = GetOptionalBool(jObj, "underline");
		style.strike    = GetOptionalBool(jObj, "strikethrough");
		style.cursed    = GetOptionalBool(jObj, "obfuscated");
		return style;
	}

	static private Predicate<Entity>	GetPredicate(JsonObject parent){
		if (!parent.has("rule"))
			return null;

		JsonElement elt = parent.get("rule");
		if (elt.isJsonArray()){
			var p = PredicateParser.CreateTypePredicateFromJson(elt, gson);
			if (p==null || p.IsEmpty())
				return null;
			else
				return p;
		}
		else if (elt.isJsonPrimitive() && elt.getAsJsonPrimitive().isString()){
			String ruleName = elt.getAsString();
			return EntityPredicates.getOrDefault(ruleName);
		}
		else
			return null;
	}

	static private TextColor	GetOptionalColor(JsonObject parent){
		if (!parent.has("color"))
			return null;

		JsonElement elt = parent.get("color");
		if (elt.isJsonNull())
			return null;
		if (!elt.isJsonPrimitive() || !elt.getAsJsonPrimitive().isString()){
			AllDeathMessages.LOGGER.error("Invalid colour format: {}", elt);
			return null;
		}

		String colourString = elt.getAsString();
		TextColor c = TextColor.parse(colourString);
		if (c == null)
			AllDeathMessages.LOGGER.error("Invalid colour name: {}", colourString);
		return c;
	}

	static private Boolean	GetOptionalBool(JsonObject parent, String keyName){
		if (!parent.has(keyName) || parent.get(keyName).isJsonNull())
			return null;
		try {
			return parent.get(keyName).getAsBoolean();
		}
		catch (Exception e){
			AllDeathMessages.LOGGER.error("Invalid boolean in style config: {}", e);
			return null;
		}
	}

}
