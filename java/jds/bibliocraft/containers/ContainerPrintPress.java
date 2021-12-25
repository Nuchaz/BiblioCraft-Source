package jds.bibliocraft.containers;

import jds.bibliocraft.slots.SlotBlankBook;
import jds.bibliocraft.slots.SlotFinishedBook;
import jds.bibliocraft.slots.SlotInk;
import jds.bibliocraft.slots.SlotPrintPlate;
import jds.bibliocraft.tileentities.TileEntityPrintPress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerPrintPress  extends Container
{

	protected TileEntityPrintPress tileEntity;
	
	protected SlotPrintPlate plateSlot;
	protected SlotBlankBook bookSlot;
	protected SlotFinishedBook doneSlot;
	protected SlotInk inkSlot;
	
	public ContainerPrintPress(InventoryPlayer inventoryPlayer, TileEntityPrintPress tile)
	{
		tileEntity = tile;
		// Add the slots!
		addSlotToContainer(this.inkSlot = new SlotInk(this, tileEntity, 0, 80, 8));
		addSlotToContainer(this.plateSlot = new SlotPrintPlate(this, tileEntity, 1, 80, 29));
		addSlotToContainer(this.bookSlot = new SlotBlankBook(this, tileEntity, 2, 36, 54));
		addSlotToContainer(this.doneSlot = new SlotFinishedBook(this, tileEntity, 3, 123, 54));
		
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
			Item potionTest = stack.getItem();


			if (slot < 4)
			{
				if (!this.mergeItemStack(stackInSlot, 4, 40, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory

			// This needs some tweakage to accomadate my needs, but will work genericly for now
			else if (!this.mergeItemStack(stackInSlot, 0, 4, false)) // use this line to limit what can be shift-clicked into place
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
