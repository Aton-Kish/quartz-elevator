package atonkish.quartzelv.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
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
          add(
              RecipeTests.createTest(
                  "Craft Quartz Elevator",
                  RecipeType.CRAFTING,
                  new LazyCraftingInput(
                      3,
                      3,
                      List.of(
                          Items.AIR,
                          Items.QUARTZ,
                          Items.AIR,
                          Items.QUARTZ,
                          Items.ENDER_PEARL,
                          Items.QUARTZ,
                          Items.AIR,
                          Items.QUARTZ,
                          Items.AIR)),
                  ModItems.QUARTZ_ELEVATOR_BLOCK));

          add(
              RecipeTests.createTest(
                  "Craft Quartz Elevator from Quartz Block",
                  RecipeType.CRAFTING,
                  new LazyCraftingInput(
                      2, 2, List.of(Items.QUARTZ_BLOCK, Items.ENDER_PEARL, Items.AIR, Items.AIR)),
                  ModItems.QUARTZ_ELEVATOR_BLOCK));

          // Smooth Quartz Elevator
          add(
              RecipeTests.createTest(
                  "Smelting Smooth Quartz Elevator",
                  RecipeType.SMELTING,
                  new LazySingleRecipeInput(ModItems.QUARTZ_ELEVATOR_BLOCK),
                  ModItems.SMOOTH_QUARTZ_ELEVATOR));

          add(
              RecipeTests.createTest(
                  "Craft Smooth Quartz Elevator from Smooth Quartz Block",
                  RecipeType.CRAFTING,
                  new LazyCraftingInput(
                      2, 2, List.of(Items.SMOOTH_QUARTZ, Items.ENDER_PEARL, Items.AIR, Items.AIR)),
                  ModItems.SMOOTH_QUARTZ_ELEVATOR));
        }
      };

  private static <I extends RecipeInput, T extends Recipe<I>> TestFunction createTest(
      String name, RecipeType<T> type, LazyRecipeInput<I> lazyInput, Item expectedItem) {
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
        1,
        (context) -> {
          // Arrange
          I input = lazyInput.load();
          ItemStack expected = new ItemStack(expectedItem);

          ServerLevel world = context.getLevel();
          RecipeManager recipeManager = world.recipeAccess();
          T recipe = recipeManager.getRecipeFor(type, input, world).orElseThrow().value();

          // Act
          ItemStack actual = recipe.assemble(input);

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

  interface LazyRecipeInput<I extends RecipeInput> {
    I load();
  }

  record LazyCraftingInput(int width, int height, List<Item> items)
      implements LazyRecipeInput<CraftingInput> {
    public CraftingInput load() {
      return CraftingInput.of(
          this.width, this.height, this.items.stream().map(item -> new ItemStack(item)).toList());
    }
  }

  record LazySingleRecipeInput(Item item) implements LazyRecipeInput<SingleRecipeInput> {
    public SingleRecipeInput load() {
      return new SingleRecipeInput(new ItemStack(this.item));
    }
  }
}
