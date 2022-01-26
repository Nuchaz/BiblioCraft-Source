package jds.bibliocraft.containers;

import jds.bibliocraft.slots.SlotCookie;
import jds.bibliocraft.tileentities.TileEntityCookieJar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCookieJar extends Container
{
	protected TileEntityCookieJar cookiejar;
	protected SlotCookie cookieSlot;
	
	public ContainerCookieJar(InventoryPlayer inventoryPlayer, TileEntityCookieJar tile)
	{
		cookiejar = tile;
		
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 0, 32, 22));
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 1, 64, 22));
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 2, 96, 22));
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 3, 128, 22));
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 4, 32, 51));
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 5, 64, 51));
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 6, 96, 51));
		addSlotToContainer(this.cookieSlot = new SlotCookie(this, cookiejar, 7, 128, 51));
		
		bindPlayerInventory(inventoryPlayer);
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
	public boolean canInteractWith(EntityPlayer player)
	{
		return cookiejar.isUsableByPlayer(player);
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
			if (slot < 8)
			{
				if (!this.mergeItemStack(stackInSlot, 8, 44, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(stackInSlot, 0, 8, false)) // use this line to limit what can be shift-clicked into place
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
