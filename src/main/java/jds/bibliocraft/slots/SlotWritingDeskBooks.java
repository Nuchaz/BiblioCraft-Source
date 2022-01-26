package jds.bibliocraft.slots;

import jds.bibliocraft.Config;
import jds.bibliocraft.containers.ContainerWritingDesk;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWritingDeskBooks extends Slot
{
	final ContainerWritingDesk writingDesk;
	
	public SlotWritingDeskBooks(ContainerWritingDesk deskContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.writingDesk = deskContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		
		if (!Config.isBlock(stack) && Config.testBookValidity(stack))
		{
			
			return true; // I removed the empty book restriction on the desk
			/*
			if (Config.allowEmptyBooks == false && stackTest instanceof ItemBook)
			{
				return false;
			}
			else
			{
				return true;
			}
			*/
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
}
