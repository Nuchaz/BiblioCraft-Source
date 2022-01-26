package jds.bibliocraft.tileentities;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.blocks.BlockBell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityBell extends BiblioTileEntity implements ITickable 
{
	private int redstone = 0;
	private int counter = 0;
	
	public TileEntityBell()
	{
		super(0, false);
	}

	@Override
	public String getName()
	{
		return BlockBell.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) { }

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) {	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt)
	{
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
	public void update() 
	{
		if (counter >= 2)
		{
			int power = getWorld().isBlockIndirectlyGettingPowered(getPos());
			if (power > redstone)
			{
				getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_DING, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			redstone = power;
			counter = 0;
		}
		else
		{
			counter++;
		}
	}
}
