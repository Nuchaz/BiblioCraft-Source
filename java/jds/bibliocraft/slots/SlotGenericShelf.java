package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerGenericShelf;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotGenericShelf extends Slot
{

	final ContainerGenericShelf genericShelf;
	
	public SlotGenericShelf(ContainerGenericShelf shelfContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.genericShelf = shelfContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		Item potionTest = stack.getItem();
		
		return true;
		
			/*
			if (potionTest instanceof ItemPotion)
			{
				return true;
			}
			else
			{
				return false;
			}
			*/
		
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
}
