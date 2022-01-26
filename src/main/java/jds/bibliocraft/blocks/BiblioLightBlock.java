package jds.bibliocraft.blocks;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumMetalType;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.states.MetalTypeProperty;
import jds.bibliocraft.states.MetalTypeState;
import jds.bibliocraft.tileentities.BiblioLightTileEntity;
import jds.bibliocraft.tileentities.BiblioTileEntity;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public abstract class BiblioLightBlock extends BiblioBlock
{
	public static String name = "bibliolight";
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumColor.class);
	
	public BiblioLightBlock(String name)
	{
		super(Material.IRON, SoundType.METAL, BlockLoader.biblioLightsTab, name);
		setLightLevel(1.0f);
	}

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new BiblioLightTileEntity();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int x = 0; x < 16; x++) /// 16 for 16 colors
		{
			subItems.add(new ItemStack(this, 1, x));
		}
	}
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	return new ExtendedBlockState(this, new IProperty[] {COLOR}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE, MetalTypeProperty.instance});//new BlockState(this, new IProperty[] {COLOR});
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	EnumColor color = EnumColor.getColorEnumFromID(meta);
    	return this.getDefaultState().withProperty(COLOR, color);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	EnumColor color = (EnumColor)state.getValue(COLOR);
    	return color.getID();
    }
    
	@Override
	public ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state) 
	{
		ExtendedBlockState exstate = new ExtendedBlockState(this, new IProperty[] {COLOR}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE, MetalTypeProperty.instance});
		return exstate;
	}

	@Override
	public IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state) 
	{
		// This is where I apply my attributes before returning. This is the blockstate that goes to my custom model
		MetalTypeState metal = new MetalTypeState(EnumMetalType.GOLD);
		if (biblioTile instanceof BiblioLightTileEntity)
		{
			BiblioLightTileEntity tile = (BiblioLightTileEntity)biblioTile;
			metal = new MetalTypeState(tile.getLightType());
		}
		EnumColor color = EnumColor.getColorEnumFromID(biblioTile.getBlockMetadata());
		state = (IExtendedBlockState)state.withProperty(MetalTypeProperty.instance, metal).withProperty(COLOR, color);
		return state;
	}

	@Override
	public abstract List<String> getModelParts(BiblioTileEntity tile);
	
	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
	     int pitch = MathHelper.floor(player.rotationPitch * 3.0F / 180.0F + 0.5D) & 3;
	     ++pitch;
	     pitch %= 4;
	     if (pitch == 0)
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.CEILING);
	     }
	     else if (pitch == 1)
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.WALL);
	     }
	     else
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.FLOOR);
	     }
	     additionalLightPlacmentCommands(biblioTile);
	}
	
	public abstract void additionalLightPlacmentCommands(BiblioTileEntity biblioTile);

	@Override
	public ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos) 
	{
		return stack;
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
				 new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				 new Vector3f(1.0f, 1.0f, 1.0f), 
				 new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public IBlockState getFinalBlockstate(IBlockState state, IBlockState newState) 
	{
		return newState;
	}

}
