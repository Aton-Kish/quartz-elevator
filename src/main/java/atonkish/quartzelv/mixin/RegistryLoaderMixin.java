package atonkish.quartzelv.mixin;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import atonkish.quartzelv.QuartzElevatorMod;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
  @Unique private static final AtomicBoolean LOADING_DYNAMIC_REGISTRIES = new AtomicBoolean(false);

  @Inject(
      method =
          "loadFromResource(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;",
      at = @At("HEAD"))
  private static void loadFromResources(
      ResourceManager resourceManager,
      List<RegistryWrapper.Impl<?>> registries,
      List<RegistryLoader.Entry<?>> entries,
      CallbackInfoReturnable<DynamicRegistryManager.Immutable> cir) {
    LOADING_DYNAMIC_REGISTRIES.set(
        entries.stream().anyMatch(entry -> entry.key() == RegistryKeys.TEST_INSTANCE));
  }

  @Inject(
      method =
          "load(Lnet/minecraft/registry/RegistryLoader$RegistryLoadable;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;",
      at =
          @At(
              value = "INVOKE",
              target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
              ordinal = 1))
  private static void beforeFreeze(
      @Coerce Object loadable,
      List<RegistryWrapper.Impl<?>> wrappers,
      List<RegistryLoader.Entry<?>> entries,
      CallbackInfoReturnable<DynamicRegistryManager.Immutable> cir,
      @Local(ordinal = 2) List<RegistryLoader.Loader<?>> registriesList) {
    if (LOADING_DYNAMIC_REGISTRIES.getAndSet(false)) {
      QuartzElevatorMod.registerDynamicEntries(registriesList);
    }
  }
}
