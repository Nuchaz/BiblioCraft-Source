package jds.bibliocraft.rendering;

import com.mojang.blaze3d.platform.GlStateManager;

import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;

public class TileEntityCaseRenderer extends TileEntityBiblioRenderer 
{

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick) 
	{
		renderSlotItem(tile.getStackInSlot(0), 0.5, 0.45, 0.2, 0.8f); 
	}
	
	@Override
	public void additionalGLStuffForItemStack()
	{
		if (getVertPosition() == EnumVertPosition.FLOOR)
		{
			GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translated(0.0, -0.3, 0.3);
		}
	}

}
