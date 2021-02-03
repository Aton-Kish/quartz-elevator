package atonkish.quartzelv.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.quartzelv.utils.MixinUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World world;
    @Shadow
    private Vec3d pos;

    @Shadow
    public abstract Box getBoundingBox();

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract void teleport(double dstX, double dstY, double dstZ);

    @Inject(at = @At("HEAD"), method = "setSneaking", cancellable = true)
    private void setSneaking(boolean sneaking, CallbackInfo info) {
        if (sneaking) {
            MixinUtil.teleportDown(world, getBlockPos(), getBoundingBox(), (Double y) -> {
                teleport(pos.x, y, pos.z);
                return (Void) null;
            });
        }
    }
}
