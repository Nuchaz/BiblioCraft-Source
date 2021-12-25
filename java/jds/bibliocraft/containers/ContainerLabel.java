package jds.bibliocraft.containers;

import jds.bibliocraft.slots.SlotLabel;
import jds.bibliocraft.tileentities.TileEntityLabel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLabel extends Container
{
	
	protected TileEntityLabel tileEntity;
	protected SlotLabel labelSlot;
	
	public ContainerLabel(InventoryPlayer inventoryPlayer, TileEntityLabel tile)
	{
		tileEntity = tile;
		
		addSlotToContainer(this.labelSlot = new SlotLabel(this,tileEntity, 0, 80, 45));
		addSlotToContainer(this.labelSlot = new SlotLabel(this,tileEntity, 1, 35, 26));
		addSlotToContainer(this.labelSlot = new SlotLabel(this,tileEntity, 2, 125, 26));
		
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
	//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			//Item potionTest = stack.getItem();


			if (slot < 3)
			{
				if (!this.mergeItemStack(stackInSlot, 3, 39, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory

			else if (!this.mergeItemStack(stackInSlot, 0, 3, false)) // I would like to make this go to the second slot first. What if I just switched the slot position?
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
}
