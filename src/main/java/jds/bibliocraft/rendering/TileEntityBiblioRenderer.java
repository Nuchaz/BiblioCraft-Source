package jds.bibliocraft.rendering;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
//import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
//import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.model.Attributes;
//import net.minecraftforge.client.model.IBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;

public abstract class TileEntityBiblioRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
	Minecraft mc = Minecraft.getMinecraft();
	private EnumFacing angle = EnumFacing.NORTH;
	private EnumVertPosition vert = EnumVertPosition.FLOOR;
	private EnumShiftPosition shift = EnumShiftPosition.NO_SHIFT;
	public float xshift;
	public float zshift;
	public int degreeAngle;
	public double globalX;
	public double globalY;
	public double globalZ;
	public Tessellator tessellator;
	public BufferBuilder worldRenderer;
	
	private RenderItem itemRenderer;
	private RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
	
	@Override
	public void render(TileEntity tileEntity, double x, double y, double z, float tick, int destroyStage, float what) 
	{
		this.globalX = x;
		this.globalY = y;
		this.globalZ = z;
		BiblioTileEntity tile = (BiblioTileEntity)tileEntity;
		if (tile != null)
		{
			this.angle = tile.getAngle();
			this.vert = tile.getVertPosition();
			this.shift = tile.getShiftPosition();
		}
		xshift = 0.0f;
		zshift = 0.0f;
	    if (this.itemRenderer == null || this.tessellator == null || this.worldRenderer == null)
	    {
	    	this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
	    	this.tessellator = Tessellator.getInstance();
	    	this.worldRenderer = tessellator.getBuffer();
	    }
	    float halfShift = 0.25f;
	    float fullShift = 0.5f;
		switch (angle)
		{
			case SOUTH:
			{
				degreeAngle = 270; 
				xshift = 1.0f; 
				zshift = 0.0f; 
				if (shift == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += -fullShift; 
					zshift += 0;
				} 
				if (shift == EnumShiftPosition.HALF_SHIFT)
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
				if (shift == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += 0.0f; 
					zshift += -fullShift;
				} 
				if (shift == EnumShiftPosition.HALF_SHIFT)
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
				if (shift == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += fullShift; 
					zshift += 0;
				} 
				if (shift == EnumShiftPosition.HALF_SHIFT)
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
				if (shift == EnumShiftPosition.FULL_SHIFT)
				{
					xshift += 0.0f; 
					zshift += fullShift;
				} 
				if (shift == EnumShiftPosition.HALF_SHIFT)
				{
					xshift += 0.0f; 
					zshift += halfShift;
				}
				break;
			}
			default:break;
		}
		render(tile, x, y, z, tick);
	}
	
	public abstract void render(BiblioTileEntity tile, double x, double y, double z, float tick);
	
	public EnumFacing getAngle()
	{
		return this.angle;
	}
	
	public EnumVertPosition getVertPosition()
	{
		return this.vert;
	}
	
	public EnumShiftPosition getShiftPosition()
	{
		return this.shift;
	}
	
	public void renderSlotItem(ItemStack stack, double x, double y, double z, float scale)
	{
		if (stack != null && stack != ItemStack.EMPTY)
		{
			switch (this.angle)
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
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			GlStateManager.translate(this.globalX + x + xshift, this.globalY + y + 0.05, this.globalZ + z + zshift);
			GlStateManager.rotate(degreeAngle+180.0F, 0.0F, 1.0F, 0.0F);
			additionalGLStuffForItemStack();
			Block testBlock = Block.getBlockFromItem(stack.getItem());
			if (isRotatedBlock(stack.getUnlocalizedName()))
			{
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			}
			
			if (stack.getItem() instanceof ItemClipboard)
			{
				GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.4, 0.05, 0.0);
			}
			
			if (testBlock == null)
			{
				// is item
				scale *= 0.7f;
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			}
			GlStateManager.scale(scale, scale, scale);
			
			this.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);	
			GlStateManager.popMatrix();
		}
	}
	
	private final String itemsThatNeedRotating[] = {
			"swordpedestal", "Typewriter", "BiblioCraft:PaintingPress", 
			"BiblioCraft:PrintingPress", "BiblioCraft:TypesettingTable", "Lantern", "Lamp", "item.tconstruct"};
	
	private boolean isRotatedBlock(String name)
	{
		boolean value = false;
		for (int i = 0; i < itemsThatNeedRotating.length; i++)
		{
			if (name.contains(itemsThatNeedRotating[i]))
			{
				value = true;
			}
		}
		return value;
	}
	
	public void additionalGLStuffForItemStack()
	{
		
	}
	
    public void renderItemMap(ItemStack stack, float x, float y, float z, float scale)
    {
    	GlStateManager.pushMatrix();
    	GlStateManager.translate(this.globalX + x, this.globalY + y + 1.02f, this.globalZ + z);
    	GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
    	scale *= 0.0063;
    	GlStateManager.scale(scale, scale, scale);
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata =Items.FILLED_MAP.getMapData(stack, this.getWorld());
        if (mapdata != null)
        {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
        GlStateManager.popMatrix();
    }
    
	public IBakedModel initModel(List<String> parts, ResourceLocation modelResource)
	{
		IModel model = null;
		try
		{
			model = ModelLoaderRegistry.getModel(modelResource); 
			model = model.process(ImmutableMap.of("flip-v", "true")); 
		}
		catch (Exception e)
		{
			model = ModelLoaderRegistry.getMissingModel();
		} 
		OBJModel.OBJState state = new OBJModel.OBJState(parts, true);
		return  model.bake(state,  Attributes.DEFAULT_BAKED_FORMAT, getModelTexture);
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> getModelTexture = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getTextureString(location));
		}
	};
	
	public String getTextureString(ResourceLocation location)
	{
		return location.toString();
	}
	
	public void renderText(String text, double xAdjust, double yAdjust, double zAdjust)
	{
		FontRenderer fontRender = this.getFontRenderer();
		float offsetx = 0.0f;
		float offsetz = 0.0f;
		switch (this.getAngle())
		{
			case NORTH:{offsetx = 0.0116f; break;}
			case SOUTH:{offsetx = -0.0116f;  break; }
			case WEST:{offsetz = -0.0116f; break;}
			case EAST:{offsetz = 0.0116f; break;}
			default: break;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.globalX + 0.5 + offsetx, this.globalY, this.globalZ + 0.5 + offsetz);
		
		switch (this.getAngle())
		{
			case SOUTH:{GlStateManager.rotate(180, 0.0f, 1.0f, 0.0f); break; } 
			case WEST:{GlStateManager.rotate(90, 0.0f, 1.0f, 0.0f); break; } 
			case EAST:{GlStateManager.rotate(-90, 0.0f, 1.0f, 0.0f); break;} 
			default: break;
		}
		
		GlStateManager.translate(-0.5 + xAdjust, yAdjust, zAdjust);
		GlStateManager.depthMask(false);
		GlStateManager.scale(0.0045F, 0.0045F, 0.0045F);
		GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
		switch (this.shift)
		{
			case HALF_SHIFT:
			{
				GlStateManager.translate(0.0, 0.0, -95.0);
				break;
			}
			case FULL_SHIFT:
			{
				GlStateManager.translate(0.0, 0.0, -205.0);
				break;
			}
			default: break;
		}
		additionalGLStuffForText();
        GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
		fontRender.drawString(text, 0, 0, 0); 
		GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
	
	public void additionalGLStuffForText()
	{
		
	}
}
