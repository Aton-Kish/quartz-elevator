package atonkish.quartzelv.block;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import atonkish.quartzelv.QuartzElevatorMod;

public class ModBlocks {
  public static final Identifier QUARTZ_ELEVATOR_BLOCK_IDENTIFIER;
  public static final Identifier SMOOTH_QUARTZ_ELEVATOR_IDENTIFIER;

  public static final Block QUARTZ_ELEVATOR_BLOCK;
  public static final Block SMOOTH_QUARTZ_ELEVATOR;

  public static void init() {}

  private static Block register(
      ResourceKey<Block> key,
      Function<BlockBehaviour.Properties, Block> factory,
      BlockBehaviour.Properties settings) {
    Block block = factory.apply(settings.setId(key));
    return Registry.register(BuiltInRegistries.BLOCK, key, block);
  }

  private static Block register(
      Identifier id,
      Function<BlockBehaviour.Properties, Block> factory,
      BlockBehaviour.Properties settings) {
    return register(keyOf(id), factory, settings);
  }

  private static ResourceKey<Block> keyOf(Identifier id) {
    return ResourceKey.create(Registries.BLOCK, id);
  }

  static {
    QUARTZ_ELEVATOR_BLOCK_IDENTIFIER =
        Identifier.fromNamespaceAndPath(QuartzElevatorMod.MOD_ID, "quartz_elevator");
    SMOOTH_QUARTZ_ELEVATOR_IDENTIFIER =
        Identifier.fromNamespaceAndPath(QuartzElevatorMod.MOD_ID, "smooth_quartz_elevator");

    QUARTZ_ELEVATOR_BLOCK =
        register(
            QUARTZ_ELEVATOR_BLOCK_IDENTIFIER,
            QuartzElevatorBlock::new,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.QUARTZ)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(0.8f));
    SMOOTH_QUARTZ_ELEVATOR =
        register(
            SMOOTH_QUARTZ_ELEVATOR_IDENTIFIER,
            QuartzElevatorBlock::new,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.QUARTZ)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(2.0f, 6.0f));
  }
}
