package jds.bibliocraft.rendering;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.helpers.BiblioEnums.EnumBiblioPaintings;
import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.helpers.PaintingUtil;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPainting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class TileEntityPaintingRenderer extends TileEntityBiblioRenderer
{
	private ResourceLocation modelLocation = new ResourceLocation("bibliocraft:block/paintingframe.obj");
    private EnumArt[] vanillaArtList = EnumArt.values();
	private EnumBiblioPaintings[] biblioArtList = EnumBiblioPaintings.values();
	private int paintingType = 0;
	private String paintingTitle = "blank";
	private boolean connectedTop = false;
	private boolean connectedLeft = false;
	private boolean connectedBottom = false;
	private boolean connectedRight = false;
	private EnumPaintingFrame style = EnumPaintingFrame.BORDERLESS;
	private int paintingMasterCorner = 0;
	private int paintingScale = 1;
	private int paintingAspectRatio = 0;
	private int paintingPixelRes = 0;
	private int paintingRotation = 0;
	private int customPaintingAspectX = 1;
	private int customPaintingAspectY = 1;
	private boolean hideFrame = false;
	private String customTex = "none";
	private IBlockState state;
	
	@Override
	public void render(BiblioTileEntity te, double x, double y, double z, float tick)
	{
		TileEntityPainting tile = (TileEntityPainting)te;
		if (state == null)
		{
			state = tile.getWorld().getBlockState(tile.getPos());
		}
		this.style = tile.getFrameStyle();
		this.paintingType = tile.getPaintingType();
		this.paintingTitle = tile.getPaintingTitle();
		this.connectedTop = tile.getConnectTop();
		this.connectedLeft = tile.getConnectLeft();
		this.connectedBottom = tile.getConnectBottom();
		this.connectedRight = tile.getConnectRight();
		this.paintingRotation = tile.getPaintingRotation();
		this.paintingMasterCorner = tile.getPaintingCorner();
		this.paintingScale = tile.getPaintingScale();
		this.paintingPixelRes = tile.getPaintingRes();
		this.paintingAspectRatio = tile.getPaintingAspectRatio();
		this.customPaintingAspectX = tile.getCustomPaintingAspectX();
		this.customPaintingAspectY = tile.getCustomPaintingAspectY();
		this.hideFrame = tile.getHideFrame();
		this.customTex = tile.getCustomTextureString();
		
        if (tile.hasPainting())
        {
        	GlStateManager.pushMatrix();
        	GlStateManager.disableLighting();
        	GlStateManager.translate(x + xshift, y, z + zshift);
        	GlStateManager.rotate(degreeAngle - 90.0F, 0.0F, 1.0F, 0.0F);
        	GlStateManager.translate(0.01, 0.5, -0.5);
	        bindTexture(CommonProxy.PAINTINGCANVAS);
	        GlStateManager.rotate(-this.paintingRotation*90, 1.0f, 0.0f, 0.0f);
	        switch (this.paintingMasterCorner)
	        {
	        	case 0:
	        	{ 
	        		switch (this.paintingRotation)
	        		{
	        			case 1:{this.paintingMasterCorner = 3; break;}
	        			case 2:{this.paintingMasterCorner = 2; break;}
	        			case 3:{this.paintingMasterCorner = 1; break;}
	        		}
	        		break;
	        	}
	        	case 1:
	        	{ 
	        		switch (this.paintingRotation)
	        		{
	        			case 1:{this.paintingMasterCorner = 0; break;}
	        			case 2:{this.paintingMasterCorner = 3; break;}
	        			case 3:{this.paintingMasterCorner = 2; break;}
	        		}
	        		break;
	        	}
	        	case 2:
	        	{ 
	        		switch (this.paintingRotation)
	        		{
	        			case 1:{this.paintingMasterCorner = 1; break;}
	        			case 2:{this.paintingMasterCorner = 0; break;}
	        			case 3:{this.paintingMasterCorner = 3; break;}
	        		}
	        		break;
	        	}
	        	case 3:
	        	{ 
	        		switch (this.paintingRotation)
	        		{
	        			case 1:{this.paintingMasterCorner = 2; break;}
	        			case 2:{this.paintingMasterCorner = 1; break;}
	        			case 3:{this.paintingMasterCorner = 0; break;}
	        		}
	        		break;
	        	}
	        }
	        switch (paintingType)
	        {
	        	case 0:
	        	{
	        		for (int i = 0; i<biblioArtList.length; i++)
	        		{
	        			if (this.paintingTitle.contentEquals(this.biblioArtList[i].title))
	        			{
	        				bindTexture(this.biblioArtList[i].paintingTextures[this.paintingAspectRatio][this.paintingPixelRes]);
	        		        switch (this.paintingMasterCorner)
	        		        {
	        		        	case 0:{ GlStateManager.translate(0.0, -0.5+(0.5*this.biblioArtList[i].sizeY[this.paintingAspectRatio]*this.paintingScale),  0.5-(0.5*this.biblioArtList[i].sizeX[this.paintingAspectRatio]*this.paintingScale)); break; }
	        		        	case 1:{ GlStateManager.translate(0.0,  0.5-(0.5*this.biblioArtList[i].sizeY[this.paintingAspectRatio]*this.paintingScale),  0.5-(0.5*this.biblioArtList[i].sizeX[this.paintingAspectRatio]*this.paintingScale)); break; }
	        		        	case 2:{ GlStateManager.translate(0.0,  0.5-(0.5*this.biblioArtList[i].sizeY[this.paintingAspectRatio]*this.paintingScale), -0.5+(0.5*this.biblioArtList[i].sizeX[this.paintingAspectRatio]*this.paintingScale)); break; }
	        		        	case 3:{ GlStateManager.translate(0.0, -0.5+(0.5*this.biblioArtList[i].sizeY[this.paintingAspectRatio]*this.paintingScale), -0.5+(0.5*this.biblioArtList[i].sizeX[this.paintingAspectRatio]*this.paintingScale)); break; }
	        		        }
	        		       GlStateManager.scale(1.0, 1.0*this.biblioArtList[i].sizeY[this.paintingAspectRatio]*this.paintingScale, 1.0*this.biblioArtList[i].sizeX[this.paintingAspectRatio]*this.paintingScale);
	        		       
	        			}
	        		}
	        		break;
				}
	        	case 1:
	        	{
	        		for (int i = 0; i<vanillaArtList.length; i++)
	        		{
	        			if (this.paintingTitle.contentEquals(vanillaArtList[i].title))
	        			{
	        				double aspectDiff = (this.vanillaArtList[i].sizeX*1.0) / (this.vanillaArtList[i].sizeY*1.0);
	        				switch (this.paintingMasterCorner)
	        				{
	        					case 0:{GlStateManager.translate(0.0, -0.5+(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeY/16.0))),  0.5-(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeX/16.0))));break;}
	        					case 1:{GlStateManager.translate(0.0,  0.5-((this.vanillaArtList[i].sizeY/16.0)*this.paintingScale)+(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeY/16.0))),  0.5-(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeX/16.0))));break;}
	        					case 2:{GlStateManager.translate(0.0,  0.5-((this.vanillaArtList[i].sizeY/16.0)*this.paintingScale)+(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeY/16.0))),  -0.5+((this.vanillaArtList[i].sizeX/16.0)*this.paintingScale)-(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeX/16.0))));break;}
	        					case 3:{GlStateManager.translate(0.0, -0.5+(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeY/16.0))),  -0.5+((this.vanillaArtList[i].sizeX/16.0)*this.paintingScale)-(0.5*(this.paintingScale*(this.vanillaArtList[i].sizeX/16.0))));break;}
	        				}
	        				GlStateManager.scale(1.0, 1.0*(this.paintingScale*(this.vanillaArtList[i].sizeY/16.0)), 1.0*(this.paintingScale*(this.vanillaArtList[i].sizeX/16.0)));
	        				bindTexture(CommonProxy.PAINTINGSHEET);
	        				renderVanillaPainting(i);
	        			}
	        		}
	        		break;
				}
	        	case 2:
	        	{
	        		boolean foundMatch = false;
	        		if (PaintingUtil.customArtNames != null && PaintingUtil.customArtNames.length > 0 && PaintingUtil.customArtResources != null)
	    			{
	    				for (int i = 0; i < PaintingUtil.customArtNames.length; i++)
	    				{
	    					if (this.paintingTitle.contentEquals(PaintingUtil.customArtNames[i]))
	    					{
	    						bindTexture(PaintingUtil.customArtResources[i]);
	    						switch (this.paintingMasterCorner)
		        		        {
		        		        	case 0:{ GlStateManager.translate(0.0, -0.5+(0.5*this.paintingScale*this.customPaintingAspectY),  0.5-(0.5*this.paintingScale*this.customPaintingAspectX)); break; }
		        		        	case 1:{ GlStateManager.translate(0.0,  0.5-(0.5*this.paintingScale*this.customPaintingAspectY),  0.5-(0.5*this.paintingScale*this.customPaintingAspectX)); break; }
		        		        	case 2:{ GlStateManager.translate(0.0,  0.5-(0.5*this.paintingScale*this.customPaintingAspectY), -0.5+(0.5*this.paintingScale*this.customPaintingAspectX)); break; }
		        		        	case 3:{ GlStateManager.translate(0.0, -0.5+(0.5*this.paintingScale*this.customPaintingAspectY), -0.5+(0.5*this.paintingScale*this.customPaintingAspectX)); break; }
		        		        }
	    						GlStateManager.scale(1.0, 1.0*this.paintingScale*this.customPaintingAspectY, 1.0*this.paintingScale*this.customPaintingAspectX);
	    						foundMatch = true;
	    					}
	    				}
	    			}
	        		if (!foundMatch)
	        		{
	        			bindTexture(CommonProxy.PAINTINGNOTFOUND); 
	        		}
	        		break;
				}
	        }
	        
	        if (!connectedBottom && !connectedLeft && !connectedRight && !connectedTop && (this.style != EnumPaintingFrame.BORDERLESS) && this.paintingType != 1 && this.paintingScale == 1 && this.paintingAspectRatio == 0)
	        {
				renderPainting(true);
	        }
	        else
	        {
	        	if (this.paintingType != 1)
	        	{
	        		renderPainting(false);
	        	}
	        }
	        GlStateManager.enableLighting();
	        GlStateManager.popMatrix();
        }
	}
	
	private void renderPart(IBakedModel model)
	{
	    worldRenderer.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);
	    for (BakedQuad quad :  model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(worldRenderer, quad, 0xFFFFFFFF);
		}
		tessellator.draw();
	}
	
	private void renderVanillaPainting(int i)
	{
        float x1 = (float)(vanillaArtList[i].offsetX) / 256.0F;
        float x2 = (float)(vanillaArtList[i].offsetX + vanillaArtList[i].sizeX) / 256.0F;
        float y1 = (float)(vanillaArtList[i].offsetY) / 256.0F;
        float y2 = (float)(vanillaArtList[i].offsetY + vanillaArtList[i].sizeY) / 256.0F;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.024, -0.5, -0.5).tex(x2, y2).endVertex();
        worldRenderer.pos(0.024,  0.5, -0.5).tex(x2, y1).endVertex();
        worldRenderer.pos(0.024,  0.5,  0.5).tex(x1, y1).endVertex();
        worldRenderer.pos(0.024, -0.5,  0.5).tex(x1, y2).endVertex();
		tessellator.draw();
	}
	
	
	// TODO I think these are left to right reveresed. Check it out
	private void renderPainting(boolean smallCanvas)
	{
		double adjust = 0.0;
		if (smallCanvas)
		{
			adjust = 0.1;
		}
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.028, -0.5 + adjust, -0.5 + adjust).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(0.028,  0.5 - adjust, -0.5 + adjust).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(0.028,  0.5 - adjust,  0.5 - adjust).tex(0.0, 0.0).endVertex();
        worldRenderer.pos(0.028, -0.5 + adjust,  0.5 - adjust).tex(0.0, 1.0).endVertex();
		tessellator.draw();
	}
}
