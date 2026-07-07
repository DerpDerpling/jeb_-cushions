package derp.jebcushions;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JebCushions implements ModInitializer {
	public static final String MOD_ID = "jeb-cushions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loaded jeb_ cushions");
	}
}
