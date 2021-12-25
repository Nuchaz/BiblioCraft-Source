package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockTypesettingTable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelTypesettingTable extends BiblioModelSimple
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockTypesettingTable.name);

	public ModelTypesettingTable()
	{
		super("bibliocraft:block/typesettingtable.obj"); 
 	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.0f, 0.0f, -1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(0.85f, 0.85f, 0.85f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.07f, -1.14f, 0.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLEFTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(1.75f, -0.4f, -0.2f), 
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