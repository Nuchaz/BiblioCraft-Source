package jds.bibliocraft.models;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.blocks.BlockBookcaseCreative;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ModelBookcase extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockBookcase.name);
	public static final ModelResourceLocation modelResourceLocationFilledBookcase = new ModelResourceLocation("bibliocraft:" + BlockBookcaseCreative.name);

	public ModelBookcase()
	{
		super("bibliocraft:block/bookcase.obj");
		this.wrapper = this;
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (!returnValue.contentEquals("bibliocraft:models/bookcase_books"))
		{
			returnValue = textureLocation;
		}
		return returnValue;
	}
}
