package jds.bibliocraft.containers;

import jds.bibliocraft.tileentities.TileEntityTypeMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerTypeMachine extends Container
{

	protected TileEntityTypeMachine tileEntity;
	//protected SlotChase chaseSlot;
	//protected SlotPlate plateSlot;
	//protected SlotSignedBook bookSlot;

	
	public ContainerTypeMachine(TileEntityTypeMachine tile)
	{
		tileEntity = tile;
		// Add the slots!
		//addSlotToContainer(this.bookSlot = new SlotSignedBook(this, tileEntity, 0, 26, 8));
		//addSlotToContainer(this.bookSlot = new SlotSignedBook(this, tileEntity, 0, 26, 62));
		//addSlotToContainer(this.chaseSlot = new SlotChase(this, tileEntity, 1, 134, 8));
		//addSlotToContainer(this.plateSlot = new SlotPlate(this, tileEntity, 2, 134, 62));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUsableByPlayer(player);
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


			if (slot < 3)
			{
				if (!this.mergeItemStack(stackInSlot, 2, 38, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory

			// This needs some tweakage to accomadate my needs, but will work genericly for now
			else if (!this.mergeItemStack(stackInSlot, 0, 2, false)) // use this line to limit what can be shift-clicked into place
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
