package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityMarkerPole;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockMarkerPole extends BiblioBlock
{

	public static final String name = "MarkerPole";
	public static final BlockMarkerPole instance = new BlockMarkerPole();
	
	public BlockMarkerPole() 
	{
		super(Material.IRON, SoundType.ANVIL, null, name);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        return new ArrayList<ItemStack>();
    }

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityMarkerPole();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("pole");
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
	     int pitch = MathHelper.floor(player.rotationPitch * 3.0F / 180.0F + 0.5D) & 3;
	     ++pitch;
	     pitch %= 4;
	     // if this were the lamp, this is where I would set the color data I think on placment?
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
	}

	@Override
	public ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos) 
	{
		return stack;
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
    
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return this.getBlockBounds(0.45F, 0.45F, 0.45F, 0.55F, 0.55F, 0.55F);
	}
    
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		if (tile.getVertPosition() == EnumVertPosition.FLOOR)
		{
			transform = transform.compose(new TRSRTransformation(new Vector3f(1.0f, 0.0f, 1.0f), 
																 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
																 new Vector3f(1.0f, 1.0f, 1.0f), 
																 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		}
		else if (tile.getVertPosition() == EnumVertPosition.WALL)
		{
			transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 1.0f), 
																 new Quat4f(0.0f, 0.0f, -1.0f, 1.0f), 
																 new Vector3f(1.0f, 1.0f, 1.0f), 
																 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		}
		else
		{
			// ceiling
			transform = transform.compose(new TRSRTransformation(new Vector3f(1.0f, 1.0f, 0.0f), 
																 new Quat4f(1.0f, 0.0f, 0.0f, 1.0f), 
																 new Vector3f(1.0f, 1.0f, 1.0f), 
																 new Quat4f(1.0f, 0.0f, 0.0f, 1.0f)));
		}
		return transform;
	}

	@Override
	public IBlockState getFinalBlockstate(IBlockState state, IBlockState newState) 
	{
		return newState;
	}
}
