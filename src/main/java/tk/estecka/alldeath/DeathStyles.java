package tk.estecka.alldeath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Predicate;
import com.google.gson.JsonElement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import tk.estecka.alldeath.config.JsonConfig;
import tk.estecka.alldeath.config.StyleParser;

public class DeathStyles
{
	static public class	MobStyle
	{
		public Predicate<Entity> predicate = e->false;
		public Boolean bold = null;
		public Boolean italic = null;
		public Boolean underline = null;

		public boolean IsEmpty(){
			return bold==null && italic==null && underline!=null;
		}
	}

	static public final String CONFIG_FILE = "alldeath-styles.json";
	static public final List<MobStyle> STYLES = new ArrayList<MobStyle>();

	static public Text	getStyledName(LivingEntity entity)
	{
		// AllDeathMessages.LOGGER.warn("Name of: {}", entity);
		Text name = entity.getDisplayName();
		Style style = name.getStyle().withUnderline(true).withColor(0xff8800).withItalic(true);
		return MutableText.of(name.getContent()).setStyle(style);
	}

	static public Text	getStyledName(Entity entity){
		if (entity instanceof LivingEntity)
			return getStyledName((LivingEntity)entity);
		else if (entity != null)
			return entity.getDisplayName();
		else
			return null;
	}

	static public void initialize() {
		JsonConfig configFile = new JsonConfig(CONFIG_FILE, AllDeathMessages.MODID, AllDeathMessages.LOGGER);
		JsonElement json;
		try {
			json = configFile.GetOrCreateJsonFile();
		} catch (IOException e){
			AllDeathMessages.LOGGER.error("Unable to load config file: {}", CONFIG_FILE);
			return;
		}

		StyleParser.CreateConfigFromJson(json);
	}

}
