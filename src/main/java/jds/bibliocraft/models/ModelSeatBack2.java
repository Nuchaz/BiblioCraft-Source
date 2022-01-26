package jds.bibliocraft.models;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.items.ItemSeatBack2;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSeatBack2 extends ModelSeatBacks
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + ItemSeatBack2.name);
	
	public ModelSeatBack2(IBakedModel model) 
	{
		super(model);
	}

	@Override
	public List<String> getModelParts(List<String> parts) 
	{
		parts.add("backWood2");
		parts.add("backCloth2");
		return parts;
	}

	@Override
	public TRSRTransformation getAdjustedGUITransform(TRSRTransformation transform) 
	{
		transform = new TRSRTransformation(new Vector3f(0.66f, -0.46f, 0.0f), 
										   new Quat4f(0.0f, 1.3f, 0.0f, 1.0f), 
										   new Vector3f(0.9f, 0.9f, 0.9f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		return transform;
	}
}