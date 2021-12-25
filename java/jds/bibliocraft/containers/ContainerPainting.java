package jds.bibliocraft.containers;

import jds.bibliocraft.items.ItemPaintingCanvas;
import jds.bibliocraft.slots.SlotPainting;
import jds.bibliocraft.tileentities.TileEntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPainting extends Container
{
	protected TileEntityPainting painting;
	protected SlotPainting canvasSlot;
	
	private boolean hasStack = false;
	//private String[] stringSizes = {"1x1","1x2"};
	
	public ContainerPainting(InventoryPlayer inventoryPlayer, TileEntityPainting tile)
	{
		this.painting = tile;
		addSlotToContainer(this.canvasSlot = new SlotPainting(this, this.painting, 0, 80, 89));
		bindPlayerInventory(inventoryPlayer);
		if (this.painting.getStackInSlot(0) != ItemStack.EMPTY)
		{
			this.hasStack = true;
		}
		else
		{
			this.hasStack = false;
		}
	}
	
	@Override
	public void detectAndSendChanges()
	{
		//System.out.println("container update");
		if (this.painting.getStackInSlot(0) != ItemStack.EMPTY)
		{
			if (!hasStack)
			{
				this.hasStack = true;
				this.painting.setContainterUpdate(true);
				//System.out.println("has a stack");
			}
		}
		else
		{
			if (hasStack)
			{
				this.hasStack = false;
				this.painting.setContainterUpdate(true);
				//System.out.println("lost a stack");
			}
		}
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 110+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18,168));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return painting.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		//this.painting.setContainterUpdate(true);
		//System.out.println("container update");
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
			else 
			{
				if ((!canvasCheck(stack) || this.painting.getStackInSlot(0) != ItemStack.EMPTY))
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
	
	private boolean canvasCheck(ItemStack stack)
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
