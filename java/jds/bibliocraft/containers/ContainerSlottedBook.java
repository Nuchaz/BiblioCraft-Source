package jds.bibliocraft.containers;

import jds.bibliocraft.items.ItemSlottedBook;
import jds.bibliocraft.slots.SlotLocked;
import jds.bibliocraft.slots.SlotSlottedBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.Constants;


public class ContainerSlottedBook extends Container
{
	
	private InventoryBasic inventory;
	public ItemStack bookStack;
	private SlotSlottedBook bookslot;
	
	public ContainerSlottedBook(InventoryPlayer inventoryPlayer)
	{
		inventory = new InventoryBasic("BookInventory", false, 1);
		bookStack = inventoryPlayer.getCurrentItem();
		if (bookStack != ItemStack.EMPTY && bookStack.getItem() instanceof ItemSlottedBook)
		{
			// LOAD NBT into inventory
			NBTTagCompound tags = bookStack.getTagCompound();
			if (tags != null)
			{
				NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < tagList.tagCount(); i++)
				{
					
					NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
					byte slot = tag.getByte("Slot");
					if (slot >= 0 && slot < inventory.getSizeInventory())
					{
						ItemStack invStack = new ItemStack(tag);
						this.inventory.setInventorySlotContents(slot, invStack);
					}
				}
			}
		}
		
		addSlotToContainer(this.bookslot = new SlotSlottedBook(this, inventory, 0, 80, -3));
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return true;
	}
	
    @Override
    public void onContainerClosed(EntityPlayer player) 
    {
    	// Save here
    	updateNBT(player);
    }
    
    private void updateNBT(EntityPlayer player)
    {
    	ItemStack newStack = player.getHeldItem(EnumHand.MAIN_HAND);
    	if (newStack != ItemStack.EMPTY && newStack.getItem() instanceof ItemSlottedBook)
    	{
    		NBTTagCompound tags = newStack.getTagCompound();
         	if (tags == null)
         	{
         		tags = new NBTTagCompound();
         	}
        	NBTTagList itemList = new NBTTagList();
        	for (int i = 0; i < inventory.getSizeInventory(); i++)
        	{
        		ItemStack stack = inventory.getStackInSlot(i);
        		if (stack != ItemStack.EMPTY)
        		{
        			NBTTagCompound tag = new NBTTagCompound();
        			tag.setByte("Slot", (byte) i);
        			stack.writeToNBT(tag);
        			itemList.appendTag(tag);
        		}
        	}
        	tags.setTag("Inventory", itemList);
        	newStack.setTagCompound(tags);
    	}

    	player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack); 
    }

    @Override
    public ItemStack slotClick(int slot, int dragType, ClickType modifier, EntityPlayer player)
    {
    	if (slot == -999)
    	{
    		return ItemStack.EMPTY;
    	}
    	return super.slotClick(slot, dragType, modifier, player);
    }
    
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		int heldSlot = inventoryPlayer.currentItem;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 96+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			if (i == heldSlot)
			{
				addSlotToContainer(new SlotLocked(inventoryPlayer, i, 8+i*18,154));
			}
			else
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18,154));
			}
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
			Item toolTest = stack.getItem();


			if (slot < 1)
			{
				if (!this.mergeItemStack(stackInSlot, 1, 37, true))  
				{
					return ItemStack.EMPTY;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory

			else if (bookslot.slottedBookCheck(stackInSlot) && !this.mergeItemStack(stackInSlot, 0, 1, false))
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
