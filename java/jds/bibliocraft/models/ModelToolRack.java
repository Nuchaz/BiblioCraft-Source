package jds.bibliocraft.models;

import jds.bibliocraft.blocks.BlockToolRack;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ModelToolRack extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockToolRack.name);

	public ModelToolRack()
	{
		super("bibliocraft:block/toolrack.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (!resourceLocation.contentEquals("minecraft:blocks/iron_block"))
		{
			returnValue = textureLocation;
		}
		return returnValue; 
	}
}
