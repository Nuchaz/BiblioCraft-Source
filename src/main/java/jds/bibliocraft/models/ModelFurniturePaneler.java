package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockFurniturePaneler;
import jds.bibliocraft.states.PanelProperty;
import jds.bibliocraft.states.PanelState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelFurniturePaneler extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockFurniturePaneler.name);
	
	private String panelTextureString = "none";
	
	public ModelFurniturePaneler()
	{
		super("bibliocraft:block/paneler.obj");
	}

	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (resourceLocation.contentEquals("minecraft:blocks/planks_oak"))
		{
			returnValue = textureLocation;
		}
		//System.out.println(resourceLocation);
		if (resourceLocation.contentEquals("minecraft:models/typewriter_paper_blank"))
		{
			//System.out.println(panelTextureString);
			if (panelTextureString == "none")
			{
				returnValue = "minecraft:blocks/planks_oak";
			}
			else
			{
				returnValue = panelTextureString;
			}
		}
		
		return returnValue;
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.28f, 0.0f, -1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.04f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
				   new Vector3f(0.9f, 0.9f, 0.9f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(2.6f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("wood");
		modelParts.add("paneler");
		return modelParts;
	}
	
	@Override
	public void getAdditionalBlockStateStuff(IExtendedBlockState state)
	{
		panelTextureString = "none";
		if (state != null && state.getUnlistedNames() != null && state.getUnlistedNames().contains(PanelProperty.instance)) // TODO there was a crash here, this may fixe it.
		{
			PanelState texString = (PanelState)state.getValue(PanelProperty.instance);
			if (texString != null && texString.getTextureString() != null)
				panelTextureString = texString.getTextureString();
		}
	}
}
