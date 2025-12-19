package tk.estecka.alldeath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleCategory;

public class AllDeathMessages
implements ModInitializer
{
	static public final String MODID = "alldeathmessages";
	static public final Logger LOGGER = LoggerFactory.getLogger("AllDeath");

	static public final GameRule<Boolean> COORD_RULE = GameRuleBuilder.forBoolean(false)
		.category(GameRuleCategory.CHAT)
		.buildAndRegister(Identifier.of(MODID, "show_death_coordinates"));
		;

	@Override
	public void	onInitialize(){
		DeathRules.initialize();
		DeathStyles.initialize();
		Commands.Register();
	}

	static public MutableText ServersideTranslatable(String key, Object ... args){
		String fallback = Language.getInstance().get(key);
		return Text.translatableWithFallback(key, fallback, args);
	}
}
