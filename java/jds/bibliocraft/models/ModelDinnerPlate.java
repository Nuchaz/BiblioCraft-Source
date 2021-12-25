package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockDinnerPlate;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelDinnerPlate extends BiblioModelSimple
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockDinnerPlate.name);

	public ModelDinnerPlate()
	{
		super("bibliocraft:block/dinnerplate.obj");
 	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.06f, 1.5f, 0.0f), 
														   new Quat4f(-0.0f, 0.0f, 0.0f, 1.0f), 
														   new Vector3f(1.65f, 1.65f, 1.65f), 
														   new Quat4f(-0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}