package atonkish.quartzelv.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.util.Teleport;
import atonkish.quartzelv.util.VerticalTeleporter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

@Mixin(MagmaCube.class)
public abstract class MagmaCubeEntityMixin extends Entity {
  public MagmaCubeEntityMixin(EntityType<?> type, Level world) {
    super(type, world);
  }

  @Inject(at = @At("HEAD"), method = "jumpFromGround", cancellable = true)
  private void jump(CallbackInfo info) {
    if (!(this.level() instanceof ServerLevel)) {
      return;
    }

    // `isPlayerOnly`: false -> Magma Cube entities can also teleport
    if (QuartzElevatorMod.CONFIG.isPlayerOnly) {
      return;
    }

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
    Teleport.teleportUp(
        this.level(), this.blockPosition(), this.getBoundingBox(), verticalTeleporter);
  }
}
