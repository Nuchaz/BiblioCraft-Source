package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BlockArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityArmorStand extends BiblioTileEntity
{
	public boolean helm = false;
	public boolean cuirass = false;
	public boolean greaves = false;
	public boolean boots = false;
	
	public int showArmorText = 0;
	
	private boolean isBottomStand = true;
	
	
	public TileEntityArmorStand()
	{
		super(4, true);
	}
	
	public void setIsBottomStand(boolean isBottom)
	{
		this.isBottomStand = isBottom;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public boolean getIsBottomStand()
	{
		return this.isBottomStand;
	}
	
	public void checkArmorSlots()
	{
		ItemStack stackTest0 = getStackInSlot(0);
		if (stackTest0 != ItemStack.EMPTY)
		{
			helm = true;
		}
		else
		{
			helm = false;
		}
		
		ItemStack stackTest1 = getStackInSlot(1);
		if (stackTest1 != ItemStack.EMPTY)
		{
			cuirass = true;
		}
		else
		{
			cuirass = false;
		}
		
		ItemStack stackTest2 = getStackInSlot(2);
		if (stackTest2 != ItemStack.EMPTY)
		{
			greaves = true;
		}
		else
		{
			greaves = false;
		}
		
		ItemStack stackTest3 = getStackInSlot(3);
		if (stackTest3 != ItemStack.EMPTY)
		{
			boots = true;
		}
		else
		{
			boots = false;
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	  
    public boolean getHelm()
    {
    	return helm;
    }
    public boolean getCuirass()
    {
    	return cuirass;
    }
    public boolean getGreaves()
    {
    	return greaves;
    }
    public boolean getBoots()
    {
    	return boots;
    }
    
    public boolean addArmor(ItemStack stack, EntityEquipmentSlot armorType)
    {
    	checkArmorSlots();
    	switch (armorType)
    	{
	    	case HEAD:{
	    		if (!helm)
	    		{
	    			setInventorySlotContents(0, stack);
	    			return true;
	    		}
	    		break;}
	    	case CHEST:{
	       		if (!cuirass)
	    		{
	    			setInventorySlotContents(1, stack);
	    			return true;
	    		}
	    		break;}
	    	case LEGS:{
	       		if (!greaves)
	    		{
	    			setInventorySlotContents(2, stack);
	    			return true;
	    		}
	    		break;}
	    	case FEET:{
	       		if (!boots)
	    		{
	    			setInventorySlotContents(3, stack);
	    			return true;
	    		}
	    		break;}
	    	default:break;
    	}
    	return false;
    }
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) 
	{
		if (!this.getIsBottomStand())
		{
			return false;
		}
		Item stackItem = itemstack.getItem();
		if (stackItem instanceof ItemArmor )
		{
			ItemArmor armorItem = (ItemArmor)stackItem;
			if (armorItem != ItemStack.EMPTY.getItem()) 
			{
				EntityEquipmentSlot armorType = armorItem.armorType;

				if (slot == 0 && armorType == EntityEquipmentSlot.FEET)
				{
					return true;
				}	
				else if (slot == 1 && armorType == EntityEquipmentSlot.LEGS)
				{
					return true;
				}
				else if (slot == 2 && armorType == EntityEquipmentSlot.CHEST)
				{
					return true;
				}
				else if (slot == 3 && armorType == EntityEquipmentSlot.HEAD)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockArmorStand.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		checkArmorSlots();
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.helm = nbt.getBoolean("helm");
		this.cuirass = nbt.getBoolean("cuirass");
		this.greaves = nbt.getBoolean("greaves");
		this.boots = nbt.getBoolean("boots");
		this.isBottomStand = nbt.getBoolean("isBottom");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt)
	{
		nbt.setBoolean("helm", this.helm);
		nbt.setBoolean("cuirass", this.cuirass);
		nbt.setBoolean("greaves", this.greaves);
		nbt.setBoolean("boots", this.boots);
		nbt.setBoolean("isBottom", this.isBottomStand);
		return nbt;
	}

	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}