package atonkish.quartzelv.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.BeforeBatch;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.block.ModBlocks;
import atonkish.quartzelv.gametest.QuartzElevatorModGameTest;
import atonkish.quartzelv.gametest.util.ModTestHelper;

public class TeleportTests {
    private <E extends LivingEntity> void testTeleportUp(TestContext context, E entity,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        // Arrange
        BlockPos blockPos1 = BlockPos.ORIGIN;
        BlockPos blockPos2 = BlockPos.ORIGIN.up(distance);

        context.setBlockState(blockPos1, elevatorBlock1);
        context.setBlockState(blockPos2, elevatorBlock2);

        entity.setPosition(context.getAbsolute(Vec3d.of(blockPos1.up(1))));

        // Act
        entity.jump();

        // Assert
        try {
            context.expectEntityAt(entity, (shouldTeleport ? blockPos2 : blockPos1).up(1));
        } catch (Exception e) {
            QuartzElevatorMod.LOGGER.error(e.getMessage());
            throw e;
        }

        context.complete();
    }

    private void testPlayerTeleportUp(TestContext context,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        ServerPlayerEntity player = ModTestHelper.createMockServerPlayer(context, GameMode.SURVIVAL);
        try {
            testTeleportUp(context, player, elevatorBlock1, elevatorBlock2, distance, shouldTeleport);
        } catch (Exception e) {
            throw e;
        } finally {
            ModTestHelper.destroyMockServerPlayer(context, player);
        }
    }

    private <E extends MobEntity> void testMobTeleportUp(TestContext context, EntityType<E> type,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        LivingEntity mob = context.spawnMob(type, BlockPos.ORIGIN);
        try {
            testTeleportUp(context, mob, elevatorBlock1, elevatorBlock2, distance, shouldTeleport);
        } catch (Exception e) {
            throw e;
        } finally {
            mob.kill();
        }
    }

    private void testTeleportDown(TestContext context, Entity entity,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        // Arrange
        BlockPos blockPos1 = BlockPos.ORIGIN.up(distance);
        BlockPos blockPos2 = BlockPos.ORIGIN;

        context.setBlockState(blockPos1, elevatorBlock1);
        context.setBlockState(blockPos2, elevatorBlock2);

        entity.setPosition(context.getAbsolute(Vec3d.of(blockPos1.up(1))));

        // Act
        entity.setSneaking(true);
        entity.setSneaking(false);

        // Assert
        try {
            context.expectEntityAt(entity, (shouldTeleport ? blockPos2 : blockPos1).up(1));
        } catch (Exception e) {
            QuartzElevatorMod.LOGGER.error(e.getMessage());
            throw e;
        }

        context.complete();
    }

    private void testPlayerTeleportDown(TestContext context,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        ServerPlayerEntity player = ModTestHelper.createMockServerPlayer(context, GameMode.SURVIVAL);
        try {
            testTeleportDown(context, player, elevatorBlock1, elevatorBlock2, distance, shouldTeleport);
        } catch (Exception e) {
            throw e;
        } finally {
            ModTestHelper.destroyMockServerPlayer(context, player);
        }
    }

    private <E extends MobEntity> void testMobTeleportDown(TestContext context, EntityType<E> type,
            Block elevatorBlock1, Block elevatorBlock2, int distance, boolean shouldTeleport) {
        LivingEntity mob = context.spawnMob(type, BlockPos.ORIGIN);
        try {
            testTeleportDown(context, mob, elevatorBlock1, elevatorBlock2, distance, shouldTeleport);
        } catch (Exception e) {
            throw e;
        } finally {
            mob.kill();
        }
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

    // player teleport up
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 65, false);
    }

    // player teleport down
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 65, false);
    }

    // mix types
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportUpFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportUpFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportDownFromQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportDownFromSmoothQuartzElevatorToQuartzElevator(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, false);
    }

    // mob teleport up
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        testMobTeleportUp(context, EntityType.ZOMBIE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testMobTeleportUp(context, EntityType.ZOMBIE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        testMobTeleportUp(context, EntityType.SLIME,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testMobTeleportUp(context, EntityType.SLIME,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportUpFromQuartzElevatorToQuartzElevator(TestContext context) {
        testMobTeleportUp(context, EntityType.MAGMA_CUBE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testMobTeleportUp(context, EntityType.MAGMA_CUBE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    // mob teleport down
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        testMobTeleportDown(context, EntityType.ZOMBIE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testMobTeleportDown(context, EntityType.ZOMBIE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        testMobTeleportDown(context, EntityType.SLIME,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testMobTeleportDown(context, EntityType.SLIME,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportDownFromQuartzElevatorToQuartzElevator(TestContext context) {
        testMobTeleportDown(context, EntityType.MAGMA_CUBE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevator(TestContext context) {
        testMobTeleportDown(context, EntityType.MAGMA_CUBE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
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

    // player teleport up
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportUpFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportUpFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportUpFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportUpFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 65, false);
    }

    // player teleport down
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportDownFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportDownFromQuartzElevatorToSmoothQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 17, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportDownFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 64, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_MIX_TYPES, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerNotTeleportDownFromSmoothQuartzElevatorToQuartzElevatorWithMixTypes(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 65, false);
    }

    /*
     * batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY
     */

    @BeforeBatch(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY)
    public void beforeBatchTeleportWithPlayerOnly(ServerWorld world) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = false;
        QuartzElevatorMod.CONFIG.isPlayerOnly = true;
        QuartzElevatorMod.CONFIG.displayParticles = true;
    }

    // player teleport up
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportUpFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(TestContext context) {
        testPlayerTeleportUp(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    // player teleport down
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportDownFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, true);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPlayerTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(
            TestContext context) {
        testPlayerTeleportDown(context, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, true);
    }

    // mob teleport up
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportUpFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportUp(context, EntityType.ZOMBIE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportUp(context, EntityType.ZOMBIE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportUpFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportUp(context, EntityType.SLIME,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportUp(context, EntityType.SLIME,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportUpFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportUp(context, EntityType.MAGMA_CUBE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportUpFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(
            TestContext context) {
        testMobTeleportUp(context, EntityType.MAGMA_CUBE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, false);
    }

    // mob teleport down
    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportDownFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportDown(context, EntityType.ZOMBIE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testZombieTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(
            TestContext context) {
        testMobTeleportDown(context, EntityType.ZOMBIE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportDownFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportDown(context, EntityType.SLIME,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSlimeTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportDown(context, EntityType.SLIME,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportDownFromQuartzElevatorToQuartzElevatorWithPlayerOnly(TestContext context) {
        testMobTeleportDown(context, EntityType.MAGMA_CUBE,
                ModBlocks.QUARTZ_ELEVATOR_BLOCK, ModBlocks.QUARTZ_ELEVATOR_BLOCK, 16, false);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_TELEPORT_WITH_PLAYER_ONLY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testMagmaCubeTeleportDownFromSmoothQuartzElevatorToSmoothQuartzElevatorWithPlayerOnly(
            TestContext context) {
        testMobTeleportDown(context, EntityType.MAGMA_CUBE,
                ModBlocks.SMOOTH_QUARTZ_ELEVATOR, ModBlocks.SMOOTH_QUARTZ_ELEVATOR, 64, false);
    }
}
