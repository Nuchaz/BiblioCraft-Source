package jds.bibliocraft.tileentities;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockToolRack;
import jds.bibliocraft.helpers.EnumVertPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityToolRack extends BiblioTileEntity 
{
	public int showWText = 0;
	
	public TileEntityToolRack()
	{
		super(4, true);
	}
	
	public boolean checkSlot(int slot)
    {
    	if (getStackInSlot(slot) != ItemStack.EMPTY)
    	{
    		return true;
    	}
    	return false;
    }
    
    public ItemStack getTool(int slot)
    {
    	ItemStack tool = inventory.get(slot);
    	if(tool != ItemStack.EMPTY)
    	{
    		return tool;
    	}
    	else
    	{
    		return ItemStack.EMPTY;
    	}
    }
    
    public void removeTool(int slot)
    {
    	if (inventory.get(slot) != ItemStack.EMPTY)
    	{
    		setInventorySlotContents(slot, ItemStack.EMPTY);
    		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    	}
    }
    
    public boolean addTool(int slot, ItemStack stack)
    {
    	if (inventory.get(slot) == ItemStack.EMPTY)
    	{
    		setInventorySlotContents(slot, stack);
    		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			if (isItemTool(stackitem, itemstack))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isItemTool(Item tool, ItemStack stack)
	{
		String toolName = tool.getItemStackDisplayName(stack);
		String toolcodeName = tool.toString();
		//String displayName = stackTest.getItemDisplayName(stack);
		if (tool instanceof ItemTool || tool instanceof ItemSword || tool instanceof ItemBow || tool instanceof ItemHoe || tool instanceof ItemFishingRod || tool instanceof ItemShears || tool instanceof ItemFlintAndSteel || Config.testToolValidity(toolName, toolcodeName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String getName() 
	{
		return BlockToolRack.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) {		setVertPosition(EnumVertPosition.WALL);}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) {}

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
