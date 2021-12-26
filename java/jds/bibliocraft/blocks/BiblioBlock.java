package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;
/*

import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemLock;
import jds.bibliocraft.tileentities.TileEntityTable;
*/
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.CustomBlockItemDataPack;
import jds.bibliocraft.states.TextureProperty;
import jds.bibliocraft.states.TextureState;
import jds.bibliocraft.tileentities.BiblioTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
//import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockReader;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.util.Constants;


public abstract class BiblioBlock extends ContainerBlock 
{
	//private boolean hasCustomWoods = false;
	
	private String customTexture = "none";
	private CustomBlockItemDataPack customData = null;
	
	
	public BiblioBlock(Material material, SoundType sound, String name)
	{
		
		super(Block.Properties.create(material).sound(sound));//   Properties.create(material));
		//this.setSoundType(sound);
		//this.soundType
		//setStepSound(sound);
		//if (tab != null)
		//{
		//	setCreativeTab(tab);
		//}

		//setUnlocalizedName("BiblioCraft:" + name);
		setRegistryName(name);
		//setRegistryName("bibliocraft:" + name);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) 
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() 
	{
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	public boolean isSolid(BlockState state) 
	{
		// this determines if the block should hide faces around it. I could use block states and check against the solid sides of the block or just leave this false.
		return false;
	}
	
	// public boolean onBlockActivated(BlockState p_220051_1_, World p_220051_2_, BlockPos p_220051_3_, PlayerEntity p_220051_4_, Hand p_220051_5_, BlockRayTraceResult p_220051_6_) {
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace)
	{
		
		boolean returnValue = true;
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			
			BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
			String playername = player.getDisplayName().getUnformattedComponentText();
			boolean islocked = biblioTile.isLocked();
			String lockeename = biblioTile.getLockee();
			if (!world.isRemote)
			{
				 ItemStack playerhand = player.getHeldItem(Hand.MAIN_HAND);
				 if (playerhand != ItemStack.EMPTY)
				 {
					 /* TODO temp
					 if (playerhand.getItem() instanceof ItemLock)
					 {
							 if (islocked)
							 {
								 if (playername.contains(lockeename))
								 {
									 biblioTile.setLocked(false); 
									 player.sendMessage(new TextComponentString(LanguageMap.getInstance().translateKey("lock.unlocked")));
								 }
								 else
								 {
									 player.sendMessage(new TextComponentString(LanguageMap.getInstance().translateKey("lock.notowner")));
								 }
							 }
							 else
							 {
								 biblioTile.setLocked(true);
								 biblioTile.setLockee(playername);
								 player.sendMessage(new TextComponentString(LanguageMap.getInstance().translateKey("lock.locked")));
							 }
							 return true;
					 }*/
				 }
				 
				if (!islocked || playername.contains(lockeename))  // The fix to all these lock issues is to use the playername.contains(lockeename) instead of an ==
				{
					if (playerhand != ItemStack.EMPTY)
					{
						/* TODO temp
						 *
						if (playerhand.getItem() instanceof ItemDrill && !(biblioTile instanceof TileEntityTable))
						{
							return false;
						}
						*/
					}
					returnValue = onBlockActivatedCustomCommands(world, pos, state, player, trace.getFace(), trace.getHitVec().x, trace.getHitVec().y, trace.getHitVec().z);
				}
				else
				{
					player.sendMessage(new StringTextComponent(LanguageMap.getInstance().translateKey("lock.notowner")));
				}
			}
			else
			{
				//client side
				returnValue = onBlockActivatedCustomCommands(world, pos, state, player, trace.getFace(), trace.getHitVec().x, trace.getHitVec().y, trace.getHitVec().z);
				ItemStack playerhand = player.getHeldItem(Hand.MAIN_HAND);
				if (playerhand != ItemStack.EMPTY)
				{
					/* TODO temp
					if (playerhand.getItem() instanceof ItemDrill)
					{
						returnValue = false;
					}*/
				}
			}
		}
		
		return returnValue;
	}
	/* gone in 1.13 I guess?
	@Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
    {
        return true;
    }*/

	/** Only runs server side on block right click if the block isnt locked or is the correct player who locked it */
	public abstract boolean onBlockActivatedCustomCommands(World world, BlockPos pos, BlockState state, PlayerEntity player, Direction side, double x, double y, double z);
	
	@Override 
	public abstract TileEntity createNewTileEntity(IBlockReader worldIn);
	
