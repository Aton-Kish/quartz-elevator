package atonkish.quartzelv.gametest;

import java.util.ArrayList;
import java.util.Collection;

import atonkish.quartzelv.gametest.testcase.AdvancementTests;
import atonkish.quartzelv.gametest.testcase.LootTableTests;
import atonkish.quartzelv.gametest.testcase.RecipeTests;
import atonkish.quartzelv.gametest.testcase.TeleportTests;
import atonkish.quartzelv.gametest.util.TestFunction;

public class QuartzElevatorModGameTest {
    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            if (System.getProperty(QuartzElevatorModGameTest.class.getPackageName()) != null) {
                addAll(AdvancementTests.TEST_FUNCTIONS);
                addAll(LootTableTests.TEST_FUNCTIONS);
                addAll(RecipeTests.TEST_FUNCTIONS);
                addAll(TeleportTests.TEST_FUNCTIONS);
            }
        }
    };
}
