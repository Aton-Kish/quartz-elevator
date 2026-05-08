package atonkish.quartzelv.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.block.QuartzElevatorBlock;

public class Teleport {
  public static void teleportUp(
      Level world, BlockPos blockPos, AABB box, VerticalTeleporter verticalTeleporter) {
    AABB relativeBox = Teleport.getRelativeBox(blockPos, box);

    Block srcBlock = world.getBlockState(blockPos.below()).getBlock();
    Identifier srcIdentifier = BuiltInRegistries.BLOCK.getKey(srcBlock);

    if (srcBlock instanceof QuartzElevatorBlock
        && QuartzElevatorBlock.isTeleportable(world, blockPos, relativeBox)) {
      int bottomY = blockPos.below().getY();
      int maxDistance =
          srcIdentifier.equals(ModBlocks.QUARTZ_ELEVATOR_BLOCK_IDENTIFIER)
              ? QuartzElevatorMod.CONFIG.quartzElevatorDistance
              : QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance;

      for (; blockPos.getY() < bottomY + maxDistance; blockPos = blockPos.above()) {
        if (blockPos.getY() >= world.getMaxY()) {
          break;
        }

        Block dstBlock = world.getBlockState(blockPos.above()).getBlock();
        Identifier dstIdentifier = BuiltInRegistries.BLOCK.getKey(dstBlock);

        BlockPos pos = blockPos.above(2);

        if (Teleport.equalsElevatorType(srcIdentifier, dstIdentifier)
            && QuartzElevatorBlock.isTeleportable(world, pos, relativeBox)) {
          verticalTeleporter.teleportY((double) pos.getY());
          world.playSound(
              (Player) null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, 1.0F);
          break;
        }
      }
    }
  }

  public static void teleportDown(
      Level world, BlockPos blockPos, AABB box, VerticalTeleporter verticalTeleporter) {
    AABB relativeBox = Teleport.getRelativeBox(blockPos, box);

    Block srcBlock = world.getBlockState(blockPos.below()).getBlock();
    Identifier srcIdentifier = BuiltInRegistries.BLOCK.getKey(srcBlock);

    if (srcBlock instanceof QuartzElevatorBlock
        && QuartzElevatorBlock.isTeleportable(world, blockPos, relativeBox)) {
      blockPos = blockPos.below();

      int topY = blockPos.getY();
      int maxDistance =
          srcIdentifier.equals(ModBlocks.QUARTZ_ELEVATOR_BLOCK_IDENTIFIER)
              ? QuartzElevatorMod.CONFIG.quartzElevatorDistance
              : QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance;

      for (; blockPos.getY() > topY - maxDistance; blockPos = blockPos.below()) {
        if (blockPos.getY() <= world.getMinY()) {
          break;
        }

        Block dstBlock = world.getBlockState(blockPos.below()).getBlock();
        Identifier dstIdentifier = BuiltInRegistries.BLOCK.getKey(dstBlock);

        BlockPos pos = blockPos;

        if (Teleport.equalsElevatorType(srcIdentifier, dstIdentifier)
            && QuartzElevatorBlock.isTeleportable(world, pos, relativeBox)) {
          verticalTeleporter.teleportY((double) pos.getY());
          world.playSound(
              (Player) null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, 1.0F);
          break;
        }
      }
    }
  }

  private static AABB getRelativeBox(BlockPos blockPos, AABB entityBox) {
    int posX = blockPos.getX();
    int posZ = blockPos.getZ();

    double x1 = entityBox.minX - posX;
    double x2 = entityBox.maxX - posX;

    double y1 = 0;
    double y2 = entityBox.maxY - entityBox.minY;

    double z1 = entityBox.minZ - posZ;
    double z2 = entityBox.maxZ - posZ;

    return new AABB(x1, y1, z1, x2, y2, z2);
  }

  private static boolean equalsElevatorType(Identifier id1, Identifier id2) {
    if (!(Teleport.isQuartzElevatorBlock(id1) && Teleport.isQuartzElevatorBlock(id2))) {
      return false;
    }

    if (QuartzElevatorMod.CONFIG.mixTypes) {
      return true;
    }

    return id1.equals(id2);
  }

  private static boolean isQuartzElevatorBlock(Identifier id) {
    return id.equals(ModBlocks.QUARTZ_ELEVATOR_BLOCK_IDENTIFIER)
        || id.equals(ModBlocks.SMOOTH_QUARTZ_ELEVATOR_IDENTIFIER);
  }
}
