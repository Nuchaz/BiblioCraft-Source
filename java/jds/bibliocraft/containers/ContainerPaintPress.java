package jds.bibliocraft.containers;

import jds.bibliocraft.items.ItemPaintingCanvas;
import jds.bibliocraft.slots.SlotPaintPress;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPaintPress extends Container
{
	protected TileEntityPaintPress paintPress;
	protected SlotPaintPress canvasSlot;
	
	public ContainerPaintPress(InventoryPlayer inventoryPlayer, TileEntityPaintPress tile)
	{
		this.paintPress = tile;
		
		addSlotToContainer(this.canvasSlot = new SlotPaintPress(this, this.paintPress, 0, 120, 139));
		bindPlayerInventory(inventoryPlayer);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 48+j*18, 159+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 48+i*18,217));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return paintPress.isUsableByPlayer(player);
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
			if (slot == 0)
			{
				if (!this.mergeItemStack(stackInSlot, 1, 37, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			else  // use this line to limit what can be shift-clicked into place
			{
				if ((!checkCanvas(stack) || this.paintPress.getStackInSlot(0) != ItemStack.EMPTY))
				{
					return ItemStack.EMPTY;
				}
				
				if (stack.getCount() == 1)
				{
					if (!this.mergeItemStack(stackInSlot, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
					
				}
				else
				{	
					stack.setCount(1);
					this.mergeItemStack(stack, 0, 1, false);
					stackInSlot.setCount(stackInSlot.getCount() - 1);
                    return ItemStack.EMPTY;
				}
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
	
	private boolean checkCanvas(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemPaintingCanvas)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
