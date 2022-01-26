package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPaintingFlat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPaintingFrameFlat extends BlockPainting
{
	public static final String name = "PaintingFrameFlat";
	public static final BlockPaintingFrameFlat instance = new BlockPaintingFrameFlat();
	
	public BlockPaintingFrameFlat()
	{
		super(name, EnumPaintingFrame.FLAT);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPaintingFlat();
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
					modelParts.add("flatTR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("flatTL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("flatT");
				}
				else
				{
					modelParts.add("flatT45");
				}
			}
			if (!painting.getConnectLeft())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("flatLB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("flatLT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("flatL");
				}
				else
				{
					modelParts.add("flatL45");
				}
			}
			if (!painting.getConnectRight())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("flatRB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("flatRT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("flatR");
				}
				else
				{
					modelParts.add("flatR45");
				}
			}
			if (!painting.getConnectBottom())
			{
				if (painting.getConnectLeft() && !painting.getConnectRight())
				{
					modelParts.add("flatBR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("flatBL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("flatB");
				}
				else
				{
					modelParts.add("flatB45");
				}
			}
			
			
			if (painting.getShowTLCorner())
			{
				if (painting.getConnectLeft() && painting.getConnectTop())
				{
					modelParts.add("flatTRcorner");
				}
			}
			if (painting.getShowTRCorner())
			{
				if (painting.getConnectTop() && painting.getConnectRight())
				{
					modelParts.add("flatBRcorner");
				}
			}
			if (painting.getShowBRCorner())
			{
				if (painting.getConnectRight() && painting.getConnectBottom())
				{
					modelParts.add("flatBLcorner");
				}
			}
			if (painting.getShowBLCorner())
			{
				if (painting.getConnectBottom() && painting.getConnectLeft())
				{
					modelParts.add("flatTLcorner");
				}
			}
		}
		//modelParts.add("flatT45");
		//modelParts.add("flatR45");
		//modelParts.add("flatB45");
		//modelParts.add("flatL45");
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		if (biblioTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)biblioTile;
			tile.setFrameStyle(EnumPaintingFrame.FLAT);
		}
	}
}
