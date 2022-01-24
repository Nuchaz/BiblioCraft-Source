package jds.bibliocraft.rendering;

import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.client.renderer.GlStateManager;

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
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.0, -0.3, 0.3);
		}
	}

}
