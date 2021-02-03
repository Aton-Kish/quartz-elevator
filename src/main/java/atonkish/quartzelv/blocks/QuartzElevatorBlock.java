package atonkish.quartzelv.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class QuartzElevatorBlock extends Block {
    public QuartzElevatorBlock(Settings settings) {
        super(settings);
    }

    public static boolean isTeleportable(World world, BlockPos blockPos, Box entityBox) {
        VoxelShape shape = world.getBlockState(blockPos).getCollisionShape(world, blockPos.up(2));
        return shape.isEmpty() || !entityBox.intersects(shape.getBoundingBox());
    }

    public static boolean isTeleportable(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getCollisionShape(world, blockPos.up(2)).isEmpty();
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos blockPos, Random random) {
        for (int i = 0; i < 3; ++i) {
            double d = (double) blockPos.getX() + 0.5D + (0.5D - random.nextDouble());
            double e = (double) blockPos.getY() + 0.5D + random.nextDouble();
            double f = (double) blockPos.getZ() + 0.5D + (0.5D - random.nextDouble());
            double g = 0.0D;
            double h = random.nextDouble() * 0.0625D;
            double l = 0.0D;
            world.addParticle(ParticleTypes.REVERSE_PORTAL, d, e, f, g, h, l);
        }
    }
}
