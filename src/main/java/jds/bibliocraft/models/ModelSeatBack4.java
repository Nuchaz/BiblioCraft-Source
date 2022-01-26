package jds.bibliocraft.models;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.items.ItemSeatBack4;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSeatBack4 extends ModelSeatBacks
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + ItemSeatBack4.name);
	
	public ModelSeatBack4(IBakedModel model) 
	{
		super(model);
	}

	@Override
	public List<String> getModelParts(List<String> parts) 
	{
		parts.add("backCloth003");
		parts.add("backsupport2");
		return parts;
	}

	@Override
	public TRSRTransformation getAdjustedGUITransform(TRSRTransformation transform) 
	{
		transform = new TRSRTransformation(new Vector3f(1.1f, -0.45f, 0.0f), 
										   new Quat4f(0.0f, 1.3f, 0.0f, 1.0f), 
										   new Vector3f(1.4f, 1.4f, 1.4f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		return transform;
	}
}