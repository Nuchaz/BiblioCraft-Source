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

import jds.bibliocraft.blocks.BiblioWoodBlock;
import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.helpers.ModelCache;
import jds.bibliocraft.states.TextureProperty;
import jds.bibliocraft.states.TextureState;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class BiblioModelWood implements IBakedModel// ,ISmartBlockModel, ISmartItemModel, IPerspectiveAwareModel
{
	private IModel model = null;
	private IBakedModel baseModel;
	private String modelLocation = " ";
	private String textureLocation = "none";
	private String customTextureLocation = "none";
	private EnumWoodType wood = EnumWoodType.FRAME;
	private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private ModelCache cache;
	private boolean gotOBJ = false; 
	
	public BiblioModelWood(String modelLoc)
	{
		this.modelLocation = modelLoc;
		this.wrapper = this;
		this.cache = new ModelCache();
	}
	
	private void getModel(IBlockState state, boolean isBlock, int attempt)
	{
	   if (this.model == null || (this.model != null && !this.model.toString().contains("obj.OBJModel")))
        {
	         try
	         {
	        	 // TODO sometimes this gets casut in a loop and casues a circular dependency error
	        	 //[jds.bibliocraft.models.BiblioModelWood:getModel:91]: Failed to load model. net.minecraftforge.client.model.ModelLoaderRegistry$LoaderException: 
	        	 //circular model dependencies, stack: [bibliocraft:block/shelf.obj, bibliocraft:block/table.obj, bibliocraft:block/case.obj]
	        	 //
	             this.model = ModelLoaderRegistry.getModel(new ResourceLocation(this.modelLocation)); 
	             model = model.process(ImmutableMap.of("flip-v", "true")); 
	             gotOBJ = true;
	         }
	         catch (Exception e)
	         {
	             this.model = ModelLoaderRegistry.getMissingModel();
	             gotOBJ = false;
	             // TODO I could just recursivly call this?, that seems dangourous. I'll do it 6 times then error.
	             if (attempt < 6)
	             {
	            	 getModel(state, isBlock, attempt + 1);
	            	 return;
	             }
	             else
	             {
	            	 //System.out.println("Failed to load model. " + e);
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
			if (exState.getUnlistedNames().contains(TextureProperty.instance))
			{
				TextureState texString = (TextureState)exState.getValue(TextureProperty.instance);
				loadAdditionalTextureStateStuff(texString);
				if (texString != null)
					customTextureLocation = texString.getTextureString();
			}
			getAdditionalBlockStateStuff(exState);
			wood = (EnumWoodType)state.getValue(BiblioWoodBlock.WOOD_TYPE);
		}
		else
		{
			loadAdditionalTextureStateStuff(null);
		}
		try
		{
			switch (wood)
			{
				case OAK: {textureLocation = "minecraft:blocks/planks_oak"; break;}
				case SPRUCE: {textureLocation = "minecraft:blocks/planks_spruce"; break;}
				case BIRCH: {textureLocation = "minecraft:blocks/planks_birch"; break;}
				case JUNGLE: {textureLocation = "minecraft:blocks/planks_jungle"; break;}
				case ACACIA: {textureLocation = "minecraft:blocks/planks_acacia"; break;}
				case DARKOAK: {textureLocation = "minecraft:blocks/planks_big_oak"; break;}
				case FRAME: 
				{
					//System.out.println(customTextureLocation.length());
					if (customTextureLocation.contains("none") || customTextureLocation.contains("minecraft:white") || customTextureLocation.length() == 0)
					{
						textureLocation = "bibliocraft:blocks/frame"; 
					}
					else
					{
						textureLocation = customTextureLocation;
					}
					break;
				}
				default: {textureLocation = "minecraft:blocks/planks_oak"; break;}	
			}
		}
		catch(NullPointerException e)
		{
			System.out.println("Null pointer thrown on obtaining the texture " + e);
		}
		try
		{
			if (state != null && state instanceof IExtendedBlockState)
			{
				//is block
			    IBakedModel bakedModel = model.bake(modelState,  DefaultVertexFormats.ITEM, textureGetter);
			    this.baseModel = bakedModel;
			}
			else
			{
				// is item
			    if (cache.hasModel(textureLocation)) 
				{
					this.baseModel = cache.getCurrentMatch();
				}
				else
				{
				    IBakedModel bakedModel = model.bake(modelState,  DefaultVertexFormats.ITEM, textureGetter);
				    if (gotOBJ)
				    	cache.addToCache(bakedModel, textureLocation);
				    this.baseModel = bakedModel;
				}
			}
		}
		catch (NullPointerException e)
		{
			System.out.println("null pointer exception thrown in attempt bake model(s) " + e);
		}
	}
	
	public void loadAdditionalTextureStateStuff(TextureState state) { }
	
	public void getAdditionalBlockStateStuff(IExtendedBlockState state){ }
	
	public abstract String getTextureLocation(String resourceLocation, String textureLocation);
	
	/** Override this I need to adjust the model parts of the item block */
	public List<String> getDefaultVisiableModelParts()
	{
		return Lists.newArrayList(OBJModel.Group.ALL);
	}
	
	protected Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Override
		public TextureAtlasSprite apply(ResourceLocation location)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getTextureLocation(location.toString(), textureLocation));
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
		TRSRTransformation 	transform = new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															   new Vector3f(1.0f, 1.0f, 1.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		
		switch (cameraTransformType)
		{
			case FIRST_PERSON_RIGHT_HAND:
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.1f, -0.1f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
												   new Vector3f(0.65f, 0.65f, 0.65f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f));
				break; 
			}
			case FIRST_PERSON_LEFT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.1f, -0.1f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(0.65f, 0.65f, 0.65f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				transform = getTweakedLeftHandTransform(transform);
				break; 
			}
			case THIRD_PERSON_RIGHT_HAND: 
			{ 
				transform = new TRSRTransformation(new Vector3f(-0.15f, 0.15f, -0.05f), 
												   new Quat4f(0.0f, -1.5f, 0.0f, 1.0f), 
												   new Vector3f(0.5f, 0.5f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
																     
				break; 
			}
			case THIRD_PERSON_LEFT_HAND:
			{
				transform = new TRSRTransformation(new Vector3f(-0.15f, 0.15f, -0.05f), 
												   new Quat4f(0.0f, 0.5f, 0.0f, 1.0f), 
												   new Vector3f(0.5f, 0.5f, 0.5f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				transform = getTweakedLeftHandTransform(transform);
				break;
			}
			case GUI: 
			{ 
				transform = new TRSRTransformation(new Vector3f(0.1f, -0.05f, 0.0f), 
												   new Quat4f(0.0f, -0.42f, 0.0f, 1.0f), 
												   new Vector3f(0.7f, 0.7f, 0.7f), 
												   new Quat4f(0.2f, -0.0f, -0.2f, 1.0f));
				transform = getTweakedGUITransform(transform);
				break; 
			}
			case GROUND:
			{ 
				transform = new TRSRTransformation(new Vector3f(0.15f, 0.0f, 0.0f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
												   new Vector3f(0.45f, 0.45f, 0.45f), 
												   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
				break; 
			}
			case FIXED: //  this is when it is on shelves
			{ 
				transform = new TRSRTransformation(new Vector3f(0.0f, 0.1f, -0.2f), 
												   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
												   new Vector3f(0.7f, 0.7f, 0.7f), 
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
		transform = getTweakedMasterTransform(transform);
		return Pair.of(this, transform.getMatrix());
	}
	
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		return transform;
	}
	
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		return transform;
	}
	
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		return transform;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		getModel(state, true, 0);
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
	
	private void setCustomTextureString(String input)
	{
		this.customTextureLocation = input;
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
			wood = EnumWoodType.getEnum(stack.getItemDamage());
			customTextureLocation = "none";
			if (stack != ItemStack.EMPTY)
			{
				
				NBTTagCompound tags = stack.getTagCompound();
				if (tags != null && tags.hasKey("renderTexture")) 
				{
					customTextureLocation = tags.getString("renderTexture");
				}
			}
			getModel(null, false, 0);
			return wrapper;
		}
	}
}
