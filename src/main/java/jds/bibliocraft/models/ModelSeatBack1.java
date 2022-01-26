package jds.bibliocraft.models;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.items.ItemSeatBack;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSeatBack1 extends ModelSeatBacks
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + ItemSeatBack.name);

	public ModelSeatBack1(IBakedModel model) 
	{
		super(model);
	}

	@Override
	public List<String> getModelParts(List<String> parts) 
	{
		parts.add("backSupport");
		parts.add("backCloth");
		return parts;
	}

	@Override
	public TRSRTransformation getAdjustedGUITransform(TRSRTransformation transform) 
	{
		transform = new TRSRTransformation(new Vector3f(0.8f, -0.5f, 0.0f), 
										   new Quat4f(0.0f, 1.3f, 0.0f, 1.0f), 
										   new Vector3f(1.0f, 1.0f, 1.0f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		return transform;
	}

}
