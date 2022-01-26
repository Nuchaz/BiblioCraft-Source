package jds.bibliocraft.events;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.Config;
import jds.bibliocraft.items.ItemAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderAtlasFace 
{
	private IBakedModel pinModels[];
	private IBakedModel atlasModel;
	private Tessellator tessellator;
	private BufferBuilder worldRenderer;
	private int color = 0;
	private boolean init = false;
	private ResourceLocation modelLocation = new ResourceLocation("bibliocraft:item/atlascover.obj");
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
	
	@SubscribeEvent
	public void renderItem(RenderSpecificHandEvent event)
	{
		// this only ever runs in first person mode
		Minecraft mc = Minecraft.getMinecraft();
		boolean is1stperson = mc.gameSettings.thirdPersonView == 0;
		ItemStack stack = mc.player.getHeldItem(event.getHand());
		//System.out.println("hand = " + event.getHand() + " stack = " + stack);
		if (Config.enableAtlas && stack.getItem() == ItemAtlas.instance && is1stperson)
		{
			if (!init)
			{
				initVariables(event);
				init = true;
			}
		    boolean isSmallLeft = false;
		    boolean isSmallRight = false;
		    if (event.getHand() == EnumHand.OFF_HAND)
		    {
		    	isSmallLeft = true;
		    }
		    
		    if (event.getHand() == EnumHand.MAIN_HAND && mc.player.getHeldItem(EnumHand.OFF_HAND) != ItemStack.EMPTY && mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() != Items.AIR)
		    {
		    	isSmallRight = true;
		    }
		    
			float swing = event.getSwingProgress();
			float equip = event.getEquipProgress();
	        float mapPitch = this.getMapAngleFromPitch(event.getInterpolatedPitch());
			draw(atlasModel, mapPitch, swing, equip, isSmallLeft, isSmallRight, 0, 0, stack, true, CommonProxy.ATLASCOVER);
			renderPins(stack, mapPitch, swing, equip, isSmallLeft, isSmallRight);
		}
	}
	
	private void initVariables(RenderSpecificHandEvent event)
	{
	    if (this.tessellator == null || this.worldRenderer == null)
	    {
	    	this.tessellator = Tessellator.getInstance();
	    	this.worldRenderer = tessellator.getBuffer();
	    }
		pinModels = new IBakedModel[16];
		List<String> pinString = new ArrayList<String>();
		pinString.add("pin");
		for (int i = 0; i < 16; i++)
		{
			color = i;
			pinModels[i] = initModel(pinString);
		}
		List<String> atlasString = new ArrayList<String>();
		atlasString.add("atlascover");
		color = 16;
		atlasModel = initModel(atlasString);
	}
	
    private void renderMapFirstPerson(ItemStack stack)
    {
    	if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemMap)
    	{
	    	Minecraft mc = Minecraft.getMinecraft();
	        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
	        //GlStateManager.scale(0.38F, 0.38F, 0.38F);
	        GlStateManager.disableLighting();
	        mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder vertexbuffer = tessellator.getBuffer();
	        //GlStateManager.translate(-0.5F, -0.5F, 0.0F);
	        GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
	        
	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
	        vertexbuffer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
	        vertexbuffer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
	        vertexbuffer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
	        vertexbuffer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
	        tessellator.draw();
	        
	        MapData mapdata = Items.FILLED_MAP.getMapData(stack, mc.world);
	
	        if (mapdata != null)
	        {
	            mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
	        }
	
	        GlStateManager.enableLighting();
    	}
    }
	
	private void draw(IBakedModel model, float pitch, float swing, float equip, boolean smallLeft, boolean smallRight, double pinx, double piny, ItemStack stack, boolean withMap, ResourceLocation texture)
	{
		GlStateManager.pushMatrix();
		//
		GlStateManager.disableLighting();
		
		//when small
		if (smallLeft || smallRight)
			pitch = 0.0F;
		
		//main logic
        float f = MathHelper.sqrt(swing);
        float f1 = -0.2F * MathHelper.sin(swing * (float)Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float)Math.PI);
        GlStateManager.translate(0.0F, -f1 / 2.0F, f2);
        GlStateManager.translate(0.0F, 0.04F + equip * -1.2F + pitch * -0.5F, -0.72F);
        GlStateManager.rotate(pitch * -85.0F, 1.0F, 0.0F, 0.0F);
        
		GlStateManager.translate(0.38 + 0.0, 0.38, -0.7 + 0.68);
		GlStateManager.rotate(90f, 0.0f, -1.0f, 0.0f);
		
		//small size
		if (smallLeft || smallRight)
		{
			double scaler = 0.489;
			GlStateManager.scale(scaler, scaler, scaler);
			
			if (smallRight)
			{
				GlStateManager.translate(0.0, -0.885, -0.865);
			}
			else
			{
				GlStateManager.translate(0.0, -0.885, 1.66);
			}
		}

		GlStateManager.scale(0.76, 0.76, 0.76);   
		GlStateManager.translate(0.0f, piny, pinx);
		Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().LOCATION_BLOCKS_TEXTURE);
	    worldRenderer.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);
		for (BakedQuad quad :  model.getQuads(null, null, 0))
		{
			LightUtil.renderQuadColor(worldRenderer, quad, 0xFFFFFFFF);
		}
		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

	}
	
	private void renderPins(ItemStack stack, float pitch, float swing, float equip, boolean isSmallLeft, boolean isSmallRight)
	{
		ItemStack map = getAtlasCurrentMap(stack);
		NBTTagCompound tags = stack.getTagCompound();
		if (map.getItem() == Items.FILLED_MAP && tags != null)
		{
			if (tags.hasKey("maps"))
			{
				NBTTagList mapList = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < mapList.tagCount(); i++)
				{
					NBTTagCompound mapTag = mapList.getCompoundTagAt(i);
					if (mapTag.hasKey("mapName") && mapTag.getString("mapName").contentEquals("Map_"+map.getItemDamage()))
					{
						if (mapTag.hasKey("xMapWaypoints") && mapTag.hasKey("yMapWaypoints") && mapTag.hasKey("MapWaypointColors"))
						{
							NBTTagList mapXPins = mapTag.getTagList("xMapWaypoints", Constants.NBT.TAG_FLOAT);
							NBTTagList mapYPins = mapTag.getTagList("yMapWaypoints", Constants.NBT.TAG_FLOAT);
							NBTTagList mapPinColors = mapTag.getTagList("MapWaypointColors", Constants.NBT.TAG_FLOAT); 
							float x = 0.0f;
							float y = 0.0f;
							float color = 0.0f;
							//System.out.println(mapXPins.tagCount());
							for (int n = 0; n < mapXPins.tagCount(); n++)
							{
								x = mapXPins.getFloatAt(n);
								y = mapYPins.getFloatAt(n);
								color = mapPinColors.getFloatAt(n);
								draw(pinModels[(int)color], pitch, swing, equip, isSmallLeft, isSmallRight, 1.0 - x, 1.0 - y, stack, false, getColorResource((int)color));
							}
						}
					}
				}
			}
		}
	}
	
	private ItemStack getAtlasCurrentMap(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			int mapSlot = tags.getInteger("mapSlot");
			if (mapSlot >= 0)
			{

				InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", false, 216);
				NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < tagList.tagCount(); i++)
				{
					NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
					byte slot = tag.getByte("Slot");
					if (slot >= 0 && slot < atlasInventory.getSizeInventory())
					{
						ItemStack invStack = new ItemStack(tag);
						atlasInventory.setInventorySlotContents(slot, invStack);
					}
				}
				ItemStack invStack = atlasInventory.getStackInSlot(mapSlot);
				return invStack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	private IBakedModel initModel(List<String> parts)
	{
		IModel model = null;
		try
		{
			model = ModelLoaderRegistry.getModel(modelLocation);
			model = model.process(ImmutableMap.of("flip-v", "true"));
		}
		catch (Exception e)
		{
			System.out.println("model failed to load");
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
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getColorTexture(color));
		}
	};
	
	private String getColorTexture(int color)
	{
		switch (color)
		{
		case 0:return "minecraft:blocks/wool_colored_black";
		case 1:return "minecraft:blocks/wool_colored_red";
		case 2:return "minecraft:blocks/wool_colored_green";
		case 3:return "minecraft:blocks/wool_colored_lime";
		case 4:return "minecraft:blocks/wool_colored_brown";
		case 5:return "minecraft:blocks/wool_colored_blue";
		case 6:return "minecraft:blocks/wool_colored_cyan";
		case 7:return "minecraft:blocks/wool_colored_light_blue";
		case 8:return "minecraft:blocks/wool_colored_purple";
		case 9:return "minecraft:blocks/wool_colored_magenta";
		case 10:return "minecraft:blocks/wool_colored_pink";
		case 11:return "minecraft:blocks/wool_colored_yellow";
		case 12:return "minecraft:blocks/wool_colored_orange";
		case 13:return "minecraft:blocks/wool_colored_gray";
		case 14:return "minecraft:blocks/wool_colored_silver";
		case 15:return "minecraft:blocks/wool_colored_white";
		case 16:return "bibliocraft:gui/atlas_cover";
		case 17:return "bibliocraft:gui/atlas_cover";
		default:return "minecraft:blocks/wool_colored_white";
		}
	}
	
	public ResourceLocation getColorResource(int color)
	{
		switch (color)
		{
			case 0:return CommonProxy.BLACKWOOL;
			case 1:return CommonProxy.REDWOOL;
			case 2:return CommonProxy.GREENWOOL;
			case 3:return CommonProxy.LIMEWOOL;
			case 4:return CommonProxy.BROWNWOOL;
			case 5:return CommonProxy.BLUEWOOL;
			case 6:return CommonProxy.CYANWOOL;
			case 7:return CommonProxy.LBLUEWOOL;
			case 8:return CommonProxy.PURPLEWOOL;
			case 9:return CommonProxy.MAGENTAWOOL;
			case 10:return CommonProxy.PINKWOOL;
			case 11:return CommonProxy.YELOOWWOOL;
			case 12:return CommonProxy.ORANGEWOOL;
			case 13:return CommonProxy.GRAYWOOL;
			case 14:return CommonProxy.LGRAYWOOL;
			case 15:return CommonProxy.WHITEWOOL;
			default:return CommonProxy.REDWOOL;
		}
	}
	
    private float getMapAngleFromPitch(float pitch)
    {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5F + 0.5F;
        return f;
    }
}
