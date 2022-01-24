package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockDiscRack;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelDiscRack extends BiblioModelSimple
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockDiscRack.name);

	public ModelDiscRack()
	{
		super("bibliocraft:block/discrack.obj");
 	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.02f, 0.88f, 0.02f), 
														   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														   new Vector3f(1.2f, 1.2f, 1.2f), 
														   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}