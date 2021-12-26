package jds.bibliocraft.slots;

import jds.bibliocraft.Config;
import jds.bibliocraft.containers.ContainerBookcase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotBook extends Slot
{
	final ContainerBookcase bookCase;

	public SlotBook(ContainerBookcase bookContainer, IInventory iInventory, int i, int j, int k) 
	{
		super(iInventory, i, j, k);
		this.bookCase = bookContainer;
	}
	

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		/* TODO temp removed
		if (Config.testBookValidity(stack))
		{
			
			return true;
		}
		else
		{
			return false;
		}*/
		return true;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }

}
