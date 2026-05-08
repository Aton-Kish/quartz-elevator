package atonkish.quartzelv.gametest.util;

import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Rotation;

public record TestFunction(
    Identifier identifier,
    String environment,
    String structure,
    int maxTicks,
    int setupTicks,
    boolean required,
    Rotation rotation,
    boolean manualOnly,
    int maxAttempts,
    int requiredSuccesses,
    boolean skyAccess,
    Consumer<GameTestHelper> testFunction) {
  public TestData<Holder<TestEnvironmentDefinition>> testData(
      Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry) {
    Holder<TestEnvironmentDefinition> testEnvironment =
        testEnvironmentDefinitionRegistry.getOrThrow(
            ResourceKey.create(Registries.TEST_ENVIRONMENT, Identifier.parse(this.environment())));

    return new TestData<>(
        testEnvironment,
        Identifier.parse(this.structure()),
        this.maxTicks(),
        this.setupTicks(),
        this.required(),
        this.rotation(),
        this.manualOnly(),
        this.maxAttempts(),
        this.requiredSuccesses(),
        this.skyAccess());
  }

  public GameTestInstance testInstance(
      Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry) {
    return new FunctionGameTestInstance(
        ResourceKey.create(Registries.TEST_FUNCTION, this.identifier()),
        this.testData(testEnvironmentDefinitionRegistry));
  }
}
