package jds.bibliocraft.blocks;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.Config;
import jds.bibliocraft.states.TextureProperty;
import jds.bibliocraft.states.TextureState;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public abstract class BiblioWoodBlock extends BiblioBlock
{
	public static PropertyEnum WOOD_TYPE;// = PropertyEnum.create("woodtype", EnumWoodType.class);
	private boolean isHalfBlock = false;
	
	public BiblioWoodBlock(String name, boolean isHalfBlock) 
	{
		super(Material.WOOD, SoundType.WOOD, BlockLoader.biblioTab, name);
		this.isHalfBlock = isHalfBlock;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int x = 0; x <= BlockLoader.NUMBER_OF_WOODS; x++)
		{
			subItems.add(new ItemStack(this, 1, x));
		}
	}
	
	@Override
	public ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos)
	{
		TileEntity wtile = world.getTileEntity(pos);
		if (wtile != null && wtile instanceof BiblioTileEntity)
		{
			BiblioTileEntity tile = (BiblioTileEntity)wtile; 
			if (tile.getBlockMetadata() == 6)
			{
				String customTexture = tile.getCustomTextureString();
				if (!customTexture.equals("none") || !customTexture.equals(""))
				{
					NBTTagCompound tags = new NBTTagCompound();
					tags.setString("renderTexture", customTexture);
					stack.setTagCompound(tags);
				}
			}
		}
		return stack;
	}
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	WOOD_TYPE = PropertyEnum.create("woodtype", EnumWoodType.class);
    	return new ExtendedBlockState(this, new IProperty[]{WOOD_TYPE}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE, TextureProperty.instance});
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {

    	EnumWoodType wood = EnumWoodType.getEnum(meta);
    	return this.getDefaultState().withProperty(WOOD_TYPE, wood);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
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
    
    public TextureState addAdditionTextureStateInformation(BiblioTileEntity tile, TextureState state)
    {
    	return state;
    }
    
    @Override
    public IBlockState getFinalBlockstate(IBlockState state, IBlockState newState)
    {
    	EnumWoodType wood = (EnumWoodType)state.getValue(WOOD_TYPE);
    	return newState.withProperty(WOOD_TYPE, wood);
    }

	/** Only runs server side on block right click if the block isnt locked or is the correct player who locked it */
	@Override
	public abstract boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ);

	@Override
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);

    /** Create a list of model parts that should be rendered based on the TileEntity. List<String> parts = new ArrayList<String>(); */
	@Override
    public abstract List<String> getModelParts(BiblioTileEntity tile);

	@Override
	public abstract void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player);
	
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

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
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
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
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
}
