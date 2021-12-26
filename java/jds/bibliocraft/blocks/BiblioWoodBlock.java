package jds.bibliocraft.blocks;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.Config;
import jds.bibliocraft.helpers.EnumWoodsType;
import jds.bibliocraft.states.TextureProperty;
import jds.bibliocraft.states.TextureState;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;;

public abstract class BiblioWoodBlock extends BiblioBlock
{
	//public static Properties WOOD_TYPE;// = PropertyEnum.create("woodtype", EnumWoodType.class);
	public boolean isHalfBlock = false;
	public EnumWoodsType woodType;
	
	public BiblioWoodBlock(String name, boolean isHalfBlock, EnumWoodsType wood) 
	{
		super(Material.WOOD, SoundType.WOOD, name + wood.getName());
		this.isHalfBlock = isHalfBlock;
		this.woodType = wood;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) 
	{
		return stateIn;
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) 
	{
		return getBiblioShape(state, worldIn, pos);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		return getBiblioShape(state, worldIn, pos);
	}
	
	@Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		return getBiblioShape(state, worldIn, pos);
	}
	
	// this is setup for half blocks but can be overriden for other shapes
	// since I 
	public VoxelShape getBiblioShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    	TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			if (this.isHalfBlock)
			{
				BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
				float shift = 0.0f;
				
				switch (biblioTile.getShiftPosition())
				{
					case NO_SHIFT: { shift = 0.0f; break; }
					case HALF_SHIFT: { shift = 0.25f; break; }
					case FULL_SHIFT: { shift = 0.5f; break; }
				}
				
				switch (biblioTile.getAngle())
				{
					case SOUTH:{output = this.getBlockBounds(0.5F-shift, 0.0F, 0.0F, 1.0F-shift, 1.0F, 1.0F); break;}
					case WEST:{output = this.getBlockBounds(0.0F, 0.0F, 0.5F-shift, 1.0F, 1.0F, 1.0F-shift); break;}
					case NORTH:{output = this.getBlockBounds(0.0F+shift, 0.0F, 0.0F, 0.5F+shift, 1.0F, 1.0F); break;}
					case EAST:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F+shift, 1.0F, 1.0F, 0.5F+shift); break;}
					default: {output = this.getBlockBounds(0.0F+shift, 0.0F, 0.0F, 0.5F+shift, 1.0F, 1.0F); break;}
				}
			}
			else
			{
				output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
		}
		return VoxelShapes.create(output);
	}
	
	@Override
	public ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos)
	{
		TileEntity wtile = world.getTileEntity(pos);
		if (wtile != null && wtile instanceof BiblioTileEntity)
		{
			BiblioTileEntity tile = (BiblioTileEntity)wtile; 
			if (this.woodType == woodType.framed)//tile.getBlockMetadata() == 6)
			{
				String customTexture = tile.getCustomTextureString();
				if (!customTexture.equals("none") || !customTexture.equals(""))
				{
					CompoundNBT tags = new CompoundNBT();
					tags.putString("renderTexture", customTexture);
					stack.setTag(tags);
				}
			}
		}
		return stack;
	}
	/* TODO is borked
    @Override
    protected BlockStateContainer createBlockState()
    {
    	WOOD_TYPE = PropertyEnum.create("woodtype", EnumWoodType.class);
    	return new ExtendedBlockState(this, new IProperty[]{WOOD_TYPE}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE, TextureProperty.instance});
    }
    
    @Override
    public BlockState getStateFromMeta(int meta)
    {

    	EnumWoodType wood = EnumWoodType.getEnum(meta);
    	return this.getDefaultState().withProperty(WOOD_TYPE, wood);
    }
    
    @Override
    public int getMetaFromState(BlockState state)
    {
    	EnumWoodType wood = (EnumWoodType)state.getValue(WOOD_TYPE);
    	return wood.getID();
    }
    
    @Override
    public ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state)
    {
    	return new ExtendedBlockState(this, new IProperty[]{WOOD_TYPE}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE, TextureProperty.instance});
    }
    
    @Override
    public IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state)
    {
    	TextureState textureString = new TextureState(biblioTile.getCustomTextureString()); 
    	textureString = addAdditionTextureStateInformation(biblioTile, textureString);
    	state = state.withProperty(TextureProperty.instance, textureString);
    	return state;
    }
    */
    public TextureState addAdditionTextureStateInformation(BiblioTileEntity tile, TextureState state)
    {
    	return state;
    }
    
    @Override
    public BlockState getFinalBlockstate(BlockState state, BlockState newState)
    {
    	//EnumWoodType wood = (EnumWoodType)state.getValue(WOOD_TYPE);
    	return newState;//newState.withProperty(WOOD_TYPE, wood);
    }

	/** Only runs server side on block right click if the block isnt locked or is the correct player who locked it */
	@Override
	public abstract boolean onBlockActivatedCustomCommands(World world, BlockPos pos, BlockState state, PlayerEntity player, Direction side, double hitX, double hitY, double hitZ);

	@Override
	public abstract TileEntity createNewTileEntity(IBlockReader worldIn);

	// TODO do I even need this anymore? now it happens on the model itself and is connected to the tile entity directly
    /** Create a list of model parts that should be rendered based on the TileEntity. List<String> parts = new ArrayList<String>(); */
	@Override
    public abstract List<String> getModelParts(BiblioTileEntity tile);

	@Override
	public abstract void additionalPlacementCommands(BiblioTileEntity biblioTile, LivingEntity player);
	
	public static enum EnumWoodType implements IStringSerializable
    {
    	OAK(0, "oak", "minecraft:blocks/planks_oak"),
    	SPRUCE(1, "spruce", "minecraft:blocks/planks_spruce"),
    	BIRCH(2, "birch", "minecraft:blocks/planks_birch"),
    	JUNGLE(3, "jungle", "minecraft:blocks/planks_jungle"),
    	ACACIA(4, "acacia", "minecraft:blocks/planks_acacia"),
    	DARKOAK(5, "darkoak", "minecraft:blocks/planks_big_oak"),
    	FRAME(6, "framed", "bibliocraft:blocks/frame");
    	
    	
    	private int ID;
    	private String name;
    	private String textureString;
    	private static final EnumWoodType[] META_LOOKUP = new EnumWoodType[values().length];
    	
    	private EnumWoodType(int ID, String name, String texture)
    	{
    		this.ID = ID;
    		this.name = name;
    		this.textureString = texture;
    	}

    	@Override
    	public String getName() 
    	{
    		return name;
    	}
    	
    	public int getID()
    	{
    		return ID;
    	}
    	
    	public String getTextureString()
    	{
    		return this.textureString;
    	}
    	
    	@Override
    	public String toString()
    	{
    		return getName();
    	}
    	
    	public static EnumWoodType getEnum(int meta)
    	{
    		EnumWoodType thing = OAK;
    		switch (meta)
    		{
    			case 0:{thing = OAK; break;}
    			case 1:{thing = SPRUCE; break;}
    			case 2:{thing = BIRCH; break;}
    			case 3:{thing = JUNGLE; break;}
    			case 4:{thing = ACACIA; break;}
    			case 5:{thing = DARKOAK; break;}
    			case 6:{thing = FRAME; break;}
    		}
    		return thing;
    	}
    	

        static
        {
          for (EnumWoodType wood : values()) {
            META_LOOKUP[wood.getID()] = wood;
          }
        }

    }
	/* TODO is borked
	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    	TileEntity tile = blockAccess.getTileEntity(pos);
		if (this.isHalfBlock && tile != null && tile instanceof BiblioTileEntity)
		{
			BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
			float shift = 0.0f;
			switch (biblioTile.getShiftPosition())
			{
				case NO_SHIFT: { shift = 0.0f; break; }
				case HALF_SHIFT: { shift = 0.25f; break; }
				case FULL_SHIFT: { shift = 0.5f; break; }
			}
			switch (biblioTile.getAngle())
			{
				case SOUTH:{output = this.getBlockBounds(0.5F-shift, 0.0F, 0.0F, 1.0F-shift, 1.0F, 1.0F); break;}
				case WEST:{output = this.getBlockBounds(0.0F, 0.0F, 0.5F-shift, 1.0F, 1.0F, 1.0F-shift); break;}
				case NORTH:{output = this.getBlockBounds(0.0F+shift, 0.0F, 0.0F, 0.5F+shift, 1.0F, 1.0F); break;}
				case EAST:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F+shift, 1.0F, 1.0F, 0.5F+shift); break;}
				default: {output = this.getBlockBounds(0.0F+shift, 0.0F, 0.0F, 0.5F+shift, 1.0F, 1.0F); break;}
			}
		}
		return output;
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
	{
		int output = 0;
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof BiblioTileEntity && Config.emitLight)
		{
			BiblioTileEntity tile = (BiblioTileEntity)te;
			for (int i = 0; i < tile.inventory.size(); i++)
			{
				ItemStack stack = tile.getStackInSlot(i);
				if (stack != ItemStack.EMPTY)
				{
					Block block = Block.getBlockFromItem(stack.getItem());
					if (block != null && !(block instanceof BiblioWoodBlock))
					{
						int light = block.getLightValue(block.getDefaultState()); //block.getLightValue(state, world, pos);
						if (light > output)
						{
							output = light;
							//TODO the light value is not updated right away for some reason.
						}
					}
				}
			}
		}
		
		return output;
	}
	*/
}
