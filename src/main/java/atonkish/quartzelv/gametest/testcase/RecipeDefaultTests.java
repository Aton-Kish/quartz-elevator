package atonkish.quartzelv.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.item.ModItems;

public class RecipeDefaultTests implements CustomTestMethodInvoker {
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

    private <I extends RecipeInput, T extends Recipe<I>> void test(TestContext context,
            RecipeType<T> type, I input, ItemStack expected) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        ServerWorld world = context.getWorld();
        ServerRecipeManager recipeManager = world.getRecipeManager();
        DynamicRegistryManager registryManager = world.getRegistryManager();
        T recipe = recipeManager.getFirstMatch(type, input, world).orElseThrow().value();

        // Act
        ItemStack actual = recipe.craft(input, registryManager);

        // Assert
        try {
            context.assertTrue(ItemStack.areEqual(actual, expected),
                    Text.of("Recipe result differs from expected."));
        } catch (Exception e) {
            QuartzElevatorMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
        }

        context.complete();
    }

    //
    // Quartz Elevator
    //

    @GameTest
    public void craftQuartzElevator(TestContext context) {
        ItemStack quartz = new ItemStack(Items.QUARTZ);
        ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
        ItemStack elevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);
        test(context,
                RecipeType.CRAFTING,
                CraftingRecipeInput.create(3, 3, List.of(
                        ItemStack.EMPTY, quartz, ItemStack.EMPTY,
                        quartz, enderPearl, quartz,
                        ItemStack.EMPTY, quartz, ItemStack.EMPTY)),
                elevator);
    }

    @GameTest
    public void craftQuartzElevatorFromQuartzBlock(TestContext context) {
        ItemStack quartzBlock = new ItemStack(Items.QUARTZ_BLOCK);
        ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
        ItemStack elevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);
        test(context,
                RecipeType.CRAFTING,
                CraftingRecipeInput.create(2, 2, List.of(
                        quartzBlock, enderPearl,
                        ItemStack.EMPTY, ItemStack.EMPTY)),
                elevator);
    }

    //
    // Smooth Quartz Elevator
    //

    @GameTest
    public void smeltSmoothQuartzElevator(TestContext context) {
        ItemStack baseElevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);
        ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);
        test(context,
                RecipeType.SMELTING,
                new SingleStackRecipeInput(baseElevator),
                elevator);
    }

    @GameTest
    public void craftSmoothQuartzElevatorFromSmoothQuartzBlock(TestContext context) {
        ItemStack smoothQuartz = new ItemStack(Items.SMOOTH_QUARTZ);
        ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
        ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);
        test(context,
                RecipeType.CRAFTING,
                CraftingRecipeInput.create(2, 2, List.of(
                        smoothQuartz, enderPearl,
                        ItemStack.EMPTY, ItemStack.EMPTY)),
                elevator);
    }
}
