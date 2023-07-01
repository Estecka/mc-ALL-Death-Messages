package tk.estecka.alldeath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

public class AllDeathMessages implements ModInitializer
{
	static public final String MODID = "alldeathmessages";
	static public final Logger LOGGER = LoggerFactory.getLogger("AllDeath");

	@Override
	public void	onInitialize(){
		DeathRules.initialize();
		DeathStyles.initialize();
	}
}
