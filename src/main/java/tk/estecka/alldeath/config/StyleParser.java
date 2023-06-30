package tk.estecka.alldeath.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.DeathStyles.MobStyle;

public class StyleParser 
{
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
		style.bold      = GetOptionalBool(jObj, "bold");
		style.italic    = GetOptionalBool(jObj, "italic");
		style.underline = GetOptionalBool(jObj, "underline");
		return style;
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
