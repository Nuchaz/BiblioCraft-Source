package jds.bibliocraft.tileentities;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.blocks.BlockCase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityCase extends BiblioTileEntity 
{
	public boolean openLid = false;;
    public boolean showText = false;
    public boolean hasRS;
	
	public TileEntityCase()
	{
		super(2, true);
	}
	
	public boolean hasRedstoneBlock()
	{
		ItemStack rsblock = getStackInSlot(0);
		if (rsblock != ItemStack.EMPTY)
		{
			if (Block.isEqualTo(Block.getBlockFromItem(rsblock.getItem()),Blocks.REDSTONE_BLOCK))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean setInnerCover(ItemStack carpet, EntityPlayer player)
	{
		if (carpet != ItemStack.EMPTY && carpet.getItem() == Item.getItemFromBlock(Blocks.CARPET) && getStackInSlot(1) == ItemStack.EMPTY)
		{
			ItemStack carpetCopy = carpet.copy();
			carpetCopy.setCount(1);
			setInventorySlotContents(1, carpetCopy);
			carpet.setCount(carpet.getCount() - 1);
			if (carpet.getCount() == 0)
			{
				player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY); 
			}
			else
			{
				player.inventory.setInventorySlotContents(player.inventory.currentItem, carpet); 
			}
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			return true;
		}
		return false;
	}
	public void removeInnerCover()
	{
		setInventorySlotContents(1, ItemStack.EMPTY);
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	public boolean hasRedStone()
	{
		return hasRS;
	}
	
	public void setShowText(boolean show)
	{
		showText = show;
	}
	public boolean getShowText()
	{
		return showText;
	}
	
	public boolean setCaseSlot(ItemStack stack)
	{
		boolean hasStack;
		if (stack == ItemStack.EMPTY)
		{
			if (isSlotFull())
			{
				setInventorySlotContents(0, ItemStack.EMPTY);
			}
			hasStack = false;
			return hasStack;
		}
		
		if (!isSlotFull())
		{
			setInventorySlotContents(0, stack);
			hasStack = true;
		}
		else
		{
			hasStack = false;
		}
		return hasStack;
	}
	
	public boolean isSlotFull()
	{
		if (inventory.get(0) != ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

    public void setOpenLid(boolean open)
    {
    	this.openLid = open;
    	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
  
    public boolean getOpenLid()
    {
    	return this.openLid;
    }

	@Override
	public String getName() 
	{
		return BlockCase.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		boolean redtest = hasRS;
		hasRS = hasRedstoneBlock();
		if (redtest != hasRS)
		{
			updateSurroundingBlocks(BlockCase.instance);
		}
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.openLid = nbt.getBoolean("LidOpen");
		this.hasRS = nbt.getBoolean("hasRedstone");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
    	nbt.setBoolean("LidOpen", openLid);
    	nbt.setBoolean("hasRedstone", hasRS);
		return nbt;
	}

	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
	
	@Override
	public boolean isEmpty() 
	{
		return false;
	}
	
}
