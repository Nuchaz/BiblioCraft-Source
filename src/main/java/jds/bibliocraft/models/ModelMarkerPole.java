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

import jds.bibliocraft.blocks.BlockMarkerPole;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelMarkerPole implements IBakedModel
{
	private IModel model = null;
	private IBakedModel baseModel;
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockMarkerPole.name);
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	
	public ModelMarkerPole()
	{
		this.wrapper = this;
	}
	
	private void getModel(IBlockState state)
	{
	   if (this.model == null || (this.model != null && !this.model.toString().contains("obj.OBJModel")))
        {
	         try
	         {
	             this.model = ModelLoaderRegistry.getModel(new ResourceLocation("bibliocraft:block/markerpole.obj")); 
	         }
	         catch (Exception e)
	         {
	             this.model = ModelLoaderRegistry.getMissingModel();
	         }
        }
	   IModel newModel = model.process(ImmutableMap.of("flip-v", "true"));
	   OBJModel.OBJState modelState = new OBJModel.OBJState(Lists.newArrayList(OBJModel.Group.ALL), true);
		if (state != null && state instanceof IExtendedBlockState)
		{
			IExtendedBlockState exState = (IExtendedBlockState)state;
			if (exState.getUnlistedNames().contains(OBJModel.OBJProperty.INSTANCE))
			{
				modelState = exState.getValue(OBJModel.OBJProperty.INSTANCE);
			}
		}
	   IBakedModel bakedModel = newModel.bake(modelState,  DefaultVertexFormats.ITEM, textureGetter);
	   this.baseModel = bakedModel;
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("bibliocraft:models/markerpole");
		}
	};

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
				transform = new TRSRTransformation(new Vector3f(0.5f, 0.25f, 0.4f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case THIRD_PERSON_RIGHT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case GUI: 
			{ 
				transform = new TRSRTransformation(new Vector3f(1.0f, 0.0f, 0.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case GROUND:
			{ 
				transform = new TRSRTransformation(new Vector3f(0.5f, 0.0f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case FIXED:
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
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
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		getModel(state);
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
			getModel(null);
			return wrapper;
		}
	}

}
