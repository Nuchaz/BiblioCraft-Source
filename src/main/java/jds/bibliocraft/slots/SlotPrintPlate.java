package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerPrintPress;
import jds.bibliocraft.items.ItemPlate;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotPrintPlate extends Slot
{

	final ContainerPrintPress printpress;
	
	public SlotPrintPlate(ContainerPrintPress pressContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.printpress = pressContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//Item potionTest = stack.getItem();
		Item plateTest = stack.getItem();
		if (plateTest instanceof ItemPlate)
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
        return 1;
    }

	
}
