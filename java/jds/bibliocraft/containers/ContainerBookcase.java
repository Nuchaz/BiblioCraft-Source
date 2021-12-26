package jds.bibliocraft.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.registries.ObjectHolder;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.Config;
import jds.bibliocraft.slots.SlotBook;

public class ContainerBookcase extends Container 
{
/*
	protected ContainerBookcase(ContainerType<?> type, int id) {
		super(type, id);
		// TODO Auto-generated constructor stub
	}*/
	protected IInventory tileEntity;
	protected SlotBook bookSlot;
	
	@ObjectHolder("bibliocraft:bookcasecontainer")
	public static ContainerType<ContainerBookcase> bookcaseContainer;
	
	//MyContainer(int windowId, PlayerInventory inv, PacketBuffer extraData)
	public ContainerBookcase (int id, PlayerInventory inv, PacketBuffer extraData)
	{
		//this(inv, id);
		// use the packetbuffer to send any extra data and pack it into this constructor.
		//extraData.readInt()
		//Minecraft.getInstance().world
		this(id, inv, new Inventory(16));
	}
	
	public ContainerBookcase (int id, PlayerInventory inv, IInventory tile)
	{
		super(bookcaseContainer, id);
		for (int i = 0; i < 2; i++)  // this changes how many slots are on the y axis
		{
			for (int j = 0; j < 8; j++) // this changes how many slots are on the x axis
			{
				// to setup container limitations all I need to do is create a class that extends Slot
				//overwrite isItemValid() and getSlotStackLimit()
				// then initialize here with a private final SlotBook bookSlot;
				addSlot(this.bookSlot = new SlotBook(this, tile, j+i*8, 17+j*18, 17+i*25)); // tileEneity is null. How to fix? Maybe pass coords along and just get it from the world?
				
				//addSlot(new Slot(inv, j+i*8, 17+j*18, 17+i*25)); // from what I understand, the j+i+x bit, the x should match the x axis slots
			}
		
		}
		bindPlayerInventory(inv);
	}
	/*
	public ContainerBookcase (PlayerInventory inventoryPlayer, TileEntity tile)
	{
		//super(ContainerBookcase.class, 0);
		tileEntity = (IInventory)tile;
		
		// Notes from tutorial say that    
		//the Slot constructor takes the IInventory and the slot number in that it binds to
        //and the x-y coordinates it resides on-screen
		
		for (int i = 0; i < 2; i++)  // this changes how many slots are on the y axis
		{
			for (int j = 0; j < 8; j++) // this changes how many slots are on the x axis
			{
				// to setup container limitations all I need to do is create a class that extends Slot
				//overwrite isItemValid() and getSlotStackLimit()
				// then initialize here with a private final SlotBook bookSlot;
				addSlot(this.bookSlot = new SlotBook(this, tileEntity, j+i*8, 17+j*18, 17+i*25));
				//addSlot(new Slot(tileEntity, j+i*8, 17+j*18, 17+i*25)); // from what I understand, the j+i+x bit, the x should match the x axis slots
			}
		
		}
		bindPlayerInventory(inventoryPlayer);
	}*/
	
	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return true;//tileEntity.isUsableByPlayer(player); TODO
	}
	
	protected void bindPlayerInventory(PlayerInventory inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlot(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 84+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			addSlot(new Slot(inventoryPlayer, i, 8+i*18,142));
		}
	}
	/*
	 * It seems I can test if things are instances of armor or tools.
	 * 
	 * stack.getItem() instanceof ItemArmor
	 * 
	 */
	//@Override
	public ItemStack transferStackInSlots(PlayerEntity player, int slot) //added an s so it doesnt override for now
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = (Slot) inventorySlots.get(slot);
//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			
			//stackSizeTest.getItemStackLimit()
			//merges the item into player inventory since its in the tileEntity
			
			if (slot < 16) // changing 9 to 6
			{
				if (!this.mergeItemStack(stackInSlot, 16, 52, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory

			else if (!Config.isBlock(stack) && Config.testBookValidity(stack) && !this.mergeItemStack(stackInSlot, 0, 16, false))  //&& stackTest.getItemStackLimit() == 1
			{
				//System.out.println(stackName);
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
