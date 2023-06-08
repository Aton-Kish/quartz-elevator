package atonkish.quartzelv.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import atonkish.quartzelv.QuartzElevatorMod;

public class ModBlocks {
    public static final Identifier QUARTZ_ELEVATOR_BLOCK_IDENTIFIER;
    public static final Identifier SMOOTH_QUARTZ_ELEVATOR_IDENTIFIER;

    public static final Block QUARTZ_ELEVATOR_BLOCK;
    public static final Block SMOOTH_QUARTZ_ELEVATOR;

    public static void init() {
    }

    static {
        QUARTZ_ELEVATOR_BLOCK_IDENTIFIER = new Identifier(QuartzElevatorMod.MOD_ID, "quartz_elevator");
        SMOOTH_QUARTZ_ELEVATOR_IDENTIFIER = new Identifier(QuartzElevatorMod.MOD_ID, "smooth_quartz_elevator");

        QUARTZ_ELEVATOR_BLOCK = Registry.register(
                Registries.BLOCK, QUARTZ_ELEVATOR_BLOCK_IDENTIFIER,
                new QuartzElevatorBlock(FabricBlockSettings
                        .of(Material.STONE, MapColor.OFF_WHITE)
                        .requiresTool()
                        .strength(0.8f)));
        SMOOTH_QUARTZ_ELEVATOR = Registry.register(
                Registries.BLOCK, SMOOTH_QUARTZ_ELEVATOR_IDENTIFIER,
                new QuartzElevatorBlock(FabricBlockSettings
                        .of(Material.STONE, MapColor.OFF_WHITE)
                        .requiresTool()
                        .strength(2.0f, 6.0f)));
    }
}
