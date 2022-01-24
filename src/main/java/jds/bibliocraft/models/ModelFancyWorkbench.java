package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockFancyWorkbench;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelFancyWorkbench extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockFancyWorkbench.name);

	public ModelFancyWorkbench()
	{
		super("bibliocraft:block/fancyworkbench.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (returnValue.contentEquals("minecraft:blocks/planks_oak"))
		{
			returnValue = textureLocation;
		}
		return returnValue;
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.24f, 0.0f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(0.9f, 0.9f, 0.9f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.5f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
