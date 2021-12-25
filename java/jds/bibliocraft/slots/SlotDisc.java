package jds.bibliocraft.slots;

import jds.bibliocraft.Config;
import jds.bibliocraft.containers.ContainerDiscRack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;

public class SlotDisc extends Slot
{
	final ContainerDiscRack rack;
	
	public SlotDisc(ContainerDiscRack discRackContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.rack = discRackContainer;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		String discName = stack.getUnlocalizedName().toLowerCase();
		if (stack.getItem() instanceof ItemRecord || Config.testDiscValidity(discName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
