package atonkish.quartzelv.item;

import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import atonkish.quartzelv.block.ModBlocks;

public class ModItems {
  public static final Item QUARTZ_ELEVATOR_BLOCK;
  public static final Item SMOOTH_QUARTZ_ELEVATOR;

  public static void init() {}

  private static ResourceKey<Item> keyOf(ResourceKey<Block> blockKey) {
    return ResourceKey.create(Registries.ITEM, blockKey.identifier());
  }

  private static Item register(Block block) {
    return register(block, BlockItem::new);
  }

  private static Item register(Block block, BiFunction<Block, Item.Properties, Item> factory) {
    return register(block, factory, new Item.Properties());
  }

  private static Item register(
      Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties settings) {
    return register(
        keyOf(block.builtInRegistryHolder().key()),
        itemSettings -> (Item) factory.apply(block, itemSettings),
        settings.useBlockDescriptionPrefix());
  }

  private static Item register(
      ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties settings) {
    Item item = factory.apply(settings.setId(key));
    if (item instanceof BlockItem blockItem) {
      blockItem.registerBlocks(Item.BY_BLOCK, item);
    }

    return Registry.register(BuiltInRegistries.ITEM, key, item);
  }

  static {
    QUARTZ_ELEVATOR_BLOCK = register(ModBlocks.QUARTZ_ELEVATOR_BLOCK);
    SMOOTH_QUARTZ_ELEVATOR = register(ModBlocks.SMOOTH_QUARTZ_ELEVATOR);
  }
}
