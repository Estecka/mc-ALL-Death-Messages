package tk.estecka.alldeath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;

import net.minecraft.entity.Entity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import tk.estecka.alldeath.config.JsonConfig;
import tk.estecka.alldeath.config.StyleParser;

public class DeathStyles
{
	static public class	MobStyle
	{
		public Predicate<Entity> predicate = e->false;
		@Nullable public TextColor color   = null;
		@Nullable public Boolean bold      = null;
		@Nullable public Boolean italic    = null;
		@Nullable public Boolean underline = null;
		@Nullable public Boolean strike    = null;
		@Nullable public Boolean cursed    = null;

		public boolean IsEmpty(){
			return color     == null
			    && bold      == null
			    && italic    == null
			    && underline == null
			    && strike    == null
			    && cursed    == null
			    ;
		}

		public MobStyle	MergeWith(MobStyle bottom)
		{
			if (this.color     == null) this.color     = bottom.color;
			if (this.bold      == null) this.bold      = bottom.bold;
			if (this.italic    == null) this.italic    = bottom.italic;
			if (this.underline == null) this.underline = bottom.underline;
			if (this.strike    == null) this.strike    = bottom.strike   ;
			if (this.cursed    == null) this.cursed    = bottom.cursed   ;
			return this;
		}
	}

	static public final String CONFIG_FILE = "alldeath-styles.json";
	static public final List<MobStyle> STYLES = new ArrayList<MobStyle>();

	static public Text	getStyledName(Entity entity)
	{
		MobStyle deathStyle = new MobStyle();
		for (var s : STYLES)
			if (s.predicate.test(entity))
				deathStyle.MergeWith(s);

		Text name = entity.getDisplayName();
		Style textStyle = name.getStyle();

		if (deathStyle.color     != null) textStyle=textStyle.withColor        (deathStyle.color    );
		if (deathStyle.bold      != null) textStyle=textStyle.withBold         (deathStyle.bold     );
		if (deathStyle.italic    != null) textStyle=textStyle.withItalic       (deathStyle.italic   );
		if (deathStyle.underline != null) textStyle=textStyle.withUnderline    (deathStyle.underline);
		if (deathStyle.strike    != null) textStyle=textStyle.withStrikethrough(deathStyle.strike   );
		if (deathStyle.cursed    != null) textStyle=textStyle.withObfuscated   (deathStyle.cursed   );

		return MutableText.of(name.getContent()).setStyle(textStyle);
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

		STYLES.addAll(StyleParser.CreateConfigFromJson(json));
	}

}
