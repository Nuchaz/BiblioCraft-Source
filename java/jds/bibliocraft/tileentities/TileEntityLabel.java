package jds.bibliocraft.tileentities;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.blocks.BlockLabel;
import jds.bibliocraft.helpers.EnumVertPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.Constants;

public class TileEntityLabel extends BiblioTileEntity 
{
	public boolean showTextC = false;
	public boolean showTextL = false;
	public boolean showTextR = false;
	
	public TileEntityLabel()
	{
		super(3, true);
	}
	
	public void setShowTextC(boolean show)
	{
		showTextC = show;
	}
	public void setShowTextL(boolean show)
	{
		showTextL = show;
	}
	public void setShowTextR(boolean show)
	{
		showTextR = show;
	}
	public boolean getShowTextC()
	{
		return showTextC;
	}
	public boolean getShowTextL()
	{
		return showTextL;
	}
	public boolean getShowTextR()
	{
		return showTextR;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockLabel.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		//worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
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
