package atonkish.quartzelv.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.util.Teleport;
import atonkish.quartzelv.util.VerticalTeleporter;

@Mixin(MagmaCubeEntity.class)
public abstract class MagmaCubeEntityMixin extends Entity {
    public MagmaCubeEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "jump", cancellable = true)
    private void jump(CallbackInfo info) {
        if (!(this.getEntityWorld() instanceof ServerWorld)) {
            return;
        }

        // `isPlayerOnly`: false -> Magma Cube entities can also teleport
        if (QuartzElevatorMod.CONFIG.isPlayerOnly) {
            return;
        }

        VerticalTeleporter verticalTeleporter = (Double y) -> {
            this.teleportTo(new TeleportTarget(
                    (ServerWorld) this.getEntityWorld(),
                    new Vec3d(this.getX(), y, this.getZ()),
                    Vec3d.ZERO,
                    this.getYaw(),
                    this.getPitch(),
                    TeleportTarget.NO_OP));
            return (Void) null;
        };
        Teleport.teleportUp(this.getEntityWorld(), this.getBlockPos(), this.getBoundingBox(), verticalTeleporter);
    }
}
