package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerPainting;
import jds.bibliocraft.items.ItemPaintingCanvas;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPainting extends Slot
{
	final ContainerPainting painting;
	
	public SlotPainting(ContainerPainting paintingContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.painting = paintingContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//System.out.println("test");
		if (stack != ItemStack.EMPTY)
		{
			if (stack.getItem() instanceof ItemPaintingCanvas)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
