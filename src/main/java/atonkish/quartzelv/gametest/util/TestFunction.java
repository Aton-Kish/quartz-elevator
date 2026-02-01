package atonkish.quartzelv.gametest.util;

import java.util.function.Consumer;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.FunctionTestInstance;
import net.minecraft.test.TestContext;
import net.minecraft.test.TestData;
import net.minecraft.test.TestEnvironmentDefinition;
import net.minecraft.test.TestInstance;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;

public record TestFunction(
    Identifier identifier,
    String environment,
    String structure,
    int maxTicks,
    int setupTicks,
    boolean required,
    BlockRotation rotation,
    boolean manualOnly,
    int maxAttempts,
    int requiredSuccesses,
    boolean skyAccess,
    Consumer<TestContext> testFunction) {
  public TestData<RegistryEntry<TestEnvironmentDefinition>> testData(
      Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry) {
    RegistryEntry<TestEnvironmentDefinition> testEnvironment =
        testEnvironmentDefinitionRegistry.getOrThrow(
            RegistryKey.of(RegistryKeys.TEST_ENVIRONMENT, Identifier.of(this.environment())));

    return new TestData<>(
        testEnvironment,
        Identifier.of(this.structure()),
        this.maxTicks(),
        this.setupTicks(),
        this.required(),
        this.rotation(),
        this.manualOnly(),
        this.maxAttempts(),
        this.requiredSuccesses(),
        this.skyAccess());
  }

  public TestInstance testInstance(
      Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry) {
    return new FunctionTestInstance(
        RegistryKey.of(RegistryKeys.TEST_FUNCTION, this.identifier()),
        this.testData(testEnvironmentDefinitionRegistry));
  }
}
