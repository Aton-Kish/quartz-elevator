package atonkish.quartzelv;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
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
    for (TestFunction testFunction : QuartzElevatorModGameTest.TEST_FUNCTIONS) {
      LOGGER.debug("Registering test function: {}", testFunction.identifier());
      Registry.register(
          BuiltInRegistries.TEST_FUNCTION, testFunction.identifier(), testFunction.testFunction());
    }
  }

  public static void registerDynamicEntries(List<RegistryDataLoader.Loader<?>> registriesList) {
    Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries =
        new IdentityHashMap<>(registriesList.size());

    for (RegistryDataLoader.Loader<?> entry : registriesList) {
      registries.put(entry.registry().key(), entry.registry());
    }

    Registry<GameTestInstance> testInstances =
        (Registry<GameTestInstance>) registries.get(Registries.TEST_INSTANCE);
    Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry =
        (Registry<TestEnvironmentDefinition>)
            Objects.requireNonNull(registries.get(Registries.TEST_ENVIRONMENT));

    for (TestFunction testFunction : QuartzElevatorModGameTest.TEST_FUNCTIONS) {
      GameTestInstance testInstance = testFunction.testInstance(testEnvironmentDefinitionRegistry);
      Registry.register(testInstances, testFunction.identifier(), testInstance);
    }
  }
}
