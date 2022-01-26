package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerClipboard;
import jds.bibliocraft.items.ItemClipboard;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotClipboard extends Slot
{
	final ContainerClipboard container;
	
	public SlotClipboard(ContainerClipboard clipboardContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i , j, k);
		this.container = clipboardContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemClipboard)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}