    /** Create a list of model parts that should be rendered based on the TileEntity. List<String> parts = new ArrayList<String>(); */
    public abstract List<String> getModelParts(BiblioTileEntity tile);
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof BiblioTileEntity)
        {
        	BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
        	this.customTexture = biblioTile.getCustomTextureString();
        	this.customData = getCustomDataOnHarvest(biblioTile);
        	ItemStack newStack = new ItemStack(this);
        	if (!(customTexture.contentEquals("none") || customTexture.contentEquals(""))) 
        	{
        		CompoundNBT tags = new CompoundNBT();
        		tags.putString("renderTexture", this.customTexture);
        		newStack.setTag(tags);
        	}
        	if (this.customData != null && this.customData.hasData) 
        	{
        		CompoundNBT tags = new CompoundNBT();
        		if (newStack.getTag() != null)
        		{
        			tags = newStack.getTag();
        		}
        		tags = this.customData.applyDataToItemStack(tags);
        		newStack.setTag(tags);
        	}
        	//spawn my newstack?
        	spawnAsEntity(world, pos, newStack);
        	//InventoryHelper.dropInventoryItems(world, pos, (IInventory)biblioTile); // maybe this work?
        	// the bookcase itself isn't spawning?
        }
        super.onBlockHarvested(world, pos, state, player);
    }
	
	public CustomBlockItemDataPack getCustomDataOnHarvest(BiblioTileEntity tile)
	{
		return new CustomBlockItemDataPack();
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) 
	{
		if (state.getBlock() != newState.getBlock()) 
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof IInventory) 
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
				//worldIn.updateComparatorOutputLevel(pos, this);
			}
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
	
	@Override
	public void spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) // the stack is the item used to break the block
	{
		// maybe I can drop my block here?
		System.out.println("drop: " + stack.getDisplayName().getString());
	}
	
	
	/* TODO this is broken in 1.13 or I dont understand it or something
	@Override
	public IItemProvider getItemDropped(BlockState state, World world, BlockPos pos, int fortune)
    {
        ArrayList<ItemStack> retern = new ArrayList<ItemStack>();
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
        
        TileEntity tile = world.getTileEntity(pos);
        int count = quantityDropped(state, rand); // TODO not more fortune setting? also this is deprechiated, fix it
        for(int i = 0; i < count; i++)
        {
        	//IItemProvider iprov = getItemDropped(state, world, pos, fortune);
            Item item = getItemDropped(state, rand, fortune);
            if (item != ItemStack.EMPTY.getItem())
            {
            	ItemStack newStack = new ItemStack(item, 1, damageDropped(state));
            	if (!(customTexture.contentEquals("none") || customTexture.contentEquals(""))) 
            	{
            		CompoundNBT tags = new CompoundNBT();
            		tags.setString("renderTexture", this.customTexture);
            		newStack.setTag(tags);
            	}
            	if (this.customData != null && this.customData.hasData) 
            	{
            		CompoundNBT tags = new CompoundNBT();
            		if (newStack.getTag() != null)
            		{
            			tags = newStack.getTag();
            		}
            		tags = this.customData.applyDataToItemStack(tags);
            		newStack.setTag(tags);
            	}
            	
            	retern.add(newStack);
            }
        }
        
        return null; // TODO FIX THIS
    }
	*/
	public BlockPos getDropPositionOffset(BlockPos oldPos, PlayerEntity player) // move this to parent class I think.
	{
		BlockPos playerPos = player.getPosition();
		
		int x = oldPos.getX() - playerPos.getX();
		int z = oldPos.getZ() - playerPos.getZ();
		if (x > 1)
			x = 1;
		if (x < -1)
			x = -1;
		if (z > 1)
			z = 1;
		if (z < -1)
			z = -1;
		x = oldPos.getX() - x;
		z = oldPos.getZ() - z;
		BlockPos pos = new BlockPos(x, oldPos.getY(), z);
		return pos;
	}
	
	@Override
    public float getBlockHardness(BlockState state, IBlockReader world, BlockPos pos)
    {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			BiblioTileEntity tilee = (BiblioTileEntity)tile;
			if (tilee.isLocked())
			{
				return -1.0F;
			}
		}
		return 3.0F;
    }
	/* I guess this is all gone is `1.13
	@Override
	public int damageDropped(BlockState state)
	{
		return state.getBlock().getMetaFromState(state); 
	}
    
	
    @Deprecated
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionShape(worldIn, pos));
    }
    /*
    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, BlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBoundsBasedOnState(world, pos);
        super.addCollisionBoxesToList(world, pos, state, mask, list, collidingEntity);
	}
    */ 
	
    public Direction getFacing(int angle)
    {
    	Direction face = Direction.WEST;
    	switch (angle)
    	{
	    	case 0:{ face = Direction.SOUTH; break; }
	    	case 2:{ face = Direction.NORTH; break; }
	    	case 3:{ face = Direction.EAST; break; }
    	}
    	return face;
    }
    //public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack)
    {
    	TileEntity tile = world.getTileEntity(pos);
    	if (tile != null && tile instanceof BiblioTileEntity)
    	{
	        int angle = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
	        ++angle;
	        angle %= 4;
	        
	        BiblioTileEntity biblioTile = (BiblioTileEntity) world.getTileEntity(pos);
	        biblioTile.setAngle(getFacing(angle));
	        CompoundNBT tags = itemStack.getTag();
	        if (tags != null)
	        {

	        	if (tags.contains("renderTexture"))
	        	{
	        		biblioTile.setCustomTexureString(tags.getString("renderTexture"));
	        	}

	        	if (tags.contains("textscale"))
	        	{
	        		customData.applyDataToBlock(tags, biblioTile); 
	        	}
	        	
	        }
	        additionalPlacementCommands(biblioTile, player);
    	}
    }
    
    /** Called when the block is placed  */
    public abstract void additionalPlacementCommands(BiblioTileEntity biblioTile, LivingEntity player);
    
    /* This no longer works, maybe not needed?
	@Override
	public void breakBlock(World world, BlockPos pos, BlockState state)
	{
		if (!(state.getBlock() instanceof BlockFancySign)) // This is so the fancy sign retains its inventory
			InventoryHelper.dropInventoryItems(world, pos, (IInventory)tileentity);//dropItems(world, pos);  I can use InventoryHlper.dropInventoryItems now to easy dropages
		super.breakBlock(world, pos, state);
	}
	/*
	public void dropItems(World world, BlockPos pos)
	{
		Random rando = new Random();
		
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!(tileEntity instanceof IInventory))
		{
			return;
		}
		IInventory inventory = (IInventory) tileEntity;
		
		for (int x = 0; x < inventory.getSizeInventory(); x++)
		{
			ItemStack item = inventory.getStackInSlot(x);
			
			if (item != ItemStack.EMPTY && item.getCount() > 0)
			{
				float ri = rando.nextFloat() * 0.8F + 0.1F;
				float rj = rando.nextFloat() * 0.8F + 0.1F;
				float rk = rando.nextFloat() * 0.8F + 0.1F;
				
				ItemEntity entityItem = new ItemEntity(world, pos.getX() + ri, pos.getY() + rj, pos.getZ() + rk, new ItemStack(item.getItem(), item.getCount(), item.getTag()));
				
				if (item.hasTag())
				{
					entityItem.getItem().setTag((CompoundNBT) item.getTag().copy());
				}
				
				float factor = 0.05F;
				entityItem.motionX = rando.nextGaussian() * factor;
				entityItem.motionY = rando.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rando.nextGaussian() * factor;
				world.spawnEntity(entityItem);
				item.setCount(0);
			}
		}
	}
	/*
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult trace, World world, BlockPos pos, PlayerEntity player)
	{
       ItemStack item = getItem(world, pos, state);
       if (item == ItemStack.EMPTY)
       {
           return ItemStack.EMPTY;
       }
       Block block = item.getItem() instanceof ItemBlock ? Block.getBlockFromItem(item.getItem()) : this;
       ItemStack stack = new ItemStack(item.getItem(), 1, block.getMetaFromState(state));
       stack = getPickBlockExtras(stack, world, pos);
       return stack;
	}
	*/
	/** Additional stuff for the getPickBlock method if needed, just return the stack if not */
	public abstract ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos);
	
