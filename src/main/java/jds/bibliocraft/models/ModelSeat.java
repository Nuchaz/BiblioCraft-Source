package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.blocks.BlockSeat;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.states.TextureState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSeat extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockSeat.name);

	private EnumWoodType backWood = EnumWoodType.OAK;
	private String customBackTexture = "none";
	private EnumColor seatColor = EnumColor.WHITE;
	private EnumColor carpetColor = EnumColor.WHITE;
	private boolean hasBack = false;
	private boolean isItem = false;
	private boolean isFramedBlock = false;
	
	public ModelSeat()
	{
		super("bibliocraft:block/seat.obj");
	}

	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (resourceLocation.contentEquals("minecraft:blocks/planks_oak"))
		{
			returnValue = textureLocation;
		}
		else if (resourceLocation.contentEquals("minecraft:blocks/planks_spruce"))
		{
			// this is the back wood. We need to add a state to carry this over.
			if (!this.hasBack)
			{
				returnValue = textureLocation;
			}
			else
			{
				returnValue = backWood.getTextureString();
				boolean hasNoCustomTexture = customBackTexture.contains("none") || customBackTexture.contains("minecraft:white");
				if (!hasNoCustomTexture && isFramedBlock)
				{
					returnValue = customBackTexture;
				}
			}
		}
		else if (resourceLocation.contentEquals("minecraft:blocks/wool_colored_red"))
		{
			returnValue = carpetColor.getWoolTextureString();
		}
		else if (resourceLocation.contentEquals("minecraft:blocks/wool_colored_white"))
		{
			if (this.isItem)
			{
				returnValue = resourceLocation;
			}
			else
			{
				returnValue = seatColor.getWoolTextureString();
			}
		}
		return returnValue; 
	}
	
	public void loadAdditionalTextureStateStuff(TextureState state) 
	{ 
		if (state != null)
		{
			this.backWood = state.getAdditionalWood();
			this.customBackTexture = state.getAdditionalTexture();
			this.seatColor = state.getColorOne();
			this.carpetColor = state.getColorTwo();
			this.hasBack = state.getFlag();
			this.isFramedBlock = state.getFlag2();
			this.isItem = false;
		}
		else
		{
			this.isItem = true;
			this.hasBack = false;
		}
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
	
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("stool");
		modelParts.add("seat");
		modelParts.add("leg1");
		modelParts.add("leg2");
		modelParts.add("leg3");
		modelParts.add("leg4");
		modelParts.add("brace1");
		modelParts.add("brace2");
		modelParts.add("brace3");
		modelParts.add("brace4");
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(1.2f, 1.2f, 1.2f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLeftHandTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(2.55f, 0.0f, 0.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
															   new Vector3f(1.0f, 1.0f, 1.0f), 
															   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
}
