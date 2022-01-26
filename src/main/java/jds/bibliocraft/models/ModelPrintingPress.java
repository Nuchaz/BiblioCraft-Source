package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.blocks.BiblioLightBlock;
import jds.bibliocraft.blocks.BlockPrintingPress;
import jds.bibliocraft.helpers.EnumColor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelPrintingPress extends BiblioModelSimple
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockPrintingPress.name);
	private int ink = 0;
	
	public ModelPrintingPress()
	{
		super("bibliocraft:block/printpress.obj");
 	}
	
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> parts = new ArrayList<String>();
		parts.add("press");
		parts.add("arm_item");
		parts.add("bed_item");
		parts.add("book1");
		parts.add("book2");
		parts.add("bookBlue");
		return parts;
	}
	
	@Override
	public String getTextureLocation(String resourceLocation)
	{
		String output = resourceLocation;
		if (resourceLocation.contains("bibliocraft:models/inkplate0"))
		{
			output = "bibliocraft:models/inkplate" + ink;
		}
		return output;
	}
	
	@Override
	public void getAdditionalModelStateStuff(IExtendedBlockState state)
	{
		EnumColor variable = (EnumColor)state.getValue(BiblioLightBlock.COLOR);
		ink = variable.getID();
	}
	
	@Override
	public TRSRTransformation getTweakedMasterTransfer(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.0f, -0.06f, -1.0f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(0.85f, 0.85f, 0.85f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedGUITransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.07f, -1.14f, 0.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(1.0f, 1.0f, 1.0f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedLEFTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(1.75f, -0.4f, -0.2f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
														     new Vector3f(0.8f, 0.8f, 0.8f), 
														     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public TRSRTransformation getTweakedRIGHTHANDTransform(TRSRTransformation transform)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-1.75f, -0.4f, -1.75f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
														     new Vector3f(0.8f, 0.8f, 0.8f), 
														     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
}