package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BlockLanternGold;
import jds.bibliocraft.blocks.BlockLanternIron;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumMetalType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelLantern extends BiblioModelColor
{
	public static final ModelResourceLocation modelResourceLocationGold = new ModelResourceLocation("bibliocraft:" + BlockLanternGold.name);
	public static final ModelResourceLocation modelResourceLocationIron = new ModelResourceLocation("bibliocraft:" + BlockLanternIron.name);
	
	public ModelLantern()
	{
		super("bibliocraft:block/lantern.obj");
	}

	@Override
	public List<String> getDefaultVisiableModelParts() 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("lamp");
		modelParts.add("candle");
		modelParts.add("glass");
		return modelParts;
	}

	@Override
	public String getTextureLocation(EnumColor color, EnumMetalType metal, String resourceLocation) 
	{
		String loc = resourceLocation;
		if (loc.contains("lanterncandle0"))
		{
			switch (color)
			{
				case WHITE:{ loc = "bibliocraft:models/lanterncandle0"; break; }
				case LIGHT_GRAY:{ loc = "bibliocraft:models/lanterncandle1"; break; }
				case GRAY:{ loc = "bibliocraft:models/lanterncandle2"; break; }
				case BLACK:{ loc = "bibliocraft:models/lanterncandle3"; break; }
				case RED:{ loc = "bibliocraft:models/lanterncandle4"; break; }
				case ORANGE:{ loc = "bibliocraft:models/lanterncandle5"; break; }
				case YELLOW:{ loc = "bibliocraft:models/lanterncandle6"; break; }
				case LIME:{ loc = "bibliocraft:models/lanterncandle7"; break; }
				case GREEN:{ loc = "bibliocraft:models/lanterncandle8"; break; }
				case CYAN:{ loc = "bibliocraft:models/lanterncandle9"; break; }
				case LIGHT_BLUE:{ loc = "bibliocraft:models/lanterncandle10"; break; }
				case BLUE:{ loc = "bibliocraft:models/lanterncandle11"; break; }
				case PURPLE:{ loc = "bibliocraft:models/lanterncandle12"; break; }
				case MAGENTA:{ loc = "bibliocraft:models/lanterncandle13"; break; }
				case PINK:{ loc = "bibliocraft:models/lanterncandle14"; break; }
				case BROWN:{ loc = "bibliocraft:models/lanterncandle15"; break; }
			}
		}
		if (loc.equals("bibliocraft:models/lantern"))
		{
			if (metal != null)
			{
				switch (metal)
				{
				case GOLD: { loc = "bibliocraft:models/lantern"; break; }
				case IRON: { loc = "bibliocraft:models/lantern_iron"; break; }
				}
			}
			else
			{
				loc = "bibliocraft:models/lantern";
			}
		}
		return loc;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.25f, 0.25f), 
														   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														   new Vector3f(1.5f, 1.5f, 1.5f), 
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
