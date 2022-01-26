package jds.bibliocraft.containers;

import jds.bibliocraft.Config;
import jds.bibliocraft.slots.SlotWritingDeskBooks;
import jds.bibliocraft.tileentities.TileEntityDesk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWritingDesk extends Container
{
	protected TileEntityDesk tileEntity;
	//protected SlotWrittenBook writtenBookSlot;
	protected SlotWritingDeskBooks bookStacks;
	
	public ContainerWritingDesk(InventoryPlayer inventoryPlayer, TileEntityDesk tile)
	{
		tileEntity = tile;
		
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 0, 80, 36)); 
		
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 1, 36, 62));
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 2, 36, 44));
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 3, 36, 26));
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 4, 36, 8));
		
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 5, 126, 62));
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 6, 126, 44));
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 7, 126, 26));
		addSlotToContainer(this.bookStacks = new SlotWritingDeskBooks(this, tileEntity, 8, 126, 8));
		
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


			if (slot < 9)
			{
				if (!this.mergeItemStack(stackInSlot, 9, 45, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!Config.isBlock(stack) && Config.testBookValidity(stack) && !this.mergeItemStack(stackInSlot, 0, 9, false))
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
