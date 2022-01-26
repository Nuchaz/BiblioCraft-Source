package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityClipboard;

public class TileEntityClipboardRenderer extends TileEntityBiblioRenderer
{
    private double textSpacing = -0.0658;

	@Override
	public void render(BiblioTileEntity tileEntity, double x, double y, double z, float tick)
	{
		TileEntityClipboard tile = (TileEntityClipboard)tileEntity;
		if (tile != null)
		{
			renderText(tile.titletext, 0.037, 0.825, 0.27); 
			renderText(tile.button0text, 0.037, 0.76, 0.222);
			renderText(tile.button1text, 0.037, 0.76+(1*textSpacing), 0.222);
			renderText(tile.button2text, 0.037, 0.76+(2*textSpacing), 0.222);
			renderText(tile.button3text, 0.037, 0.76+(3*textSpacing), 0.222);
			renderText(tile.button4text, 0.037, 0.76+(4*textSpacing), 0.222);
			renderText(tile.button5text, 0.037, 0.76+(5*textSpacing), 0.222);
			renderText(tile.button6text, 0.037, 0.76+(6*textSpacing), 0.222);
			renderText(tile.button7text, 0.037, 0.76+(7*textSpacing), 0.222);
			renderText(tile.button8text, 0.037, 0.76+(8*textSpacing), 0.222);
			String pageNum = ""+tile.currentPage;
			if (tile.currentPage > 9)
			{
				renderText(pageNum, 0.037, 0.17, 0.03);
			}
			else
			{
				renderText(pageNum, 0.037, 0.17, 0.02);
			}
		}
	}
}
