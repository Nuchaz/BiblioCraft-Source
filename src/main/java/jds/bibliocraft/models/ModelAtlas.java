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

import jds.bibliocraft.helpers.ModelCache;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemWaypointCompass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.util.Constants;

public class ModelAtlas implements IBakedModel
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" +  ItemAtlas.name);
	private IBakedModel baseModel;
	private IBakedModel simpleAtlas;
	List<String> simpleParts;
	List<String> modelParts;
	IModel model = null;
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private ModelCache cache;
	private float needle = 1.0f;
	private boolean gotOBJ = false; 
	
	public ModelAtlas(IBakedModel model)
	{
		this.baseModel = model;
		this.modelParts = new ArrayList<String>();
		this.modelParts.add("book");
		this.modelParts.add("compass");
		this.modelParts.add("1");
		this.simpleParts = new ArrayList<String>();
		this.simpleParts.add("book");
		this.wrapper = this;
		this.cache = new ModelCache();
	}

	private boolean checkForMatchingCompass(NBTTagCompound tags, int compass)
	{
		NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < 6)
			{
				ItemStack invStack = new ItemStack(tag);
				//System.out.println(savedComp+"     to     "+slot);
				if (invStack != null && invStack.getItem() instanceof ItemWaypointCompass && compass == slot)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private int getCompassNeedlePart(float needleAngle) 
	{
		while (needleAngle < 0.0f)
		{
			needleAngle += 360.0f;
		}
		while (needleAngle > 360.0f)
		{
			needleAngle -= 360.0f;
		}
		needleAngle /= 10.0f;
		int result = 36 - (int)needleAngle;
		return result;
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
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
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
        TRSRTransformation transform = new TRSRTransformation(ModelRotation.X0_Y90);
        //TRSRTransformation needleTransform = new TRSRTransformation(ModelRotation.X0_Y90);
        
		switch (cameraTransformType)
		{
		case FIRST_PERSON_RIGHT_HAND: 
		{ 
			break; 
		}
		case THIRD_PERSON_RIGHT_HAND: 
		{ 
			transform = new TRSRTransformation(new Vector3f(-0.64f, 0.1f, 0.2f), 
											   new Quat4f(18.0f, 0.0f, 7.0f, 1.0f), 
											   new Vector3f(0.8f, 0.8f, 0.8f), 
											   new Quat4f(-1.1f, 0.0f, 0.1f, 1.0f));
			transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
															     new Quat4f(0.5f, 2.0f, 0.0f, 1.0f), 
															     new Vector3f(1.0f, 1.0f, 1.0f), 
															     new Quat4f(0.0f, 2.0f, 0.0f, 1.0f)));
			break; 
		}
		case THIRD_PERSON_LEFT_HAND: 
		{ 
			transform = new TRSRTransformation(new Vector3f(0.1f, 0.1f, 0.5f), 
											   new Quat4f(18.0f, 0.0f, 7.0f, 1.0f), 
											   new Vector3f(0.8f, 0.8f, 0.8f), 
											   new Quat4f(-1.1f, 0.0f, 0.1f, 1.0f));
			transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
															     new Quat4f(-0.5f, -2.0f, 0.0f, 1.0f), 
															     new Vector3f(1.0f, 1.0f, 1.0f), 
															     new Quat4f(0.0f, -2.0f, 0.0f, 1.0f)));
			break; 
		}
		case GUI: 
		{ 
			transform = new TRSRTransformation(new Vector3f(-0.66f, 0.05f, 0.5f), 
											   new Quat4f(1.0f, -1.0f, -1.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, -0.2f, 0.06f, 0.5f));
			break; 
		}
		case GROUND:
		{ 
			transform = new TRSRTransformation(new Vector3f(0.25f, 0.15f, 0.25f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(0.5f, 0.5f, 0.5f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 
		}
		case FIXED:
		{ 
			transform = new TRSRTransformation(new Vector3f(0.5f, 0.24f, 0.5f), 
					   						   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
					   						   new Vector3f(1.0f, 1.0f, 1.0f), 
					   						   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 
		}
		default: break;
		}
		
        return Pair.of(this,  transform.getMatrix());
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
			if (simpleAtlas == null || (simpleAtlas != null && !simpleAtlas.toString().contains("obj.OBJModel")))
			{
		         try
		         {
		             model = ModelLoaderRegistry.getModel(new ResourceLocation("bibliocraft:item/atlas.obj")); 
		             model = model.process(ImmutableMap.of("flip-v", "true"));
		             gotOBJ = true;
		         }
		         catch (Exception e)
		         {
		             model = ModelLoaderRegistry.getMissingModel();
		             gotOBJ = false;
		         }


		         TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		         OBJModel.OBJState partList = new OBJModel.OBJState(simpleParts, true, transform);
		 		 simpleAtlas = model.bake(partList,  DefaultVertexFormats.ITEM, textureGetter); 
			}
			
			baseModel = simpleAtlas;
			
	        NBTTagCompound tags = stack.getTagCompound();
	        if (tags != null && tags.hasKey("needleAngle") && tags.hasKey("savedCompass"))
	        {
	        	int compass = tags.getInteger("savedCompass");
	        	if (compass != -1 && checkForMatchingCompass(tags, compass))
	        	{
	        		modelParts = new ArrayList<String>();
	    			modelParts.add("book");
	    			modelParts.add("compass");
	    			String part = "" + getCompassNeedlePart(tags.getFloat("needleAngle") + 90.0f);
	    			modelParts.add(part);
	    	        try
	    	        {
	    	        	model = ModelLoaderRegistry.getModel(new ResourceLocation("bibliocraft:item/atlas.obj")); 
	    	        }
	    	        catch (Exception e)
	    	        {
	    	        	model = ModelLoaderRegistry.getMissingModel();
	    	        }
	    	        model = model.process(ImmutableMap.of("flip-v", "true"));
	    	        TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
	    	        OBJModel.OBJState partList = new OBJModel.OBJState(modelParts, true, transform);
	    	        if (cache.hasModel(part))
	    			{
	    				baseModel = cache.getCurrentMatch();
	    			}
	    			else
	    			{
	    			    IBakedModel bakedModel = model.bake(partList,  DefaultVertexFormats.ITEM, textureGetter);
	    			    if (gotOBJ)
	    			    	cache.addToCache(bakedModel, part);
	    			    baseModel = bakedModel;
	    			}
	        	}
	        }
			return wrapper;
		}
	}
}
