package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerFancySign;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFancySign extends Slot
{
	final ContainerFancySign sign;
	
	public SlotFancySign(ContainerFancySign container, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.sign = container;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

}
