package jds.bibliocraft.containers;

import jds.bibliocraft.slots.SlotClipboard;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerClipboard extends Container 
{
	private TileEntityClipboard tileEntity;
	private SlotClipboard slotClippy;
	
	public ContainerClipboard(InventoryPlayer inventoryPlayer, TileEntityClipboard tile)
	{
		this.tileEntity = tile;
		
		addSlotToContainer(this.slotClippy = new SlotClipboard(this, tileEntity, 0, 20, 20));
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
			
			if (slot < 1) // changing 9 to 6
			{
				if (!this.mergeItemStack(stackInSlot, 1, 37, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(stackInSlot, 0, 1, false))  //&& stackTest.getItemStackLimit() == 1
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
