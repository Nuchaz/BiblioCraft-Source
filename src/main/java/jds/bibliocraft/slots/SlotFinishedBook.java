package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerPrintPress;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFinishedBook extends Slot
{

	final ContainerPrintPress printpress;
	
	public SlotFinishedBook(ContainerPrintPress pressContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.printpress = pressContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//Item potionTest = stack.getItem();
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }

	
}
