package atonkish.quartzelv.gametest;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.BeforeBatch;
import net.minecraft.test.CustomTestProvider;
import net.minecraft.test.TestFunction;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.gametest.testcase.AdvancementTests;
import atonkish.quartzelv.gametest.testcase.LootTableTests;
import atonkish.quartzelv.gametest.testcase.RecipeTests;
import atonkish.quartzelv.gametest.testcase.TeleportTests;

public class QuartzElevatorModGameTest {
    @BeforeBatch(batchId = TeleportTests.BATCH_ID_DEFAULT)
    public void beforeBatchDefalt(ServerWorld world) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = false;
        QuartzElevatorMod.CONFIG.isPlayerOnly = false;
        QuartzElevatorMod.CONFIG.displayParticles = true;
    }

    @BeforeBatch(batchId = TeleportTests.BATCH_ID_WITH_MIX_TYPES)
    public void beforeBatchWithMixTypes(ServerWorld world) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = true;
        QuartzElevatorMod.CONFIG.isPlayerOnly = false;
        QuartzElevatorMod.CONFIG.displayParticles = true;
    }

    @BeforeBatch(batchId = TeleportTests.BATCH_ID_WITH_PLAYER_ONLY)
    public void beforeBatchWithPlayerOnly(ServerWorld world) {
        QuartzElevatorMod.CONFIG.quartzElevatorDistance = 16;
        QuartzElevatorMod.CONFIG.smoothQuartzElevatorDistance = 64;
        QuartzElevatorMod.CONFIG.mixTypes = false;
        QuartzElevatorMod.CONFIG.isPlayerOnly = true;
        QuartzElevatorMod.CONFIG.displayParticles = true;
    }

    @CustomTestProvider
    public Collection<TestFunction> registerTests() {
        Collection<TestFunction> testFunctions = new ArrayList<>();

        if (System.getProperty(this.getClass().getPackageName()) == null) {
            return testFunctions;
        }

        testFunctions.addAll(AdvancementTests.TEST_FUNCTIONS);
        testFunctions.addAll(LootTableTests.TEST_FUNCTIONS);
        testFunctions.addAll(RecipeTests.TEST_FUNCTIONS);
        testFunctions.addAll(TeleportTests.TEST_FUNCTIONS);

        return testFunctions;
    }
}
