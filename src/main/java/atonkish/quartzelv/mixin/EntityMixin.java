package atonkish.quartzelv.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.util.Teleport;
import atonkish.quartzelv.util.VerticalTeleporter;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract void refreshPositionAfterTeleport(double x, double y, double z);

    @Shadow
    public abstract void teleport(double destX, double destY, double destZ);

    @Shadow
    public abstract Box getBoundingBox();

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract World getWorld();

    @Inject(at = @At("HEAD"), method = "setSneaking", cancellable = true)
    private void setSneaking(boolean sneaking, CallbackInfo info) {
        if (!(this.getWorld() instanceof ServerWorld)) {
            return;
        }

        // `isPlayerOnly`: false -> all entities can teleport
        // `isPlayerOnly`: true -> only player entities can teleport
        if (QuartzElevatorMod.CONFIG.isPlayerOnly && !this.getClass().equals(ServerPlayerEntity.class)) {
            return;
        }

        if (sneaking) {
            VerticalTeleporter verticalTeleporter = (Double y) -> {
                this.refreshPositionAfterTeleport(this.getX(), y, this.getZ());
                this.teleport(this.getX(), y, this.getZ());
                return (Void) null;
            };
            Teleport.teleportDown(this.getWorld(), this.getBlockPos(), this.getBoundingBox(), verticalTeleporter);
        }
    }
}
