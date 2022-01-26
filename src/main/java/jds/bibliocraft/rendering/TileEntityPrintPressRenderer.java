package jds.bibliocraft.rendering;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPrintPress;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class TileEntityPrintPressRenderer extends TileEntityBiblioRenderer
{
	private ResourceLocation modelLocation = new ResourceLocation("bibliocraft:block/printpress.obj");
	private IBakedModel bed;
	private IBakedModel arm;
	private IBlockState state;
	
	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick)
	{
		TileEntityPrintPress press = (TileEntityPrintPress)tile;
		if (state == null)
		{
			state = tile.getWorld().getBlockState(tile.getPos());
		}
		if (bed == null || arm == null)
		{
			List<String> bedPart = new ArrayList<String>();
			bedPart.add("bed");
			List<String> armpart = new ArrayList<String>();
			armpart.add("arm");
			bed = initModel(bedPart, modelLocation);
			arm = initModel(armpart, modelLocation);
		}
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderPart(arm, 1.0, 0.91, 0.12, press.armAngle); // range from -5.0f to -115.0f
		renderPart(bed, 1.0, 0.55, 0.3, press.bedAngle); // range is from 0.0f to 25.0f
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
