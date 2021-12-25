package jds.bibliocraft.tileentities;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockSwordPedestal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

public class TileEntitySwordPedestal extends BiblioTileEntity 
{
	public boolean showText = false;
	
	public TileEntitySwordPedestal()
	{
		super(1, false);
	}
	
	public void setShowText(boolean setter)
	{
		showText = setter;
	}
	public boolean getShowText()
	{
		return showText;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		// for use with pipes and hoppers and such
		if (itemstack != ItemStack.EMPTY)
		{
			if (itemstack.getItem() instanceof ItemSword || itemstack.getUnlocalizedName().toLowerCase().contains("sword") || itemstack.getUnlocalizedName().toLowerCase().contains("gt.metatool.01.0"))
			{
				if (itemstack.getItem() == Item.getItemFromBlock(BlockSwordPedestal.instance))
				{
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockSwordPedestal.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		updateSurroundingBlocks(BlockSwordPedestal.instance);
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
