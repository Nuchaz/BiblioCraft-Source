package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import jds.bibliocraft.helpers.CustomBlockItemDataPack;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemLock;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BiblioBlock extends BlockContainer 
{
	//private boolean hasCustomWoods = false;
	
	private String customTexture = "none";
	private CustomBlockItemDataPack customData = null;
	
	
	public BiblioBlock(Material material, SoundType sound, CreativeTabs tab, String name)
	{
		super(material);
		this.setSoundType(sound);
		//setStepSound(sound);
		if (tab != null)
		{
			setCreativeTab(tab);
		}

		setUnlocalizedName("BiblioCraft:" + name);
		setRegistryName(name);
		//setRegistryName("bibliocraft:" + name);
	}
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		boolean returnValue = true;
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
			String playername = player.getDisplayNameString();
			boolean islocked = biblioTile.isLocked();
			String lockeename = biblioTile.getLockee();
			if (!world.isRemote)
			{
				 ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
				 if (playerhand != ItemStack.EMPTY)
				 {
					 if (playerhand.getItem() instanceof ItemLock)
					 {
							 if (islocked)
							 {
								 if (playername.contains(lockeename))
								 {
									 biblioTile.setLocked(false); 
									 player.sendMessage(new TextComponentString(I18n.translateToLocal("lock.unlocked")));
								 }
								 else
								 {
									 player.sendMessage(new TextComponentString(I18n.translateToLocal("lock.notowner")));
								 }
							 }
							 else
							 {
								 biblioTile.setLocked(true);
								 biblioTile.setLockee(playername);
								 player.sendMessage(new TextComponentString(I18n.translateToLocal("lock.locked")));
							 }
							 return true;
					 }
				 }
				if (!islocked || playername.contains(lockeename))  // The fix to all these lock issues is to use the playername.contains(lockeename) instead of an ==
				{
					if (playerhand != ItemStack.EMPTY)
					{
						if (playerhand.getItem() instanceof ItemDrill && !(biblioTile instanceof TileEntityTable))
						{
							return false;
						}
						
					}
					returnValue = onBlockActivatedCustomCommands(world, pos, state, player, side, hitX, hitY, hitZ);
				}
				else
				{
					player.sendMessage(new TextComponentString(I18n.translateToLocal("lock.notowner")));
				}
			}
			else
			{
				//client side
				returnValue = onBlockActivatedCustomCommands(world, pos, state, player, side, hitX, hitY, hitZ);
				ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
				if (playerhand != ItemStack.EMPTY)
				{
					if (playerhand.getItem() instanceof ItemDrill)
					{
						returnValue = false;
					}
				}
			}
		}
		
		return returnValue;
	}
	
	@Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

	/** Only runs server side on block right click if the block isnt locked or is the correct player who locked it */
	public abstract boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ);
	
	@Override 
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);
	
    /** Create a list of model parts that should be rendered based on the TileEntity. List<String> parts = new ArrayList<String>(); */
    public abstract List<String> getModelParts(BiblioTileEntity tile);
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof BiblioTileEntity)
        {
        	BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
        	this.customTexture = biblioTile.getCustomTextureString();
        	this.customData = getCustomDataOnHarvest(biblioTile);
        }
    }
	
	public CustomBlockItemDataPack getCustomDataOnHarvest(BiblioTileEntity tile)
	{
		return new CustomBlockItemDataPack();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ArrayList<ItemStack> retern = new ArrayList<ItemStack>();
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
        
        TileEntity tile = world.getTileEntity(pos);
        int count = quantityDropped(state, fortune, rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(state, rand, fortune);
            if (item != ItemStack.EMPTY.getItem())
            {
            	ItemStack newStack = new ItemStack(item, 1, damageDropped(state));
            	if (!(customTexture.contentEquals("none") || customTexture.contentEquals("")))
            	{
            		NBTTagCompound tags = new NBTTagCompound();
            		tags.setString("renderTexture", this.customTexture);
            		newStack.setTagCompound(tags);
            	}
            	if (this.customData != null && this.customData.hasData) 
            	{
            		NBTTagCompound tags = new NBTTagCompound();
            		if (newStack.getTagCompound() != null)
            		{
            			tags = newStack.getTagCompound();
            		}
            		tags = this.customData.applyDataToItemStack(tags);
            		newStack.setTagCompound(tags);
            	}
            	
            	retern.add(newStack);
            }
        }
        
        return retern;
    }
	
	public BlockPos getDropPositionOffset(BlockPos oldPos, EntityPlayer player) // move this to parent class I think.
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
	
	public void dropStackInSlot(World world, BlockPos pos, int slot, BlockPos extractPos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(!(tileEntity instanceof IInventory))
		{
			return;
		}
		
		IInventory inventory = (IInventory) tileEntity;
		BiblioTileEntity biblioTile = (BiblioTileEntity) tileEntity;
		ItemStack stack;
		stack = biblioTile.getStackInSlot(slot);
		if (stack != ItemStack.EMPTY && stack.getCount() > 0)
		{
			float adjusti = 0.0F;
			float adjustk = 0.0F;
			switch (biblioTile.getAngle())
			{
				case SOUTH: {adjusti = -0.2F; adjustk = 0.0F; break;}
				case WEST: {adjusti = 0.0F; adjustk = -0.2F; break;}
				case NORTH: {adjusti = 0.2F; adjustk = 0.0F; break;}
				case EAST: {adjusti = 0.0F; adjustk = 0.2F; break;}
				default: {adjusti = 0.2F; adjustk = 0.0F; break;}
			}
			
			EntityItem entityItem = new EntityItem(world, extractPos.getX()+0.5F+adjusti, extractPos.getY() + 0.5F, extractPos.getZ() +0.5F+adjustk, new ItemStack(stack.getItem(), stack.getCount(), stack.getItemDamage()));
			
			if (stack.hasTagCompound())
			{
				entityItem.getItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
			}
			
			entityItem.motionX = 0;
			entityItem.motionY = 0;
			entityItem.motionZ = 0;
			world.spawnEntity(entityItem);
			stack.setCount(0);
		}
		
	}
	
	@Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos)
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
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getBlock().getMetaFromState(state); 
	}
    
	
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
    }
    /*
    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBoundsBasedOnState(world, pos);
        super.addCollisionBoxesToList(world, pos, state, mask, list, collidingEntity);
	}
    */ 
    public EnumFacing getFacing(int angle)
    {
    	EnumFacing face = EnumFacing.WEST;
    	switch (angle)
    	{
	    	case 0:{ face = EnumFacing.SOUTH; break; }
	    	case 2:{ face = EnumFacing.NORTH; break; }
	    	case 3:{ face = EnumFacing.EAST; break; }
    	}
    	return face;
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack itemStack)
    {
    	TileEntity tile = world.getTileEntity(pos);
    	if (tile != null && tile instanceof BiblioTileEntity)
    	{
	        int angle = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
	        ++angle;
	        angle %= 4;
	        
	        BiblioTileEntity biblioTile = (BiblioTileEntity) world.getTileEntity(pos);
	        biblioTile.setAngle(getFacing(angle));
	        NBTTagCompound tags = itemStack.getTagCompound();
	        if (tags != null)
	        {
	        	if (tags.hasKey("renderTexture"))
	        	{
	        		biblioTile.setCustomTexureString(tags.getString("renderTexture"));
	        	}

	        	if (tags.hasKey("textscale"))
	        	{
	        		customData.applyDataToBlock(tags, biblioTile); 
	        	}
	        	
	        }
	        additionalPlacementCommands(biblioTile, player);
    	}
    }
    
    /** Called when the block is placed  */
    public abstract void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player);
    
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!(state.getBlock() instanceof BlockFancySign)) // This is so the fancy sign retains its inventory
			dropItems(world, pos); 
		super.breakBlock(world, pos, state);
	}
	
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
				
				EntityItem entityItem = new EntityItem(world, pos.getX() + ri, pos.getY() + rj, pos.getZ() + rk, new ItemStack(item.getItem(), item.getCount(), item.getItemDamage()));
				
				if (item.hasTagCompound())
				{
					entityItem.getItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
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
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult trace, World world, BlockPos pos, EntityPlayer player)
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
	
	/** Additional stuff for the getPickBlock method if needed, just return the stack if not */
	public abstract ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos);
	
    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }
