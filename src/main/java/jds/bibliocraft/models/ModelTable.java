package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockTable;
import jds.bibliocraft.states.TextureState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelTable extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockTable.name);
	private String clothTexture = "minecraft:blocks/wool_colored_white";
	private String carpetTexture = "minecraft:blocks/wool_colored_white";

	public ModelTable()
	{
		super("bibliocraft:block/table.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (resourceLocation.contentEquals("minecraft:blocks/planks_oak"))
		{
			returnValue = textureLocation;
		}
		else if (resourceLocation.contentEquals("minecraft:blocks/wool_colored_red"))
		{
			returnValue = clothTexture;
		}
		else if (resourceLocation.contentEquals("minecraft:blocks/wool_colored_white"))
		{
			returnValue = carpetTexture;
		}
		return returnValue; 
	}
	
	@Override
	public void loadAdditionalTextureStateStuff(TextureState state) 
	{ 
		if (state != null)
		{
			this.clothTexture = state.getColorOne().getWoolTextureString();
			this.carpetTexture = state.getColorTwo().getWoolTextureString();
		}
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.25f, 0.0f, -1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("monoleg");
		modelParts.add("tableBevel01");
		modelParts.add("tableBevel02");
		modelParts.add("tableBevel03");
		modelParts.add("tableBevel04");
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, -0.05f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(2.5f, 0.0f, 0.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															   new Vector3f(1.0f, 1.0f, 1.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