/*
    @Override
    public boolean isFullCube(BlockState state) { return false; }

    @Override
    public boolean isFullyOpaque(BlockState state) { return false; }
  
    @Override
    public EnumBlockRenderType getRenderType(BlockState state) { return EnumBlockRenderType.MODEL; }
	/*
	@Override
	public Direction[] getValidRotations(World worldObj, BlockPos pos) 
	{
		Direction[] axises = {Direction.UP, Direction.DOWN};
		return axises;
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, Direction axis) 
	{
		boolean returnValue = false;
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			BiblioTileEntity te = (BiblioTileEntity)tile;
			Direction angle = te.getAngle();
			returnValue = true;
			switch (axis)
			{
				case DOWN: 
				{
					te.setAngle(angle.rotateY());
					break;
				}
				case UP:
				{
					te.setAngle(angle.rotateYCCW());
					break;
				}
				default: returnValue = false;
			}
		}
		return returnValue;
	}
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	//return new BlockStateContainer(this, new IProperty[0]);
    	return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE});
    }
    
    @Override
    public BlockState getStateFromMeta(int meta)
    {
    	return  this.getDefaultState();
    }
    
    @Override
    public int getMetaFromState(BlockState state)
    {
    	return 0;
    }
    
    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
    	ExtendedBlockState exstate = new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE});
    	BlockState st = new BlockState(this, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE});
    	exstate = getExtendedBlockStateAlternate(exstate);
    	BlockState newState = ((IExtendedBlockState) exstate.getBaseState());
    	OBJModel.OBJState partList = new OBJModel.OBJState(Lists.newArrayList(OBJModel.Group.ALL), true);
    	TileEntity tile = world.getTileEntity(pos);
    	if (tile != null && tile instanceof BiblioTileEntity)
    	{
    		BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
    		List<String> modelParts = getModelParts(biblioTile);
    		TRSRTransformation transform = new TRSRTransformation(biblioTile.getAngle());
    		switch (biblioTile.getShiftPosition())
    		{
	    		case HALF_SHIFT: 
	    		{
	    			transform = transform.compose(new TRSRTransformation(new Vector3f(0.25f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
	    			break; 
    			}
	    		case FULL_SHIFT:
	    		{ 
	    			transform = transform.compose(new TRSRTransformation(new Vector3f(0.5f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
	    			break; 
	    		}
	    		default: break;
    		}
    		transform = getAdditionalTransforms(transform, biblioTile);
    		partList = new OBJModel.OBJState(modelParts, true, transform);
    		newState = newState.with(OBJModel.OBJProperty.INSTANCE, partList);
    		//newState.with
    		newState = getIExtendedBlockStateAlternate(biblioTile, newState);
    	}
    	
    	return getFinalBlockstate(state, newState);
    }
    */
    public abstract BlockState getFinalBlockstate(BlockState state, BlockState newState);
    
    public abstract TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile);
    
    /** This can be used to change the default extended block state for adding additional properties */
    //public abstract ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state);
    
    /** Allows changing and adding properties to the IExtendedBlockState that is to be returned */
   // public abstract IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state);
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer)
    {
    	//System.out.println("Block Render Layer: " + layer.toString()); // TODO temp
    	boolean output = false;
    	if (layer == layer.CUTOUT)
    	{
    		output = true;
    	}
        return output;
    }
    
    
    public static boolean isFrontOfBlock(Direction face, Direction angle)
    {
    	boolean returnValue = false;
    	if ((face == Direction.SOUTH && angle == Direction.EAST) ||
			(face == Direction.WEST && angle == Direction.SOUTH) ||
			(face == Direction.NORTH && angle == Direction.WEST) ||
			(face == Direction.EAST && angle == Direction.NORTH))
    	{
    		returnValue = true;
    	}
    	return returnValue;
    }
    
    public static boolean isBackOfBlock(Direction face, Direction angle)
    {
    	boolean returnValue = false;
    	if ((face == Direction.SOUTH && angle == Direction.WEST) ||
			(face == Direction.WEST && angle == Direction.NORTH) ||
			(face == Direction.NORTH && angle == Direction.EAST) ||
			(face == Direction.EAST && angle == Direction.SOUTH))
    	{
    		returnValue = true;
    	}
    	return returnValue;
    }
   
 	public static int getSlotNumberFromClickon2x2block(Direction angle, float hitX, float hitY, float hitZ)
 	{
 		 int xCheck = (int)(hitX * 2);
 		 int yCheck = (int)(hitY * 2);
 		 int zCheck = (int)(hitZ * 2);
 		 int slot = -1;
 		 
 		if ((yCheck == 1 && zCheck == 0 && angle == Direction.SOUTH) || (yCheck == 1 && xCheck == 1 && angle == Direction.WEST) || (yCheck == 1 && zCheck == 1 && angle == Direction.NORTH) || (yCheck == 1 && xCheck == 0 && angle == Direction.EAST))
 		 {
 			 slot = 0;
 		 }
 		 if ((yCheck == 1 && zCheck == 1 && angle == Direction.SOUTH) || (yCheck == 1 && xCheck == 0 && angle == Direction.WEST) || (yCheck == 1 && zCheck == 0 && angle == Direction.NORTH) || (yCheck == 1 && xCheck == 1 && angle == Direction.EAST))
 		 {
 			 slot = 1;
 		 }
 		 if ((yCheck == 0 && zCheck == 0 && angle == Direction.SOUTH) || (yCheck == 0 && xCheck == 1 && angle == Direction.WEST) || (yCheck == 0 && zCheck == 1 && angle == Direction.NORTH) || (yCheck == 0 && xCheck == 0 && angle == Direction.EAST))
 		 {
 			 slot = 2;
 		 }
 		 if ((yCheck == 0 && zCheck == 1 && angle == Direction.SOUTH) || (yCheck == 0 && xCheck == 0 && angle == Direction.WEST) || (yCheck == 0 && zCheck == 0 && angle == Direction.NORTH) || (yCheck == 0 && xCheck == 1 && angle == Direction.EAST))
 		 {
 			 slot = 3;
 		 }
 		 return slot;
 	}
 	
 	  public AxisAlignedBB getBlockBounds(float x1, float y1, float z1, float x2, float y2, float z2)
 	  {
 		  return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
 	  }
 	  
}
