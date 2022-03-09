
package co.nickxwlm.minetts.item.crafting;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;

import co.nickxwlm.minetts.item.ItemPieceLinOre;
import co.nickxwlm.minetts.item.ItemLineziumingot;
import co.nickxwlm.minetts.ElementsMinettsMod;

@ElementsMinettsMod.ModElement.Tag
public class RecipeLineziumingotcraft extends ElementsMinettsMod.ModElement {
	public RecipeLineziumingotcraft(ElementsMinettsMod instance) {
		super(instance, 35);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		GameRegistry.addSmelting(new ItemStack(ItemPieceLinOre.block, (int) (1)), new ItemStack(ItemLineziumingot.block, (int) (1)), 5F);
	}
}
