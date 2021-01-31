package atonkish.quartzelv.registry;

import java.util.ArrayList;
import java.util.List;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.blocks.QuartzElevatorBlock;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class QuartzElevatorRegistry {
    /*
     * Item Group Builder
     */
    private static FabricItemGroupBuilder quartzElevatorItemGroupBuilder = FabricItemGroupBuilder
            .create(new Identifier(QuartzElevatorMod.MOD_ID, "item_group"));
    private static List<ItemStack> quartzElevatorStacks = new ArrayList<ItemStack>();

    /*
     * Quartz Elevator
     */
    // Normal Quartz
    public static final QuartzElevatorBlock QUARTZ_ELEVATOR_BLOCK = registerQuartzElevator(createQuartzElevatorBlock(),
            "quartz_elevator");

    // Smooth Quartz
    public static final QuartzElevatorBlock SMOOTH_QUARTZ_ELEVATOR_BLOCK = registerQuartzElevator(
            createQuartzElevatorBlock(), "smooth_quartz_elevator");

    /*
     * Finalized Item Group
     */
    public static final ItemGroup QUARTZ_ELEVATOR_ITEM_GROUP = quartzElevatorItemGroupBuilder
            .icon(() -> new ItemStack(QUARTZ_ELEVATOR_BLOCK)).appendItems(stacks -> {
                for (ItemStack item : quartzElevatorStacks) {
                    stacks.add(item);
                }
            }).build();

    /*
     * Functions
     */
    public static void init() {
    }

    public static QuartzElevatorBlock createQuartzElevatorBlock() {
        return new QuartzElevatorBlock(FabricBlockSettings.copy(Blocks.QUARTZ_BLOCK));
    }

    public static QuartzElevatorBlock registerQuartzElevator(QuartzElevatorBlock block, String id,
            ItemGroup itemGroup) {
        Registry.register(Registry.BLOCK, new Identifier(QuartzElevatorMod.MOD_ID, id), block);
        Registry.register(Registry.ITEM, new Identifier(QuartzElevatorMod.MOD_ID, id),
                new BlockItem(block, new Item.Settings().group(itemGroup)));
        quartzElevatorStacks.add(new ItemStack(block));
        return block;
    }

    public static QuartzElevatorBlock registerQuartzElevator(QuartzElevatorBlock block, String id) {
        return registerQuartzElevator(block, id, ItemGroup.DECORATIONS);
    }
}
