package atonkish.quartzelv.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup QUARTZ_ELEVATOR;

    public static void init() {
        ItemGroupEvents
                .modifyEntriesEvent(QUARTZ_ELEVATOR)
                .register(content -> {
                    content.add(ModBlocks.QUARTZ_ELEVATOR_BLOCK);
                    content.add(ModBlocks.SMOOTH_QUARTZ_ELEVATOR);
                });
    }

    static {
        QUARTZ_ELEVATOR = FabricItemGroup
                .builder(new Identifier(QuartzElevatorMod.MOD_ID, "quartz_elevator"))
                .icon(() -> new ItemStack(ModBlocks.QUARTZ_ELEVATOR_BLOCK))
                .build();
    }
}
