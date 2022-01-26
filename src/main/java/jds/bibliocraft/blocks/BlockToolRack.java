package jds.bibliocraft.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.containers.ContainerWeaponRack;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityToolRack;
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

public class BlockToolRack extends BiblioWoodBlock
{
	public static final String name = "ToolRack";
	public static final BlockToolRack instance = new BlockToolRack();
	
	public BlockToolRack()
	{
		super(name, true);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			BiblioTileEntity tile = (BiblioTileEntity)world.getTileEntity(pos);
			if (isFrontOfBlock(side, tile.getAngle()) && !player.isSneaking())
			{
				 int slot = getSlotNumberFromClickon2x2block(tile.getAngle(), hitX, hitY, hitZ);
				 boolean testValue = false; // turns true if I add or remove something from the shelf
				 if (slot >= 0)
				 {
					 if (player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY && ContainerWeaponRack.isItemTool(player.getHeldItem(EnumHand.MAIN_HAND).getItem(), player.getHeldItem(EnumHand.MAIN_HAND)))
					 {
						 testValue = tile.addStackToInventoryFromWorld(player.getHeldItem(EnumHand.MAIN_HAND), slot, player);
					 }
					 
					 if (!testValue)
					 {
						 testValue = tile.removeStackFromInventoryFromWorld(slot, player, this);
					 }
				 }
			}
			else
			{
				player.openGui(BiblioCraft.instance, 4, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityToolRack();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		
		
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		return transform;
	}
}
