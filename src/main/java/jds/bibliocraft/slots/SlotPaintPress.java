package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerPaintPress;
import jds.bibliocraft.items.ItemPaintingCanvas;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPaintPress extends Slot
{
	final ContainerPaintPress paintPress;
	
	public SlotPaintPress(ContainerPaintPress paintPressContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.paintPress = paintPressContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
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
