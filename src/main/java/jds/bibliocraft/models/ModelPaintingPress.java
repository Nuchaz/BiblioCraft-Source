package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockPaintingPress;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelPaintingPress extends BiblioModelSimple
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockPaintingPress.name);

	public ModelPaintingPress()
	{
		super("bibliocraft:block/paintpress.obj"); 
 	}
	
	@Override
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("canvas");
		modelParts.add("painting");
		modelParts.add("base");
		modelParts.add("lid_item");
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.0f, 0.0f, -1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.07f, -1.0f, 0.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(0.9f, 0.9f, 0.9f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLEFTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(1.75f, -0.34f, -0.2f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(0.8f, 0.8f, 0.8f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedRIGHTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.75f, -0.4f, -1.75f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(0.8f, 0.8f, 0.8f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
}