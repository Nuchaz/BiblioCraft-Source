package jds.bibliocraft.blocks;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public abstract class BiblioColorBlock extends BiblioBlock
{
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumColor.class);

    public BiblioColorBlock(Material material, SoundType sound, String name)
    {
    	super(material, sound, BlockLoader.biblioTab, name);
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
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player)
	{
		
	}

	@Override
	public ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos)
	{
		return stack;
	}

	@Override
	public IBlockState getFinalBlockstate(IBlockState state, IBlockState newState)
	{
		return newState;
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile)
	{
		return transform;
	}


	@Override
	public ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state) 
	{
		ExtendedBlockState exstate = new ExtendedBlockState(this, new IProperty[] {COLOR}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE});
		return exstate;
	}

	@Override
	public IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state) 
	{
		EnumColor color = EnumColor.getColorEnumFromID(biblioTile.getBlockMetadata());
		state = (IExtendedBlockState)state.withProperty(COLOR, color);
		return state;
	}
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	return new ExtendedBlockState(this, new IProperty[] {COLOR}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE});
    }
}
