package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerSlottedBook;
import jds.bibliocraft.items.ItemSlottedBook;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSlottedBook extends Slot
{
	private ContainerSlottedBook slottedContainer;
	
	public SlotSlottedBook(ContainerSlottedBook container, IInventory iInventory, int x, int y, int z)
	{
		super(iInventory, x, y, z);
		slottedContainer = container;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return slottedBookCheck(stack);
	}
	
	public static boolean slottedBookCheck(ItemStack book)
	{
		if (book != ItemStack.EMPTY)
		{
			if (book.getItem() instanceof ItemSlottedBook)
			{
				return false;
			}
		}
		return true;
	}
}
