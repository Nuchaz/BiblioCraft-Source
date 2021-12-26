package jds.bibliocraft.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import jds.bibliocraft.blocks.BiblioWoodBlock;
import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.helpers.EnumWoodsType;
import jds.bibliocraft.helpers.ModelCache;
import jds.bibliocraft.states.TextureProperty;
import jds.bibliocraft.states.TextureState;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ItemOverride;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.client.model.obj.OBJModel;

public abstract class BiblioModelWood implements IDynamicBakedModel// ,ISmartBlockModel, ISmartItemModel, IPerspectiveAwareModel
{
	private IModel model = null;
	private IBakedModel baseModel;
	private String modelLocation = " ";
	public String textureLocation = "none";
	public String customTextureLocation = "none";
	public EnumWoodsType wood = EnumWoodsType.framed;
	//private CustomItemOverrideList overrides = new CustomItemOverrideList();
	public IBakedModel wrapper;
	private ModelCache cache;
	private boolean gotOBJ = false; 
	public EnumVertPosition vertpos = EnumVertPosition.FLOOR;
	public EnumShiftPosition shiftpos = EnumShiftPosition.NO_SHIFT;
	public Direction angle = Direction.NORTH;
	
	private ModelLoader loader;
	
	
	//System.out.println(tileData.getData(BiblioTileEntity.TEXTURE));
	//System.out.println(tileData.getData(BiblioTileEntity.DIRECTION));
	//System.out.println(tileData.getData(BiblioTileEntity.SHIFTPOS));
	//System.out.println(tileData.getData(BiblioTileEntity.VERTPOS));
	
	public BiblioModelWood(ModelBakeEvent event, String modelLoc, boolean isItem)
	{
		this.modelLocation = modelLoc;
		this.wrapper = this;
		this.cache = new ModelCache();
		this.loader = event.getModelLoader();
		getModel(isItem);

	}

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IEnviromentBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
    {
    	System.out.println("biblio testy testy");
    	return tileData;
    }
	
	@Override
	public ItemOverrideList getOverrides() 
	{
		return null;
	}
	
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand, IModelData modelData)
    {
        return baseModel.getQuads(state, side, rand, modelData);//modelData.getData(MAGIC_PROP) ? stone.getQuads(state, side, rand, modelData) : dirt.getQuads(state, side, rand, modelData);
    }
    
    public TRSRTransformation getTransform()
    {
    	TRSRTransformation transform = TRSRTransformation.from(angle);
  
    	
    	switch (shiftpos)
    	{
	    	case NO_SHIFT: break;
	    	case HALF_SHIFT: transform.compose(new TRSRTransformation(new Vector3f(0.25f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f))); break;
	    	case FULL_SHIFT: transform.compose(new TRSRTransformation(new Vector3f(0.5f, 0.0f, 0.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f))); break;
	    	default: break;
    	}
    	/*
    	switch (angle)
    	{
	    	case NORTH: break;
	    	case EAST: break;
	    	case SOUTH: break;
	    	case WEST: break;
	    	default: break;
    	}*/
    	return transform;
    }
    
    @SuppressWarnings("deprecation")
	public void getModel(boolean isItem)
    {
		IUnbakedModel m = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(modelLocation));
        if (m instanceof OBJModel) 
        {
        	//OBJModel modelo = (OBJModel)m;
        	m = m.process(ImmutableMap.of("flip-v", "true"));
        	//omodel.
        	OBJModel.OBJState modelState = new OBJModel.OBJState(getDefaultVisiableModelParts(), true, getTransform());
        	switch (wood)
			{
				case oak: {textureLocation = "minecraft:block/oak_planks"; break;} //minecraft:block/oak_planks
				case spruce: {textureLocation = "minecraft:block/spruce_planks"; break;}
				case birch: {textureLocation = "minecraft:block/birch_planks"; break;}
				case jungle: {textureLocation = "minecraft:block/jungle_planks"; break;}
				case acacia: {textureLocation = "minecraft:block/acacia_planks"; break;}
				case darkoak: {textureLocation = "minecraft:block/big_oak_planks"; break;}
				case framed: 
				{
					//System.out.println(customTextureLocation.length());
					if (customTextureLocation.contains("none") || customTextureLocation.contains("minecraft:white") || customTextureLocation.length() == 0)
					{
						textureLocation = "minecraft:block/oak_planks";//"bibliocraft:blocks/frame"; 
					}
					else
					{
						textureLocation = "minecraft:block/oak_planks";//customTextureLocation;
					}
					break;
				}
				default: {textureLocation = "minecraft:block/oak_planks"; break;}	
			}
        	//IBakedModel bakedModel = model.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), modelState, DefaultVertexFormats.ITEM);
        	//IBakedModel bakedModel = model.bake(modelState,  DefaultVertexFormats.ITEM, textureGetter);
        	baseModel = m.bake(loader, textureGetter, new BasicState(modelState, false), DefaultVertexFormats.ITEM);
        }
    }
	/*
	private void getModel(BlockState state, boolean isBlock, int attempt)
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
	*/
	public void loadAdditionalTextureStateStuff(TextureState state) { }
	
	//public void getAdditionalBlockStateStuff(IExtendedBlockState state){ }
	
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
			return Minecraft.getInstance().getTextureMap().getAtlasSprite(getTextureLocation(location.toString(), textureLocation));
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
			return Minecraft.getInstance().getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak");
		}
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() 
	{
		return ItemCameraTransforms.DEFAULT;
	}
	/*
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
	*/
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

	/*
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand)
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
*//*
	@Override
	public ItemOverrideList getOverrides() 
	{
		return overrides;
	}
	
	private void setCustomTextureString(String input)
	{
		this.customTextureLocation = input;
	}*/
	
	private class CustomItemOverrideList extends ItemOverrideList
	{
		private CustomItemOverrideList()
		{
			//super(ImmutableList.<ItemOverride>of());
		}
		
		@Override
		public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn)
		{
			getModel(true);
			return baseModel;
		}
		
		/*
		@Nonnull
		@Override
		public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entity) 
		{
			wood = EnumWoodType.getEnum(stack.getDamage());
			customTextureLocation = "none";
			if (stack != ItemStack.EMPTY)
			{
				
				CompoundNBT tags = stack.getTag();
				if (tags != null && tags.hasKey("renderTexture")) 
				{
					customTextureLocation = tags.getString("renderTexture");
				}
			}
			getModel(null, false, 0);
			return wrapper;
		}*/
	}
	
}
