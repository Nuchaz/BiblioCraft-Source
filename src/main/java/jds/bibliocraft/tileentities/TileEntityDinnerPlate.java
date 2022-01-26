package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BlockDinnerPlate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileEntityDinnerPlate extends BiblioTileEntity 
{
	
	public TileEntityDinnerPlate()
	{
		super(3, false);
	}
	
	
	public boolean isFoodHaveBowl(ItemStack food, World world, EntityPlayer player)
	{
		if (food.getItem() instanceof ItemFood)
		{
			ItemStack bowlTest = food.onItemUseFinish(world, player);// this changed, but seems to work I think
			if (bowlTest != ItemStack.EMPTY)
			{
				if (bowlTest.getItem() == Items.BOWL )//Item.bowlEmpty)  this might need some attention later
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public int addFood(ItemStack newFood)
	{
		int addedFood = -1;
		ItemStack slot1Stack = getStackInSlot(0);
		ItemStack slot2Stack = getStackInSlot(1);
		ItemStack slot3Stack = getStackInSlot(2);
		if (slot1Stack == ItemStack.EMPTY)
		{
			setInventorySlotContents(0, newFood);
			return 0;
		}
		else if (slot1Stack.getItem() == newFood.getItem() && slot1Stack.getItemDamage() == newFood.getItemDamage())
		{
			if (slot1Stack.getCount() < slot1Stack.getMaxStackSize())
			{
				if ((slot1Stack.getCount() + newFood.getCount()) < slot1Stack.getMaxStackSize())
				{
					slot1Stack.setCount(slot1Stack.getCount() + newFood.getCount());
					setInventorySlotContents(0, slot1Stack);
					return 0;
				}
				else
				{
					addedFood = (newFood.getCount() - (slot1Stack.getMaxStackSize() - slot1Stack.getCount()));
					slot1Stack.setCount(slot1Stack.getMaxStackSize());
					return addedFood;
				}
			}
		}
		
		if (slot2Stack == ItemStack.EMPTY)
		{
			setInventorySlotContents(1, newFood);
			return 0;
		}
		else if (slot2Stack.getItem() == newFood.getItem() && slot2Stack.getItemDamage() == newFood.getItemDamage())
		{
			if (slot2Stack.getCount() < slot2Stack.getMaxStackSize())
			{
				if ((slot2Stack.getCount() + newFood.getCount()) < slot2Stack.getMaxStackSize())
				{
					slot2Stack.setCount(slot2Stack.getCount() + newFood.getCount());
					setInventorySlotContents(1, slot2Stack);
					return 0;
				}
				else
				{
					addedFood = (newFood.getCount() - (slot2Stack.getMaxStackSize() - slot2Stack.getCount()));
					slot2Stack.setCount(slot2Stack.getMaxStackSize());
					return addedFood;
				}
			}
		}
		
		if (slot3Stack == ItemStack.EMPTY)
		{
			setInventorySlotContents(2, newFood);
			return 0;
		}
		else if (slot3Stack.getItem() == newFood.getItem() && slot3Stack.getItemDamage() == newFood.getItemDamage())
		{
			if (slot3Stack.getCount() < slot3Stack.getMaxStackSize())
			{
				if ((slot3Stack.getCount() + newFood.getCount()) < slot3Stack.getMaxStackSize())
				{
					slot3Stack.setCount(slot3Stack.getCount() + newFood.getCount());
					setInventorySlotContents(2, slot3Stack);
					return 0;
				}
				else
				{
					addedFood = (newFood.getCount() - (slot3Stack.getMaxStackSize() - slot3Stack.getCount()));
					slot3Stack.setCount(slot3Stack.getMaxStackSize());
					return addedFood;
				}
			}
		}
		
		return -1;
	}
	
	public boolean isPlateEmpty()
	{
		ItemStack slot1Stack = getStackInSlot(0);
		ItemStack slot2Stack = getStackInSlot(1);
		ItemStack slot3Stack = getStackInSlot(2);
		if (slot1Stack == ItemStack.EMPTY && slot2Stack == ItemStack.EMPTY && slot3Stack == ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
	public ItemStack getFood(int slot)
	{
		ItemStack newFood = getStackInSlot(slot).copy();
		ItemStack plateFood = getStackInSlot(slot);
		if (plateFood.getCount() > 1)
		{
			plateFood.setCount(plateFood.getCount() - 1);
			setInventorySlotContents(slot, plateFood);
		}
		else
		{
			setInventorySlotContents(slot, ItemStack.EMPTY);
		}
		newFood.setCount(1);
		return newFood;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		//   this has to do with buildcraft pipes / hoppers
		// this should only accept foods
		if (itemstack != ItemStack.EMPTY)
		{
			if (itemstack.getItem() instanceof ItemFood)
			{
				return true;
			}
		}
		return false;
	}
	

	@Override
	public String getName() 
	{
		return BlockDinnerPlate.name;
	}
	
	
	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		
	}
	
	
	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		
	}
	
	
	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
