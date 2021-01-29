package atonkish.quartzelv;

import atonkish.quartzelv.blocks.QuartzElevatorBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class QuartzElevatorMod implements ModInitializer {
	public static final String MOD_ID = "quartzelv";

	public static final QuartzElevatorBlock QUARTZ_ELEVATOR_BLOCK = new QuartzElevatorBlock(
			FabricBlockSettings.copy(Blocks.QUARTZ_BLOCK));

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "quartz_elevator"), QUARTZ_ELEVATOR_BLOCK);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "quartz_elevator"),
				new BlockItem(QUARTZ_ELEVATOR_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS)));
	}
}