/*
    @Override
    public boolean isFullyOpaque(IBlockState state) { return false; }
*/  
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) { return EnumBlockRenderType.MODEL; }
	
	@Override
	public EnumFacing[] getValidRotations(World worldObj, BlockPos pos) 
	{
		EnumFacing[] axises = {EnumFacing.UP, EnumFacing.DOWN};
		return axises;
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) 
	{
		boolean returnValue = false;
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			BiblioTileEntity te = (BiblioTileEntity)tile;
			EnumFacing angle = te.getAngle();
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
    public IBlockState getStateFromMeta(int meta)
    {
    	return  this.getDefaultState();
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	return 0;
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
    	ExtendedBlockState exstate = new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE});
    	exstate = getExtendedBlockStateAlternate(exstate);
    	IExtendedBlockState newState = ((IExtendedBlockState) exstate.getBaseState());
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
    		newState = newState.withProperty(OBJModel.OBJProperty.INSTANCE, partList);
    		newState = getIExtendedBlockStateAlternate(biblioTile, newState);
    	}
    	
    	return getFinalBlockstate(state, newState);
    }
    
    public abstract IBlockState getFinalBlockstate(IBlockState state, IBlockState newState);
    
    public abstract TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile);
    
    /** This can be used to change the default extended block state for adding additional properties */
    public abstract ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state);
    
    /** Allows changing and adding properties to the IExtendedBlockState that is to be returned */
    public abstract IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state);
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
    	boolean output = false;
    	if (layer == layer.CUTOUT)
    	{
    		output = true;
    	}
        return output;
    }
    
    
    public static boolean isFrontOfBlock(EnumFacing face, EnumFacing angle)
    {
    	boolean returnValue = false;
    	if ((face == EnumFacing.SOUTH && angle == EnumFacing.EAST) ||
			(face == EnumFacing.WEST && angle == EnumFacing.SOUTH) ||
			(face == EnumFacing.NORTH && angle == EnumFacing.WEST) ||
			(face == EnumFacing.EAST && angle == EnumFacing.NORTH))
    	{
    		returnValue = true;
    	}
    	return returnValue;
    }
    
    public static boolean isBackOfBlock(EnumFacing face, EnumFacing angle)
    {
    	boolean returnValue = false;
    	if ((face == EnumFacing.SOUTH && angle == EnumFacing.WEST) ||
			(face == EnumFacing.WEST && angle == EnumFacing.NORTH) ||
			(face == EnumFacing.NORTH && angle == EnumFacing.EAST) ||
			(face == EnumFacing.EAST && angle == EnumFacing.SOUTH))
    	{
    		returnValue = true;
    	}
    	return returnValue;
    }
   
 	public static int getSlotNumberFromClickon2x2block(EnumFacing angle, float hitX, float hitY, float hitZ)
 	{
 		 int xCheck = (int)(hitX * 2);
 		 int yCheck = (int)(hitY * 2);
 		 int zCheck = (int)(hitZ * 2);
 		 int slot = -1;
 		 
 		if ((yCheck == 1 && zCheck == 0 && angle == EnumFacing.SOUTH) || (yCheck == 1 && xCheck == 1 && angle == EnumFacing.WEST) || (yCheck == 1 && zCheck == 1 && angle == EnumFacing.NORTH) || (yCheck == 1 && xCheck == 0 && angle == EnumFacing.EAST))
 		 {
 			 slot = 0;
 		 }
 		 if ((yCheck == 1 && zCheck == 1 && angle == EnumFacing.SOUTH) || (yCheck == 1 && xCheck == 0 && angle == EnumFacing.WEST) || (yCheck == 1 && zCheck == 0 && angle == EnumFacing.NORTH) || (yCheck == 1 && xCheck == 1 && angle == EnumFacing.EAST))
 		 {
 			 slot = 1;
 		 }
 		 if ((yCheck == 0 && zCheck == 0 && angle == EnumFacing.SOUTH) || (yCheck == 0 && xCheck == 1 && angle == EnumFacing.WEST) || (yCheck == 0 && zCheck == 1 && angle == EnumFacing.NORTH) || (yCheck == 0 && xCheck == 0 && angle == EnumFacing.EAST))
 		 {
 			 slot = 2;
 		 }
 		 if ((yCheck == 0 && zCheck == 1 && angle == EnumFacing.SOUTH) || (yCheck == 0 && xCheck == 0 && angle == EnumFacing.WEST) || (yCheck == 0 && zCheck == 0 && angle == EnumFacing.NORTH) || (yCheck == 0 && xCheck == 1 && angle == EnumFacing.EAST))
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
