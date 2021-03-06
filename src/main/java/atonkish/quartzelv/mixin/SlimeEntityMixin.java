package atonkish.quartzelv.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.utils.MixinUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends Entity {
    public SlimeEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "jump", cancellable = true)
    private void jump(CallbackInfo info) {
        if (!QuartzElevatorMod.CONFIG.isPlayerOnly) {
            // `isPlayerOnly`: false -> Slime entities can also teleport
            MixinUtil.teleportUp(world, getBlockPos(), getBoundingBox(), (Double y) -> {
                teleport(getX(), y, getZ());
                return (Void) null;
            });
        }
    }
}
