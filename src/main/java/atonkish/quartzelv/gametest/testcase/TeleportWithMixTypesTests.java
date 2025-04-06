package atonkish.quartzelv.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.util.MockServerPlayerHelper;

public class TeleportWithMixTypesTests implements CustomTestMethodInvoker {
    public void invokeTestMethod(TestContext context, Method method) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = true;
        QuartzElevatorMod.CONFIG.isPlayerOnly = false;
        QuartzElevatorMod.CONFIG.displayParticles = true;

        try {
            method.invoke(this, context);
        } catch (InvocationTargetException e) {
            // Ensure that any GameTestException are propagated without wrapping
            if (e.getTargetException() instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            throw new RuntimeException("Failed to invoke test method", e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to invoke test method", e);
        }
    }

    private void playerTeleportUpTest(TestContext context,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

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
            QuartzElevatorMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
        } finally {
            MockServerPlayerHelper.destroy(context, player);
        }

        context.complete();
    }

    private void playerTeleportDownTest(TestContext context,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

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
            QuartzElevatorMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
        } finally {
            MockServerPlayerHelper.destroy(context, player);
        }

        context.complete();
    }

    //
    // Player: Stone Elevator -> Smooth Quartz Elevator
    //

    @GameTest
    public void playerTeleportUpFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.playerTeleportUpTest(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                16,
                true);
    }

    @GameTest
    public void playerDoesNotTeleportUpFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.playerTeleportUpTest(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                17,
                false);
    }

    @GameTest
    public void playerTeleportDownFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.playerTeleportDownTest(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                16,
                true);
    }

    @GameTest
    public void playerDoesNotTeleportDownFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.playerTeleportDownTest(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                17,
                false);
    }

    //
    // Player: Smooth Quartz Elevator -> Quartz Elevator
    //

    @GameTest
    public void playerTeleportUpFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        this.playerTeleportUpTest(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                64,
                true);
    }

    @GameTest
    public void playerDoesNotTeleportUpFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        this.playerTeleportUpTest(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                65,
                false);
    }

    @GameTest
    public void playerTeleportDownFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        this.playerTeleportDownTest(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                64,
                true);
    }

    @GameTest
    public void playerDoesNotTeleportDownFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        this.playerTeleportDownTest(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                65,
                false);
    }
}
