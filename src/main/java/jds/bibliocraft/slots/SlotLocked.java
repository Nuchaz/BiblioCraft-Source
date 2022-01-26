package jds.bibliocraft.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotLocked extends Slot
{
	
	public SlotLocked(IInventory iInventory, int id, int x, int y)
	{
		super(iInventory, id, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
}
