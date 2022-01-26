package jds.bibliocraft.tileentities;

import java.util.ArrayList;

import jds.bibliocraft.blocks.BlockCookieJar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

public class TileEntityCookieJar extends BiblioTileEntity implements ITickable
{
	public boolean isOpen = false;
	public int cookiecount = 0;
	public String[] cookieNames = new String[0];

	public TileEntityCookieJar()
	{
		super(8, false);
	}
	
	public String[] getCookieNames()
	{
		return this.cookieNames;
	}
	
	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	public void setCookies()
	{
		int cookies = 0;
		ArrayList<String> names = new ArrayList<String>();
		for(int x=0; x < 8; x++)
		{
			if (getStackInSlot(x) != ItemStack.EMPTY)
			{
				cookies++;
				names.add(getStackInSlot(x).getUnlocalizedName());
			}
		}
		cookiecount = cookies;
		this.cookieNames = new String[cookiecount];
		for (int x = 0; x < cookiecount; x++)
		{
			this.cookieNames[x] = names.get(x);
		}
		
	}
	public int getCookies()
	{
		return cookiecount;
	}
	
	public void setIsOpen(boolean openness)
	{
		isOpen = openness;
		updateSurroundingBlocks(BlockCookieJar.instance);
	}
	
	public boolean getIsOpen()
	{
		return isOpen;
	}

	@Override
	public String getName() 
	{
		return BlockCookieJar.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		setCookies();
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.isOpen = nbt.getBoolean("isOpen");
		this.cookiecount = nbt.getInteger("cookiecount");
		
		NBTTagList strings = nbt.getTagList("names", Constants.NBT.TAG_STRING);
		if (strings.tagCount() > 0)
		{
			this.cookieNames = new String[strings.tagCount()];
			for (int x = 0; x < strings.tagCount(); x++)
			{
				this.cookieNames[x] = strings.getStringTagAt(x);
			}
		}
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
    	nbt.setBoolean("isOpen", isOpen);
    	nbt.setInteger("cookiecount", cookiecount);
    	
    	NBTTagList strings = new NBTTagList();
    	for (int x = 0; x < this.cookieNames.length; x++)
    	{
    		strings.appendTag(new NBTTagString(this.cookieNames[x]));
    	}
    	nbt.setTag("names", strings);
		return nbt;
	}

	int counter = 0;
	
	@Override
	public void update()
	{
		if (!this.world.isRemote && this.isOpen)
		{
			if (counter >= 20)
			{
				counter = 0;
				this.setIsOpen(false);
			}
			else
			{
				counter++;
			}
		}
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
