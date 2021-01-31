package atonkish.quartzelv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atonkish.quartzelv.config.QuartzElevatorConfig;
import atonkish.quartzelv.registry.QuartzElevatorRegistry;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class QuartzElevatorMod implements ModInitializer {
	public static final String MOD_ID = "quartzelv";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static QuartzElevatorConfig CONFIG;

	@Override
	public void onInitialize() {
		QuartzElevatorRegistry.init();

		// Auto Config
		AutoConfig.register(QuartzElevatorConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(QuartzElevatorConfig.class).getConfig();
	}
}
