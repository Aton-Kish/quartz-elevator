package atonkish.quartzelv.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.PositionedException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.concurrent.CompletableFuture;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.QuartzElevatorModGameTest;
import atonkish.quartzelv.gametest.util.ModTestHelper;

public class LootTableTests {
    private void testBreakElevatorWithTool(TestContext context, Block elevatorBlock, Item tool,
            boolean shouldDrop) {
        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, elevatorBlock);

        ServerPlayerEntity player = ModTestHelper.createMockServerPlayer(context, GameMode.SURVIVAL);
        player.setPosition(context.getAbsolute(Vec3d.of(blockPos.south(4))));
        player.setStackInHand(Hand.MAIN_HAND, new ItemStack(tool));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos), PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                    Direction.NORTH, context.getWorld().getTopY(), 0);

            futurePartialAct1.complete(null);
        });

        long tickBlockBreaking = (long) Math.ceil(
                1.0D / context.getBlockState(blockPos).calcBlockBreakingDelta(player,
                        context.getWorld(), blockPos));
        context.runAtTick(tickBlockBreaking, () -> {
            player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos),
                    PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                    Direction.NORTH, context.getWorld().getTopY(), 0);

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.expectBlock(Blocks.AIR, blockPos);
                context.expectEntitiesAround(EntityType.ITEM, blockPos, shouldDrop ? 1 : 0, 1);
            } catch (PositionedException e) {
                QuartzElevatorMod.LOGGER.error(e.getMessage());
                throw e;
            } finally {
                ModTestHelper.destroyMockServerPlayer(context, player);
            }

            context.complete();
        });
    }

    /*
     * batchId = QuartzElevatorModGameTest.BATCH_ID_LOOT_TABLE
     */

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakQuartzElevatorWithPickaxe(TestContext context) {
        testBreakElevatorWithTool(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, Items.NETHERITE_PICKAXE, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakQuartzElevatorWithAxe(TestContext context) {
        testBreakElevatorWithTool(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, Items.NETHERITE_AXE, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakQuartzElevatorWithoutTool(TestContext context) {
        testBreakElevatorWithTool(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, Items.AIR, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakSmoothQuartzElevatorWithPickaxe(TestContext context) {
        testBreakElevatorWithTool(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, Items.NETHERITE_PICKAXE, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakSmoothQuartzElevatorWithAxe(TestContext context) {
        testBreakElevatorWithTool(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, Items.NETHERITE_AXE, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakSmoothQuartzElevatorWithoutTool(TestContext context) {
        testBreakElevatorWithTool(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, Items.AIR, false);
    }
}
