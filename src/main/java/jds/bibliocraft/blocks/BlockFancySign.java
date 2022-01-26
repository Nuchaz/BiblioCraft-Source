package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.helpers.CustomBlockItemDataPack;
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockFancySign extends BiblioWoodBlock
{
	public static final String name = "FancySign";
	public static final BlockFancySign instance = new BlockFancySign();
	
	public BlockFancySign() 
	{
		super(name, false);
	}
	
	@Override 
	public CustomBlockItemDataPack getCustomDataOnHarvest(BiblioTileEntity tile)
	{
		CustomBlockItemDataPack data = new CustomBlockItemDataPack();
		if (tile instanceof TileEntityFancySign)
		{
			TileEntityFancySign sign = (TileEntityFancySign)tile;
			if (sign.doesSignContainUserData())
				data.AddFancySignData(sign.inventory, sign.slot1Scale, sign.slot1Rot, sign.slot1X, sign.slot1Y, sign.slot2Scale, sign.slot2Rot, sign.slot2X, sign.slot2Y, sign.numOfLines, sign.textScale, sign.text);
		}
		return data;
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote && player.isSneaking())
		{
			player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ()); 
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityFancySign();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("sign");
		modelParts.add("front");
		if (tile.getVertPosition() == EnumVertPosition.FLOOR)
		{
			modelParts.add("feetBottom");
		}
		if (tile.getVertPosition() == EnumVertPosition.CEILING)
		{
			modelParts.add("feetTop");
		}
		return modelParts;
	}

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
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.0f, 0.0f, -0.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
			     new Vector3f(1.0f, 1.0f, 1.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		if (tile.getShiftPosition() == EnumShiftPosition.HALF_SHIFT)
		{
			transform = transform.compose(new TRSRTransformation(new Vector3f(-0.178f, 0.0f, 0.0f), 
				     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
				     new Vector3f(1.0f, 1.0f, 1.0f), 
				     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		}
		else if (tile.getShiftPosition() == EnumShiftPosition.FULL_SHIFT)
		{
			transform = transform.compose(new TRSRTransformation(new Vector3f(-0.427f, 0.0f, 0.0f), 
				     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
				     new Vector3f(1.0f, 1.0f, 1.0f), 
				     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		}
		return transform;
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tile = blockAccess.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityFancySign)
    	{
    		TileEntityFancySign sign = (TileEntityFancySign)tile;
    		EnumShiftPosition shift = sign.getShiftPosition();
    		switch (sign.getAngle())
    		{
    			case SOUTH:
    			{
    				if (shift == EnumShiftPosition.NO_SHIFT)
    				{
    					output = this.getBlockBounds(0.94F, 0.2F, 0.0F, 1.0F, 0.8F, 1.0F); 
    				}
    				else if (shift == EnumShiftPosition.HALF_SHIFT)
    				{
    					output = this.getBlockBounds(0.51F, 0.2F, 0.0F, 0.57F, 0.8F, 1.0F); 
    				}
    				else if (shift == EnumShiftPosition.FULL_SHIFT)
    				{
    					output = this.getBlockBounds(0.07F, 0.2F, 0.0F, 0.01F, 0.8F, 1.0F); 
    				}
    				break;
				}
    			case WEST:
    			{
     				if (shift == EnumShiftPosition.NO_SHIFT)
    				{
     					output = this.getBlockBounds(0.0F, 0.2F, 0.94F, 1.0F, 0.8F, 1.0F); 
    				}
    				else if (shift == EnumShiftPosition.HALF_SHIFT)
    				{
    					output = this.getBlockBounds(0.0F, 0.2F, 0.51F, 1.0F, 0.8F, 0.57F); 
    				}
    				else if (shift == EnumShiftPosition.FULL_SHIFT)
    				{
    					output = this.getBlockBounds(0.0F, 0.2F, 0.07F, 1.0F, 0.8F, 0.01F); 
    				}
    				break;
				}
    			case NORTH:
    			{
     				if (shift == EnumShiftPosition.NO_SHIFT)
    				{
     					output = this.getBlockBounds(0.0F, 0.2F, 0.0F, 0.06F, 0.8F, 1.0F); 
    				}
    				else if (shift == EnumShiftPosition.HALF_SHIFT)
    				{
    					output = this.getBlockBounds(0.43F, 0.2F, 0.0F, 0.49F, 0.8F, 1.0F); 
    				}
    				else if (shift == EnumShiftPosition.FULL_SHIFT)
    				{
    					output = this.getBlockBounds(0.93F, 0.2F, 0.0F, 0.99F, 0.8F, 1.0F); 
    				}
    				break;
				}
    			case EAST:
    			{
     				if (shift == EnumShiftPosition.NO_SHIFT)
    				{
     					output = this.getBlockBounds(0.0F, 0.2F, 0.0F, 1.0F, 0.8F, 0.06F); 
    				}
    				else if (shift == EnumShiftPosition.HALF_SHIFT)
    				{
    					output = this.getBlockBounds(0.0F, 0.2F, 0.43F, 1.0F, 0.8F, 0.49F);
    				}
    				else if (shift == EnumShiftPosition.FULL_SHIFT)
    				{
    					output = this.getBlockBounds(0.0F, 0.2F, 0.93F, 1.0F, 0.8F, 0.99F);
    				}
    				break;
				}
    			default: break;
    		}
    	}
		return output;
	}
}
