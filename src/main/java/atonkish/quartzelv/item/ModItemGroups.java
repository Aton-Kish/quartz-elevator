package atonkish.quartzelv.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> QUARTZ_ELEVATOR;

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, ModItemGroups.QUARTZ_ELEVATOR, FabricItemGroup
                .builder()
                .displayName(Text.translatable(String.format("itemGroup.%s.%s", QuartzElevatorMod.MOD_ID,
                        ModItemGroups.QUARTZ_ELEVATOR.getValue().getPath())))
                .icon(() -> new ItemStack(ModBlocks.QUARTZ_ELEVATOR_BLOCK))
                .build());
        ItemGroupEvents
                .modifyEntriesEvent(ModItemGroups.QUARTZ_ELEVATOR)
                .register(content -> {
                    content.add(ModBlocks.QUARTZ_ELEVATOR_BLOCK);
                    content.add(ModBlocks.SMOOTH_QUARTZ_ELEVATOR);
                });
    }

    private static RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(QuartzElevatorMod.MOD_ID, id));
    }

    static {
        QUARTZ_ELEVATOR = ModItemGroups.register("quartz_elevator");
    }
}
