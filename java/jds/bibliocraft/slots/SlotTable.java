package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerTable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTable extends Slot
{
	final ContainerTable table;
	
	public SlotTable(ContainerTable tableContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.table = tableContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//Item toolTest = stack.getItem();
		
		return true;
		/*
		if (weaponCase.isItemTool(toolTest, stack))
		{
			return true;
		}
		else
		{
			return false;
		}
		*/
		
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
}
