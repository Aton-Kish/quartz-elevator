package atonkish.quartzelv.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

import java.util.Arrays;
import java.util.List;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.gametest.QuartzElevatorModGameTest;
import atonkish.quartzelv.gametest.util.ModTestScreenHander;
import atonkish.quartzelv.item.ModItems;

public class RecipeTests {
    private <C extends Inventory, T extends Recipe<C>> void testCraftElevator(TestContext context,
            RecipeType<T> type, C inventory, ItemStack expected) {
        // Arrange
        ServerWorld world = context.getWorld();
        RecipeManager recipeManager = world.getRecipeManager();
        DynamicRegistryManager registryManager = world.getRegistryManager();
        T recipe = recipeManager.getFirstMatch(type, inventory, world).orElseThrow().value();

        // Act
        ItemStack actual = recipe.craft(inventory, registryManager);

        // Assert
        try {
            context.assertEquals(actual.getItem(), expected.getItem(), "recipe result item");
            context.assertEquals(actual.getCount(), expected.getCount(), "recipe result count");
        } catch (Exception e) {
            QuartzElevatorMod.LOGGER.error(e.getMessage());
            throw e;
        }

        context.complete();
    }

    /*
     * batchId = QuartzElevatorModGameTest.BATCH_ID_RECIPE
     */

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCraftQuartzElevator(TestContext context) {
        RecipeInputInventory inventory = new CraftingInventory(new ModTestScreenHander(), 3, 3);
        List<ItemStack> inputStacks = Arrays.asList(
                ItemStack.EMPTY, new ItemStack(Items.QUARTZ), ItemStack.EMPTY,
                new ItemStack(Items.QUARTZ), new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.QUARTZ),
                ItemStack.EMPTY, new ItemStack(Items.QUARTZ), ItemStack.EMPTY);
        for (int i = 0; i < inputStacks.size(); i++) {
            inventory.setStack(i, inputStacks.get(i));
        }

        ItemStack result = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);

        testCraftElevator(context, RecipeType.CRAFTING, inventory, result);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCraftQuartzElevatorFromQuartzBlock(TestContext context) {
        RecipeInputInventory inventory = new CraftingInventory(new ModTestScreenHander(), 3, 3);
        List<ItemStack> inputStacks = Arrays.asList(
                new ItemStack(Items.QUARTZ_BLOCK),
                new ItemStack(Items.ENDER_PEARL));
        for (int i = 0; i < inputStacks.size(); i++) {
            inventory.setStack(i, inputStacks.get(i));
        }

        ItemStack result = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);

        testCraftElevator(context, RecipeType.CRAFTING, inventory, result);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSmeltingSmoothQuartzElevator(TestContext context) {
        Inventory inventory = new SimpleInventory(new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK));

        ItemStack result = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

        testCraftElevator(context, RecipeType.SMELTING, inventory, result);
    }

    @GameTest(batchId = QuartzElevatorModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCraftSmoothQuartzElevatorFromSmoothQuartzBlock(TestContext context) {
        RecipeInputInventory inventory = new CraftingInventory(new ModTestScreenHander(), 3, 3);
        List<ItemStack> inputStacks = Arrays.asList(
                new ItemStack(Items.SMOOTH_QUARTZ),
                new ItemStack(Items.ENDER_PEARL));
        for (int i = 0; i < inputStacks.size(); i++) {
            inventory.setStack(i, inputStacks.get(i));
        }

        ItemStack result = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

        testCraftElevator(context, RecipeType.CRAFTING, inventory, result);
    }
}
