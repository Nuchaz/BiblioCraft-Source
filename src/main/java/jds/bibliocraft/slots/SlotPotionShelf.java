package jds.bibliocraft.slots;

import jds.bibliocraft.Config;
import jds.bibliocraft.containers.ContainerPotionShelf;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotPotionShelf extends Slot
{
	final ContainerPotionShelf potionShelf;
	
	public SlotPotionShelf(ContainerPotionShelf potionContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.potionShelf = potionContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		Item potionTest = stack.getItem();
		String potDisplayName = potionTest.getItemStackDisplayName(stack);
				//getItemDisplayName(stack);
		String potName = stack.getUnlocalizedName();

		if (Config.testPotionValidity(potName, potDisplayName, potionTest))
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
		// I need to decide if I want to allow stacks of essense or whatever.
        return 64;
    }

}
