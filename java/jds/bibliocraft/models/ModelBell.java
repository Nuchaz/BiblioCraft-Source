package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockBell;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelBell extends BiblioModelSimple
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockBell.name);

	public ModelBell()
	{
		super("bibliocraft:block/bell.obj"); 
 	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.0f, 0.5f, -1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(2.0f, 2.0f, 2.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 1.85f, 0.09f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(2.0f, 2.0f, 2.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLEFTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.0f, -0.0f, -2.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
}
