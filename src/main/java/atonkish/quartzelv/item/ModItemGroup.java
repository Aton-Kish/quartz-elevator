package atonkish.quartzelv.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;

public class ModItemGroup {
    public static final ItemGroup QUARTZ_ELEVATOR_ITEM_GROUP;

    public static void init() {
        ItemGroupEvents
                .modifyEntriesEvent(QUARTZ_ELEVATOR_ITEM_GROUP)
                .register(content -> {
                    content.add(ModBlocks.QUARTZ_ELEVATOR_BLOCK);
                    content.add(ModBlocks.SMOOTH_QUARTZ_ELEVATOR);
                });
    }

    private static ItemGroup create(String id, ItemConvertible item) {
        return FabricItemGroup
                .builder(new Identifier(QuartzElevatorMod.MOD_ID, id))
                .icon(() -> new ItemStack(item))
                .build();
    }

    static {
        QUARTZ_ELEVATOR_ITEM_GROUP = ModItemGroup.create("quartz_elevator", ModBlocks.QUARTZ_ELEVATOR_BLOCK);
    }
}
