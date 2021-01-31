package atonkish.quartzelv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atonkish.quartzelv.registry.QuartzElevatorRegistry;
import net.fabricmc.api.ModInitializer;

public class QuartzElevatorMod implements ModInitializer {
	public static final String MOD_ID = "quartzelv";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		QuartzElevatorRegistry.init();
	}
}
