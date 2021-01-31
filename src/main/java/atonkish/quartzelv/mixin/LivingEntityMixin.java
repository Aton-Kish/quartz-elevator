package atonkish.quartzelv.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.blocks.QuartzElevatorBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "jump", cancellable = true)
    private void jump(CallbackInfo info) {
        BlockPos blockPos = getBlockPos();
        Block block = world.getBlockState(blockPos.down()).getBlock();

        if (block instanceof QuartzElevatorBlock) {
            int bottomY = blockPos.down().getY();
            for (; blockPos.getY() < bottomY + QuartzElevatorMod.CONFIG.quartzElevatorDistance; blockPos = blockPos
                    .up()) {
                if (blockPos.getY() >= world.getHeight()) {
                    break;
                } else if ((world.getBlockState(blockPos.up()).getBlock().equals(block))
                        && QuartzElevatorBlock.isTeleportable(world, blockPos.up(2))) {
                    world.playSound((PlayerEntity) null, blockPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    teleport(getX(), blockPos.up(2).getY(), getZ());
                    break;
                }
            }
        }
    }
}
