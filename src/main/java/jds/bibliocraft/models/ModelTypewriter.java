package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockTypeWriter;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumMetalType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelTypewriter extends BiblioModelColor
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockTypeWriter.name);
	
	public ModelTypewriter()
	{
		super("bibliocraft:block/typewriter.obj");
	}

	@Override
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("base");
		modelParts.add("slide");
		modelParts.add("paperLine1");
		return modelParts;
	}

	@Override
	public String getTextureLocation(EnumColor color, EnumMetalType metal, String resourceLocation)
	{
		String output = resourceLocation;
		if (resourceLocation.contains("bibliocraft:models/typewriter0"))
		{
			switch (color)
			{
				case WHITE: { output = "bibliocraft:models/typewriter0"; break; }
				case LIGHT_GRAY: { output = "bibliocraft:models/typewriter1"; break; }
				case GRAY: { output = "bibliocraft:models/typewriter2"; break; }
				case BLACK: { output = "bibliocraft:models/typewriter3"; break; }
				case RED: { output = "bibliocraft:models/typewriter4"; break; }
				case ORANGE: { output = "bibliocraft:models/typewriter5"; break; }
				case YELLOW: { output = "bibliocraft:models/typewriter6"; break; }
				case LIME: { output = "bibliocraft:models/typewriter7"; break; }
				case GREEN: { output = "bibliocraft:models/typewriter8"; break; }
				case CYAN: { output = "bibliocraft:models/typewriter9"; break; }
				case LIGHT_BLUE: { output = "bibliocraft:models/typewriter10"; break; }
				case BLUE: { output = "bibliocraft:models/typewriter11"; break; }
				case PURPLE: { output = "bibliocraft:models/typewriter12"; break; }
				case MAGENTA: { output = "bibliocraft:models/typewriter13"; break; }
				case PINK: { output = "bibliocraft:models/typewriter14"; break; }
				case BROWN: { output = "bibliocraft:models/typewriter15"; break; }
			}
		}
		return output;
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.5f, 0.2f, 0.38f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
										   new Vector3f(1.5f, 1.5f, 1.5f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		   
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLEFTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.75f, 0.0f, 0.05f), 
															 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															 new Vector3f(1.0f, 1.0f, 1.0f), 
															 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.12f, 0.03f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
										   new Vector3f(1.1f, 1.1f, 1.1f), 
										   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
