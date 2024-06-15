package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

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
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.gametest.util.VoidScreenHander;
import atonkish.quartzelv.item.ModItems;

public class RecipeTests {
    public static final String BATCH_ID = QuartzElevatorMod.MOD_ID + ":LootTableBatch";

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Quartz Elevator
            {
                ItemStack quartz = new ItemStack(Items.QUARTZ);
                ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
                ItemStack elevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);

                add(RecipeTests.createTest(
                        "Craft Quartz Elevator",
                        RecipeType.CRAFTING,
                        RecipeTests.create3x3CraftingInventory(
                                ItemStack.EMPTY, quartz, ItemStack.EMPTY,
                                quartz, enderPearl, quartz,
                                ItemStack.EMPTY, quartz, ItemStack.EMPTY),
                        elevator));
            }

            {
                ItemStack quartzBlock = new ItemStack(Items.QUARTZ_BLOCK);
                ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
                ItemStack elevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);

                add(RecipeTests.createTest(
                        "Craft Quartz Elevator from Quartz Block",
                        RecipeType.CRAFTING,
                        RecipeTests.create3x3CraftingInventory(
                                quartzBlock, enderPearl, ItemStack.EMPTY,
                                ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                                ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY),
                        elevator));
            }

            // Smooth Quartz Elevator
            {
                ItemStack baseElevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);
                ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

                add(RecipeTests.createTest(
                        "Smelting Smooth Quartz Elevator",
                        RecipeType.SMELTING,
                        new SimpleInventory(baseElevator),
                        elevator));
            }

            {
                ItemStack smoothQuartz = new ItemStack(Items.SMOOTH_QUARTZ);
                ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
                ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

                add(RecipeTests.createTest(
                        "Craft Smooth Quartz Elevator from Smooth Quartz Block",
                        RecipeType.CRAFTING,
                        RecipeTests.create3x3CraftingInventory(
                                smoothQuartz, enderPearl, ItemStack.EMPTY,
                                ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                                ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY),
                        elevator));
            }
        }
    };

    private static <C extends Inventory, T extends Recipe<C>> TestFunction createTest(String name, RecipeType<T> type,
            C inventory, ItemStack expected) {
        String testName = String.format("%s %s %s",
                QuartzElevatorMod.MOD_ID,
                RecipeTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                RecipeTests.BATCH_ID,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                100,
                0L,
                true,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    ServerWorld world = context.getWorld();
                    RecipeManager recipeManager = world.getRecipeManager();
                    DynamicRegistryManager registryManager = world.getRegistryManager();
                    T recipe = recipeManager.getFirstMatch(type, inventory, world).orElseThrow().value();

                    // Act
                    ItemStack actual = recipe.craft(inventory, registryManager);

                    // Assert
                    try {
                        context.assertTrue(ItemStack.areEqual(actual, expected),
                                "Recipe result differs from expected.");
                    } catch (Exception e) {
                        QuartzElevatorMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }

    private static RecipeInputInventory create3x3CraftingInventory(
            ItemStack stack1, ItemStack stack2, ItemStack stack3,
            ItemStack stack4, ItemStack stack5, ItemStack stack6,
            ItemStack stack7, ItemStack stack8, ItemStack stack9) {
        RecipeInputInventory inventory = new CraftingInventory(new VoidScreenHander(), 3, 3);
        inventory.setStack(0, stack1);
        inventory.setStack(1, stack2);
        inventory.setStack(2, stack3);
        inventory.setStack(3, stack4);
        inventory.setStack(4, stack5);
        inventory.setStack(5, stack6);
        inventory.setStack(6, stack7);
        inventory.setStack(7, stack8);
        inventory.setStack(8, stack9);
        return inventory;
    }
}
