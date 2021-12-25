package jds.bibliocraft.containers;

import jds.bibliocraft.Config;
import jds.bibliocraft.slots.SlotWeaponRack;
import jds.bibliocraft.tileentities.TileEntityToolRack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class ContainerWeaponRack extends Container
{
	protected TileEntityToolRack tileEntity;
	protected SlotWeaponRack toolSlot;
	
	public ContainerWeaponRack(InventoryPlayer inventoryPlayer, TileEntityToolRack tile)
	{
		tileEntity = tile;
		
		addSlotToContainer(this.toolSlot = new SlotWeaponRack(this, tileEntity, 0, 53, 15)); 
		addSlotToContainer(this.toolSlot = new SlotWeaponRack(this, tileEntity, 1, 107, 15));
		addSlotToContainer(this.toolSlot = new SlotWeaponRack(this, tileEntity, 2, 53, 53));
		addSlotToContainer(this.toolSlot = new SlotWeaponRack(this, tileEntity, 3, 107, 53));
		
		bindPlayerInventory(inventoryPlayer);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUsableByPlayer(player);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 84+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18,142));
		}
	}



@Override
public ItemStack transferStackInSlot(EntityPlayer player, int slot)
{
	ItemStack stack = ItemStack.EMPTY;
	Slot slotObject = (Slot) inventorySlots.get(slot);

	if (slotObject != null && slotObject.getHasStack())
	{
		ItemStack stackInSlot = slotObject.getStack();
		stack = stackInSlot.copy();
		Item toolTest = stack.getItem();


		if (slot < 4)
		{
			if (!this.mergeItemStack(stackInSlot, 4, 40, true))  // changing 9 to 6
			{
				return ItemStack.EMPTY;
			}
		}
		else if (isItemTool(toolTest, stack) && !this.mergeItemStack(stackInSlot, 0, 4, false))
		{
			return ItemStack.EMPTY;
		}

		
		if (stackInSlot.getCount() == 0)
		{
			slotObject.putStack(ItemStack.EMPTY);
			
		} else 
		{
			slotObject.onSlotChanged();
		}
		
		if (stackInSlot.getCount() == stack.getCount())
		{
			return ItemStack.EMPTY;
		}
		slotObject.onTake(player, stackInSlot);
	}
	return stack;
}

public static boolean isItemTool(Item tool, ItemStack stack)
{
	String toolName = tool.getItemStackDisplayName(stack);
	String toolcodeName = stack.getUnlocalizedName().toLowerCase();
	if (tool instanceof ItemTool || tool instanceof ItemSword || tool instanceof ItemBow || tool instanceof ItemHoe || tool instanceof ItemFishingRod || tool instanceof ItemShears || tool instanceof ItemFlintAndSteel || Config.testToolValidity(toolName, toolcodeName))
	{
		return true;
	}
	else
	{
		return false;
	}
}

}