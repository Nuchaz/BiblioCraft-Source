package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerWeaponCase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotWeaponCase extends Slot
{
	final ContainerWeaponCase weaponCase;
	
	public SlotWeaponCase(ContainerWeaponCase caseContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.weaponCase = caseContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		Item toolTest = stack.getItem();
		
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
