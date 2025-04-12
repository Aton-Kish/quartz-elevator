package atonkish.quartzelv;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.test.TestEnvironmentDefinition;
import net.minecraft.test.TestInstance;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.gametest.FabricGameTestRunner;
import net.fabricmc.loader.api.FabricLoader;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.QuartzElevatorModGameTest;
import atonkish.quartzelv.gametest.util.TestFunction;
import atonkish.quartzelv.item.ModItemGroups;
import atonkish.quartzelv.item.ModItems;

public class QuartzElevatorMod implements ModInitializer {
	public static final String MOD_ID = "quartzelv";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static QuartzElevatorConfig CONFIG;

	@Override
	public void onInitialize() {
		// Blocks
		ModBlocks.init();

		// Items
		ModItemGroups.init();
		ModItems.init();

		// Auto Config
		AutoConfig.register(QuartzElevatorConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(QuartzElevatorConfig.class).getConfig();

		// Game Tests
		this.onInitializeGameTest();
	}

	private void onInitializeGameTest() {
		if (!(FabricGameTestRunner.ENABLED || FabricLoader.getInstance().isDevelopmentEnvironment())) {
			return;
		}

		for (TestFunction testFunction : QuartzElevatorModGameTest.TEST_FUNCTIONS) {
			LOGGER.debug("Registering test function: {}", testFunction.identifier());
			Registry.register(Registries.TEST_FUNCTION, testFunction.identifier(), testFunction.testFunction());
		}
	}

	public static void registerDynamicEntries(List<RegistryLoader.Loader<?>> registriesList) {
		Map<RegistryKey<? extends Registry<?>>, Registry<?>> registries = new IdentityHashMap<>(registriesList.size());

		for (RegistryLoader.Loader<?> entry : registriesList) {
			registries.put(entry.registry().getKey(), entry.registry());
		}

		Registry<TestInstance> testInstances = (Registry<TestInstance>) registries.get(RegistryKeys.TEST_INSTANCE);
		Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry = (Registry<TestEnvironmentDefinition>) Objects
				.requireNonNull(registries.get(RegistryKeys.TEST_ENVIRONMENT));

		for (TestFunction testFunction : QuartzElevatorModGameTest.TEST_FUNCTIONS) {
			TestInstance testInstance = testFunction.testInstance(testEnvironmentDefinitionRegistry);
			Registry.register(testInstances, testFunction.identifier(), testInstance);
		}
	}
}
