package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Rotation;
import atonkish.quartzelv.QuartzElevatorMod;
import atonkish.quartzelv.gametest.util.TestFunction;
import atonkish.quartzelv.gametest.util.TestIdentifier;
import atonkish.quartzelv.item.ModItems;

public class RecipeTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:recipe/default", QuartzElevatorMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Quartz Elevator
          {
            ItemStack quartz = new ItemStack(Items.QUARTZ);
            ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
            ItemStack elevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);

            add(
                RecipeTests.createTest(
                    "Craft Quartz Elevator",
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            ItemStack.EMPTY,
                            quartz,
                            ItemStack.EMPTY,
                            quartz,
                            enderPearl,
                            quartz,
                            ItemStack.EMPTY,
                            quartz,
                            ItemStack.EMPTY)),
                    elevator));
          }

          {
            ItemStack quartzBlock = new ItemStack(Items.QUARTZ_BLOCK);
            ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
            ItemStack elevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);

            add(
                RecipeTests.createTest(
                    "Craft Quartz Elevator from Quartz Block",
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        2, 2, List.of(quartzBlock, enderPearl, ItemStack.EMPTY, ItemStack.EMPTY)),
                    elevator));
          }

          // Smooth Quartz Elevator
          {
            ItemStack baseElevator = new ItemStack(ModItems.QUARTZ_ELEVATOR_BLOCK);
            ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

            add(
                RecipeTests.createTest(
                    "Smelting Smooth Quartz Elevator",
                    RecipeType.SMELTING,
                    new SingleRecipeInput(baseElevator),
                    elevator));
          }

          {
            ItemStack smoothQuartz = new ItemStack(Items.SMOOTH_QUARTZ);
            ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
            ItemStack elevator = new ItemStack(ModItems.SMOOTH_QUARTZ_ELEVATOR);

            add(
                RecipeTests.createTest(
                    "Craft Smooth Quartz Elevator from Smooth Quartz Block",
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        2, 2, List.of(smoothQuartz, enderPearl, ItemStack.EMPTY, ItemStack.EMPTY)),
                    elevator));
          }
        }
      };

  private static <I extends RecipeInput, T extends Recipe<I>> TestFunction createTest(
      String name, RecipeType<T> type, I input, ItemStack expected) {
    Identifier testIdentifier =
        TestIdentifier.of(QuartzElevatorMod.MOD_ID, RecipeTests.class, name);

    return new TestFunction(
        testIdentifier,
        RecipeTests.TEST_ENVIRONMENT_DEFAULT,
        RecipeTests.TEST_STRUCTURE_EMPTY,
        20,
        0,
        true,
        Rotation.NONE,
        false,
        1,
        1,
        false,
        (context) -> {
          // Arrange
          ServerLevel world = context.getLevel();
          RecipeManager recipeManager = world.recipeAccess();
          RegistryAccess registryManager = world.registryAccess();
          T recipe = recipeManager.getRecipeFor(type, input, world).orElseThrow().value();

          // Act
          ItemStack actual = recipe.assemble(input, registryManager);

          // Assert
          try {
            context.assertTrue(
                ItemStack.matches(actual, expected),
                Component.nullToEmpty("Recipe result differs from expected."));
          } catch (Exception e) {
            QuartzElevatorMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
            throw e;
          }

          context.succeed();
        });
  }
}
