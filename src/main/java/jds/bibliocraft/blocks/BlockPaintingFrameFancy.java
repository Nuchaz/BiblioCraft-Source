package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPaintingFancy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPaintingFrameFancy extends BlockPainting
{
	public static final String name = "PaintingFrameFancy";
	public static final BlockPaintingFrameFancy instance = new BlockPaintingFrameFancy();
	
	public BlockPaintingFrameFancy()
	{
		super(name, EnumPaintingFrame.FANCY);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPaintingFancy();
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
					modelParts.add("fancyTR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("fancyTL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("fancyT");
				}
				else
				{
					modelParts.add("fancyT45");
				}
			}
			if (!painting.getConnectLeft())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("fancyLB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("fancyLT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("fancyL");
				}
				else
				{
					modelParts.add("fancyL45");
				}
			}
			if (!painting.getConnectRight())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("fancyRB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("fancyRT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("fancyR");
				}
				else
				{
					modelParts.add("fancyR45");
				}
			}
			if (!painting.getConnectBottom())
			{
				if (painting.getConnectLeft() && !painting.getConnectRight())
				{
					modelParts.add("fancyBR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("fancyBL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("fancyB");
				}
				else
				{
					modelParts.add("fancyB45");
				}
			}
			
			
			if (painting.getShowTLCorner())
			{
				if (painting.getConnectLeft() && painting.getConnectTop())
				{
					modelParts.add("fancyTRcorner");
				}
			}
			if (painting.getShowTRCorner())
			{
				if (painting.getConnectTop() && painting.getConnectRight())
				{
					modelParts.add("fancyBRcorner");
				}
			}
			if (painting.getShowBRCorner())
			{
				if (painting.getConnectRight() && painting.getConnectBottom())
				{
					modelParts.add("fancyBLcorner");
				}
			}
			if (painting.getShowBLCorner())
			{
				if (painting.getConnectBottom() && painting.getConnectLeft())
				{
					modelParts.add("fancyTLcorner");
				}
			}
		}
		//modelParts.add("fancyT45");
		//modelParts.add("fancyR45");
		//modelParts.add("fancyB45");
		//modelParts.add("fancyL45");
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		if (biblioTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)biblioTile;
			tile.setFrameStyle(EnumPaintingFrame.FANCY);
		}
	}
}
