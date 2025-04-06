package atonkish.quartzelv.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.util.MockServerPlayerHelper;

public class LootTableDefaultTests implements CustomTestMethodInvoker {
    public void invokeTestMethod(TestContext context, Method method) {
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

    private void test(TestContext context, Block elevatorBlock, Item tool, boolean shouldDrop) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, elevatorBlock);

        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));
        player.setStackInHand(Hand.MAIN_HAND, new ItemStack(tool));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos), PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                    Direction.NORTH, context.getWorld().getHeight(), 0);

            futurePartialAct1.complete(null);
        });

        long tickBlockBreaking = (long) Math.ceil(
                1.0D / context.getBlockState(blockPos).calcBlockBreakingDelta(player,
                        context.getWorld(), blockPos));
        context.runAtTick(tickBlockBreaking, () -> {
            player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos),
                    PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                    Direction.NORTH, context.getWorld().getHeight(), 0);

            futurePartialAct2.complete(null);
        });

        QuartzElevatorMod.LOGGER.info("[{}] {} can be mined in {} ticks by {}",
                testName,
                elevatorBlock.getName().getString(),
                tickBlockBreaking,
                tool.getName().getString());

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.expectBlock(Blocks.AIR, blockPos);
                context.expectEntitiesAround(EntityType.ITEM, blockPos, shouldDrop ? 1 : 0, 1);
            } catch (Exception e) {
                QuartzElevatorMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                throw e;
            } finally {
                MockServerPlayerHelper.destroy(context, player);
            }

            context.complete();
        });
    }

    //
    // Quartz Elevator
    //

    @GameTest(maxTicks = 1000)
    public void breakQuartzElevatorWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakQuartzElevatorWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                Items.NETHERITE_AXE,
                false);
    }

    @GameTest(maxTicks = 1000)
    public void breakQuartzElevatorWithoutTools(TestContext context) {
        test(context,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                Items.AIR,
                false);
    }

    //
    // Smooth Quartz Elevator
    //

    @GameTest(maxTicks = 1000)
    public void breakSmoothQuartzElevatorWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakSmoothQuartzElevatorWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                Items.NETHERITE_AXE,
                false);
    }

    @GameTest(maxTicks = 1000)
    public void breakSmoothQuartzElevatorWithoutTools(TestContext context) {
        test(context,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                Items.AIR,
                false);
    }
}
