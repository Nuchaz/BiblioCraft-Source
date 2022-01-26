package jds.bibliocraft.tileentities;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockPotionShelf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityPotionShelf extends BiblioTileEntity 
{
	public int showPotText = 0;
	
	public TileEntityPotionShelf()
	{
		super(12, true);
	}
	
	public boolean checkSlot(int slot)
    {
    	if (getStackInSlot(slot) != ItemStack.EMPTY)
    	{
    		return true;
    	}
    	return false;
    }
	
    public void setShowPotText(int pottext)
    {
    	showPotText = pottext;
    }
    public int getShowPotText()
    {
    	return showPotText;
    }
    
    public boolean addPotion(ItemStack pot, int slot)
    {
    	if (pot ==ItemStack.EMPTY && getStackInSlot(slot) != ItemStack.EMPTY)
    	{
    		setInventorySlotContents(slot, ItemStack.EMPTY);
    		return false;
    	}
    	if (pot != ItemStack.EMPTY && getStackInSlot(slot) == ItemStack.EMPTY)
    	{
    		setInventorySlotContents(slot, pot);
    		return true;
    		
    	}
    	return false;
    }
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			String stackName = stackitem.toString();
			String displayName = stackitem.getItemStackDisplayName(itemstack);
			if (Config.testPotionValidity(stackName, displayName, stackitem))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockPotionShelf.name;
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
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
