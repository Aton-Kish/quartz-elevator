package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.util.MockServerPlayerHelper;
import atonkish.quartzelv.gametest.util.TestFunction;
import atonkish.quartzelv.gametest.util.TestIdentifier;

public class LootTableTests {
    private static final String TEST_ENVIRONMENT_DEFAULT = String.format("%s:loot_table/default",
            QuartzElevatorMod.MOD_ID);
    private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Quartz Elevator
            add(LootTableTests.createTest(
                    "Break Quartz Elevator with Netherite Pickaxe",
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    Items.NETHERITE_PICKAXE,
                    true));
            add(LootTableTests.createTest(
                    "Break Quartz Elevator with Netherite Axe",
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    Items.NETHERITE_AXE,
                    false));
            add(LootTableTests.createTest(
                    "Break Quartz Elevator without tools",
                    ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                    Items.AIR,
                    false));

            // Smooth Quartz Elevator
            add(LootTableTests.createTest(
                    "Break Smooth Quartz Elevator with Netherite Pickaxe",
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    Items.NETHERITE_PICKAXE,
                    true));
            add(LootTableTests.createTest(
                    "Break Smooth Quartz Elevator with Netherite Axe",
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    Items.NETHERITE_AXE,
                    false));
            add(LootTableTests.createTest(
                    "Break Smooth Quartz Elevator without tools",
                    ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                    Items.AIR,
                    false));
        }
    };

    private static TestFunction createTest(String name, Block elevatorBlock, Item tool, boolean shouldDrop) {
        Identifier testIdentifier = TestIdentifier.of(QuartzElevatorMod.MOD_ID,
                LootTableTests.class,
                name);

        return new TestFunction(
                testIdentifier,
                LootTableTests.TEST_ENVIRONMENT_DEFAULT,
                LootTableTests.TEST_STRUCTURE_EMPTY,
                1000,
                0,
                true,
                BlockRotation.NONE,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos = BlockPos.ORIGIN;
                    context.setBlockState(blockPos, elevatorBlock);

                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL,
                            Vec3d.of(blockPos.south(4)));
                    player.setStackInHand(Hand.MAIN_HAND, new ItemStack(tool));

                    // Act
                    CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
                    CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

                    long tickOrigin = 0;
                    context.runAtTick(tickOrigin, () -> {
                        player.interactionManager.processBlockBreakingAction(context.getAbsolutePos(blockPos),
                                PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                                Direction.NORTH,
                                context.getWorld().getHeight(),
                                0);

                        futurePartialAct1.complete(null);
                    });

                    long tickBlockBreaking = (long) Math.ceil(1.0D / context
                            .getBlockState(blockPos)
                            .calcBlockBreakingDelta(player, context.getWorld(), blockPos));
                    context.runAtTick(tickBlockBreaking, () -> {
                        player.interactionManager.processBlockBreakingAction(context.getAbsolutePos(blockPos),
                                PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                                Direction.NORTH,
                                context.getWorld().getHeight(),
                                0);

                        futurePartialAct2.complete(null);
                    });

                    QuartzElevatorMod.LOGGER.info("[{}] {} can be mined in {} ticks by {}",
                            testIdentifier,
                            elevatorBlock.getName().getString(),
                            tickBlockBreaking,
                            tool.getName().getString());

                    // Assert
                    CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
                        try {
                            context.expectBlock(Blocks.AIR, blockPos);
                            context.expectEntitiesAround(EntityType.ITEM, blockPos, shouldDrop ? 1 : 0, 1);
                        } catch (Exception e) {
                            QuartzElevatorMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                            throw e;
                        } finally {
                            MockServerPlayerHelper.destroy(context, player);
                        }

                        context.complete();
                    });
                });
    }
}
