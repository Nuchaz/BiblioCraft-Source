package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPaintingMiddle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPaintingFrameMiddle extends BlockPainting
{
	public static final String name = "PaintingFrameMiddle";
	public static final BlockPaintingFrameMiddle instance = new BlockPaintingFrameMiddle();
	
	public BlockPaintingFrameMiddle()
	{
		super(name, EnumPaintingFrame.MIDDLE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPaintingMiddle();
	}
	
	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		//List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("back");
		if (tile != null && tile instanceof TileEntityPainting)
		{
			TileEntityPainting painting = (TileEntityPainting)tile;
			//if (!painting.hasPainting())
			//{
		        if (!painting.getConnectBottom() && !painting.getConnectLeft() && !painting.getConnectRight() && !painting.getConnectTop())
		        {
		        	modelParts.add("canvas");
		        }
		        else
		        {
		        	modelParts.add("largeCanvas");
		        }
			//}
			if (!painting.getConnectTop())
			{
				if (painting.getConnectLeft() && !painting.getConnectRight())
				{
					modelParts.add("middleTR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("middleTL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("middleT");
				}
				else
				{
					modelParts.add("middleT45");
				}
			}
			if (!painting.getConnectLeft())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("middleLB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("middleLT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("middleL");
				}
				else
				{
					modelParts.add("middleL45");
				}
			}
			if (!painting.getConnectRight())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("middleRB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("middleRT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("middleR");
				}
				else
				{
					modelParts.add("middleR45");
				}
			}
			if (!painting.getConnectBottom())
			{
				if (painting.getConnectLeft() && !painting.getConnectRight())
				{
					modelParts.add("middleBR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("middleBL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("middleB");
				}
				else
				{
					modelParts.add("middleB45");
				}
			}
			
			
			if (painting.getShowTLCorner())
			{
				if (painting.getConnectLeft() && painting.getConnectTop())
				{
					modelParts.add("middleTRcorner");
				}
			}
			if (painting.getShowTRCorner())
			{
				if (painting.getConnectTop() && painting.getConnectRight())
				{
					modelParts.add("middleBRcorner");
				}
			}
			if (painting.getShowBRCorner())
			{
				if (painting.getConnectRight() && painting.getConnectBottom())
				{
					modelParts.add("middleBLcorner");
				}
			}
			if (painting.getShowBLCorner())
			{
				if (painting.getConnectBottom() && painting.getConnectLeft())
				{
					modelParts.add("middleTLcorner");
				}
			}
		}
		//modelParts.add("middleT45");
		//modelParts.add("middleR45");
		//modelParts.add("middleB45");
		//modelParts.add("middleL45");
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		if (biblioTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)biblioTile;
			tile.setFrameStyle(EnumPaintingFrame.MIDDLE);
		}
	}
}
