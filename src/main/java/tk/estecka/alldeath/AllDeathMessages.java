package tk.estecka.alldeath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class AllDeathMessages
implements ModInitializer
{
	static public final String MODID = "alldeathmessages";
	static public final Logger LOGGER = LoggerFactory.getLogger("AllDeath");

	static public final GameRule<Boolean> COORD_RULE = GameRuleBuilder.forBoolean(false)
		.category(GameRuleCategory.CHAT)
		.buildAndRegister(Identifier.fromNamespaceAndPath(MODID, "show_death_coordinates"));
		;

	@Override
	public void	onInitialize(){
		DeathRules.initialize();
		DeathStyles.initialize();
		Commands.Register();
	}

	static public MutableComponent ServersideTranslatable(String key, Object ... args){
		String fallback = Language.getInstance().getOrDefault(key);
		return Component.translatableWithFallback(key, fallback, args);
	}
}
