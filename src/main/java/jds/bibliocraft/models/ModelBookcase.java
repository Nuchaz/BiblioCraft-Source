package jds.bibliocraft.models;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.blocks.BlockBookcaseCreative;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelBookcase extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockBookcase.name);
	public static final ModelResourceLocation modelResourceLocationFilledBookcase = new ModelResourceLocation("bibliocraft:" + BlockBookcaseCreative.name);

	public ModelBookcase()
	{
		super("bibliocraft:block/bookcase.obj");
		this.wrapper = this;
	}
	
	@Override
	public String getTextureLocation(String resourceLocation, String textureLocation) 
	{
		String returnValue = resourceLocation;
		if (!returnValue.contentEquals("bibliocraft:models/bookcase_books"))
		{
			returnValue = textureLocation;
		}
		return returnValue;
	}
}
