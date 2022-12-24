package atonkish.quartzelv.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.block.QuartzElevatorBlock;

public class Teleport {
    public static void teleportUp(World world, BlockPos blockPos, Box box, VerticalTeleporter verticalTeleporter) {
        Box relativeBox = Teleport.getRelativeBox(blockPos, box);

        Block srcBlock = world.getBlockState(blockPos.down()).getBlock();
        Identifier srcIdentifier = Registries.BLOCK.getId(srcBlock);

        if (srcBlock instanceof QuartzElevatorBlock
                && QuartzElevatorBlock.isTeleportable(world, blockPos, relativeBox)) {
            int bottomY = blockPos.down().getY();
            int maxDistance = srcIdentifier.equals(ModBlocks.QUARTZ_ELEVATOR_BLOCK_IDENTIFIER)
                    ? QuartzElevatorMod.CONFIG.quartzElevatorDistance
                    : QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance;

            for (; blockPos.getY() < bottomY + maxDistance; blockPos = blockPos.up()) {
                if (blockPos.getY() >= world.getTopY()) {
                    break;
                }

                Block dstBlock = world.getBlockState(blockPos.up()).getBlock();
                Identifier dstIdentifier = Registries.BLOCK.getId(dstBlock);

                BlockPos pos = blockPos.up(2);

                if (Teleport.equalsElevatorType(srcIdentifier, dstIdentifier)
                        && QuartzElevatorBlock.isTeleportable(world, pos, relativeBox)) {
                    verticalTeleporter.teleportY((double) pos.getY());
                    world.playSound((PlayerEntity) null, pos,
                            SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    public static void teleportDown(World world, BlockPos blockPos, Box box, VerticalTeleporter verticalTeleporter) {
        Box relativeBox = Teleport.getRelativeBox(blockPos, box);

        Block srcBlock = world.getBlockState(blockPos.down()).getBlock();
        Identifier srcIdentifier = Registries.BLOCK.getId(srcBlock);

        if (srcBlock instanceof QuartzElevatorBlock
                && QuartzElevatorBlock.isTeleportable(world, blockPos, relativeBox)) {
            blockPos = blockPos.down();

            int topY = blockPos.getY();
            int maxDistance = srcIdentifier.equals(ModBlocks.QUARTZ_ELEVATOR_BLOCK_IDENTIFIER)
                    ? QuartzElevatorMod.CONFIG.quartzElevatorDistance
                    : QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance;

            for (; blockPos.getY() > topY - maxDistance; blockPos = blockPos.down()) {
                if (blockPos.getY() <= world.getBottomY()) {
                    break;
                }

                Block dstBlock = world.getBlockState(blockPos.down()).getBlock();
                Identifier dstIdentifier = Registries.BLOCK.getId(dstBlock);

                BlockPos pos = blockPos;

                if (Teleport.equalsElevatorType(srcIdentifier, dstIdentifier)
                        && QuartzElevatorBlock.isTeleportable(world, pos, relativeBox)) {
                    verticalTeleporter.teleportY((double) pos.getY());
                    world.playSound((PlayerEntity) null, pos,
                            SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    private static Box getRelativeBox(BlockPos blockPos, Box entityBox) {
        int posX = blockPos.getX();
        int posZ = blockPos.getZ();

        double x1 = entityBox.minX - posX;
        double x2 = entityBox.maxX - posX;

        double y1 = 0;
        double y2 = entityBox.maxY - entityBox.minY;

        double z1 = entityBox.minZ - posZ;
        double z2 = entityBox.maxZ - posZ;

        return new Box(x1, y1, z1, x2, y2, z2);
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
