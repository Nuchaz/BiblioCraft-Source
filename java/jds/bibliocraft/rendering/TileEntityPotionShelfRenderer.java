package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;

public class TileEntityPotionShelfRenderer extends TileEntityBiblioRenderer
{

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick) 
	{
		renderSlotItem(tile.getStackInSlot(0), 0.15, 0.81, 0.25, 0.42f); 
		renderSlotItem(tile.getStackInSlot(1), 0.38, 0.81, 0.26, 0.42f); 
		renderSlotItem(tile.getStackInSlot(2), 0.61, 0.81, 0.25, 0.42f); 
		renderSlotItem(tile.getStackInSlot(3), 0.84, 0.81, 0.26, 0.42f); 
		
		renderSlotItem(tile.getStackInSlot(4), 0.15, 0.48, 0.26, 0.42f); 
		renderSlotItem(tile.getStackInSlot(5), 0.38, 0.48, 0.25, 0.42f); 
		renderSlotItem(tile.getStackInSlot(6), 0.61, 0.48, 0.26, 0.42f); 
		renderSlotItem(tile.getStackInSlot(7), 0.84, 0.48, 0.25, 0.42f); 
		
		renderSlotItem(tile.getStackInSlot(8),  0.15, 0.13, 0.25, 0.42f); 
		renderSlotItem(tile.getStackInSlot(9),  0.38, 0.13, 0.26, 0.42f); 
		renderSlotItem(tile.getStackInSlot(10), 0.61, 0.13, 0.25, 0.42f); 
		renderSlotItem(tile.getStackInSlot(11), 0.84, 0.13, 0.26, 0.42f); 
	}

}
