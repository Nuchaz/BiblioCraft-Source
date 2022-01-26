package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockPaintingFrameBorderless;
import jds.bibliocraft.blocks.BlockPaintingFrameFancy;
import jds.bibliocraft.blocks.BlockPaintingFrameFlat;
import jds.bibliocraft.blocks.BlockPaintingFrameMiddle;
import jds.bibliocraft.blocks.BlockPaintingFrameSimple;
import jds.bibliocraft.helpers.EnumPaintingFrame;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelPainting extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocationBorderless = new ModelResourceLocation("bibliocraft:" + BlockPaintingFrameBorderless.name);
	public static final ModelResourceLocation modelResourceLocationSimple = new ModelResourceLocation("bibliocraft:" + BlockPaintingFrameSimple.name);
	public static final ModelResourceLocation modelResourceLocationFlat = new ModelResourceLocation("bibliocraft:" + BlockPaintingFrameFlat.name);
	public static final ModelResourceLocation modelResourceLocationMiddle = new ModelResourceLocation("bibliocraft:" + BlockPaintingFrameMiddle.name);
	public static final ModelResourceLocation modelResourceLocationFancy = new ModelResourceLocation("bibliocraft:" + BlockPaintingFrameFancy.name);
	private EnumPaintingFrame frame;

	public ModelPainting(EnumPaintingFrame frameType)
	{
		super("bibliocraft:block/paintingframe.obj");
		this.frame = frameType;
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (resourceLocation.contentEquals("minecraft:blocks/planks_oak"))
		{
			returnValue = textureLocation;
		}
		return returnValue; 
	}
	
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("back");
		modelParts.add("largeCanvas");
		switch (this.frame)
		{
			case BORDERLESS:
			{
				modelParts.add("largeCanvas");
				
				break;
			}
			case FLAT:
			{
				modelParts.add("flatT45");
				modelParts.add("flatR45");
				modelParts.add("flatB45");
				modelParts.add("flatL45");
				modelParts.add("canvas");
				break;
			}
			case SIMPLE:
			{
				modelParts.add("simpleT45");
				modelParts.add("simpleR45");
				modelParts.add("simpleB45");
				modelParts.add("simpleL45");
				modelParts.add("canvas");
				break;
			}
			case MIDDLE:
			{
				modelParts.add("middleT45");
				modelParts.add("middleR45");
				modelParts.add("middleB45");
				modelParts.add("middleL45");
				modelParts.add("canvas");
				break;
			}
			case FANCY:
			{
				modelParts.add("fancyT45");
				modelParts.add("fancyR45");
				modelParts.add("fancyB45");
				modelParts.add("fancyL45");
				modelParts.add("canvas");
				break;
			}
		}
		
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.1f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.3f, 0.0f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.1f, 1.1f, 1.1f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
