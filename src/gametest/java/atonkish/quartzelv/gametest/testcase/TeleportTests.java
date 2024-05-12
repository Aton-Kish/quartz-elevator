package atonkish.quartzelv.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.BeforeBatch;
import net.minecraft.test.GameTest;
import net.minecraft.test.PositionedException;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.QuartzElevatorModGameTest;
import atonkish.quartzelv.gametest.util.ModTestHelper;

public class TeleportTests {
    private void testTeleportUp(TestContext context, Block elevatorBlock1, Block elevatorBlock2, int distance,
            boolean shouldTeleport) {
        // Arrange
        BlockPos blockPos1 = BlockPos.ORIGIN;
        BlockPos blockPos2 = BlockPos.ORIGIN.up(distance);

        context.setBlockState(blockPos1, elevatorBlock1);
        context.setBlockState(blockPos2, elevatorBlock2);

        ServerPlayerEntity player = ModTestHelper.createMockServerPlayer(context, GameMode.SURVIVAL);
        player.setPosition(context.getAbsolute(Vec3d.of(blockPos1.up(1))));

        // Act
        player.jump();

        // Assert
        try {
            context.expectEntityAt(player, (shouldTeleport ? blockPos2 : blockPos1).up(1));
        } catch (PositionedException e) {
            QuartzElevatorMod.LOGGER.error(e.getMessage());
            throw e;
        } finally {
            ModTestHelper.destroyMockServerPlayer(context, player);
        }

        context.complete();
    }

    private void testTeleportDown(TestContext context, Block elevatorBlock1, Block elevatorBlock2, int distance,
            boolean shouldTeleport) {
        // Arrange
        BlockPos blockPos1 = BlockPos.ORIGIN.up(distance);
        BlockPos blockPos2 = BlockPos.ORIGIN;

        context.setBlockState(blockPos1, elevatorBlock1);
        context.setBlockState(blockPos2, elevatorBlock2);

        ServerPlayerEntity player = ModTestHelper.createMockServerPlayer(context, GameMode.SURVIVAL);
        player.setPosition(context.getAbsolute(Vec3d.of(blockPos1.up(1))));

        // Act
        player.setSneaking(true);
        player.setSneaking(false);

        // Assert
        try {
            context.expectEntityAt(player, (shouldTeleport ? blockPos2 : blockPos1).up(1));
        } catch (PositionedException e) {
            QuartzElevatorMod.LOGGER.error(e.getMessage());
            throw e;
        } finally {
            ModTestHelper.destroyMockServerPlayer(context, player);
        }

        context.complete();
    }

    /*
     * batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT
     */

    @BeforeBatch(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT)
    public void beforeBatchTeleport(ServerWorld world) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = false;
        QuartzElevatorMod.CONFIG.isPlayerOnly = false;
        QuartzElevatorMod.CONFIG.displayParticles = true;
    }

    // teleportUp
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        testTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        testTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 65, false);
    }

    // teleportDown
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        testTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        testTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 65, false);
    }

    // mix types
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportUpFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportUpFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        testTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportDownFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportDownFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        testTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, false);
    }

    /*
     * batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES
     */

    @BeforeBatch(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES)
    public void beforeBatchTeleportWithMixTypesConfig(ServerWorld world) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = true;
        QuartzElevatorMod.CONFIG.isPlayerOnly = false;
        QuartzElevatorMod.CONFIG.displayParticles = true;
    }

    // teleportUp
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportUpFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportUpFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportUpFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportUpFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 65, false);
    }

    // teleportDown
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportDownFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportDownFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testTeleportDownFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testNotTeleportDownFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 65, false);
    }
}
