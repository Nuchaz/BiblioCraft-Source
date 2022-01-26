package jds.bibliocraft.rendering;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityTypewriter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class TileEntityTypeWriterRenderer extends TileEntityBiblioRenderer
{
	private ResourceLocation modelLocation = new ResourceLocation("bibliocraft:block/typewriter.obj");
	private IBakedModel[] slideModels = new IBakedModel[16];
	private IBakedModel[] paperModels = new IBakedModel[16];
	private EnumColor color = EnumColor.WHITE;
	private String paperTexture = "bibliocraft:models/typewriter_paper_blank";
	private boolean init = false;
	private IBlockState state;
	
	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick)
	{
		if (!init)
		{
			initModels();
			init = true;
		}
		
		if (tile instanceof TileEntityTypewriter)
		{
			TileEntityTypewriter typewriter = (TileEntityTypewriter)tile;
			if (state == null)
			{
				state = tile.getWorld().getBlockState(tile.getPos());
			}
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			double adjust = 0.14; 
			int writeCount = typewriter.getBookWriteCount();
			if (writeCount == 0)
			{
				adjust = 0.0;
			}
			else
			{
				adjust = 0.14 - (0.02 * writeCount);
			}

			if (typewriter.getHasPaper())
			{
				renderPart(paperModels[typewriter.getBookWriteCount()], adjust);
			}
			renderPart(slideModels[tile.getBlockMetadata()], adjust);
		}
	}
	
	private void renderPart(IBakedModel model, double x)
	{
		double y = 0.0;
		double z = 0.09;
		float rotation = 0.0f;
		x += 1.0;
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
		GlStateManager.rotate(degreeAngle + 90.0f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(rotation, 1.0f, 0.0f, 0.0f);
	    worldRenderer.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);
	    for (BakedQuad quad :  model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(worldRenderer, quad, 0xFFFFFFFF);
		}
		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
	
	private void initModels()
	{
		for (int i = 0; i < EnumColor.values().length; i++)
		{
			
			IModel model = null;
			try
			{
				model = ModelLoaderRegistry.getModel(modelLocation); 
			}
			catch (Exception e)
			{
				
				model = ModelLoaderRegistry.getMissingModel();
			}
			model = model.process(ImmutableMap.of("flip-v", "true"));
			List<String> slide = new ArrayList<String>();
			slide.add("slide");
			OBJModel.OBJState slideState = new OBJModel.OBJState(slide, true);
			color = EnumColor.getColorEnumFromID(i);
			slideModels[i] = model.bake(slideState,  Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
		}
		
		for (int i = 0; i < paperModels.length; i++)
		{
			
			IModel model = null;
			try
			{
				model = ModelLoaderRegistry.getModel(modelLocation); 
			}
			catch (Exception e)
			{
				
				model = ModelLoaderRegistry.getMissingModel();
			}
			model = model.process(ImmutableMap.of("flip-v", "true"));
			List<String> paper = new ArrayList<String>();
			String paperModel = "paperLine1";
			if (i >= 0)
			{
				paperModel = "paperLine1";
			}
			if (i >= 3)
			{
				paperModel = "paperLine2";
			}
			if (i >= 5)
			{
				paperModel = "paperLine3";
			}
			if (i >= 8)
			{
				paperModel = "paperLine4";
			}
			if (i >= 10)
			{
				paperModel = "paperLine5";
			}
			if (i >= 12)
			{
				paperModel = "paperLine6";
			}
			if (i >= 14)
			{
				paperModel = "paperLine7";
			}
			paper.add(paperModel);
			OBJModel.OBJState paperState = new OBJModel.OBJState(paper, true);
			if (i == 0)
			{
				paperTexture = "bibliocraft:models/typewriter_paper_blank";
			}
			else
			{
				paperTexture = "bibliocraft:models/typewriter_paper_" + i;
			}
			paperModels[i] = model.bake(paperState,  Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
		}
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			String returnValue = location.toString();
			if (returnValue.contains("bibliocraft:models/typewriter0"))
			{
				switch (color)
				{
					case WHITE: { returnValue = "bibliocraft:models/typewriter0"; break; }
					case LIGHT_GRAY: { returnValue = "bibliocraft:models/typewriter1"; break; }
					case GRAY: { returnValue = "bibliocraft:models/typewriter2"; break; }
					case BLACK: { returnValue = "bibliocraft:models/typewriter3"; break; }
					case RED: { returnValue = "bibliocraft:models/typewriter4"; break; }
					case ORANGE: { returnValue = "bibliocraft:models/typewriter5"; break; }
					case YELLOW: { returnValue = "bibliocraft:models/typewriter6"; break; }
					case LIME: { returnValue = "bibliocraft:models/typewriter7"; break; }
					case GREEN: { returnValue = "bibliocraft:models/typewriter8"; break; }
					case CYAN: { returnValue = "bibliocraft:models/typewriter9"; break; }
					case LIGHT_BLUE: { returnValue = "bibliocraft:models/typewriter10"; break; }
					case BLUE: { returnValue = "bibliocraft:models/typewriter11"; break; }
					case PURPLE: { returnValue = "bibliocraft:models/typewriter12"; break; }
					case MAGENTA: { returnValue = "bibliocraft:models/typewriter13"; break; }
					case PINK: { returnValue = "bibliocraft:models/typewriter14"; break; }
					case BROWN: { returnValue = "bibliocraft:models/typewriter15"; break; }
				}
			}
			if (returnValue.contains("bibliocraft:models/typewriter_paper_"))
			{
				
				returnValue = paperTexture;
			}
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(returnValue);
		}
	};
}
