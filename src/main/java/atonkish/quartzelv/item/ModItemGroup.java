package atonkish.quartzelv.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;

public class ModItemGroup {
    public static final ItemGroup QUARTZ_ELEVATOR_ITEM_GROUP;

    public static void init() {
    }

    static {
        QUARTZ_ELEVATOR_ITEM_GROUP = FabricItemGroupBuilder
                .create(new Identifier(QuartzElevatorMod.MOD_ID, "quartz_elevator"))
                .icon(() -> new ItemStack(ModBlocks.QUARTZ_ELEVATOR_BLOCK)).appendItems(stacks -> {
                    stacks.add(new ItemStack(ModBlocks.QUARTZ_ELEVATOR_BLOCK));
                    stacks.add(new ItemStack(ModBlocks.SMOOTH_QUARTZ_ELEVATOR));
                }).build();
    }
}
