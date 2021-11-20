package atonkish.quartzelv.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.quartzelv.block.ModBlocks;

public class ModItems {
    public static final Item QUARTZ_ELEVATOR_BLOCK;
    public static final Item SMOOTH_QUARTZ_ELEVATOR;

    public static void init() {
    }

    private static Item register(Block block, ItemGroup group) {
        return register(new BlockItem(block, new Item.Settings().group(group)));
    }

    private static Item register(BlockItem item) {
        return register(item.getBlock(), (Item) item);
    }

    protected static Item register(Block block, Item item) {
        return register(Registry.BLOCK.getId(block), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registry.ITEM, id, item);
    }

    static {
        QUARTZ_ELEVATOR_BLOCK = register(ModBlocks.QUARTZ_ELEVATOR_BLOCK, ItemGroup.DECORATIONS);
        SMOOTH_QUARTZ_ELEVATOR = register(ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ItemGroup.DECORATIONS);
    }
}
