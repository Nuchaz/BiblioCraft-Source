package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockLampGold;
import jds.bibliocraft.blocks.BlockLampIron;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumMetalType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelLamp extends BiblioModelColor
{
	public static final ModelResourceLocation modelResourceLocationGold = new ModelResourceLocation("bibliocraft:" + BlockLampGold.name);
	public static final ModelResourceLocation modelResourceLocationIron = new ModelResourceLocation("bibliocraft:" + BlockLampIron.name);
	
	public ModelLamp()
	{
		super("bibliocraft:block/lamp.obj");
	}

	@Override
	public List<String> getDefaultVisiableModelParts() 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("baseFloor");
		modelParts.add("lampTopFloor");
		return modelParts;
	}

	@Override
	public String getTextureLocation(EnumColor color, EnumMetalType metal, String resourceLocation) 
	{
		
		String loc = resourceLocation;
		if (loc.contains("lamplight0"))
		{
			switch (color)
			{
				case WHITE:{ loc = "bibliocraft:models/lamplight0"; break; }
				case LIGHT_GRAY:{ loc = "bibliocraft:models/lamplight1"; break; }
				case GRAY:{ loc = "bibliocraft:models/lamplight2"; break; }
				case BLACK:{ loc = "bibliocraft:models/lamplight3"; break; }
				case RED:{ loc = "bibliocraft:models/lamplight4"; break; }
				case ORANGE:{ loc = "bibliocraft:models/lamplight5"; break; }
				case YELLOW:{ loc = "bibliocraft:models/lamplight6"; break; }
				case LIME:{ loc = "bibliocraft:models/lamplight7"; break; }
				case GREEN:{ loc = "bibliocraft:models/lamplight8"; break; }
				case CYAN:{ loc = "bibliocraft:models/lamplight9"; break; }
				case LIGHT_BLUE:{ loc = "bibliocraft:models/lamplight10"; break; }
				case BLUE:{ loc = "bibliocraft:models/lamplight11"; break; }
				case PURPLE:{ loc = "bibliocraft:models/lamplight12"; break; }
				case MAGENTA:{ loc = "bibliocraft:models/lamplight13"; break; }
				case PINK:{ loc = "bibliocraft:models/lamplight14"; break; }
				case BROWN:{ loc = "bibliocraft:models/lamplight15"; break; }
			}
		}
		if (loc.equals("bibliocraft:models/lamp"))
		{
			if (metal != null)
			{
				switch (metal)
				{
				case GOLD: { loc = "bibliocraft:models/lamp"; break; }
				case IRON: { loc = "bibliocraft:models/lamp_iron"; break; }
				}
			}
			else
			{
				loc = "bibliocraft:models/lamp";
			}
		}
		return loc;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.1f, -0.35f, 0.1f), 
														   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														   new Vector3f(1.0f, 1.0f, 1.0f), 
														   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLEFTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.15f, 0.05f, 0.0f), 
															 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															 new Vector3f(1.0f, 1.0f, 1.0f), 
															 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, -0.1f), 
															 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															 new Vector3f(1.0f, 1.0f, 1.0f), 
															 new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
