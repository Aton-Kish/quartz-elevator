package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
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
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;

import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.gametest.util.TestFunction;
import atonkish.quartzelv.gametest.util.TestIdentifier;
import atonkish.quartzelv.item.ModItems;

public class RecipeTests {
    private static final String TEST_ENVIRONMENT_DEFAULT = String.format("%s:recipe/default",
            QuartzElevatorMod.MOD_ID);
    private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

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
                        CraftingRecipeInput.create(3, 3, List.of(
                                ItemStack.EMPTY, quartz, ItemStack.EMPTY,
                                quartz, enderPearl, quartz,
                                ItemStack.EMPTY, quartz, ItemStack.EMPTY)),
                        elevator));
            }

            {
                ItemStack quartzBlock = new ItemStack(Items.QUARTZ_BLOCK);
                ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
                ItemStack elevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);

                add(RecipeTests.createTest(
                        "Craft Quartz Elevator from Quartz Block",
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(2, 2, List.of(
                                quartzBlock, enderPearl,
                                ItemStack.EMPTY, ItemStack.EMPTY)),
                        elevator));
            }

            // Smooth Quartz Elevator
            {
                ItemStack baseElevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);
                ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

                add(RecipeTests.createTest(
                        "Smelting Smooth Quartz Elevator",
                        RecipeType.SMELTING,
                        new SingleStackRecipeInput(baseElevator),
                        elevator));
            }

            {
                ItemStack smoothQuartz = new ItemStack(Items.SMOOTH_QUARTZ);
                ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
                ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

                add(RecipeTests.createTest(
                        "Craft Smooth Quartz Elevator from Smooth Quartz Block",
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(2, 2, List.of(
                                smoothQuartz, enderPearl,
                                ItemStack.EMPTY, ItemStack.EMPTY)),
                        elevator));
            }
        }
    };

    private static <I extends RecipeInput, T extends Recipe<I>> TestFunction createTest(String name,
            RecipeType<T> type,
            I input,
            ItemStack expected) {
        Identifier testIdentifier = TestIdentifier.of(QuartzElevatorMod.MOD_ID,
                RecipeTests.class,
                name);

        return new TestFunction(
                testIdentifier,
                RecipeTests.TEST_ENVIRONMENT_DEFAULT,
                RecipeTests.TEST_STRUCTURE_EMPTY,
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
                        QuartzElevatorMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }
}
