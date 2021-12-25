package jds.bibliocraft.containers;

import jds.bibliocraft.Config;
import jds.bibliocraft.slots.SlotPotionShelf;
import jds.bibliocraft.tileentities.TileEntityPotionShelf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerPotionShelf extends Container
{
	
	protected TileEntityPotionShelf tileEntity;
	protected SlotPotionShelf potionSlot;
	
	public ContainerPotionShelf(InventoryPlayer inventoryPlayer, TileEntityPotionShelf tile)
	{
		tileEntity = tile;
		
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 0, 53, 15)); 
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 1, 71, 15));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 2, 89, 15));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 3, 107, 15));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 4, 53, 34));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 5, 71, 34));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 6, 89, 34));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 7, 107, 34));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 8, 53, 53));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 9, 71, 53));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 10, 89, 53));
		addSlotToContainer(this.potionSlot = new SlotPotionShelf(this, tileEntity, 11, 107, 53));
		
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
			String potDisplayName = stack.getDisplayName();
			String potName =stack.getUnlocalizedName();
		    //potName = potionTest.toString();


			if (slot < 12)
			{
				if (!this.mergeItemStack(stackInSlot, 12, 48, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory

			else if (Config.testPotionValidity(potName, potDisplayName, potionTest) && !this.mergeItemStack(stackInSlot, 0, 12, false  ))
			{
				return ItemStack.EMPTY;
			}

			// potionTest instanceof ItemPotion && !this.mergeItemStack(stackInSlot, 0, 12, false) ||
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
