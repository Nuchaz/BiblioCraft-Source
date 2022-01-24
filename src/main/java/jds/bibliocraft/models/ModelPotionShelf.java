package jds.bibliocraft.models;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockPotionShelf;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelPotionShelf extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockPotionShelf.name);

	public ModelPotionShelf()
	{
		super("bibliocraft:block/potionshelf.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		return textureLocation; 
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.0f, 0.0f, -1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(2.0f, 0.0f, 0.0f), 
									   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
									   new Vector3f(1.0f, 1.0f, 1.0f), 
									   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
