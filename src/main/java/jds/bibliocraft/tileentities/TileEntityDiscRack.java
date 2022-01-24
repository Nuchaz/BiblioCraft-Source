package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BlockDiscRack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

public class TileEntityDiscRack extends BiblioTileEntity
{
	public boolean wallRotated = false;
	
	public TileEntityDiscRack()
	{
		super(9, false);
	}
	
	/*
	public boolean addDisc(int slot, ItemStack newDisc)
	{
		ItemStack slotStack = getStackInSlot(slot);
		if (slotStack == null)
		{
			setInventorySlotContents(slot, newDisc);
			return true;
		}
		else
		{
			return false;
		}
	}
	*/
	public void setWallRotation()
	{
		if (wallRotated)
		{
			wallRotated = false;
		}
		else
		{
			wallRotated = true;
		}
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	public boolean getWallRotation()
	{
		return wallRotated;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		//    this has to do with buildcraft pipes / hoppers
		if (itemstack != ItemStack.EMPTY)
		{
			if (itemstack.getItem() instanceof ItemRecord || itemstack.getUnlocalizedName().contains("disc") || itemstack.getUnlocalizedName().contains("disk"))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockDiscRack.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) {}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.wallRotated = nbt.getBoolean("wallRotated");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		nbt.setBoolean("wallRotated", wallRotated);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
