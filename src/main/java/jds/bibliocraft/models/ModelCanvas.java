package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.apache.commons.lang3.tuple.Pair;

import jds.bibliocraft.helpers.BiblioEnums.EnumBiblioPaintings;
import jds.bibliocraft.helpers.ModelCache;
import jds.bibliocraft.helpers.PaintingUtil;
import jds.bibliocraft.items.ItemPaintingCanvas;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelCanvas implements IBakedModel
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + ItemPaintingCanvas.name);
	private IBakedModel baseModel;
	List<String> modelParts;
	IModel model = null;
	private String paintingTitle = "blank";
	private int paintingType = 0;
	private EnumBiblioPaintings[] biblioArtList = EnumBiblioPaintings.values();
	private EnumArt[] vanillaArtList = EnumArt.values();
	private BufferBuilder worldRenderer; // = Minecraft.getMinecraft().getRenderManager().
	public Tessellator tessellator;
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private ModelCache cache;
	private boolean gotOBJ = false; 
	
	public ModelCanvas(IBakedModel model)
	{
		this.baseModel = model;
		this.modelParts = new ArrayList<String>();
		this.modelParts.add("canvas"); 
		this.modelParts.add("painting"); 
		this.wrapper = this;
		this.cache = new ModelCache();
	}
	
	private void renderVanillaPainting(float i, float j, float k)
	{
		for (int x = 0; x<this.vanillaArtList.length; x++)
		{
			if (this.paintingTitle.contentEquals(this.vanillaArtList[x].title))
			{
		        float x1 = (float)(vanillaArtList[x].offsetX) / 256.0F;
		        float x2 = (float)(vanillaArtList[x].offsetX + vanillaArtList[x].sizeX) / 256.0F;
		        float y1 = (float)(vanillaArtList[x].offsetY) / 256.0F;
		        float y2 = (float)(vanillaArtList[x].offsetY + vanillaArtList[x].sizeY) / 256.0F;
				worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
				//tess.startDrawingQuads();
				worldRenderer.pos(0.035, -0.4, -0.4).tex(x2, y2);
				worldRenderer.pos(0.035,  0.4, -0.4).tex(x2, y1);
				worldRenderer.pos(0.035,  0.4,  0.4).tex(x1, y1);
				worldRenderer.pos(0.035, -0.4,  0.4).tex(x1, y2);
				tessellator.draw();
			}
		}
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			String output = location.toString();
			if (!paintingTitle.contains("blank") && output.contains("bibliocraft:paintings/canvas"))
			{
				switch (paintingType)
				{
					case 0: // bibliocraft
					{
						for (int i = 0; i < biblioArtList.length; i++)
						{
							if (paintingTitle.contentEquals(biblioArtList[i].title))
							{
								output = biblioArtList[i].paintingTexturesStrings[0][0];
							}
						}
						break;
					}
					case 1: // vanilla
					{
						output = "bibliocraft:paintings/vanilla";
						break;
					}
					case 2: // custom
					{
						if (PaintingUtil.customArtNames != null && PaintingUtil.customArtNames.length > 0 && PaintingUtil.customArtResources != null)
						{
							for (int i = 0; i < PaintingUtil.customArtNames.length; i++)
							{
								if (paintingTitle.contentEquals(PaintingUtil.customArtNames[i]))
								{
									if (PaintingUtil.customArtHeights[i] == PaintingUtil.customArtWidths[i])
									{
										output = PaintingUtil.customArtResourceStrings[i];
									}
									else
									{
										output = "bibliocraft:paintings/custom";
									}
								}
							}
						}	
						break;
					}
				}
			}
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(output);
		}
	};
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
		TRSRTransformation 	transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															   new Vector3f(1.0f, 1.0f, 1.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		switch (cameraTransformType)
		{
			case FIRST_PERSON_RIGHT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, -0.1f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f));
				break; 
			}
			case THIRD_PERSON_RIGHT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(-0.1f, 0.18f, 0.22f), 
												   new Quat4f(0.0f, -1.75f, 0.0f, 1.0f), 
												   new Vector3f(0.5f, 0.5f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));

																     
				break; 
			}
			case THIRD_PERSON_LEFT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(-0.1f, 0.18f, 0.22f), 
												   new Quat4f(0.0f, 0.75f, 0.0f, 1.0f), 
												   new Vector3f(0.5f, 0.5f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));

																     
				break; 
			}
			case GUI: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.33f, -0.0f, 0.0f), 
												   new Quat4f(0.0f, -0.5f, 0.0f, 1.0f), 
												   new Vector3f(1.1f, 1.1f, 1.1f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case GROUND:
			{ 
				transform = new TRSRTransformation(new Vector3f(0.25f, 0.0f, 0.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(0.5f, 0.5f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case FIXED:
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, -0.05f, -0.5f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case NONE:
			{
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			}
			default: break;
		}
		return Pair.of(this, transform.getMatrix());
	}
	
	@Override
	public boolean isGui3d() 
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() 
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() 
	{
		try
		{
			return this.baseModel.getParticleTexture();
		}
		catch (NullPointerException e)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_oak");
		}
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() 
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public boolean isAmbientOcclusion() 
	{
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		try 
		{
			List<BakedQuad> q = this.baseModel.getQuads(state, side, rand);
			return q;
		}
		catch (NullPointerException e)
		{
			return new ArrayList<BakedQuad>();
		}
	}

	@Override
	public ItemOverrideList getOverrides() 
	{
		return overrides;
	}
	
	private class CustomItemOverrideList extends ItemOverrideList
	{
		private CustomItemOverrideList()
		{
			super(ImmutableList.<ItemOverride>of());
		}
		
		@Nonnull
		@Override
		public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entity) 
		{
			if (tessellator == null || worldRenderer == null)
		    {
		    	tessellator = Tessellator.getInstance();
		    	worldRenderer = tessellator.getBuffer();
		    }
		    
	        NBTTagCompound tags = stack.getTagCompound();
	        if (tags != null && tags.hasKey("paintingTitle") && tags.hasKey("paintingType"))
	        {
	        	paintingTitle = tags.getString("paintingTitle");
	        	paintingType = tags.getInteger("paintingType");
	        }
	        else
	        {
	        	paintingTitle = "blank";
	        }

	        if (model == null  || (model != null && !model.toString().contains("obj.OBJModel")))
	        {
		        try
		        {
		            model = ModelLoaderRegistry.getModel(new ResourceLocation("bibliocraft:item/canvas.obj")); 
		             model = model.process(ImmutableMap.of("flip-v", "true"));
		             gotOBJ = true;
		        }
		        catch (Exception e)
		        {
		            model = ModelLoaderRegistry.getMissingModel();
		            gotOBJ = false;
		        }

	        }
	        TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
	        OBJModel.OBJState partList = new OBJModel.OBJState(modelParts, true, transform);
	        
		    if (cache.hasModel(paintingTitle))
			{
				baseModel = cache.getCurrentMatch();
			}
			else
			{
			    IBakedModel bakedModel = model.bake(partList,  DefaultVertexFormats.ITEM, textureGetter);
			    if (gotOBJ)
			    	cache.addToCache(bakedModel, paintingTitle);
			    baseModel = bakedModel;
			}
		    
			return wrapper;
		}
	}


}
