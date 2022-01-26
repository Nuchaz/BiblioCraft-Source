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

import jds.bibliocraft.blocks.BlockClipboard;
import jds.bibliocraft.helpers.ModelCache;
import jds.bibliocraft.items.ItemClipboard;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelClipboard implements IBakedModel// ISmartBlockModel, ISmartItemModel, IPerspectiveAwareModel
{
	private IModel model = null;
	private IBakedModel baseModel;
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockClipboard.name);
	public static final ModelResourceLocation clipboardBasicModel = new ModelResourceLocation("bibliocraft:clipboardsimple");
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private FontRenderer textRender;
	private ModelCache cache;
	private boolean gotOBJ = false; 
	
	public ModelClipboard()
	{
		this.textRender = Minecraft.getMinecraft().fontRenderer;
		this.wrapper = this;
		this.cache = new ModelCache();
	}
	
	private void getModel(IBlockState state, ItemStack stack)
	{
		if (this.model == null || (this.model != null && !this.model.toString().contains("obj.OBJModel")))
		{
	         try
	         {
	             this.model = ModelLoaderRegistry.getModel(new ResourceLocation("bibliocraft:block/clipboard.obj")); 
	             this.model = model.process(ImmutableMap.of("flip-v", "true"));
	             gotOBJ = true;
	         }
	         catch (Exception e)
	         {
	             this.model = ModelLoaderRegistry.getMissingModel();
	             gotOBJ = false;
	         }	
        }
	    
	    OBJModel.OBJState modelState = new OBJModel.OBJState(Lists.newArrayList(OBJModel.Group.ALL), true);
		if (state != null && state instanceof IExtendedBlockState)
		{
			IExtendedBlockState exState = (IExtendedBlockState)state;
			if (exState.getUnlistedNames().contains(OBJModel.OBJProperty.INSTANCE))
			{
				modelState = exState.getValue(OBJModel.OBJProperty.INSTANCE);
				IBakedModel bakedModel = this.model.bake(modelState,  DefaultVertexFormats.ITEM, textureGetter);
				this.baseModel = bakedModel;
			}

			if (modelState == null)
			{
				return;
			}
		}
		if (state == null && stack.getItem() == ItemClipboard.instance)
		{
			String cacheName = "clipboard" + getModelPartsNumberString(stack);
			if (cache.hasModel(cacheName))
			{
				baseModel = cache.getCurrentMatch();
			}
			else
			{
				modelState = new OBJModel.OBJState(getModelParts(stack), true);
				IBakedModel bakedModel = this.model.bake(new OBJModel.OBJState(getModelParts(stack), true),  DefaultVertexFormats.ITEM, textureGetter);
			    if (gotOBJ)
			    	cache.addToCache(bakedModel, cacheName);
			    baseModel = bakedModel;
			    
			}
		}
	}
	
	private String getModelPartsNumberString(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		String value = "";
    	if (tags != null)
    	{
    		int currentPage = tags.getInteger("currentPage");
    		int totalPages = tags.getInteger("totalPages");
    		String pagenum = "page"+currentPage;
    		NBTTagCompound pagetag = tags.getCompoundTag(pagenum);
    		if (pagetag != null)
    		{
    			int[] states = pagetag.getIntArray("taskStates");
    			if (states != null && states.length == 9)
    			{
    				for (int i = 0; i < states.length; i++)
    					value += states[i];
    			}
    		}
    	}
    	return value;
	}
	
	public List<String> getModelParts(ItemStack stack) 
	{
		List<String> modelParts = new ArrayList<String>();
		NBTTagCompound tags = stack.getTagCompound();
    	if (tags != null)
    	{
    		int currentPage = tags.getInteger("currentPage");
    		int totalPages = tags.getInteger("totalPages");
    		String pagenum = "page"+currentPage;
    		NBTTagCompound pagetag = tags.getCompoundTag(pagenum);
    		if (pagetag != null)
    		{
    			int[] states = pagetag.getIntArray("taskStates");
    			if (states != null && states.length == 9)
    			{
    				switch (states[0])
    				{
    				case 1: { modelParts.add("box1c"); break; }
    				case 2: { modelParts.add("box1x"); break; }
    				}
    				
    				switch (states[1])
    				{
    				case 1: { modelParts.add("box2c"); break; }
    				case 2: { modelParts.add("box2x"); break; }
    				}
    				
    				switch (states[2])
    				{
    				case 1: { modelParts.add("box3c"); break; }
    				case 2: { modelParts.add("box3x"); break; }
    				}
    				
    				switch (states[3])
    				{
    				case 1: { modelParts.add("box4c"); break; }
    				case 2: { modelParts.add("box4x"); break; }
    				}
    				
    				switch (states[4])
    				{
    				case 1: { modelParts.add("box5c"); break; }
    				case 2: { modelParts.add("box5x"); break; }
    				}
    				
    				switch (states[5])
    				{
    				case 1: { modelParts.add("box6c"); break; }
    				case 2: { modelParts.add("box6x"); break; }
    				}
    				
    				switch (states[6])
    				{
    				case 1: { modelParts.add("box7c"); break; }
    				case 2: { modelParts.add("box7x"); break; }
    				}
    				
    				switch (states[7])
    				{
    				case 1: { modelParts.add("box8c"); break; }
    				case 2: { modelParts.add("box8x"); break; }
    				}
    				
    				switch (states[8])
    				{
    				case 1: { modelParts.add("box9c"); break; }
    				case 2: { modelParts.add("box9x"); break; }
    				}
    			}
    		}
    	}
		modelParts.add("Clipboard");
		return modelParts;
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("bibliocraft:models/clipboard");
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
				transform = new TRSRTransformation(new Vector3f(-0.05f, 0.25f, 0.3f), 
												   new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
												   new Vector3f(0.5f, 0.5f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case FIRST_PERSON_LEFT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.25f, 0.3f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
												   new Vector3f(0.5f, 0.5f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case THIRD_PERSON_RIGHT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.2f, 0.4f), 
												   new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
												   new Vector3f(0.75f, 0.75f, 0.75f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case THIRD_PERSON_LEFT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.2f, 0.4f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
												   new Vector3f(0.75f, 0.75f, 0.75f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case GUI: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
												   new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
												   new Vector3f(1.0f, 1.0f, 1.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
				
			}
			case GROUND:
			{ 
				transform = new TRSRTransformation(new Vector3f(0.35f, 0.15f, 0.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(0.75f, 0.75f, 0.75f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			default: break;
		}
		return Pair.of(this, transform.getMatrix());
		
		
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		getModel(state, ItemStack.EMPTY);
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
			getModel(null, stack);
			//baseModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(modelResourceLocation);
			return wrapper;
		}
	}
}
