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

import jds.bibliocraft.blocks.BiblioLightBlock;
import jds.bibliocraft.blocks.BlockLampGold;
import jds.bibliocraft.blocks.BlockLampIron;
import jds.bibliocraft.blocks.BlockLanternGold;
import jds.bibliocraft.blocks.BlockLanternIron;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumMetalType;
import jds.bibliocraft.helpers.ModelCache;
import jds.bibliocraft.states.MetalTypeProperty;
import jds.bibliocraft.states.MetalTypeState;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.IModelCustomData;
//import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class BiblioModelColor implements IBakedModel
{
	private IModel model = null;
	private IBakedModel baseModel;
	private String modelLocation = " ";
	private EnumColor blockColor = EnumColor.WHITE;
	private EnumMetalType metalType = EnumMetalType.GOLD;
	private String innerColor = "minecraft:blocks/wool_colored_white";
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private ModelCache cache;
	private boolean gotOBJ = false; 
	
	//@AtlasSprite(resource = "randomthings:blocks/fluidDisplay")
	//public static TextureAtlasSprite defaultSprite 
	
	public BiblioModelColor(String modelLoc)
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
	             model = ((IModel)model).process(ImmutableMap.of("flip-v", "true"));
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
			if (exState.getUnlistedNames().contains(MetalTypeProperty.instance))
			{
				MetalTypeState metal = (MetalTypeState)exState.getValue(MetalTypeProperty.instance);
				this.metalType = metal.getMetalType();
			}
			this.blockColor = (EnumColor)exState.getValue(BiblioLightBlock.COLOR);
		}
		
		Function<ResourceLocation, TextureAtlasSprite> texture = textureGetter;
		if (state != null && state instanceof IExtendedBlockState)
		{
			   IBakedModel bakedModel = model.bake(modelState,  DefaultVertexFormats.ITEM, texture);
			   this.baseModel = bakedModel;
		}
		else
		{
			String name = blockColor.getName() + metalType.getID();
			if (cache.hasModel(name))
			{
				this.baseModel = cache.getCurrentMatch();
			}
			else
			{
			    IBakedModel bakedModel = model.bake(modelState,  DefaultVertexFormats.ITEM, texture);
			    if (gotOBJ)
			    	cache.addToCache(bakedModel, name);
			    this.baseModel = bakedModel;
			}
			
		}
	}
	
	/** get an ArrayList of default models parts to be shown. Used for default item display behaviour */
	public abstract List<String> getDefaultVisiableModelParts();
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getTextureLocation(blockColor, metalType, location.toString()));
		}
	};
	
	/** should return the correct texure location string based on the incoming string. To use default textures from .mtl, just return resourceLocation */
	public abstract String getTextureLocation(EnumColor color, EnumMetalType metal, String resourceLocation);
/*
	@Override
	public IBakedModel handleItemState(ItemStack stack)
	{
		this.blockColor = EnumColor.getColorEnumFromID(stack.getItemDamage());
		if (stack != null)
		{
			Item item = stack.getItem();
			if (item == Item.getItemFromBlock(BlockLampGold.instance) || item == Item.getItemFromBlock(BlockLanternGold.instance))
			{
				this.metalType = EnumMetalType.GOLD;
			}
			else if (item == Item.getItemFromBlock(BlockLampIron.instance) || item == Item.getItemFromBlock(BlockLanternIron.instance))
			{
				this.metalType = EnumMetalType.IRON;
			}
		}
		getModel(null);
		return this;
	}

	
	@Override
	public IBakedModel handleBlockState(IBlockState state) 
	{
		getModel(state);
		return this;
	}
/*
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing face) 
	{
		return baseModel.getFaceQuads(face);
	}

	@Override
	public List<BakedQuad> getGeneralQuads() 
	{
		return baseModel.getGeneralQuads();
	}
	
		@Override
	public VertexFormat getFormat() 
	{
		return Attributes.DEFAULT_BAKED_FORMAT;
	}
*/
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
			transform = new TRSRTransformation(new Vector3f(1.5f, 0.25f, 0.9f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 	
			}
			case FIRST_PERSON_LEFT_HAND: 
			{ 
			transform = new TRSRTransformation(new Vector3f(1.75f, 0.2f, -1.37f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f));
			transform = getTweakedLEFTHANDTransform(transform);
			break; 
			}
			case THIRD_PERSON_RIGHT_HAND: 
			{ 
			transform = new TRSRTransformation(new Vector3f(0.9f, 0.4f, 0.2f), 
											   new Quat4f(0.0f, 0.0f, -0.25f, 1.0f), 
											   new Vector3f(0.65f, 0.65f, 0.65f), 
											   new Quat4f(1.0f, 0.0f, 0.0f, 1.0f));
			transform = transform.compose(	   new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
			break; 
			}
			case THIRD_PERSON_LEFT_HAND: 
			{ 
			transform = new TRSRTransformation(new Vector3f(-0.28f, 1.2f, 0.2f), 
											   new Quat4f(0.0f, 0.0f, -0.25f, 1.0f), 
											   new Vector3f(0.65f, 0.65f, 0.65f), 
											   new Quat4f(1.0f, 0.0f, 0.0f, 1.0f));
			transform = transform.compose(	   new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
											   new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
											   new Vector3f(1.0f, 1.0f, 1.0f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
			transform = getTweakedLEFTHANDTransform(transform);
			break;
			}
			case GUI: 
			{ 
			transform = new TRSRTransformation(new Vector3f(0.0f, 0.75f, 0.0f), 
											   new Quat4f(0.25f, 1.0f, 0.25f, 1.0f), 
											   new Vector3f(0.75f, 0.75f, 0.75f), 
											   new Quat4f(0.0f, 0.4f, 0.0f, 1.0f));
			transform = getTweakedGUITransform(transform);
			break; 
			}
			case GROUND:
			{ 
			transform = new TRSRTransformation(new Vector3f(0.55f, 0.1f, 0.55f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
											   new Vector3f(0.5f, 0.5f, 0.5f), 
											   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
			break; 
			}
			case FIXED:
			{ 
			transform = new TRSRTransformation(new Vector3f(0.83f, 0.15f, -0.8f), 
											   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
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
			//System.out.println("The quads returned a null value" + e); // this fires a lot on a bad world and is messy
			return new ArrayList<BakedQuad>();
		}
		/*
		if (this.baseModel != null)
		{
			return this.baseModel.getQuads(state, side, rand);
		}
		else
		{
			List<BakedQuad> blankList = new ArrayList<BakedQuad>();
			return blankList;
		}*/
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
			blockColor = EnumColor.getColorEnumFromID(stack.getItemDamage());
			if (stack != ItemStack.EMPTY)
			{
				Item item = stack.getItem();
				if (item == Item.getItemFromBlock(BlockLampGold.instance) || item == Item.getItemFromBlock(BlockLanternGold.instance))
				{
					metalType = EnumMetalType.GOLD;
				}
				else if (item == Item.getItemFromBlock(BlockLampIron.instance) || item == Item.getItemFromBlock(BlockLanternIron.instance))
				{
					metalType = EnumMetalType.IRON;
				}
			}
			getModel(null, 0);
			return wrapper;
		}
	}
}
