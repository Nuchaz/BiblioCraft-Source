package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerDinnerPlate;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class SlotFood extends Slot
{
	final ContainerDinnerPlate plate;
	
	public SlotFood(ContainerDinnerPlate plateContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.plate = plateContainer;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemFood)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
