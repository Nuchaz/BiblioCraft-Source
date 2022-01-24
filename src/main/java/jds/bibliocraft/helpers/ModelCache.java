package jds.bibliocraft.helpers;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.model.IBakedModel;

public class ModelCache 
{
	private ArrayList<ModelCachePackage> models;
	private IBakedModel currentMatch;
	
	public ModelCache()
	{
		models = new ArrayList<ModelCachePackage>();
		currentMatch = null;
	}
	
	public void addToCache(IBakedModel model, String name)
	{
		ModelCachePackage pack = new ModelCachePackage(model, name);
		models.add(pack);
	}
	
	public boolean hasModel(String name)
	{
		boolean output = false;
		for (int i = 0; i < models.size(); i++)
		{
			ModelCachePackage pack = models.get(i);
			if (pack.getTextureName().contentEquals(name))
			{
				output = true;
				currentMatch = pack.getModel();
				break;
			}
		}
		return output;
	}
	
	public IBakedModel getCurrentMatch()
	{
		return this.currentMatch;
	}
	
	
	
}
