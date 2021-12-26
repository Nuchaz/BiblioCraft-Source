package jds.bibliocraft.tileentities;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BiblioBlock;
import jds.bibliocraft.blocks.BiblioWoodBlock;
import jds.bibliocraft.helpers.BiblioRenderHelper;
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.helpers.EnumWoodsType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.IItemHandler;

public abstract class BiblioTileEntity extends TileEntity implements IInventory, IItemHandler
{

	private Direction angle = Direction.NORTH;
	private EnumShiftPosition shift = EnumShiftPosition.NO_SHIFT; 
	private EnumVertPosition vertPosition = EnumVertPosition.WALL;
	public NonNullList<ItemStack> inventory;
	private String customTexture = "none";
	private boolean isRetexturable; // this also means that the block comes in all the wood flavors
	private boolean isLocked = false;
	private String lockee = "";
	private int renderBoxAdditionalSize = 1;
	
	//private VoxelShape collisionBox;
	
	// TODO I think I need to track colors / wood type here somewhere.
	
	public BiblioTileEntity(TileEntityType<?> tile, int inventorySize, boolean canRetexture)
	{
		super(tile);
		this.inventory = NonNullList.<ItemStack>withSize(inventorySize, ItemStack.EMPTY); //new ItemStack[inventorySize];
		this.isRetexturable = canRetexture;
	}
	
	public static ModelProperty<String> TEXTURE = new ModelProperty<String>();
	public static ModelProperty<Direction> DIRECTION = new ModelProperty<Direction>();
	public static ModelProperty<EnumShiftPosition> SHIFTPOS = new ModelProperty<EnumShiftPosition>();
	public static ModelProperty<EnumVertPosition> VERTPOS = new ModelProperty<EnumVertPosition>();
	//public static ModelProperty<EnumWoodsType> WOOD = new ModelProperty<EnumWoodsType>();
	
	@Override
	public IModelData getModelData() 
	{
		System.out.println("biblio tile getModelData");
		ModelDataMap.Builder map = new ModelDataMap.Builder().withInitial(TEXTURE, customTexture).withInitial(DIRECTION, angle).withInitial(SHIFTPOS, shift).withInitial(VERTPOS, vertPosition);
		map = getAdditionalModelData(map);
		return map.build();
	}
	
	public ModelDataMap.Builder getAdditionalModelData(ModelDataMap.Builder map)
	{
		return map;
	}

	public boolean addStackToInventoryFromWorld(ItemStack stack, int slot, PlayerEntity player)
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
	    	if (stack1.getItem() == stack2.getItem() && stack1.getDamage() == stack2.getDamage())
	    	{
	    		CompoundNBT tag1 = stack1.getTag();
	    		CompoundNBT tag2 = stack2.getTag();
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
	
	public boolean addStackToInventoryFromWorldSingleStackSize(ItemStack stack, int slot, PlayerEntity player)
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
	
	@SuppressWarnings("static-access")
	public boolean removeStackFromInventoryFromWorld(int slot, PlayerEntity player, BiblioBlock block)
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
			//block.dropStackInSlot(this.world, this.getPos(), slot, newPos);
			//block.spawnDrops(state, worldIn, newPos, tileEntityIn, entityIn, stack);
			block.spawnAsEntity(world, newPos, this.getStackInSlot(slot));
			setInventorySlotContents(slot, ItemStack.EMPTY);
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			returnValue = true;
		}
		
