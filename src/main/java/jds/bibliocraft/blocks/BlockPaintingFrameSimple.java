package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPaintingSimple;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPaintingFrameSimple extends BlockPainting
{
	public static final String name = "PaintingFrameSimple";
	public static final BlockPaintingFrameSimple instance = new BlockPaintingFrameSimple();
	
	public BlockPaintingFrameSimple()
	{
		super(name, EnumPaintingFrame.SIMPLE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPaintingSimple();
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
					modelParts.add("simpleTR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("simpleTL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("simpleT");
				}
				else
				{
					modelParts.add("simpleT45");
				}
			}
			if (!painting.getConnectLeft())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("simpleLB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("simpleLT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("simpleL");
				}
				else
				{
					modelParts.add("simpleL45");
				}
			}
			if (!painting.getConnectRight())
			{
				if (painting.getConnectTop() && !painting.getConnectBottom())
				{
					modelParts.add("simpleRB45");
				}
				else if (!painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("simpleRT45");
				}
				else if (painting.getConnectTop() && painting.getConnectBottom())
				{
					modelParts.add("simpleR");
				}
				else
				{
					modelParts.add("simpleR45");
				}
			}
			if (!painting.getConnectBottom())
			{
				if (painting.getConnectLeft() && !painting.getConnectRight())
				{
					modelParts.add("simpleBR45");
				}
				else if (!painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("simpleBL45");
				}
				else if (painting.getConnectLeft() && painting.getConnectRight())
				{
					modelParts.add("simpleB");
				}
				else
				{
					modelParts.add("simpleB45");
				}
			}
			
			
			if (painting.getShowTLCorner())
			{
				if (painting.getConnectLeft() && painting.getConnectTop())
				{
					modelParts.add("simpleTRcorner");
				}
			}
			if (painting.getShowTRCorner())
			{
				if (painting.getConnectTop() && painting.getConnectRight())
				{
					modelParts.add("simpleBRcorner");
				}
			}
			if (painting.getShowBRCorner())
			{
				if (painting.getConnectRight() && painting.getConnectBottom())
				{
					modelParts.add("simpleBLcorner");
				}
			}
			if (painting.getShowBLCorner())
			{
				if (painting.getConnectBottom() && painting.getConnectLeft())
				{
					modelParts.add("simpleTLcorner");
				}
			}
		}
		//modelParts.add("simpleT45");
		//modelParts.add("simpleR45");
		//modelParts.add("simpleB45");
		//modelParts.add("simpleL45");
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		if (biblioTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)biblioTile;
			tile.setFrameStyle(EnumPaintingFrame.SIMPLE);
		}
	}
}
