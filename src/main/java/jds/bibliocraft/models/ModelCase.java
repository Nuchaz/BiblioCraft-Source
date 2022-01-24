package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockCase;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.states.TextureState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelCase extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockCase.name);
	
	private String innerColor = "minecraft:blocks/wool_colored_white";

	public ModelCase()
	{
		super("bibliocraft:block/case.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (resourceLocation.contentEquals("minecraft:blocks/planks_oak"))
		{
			returnValue = textureLocation;
		}
		if (resourceLocation.contentEquals("minecraft:blocks/wool_colored_white"))
		{
			returnValue = innerColor;
		}
		return returnValue; 
	}
	
	@Override
	public void loadAdditionalTextureStateStuff(TextureState state) 
	{ 
		if (state != null)
		{
			
			this.innerColor = state.getColorOne().getWoolTextureString();
		}
		else
		{
			
			this.innerColor = EnumColor.WHITE.getWoolTextureString();
		}
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		// if vert position shows, move to floor. Wall is default
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.22f, 0.0f, 0.0f), 
										     				 new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
										     				 new Vector3f(1.0f, 1.0f, 1.0f), 
					     									 new Quat4f(0.0f, 0.0f, 1.0f, 1.0f)));
		return transform;
	}
	
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("case_inside");
		modelParts.add("case_bottom");
		modelParts.add("case_lid_glass_item");
		modelParts.add("case_lid_latch_item");
		modelParts.add("case_lid_wood_item");
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.22f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 0.0f, 1.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 0.0f, 1.0f, 1.0f)));
		return transform;
	}
}
