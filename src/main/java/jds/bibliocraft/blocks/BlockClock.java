package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.gui.GuiClock;
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityClock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockClock extends BiblioWoodBlock
{
	public static final String name = "Clock";
	public static final BlockClock instance = new BlockClock();
	
	public BlockClock()
	{
		super(name, false);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (world.isRemote && player.isSneaking())
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityClock)
			{
				TileEntityClock ctile = (TileEntityClock)tile;
				//ctile.setVertPosition(EnumVertPosition.CEILING);
				if (ctile.getVertPosition() == EnumVertPosition.FLOOR)
				{
					TileEntity upperTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
					if (upperTile != null && upperTile instanceof TileEntityClock)
					{
						openGUI((TileEntityClock)upperTile, player);
					}
				}
				else
				{
					openGUI(ctile, player);
				}
			}
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void openGUI(TileEntityClock clock, EntityPlayer player)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiClock(clock, player));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityClock();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		
		if (tile.getVertPosition() == EnumVertPosition.WALL)
		{
			modelParts.add("face");
			modelParts.add("smallClock");
			modelParts.add("smallHardware");
		}
		else if (tile.getVertPosition() == EnumVertPosition.CEILING)
		{
			modelParts.add("face");
			modelParts.add("largeClock");
			modelParts.add("largeHardware");
		}
		//modelParts.add("itemhardware");
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		
		
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.17f, 0.0f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		
		switch (tile.getShiftPosition())
		{
    		case HALF_SHIFT: 
    		{
    			transform = transform.compose(new TRSRTransformation(new Vector3f(0.1f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
    			break; 
			}
    		case FULL_SHIFT:
    		{ 
    			transform = transform.compose(new TRSRTransformation(new Vector3f(0.168f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
    			break; 
    		}
    		default: break;
		}
		return transform;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    	TileEntity te = blockAccess.getTileEntity(pos);
    	if (te != null && te instanceof TileEntityClock)
    	{
    		TileEntityClock tile = (TileEntityClock)te;
    		EnumFacing angleGet = tile.getAngle();
    		float shiftAmount = 0.0f;
    		if (tile.getShiftPosition() == EnumShiftPosition.FULL_SHIFT)
    		{
    			shiftAmount = 0.675f;
    		}
    		else if (tile.getShiftPosition() == EnumShiftPosition.HALF_SHIFT)
    		{
    			shiftAmount = 0.3625f;
    		}
    		
    		switch (angleGet)
			{
				case SOUTH:{output = this.getBlockBounds(0.73F - shiftAmount, 0.0F, 0.3F, 1.0F - shiftAmount, 1.0F, 0.7F); break;}
				case WEST:{output = this.getBlockBounds(0.3F, 0.0F, 0.73F - shiftAmount, 0.7F, 1.0F, 1.0F - shiftAmount); break;}
				case NORTH:{output = this.getBlockBounds(0.0F + shiftAmount, 0.0F, 0.3F, 0.27F + shiftAmount, 1.0F, 0.7F); break;}
				case EAST:{output = this.getBlockBounds(0.3F, 0.0F, 0.0F + shiftAmount, 0.7F, 1.0F, 0.27F + shiftAmount); break;}
				default: {output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F); break;}
			}
		}
    	return output;
	}

	@Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityClock)
		{
			TileEntityClock clock = (TileEntityClock)tile;
			if (clock.redstoneActive)
			{
				return 15;
			}
		}
		return 0;
    }
	
	@Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return getWeakPower(state, world, pos, side);
    }
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		dropItems(world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityClock)
		{
			TileEntityClock clock = (TileEntityClock)tile;
			if (clock.getVertPosition() == EnumVertPosition.FLOOR)
			{
				TileEntityClock topClock = (TileEntityClock)world.getTileEntity(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
				topClock.setVertPosition(EnumVertPosition.WALL);
			}
			else if (clock.getVertPosition() == EnumVertPosition.CEILING)
			{
				TileEntityClock bottomClock = (TileEntityClock)world.getTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
				bottomClock.setVertPosition(EnumVertPosition.WALL);
			}
		}
		super.breakBlock(world, pos, state);
	}
}
