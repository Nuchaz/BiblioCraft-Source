package jds.bibliocraft.rendering;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityClock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class TileEntityClockRenderer extends TileEntityBiblioRenderer
{
	private ResourceLocation modelLocation = new ResourceLocation("bibliocraft:block/clock.obj");
	private IBakedModel minHand;
	private IBakedModel hourHand;
	private IBakedModel smallPendulum;
	private IBakedModel largePendulum;
	private IBlockState state;
	//private Tessellator tessellator;
	//private WorldRenderer worldRenderer;
	

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick) 
	{
		TileEntityClock clock = (TileEntityClock)tile;
		if (state == null)
		{
			state = clock.getWorld().getBlockState(clock.getPos());
		}
		if (minHand == null || hourHand == null || smallPendulum == null || largePendulum == null || tessellator == null || worldRenderer == null)
		{
			List<String> hourPart = new ArrayList<String>();
			hourPart.add("hour");
			List<String> minPart = new ArrayList<String>();
			minPart.add("second");
			List<String> smallPendulumPart = new ArrayList<String>();
			smallPendulumPart.add("smallPendulum");
			List<String> largePendulumPart = new ArrayList<String>();
			largePendulumPart.add("largePendulum");
			minHand = initModel(minPart, modelLocation);
			hourHand = initModel(hourPart, modelLocation);
			smallPendulum = initModel(smallPendulumPart, modelLocation);
			largePendulum = initModel(largePendulumPart, modelLocation);
			
		}
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		if (getVertPosition() == EnumVertPosition.WALL || getVertPosition() == EnumVertPosition.CEILING)
		{
			renderPart(minHand, 0.5, 0.715, 0.25, clock.secondCount);
			renderPart(hourHand, 0.5, 0.715, 0.25, clock.hourCount);
		}
		if (getVertPosition() == EnumVertPosition.WALL)
		{
			renderPart(smallPendulum, 0.5, 0.5, 0.21, clock.pendulumCount);
		}
		else if (getVertPosition() == EnumVertPosition.CEILING)
		{
			renderPart(largePendulum, 0.5, 0.5, 0.21, clock.pendulumCount);
		}
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

		float halfShift = 0.35f;
	    float fullShift = 0.675f;
		switch (this.getAngle())
		{
			case SOUTH:
			{
				degreeAngle = 270; 
				xshift = 1.0f; 
				zshift = 0.0f; 
				if (this.getShiftPosition() == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += -fullShift; 
					zshift += 0;
				} 
				if (this.getShiftPosition() == EnumShiftPosition.HALF_SHIFT)
				{
					xshift += -halfShift; 
					zshift += 0;
				} 
				break;
			}
			case WEST:
			{
				degreeAngle = 180;  
				xshift = 1.0f; 
				zshift = 1.0f; 
				if (this.getShiftPosition() == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += 0.0f; 
					zshift += -fullShift;
				} 
				if (this.getShiftPosition() == EnumShiftPosition.HALF_SHIFT)
				{
					xshift += 0.0f; 
					zshift += -halfShift;
				} 
				break;
			}
			case NORTH:
			{
				degreeAngle = 90;  
				xshift = 0.0f; 
				zshift = 1.0f; 
				if (this.getShiftPosition() == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += fullShift; 
					zshift += 0;
				} 
				if (this.getShiftPosition() == EnumShiftPosition.HALF_SHIFT)
				{
					xshift += halfShift; 
					zshift += 0;
				} 
				break;
			}
			case EAST:
			{
				degreeAngle = 0;    
				xshift = 0.0f; 
				zshift = 0.0f; 
				if (this.getShiftPosition() == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += 0.0f; 
					zshift += fullShift;
				} 
				if (this.getShiftPosition() == EnumShiftPosition.HALF_SHIFT)
				{
					xshift += 0.0f; 
					zshift += halfShift;
				}
				break;
			}
			default:break;
		}
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translate(this.globalX + this.xshift + x, this.globalY + y, this.globalZ + this.zshift + z);
		GlStateManager.rotate(degreeAngle + 90.0f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(rotation, 1.0f, 0.0f, 0.0f);
	    worldRenderer.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);
	    //model.getQuads(state, side, rand)
	   
		for (BakedQuad quad :  model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(worldRenderer, quad, 0xFFFFFFFF);
		}
		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
