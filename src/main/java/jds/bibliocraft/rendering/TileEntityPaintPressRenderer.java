package jds.bibliocraft.rendering;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.helpers.BiblioEnums.EnumBiblioPaintings;
import jds.bibliocraft.helpers.PaintingUtil;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class TileEntityPaintPressRenderer extends TileEntityBiblioRenderer
{
	private ResourceLocation modelLocation = new ResourceLocation("bibliocraft:block/paintpress.obj");
	private EnumArt[] vanillaArtList = EnumArt.values();
	private EnumBiblioPaintings[] biblioArtList = EnumBiblioPaintings.values();
	private IBakedModel lid;
	private IBakedModel canvas;
	private ItemStack painting = ItemStack.EMPTY;
	private ItemStack prevPainting = ItemStack.EMPTY;
	public static final ResourceLocation PAINTINGCANVAS = new ResourceLocation("bibliocraft", "textures/paintings/canvas.png");
	public static final ResourceLocation PAINTINGSHEET = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
	private String paintingTitle = "blank";
	private String paintingString = "bibliocraft:paintings/canvas";
	private boolean updatePainting = true;
	private IBlockState state;

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick)
	{
		TileEntityPaintPress press = (TileEntityPaintPress)tile;
		if (state == null)
		{
			state = tile.getWorld().getBlockState(tile.getPos());
		}
		paintingTitle = press.getPaintingTitle();
		painting = tile.getStackInSlot(0);
		if (lid == null)
		{
			List<String> lidPart = new ArrayList<String>();
			lidPart.add("lid");
			lid = initModel(lidPart, modelLocation);
		}
		
		paintingString = "bibliocraft:paintings/canvas";
		if (painting != ItemStack.EMPTY)
		{
			if (press.getPaintingType() == 0)
			{
				for (int i = 0; i < this.biblioArtList.length; i++)
				{
					if (this.paintingTitle.contentEquals(this.biblioArtList[i].title))
					{
						paintingString = this.biblioArtList[i].paintingTexturesStrings[0][0];
					}
				}
			}
			else if (press.getPaintingType() == 2)
			{
				if (PaintingUtil.customArtNames != null && PaintingUtil.customArtNames.length > 0 && PaintingUtil.customArtResources != null)
				{
					for (int i = 0; i < PaintingUtil.customArtNames.length; i++)
					{
						if (this.paintingTitle.contentEquals(PaintingUtil.customArtNames[i]))
						{
							paintingString = PaintingUtil.customArtResourceStrings[i];
						}
					}
				}	
			}
		}
		List<String> canvasPart  = new ArrayList<String>();
		canvasPart.add("painting");
		canvas = initModel(canvasPart, modelLocation);
		prevPainting = painting;
		
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderPart(lid, 1.0, 0.42, 0.0, -press.lidAngle);
		
		if (painting != ItemStack.EMPTY)
		{
			if (press.getPaintingType() == 1)
			{
				bindTexture(PAINTINGSHEET);
				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				for (int i = 0; i<vanillaArtList.length; i++)
				{
					if (this.paintingTitle.contentEquals(vanillaArtList[i].title))
					{
						drawVanillaPainting(x, y, z, i);
					}
				}
				GlStateManager.enableLighting();
				GlStateManager.popMatrix(); 
			}
			else if (press.getPaintingType() == 2)
			{
				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				for (int i = 0; i < PaintingUtil.customArtNames.length; i++)
				{
					if (this.paintingTitle.contentEquals(PaintingUtil.customArtNames[i]))
					{
						bindTexture(PaintingUtil.customArtResources[i]);
						drawCustomPainting(x, y, z, i);
					}
				}
				GlStateManager.enableLighting();
				GlStateManager.popMatrix(); 
			}
			else
			{
				renderPart(canvas, 1.0, 0.0, 0.0, 0.0f);
			}
		}
	}
	
	@Override
	public String getTextureString(ResourceLocation location)
	{
		String output = location.toString();
		if (output.contains("canvas"))
		{
			output = paintingString;
		}
		return output;
	}
	
	public void drawCustomPainting(double x, double y, double z, int e) /// hmmm
	{
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        float x1 = 0.0f;
        float x2 = 1.0f;
        float y1 = 0.0f;
        float y2 = 1.0f;
		switch (this.getAngle())
		{
			case SOUTH:
			{
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5-0.4).tex(x1, y1).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5-0.4).tex(x1, y2).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5+0.4).tex(x2, y2).endVertex();
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5+0.4).tex(x2, y1).endVertex();
				break;
			}
			case WEST:
			{
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5-0.4).tex(x1, y2).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5-0.4).tex(x2, y2).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5+0.4).tex(x2, y1).endVertex();
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5+0.4).tex(x1, y1).endVertex();
				break;
			}
			case NORTH:
			{
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5-0.4).tex(x2, y2).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5-0.4).tex(x2, y1).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5+0.4).tex(x1, y1).endVertex();
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5+0.4).tex(x1, y2).endVertex();
				break;
			}
			case EAST:
			{
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5-0.4).tex(x2, y1).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5-0.4).tex(x1, y1).endVertex();
				worldRenderer.pos(x+0.5-0.4, y+0.5-0.126, z+0.5+0.4).tex(x1, y2).endVertex();
				worldRenderer.pos(x+0.5+0.4, y+0.5-0.126, z+0.5+0.4).tex(x2, y2).endVertex();
				break;
			}
			default: break;
		}
		tessellator.draw();
	}
	// */
	public void drawVanillaPainting(double i, double j, double k, int x)
	{
        float x1 = (float)(vanillaArtList[x].offsetX) / 256.0F;
        float x2 = (float)(vanillaArtList[x].offsetX + vanillaArtList[x].sizeX) / 256.0F;
        float y1 = (float)(vanillaArtList[x].offsetY) / 256.0F;
        float y2 = (float)(vanillaArtList[x].offsetY + vanillaArtList[x].sizeY) / 256.0F;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		switch (this.getAngle())
		{
			case SOUTH:
			{
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5-0.4).tex(x1, y1).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5-0.4).tex(x1, y2).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5+0.4).tex(x2, y2).endVertex();
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5+0.4).tex(x2, y1).endVertex();
				break;
			}
			case WEST:
			{
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5-0.4).tex(x1, y2).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5-0.4).tex(x2, y2).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5+0.4).tex(x2, y1).endVertex();
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5+0.4).tex(x1, y1).endVertex();
				break;
			}
			case NORTH:
			{
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5-0.4).tex(x2, y2).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5-0.4).tex(x2, y1).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5+0.4).tex(x1, y1).endVertex();
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5+0.4).tex(x1, y2).endVertex();
				break;
			}
			case EAST:
			{
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5-0.4).tex(x2, y1).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5-0.4).tex(x1, y1).endVertex();
				worldRenderer.pos(i+0.5-0.4, j+0.5-0.126, k+0.5+0.4).tex(x1, y2).endVertex();
				worldRenderer.pos(i+0.5+0.4, j+0.5-0.126, k+0.5+0.4).tex(x2, y2).endVertex();
				break;
			}
			default: break;
		}
		tessellator.draw();
	}

	private void renderPart(IBakedModel model, double x, double y, double z, float rotation)
	{
		switch (this.getAngle())
		{
			case SOUTH: 
			{ 
				double tx = x;
				x = -z;
				z = tx;
				break; 
			}
			case WEST: 
			{ 
				x *= -1;
				z *= -1;
				break; 
			}
			case NORTH: 
			{ 
				double tx = x;
				x = z;
				z = -tx;
				break; 
			}
			case EAST: 
			{ 
				break; 
			}
			default: break;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translate(this.globalX + this.xshift + x, this.globalY + y, this.globalZ + this.zshift + z);
		GlStateManager.rotate(degreeAngle - 90.0f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(rotation, 0.0f, 0.0f, 1.0f);
	    worldRenderer.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);
	    for (BakedQuad quad :  model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(worldRenderer, quad, 0xFFFFFFFF);
		}
		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
