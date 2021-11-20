package atonkish.quartzelv.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.quartzelv.QuartzElevatorMod;

public class ModBlocks {
    public static final Block QUARTZ_ELEVATOR_BLOCK;
    public static final Block SMOOTH_QUARTZ_ELEVATOR;

    public static void init() {
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(QuartzElevatorMod.MOD_ID, id), block);
    }

    static {
        QUARTZ_ELEVATOR_BLOCK = register("quartz_elevator", new QuartzElevatorBlock(
                FabricBlockSettings.of(Material.STONE, MapColor.OFF_WHITE).requiresTool().strength(0.8f)));
        SMOOTH_QUARTZ_ELEVATOR = register("smooth_quartz_elevator", new QuartzElevatorBlock(
                FabricBlockSettings.of(Material.STONE, MapColor.OFF_WHITE).requiresTool().strength(2.0f, 6.0f)));
    }
}
