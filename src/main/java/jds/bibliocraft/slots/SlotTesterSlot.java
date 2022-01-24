package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerNameTester;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTesterSlot extends Slot
{

	private ContainerNameTester container;
	
	public SlotTesterSlot(ContainerNameTester itemContainer, IInventory inventory, int i, int j, int k)
	{
		super(inventory, i, j, k);
		this.container = itemContainer;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}

}
