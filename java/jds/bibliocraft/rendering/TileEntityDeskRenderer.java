package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityDesk;

public class TileEntityDeskRenderer extends TileEntityBiblioRenderer
{

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick) 
	{
		if (tile instanceof TileEntityDesk)
		{
			TileEntityDesk desk = (TileEntityDesk)tile;
			if (desk.getHasMap())
			{
				switch (desk.getAngle())
				{
					case SOUTH: { renderItemMap(desk.getStackInSlot(0), 0.03f, 0.0f, 0.222f, 0.65f); break; }
					case WEST: { renderItemMap(desk.getStackInSlot(0), 0.222f, 0.0f, 0.03f, 0.65f); break; }
					case NORTH: { renderItemMap(desk.getStackInSlot(0), 0.444f, 0.0f, 0.222f, 0.65f); break; }
					case EAST: { renderItemMap(desk.getStackInSlot(0), 0.26f, 0.0f, 0.444f, 0.65f); break; }
					default: break;
				}
				
			}
		}
	}

}
