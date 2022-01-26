package jds.bibliocraft.models;

import jds.bibliocraft.blocks.BlockShelf;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ModelShelf extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockShelf.name);
	
	public ModelShelf()
	{
		super("bibliocraft:block/shelf.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		return textureLocation;
	}
}
