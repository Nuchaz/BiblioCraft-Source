package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockLabel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelLabel extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockLabel.name);

	public ModelLabel()
	{
		super("bibliocraft:block/label.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		return textureLocation; 
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.75f, 0.0f, -1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.18f, 0.47f, -0.18f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(2.5f, 2.5f, 2.5f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(1.5f, 0.0f, 0.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															   new Vector3f(1.0f, 1.0f, 1.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
