
package co.nickxwlm.minetts.fuel;

import net.minecraft.item.ItemStack;

import co.nickxwlm.minetts.item.ItemWirecutt;
import co.nickxwlm.minetts.ElementsMinettsMod;

@ElementsMinettsMod.ModElement.Tag
public class FuelWirecutted extends ElementsMinettsMod.ModElement {
	public FuelWirecutted(ElementsMinettsMod instance) {
		super(instance, 41);
	}

	@Override
	public int addFuel(ItemStack fuel) {
		if (fuel.getItem() == new ItemStack(ItemWirecutt.block, (int) (1)).getItem())
			return 240;
		return 0;
	}
}
