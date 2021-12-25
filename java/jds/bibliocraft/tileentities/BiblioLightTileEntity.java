package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BlockLampGold;
import jds.bibliocraft.helpers.EnumMetalType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class BiblioLightTileEntity extends BiblioTileEntity
{
	private EnumMetalType type;
	
	public BiblioLightTileEntity()
	{
		super(0, false);
	}
	
	public EnumMetalType getLightType()
	{
		return this.type;
	}
	
	public void setLightType(EnumMetalType light)
	{
		this.type =  light;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		//worldObj.markBlockForUpdate(getPos());
	}

	@Override
	public String getName() 
	{
		return BlockLampGold.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.type = EnumMetalType.getEnumFromID(nbt.getInteger("type"));
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		nbt.setInteger("type", this.type.getID());
		return nbt;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 0;
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

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return false;
	}
}

