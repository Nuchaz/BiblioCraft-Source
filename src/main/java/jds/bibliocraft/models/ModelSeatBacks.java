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

import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.helpers.ModelCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

public abstract class ModelSeatBacks implements IBakedModel
{
	private IBakedModel baseModel;
	List<String> modelParts;
	IModel model = null;
	private String woodTextureString = "none";
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private ModelCache cache;
	private boolean gotOBJ = false; 
	
	public ModelSeatBacks(IBakedModel model)
	{
		this.modelParts = new ArrayList<String>();
		this.modelParts = getModelParts(this.modelParts);
		this.wrapper = this;
		this.cache = new ModelCache();
	}
	
	public abstract List<String> getModelParts(List<String> parts);

	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			String texture = location.toString();
			if (texture.contains("minecraft:blocks/planks_spruce"))
			{
				texture = woodTextureString;
			}
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture);
		}
	};
	
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
		if (this.baseModel.getParticleTexture() == null)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_oak");
		}
		return this.baseModel.getParticleTexture();
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
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
        TRSRTransformation transform = new TRSRTransformation(ModelRotation.X0_Y90);
        
		switch (cameraTransformType)
		{
		case FIRST_PERSON_RIGHT_HAND: 
		{ 
			transform = new TRSRTransformation(new Vector3f(1.2f, -0.6f, 0.5f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 
		}
		case FIRST_PERSON_LEFT_HAND: 
		{ 
			transform = new TRSRTransformation(new Vector3f(1.25f, -0.6f, -1.6f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f));
			break; 
		}
		case THIRD_PERSON_RIGHT_HAND: 
		{ 
			transform = new TRSRTransformation(new Vector3f(0.2f, -0.45f, 0.1f), 
											   new Quat4f(0.7f, 0.0f, 0.2f, 1.0f), 
											   new Vector3f(0.5f, 0.5f, 0.5f), 
											   new Quat4f(0.0f, -0.3f, 0.0f, 1.0f));
			break; 
		}
		case THIRD_PERSON_LEFT_HAND: 
		{ 
			transform = new TRSRTransformation(new Vector3f(0.2f, 0.5f, -0.5f), 
											   new Quat4f(0.7f, 1.0f, 0.2f, 1.0f), 
											   new Vector3f(0.5f, 0.5f, 0.5f), 
											   new Quat4f(0.0f, 1.3f, 0.0f, 1.0f));
			break; 
		}
		case GUI: 
		{ 
			transform = getAdjustedGUITransform(transform);
			break; 
		}
		case GROUND:
		{ 
			transform = new TRSRTransformation(new Vector3f(0.3f, -0.2f, 0.42f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(0.4f, 0.4f, 0.4f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 
		}
		case FIXED:
		{ 
			transform = new TRSRTransformation(new Vector3f(-0.91f, -0.4f, 0.6f), 
					   						   new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
					   						   new Vector3f(0.9f, 0.9f, 0.9f), 
					   						   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 
		}
		default: break;
		}
        return Pair.of(this,  transform.getMatrix());
	}
	
	public abstract TRSRTransformation getAdjustedGUITransform(TRSRTransformation transform);

	
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
			String customTextureLocation = "none";
			NBTTagCompound tags = stack.getTagCompound();
			if (tags != null && tags.hasKey("renderTexture")) 
			{
				customTextureLocation = tags.getString("renderTexture");
			} 
	        
	    	switch (EnumWoodType.getEnum(stack.getItemDamage()))
			{
				case OAK: {woodTextureString = "minecraft:blocks/planks_oak"; break;}
				case SPRUCE: {woodTextureString = "minecraft:blocks/planks_spruce"; break;}
				case BIRCH: {woodTextureString = "minecraft:blocks/planks_birch"; break;}
				case JUNGLE: {woodTextureString = "minecraft:blocks/planks_jungle"; break;}
				case ACACIA: {woodTextureString = "minecraft:blocks/planks_acacia"; break;}
				case DARKOAK: {woodTextureString = "minecraft:blocks/planks_big_oak"; break;}
				case FRAME: 
				{
					if (customTextureLocation.contains("none") || customTextureLocation.contains("minecraft:white"))
					{
						woodTextureString = "bibliocraft:blocks/frame"; 
					}
					else
					{
						woodTextureString = customTextureLocation;
					}
					break;
				}
				default: {woodTextureString = "minecraft:blocks/planks_oak"; break;}	
			}
		    	
	        if (model == null || (model != null && !model.toString().contains("obj.OBJModel")))
	        {
		         try
		         {
		            model = ModelLoaderRegistry.getModel(new ResourceLocation("bibliocraft:block/seat.obj")); 
		            model = model.process(ImmutableMap.of("flip-v", "true"));
		            gotOBJ = true;
		         }
		         catch (Exception e)
		         {
		            model = ModelLoaderRegistry.getMissingModel();
		         }
		        model = model.process(ImmutableMap.of("flip-v", "true"));
	        }
	        
	        if (cache.hasModel(woodTextureString))
			{
				baseModel = cache.getCurrentMatch();
			}
			else
			{
				TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				OBJModel.OBJState partList = new OBJModel.OBJState(modelParts, true, transform);
			    IBakedModel bakedModel = model.bake(partList,  DefaultVertexFormats.ITEM, textureGetter);
			    if (gotOBJ)
			    	cache.addToCache(bakedModel, woodTextureString);
			    baseModel = bakedModel;
			}
			
			
			return wrapper;
		}
	}
}
