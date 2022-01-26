package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.helpers.EnumMetalType;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioLightTileEntity;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLampGold extends BiblioLightBlock
{
	public static String name = "LampGold";
	public static BlockLampGold instance = new BlockLampGold();
	
	public BlockLampGold()
	{
		super(name);
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		switch (tile.getVertPosition())
		{
			case CEILING: 
			{
				modelParts.add("ceilingPlate");
				modelParts.add("lampTopCeiling");
				break;
			}
			case WALL:
			{
				modelParts.add("wallPlate");
				modelParts.add("lampTopWall");
				break;
			}
			case FLOOR:
			{
				modelParts.add("baseFloor");
				modelParts.add("lampTopFloor");
				break;
			}
		}
		return modelParts;
	}

	@Override
	public void additionalLightPlacmentCommands(BiblioTileEntity biblioTile) 
	{
		if (biblioTile instanceof BiblioLightTileEntity)
		{
			BiblioLightTileEntity light = (BiblioLightTileEntity)biblioTile;
			light.setLightType(EnumMetalType.GOLD);
		}
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity pretile = blockAccess.getTileEntity(pos);
    	if (pretile != null && pretile instanceof BiblioLightTileEntity)
    	{
    		BiblioLightTileEntity tile = (BiblioLightTileEntity)pretile;
    		EnumVertPosition style = tile.getVertPosition();
    		switch (style)
    		{
    			case FLOOR:{output = this.getBlockBounds(0.18F, 0.0F, 0.18F, 0.82F, 1.0F, 0.82F); break;}
    			case WALL:{output = this.getBlockBounds(0.1F, 0.05F, 0.1F, 0.9F, 0.55F, 0.9F); break;}
    			case CEILING:{output = this.getBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F); break;}
    			default:{output = this.getBlockBounds(0.18F, 0.0F, 0.18F, 0.82F, 1.0F, 0.82F); break;}
    		}
    	}
    	return output;
	}
}
