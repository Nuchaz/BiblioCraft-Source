package jds.bibliocraft.blocks;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityLabel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockLabel extends BiblioWoodBlock
{
	public static final String name = "Label";
	public static final BlockLabel instance = new BlockLabel();

	public BlockLabel()
	{
		super(name, false);
	}

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote && player.isSneaking())
		{
			player.openGui(BiblioCraft.instance, 6, world, pos.getX(), pos.getY(), pos.getZ()); 
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityLabel();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		
		
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.0f, 0.0f, -0.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
			     new Vector3f(1.0f, 1.0f, 1.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tile = blockAccess.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityLabel)
		{
			TileEntityLabel labelTile = (TileEntityLabel) tile;
			switch (labelTile.getAngle())
			{
				case SOUTH:{output = this.getBlockBounds(0.94F, 0.12F, 0.28F, 1.0F, 0.38F, 0.72F);break;}
				case WEST:{output = this.getBlockBounds(0.28F, 0.12F, 0.94F, 0.72F, 0.38F, 1.0F);break;}
				case NORTH:{output = this.getBlockBounds(0.0F, 0.12F, 0.28F, 0.06F, 0.38F, 0.72F);break;}
				case EAST:{output = this.getBlockBounds(0.28F, 0.12F, 0.0F, 0.72F, 0.38F, 0.06F);break;}
				default:break;
			}
		}
		return output;
	}
    
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return null;
    }
}