		return returnValue;
	}
	
	/** Called when something is added or change in the inventory */
	public abstract void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack);
	
    /** Use this to load custom tags from the NBT data  */
    public abstract void loadCustomNBTData(CompoundNBT nbt);
    
    /** Use this to save custom NBT data tags */
    public abstract CompoundNBT writeCustomNBTData(CompoundNBT nbt);
	
	public void setAngle(Direction facing)
	{
		this.angle = facing;
		//System.out.println("Biblio Set Angle"); // TODO maybe this is where I should trigger a collision box change, then just cache it
		//System.out.println(world.isRemote); // so this runs both server and client. mmm hmm.
		// I might only need the voxel thing client side?
		//getBiblioShape();
		
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setShiftPosition(EnumShiftPosition position)
	{
		this.shift = position;
		//getBiblioShape();
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setVertPosition(EnumVertPosition position)
	{
		this.vertPosition = position;
		//getBiblioShape();
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	/*
	private void getBiblioShape()
	{
		AxisAlignedBB output = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		
		if (this.getBlockState().getBlock() instanceof BiblioWoodBlock && ((BiblioWoodBlock)this.getBlockState().getBlock()).isHalfBlock)
		{
			float shift = 0.0f;
			switch (this.shift)
			{
				case NO_SHIFT: { shift = 0.0f; break; }
				case HALF_SHIFT: { shift = 0.25f; break; }
				case FULL_SHIFT: { shift = 0.5f; break; }
			}
			switch (this.angle)
			{
				case SOUTH:{output = new AxisAlignedBB(0.5F-shift, 0.0F, 0.0F, 1.0F-shift, 1.0F, 1.0F); break;}
				case WEST:{output = new AxisAlignedBB(0.0F, 0.0F, 0.5F-shift, 1.0F, 1.0F, 1.0F-shift); break;}
				case NORTH:{output = new AxisAlignedBB(0.0F+shift, 0.0F, 0.0F, 0.5F+shift, 1.0F, 1.0F); break;}
				case EAST:{output = new AxisAlignedBB(0.0F, 0.0F, 0.0F+shift, 1.0F, 1.0F, 0.5F+shift); break;}
				default: {output = new AxisAlignedBB(0.0F+shift, 0.0F, 0.0F, 0.5F+shift, 1.0F, 1.0F); break;}
			}
		}
		
		//VoxelShape collision = VoxelShapes.create(output);
		this.collisionBox = VoxelShapes.create(output); 
	}
	
	public VoxelShape getCollision()
	{
		if (this.collisionBox == null)
		{
			getBiblioShape();
		}
		return this.collisionBox;
		
	}*/
	
	public Direction getAngle()
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
		//world.markForRerender(pos); broke in 1.14.4 TODO this is important 
		
		world.func_225319_b(this.getPos(), this.getBlockState(), this.getBlockState().rotate(Rotation.CLOCKWISE_90)); // this is the replacment, but its not getting called for some reason?
		//world.markBlockRangeForRenderUpdate(pos, pos);
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
		// 		System.out.println("is client: " + world.isRemote);
		// TODO am I going to have to send a packet to the client and force the update?
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
				stack = stack.split(amount);
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
    @OnlyIn(Dist.CLIENT)
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
	/*
	@Override
	public boolean hasCustomName() 
	{
		return false;
	}
*/
	@Override
	public void openInventory(PlayerEntity player) {}

	@Override
	public void closeInventory(PlayerEntity player) {}
/*
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
*/
	@Override
	public void clear() 
	{
		
	}
	
	@Override
    public void read(CompoundNBT nbt)
    {
		super.read(nbt);
		loadNBTData(nbt);
    }
	
    @Override
    public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet)
    {
    	CompoundNBT nbtData = packet.getNbtCompound();
    	loadNBTData(nbtData);
    	if (world.isRemote)
    	{
    		ModelDataManager.requestModelDataRefresh(this);
    		System.out.println("requesting model data refresh");
			//world.markForRerender(this.pos);
    	}
    	//world.markForRerender(getPos());broke in 1.14.4. // markBlockRangeForRenderUpdate(getPos(), getPos()); TODO this is important
    	
    	world.func_225319_b(this.getPos(), this.getBlockState(), this.getBlockState().rotate(Rotation.CLOCKWISE_90));
    }
    
    private void loadNBTData(CompoundNBT nbt)
    {
    	ListNBT tagList = nbt.getList("Inventory", Constants.NBT.TAG_COMPOUND);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY); //new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tagList.size(); i++)
		{
			CompoundNBT tag = (CompoundNBT) tagList.getCompound(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < this.inventory.size())
			{
				this.inventory.set(slot, ItemStack.read(tag));
			}
		}

		this.isLocked = nbt.getBoolean("locked");
		this.lockee = nbt.getString("lockee");
		// angle, shift, vert position
		this.angle = getFacingFromAngleID(nbt.getInt("angle"));
		this.shift = EnumShiftPosition.getEnumFromID(nbt.getInt("shift"));
		this.vertPosition = EnumVertPosition.getEnumFromID(nbt.getInt("position"));
		loadCustomNBTData(nbt);
		if (isRetexturable)
			this.customTexture = nbt.getString("customTexture");
		
    }
	
	@Override
    public CompoundNBT write(CompoundNBT input)
    {
		super.write(input);
		//CompoundNBT nbt = writeNBTData(input);
		//input = new CompoundNBT();
		//super.write(input);
    	return writeNBTData(input);
    }
	
	   /**
	    * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
	    * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
	    */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() 
    {
    	CompoundNBT dataTag = new CompoundNBT();
    	dataTag = writeNBTData(dataTag);
    	return new SUpdateTileEntityPacket(pos, 1, dataTag);
    }
	
	
	   /**
	    * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
	    * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
	    */
	@Override
	public CompoundNBT getUpdateTag() 
	{
		System.out.println("biblio testo 6");
		CompoundNBT tags = super.getUpdateTag();
		return writeNBTData(tags);
	}
	
    private CompoundNBT writeNBTData(CompoundNBT nbt)
    {
    	ListNBT itemList = new ListNBT();
    	for (int i = 0; i < inventory.size(); i++)
    	{
    		ItemStack stack = inventory.get(i);
    		if (stack != ItemStack.EMPTY)
    		{
    			CompoundNBT tag = new CompoundNBT();
    			tag.putByte("Slot", (byte) i);
    			stack.write(tag);
    			itemList.add(tag);
    		}
    	}
    	//nbt.put
    	nbt.put("Inventory", itemList);
    	nbt.putBoolean("locked", isLocked);
    	nbt.putString("lockee", lockee);
    	nbt.putInt("angle", getAngleIDFromFacing(angle));
    	nbt.putInt("shift", shift.getID());
    	nbt.putInt("position", vertPosition.getID());
    	nbt = writeCustomNBTData(nbt);
    	if (isRetexturable)
    		nbt.putString("customTexture", customTexture);
    	return nbt;
    }
    
    private int getAngleIDFromFacing(Direction facing)
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
    
    private Direction getFacingFromAngleID(int angle)
    {
    	Direction face = Direction.SOUTH; 
    	switch (angle)
    	{
	    	case 1:{ face = Direction.WEST; break; }
	    	case 2:{ face = Direction.NORTH; break; }
	    	case 3:{ face = Direction.EAST; break; }
	    	case 4:{ face = Direction.DOWN; break; }
	    	case 5:{ face = Direction.UP; break; }
    	}
    	return face;
    }
    
    public void updateSurroundingBlocks(Block blocktype)
    {
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(),		pos.getY(), 	pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() + 1, 	pos.getY(), 	pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() - 1, 	pos.getY(), 	pos.getZ() + 1), blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ() - 1), blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() + 1, pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() - 1, pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() + 2, 	pos.getY(), 	pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX() - 2,	pos.getY(), 	pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ() + 2), blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY(), 	pos.getZ() - 2), blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() + 2, pos.getZ()), 	 blocktype);
		world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), 		pos.getY() - 2, pos.getZ()), 	 blocktype);
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
    /* TODO shouldRefresh is gone?
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
    {
        return true;
    }
    */
    
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
	public boolean isUsableByPlayer(PlayerEntity player) 
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
