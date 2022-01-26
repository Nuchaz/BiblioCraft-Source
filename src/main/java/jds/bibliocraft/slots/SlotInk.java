package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerPrintPress;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class SlotInk extends Slot
{
	final ContainerPrintPress printpress;
	
	public SlotInk(ContainerPrintPress pressContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.printpress = pressContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//Item potionTest = stack.getItem();
		Item inktest = stack.getItem();
		if (inktest instanceof ItemDye)
		{
			return true;
			
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
