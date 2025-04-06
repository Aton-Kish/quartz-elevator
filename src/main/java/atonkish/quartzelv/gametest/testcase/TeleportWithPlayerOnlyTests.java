package atonkish.quartzelv.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
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

public class TeleportWithPlayerOnlyTests implements CustomTestMethodInvoker {
    public void invokeTestMethod(TestContext context, Method method) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = false;
        QuartzElevatorMod.CONFIG.isPlayerOnly = true;
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

    private <E extends MobEntity> void mobTeleportUpTest(TestContext context,
            EntityType<E> type, Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
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

        LivingEntity mob = context.spawnMob(type, blockPos1.up(1));

        // Act
        mob.jump();

        // Assert
        try {
            context.expectEntityAt(mob, (shouldTeleport ? blockPos2 : blockPos1).up(1));
        } catch (Exception e) {
            QuartzElevatorMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
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
    // Player: Quartz Elevator -> Quartz Elevator
    //

    @GameTest
    public void playerTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        this.playerTeleportUpTest(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                16,
                true);
    }

    @GameTest
    public void playerTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        this.playerTeleportDownTest(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                16,
                true);
    }

    //
    // Player: Smooth Quartz Elevator -> Smooth Quartz Elevator
    //

    @GameTest
    public void playerTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.playerTeleportUpTest(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                64,
                true);
    }

    @GameTest
    public void playerTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.playerTeleportDownTest(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                64,
                true);
    }

    //
    // Zombie
    //

    @GameTest
    public void zombieDoesNotTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        this.mobTeleportUpTest(context,
                EntityType.ZOMBIE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                16,
                false);
    }

    @GameTest
    public void zombieDoesNotTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.mobTeleportUpTest(context,
                EntityType.ZOMBIE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                64,
                false);
    }

    //
    // Slime
    //

    @GameTest
    public void slimeDoesNotTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        this.mobTeleportUpTest(context,
                EntityType.SLIME,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                16,
                false);
    }

    @GameTest
    public void slimeDoesNotTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.mobTeleportUpTest(context,
                EntityType.SLIME,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                64,
                false);
    }

    //
    // Magma Cube
    //

    @GameTest
    public void magmaCubeDoesNotTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        this.mobTeleportUpTest(context,
                EntityType.MAGMA_CUBE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                16,
                false);
    }

    @GameTest
    public void magmaCubeDoesNotTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        this.mobTeleportUpTest(context,
                EntityType.MAGMA_CUBE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                64,
                false);
    }
}
