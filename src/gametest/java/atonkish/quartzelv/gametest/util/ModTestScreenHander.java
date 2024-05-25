package atonkish.quartzelv.gametest.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class ModTestScreenHander extends ScreenHandler {
    public ModTestScreenHander() {
        super(null, 0);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        throw new UnsupportedOperationException("not implemented");
    };

    @Override
    public boolean canUse(PlayerEntity player) {
        throw new UnsupportedOperationException("not implemented");
    };
}
