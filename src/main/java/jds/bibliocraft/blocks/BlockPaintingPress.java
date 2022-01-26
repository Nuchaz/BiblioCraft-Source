package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.items.ItemPaintingCanvas;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPaintingPress extends BiblioSimpleBlock
{
	public static final BlockPaintingPress instance = new BlockPaintingPress();
	public static final String name = "PaintingPress";
	
	public BlockPaintingPress()
	{
		super(Material.IRON, SoundType.ANVIL, name);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote && tile != null && tile instanceof TileEntityPaintPress)
		{
			TileEntityPaintPress paintPress = (TileEntityPaintPress)tile;
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			if (playerhand != ItemStack.EMPTY && playerhand.getItem() instanceof ItemPaintingCanvas)
			{
				int canvasAddReturn = paintPress.addCanvas(playerhand);
				if (canvasAddReturn == 0)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
					return true;
				}
				else if (canvasAddReturn > 0)
				{
					playerhand.setCount(canvasAddReturn);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, playerhand);
					return true;
				}
			}
			
			if (player.isSneaking())
			{
				if (paintPress.getStackInSlot(0) != ItemStack.EMPTY)
				{
					paintPress.removeStackFromInventoryFromWorld(0, player, this);
					//dropPainting(world, i, j, k);
					//paintPress.addCanvas(null);
					return true;
				}
			}
			
			if (isFrontHandleSpot(paintPress.getAngle(), side, hitX, hitY, hitZ))
			{
				if (paintPress.getStackInSlot(0) != ItemStack.EMPTY)
				{
					paintPress.setCycle(true);
					return true;
				
				}
			}
			player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());		
			return true;
			
		}
		return true;
	}
	
	public boolean isFrontHandleSpot(EnumFacing angle, EnumFacing face, float hitX, float hitY, float hitZ)
	{
		//System.out.println("angle  "+angle+"    face"+face+"    hitX  "+hitX+"   hitY  "+hitY+"    HitZ  "+hitZ);
		
		switch (angle)
		{
			case SOUTH:
			{
				if ((face == EnumFacing.WEST && hitY > 0.75)||(face == EnumFacing.UP && hitX < 0.16f))
				{
					return true;
				}
				break;
			}
			case WEST:
			{
				if ((face == EnumFacing.NORTH && hitY > 0.75)||(face == EnumFacing.UP && hitZ < 0.16f))
				{
					return true;
				}
				break;
			}
			case NORTH:
			{
				if ((face == EnumFacing.EAST && hitY > 0.75)||(face == EnumFacing.UP && hitX > 0.84f))
				{
					return true;
				}
				break;
			}
			case EAST:
			{
				if ((face == EnumFacing.SOUTH && hitY > 0.75)||(face == EnumFacing.UP && hitZ > 0.84f))
				{
					return true;
				}
				break;
			}
			default: break;
		}
		
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityPaintPress();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile)
	{
		List<String> modelParts = new ArrayList<String>();
		
		//modelParts.add("painting");
		modelParts.add("base");
		if (tile.getStackInSlot(0) != ItemStack.EMPTY)
				modelParts.add("canvas");
		return modelParts;
	}
}
