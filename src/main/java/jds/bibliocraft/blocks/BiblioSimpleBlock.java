package jds.bibliocraft.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class BiblioSimpleBlock extends BiblioBlock
{
	public BiblioSimpleBlock(Material material, SoundType sound, String name)
	{
		super(material, sound, BlockLoader.biblioTab, name);
	}
	
	@Override
	public List<String> getModelParts(BiblioTileEntity tile)
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		return modelParts;
	}
	
	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) { }

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
		return state;
	}

	@Override
	public IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state)
	{
		return state;
	}
}
