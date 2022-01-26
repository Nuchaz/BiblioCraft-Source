package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerFancyWorkbench;
import jds.bibliocraft.items.ItemRecipeBook;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;

public class SlotFancyWorkbench extends Slot
{

	final ContainerFancyWorkbench bench;
	private boolean recipeslot = false;
	
	public SlotFancyWorkbench(ContainerFancyWorkbench container, IInventory iInventory, int i, int j, int k, boolean isrecipeslot)
	{
		super(iInventory, i, j, k);
		this.bench = container;
		this.recipeslot = isrecipeslot;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if (stack != ItemStack.EMPTY)
		{
			if (stack.getItem() instanceof ItemBook || stack.getItem() instanceof ItemRecipeBook)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		if (recipeslot)
		{
			return 1;
		}
		else
		{
			return 64;
		}
	}
}
