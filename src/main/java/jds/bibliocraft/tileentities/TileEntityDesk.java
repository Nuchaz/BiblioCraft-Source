package jds.bibliocraft.tileentities;

import java.util.Iterator;
import java.util.List;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockDesk;
import jds.bibliocraft.helpers.EnumRelativeLocation;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class TileEntityDesk extends BiblioTileEntity implements ITickable, ISidedInventory 
{
	public int leftStack;
	public int rightStack;
	public boolean hasBook;
	public boolean hasMap;
	private int counter = 1;
	public EnumRelativeLocation singleLeftRightCenter = EnumRelativeLocation.SINGLE;
	public boolean showBookname = false;
	public int currentPage;
	public EntityItemFrame fauxFrame;
	public boolean isVanilla = true;

	public TileEntityDesk()
	{
		super(10, true);
	}
	
	public void setSingleLeftRightCenter(EnumRelativeLocation slrc)
	{
		this.singleLeftRightCenter = slrc;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public EnumRelativeLocation getSingleLeftRightCenter()
	{
		return this.singleLeftRightCenter;
	}
	
	public void setShowBookName(boolean show)
	{
		showBookname = show;
	}
	public boolean showBookName()
	{
		return showBookname;
	}
	
	public boolean getHasMap()
	{
		return hasMap;
	}
	
	public void checkStackedBooks()
	{
		ItemStack writtenBookTest = getStackInSlot(0);
		hasMap = false;
		hasBook = false;
		if (writtenBookTest != ItemStack.EMPTY && writtenBookTest.getItem() != Items.AIR)
		{
			hasBook = true;
			if (writtenBookTest.getItem() ==Items.FILLED_MAP)
			{
				hasMap = true;
			}
		}
		else
		{
			hasBook = false;
		}
		leftStack = 0;
		for (int s = 1; s < 5; s++)
		{
			ItemStack stackTest = getStackInSlot(s);
			if (stackTest != ItemStack.EMPTY && stackTest.getItem() != Items.AIR)
			{
				leftStack = leftStack + 1;
			}
		}
		rightStack = 0;
		for (int s2 = 5; s2 < 9; s2++)
		{
			ItemStack stackTest = getStackInSlot(s2);
			if (stackTest != ItemStack.EMPTY && stackTest.getItem() != Items.AIR)
			{
				rightStack = rightStack + 1;
			}
		}
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void overwriteWrittenBook(ItemStack stack)
	{
		if (getStackInSlot(0) != ItemStack.EMPTY)
		{
			setInventorySlotContents(0, stack);
		}
	}
	///////////////Here is a collection of methods to build for updated support
	/// if I add current page methods, I need to tweak the written book
	/// so that if a book is added or removed, currpage is reset to 0
	public void setCurrentPage(int page)
	{
		currentPage = page;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	public int getCurrentPage()
	{
		return currentPage;
	}
	
	public boolean setOpenBookStack(ItemStack stack)
	{
		if (stack == ItemStack.EMPTY)
		{
			setInventorySlotContents(0, ItemStack.EMPTY);
			//deskInventory[0] = ItemStack.EMPTY;
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			return false;
		}
		if (inventory.get(0) == ItemStack.EMPTY && stack != ItemStack.EMPTY)
		{
			setInventorySlotContents(0, stack);
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			return true;
		}
		return false;
	}
	
	public int getLeftBookFullSlot()
	{
		int slot = 1;
		if (getStackInSlot(4) != ItemStack.EMPTY)
		{
			slot = 4;
		}
		else if (getStackInSlot(3) != ItemStack.EMPTY)
		{
			slot = 3;
		}
		else if (getStackInSlot(2) != ItemStack.EMPTY)
		{
			slot = 2;
		}
		else if (getStackInSlot(1) != ItemStack.EMPTY)
		{
			slot = 1;
		}
		return slot;
	}
	
	public int getLeftBookEmptySlot()
	{
		int slot = -1;
		if (getStackInSlot(1) == ItemStack.EMPTY)
		{
			slot = 1;
		}
		else if (getStackInSlot(2) == ItemStack.EMPTY)
		{
			slot = 2;
		}
		else if (getStackInSlot(3) == ItemStack.EMPTY)
		{
			slot = 3;
		}
		else if (getStackInSlot(4) == ItemStack.EMPTY)
		{
			slot = 4;
		}
		return slot;
	}
	
	public int getRightBookFullSlot()
	{
		int slot = -1;
		if (getStackInSlot(8) != ItemStack.EMPTY)
		{
			slot = 8;
		}
		else if (getStackInSlot(7) != ItemStack.EMPTY)
		{
			slot = 7;
		}
		else if (getStackInSlot(6) != ItemStack.EMPTY)
		{
			slot = 6;
		}
		else if (getStackInSlot(5) != ItemStack.EMPTY)
		{
			slot = 5;
		}
		return slot;
	}
	
	public int getRightBookEmptySlot()
	{
		int slot = -1;
		if (getStackInSlot(5) == ItemStack.EMPTY)
		{
			slot = 5;
		}
		else if (getStackInSlot(6) == ItemStack.EMPTY)
		{
			slot = 6;
		}
		else if (getStackInSlot(7) == ItemStack.EMPTY)
		{
			slot = 7;
		}
		else if (getStackInSlot(8) == ItemStack.EMPTY)
		{
			slot = 8;
		}
		return slot;
	}
	
    public ItemStack getOpenBook()
    {
    	return getStackInSlot(0);
    }
	
    
    public boolean checkSlot(int slot)
    {
    	if (getStackInSlot(slot) != ItemStack.EMPTY)
    	{
    		return true;
    	}
    	return false;
    }
    
    public int getLeftBookStack()
    {
    	return leftStack;
    }
    public int getRightBookStack()
    {
    	return rightStack;
    }
    
    public boolean hasWrittenBook()
    {
    	return hasBook;
    }
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			if (!Config.isBlock(itemstack) && Config.testBookValidity(itemstack))
			{
				return true;
			}
		}
		return false;
	}
	
	public void setFrame(World world)
	{
		fauxFrame = new EntityItemFrame(world);
	}
	
	int count = 0;
	@Override
	public void update() 
	{
		if (!this.world.isRemote)
		{
			if (counter >= Config.mapUpdateRate)
			{
				counter = 1;
				if (hasMap)
				{
					if (this.fauxFrame == null)
					{
						if (this.world != null)
						{
							this.setFrame(this.world);
						}
						return;
					}
					List players = this.world.playerEntities;
					ItemStack mapstack = getStackInSlot(0);
					mapstack.setItemFrame(fauxFrame);
					MapData mapdata =Items.FILLED_MAP.getMapData(mapstack, this.world);
		            Iterator iterator = players.iterator();
		            while (iterator.hasNext())
		            {
						EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
						Items.FILLED_MAP.updateMapData(getWorld(), entityplayermp, mapdata);
						Packet packet = mapdata.getMapPacket(mapstack, getWorld(), entityplayermp);
						if (packet != null)
						{
							//entityplayermp.playerNetServerHandler.sendPacket(packet);
							entityplayermp.connection.sendPacket(packet);
						}
		            }
				}
			}
			else
			{
				counter++;
			}
		}
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
		{
			int[] slots = {1,2,3,4,5,6,7,8};
			return slots;
		}
		else
		{
			int[] slots = {0,1,2,3,4,5,6,7,8};
			return slots;
		}
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, EnumFacing side)
	{
		if (slot >= 0 && slot <= 8)
		{
			if (Config.testBookValidity(itemstack))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side)
	{
		if (slot >= 0 && slot <= 8)
		{
			return true;
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockDesk.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		checkStackedBooks();
		if (slot == 0 && inventory.get(0) == ItemStack.EMPTY)
		{
			currentPage = 0;
		}
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.leftStack = nbt.getInteger("leftBookStack");
		this.rightStack = nbt.getInteger("rightBookStack");
		this.hasBook = nbt.getBoolean("hasWrittenBook");
		this.currentPage = nbt.getInteger("CurrentPage");
		this.hasMap = nbt.getBoolean("hasMap");
		this.singleLeftRightCenter = EnumRelativeLocation.getEnumFromID(nbt.getInteger("singleLeftRightCenter"));
		this.isVanilla = nbt.getBoolean("isVanilla");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
    	nbt.setInteger("leftBookStack", leftStack);
    	nbt.setInteger("rightBookStack", rightStack);
    	nbt.setBoolean("hasWrittenBook", hasBook);
    	nbt.setInteger("CurrentPage", currentPage);
    	nbt.setBoolean("hasMap", hasMap);
    	nbt.setInteger("singleLeftRightCenter", this.singleLeftRightCenter.getID());
    	nbt.setBoolean("isVanilla", this.isVanilla);
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
