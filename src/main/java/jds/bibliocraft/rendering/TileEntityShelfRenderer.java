package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;

public class TileEntityShelfRenderer extends TileEntityBiblioRenderer
{
	private float scale = 0.6f;
	
	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick)
	{
		this.renderSlotItem(tile.getStackInSlot(0), 0.25, 0.66, 0.25, 0.6f);
		this.renderSlotItem(tile.getStackInSlot(1), 0.75, 0.66, 0.25, 0.6f);
		this.renderSlotItem(tile.getStackInSlot(2), 0.25, 0.17, 0.25, 0.6f);
		this.renderSlotItem(tile.getStackInSlot(3), 0.75, 0.17, 0.25, 0.6f);
	}
}
