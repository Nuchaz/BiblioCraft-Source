package jds.bibliocraft.models;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import jds.bibliocraft.blocks.BlockSwordPedestal;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumMetalType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSwordPedestal extends BiblioModelColor
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockSwordPedestal.name);
	
	public ModelSwordPedestal()
	{
		super("bibliocraft:block/swordpedestal.obj");
	}

	@Override
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		return modelParts;
	}

	@Override
	public String getTextureLocation(EnumColor color, EnumMetalType metal, String resourceLocation)
	{
		String output = resourceLocation;
		if (resourceLocation.contains("wool_colored_white"))
		{
			output = color.getWoolTextureString();
		}
		return output;
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.09f, 0.05f, 0.1f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
										   new Vector3f(1.2f, 1.2f, 1.2f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		   
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.22f, 0.02f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
										   new Vector3f(1.1f, 1.1f, 1.1f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
