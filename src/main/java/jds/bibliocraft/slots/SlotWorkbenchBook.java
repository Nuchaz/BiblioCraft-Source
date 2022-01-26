package jds.bibliocraft.slots;

import jds.bibliocraft.Config;
import jds.bibliocraft.containers.ContainerFancyWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWorkbenchBook extends Slot
{
	final ContainerFancyWorkbench container;
	public SlotWorkbenchBook(ContainerFancyWorkbench bench, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.container = bench;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		
		if (Config.testBookValidity(stack))
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
