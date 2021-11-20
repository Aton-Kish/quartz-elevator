package atonkish.quartzelv.utils;

import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.QuartzElevatorBlock;

public final class MixinUtil {
    @FunctionalInterface
    public static interface Funciton1 {
        Void teleportY(Double y);
    }

    public static void teleportUp(World world, BlockPos blockPos, Box entityBox, Funciton1 func) {
        Box relEntityBox = getRelEntityBox(blockPos, entityBox);
        Block block = world.getBlockState(blockPos.down()).getBlock();

        if (block instanceof QuartzElevatorBlock && QuartzElevatorBlock.isTeleportable(world, blockPos, relEntityBox)) {
            String blockKey = extractBlockKey(block);
            int maxDistance = isSmooth(blockKey) ? QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance
                    : QuartzElevatorMod.CONFIG.quartzElevatorDistance;
            int bottomY = blockPos.down().getY();
            for (; blockPos.getY() < bottomY + maxDistance; blockPos = blockPos.up()) {
                if (blockPos.getY() >= world.getTopY()) {
                    break;
                }

                Block dstBlock = world.getBlockState(blockPos.up()).getBlock();
                String dstBlockKey = extractBlockKey(dstBlock);
                if (isEqualElevatorTypes(blockKey, dstBlockKey)
                        && QuartzElevatorBlock.isTeleportable(world, blockPos.up(2), relEntityBox)) {
                    func.teleportY((double) blockPos.up(2).getY());
                    world.playSound((PlayerEntity) null, blockPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    public static void teleportDown(World world, BlockPos blockPos, Box entityBox, Funciton1 func) {
        Box relEntityBox = getRelEntityBox(blockPos, entityBox);
        Block block = world.getBlockState(blockPos.down()).getBlock();

        if (block instanceof QuartzElevatorBlock && QuartzElevatorBlock.isTeleportable(world, blockPos, relEntityBox)) {
            blockPos = blockPos.down();

            String blockKey = extractBlockKey(block);
            int maxDistance = isSmooth(blockKey) ? QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance
                    : QuartzElevatorMod.CONFIG.quartzElevatorDistance;
            int topY = blockPos.getY();
            for (; blockPos.getY() > topY - maxDistance; blockPos = blockPos.down()) {
                if (blockPos.getY() <= world.getBottomY()) {
                    break;
                }

                Block dstBlock = world.getBlockState(blockPos.down()).getBlock();
                String dstBlockKey = extractBlockKey(dstBlock);
                if (isEqualElevatorTypes(blockKey, dstBlockKey)
                        && QuartzElevatorBlock.isTeleportable(world, blockPos, relEntityBox)) {
                    func.teleportY((double) blockPos.getY());
                    world.playSound((PlayerEntity) null, blockPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    public static Box getRelEntityBox(BlockPos blockPos, Box entityBox) {
        int blockPosX = blockPos.getX();
        int blockPosZ = blockPos.getZ();
        return new Box(entityBox.minX - blockPosX, 0, entityBox.minZ - blockPosZ, entityBox.maxX - blockPosX,
                entityBox.maxY - entityBox.minY, entityBox.maxZ - blockPosZ);
    }

    public static String extractBlockKey(Block block) {
        String translationKey = block.getTranslationKey();
        String[] keyArr = translationKey.split(Pattern.quote("."));
        return keyArr[keyArr.length - 1];
    }

    public static boolean isSmooth(String blockKey) {
        return blockKey.startsWith("smooth");
    }

    private static boolean isEqualElevatorTypes(String srcBlockKey, String dstBlockKey) {
        if (QuartzElevatorMod.CONFIG.mixTypes) {
            srcBlockKey = srcBlockKey.replaceFirst("smooth_", "");
            dstBlockKey = dstBlockKey.replaceFirst("smooth_", "");
        }
        return srcBlockKey.equals(dstBlockKey);
    }
}
