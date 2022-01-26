package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BiblioBlock;
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

public abstract class BiblioTileEntity extends TileEntity implements IInventory, IItemHandler
{
	private EnumFacing angle = EnumFacing.NORTH;
	private EnumShiftPosition shift = EnumShiftPosition.NO_SHIFT; 
	private EnumVertPosition vertPosition = EnumVertPosition.WALL;
	public NonNullList<ItemStack> inventory;
	private String customTexture = "none";
	private boolean isRetexturable; // this also means that the block comes in all the wood flavors
	private boolean isLocked = false;
	private String lockee = "";
	private int renderBoxAdditionalSize = 1;
	
	public BiblioTileEntity(int inventorySize, boolean canRetexture)
	{
		this.inventory = NonNullList.<ItemStack>withSize(inventorySize, ItemStack.EMPTY); //new ItemStack[inventorySize];
		this.isRetexturable = canRetexture;
	}
	

	public boolean addStackToInventoryFromWorld(ItemStack stack, int slot, EntityPlayer player)
	{
		if (slot == -1)
			return false;
		boolean returnValue = false;
		ItemStack currentStack = getStackInSlot(slot);
		if (stack != ItemStack.EMPTY && currentStack == ItemStack.EMPTY)
		{
			setInventorySlotContents(slot, stack);
			player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY); 
			returnValue = true;
		}
		else if (stack != ItemStack.EMPTY && currentStack != ItemStack.EMPTY)
		{
			ItemStack leftStack = stack.copy();
			ItemStack rightStack = currentStack.copy();
			leftStack.setCount(1);
			rightStack.setCount(1);
			if (getIsItemStacksEqual(leftStack, rightStack))
			{
				int total = stack.getCount() + currentStack.getCount();
				if (total > stack.getMaxStackSize() && currentStack.getCount() != currentStack.getMaxStackSize())
				{
					currentStack.setCount(stack.getMaxStackSize());
					stack.setCount(total - stack.getMaxStackSize());
					setInventorySlotContents(slot, currentStack);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, stack); 
					returnValue = true;
				}
				else if (total <= stack.getMaxStackSize())
				{
					currentStack.setCount(total);
					setInventorySlotContents(slot, currentStack);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY); 
					returnValue = true;
				}
			}
			
			if (returnValue)
			{
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			}
		}
		return returnValue;
	}
	
	public boolean getIsItemStacksEqual(ItemStack stack1, ItemStack stack2)
	{
		boolean output = false;
	    if (stack1 != ItemStack.EMPTY && stack2 != ItemStack.EMPTY)
	    {
	    	if (stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage())
	    	{
	    		NBTTagCompound tag1 = stack1.getTagCompound();
	    		NBTTagCompound tag2 = stack2.getTagCompound();
	    		if (tag1 == null && tag2 == null)
	    		{
	    			output = true;
	    		}
	    		else
	    		{
	    			output = tag1.equals(tag2);
	    		}
	    	}
	    }
		return output;
	}
	
	public boolean addStackToInventoryFromWorldSingleStackSize(ItemStack stack, int slot, EntityPlayer player)
	{
		boolean returnValue = false;
		ItemStack currentStack = getStackInSlot(slot);
		if (stack != ItemStack.EMPTY && currentStack == ItemStack.EMPTY)
		{
			if (stack.getCount() > 1)
			{
				ItemStack newStack = stack.copy();
				newStack.setCount(1);
				stack.setCount(stack.getCount() - 1);
				setInventorySlotContents(slot, newStack);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, stack); 
			}
			else
			{
				setInventorySlotContents(slot, stack);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY); 
			}
			returnValue = true;
		}
		
		if (returnValue)
		{
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		}
		return returnValue;
	}
	
	public boolean removeStackFromInventoryFromWorld(int slot, EntityPlayer player, BiblioBlock block)
	{
		boolean returnValue = false;
		ItemStack stack = getStackInSlot(slot);
		if (stack != ItemStack.EMPTY)
		{
			BlockPos newPos = this.getPos();
			if (player != null)
			{
				newPos = block.getDropPositionOffset(this.getPos(), player);
			}
			block.dropStackInSlot(this.world, this.getPos(), slot, newPos);
			setInventorySlotContents(slot, ItemStack.EMPTY);
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			returnValue = true;
		}
		
		return returnValue;
	}
	
	/** Called when something is added or change in the inventory */
	public abstract void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack);
	
    /** Use this to load custom tags from the NBT data  */
    public abstract void loadCustomNBTData(NBTTagCompound nbt);
    
    /** Use this to save custom NBT data tags */
    public abstract NBTTagCompound writeCustomNBTData(NBTTagCompound nbt);
	
	public void setAngle(EnumFacing facing)
	{
		this.angle = facing;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setShiftPosition(EnumShiftPosition position)
	{
		this.shift = position;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setVertPosition(EnumVertPosition position)
	{
		this.vertPosition = position;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public EnumFacing getAngle()
	{
		return this.angle;
	}
	
	public EnumShiftPosition getShiftPosition()
	{
		return this.shift;
	}
	
	public EnumVertPosition getVertPosition()
	{
		return this.vertPosition;
	}
	
	public boolean canRetextureBlock()
	{
		return this.isRetexturable;
	}
	
	public String getCustomTextureString()
	{
		return this.customTexture;
	}
	
	public void setCustomTexureString(String tex)
	{
		this.customTexture = tex;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		world.markBlockRangeForRenderUpdate(pos, pos);
	}

	public boolean isLocked()
	{
		return isLocked;
	}
    
	// as of now it only locks single blocks, so double clocks will have to be locked on the top and bottom blocks, as well as other multi blocks. I guess that is ok.
	public void setLocked(boolean locked)
	{
		isLocked = locked;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public String getLockee()
	{
		return lockee;
	}
	
	public void setLockee(String lockeeperson)
	{
		lockee = lockeeperson;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		ItemStack output = ItemStack.EMPTY;
		if (slot >= 0 && slot < this.inventory.size())
		{
			output = inventory.get(slot);
		}
		return output;
	}
	
	@Override 
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		if (slot >= 0 && slot < this.inventory.size())
		{
			inventory.set(slot, stack);
			if (stack != ItemStack.EMPTY && stack.getCount() > getInventoryStackLimit()) // this may be a place that needs to be edited for limiting stuff, maybe look at brewing stand tile entity for reference on these limits I would like to impose
			{
				stack.setCount(getInventoryStackLimit());
			}
			setInventorySlotContentsAdditionalCommands(slot, stack);
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		}
	}
	

	
	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		Item stackSizeTest = stack.getItem();
		if (stack != ItemStack.EMPTY)
		{
			if (stack.getCount() <= amount)
			{
				setInventorySlotContents(slot, ItemStack.EMPTY);
			}
			else
			{
				stack = stack.splitStack(amount);
				if (stack.getCount() == 0)
				{
					setInventorySlotContents(slot, ItemStack.EMPTY);
				}
			}
		}
		return stack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != ItemStack.EMPTY)
		{
			setInventorySlotContents(slot, ItemStack.EMPTY);
		}
		return stack;
	}
	
	@Override
	public abstract int getInventoryStackLimit();

	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB bb = INFINITE_EXTENT_AABB;
        bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + renderBoxAdditionalSize, pos.getY() + renderBoxAdditionalSize, pos.getZ() + renderBoxAdditionalSize);
        return bb;
    }
	
	public void setRenderBoxAdditionalSize(int size)
	{
		this.renderBoxAdditionalSize = size;
	}
	
	@Override
	public boolean hasCustomName() 
	{
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public int getField(int id) 
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) 
	{

	}

	@Override
	public int getFieldCount() 
	{
		return 0;
	}

	@Override
	public void clear() 
	{
		
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		loadNBTData(nbt);
    }
	
    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet)
    {
    	NBTTagCompound nbtData = packet.getNbtCompound();
    	loadNBTData(nbtData);
    	world.markBlockRangeForRenderUpdate(getPos(), getPos());
    }
    
    private void loadNBTData(NBTTagCompound nbt)
    {
    	NBTTagList tagList = nbt.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY); //new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < this.inventory.size())
			{
				this.inventory.set(slot, new ItemStack(tag));
			}
		}

		this.isLocked = nbt.getBoolean("locked");
		this.lockee = nbt.getString("lockee");
		// angle, shift, vert position
		this.angle = getFacingFromAngleID(nbt.getInteger("angle"));
		this.shift = EnumShiftPosition.getEnumFromID(nbt.getInteger("shift"));
		this.vertPosition = EnumVertPosition.getEnumFromID(nbt.getInteger("position"));
		loadCustomNBTData(nbt);
		if (isRetexturable)
			this.customTexture = nbt.getString("customTexture");
		
    }
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
		super.writeToNBT(nbt);
    	nbt = writeNBTData(nbt);
    	return nbt;
    }
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() 
    {
    	NBTTagCompound dataTag = new NBTTagCompound();
    	dataTag = writeNBTData(dataTag);
    	return new SPacketUpdateTileEntity(pos, 1, dataTag);
    }
	
	
	@Override
	public NBTTagCompound getUpdateTag() 
	{
		NBTTagCompound tags = super.getUpdateTag();
		return writeNBTData(tags);
	}
	
    private NBTTagCompound writeNBTData(NBTTagCompound nbt)
    {
    	NBTTagList itemList = new NBTTagList();
    	for (int i = 0; i < inventory.size(); i++)
    	{
    		ItemStack stack = inventory.get(i);
    		if (stack != ItemStack.EMPTY)
    		{
    			NBTTagCompound tag = new NBTTagCompound();
    			tag.setByte("Slot", (byte) i);
    			stack.writeToNBT(tag);
    			itemList.appendTag(tag);
    		}
    	}
    	nbt.setTag("Inventory", itemList);
    	nbt.setBoolean("locked", isLocked);
    	nbt.setString("lockee", lockee);
    	nbt.setInteger("angle", getAngleIDFromFacing(angle));
    	nbt.setInteger("shift", shift.getID());
    	nbt.setInteger("position", vertPosition.getID());
    	nbt = writeCustomNBTData(nbt);
    	if (isRetexturable)
    		nbt.setString("customTexture", customTexture);
    	return nbt;
    }
    
    private int getAngleIDFromFacing(EnumFacing facing)
    {
    	int angleID = 0;
    	switch (facing)
    	{
	    	case WEST: { angleID = 1; break; }
	    	case NORTH: { angleID = 2; break; }
	    	case EAST: { angleID = 3; break; }
	    	case DOWN: { angleID = 4; break; }
	    	case UP: { angleID = 5; break; }
	    	default: { angleID = 0; break; }
    	}
    	return angleID;
    }
    
    public int getAngleID()
    {
    	return getAngleIDFromFacing(getAngle());
    }
    
    private EnumFacing getFacingFromAngleID(int angle)
    {
    	EnumFacing face = EnumFacing.SOUTH; 
    	switch (angle)
    	{
	    	case 1:{ face = EnumFacing.WEST; break; }
	    	case 2:{ face = EnumFacing.NORTH; break; }
	    	case 3:{ face = EnumFacing.EAST; break; }
	    	case 4:{ face = EnumFacing.DOWN; break; }
	    	case 5:{ face = EnumFacing.UP; break; }
    	}
    	return face;
    }
    
    public void updateSurroundingBlocks(Block blocktype)
    {
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(),		pos.getY(), 	pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() + 1, 	pos.getY(), 	pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() - 1, 	pos.getY(), 	pos.getZ() + 1), blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ() - 1), blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() + 1, pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() - 1, pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() + 2, 	pos.getY(), 	pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() - 2,	pos.getY(), 	pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ() + 2), blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ() - 2), blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() + 2, pos.getZ()), 	 blocktype, true);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() - 2, pos.getZ()), 	 blocktype, true);
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return true;
    }
    
	@Override
	public boolean isEmpty() 
	{
		boolean output = true;
		for (int i = 0; i < inventory.size(); i++)
		{
			if (inventory.get(i) != ItemStack.EMPTY)
			{
				output = false;
				break;
			}
		}
		return output;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
	}

	@Override
	public int getSlots() 
	{
		// TODO This might have to be tweaked for things like the chair and table that have extra special slots for carpets
		return this.inventory.size();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
	{
		ItemStack returnStack = stack;
		if (slot < this.inventory.size())
		{
			ItemStack currentSlot = this.getStackInSlot(slot);
			if (currentSlot != ItemStack.EMPTY)
			{
				if (stack.getItem() == currentSlot.getItem() && currentSlot.getCount() < currentSlot.getMaxStackSize())
				{
					if (!simulate)
					{
						int count = currentSlot.getCount() + stack.getCount();
						if (count > stack.getMaxStackSize())
						{
							currentSlot.setCount(currentSlot.getMaxStackSize());
							setInventorySlotContents(slot, currentSlot);
							returnStack = stack.copy();
							returnStack.setCount(count - currentSlot.getMaxStackSize());
						}
						else
						{
							stack.setCount(count);
							setInventorySlotContents(slot, stack);
							returnStack = ItemStack.EMPTY;
						}
					}
				}
			}
			else
			{
				if (!simulate)
				{
					this.setInventorySlotContents(slot, stack);
					returnStack = ItemStack.EMPTY;
				}
			}
		}
		return returnStack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		ItemStack result = ItemStack.EMPTY;
		if (slot < this.inventory.size())
		{
			ItemStack slottedStack = this.getStackInSlot(slot);
			if (slottedStack != ItemStack.EMPTY && !simulate)
			{
				result = slottedStack.copy();
				if (amount >= slottedStack.getCount())
				{
					// send it all
					this.setInventorySlotContents(slot, ItemStack.EMPTY);
				}
				else
				{
					result.setCount(amount);
					slottedStack.setCount(slottedStack.getCount() - amount);
					this.setInventorySlotContents(slot, slottedStack);
				}
			}
			
			if (simulate)
			{
				// TODO return the simulated extracted ItemStack
			}
		}
		return result;
	}

	@Override
	public int getSlotLimit(int slot) 
	{
		// TODO I may have to tweak this for certain use cases? Like map frames, armor stands, clipboard block, 
		return 64;
	}

}
