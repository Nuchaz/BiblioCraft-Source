package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockDesk;
import jds.bibliocraft.states.TextureState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelDesk extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockDesk.name);
	private String carpetString = "minecraft:blocks/wool_colored_white";

	public ModelDesk()
	{
		super("bibliocraft:block/desk.obj");
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (returnValue.contentEquals("minecraft:blocks/planks_oak"))
		{
			returnValue = textureLocation;
		}
		if (returnValue.contentEquals("minecraft:blocks/wool_colored_white"))
		{
			returnValue = carpetString;
		}
		return returnValue;
	}
	
	@Override
	public void loadAdditionalTextureStateStuff(TextureState state) 
	{ 
		if (state != null)
		{
			this.carpetString = state.getColorOne().getWoolTextureString();
		}
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.25f, 0.0f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("candle");
		modelParts.add("pen");
		modelParts.add("deskTopRight");
		modelParts.add("deskTopLeft");
		modelParts.add("legLeft");
		modelParts.add("legRight");
		modelParts.add("shelfSingle");
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, -0.02f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(0.9f, 0.9f, 0.9f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.5f, 0.0f, 0.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															   new Vector3f(1.0f, 1.0f, 1.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
