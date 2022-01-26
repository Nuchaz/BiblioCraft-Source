package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityShelf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;


public class BlockShelf extends BiblioWoodBlock 
{
	public static final String name = "Shelf";
	public static final BlockShelf instance = new BlockShelf();
	
	public BlockShelf() 
	{
		super(name, true);
	}

	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			BiblioTileEntity biblioTile = (BiblioTileEntity)world.getTileEntity(pos);
			if (biblioTile instanceof TileEntityShelf)
			{
				TileEntityShelf tile = (TileEntityShelf)biblioTile;
				
				 if (player.isSneaking())
				 {
					 player.openGui(BiblioCraft.instance, 3, world, pos.getX(), pos.getY(), pos.getZ());
					 return true;
				 }
			
				 int slot = getSlotNumberFromClickon2x2block(tile.getAngle(), hitX, hitY, hitZ);
				 boolean testValue = false; // turns true if I add or remove something from the shelf
				 if (slot >= 0 && !player.isSneaking())
				 {
					 if (playerhand != ItemStack.EMPTY)
					 {
						 testValue = tile.addStackToInventoryFromWorld(playerhand, slot, player);
					 }
					 
					 if (!testValue)
					 {
						 testValue = tile.removeStackFromInventoryFromWorld(slot, player, this);
					 }
				 }
				 
				 if (!testValue)
				 {
					 player.openGui(BiblioCraft.instance, 3, world, pos.getX(), pos.getY(), pos.getZ());
				 }
				 return true;
				 
			}
		}
		return true;
	}
	

	@Override
    public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player)
    {

    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityShelf(); 
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		if (tile instanceof TileEntityShelf)
		{
			TileEntityShelf shelf = (TileEntityShelf)tile;
	    	boolean hasTop = shelf.getTop();
	    	if (!hasTop)
	    	{
	    		modelParts = new ArrayList<String>();
	    		modelParts.add("shelf_bottom");
	    	}
		}
		return modelParts;
	}
	
	private boolean checkIfIsBackOfBlock(int angle, int face)
	{
		boolean angle1 = angle == 0 && face == 5;
		boolean angle2 = angle == 3 && face == 2;
		boolean angle3 = angle == 2 && face == 4;
		boolean angle4 = angle == 1 && face == 3;
		if (angle1 || angle2 || angle3 || angle4)
		{
			return true;
		}
		return false;
	}
	 
	@Override	
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityShelf)
		{
			TileEntityShelf shelf = (TileEntityShelf)tile;
			BlockPos upperBlockPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
		    Block testBlock = world.getBlockState(upperBlockPos).getBlock();
		    boolean isBlock;
		    if (shelf != null)
		    {
		        if (!world.isAirBlock(upperBlockPos))
		        {
		        	isBlock = true;
		        }
		        else
		        {
		        	isBlock = false;
		        }
		    	shelf.setTop(isBlock);
		    }
		} 
	}
	
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		return transform;
	}
}