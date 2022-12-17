package atonkish.quartzelv.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.utils.MixinUtil;

@Mixin(MagmaCubeEntity.class)
public abstract class MagmaCubeEntityMixin extends Entity {
    public MagmaCubeEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "jump", cancellable = true)
    private void jump(CallbackInfo info) {
        if (!QuartzElevatorMod.CONFIG.isPlayerOnly) {
            // `isPlayerOnly`: false -> Magma Cube entities can also teleport
            MixinUtil.teleportUp(this.world, this.getBlockPos(), this.getBoundingBox(), (Double y) -> {
                if (this.world instanceof ServerWorld) {
                    this.refreshPositionAfterTeleport(this.getX(), y, this.getZ());
                } else {
                    this.teleport(this.getX(), y, this.getZ());
                }
                return (Void) null;
            });
        }
    }
}
