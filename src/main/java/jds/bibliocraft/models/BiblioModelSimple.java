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
import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.Pair;

import jds.bibliocraft.helpers.ModelCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
//import net.minecraftforge.client.model.IBakedModel;
import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.ISmartBlockModel;
//import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class BiblioModelSimple implements IBakedModel //, ISmartBlockModel, ISmartItemModel
{
	private IModel model = null;
	private IBakedModel baseModel;
	private String modelLocation = " ";
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private ModelCache cache;
	private boolean gotOBJ = false; 
	
	public BiblioModelSimple(String modelLoc)
	{
		this.modelLocation = modelLoc;
		this.wrapper = this;
		this.cache = new ModelCache();
	}
	
	private void getModel(IBlockState state, int attempt)
	{
		if (this.model == null || (this.model != null && !this.model.toString().contains("obj.OBJModel")))
		{
	         try
	         {
	             this.model = ModelLoaderRegistry.getModel(new ResourceLocation(this.modelLocation)); 
	             model = model.process(ImmutableMap.of("flip-v", "true"));
	             gotOBJ = true;
	         }
	         catch (Exception e)
	         {
	             this.model = ModelLoaderRegistry.getMissingModel();
	             gotOBJ = false;
	             if (attempt < 6)
	             {
	            	 getModel(state, attempt + 1);
	            	 return;
	             }
	         }
		}
		
		OBJModel.OBJState modelState = new OBJModel.OBJState(getDefaultVisiableModelParts(), true); 
		if (state != null && state instanceof IExtendedBlockState)
		{

			IExtendedBlockState exState = (IExtendedBlockState)state;
			if (exState.getUnlistedNames().contains(OBJModel.OBJProperty.INSTANCE))
			{
				modelState = exState.getValue(OBJModel.OBJProperty.INSTANCE);
			}
			if (modelState == null)
			{
				return;
			}
			getAdditionalModelStateStuff(exState);
		}
		
		Function<ResourceLocation, TextureAtlasSprite> texture = textureGetter;
		if (state != null && state instanceof IExtendedBlockState)
		{
			   IBakedModel bakedModel = model.bake(modelState,  DefaultVertexFormats.ITEM, texture);
			   this.baseModel = bakedModel;
		}
		else
		{
			if (cache.hasModel(texture.toString()))
			{
				this.baseModel = cache.getCurrentMatch();
			}
			else
			{
			    IBakedModel bakedModel = model.bake(modelState,  DefaultVertexFormats.ITEM, texture);
			    if (gotOBJ)
			    	cache.addToCache(bakedModel, texture.toString());
			    this.baseModel = bakedModel;
			}
			
		}
		//System.out.println(test.toString());
		


	}
	
	public void getAdditionalModelStateStuff(IExtendedBlockState state)
	{
		
	}
	
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		return modelParts;
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			// the color thing doesnt work right
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getTextureLocation(location.toString()));
		}
	};
	
	public String getTextureLocation(String resourceLocation)
	{
		return resourceLocation;
	}
	
	@Override
	public boolean isAmbientOcclusion() 
	{
		return false;
	}

	@Override
	public boolean isGui3d() 
	{
		return false;
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
			transform = new TRSRTransformation(new Vector3f(1.4f, 0.2f, -1.5f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			transform = getTweakedRIGHTHANDTransform(transform);
			break; 
			}
			case FIRST_PERSON_LEFT_HAND: 
			{ 
			transform = new TRSRTransformation(new Vector3f(1.4f, 0.2f, 0.5f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			transform = getTweakedLEFTHANDTransform(transform);
			break; 
			}
			case THIRD_PERSON_RIGHT_HAND: 
			{ 
			transform = new TRSRTransformation(new Vector3f(0.6f, 0.8f, 0.25f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(0.65f, 0.65f, 0.65f), 
											   new Quat4f(1.0f, 0.0f, 0.0f, 1.0f));
			transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
			transform = getTweakedRIGHTHANDTransform(transform);
			break; 
			}
			case THIRD_PERSON_LEFT_HAND: 
			{ 
			transform = new TRSRTransformation(new Vector3f(0.65f, -0.5f, 0.25f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(0.65f, 0.65f, 0.65f), 
											   new Quat4f(1.0f, 0.0f, 0.0f, 1.0f));
			transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
																   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
																   new Vector3f(1.0f, 1.0f, 1.0f), 
																   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
			transform = getTweakedLEFTHANDTransform(transform);
			break; 
			}
			case GUI: 
			{ 
			transform = new TRSRTransformation(new Vector3f(0.0f, 0.26f, 0.0f), 
											   new Quat4f(0.25f, 1.0f, 0.25f, 1.0f), 
											   new Vector3f(0.75f, 0.75f, 0.75f), 
											   new Quat4f(0.0f, 0.4f, 0.0f, 1.0f));
			transform = getTweakedGUITransform(transform);
			break; 
			}
			case GROUND:
			{ 
			transform = new TRSRTransformation(new Vector3f(0.5f, 0.05f, 0.5f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(0.5f, 0.5f, 0.5f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 
			}
			case FIXED:
			{ 
			transform = new TRSRTransformation(new Vector3f(-0.75f, 0.12f, 0.75f), 
											   new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
											   new Vector3f(0.75f, 0.75f, 0.75f), 
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
		transform = getTweakedMasterTransfer(transform);
		return Pair.of(this, transform.getMatrix());
	}
	
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		return transform;
	}
	
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		return transform;
	}
	
	public TRSRTransformation getTweakedLEFTHANDTransform(TRSRTransformation transform)
	{
		return transform;
	}
	
	public TRSRTransformation getTweakedRIGHTHANDTransform(TRSRTransformation transform)
	{
		return transform;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		getModel(state, 0);
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
			getModel(null, 0);
			return wrapper;
		}
	}
}
