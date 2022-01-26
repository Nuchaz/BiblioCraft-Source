package jds.bibliocraft.containers;

import jds.bibliocraft.slots.SlotArmorBoots;
import jds.bibliocraft.slots.SlotArmorCuirass;
import jds.bibliocraft.slots.SlotArmorGreaves;
import jds.bibliocraft.slots.SlotArmorHelm;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;

public class ContainerArmor extends Container
{
	
	protected TileEntityArmorStand tileEntity;
	protected SlotArmorHelm helmSlot;
	protected SlotArmorCuirass cuirassSlot;
	protected SlotArmorGreaves greavesSlot;
	protected SlotArmorBoots bootsSlot;
	
	public ContainerArmor (InventoryPlayer inventoryPlayer, TileEntityArmorStand tile)
	{
		tileEntity = tile;
		
		addSlotToContainer(this.helmSlot = new SlotArmorHelm(this, tileEntity, 0, 80, 8));
		addSlotToContainer(this.cuirassSlot = new SlotArmorCuirass(this, tileEntity, 1, 80, 26));
		addSlotToContainer(this.greavesSlot = new SlotArmorGreaves(this, tileEntity, 2, 80, 44));
		addSlotToContainer(this.bootsSlot = new SlotArmorBoots(this, tileEntity, 3, 80, 62));
		
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
		addSlotToContainer(new SlotArmorHelm(this, inventoryPlayer, 39, 126, 8));
		addSlotToContainer(new SlotArmorCuirass(this, inventoryPlayer, 38, 126, 26));
		addSlotToContainer(new SlotArmorGreaves(this, inventoryPlayer, 37, 126, 44));
		addSlotToContainer(new SlotArmorBoots(this, inventoryPlayer, 36, 126, 62));
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
			Item stackItem = stack.getItem();
			
			if (stackItem instanceof ItemArmor )
			{
				ItemArmor armorItem = (ItemArmor)stackItem;
				EntityEquipmentSlot armorType = armorItem.armorType;
				if (slot == 0)
				{
					if (player.inventory.armorInventory.get(3) == ItemStack.EMPTY)
					{
						if (!this.mergeItemStack(stackInSlot, 40, 41, true))  
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 4, 40, true))  
						{
							return ItemStack.EMPTY;
						}
					}
					//System.out.println(player.inventory.armorInventory[0));
				}
				else if (slot == 1)
				{
					if (player.inventory.armorInventory.get(2) == ItemStack.EMPTY)
					{
						if (!this.mergeItemStack(stackInSlot, 41, 42, true))  
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 4, 40, true))  
						{
							return ItemStack.EMPTY;
						}
					}
				}
				else if (slot == 2)
				{
					if (player.inventory.armorInventory.get(1) == ItemStack.EMPTY)
					{
						if (!this.mergeItemStack(stackInSlot, 42, 43, true))  
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 4, 40, true))  
						{
							return ItemStack.EMPTY;
						}
					}	
				}
				else if (slot == 3)
				{
					if (player.inventory.armorInventory.get(0) == ItemStack.EMPTY)
					{
						if (!this.mergeItemStack(stackInSlot, 43, 44, true))  
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 4, 40, true))  
						{
							return ItemStack.EMPTY;
						}
					}	
				}
				
				/*
				if (slot < 4) // changing 9 to 6
				{
					if (!this.mergeItemStack(stackInSlot, 4, 40, true))  
					{
						return null;
					}
					/*
					else if (armorType == 1 && !this.mergeItemStack(stackInSlot, 41, 42, true))  
					{
						return null;
					}
					else if (armorType == 2 && !this.mergeItemStack(stackInSlot, 42, 43, true))  
					{
						return null;
					}
					else if (armorType == 3 && !this.mergeItemStack(stackInSlot, 43, 44, true)) 
					{
						return null;
					}
					
					else if (!this.mergeItemStack(stackInSlot, 4, 40, true))  // this merges into the player inventory, I should be able to tell it to goto the armor slots first here I think
					{
						return null;
					}
					
				}
				*/
			else if (armorType == EntityEquipmentSlot.FEET)
			{
				if (player.inventory.armorInventory.get(3) == ItemStack.EMPTY)
				{
					if (!this.mergeItemStack(stackInSlot, 40, 41, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (!this.mergeItemStack(stackInSlot, 0, 1, false)) // this is where I setup the armor thing
				{
					return ItemStack.EMPTY;
				}
			}
			else if (armorType == EntityEquipmentSlot.LEGS)
			{
				if (player.inventory.armorInventory.get(2) == ItemStack.EMPTY)
				{
					if (!this.mergeItemStack(stackInSlot, 41, 42, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (!this.mergeItemStack(stackInSlot, 1, 2, false)) // this is where I setup the armor thing
				{
					return ItemStack.EMPTY;
				}
			}	
			else if (armorType == EntityEquipmentSlot.CHEST)
			{
				
				if (player.inventory.armorInventory.get(1) == ItemStack.EMPTY)
				{
					if (!this.mergeItemStack(stackInSlot, 42, 43, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (!this.mergeItemStack(stackInSlot, 2, 3, false)) // this is where I setup the armor thing
				{
					return ItemStack.EMPTY;
				}
			}
			else if (armorType == EntityEquipmentSlot.HEAD)
			{
				if (player.inventory.armorInventory.get(0) == ItemStack.EMPTY)
				{
					if (!this.mergeItemStack(stackInSlot, 43, 44, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (!this.mergeItemStack(stackInSlot, 3, 4, false)) // this is where I setup the armor thing
				{
					return ItemStack.EMPTY;
				}
			}
				/*
				else if (armorType == 1 && !this.mergeItemStack(stackInSlot, 1, 2, false)) 
				{
					return null;
				}
				else if (armorType == 2 && !this.mergeItemStack(stackInSlot, 2, 3, false)) 
				{
					return null;
				}
				else if (armorType == 3 && !this.mergeItemStack(stackInSlot, 3, 4, false)) 
				{
					return null;
				}
				*/
			}
			
			// The copy starts here
			//Block pumpkinTest = stackInSlot.;
			if (stackItem instanceof ItemSkull || (Block.isEqualTo(Block.getBlockFromItem(stackItem), Blocks.PUMPKIN) && stackInSlot.getCount() == 1))
			{
			//stackSizeTest.getItemStackLimit()
		
			//merges the item into player inventory since its in the tileEntity
				if (slot == 0) // changing 9 to 6
				{
					if (!this.mergeItemStack(stackInSlot, 4, 40, true))  
					{
						return ItemStack.EMPTY;
					}
				}
			
				else if (!this.mergeItemStack(stackInSlot, 0, 1, false)) // this is where I setup the armor thing
				{
					return ItemStack.EMPTY;
				}
			
			}
			// the copy ends here
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
	
	public boolean armorTest(Item armorItem, int armortype, int slot)
	{
		
		if (armorItem instanceof ItemArmor && armortype == slot)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
