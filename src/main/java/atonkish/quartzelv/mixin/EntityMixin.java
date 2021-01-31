package atonkish.quartzelv.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.blocks.QuartzElevatorBlock;
import atonkish.quartzelv.utils.MixinUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World world;
    @Shadow
    private Vec3d pos;

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract void teleport(double dstX, double dstY, double dstZ);

    @Inject(at = @At("HEAD"), method = "setSneaking", cancellable = true)
    private void setSneaking(boolean sneaking, CallbackInfo info) {
        if (sneaking) {
            BlockPos blockPos = getBlockPos();
            Block block = world.getBlockState(blockPos.down()).getBlock();

            if (block instanceof QuartzElevatorBlock) {
                blockPos = blockPos.down();

                String blockKey = MixinUtil.extractBlockKey(block);
                int maxDistance = MixinUtil.isSmooth(blockKey) ? QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance
                        : QuartzElevatorMod.CONFIG.quartzElevatorDistance;
                int topY = blockPos.getY();
                for (; blockPos.getY() > topY - maxDistance; blockPos = blockPos.down()) {
                    if (blockPos.getY() <= 0) {
                        break;
                    } else if ((world.getBlockState(blockPos.down()).getBlock().equals(block))
                            && QuartzElevatorBlock.isTeleportable(world, blockPos)) {
                        world.playSound((PlayerEntity) null, blockPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                                SoundCategory.BLOCKS, 1.0F, 1.0F);
                        teleport(this.pos.x, blockPos.getY(), this.pos.z);
                        break;
                    }
                }
            }
        }
    }
}
