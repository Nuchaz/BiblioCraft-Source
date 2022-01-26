package jds.bibliocraft.models;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.items.ItemSeatBack5;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSeatBack5 extends ModelSeatBacks
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + ItemSeatBack5.name);
	
	public ModelSeatBack5(IBakedModel model) 
	{
		super(model);
	}

	@Override
	public List<String> getModelParts(List<String> parts) 
	{
		parts.add("backCloth2");
		parts.add("backWood2");
		parts.add("fancyBackWood");
		return parts;
	}

	@Override
	public TRSRTransformation getAdjustedGUITransform(TRSRTransformation transform) 
	{
		transform = new TRSRTransformation(new Vector3f(0.55f, -0.5f, 0.0f), 
				   new Quat4f(0.0f, 1.3f, 0.0f, 1.0f), 
				   new Vector3f(0.75f, 0.75f, 0.75f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		return transform;
	}
}