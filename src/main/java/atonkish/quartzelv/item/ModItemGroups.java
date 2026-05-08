package atonkish.quartzelv.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;

public class ModItemGroups {
  public static final ResourceKey<CreativeModeTab> QUARTZ_ELEVATOR;

  public static void init() {
    Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ModItemGroups.QUARTZ_ELEVATOR,
        FabricCreativeModeTab.builder()
            .title(
                Component.translatable(
                    String.format(
                        "itemGroup.%s.%s",
                        QuartzElevatorMod.MOD_ID,
                        ModItemGroups.QUARTZ_ELEVATOR.identifier().getPath())))
            .icon(() -> new ItemStack(ModBlocks.QUARTZ_ELEVATOR_BLOCK))
            .build());

    CreativeModeTabEvents.modifyOutputEvent(ModItemGroups.QUARTZ_ELEVATOR)
        .register(
            creativeTab -> {
              creativeTab.accept(ModBlocks.QUARTZ_ELEVATOR_BLOCK);
              creativeTab.accept(ModBlocks.SMOOTH_QUARTZ_ELEVATOR);
            });
  }

  private static ResourceKey<CreativeModeTab> register(String id) {
    return ResourceKey.create(
        Registries.CREATIVE_MODE_TAB,
        Identifier.fromNamespaceAndPath(QuartzElevatorMod.MOD_ID, id));
  }

  static {
    QUARTZ_ELEVATOR = ModItemGroups.register("quartz_elevator");
  }
}
