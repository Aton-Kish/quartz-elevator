package atonkish.quartzelv.block;

import atonkish.quartzelv.QuartzElevatorMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class QuartzElevatorBlock extends Block {
  public QuartzElevatorBlock(Properties settings) {
    super(settings);
  }

  public static boolean isTeleportable(Level world, BlockPos blockPos, AABB entityBox) {
    VoxelShape shape = world.getBlockState(blockPos).getCollisionShape(world, blockPos.above(2));
    return shape.isEmpty() || !entityBox.intersects(shape.bounds());
  }

  @Override
  public void animateTick(BlockState state, Level world, BlockPos blockPos, RandomSource random) {
    if (!QuartzElevatorMod.CONFIG.displayParticles) {
      return;
    }

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
