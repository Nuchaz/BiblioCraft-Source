package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockCookieJar;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelCookieJar extends BiblioModelSimple
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockCookieJar.name);

	public ModelCookieJar()
	{
		super("bibliocraft:block/cookiejar.obj");
 	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.55f, 0.02f), 
														   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														   new Vector3f(1.1f, 1.1f, 1.1f), 
														   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}