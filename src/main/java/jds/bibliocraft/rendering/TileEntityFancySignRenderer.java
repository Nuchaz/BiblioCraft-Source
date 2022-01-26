package jds.bibliocraft.rendering;

import jds.bibliocraft.Config;
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class TileEntityFancySignRenderer extends TileEntityBiblioRenderer
{
	/*
	private int s1scale = 1;
	private int s1rot = 0;
	private int s1x = 0;
	private int s1y = 0;
	private int s2scale = 1;
	private int s2rot = 0;
	private int s2x = 0;
	private int s2y = 0;
	*/
	private int slotx = 0;
	private int sloty = 0;
	private int slotScale = 1;
	private int slotRot = 0;
	private ItemStack slot;
	
	private int[] textscale = new int[15];
	//private String[] text = new String[15];
	//private int numOfLines = 15;
	private int linespace = 0;
	private int scaledLinesNumber[] = {16, 11, 7, 5, 4, 3};
	private float[] scalesText = {1.0f, 1.5f, 2.0f, 3.0f, 4.0f, 5.0f};
	private float[] antiScalesText = {(1.0f/scalesText[0]),(1.0f/scalesText[1]),(1.0f/scalesText[2]),(1.0f/scalesText[3]),(1.0f/scalesText[4]),(1.0f/scalesText[5])};
	private double[] xScaleOffset = {0.0, 0.008, 0.016, 0.024, 0.032, 0.04, 0.048, 0.056, 0.064};
	private double[] yScaleOffset = {0.0, 0.02, 0.0415, 0.062, 0.083, 0.104, 0.124, 0.146, 0.167};
	private float[] itemScale = {0.16f, 0.285f, 0.41f, 0.535f, 0.66f, 0.785f, 0.91f, 1.035f, 1.0f};
	private int textLine = 0;
	
	@Override
	public void render(BiblioTileEntity tileEntity, double x, double y, double z, float tick)
	{
		if (tileEntity instanceof TileEntityFancySign)
		{
			float[] itemScaleTEST = 	{0.08f, 0.16f, 	0.24f, 	0.31f, 0.39f, 0.47f, 0.55f, 0.64f, 0.71f};
			double[] xScaleOffsetTEST = {0.02, 	0.015, 	0.023, 	0.029, 0.031, 0.018, 0.022, 0.03,  0.03};
			double[] yScaleOffsetTEST = {0.008, 	0.01, 	0.02, 	0.025, 0.03,  0.03,  0.04,  0.05,  0.05};
			TileEntityFancySign tile = (TileEntityFancySign)tileEntity;
			textscale = tile.textScale;
			slot = tile.getStackInSlot(0);
			slotx = tile.slot1X;
			sloty = tile.slot1Y;
			slotScale = tile.slot1Scale;
			slotRot = tile.slot1Rot;
			renderSlotItem(slot, 
					0.503f + slotx * 0.004f + xScaleOffsetTEST[slotScale], 
					0.445f - sloty * 0.004f - yScaleOffsetTEST[slotScale], 
					0.06, 
					itemScaleTEST[slotScale]);
			slot = tile.getStackInSlot(1);
			slotx = tile.slot2X;
			sloty = tile.slot2Y;
			slotScale = tile.slot2Scale;
			slotRot = tile.slot2Rot;
			renderSlotItem(slot, 
					0.503f + slotx * 0.004f + xScaleOffsetTEST[slotScale], 
					0.445f - sloty * 0.004f - yScaleOffsetTEST[slotScale],
					0.06,
					itemScaleTEST[slotScale]);
			linespace = 0;
			
			for (int n = 0; n < tile.numOfLines; n++)
			{
				textLine = n;
				renderText(tile.text[n], 0.047, 0.737 - linespace * 0.004, 0.4325);
				linespace += (int)8*(16.0f/scaledLinesNumber[tile.textScale[n]]);
			}
		}
	}
	
	@Override
	public void additionalGLStuffForItemStack()
	{
		if (getShiftPosition() == EnumShiftPosition.HALF_SHIFT)
		{
			GlStateManager.translate(0.0f, 0.0f, -0.18f);
		}
		else if (getShiftPosition() == EnumShiftPosition.FULL_SHIFT)
		{
			GlStateManager.translate(0.0f, 0.0f, -0.425f);
		}
		GlStateManager.scale(1.0f, 1.0f, 0.01f);
		if (Config.isBlock(slot))
		{
			if (slotRot == 0)
			{
				GlStateManager.rotate(45, -0.52f, 1.0f, -0.2f);
			}
			else if (slotRot == 2)
			{
				GlStateManager.rotate(-45, 0.52f, 1.0f, -0.2f);
			}
		}
		else
		{
			if (slotRot == 0)
			{
				GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
			}
		}
		
	}
	
	@Override
	public void additionalGLStuffForText()
	{
		GlStateManager.scale(scalesText[textscale[textLine]] * 0.89, scalesText[textscale[textLine]] * 0.89, 1.0);
	}
}
