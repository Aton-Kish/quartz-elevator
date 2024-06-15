package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.util.MockServerPlayerHelper;

public class TeleportTests {
    public static final String BATCH_ID_DEFAULT = QuartzElevatorMod.MOD_ID + ":TeleportBatch";
    public static final String BATCH_ID_WITH_MIX_TYPES = QuartzElevatorMod.MOD_ID + ":TeleportWithMixTypesBatch";
    public static final String BATCH_ID_WITH_PLAYER_ONLY = QuartzElevatorMod.MOD_ID + ":TeleportWithPlayerOnlyBatch";

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            //
            // Default
            //

            // Player: Quartz Elevator -> Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player teleport up from Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    true));
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player does not teleport up from Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    17,
                    false));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player teleport down from Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    true));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player does not teleport down from Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    17,
                    false));

            // Player: Smooth Quartz Elevator -> Smooth Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    true));
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player does not teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    65,
                    false));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player teleport down from Smooth Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    true));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player does not teleport down from Smooth Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    65,
                    false));

            // Player: Quartz Elevator -> Smooth Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player does not teleport up from Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    16,
                    false));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player does not teleport down from Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    16,
                    false));

            // Player: Smooth Quartz Elevator -> Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player does not teleport up from Smooth Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    64,
                    false));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player does not teleport down from Smooth Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    64,
                    false));

            // Zombie
            add(TeleportTests.createTestMobTeleportUp(
                    "Zombie teleport up from Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    EntityType.ZOMBIE,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    true));
            add(TeleportTests.createTestMobTeleportUp(
                    "Zombie teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    EntityType.ZOMBIE,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    true));

            // Slime
            add(TeleportTests.createTestMobTeleportUp(
                    "Slime teleport up from Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    EntityType.SLIME,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    true));
            add(TeleportTests.createTestMobTeleportUp(
                    "Slime teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    EntityType.SLIME,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    true));

            // Magma Cube
            add(TeleportTests.createTestMobTeleportUp(
                    "Magma Cube teleport up from Quartz Elevator to Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    EntityType.MAGMA_CUBE,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    true));
            add(TeleportTests.createTestMobTeleportUp(
                    "Magma Cube teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                    TeleportTests.BATCH_ID_DEFAULT,
                    EntityType.MAGMA_CUBE,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    true));

            //
            // With Mix Types
            //

            // Player: Quartz Elevator -> Smooth Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player teleport up from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    16,
                    true));
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player does not teleport up from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    17,
                    false));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player teleport down from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    16,
                    true));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player does not teleport down from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    17,
                    false));

            // Player: Smooth Quartz Elevator -> Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player teleport up from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    64,
                    true));
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player does not teleport up from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    65,
                    false));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player teleport down from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    64,
                    true));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player does not teleport down from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                    TeleportTests.BATCH_ID_WITH_MIX_TYPES,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    65,
                    false));

            //
            // With Player Only
            //

            // Player: Quartz Elevator -> Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    true));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player teleport down from Quartz Elevator to Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    true));

            // Player: Smooth Quartz Elevator -> Smooth Quartz Elevator
            add(TeleportTests.createTestPlayerTeleportUp(
                    "Player teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    true));
            add(TeleportTests.createTestPlayerTeleportDown(
                    "Player teleport down from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    true));

            // Zombie
            add(TeleportTests.createTestMobTeleportUp(
                    "Zombie teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    EntityType.ZOMBIE,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    false));
            add(TeleportTests.createTestMobTeleportUp(
                    "Zombie teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    EntityType.ZOMBIE,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    false));

            // Slime
            add(TeleportTests.createTestMobTeleportUp(
                    "Slime teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    EntityType.SLIME,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    false));
            add(TeleportTests.createTestMobTeleportUp(
                    "Slime teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    EntityType.SLIME,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    false));

            // Magma Cube
            add(TeleportTests.createTestMobTeleportUp(
                    "Magma Cube teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    EntityType.MAGMA_CUBE,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    16,
                    false));
            add(TeleportTests.createTestMobTeleportUp(
                    "Magma Cube teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                    TeleportTests.BATCH_ID_WITH_PLAYER_ONLY,
                    EntityType.MAGMA_CUBE,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    64,
                    false));
        }
    };

    private static TestFunction createTestPlayerTeleportUp(String name, String batchId,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                TeleportTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                batchId,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                100,
                0L,
                true,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos1 = BlockPos.ORIGIN;
                    BlockPos blockPos2 = BlockPos.ORIGIN.up(distance);

                    context.setBlockState(blockPos1, elevatorBlock1);
                    context.setBlockState(blockPos2, elevatorBlock2);

                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL, Vec3d.of(blockPos1.up(1)));

                    // Act
                    player.jump();

                    // Assert
                    try {
                        context.expectEntityAt(player, (shouldTeleport ? blockPos2 : blockPos1).up(1));
                    } catch (Exception e) {
                        QuartzElevatorMod.LOGGER.error(e.getMessage());
                        throw e;
                    } finally {
                        MockServerPlayerHelper.destroy(context, player);
                    }

                    context.complete();
                });
    }

    private static <E extends MobEntity> TestFunction createTestMobTeleportUp(String name, String batchId,
            EntityType<E> type, Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                TeleportTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                batchId,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                100,
                0L,
                true,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos1 = BlockPos.ORIGIN;
                    BlockPos blockPos2 = BlockPos.ORIGIN.up(distance);

                    context.setBlockState(blockPos1, elevatorBlock1);
                    context.setBlockState(blockPos2, elevatorBlock2);

                    LivingEntity mob = context.spawnMob(type, blockPos1.up(1));

                    // Act
                    mob.jump();

                    // Assert
                    try {
                        context.expectEntityAt(mob, (shouldTeleport ? blockPos2 : blockPos1).up(1));
                    } catch (Exception e) {
                        QuartzElevatorMod.LOGGER.error(e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }

    private static TestFunction createTestPlayerTeleportDown(String name, String batchId,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                TeleportTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                batchId,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                100,
                0L,
                true,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos1 = BlockPos.ORIGIN.up(distance);
                    BlockPos blockPos2 = BlockPos.ORIGIN;

                    context.setBlockState(blockPos1, elevatorBlock1);
                    context.setBlockState(blockPos2, elevatorBlock2);

                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL, Vec3d.of(blockPos1.up(1)));

                    // Act
                    player.setSneaking(true);
                    player.setSneaking(false);

                    // Assert
                    try {
                        context.expectEntityAt(player, (shouldTeleport ? blockPos2 : blockPos1).up(1));
                    } catch (Exception e) {
                        QuartzElevatorMod.LOGGER.error(e.getMessage());
                        throw e;
                    } finally {
                        MockServerPlayerHelper.destroy(context, player);
                    }

                    context.complete();
                });
    }
}
