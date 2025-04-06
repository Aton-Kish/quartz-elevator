package atonkish.quartzelv.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.gametest.util.MockServerPlayerHelper;
import atonkish.quartzelv.item.ModItems;

public class AdvancementDefaultTests implements CustomTestMethodInvoker {
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

    private void test(TestContext context, Item item, Identifier advancementId) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(BlockPos.ORIGIN));
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(advancementId);
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

    @GameTest
    public void obtainQuartzElevatorRecipeAdvancementByHavingQuartz(TestContext context) {
        test(context,
                Items.QUARTZ,
                Identifier.of(QuartzElevatorMod.MOD_ID,
                        "recipes/building_blocks/quartz_elevator"));
    }

    @GameTest
    public void obtainQuartzElevatorRecipeAdvancementByHavingQuartzBlock(TestContext context) {
        test(context,
                Items.QUARTZ_BLOCK,
                Identifier.of(QuartzElevatorMod.MOD_ID,
                        "recipes/building_blocks/quartz_elevator_from_quartz_block"));
    }

    //
    // Smooth Quartz Elevator
    //

    @GameTest
    public void obtainSmoothQuartzElevatorRecipeAdvancementByHavingQuartzElevator(TestContext context) {
        test(context,
                ModItems.QUARTZ_ELEVATOR_BLOCK,
                Identifier.of(QuartzElevatorMod.MOD_ID,
                        "recipes/building_blocks/smooth_quartz_elevator"));
    }

    @GameTest
    public void obtainSmoothQuartzElevatorRecipeAdvancementByHavingSmoothQuartz(TestContext context) {
        test(context,
                Items.SMOOTH_QUARTZ,
                Identifier.of(QuartzElevatorMod.MOD_ID,
                        "recipes/building_blocks/smooth_quartz_elevator_from_smooth_quartz"));
    }
}
