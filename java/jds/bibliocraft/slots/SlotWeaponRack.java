package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerWeaponRack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotWeaponRack extends Slot
{

	final ContainerWeaponRack weaponRack;
	
	public SlotWeaponRack(ContainerWeaponRack toolContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.weaponRack = toolContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		Item toolTest = stack.getItem();
		
		//return true;
			
		if (ContainerWeaponRack.isItemTool(toolTest, stack))
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
