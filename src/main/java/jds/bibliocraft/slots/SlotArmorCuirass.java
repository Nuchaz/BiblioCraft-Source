package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class SlotArmorCuirass extends Slot
{
final ContainerArmor armorStand;
	
	public SlotArmorCuirass(ContainerArmor armorContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.armorStand = armorContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		// here is where we can setup conditions to test if an item is valid
		if (stack == ItemStack.EMPTY)
		{
			return false;
		}
		//
		Item chestItem = stack.getItem();
		
		if (chestItem instanceof ItemArmor)
		{
			ItemArmor armorChest = (ItemArmor)chestItem;
			EntityEquipmentSlot armorType = armorChest.armorType;
			if (armorType == EntityEquipmentSlot.CHEST)
			{
			return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}

