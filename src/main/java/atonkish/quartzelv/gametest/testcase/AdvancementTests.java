package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.gametest.util.MockServerPlayerHelper;
import atonkish.quartzelv.gametest.util.TestFunction;
import atonkish.quartzelv.gametest.util.TestIdentifier;
import atonkish.quartzelv.item.ModItems;

public class AdvancementTests {
    private static final String TEST_ENVIRONMENT_DEFAULT = String.format("%s:advancement/default",
            QuartzElevatorMod.MOD_ID);
    private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Quartz Elevator
            add(AdvancementTests.createTest(
                    "Obtain Quartz Elevator recipe advancement by having Quartz",
                    Items.QUARTZ,
                    Identifier.of(QuartzElevatorMod.MOD_ID, "recipes/building_blocks/quartz_elevator")));
            add(AdvancementTests.createTest(
                    "Obtain Quartz Elevator recipe advancement by having Quartz Block",
                    Items.QUARTZ_BLOCK,
                    Identifier.of(QuartzElevatorMod.MOD_ID,
                            "recipes/building_blocks/quartz_elevator_from_quartz_block")));

            // Smooth Quartz Elevator
            add(AdvancementTests.createTest(
                    "Obtain Smooth Quartz Elevator recipe advancement by having Quartz Elevator",
                    ModItems.QUARTZ_ELEVATOR_BLOCK,
                    Identifier.of(QuartzElevatorMod.MOD_ID, "recipes/building_blocks/smooth_quartz_elevator")));
            add(AdvancementTests.createTest(
                    "Obtain Smooth Quartz Elevator recipe advancement by Smooth Quartz",
                    Items.SMOOTH_QUARTZ,
                    Identifier.of(QuartzElevatorMod.MOD_ID,
                            "recipes/building_blocks/smooth_quartz_elevator_from_smooth_quartz")));
        }
    };

    private static TestFunction createTest(String name, Item item, Identifier advancementId) {
        Identifier testIdentifier = TestIdentifier.of(QuartzElevatorMod.MOD_ID,
                AdvancementTests.class,
                name);

        return new TestFunction(
                testIdentifier,
                AdvancementTests.TEST_ENVIRONMENT_DEFAULT,
                AdvancementTests.TEST_STRUCTURE_EMPTY,
                20,
                0,
                true,
                BlockRotation.NONE,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL,
                            Vec3d.of(BlockPos.ORIGIN));
                    AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader().get(advancementId);
                    AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);

                    // Act
                    CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
                    CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

                    Map<String, Boolean> progressMap = new HashMap<String, Boolean>();
                    String progressMapKeyBeforeHavingItem = "beforeHavingItem";
                    String progressMapKeyAfterHavingItem = "afterHavingItem";

                    long tickOrigin = 0;
                    context.runAtTick(tickOrigin, () -> {
                        progressMap.put(progressMapKeyBeforeHavingItem, progress.isDone());

                        player.giveItemStack(new ItemStack(item));

                        futurePartialAct1.complete(null);
                    });

                    long tickObtained = 1;
                    context.runAtTick(tickObtained, () -> {
                        progressMap.put(progressMapKeyAfterHavingItem, progress.isDone());

                        futurePartialAct2.complete(null);
                    });

                    // Assert
                    CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
                        try {
                            context.assertFalse(progressMap.get(progressMapKeyBeforeHavingItem),
                                    Text.of(String.format(
                                            "Expected that advancement %s has not been done yet, but it has been already done.",
                                            entry)));
                            context.assertTrue(progressMap.get(progressMapKeyAfterHavingItem),
                                    Text.of(String.format(
                                            "Expected that advancement %s has been done, but it has not been done yet.",
                                            entry)));
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
