package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerArmor;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;


public class SlotArmorHelm extends Slot
{

	final ContainerArmor armorStand;
	
	public SlotArmorHelm(ContainerArmor armorContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.armorStand = armorContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//armorStand.
		if (stack == ItemStack.EMPTY)
		{
			return false;
		}
		//Item helmItem = stack.getItem();
		// here is where we can setup conditions to test if an item is valid
		
		Item helmItem = stack.getItem();
		//System.out.println(helmItem.getItemDisplayName(stack));
		if (helmItem instanceof ItemSkull || Block.isEqualTo(Block.getBlockFromItem(helmItem), Blocks.PUMPKIN))//helmItem == Blocks.pumpkin.getIdFromBlock(BlockPumpkin))//stack.itemID == 86)
		{
			return true;
		}
		//ItemStack pumpkinTest = new ItemStack(Block.pumpkin, 1, 0);
		//System.out.println("try try");
		if (helmItem instanceof ItemArmor)
		{
			ItemArmor armorHelm = (ItemArmor)helmItem;
			EntityEquipmentSlot armorType = armorHelm.armorType;
			if (armorType == EntityEquipmentSlot.HEAD)
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
		
		//return true;
	}
	
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}
