package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPaintingBorderless;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPaintingFrameBorderless extends BlockPainting
{
	public static final String name = "PaintingFrameBorderless";
	public static final BlockPaintingFrameBorderless instance = new BlockPaintingFrameBorderless();
	
	public BlockPaintingFrameBorderless()
	{
		super(name, EnumPaintingFrame.BORDERLESS);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPaintingBorderless();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		if (tile != null && tile instanceof TileEntityPainting)
		{
			TileEntityPainting painting = (TileEntityPainting)tile;
			if (!painting.hideFrame)
			{
				modelParts.add("back");
				//if (!painting.hasPainting())
				//{
					modelParts.add("largeCanvas");
				//}
			}
		}
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		if (biblioTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)biblioTile;
			tile.setFrameStyle(EnumPaintingFrame.BORDERLESS);
			onBlockPlacedConnect(biblioTile.getWorld(), biblioTile.getPos().getX(), biblioTile.getPos().getY(), biblioTile.getPos().getZ(), tile, true); // is this a true?
		}
	}
}
