package atonkish.quartzelv.item;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import atonkish.quartzelv.block.ModBlocks;

public class ModItems {
    public static final Item QUARTZ_ELEVATOR_BLOCK;
    public static final Item SMOOTH_QUARTZ_ELEVATOR;

    public static void init() {
    }

    private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }

    private static Item register(Block block) {
        return register(block, BlockItem::new);
    }

    private static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory) {
        return register(block, factory, new Item.Settings());
    }

    private static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        return register(
                keyOf(block.getRegistryEntry().registryKey()),
                itemSettings -> (Item) factory.apply(block, itemSettings), settings.useBlockPrefixedTranslationKey());
    }

    private static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }

    static {
        QUARTZ_ELEVATOR_BLOCK = register(ModBlocks.QUARTZ_ELEVATOR_BLOCK);
        SMOOTH_QUARTZ_ELEVATOR = register(ModBlocks.SMOOTH_QUARTZ_ELEVATOR);
    }
}
