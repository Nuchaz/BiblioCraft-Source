package jds.bibliocraft.containers;

import jds.bibliocraft.Config;
import jds.bibliocraft.slots.SlotPanalerPanels;
import jds.bibliocraft.slots.SlotPanelerInput;
import jds.bibliocraft.slots.SlotPanelerOutput;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerFurniturePaneler extends Container 
{
	public TileEntityFurniturePaneler tileEntity;
	private SlotPanalerPanels slotPanels;
	private SlotPanelerInput slotInput;
	private SlotPanelerOutput slotOutput;
	public World world;
	
	public ContainerFurniturePaneler(InventoryPlayer inventoryPlayer, TileEntityFurniturePaneler tile)
	{
		this.tileEntity = tile;
		this.tileEntity.eventHandler = this;
		world = tile.getWorld();
		addSlotToContainer(this.slotPanels = new SlotPanalerPanels(this, tileEntity, 0, 80, 27));
		addSlotToContainer(this.slotInput = new SlotPanelerInput(this, tileEntity, 1, 62, 56));
		addSlotToContainer(this.slotOutput = new SlotPanelerOutput(this, tileEntity, 2, 98, 56));
		this.onCraftMatrixChanged(this.tileEntity);
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
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
		this.tileEntity.updateRecipeManager();
		this.detectAndSendChanges();
    }
    
    
    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.tileEntity.eventHandler = null;
        this.tileEntity.playerFromBlock = null;
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
			if (slot < 3)
			{
				//Panels slot
				if (!this.mergeItemStack(stackInSlot, 3, 39, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (checkIfIsPanelsBlock(stack) && !this.mergeItemStack(stackInSlot, 0, 1, false))
			{
				return ItemStack.EMPTY;
			}
			else if (tileEntity.checkIfFramedBiblioCraftBlock(stack) && !this.mergeItemStack(stackInSlot, 1, 2, false))
			{
				return ItemStack.EMPTY;
			}
			
			if (stackInSlot.getCount() == 0)
			{
				slotObject.putStack(ItemStack.EMPTY);
			} 
			else 
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

	private boolean checkIfIsPanelsBlock(ItemStack stack)
	{
		if (Config.isBlock(stack))
		{
			Block thing = Block.getBlockFromItem(stack.getItem());
			boolean thaumcraftException = stack.getUnlocalizedName().contains("tile.blockWoodenDevice");
			if ((thing.isOpaqueCube(thing.getDefaultState())) || thaumcraftException)
			{
				return true;
			}
		}
		return false;
	}
	

}
