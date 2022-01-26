package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BlockMarkerPole;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityMarkerPole extends BiblioTileEntity  
{
	//public int direction = 0;

	public TileEntityMarkerPole()
	{
		super(0, false);
		this.setRenderBoxAdditionalSize(2);
	}

	@Override
	public String getName() 
	{
		return BlockMarkerPole.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) { }

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		//this.direction = nbt.getInteger("direction");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		//nbt.setInteger("direction", direction);
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
}
