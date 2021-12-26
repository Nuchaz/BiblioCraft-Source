package jds.bibliocraft.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.blocks.BlockBookcaseCreative;
import jds.bibliocraft.helpers.EnumWoodsType;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
//import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelBookcase extends BiblioModelWood
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("bibliocraft:" + BlockBookcase.name + EnumWoodsType.oak.getName());
	public static final ModelResourceLocation modelResourceLocationFilledBookcase = new ModelResourceLocation("bibliocraft:" + BlockBookcaseCreative.name);

	private int[] books = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 
	
	public ModelBookcase(ModelBakeEvent event, EnumWoodsType wood, boolean isItem)
	{
		super(event, "bibliocraft:block/bookcase.obj", isItem);
		this.wrapper = this;
		this.wood = wood;
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

	/*
	 * 
	 *     @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IEnviromentBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
    {
        return originalModel.getModelData(world, pos, state, tileData);
    }
	 */
    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IEnviromentBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
    {
    	System.out.println("biblio getting model data");
    	// TODO I should probly do this in the parent class and just have an "extra" model data position.
    	//books = tileData.getData(TileEntityBookcase.BOOKS);
    	this.customTextureLocation = tileData.getData(BiblioTileEntity.TEXTURE);
    	this.angle = tileData.getData(BiblioTileEntity.DIRECTION);
    	this.shiftpos = tileData.getData(BiblioTileEntity.SHIFTPOS);
    	this.vertpos = tileData.getData(BiblioTileEntity.VERTPOS);
    	
    	if (tileData.getData(TileEntityBookcase.BOOK1) != null)
        	books[0] = tileData.getData(TileEntityBookcase.BOOK1) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK2) != null)
    		books[1] = tileData.getData(TileEntityBookcase.BOOK2) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK3) != null)
    		books[2] = tileData.getData(TileEntityBookcase.BOOK3) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK4) != null)
    		books[3] = tileData.getData(TileEntityBookcase.BOOK4) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK5) != null)
    		books[4] = tileData.getData(TileEntityBookcase.BOOK5) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK6) != null)
    		books[5] = tileData.getData(TileEntityBookcase.BOOK6) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK7) != null)
    		books[6] = tileData.getData(TileEntityBookcase.BOOK7) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK8) != null)
    		books[7] = tileData.getData(TileEntityBookcase.BOOK8) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK9) != null)
    		books[8] = tileData.getData(TileEntityBookcase.BOOK9) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK10) != null)
    		books[9] = tileData.getData(TileEntityBookcase.BOOK10) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK11) != null)
    		books[10] = tileData.getData(TileEntityBookcase.BOOK11) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK12) != null)
    		books[11] = tileData.getData(TileEntityBookcase.BOOK12) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK13) != null)
    		books[12] = tileData.getData(TileEntityBookcase.BOOK13) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK14) != null)
    		books[13] = tileData.getData(TileEntityBookcase.BOOK14) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK15) != null)
    		books[14] = tileData.getData(TileEntityBookcase.BOOK15) ? 1 : 0;
    	if (tileData.getData(TileEntityBookcase.BOOK16) != null)
    		books[15] = tileData.getData(TileEntityBookcase.BOOK16) ? 1 : 0;
    	
    	//System.out.println(tileData.getData(TileEntityBookcase.BOOK1) + "" +  books[0]);
    	//this.wood = tileData.getData(BiblioTileEntity.WOOD);
    	//System.out.println(tileData.getData(BiblioTileEntity.TEXTURE));
    	//System.out.println(tileData.getData(BiblioTileEntity.DIRECTION));
    	//System.out.println(tileData.getData(BiblioTileEntity.SHIFTPOS));
    	//System.out.println(tileData.getData(BiblioTileEntity.VERTPOS));
    	//System.out.println(books.toString());
    	//System.out.println("aw yaeh 2");
    	
    	System.out.println(books.toString());
    	
    	
    	getModel(false);
    	return tileData;
    }
    
	public List<String> getDefaultVisiableModelParts()
	{
		List<String> modelParts = new ArrayList<String>();
    	modelParts.add("bookcase");
    	//System.out.println(books.length);
    	if (books != null)
    	for (int i = 0; i < books.length; i++)
    	{
    		if (books[i] == 1)
    		{
    			modelParts.add("book" + (i+1));
    		}
    	}
    	
		return modelParts;
	}

}
