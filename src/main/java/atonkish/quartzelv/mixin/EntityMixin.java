package atonkish.quartzelv.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.util.Teleport;
import atonkish.quartzelv.util.VerticalTeleporter;

@Mixin(Entity.class)
public abstract class EntityMixin {
  @Shadow
  public abstract Entity teleport(TeleportTransition teleportTarget);

  @Shadow
  public abstract AABB getBoundingBox();

  @Shadow
  public abstract BlockPos blockPosition();

  @Shadow
  public abstract double getX();

  @Shadow
  public abstract double getZ();

  // CHECKSTYLE.SUPPRESS: AbbreviationAsWordInName for +2 lines
  @Shadow
  public abstract float getYRot();

  // CHECKSTYLE.SUPPRESS: AbbreviationAsWordInName for +2 lines
  @Shadow
  public abstract float getXRot();

  @Shadow
  public abstract Level level();

  @Inject(at = @At("HEAD"), method = "setShiftKeyDown", cancellable = true)
  private void setSneaking(boolean sneaking, CallbackInfo info) {
    if (!(this.level() instanceof ServerLevel)) {
      return;
    }

    // `isPlayerOnly`: false -> all entities can teleport
    // `isPlayerOnly`: true -> only player entities can teleport
    if (QuartzElevatorMod.CONFIG.isPlayerOnly && !this.getClass().equals(ServerPlayer.class)) {
      return;
    }

    if (sneaking) {
      VerticalTeleporter verticalTeleporter =
          (Double y) -> {
            this.teleport(
                new TeleportTransition(
                    (ServerLevel) this.level(),
                    new Vec3(this.getX(), y, this.getZ()),
                    Vec3.ZERO,
                    this.getYRot(),
                    this.getXRot(),
                    TeleportTransition.DO_NOTHING));
            return (Void) null;
          };
      Teleport.teleportDown(
          this.level(), this.blockPosition(), this.getBoundingBox(), verticalTeleporter);
    }
  }
}
