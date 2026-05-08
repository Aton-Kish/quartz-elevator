package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.util.MockServerPlayerHelper;
import atonkish.quartzelv.gametest.util.TestFunction;
import atonkish.quartzelv.gametest.util.TestIdentifier;

public class TeleportTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:teleport/default", QuartzElevatorMod.MOD_ID);
  private static final String TEST_ENVIRONMENT_WITH_MIX_TYPES =
      String.format("%s:teleport/with_mix_types", QuartzElevatorMod.MOD_ID);
  private static final String TEST_ENVIRONMENT_WITH_PLAYER_ONLY =
      String.format("%s:teleport/with_player_only", QuartzElevatorMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          //
          // Default
          //

          // Player: Quartz Elevator -> Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player teleport up from Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player does not teleport up from Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  17,
                  false));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player teleport down from Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player does not teleport down from Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  17,
                  false));

          // Player: Smooth Quartz Elevator -> Smooth Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player does not teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  65,
                  false));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player teleport down from Smooth Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player does not teleport down from Smooth Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  65,
                  false));

          // Player: Quartz Elevator -> Smooth Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player does not teleport up from Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  16,
                  false));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player does not teleport down from Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  16,
                  false));

          // Player: Smooth Quartz Elevator -> Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player does not teleport up from Smooth Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  64,
                  false));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player does not teleport down from Smooth Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  64,
                  false));

          // Zombie
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Zombie teleport up from Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  EntityType.ZOMBIE,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  true));
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Zombie teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  EntityType.ZOMBIE,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  true));

          // Slime
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Slime teleport up from Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  EntityType.SLIME,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  true));
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Slime teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  EntityType.SLIME,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  true));

          // Magma Cube
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Magma Cube teleport up from Quartz Elevator to Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  EntityType.MAGMA_CUBE,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  true));
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Magma Cube teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator",
                  TeleportTests.TEST_ENVIRONMENT_DEFAULT,
                  EntityType.MAGMA_CUBE,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  true));

          //
          // With Mix Types
          //

          // Player: Quartz Elevator -> Smooth Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player teleport up from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  16,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player does not teleport up from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  17,
                  false));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player teleport down from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  16,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player does not teleport down from Quartz Elevator to Smooth Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  17,
                  false));

          // Player: Smooth Quartz Elevator -> Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player teleport up from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  64,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player does not teleport up from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  65,
                  false));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player teleport down from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  64,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player does not teleport down from Smooth Quartz Elevator to Quartz Elevator with mix types enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  65,
                  false));

          //
          // With Player Only
          //

          // Player: Quartz Elevator -> Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player teleport down from Quartz Elevator to Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  true));

          // Player: Smooth Quartz Elevator -> Smooth Quartz Elevator
          add(
              TeleportTests.createTestPlayerTeleportUp(
                  "Player teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  true));
          add(
              TeleportTests.createTestPlayerTeleportDown(
                  "Player teleport down from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  true));

          // Zombie
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Zombie teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  EntityType.ZOMBIE,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  false));
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Zombie teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  EntityType.ZOMBIE,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  false));

          // Slime
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Slime teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  EntityType.SLIME,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  false));
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Slime teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  EntityType.SLIME,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  false));

          // Magma Cube
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Magma Cube teleport up from Quartz Elevator to Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  EntityType.MAGMA_CUBE,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  ModBlocks.QUARTZ_ELEVATOR_BLOCK,
                  16,
                  false));
          add(
              TeleportTests.createTestMobTeleportUp(
                  "Magma Cube teleport up from Smooth Quartz Elevator to Smooth Quartz Elevator with player only enabled",
                  TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY,
                  EntityType.MAGMA_CUBE,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  ModBlocks.SMOOTH_QUARTZ_ELEVATOR,
                  64,
                  false));
        }
      };

  private static void beforeTest(GameTestHelper context, String environment) {
    if (environment == TeleportTests.TEST_ENVIRONMENT_WITH_MIX_TYPES) {
      QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
      QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
      QuartzElevatorMod.CONFIG.mixTypes = true;
      QuartzElevatorMod.CONFIG.isPlayerOnly = false;
      QuartzElevatorMod.CONFIG.displayParticles = true;
    } else if (environment == TeleportTests.TEST_ENVIRONMENT_WITH_PLAYER_ONLY) {
      QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
      QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
      QuartzElevatorMod.CONFIG.mixTypes = false;
      QuartzElevatorMod.CONFIG.isPlayerOnly = true;
      QuartzElevatorMod.CONFIG.displayParticles = true;
    } else {
      QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
      QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
      QuartzElevatorMod.CONFIG.mixTypes = false;
      QuartzElevatorMod.CONFIG.isPlayerOnly = false;
      QuartzElevatorMod.CONFIG.displayParticles = true;
    }
  }

  private static TestFunction createTestPlayerTeleportUp(
      String name,
      String environment,
      Block elevatorBlock1,
      Block elevatorBlock2,
      int distance,
      boolean shouldTeleport) {
    Identifier testIdentifier =
        TestIdentifier.of(QuartzElevatorMod.MOD_ID, TeleportTests.class, name);

    return new TestFunction(
        testIdentifier,
        environment,
        TeleportTests.TEST_STRUCTURE_EMPTY,
        20,
        0,
        true,
        Rotation.NONE,
        false,
        1,
        1,
        false,
        1,
        (context) -> {
          // Arrange
          TeleportTests.beforeTest(context, environment);

          BlockPos blockPos1 = BlockPos.ZERO;
          BlockPos blockPos2 = BlockPos.ZERO.above(distance);

          context.setBlock(blockPos1, elevatorBlock1);
          context.setBlock(blockPos2, elevatorBlock2);

          ServerPlayer player =
              MockServerPlayerHelper.spawn(
                  context, GameType.SURVIVAL, Vec3.atLowerCornerOf(blockPos1.above(1)));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          long tickOrigin = 0;
          context.runAtTickTime(
              tickOrigin,
              () -> {
                player.jumpFromGround();

                futurePartialAct1.complete(null);
              });

          long tickAfterTeleporting = 1;
          context.runAtTickTime(
              tickAfterTeleporting,
              () -> {
                futurePartialAct2.complete(null);
              });

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.assertEntityInstancePresent(
                          player, (shouldTeleport ? blockPos2 : blockPos1).above(1));
                    } catch (Exception e) {
                      QuartzElevatorMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                      throw e;
                    } finally {
                      MockServerPlayerHelper.destroy(context, player);
                    }

                    context.succeed();
                  });

          context.succeed();
        });
  }

  private static <E extends Mob> TestFunction createTestMobTeleportUp(
      String name,
      String environment,
      EntityType<E> type,
      Block elevatorBlock1,
      Block elevatorBlock2,
      int distance,
      boolean shouldTeleport) {
    Identifier testIdentifier =
        TestIdentifier.of(QuartzElevatorMod.MOD_ID, TeleportTests.class, name);

    return new TestFunction(
        testIdentifier,
        environment,
        TeleportTests.TEST_STRUCTURE_EMPTY,
        20,
        0,
        true,
        Rotation.NONE,
        false,
        1,
        1,
        false,
        1,
        (context) -> {
          // Arrange
          BlockPos blockPos1 = BlockPos.ZERO;
          BlockPos blockPos2 = BlockPos.ZERO.above(distance);

          context.setBlock(blockPos1, elevatorBlock1);
          context.setBlock(blockPos2, elevatorBlock2);

          LivingEntity mob = context.spawnWithNoFreeWill(type, blockPos1.above(1));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          long tickOrigin = 0;
          context.runAtTickTime(
              tickOrigin,
              () -> {
                mob.jumpFromGround();

                futurePartialAct1.complete(null);
              });

          long tickAfterTeleporting = 1;
          context.runAtTickTime(
              tickAfterTeleporting,
              () -> {
                futurePartialAct2.complete(null);
              });

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.assertEntityInstancePresent(
                          mob, (shouldTeleport ? blockPos2 : blockPos1).above(1));
                    } catch (Exception e) {
                      QuartzElevatorMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                      throw e;
                    }

                    context.succeed();
                  });

          context.succeed();
        });
  }

  private static TestFunction createTestPlayerTeleportDown(
      String name,
      String environment,
      Block elevatorBlock1,
      Block elevatorBlock2,
      int distance,
      boolean shouldTeleport) {
    Identifier testIdentifier =
        TestIdentifier.of(QuartzElevatorMod.MOD_ID, TeleportTests.class, name);

    return new TestFunction(
        testIdentifier,
        environment,
        TeleportTests.TEST_STRUCTURE_EMPTY,
        20,
        0,
        true,
        Rotation.NONE,
        false,
        1,
        1,
        false,
        1,
        (context) -> {
          // Arrange
          BlockPos blockPos1 = BlockPos.ZERO.above(distance);
          BlockPos blockPos2 = BlockPos.ZERO;

          context.setBlock(blockPos1, elevatorBlock1);
          context.setBlock(blockPos2, elevatorBlock2);

          ServerPlayer player =
              MockServerPlayerHelper.spawn(
                  context, GameType.SURVIVAL, Vec3.atLowerCornerOf(blockPos1.above(1)));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          long tickOrigin = 0;
          context.runAtTickTime(
              tickOrigin,
              () -> {
                player.setShiftKeyDown(true);
                player.setShiftKeyDown(false);

                futurePartialAct1.complete(null);
              });

          long tickAfterTeleporting = 1;
          context.runAtTickTime(
              tickAfterTeleporting,
              () -> {
                futurePartialAct2.complete(null);
              });

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.assertEntityInstancePresent(
                          player, (shouldTeleport ? blockPos2 : blockPos1).above(1));
                    } catch (Exception e) {
                      QuartzElevatorMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                      throw e;
                    } finally {
                      MockServerPlayerHelper.destroy(context, player);
                    }

                    context.succeed();
                  });

          context.succeed();
        });
  }
}
