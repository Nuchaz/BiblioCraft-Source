package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.client.renderer.GlStateManager;

public class TileEntitySwordPedestalRenderer extends TileEntityBiblioRenderer
{

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick)
	{
		renderSlotItem(tile.getStackInSlot(0), 0.5, 0.6, 0.5, 0.9f);
		
	}

	@Override
	public void additionalGLStuffForItemStack()
	{
		GlStateManager.rotate(135, 0.0f, 0.0f, 1.0f);
	}
}
