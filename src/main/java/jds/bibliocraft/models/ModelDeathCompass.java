package jds.bibliocraft.models;

import jds.bibliocraft.items.ItemDeathCompass;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ModelDeathCompass extends ModelCompass
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + ItemDeathCompass.name);
	
	public ModelDeathCompass(IBakedModel model)
	{
		super(model);
	}

	@Override
	public String getTextureString()
	{
		return "bibliocraft:models/deathcompass";
	}
}
