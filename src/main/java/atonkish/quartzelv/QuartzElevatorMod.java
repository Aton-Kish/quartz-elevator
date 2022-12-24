package atonkish.quartzelv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ModInitializer;

import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.config.QuartzElevatorConfig;
import atonkish.quartzelv.item.ModItemGroup;
import atonkish.quartzelv.item.ModItems;

public class QuartzElevatorMod implements ModInitializer {
	public static final String MOD_ID = "quartzelv";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static QuartzElevatorConfig CONFIG;

	@Override
	public void onInitialize() {
		// Blocks
		ModBlocks.init();

		// Items
		ModItemGroup.init();
		ModItems.init();

		// Auto Config
		AutoConfig.register(QuartzElevatorConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(QuartzElevatorConfig.class).getConfig();
	}
}
