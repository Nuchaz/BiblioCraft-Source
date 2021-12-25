package jds.bibliocraft.helpers;

import net.minecraft.client.renderer.block.model.IBakedModel;

public class ModelCachePackage 
{
	private IBakedModel model;
	private String name;
	
	public ModelCachePackage(IBakedModel modelIn, String nameIn)
	{
		this.model = modelIn;
		this.name = nameIn;
	}
	
	public IBakedModel getModel()
	{
		return this.model;
	}
	
	public String getTextureName()
	{
		return this.name;
	}
}
