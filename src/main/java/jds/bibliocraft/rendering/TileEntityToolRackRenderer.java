package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;

public class TileEntityToolRackRenderer extends TileEntityBiblioRenderer
{

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick) 
	{
		renderSlotItem(tile.getStackInSlot(0), 0.32, 0.62, 0.4, 0.6f); 
		renderSlotItem(tile.getStackInSlot(1), 0.68, 0.62, 0.4, 0.6f); 
		renderSlotItem(tile.getStackInSlot(2), 0.32, 0.26, 0.42, 0.6f); 
		renderSlotItem(tile.getStackInSlot(3), 0.68, 0.26, 0.42, 0.6f); 
	}

}
