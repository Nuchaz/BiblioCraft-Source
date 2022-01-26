package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityFancyWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockFancyWorkbench extends BiblioWoodBlock
{
	public static final String name = "FancyWorkbench";
	public static final BlockFancyWorkbench instance = new BlockFancyWorkbench();
	
	public BlockFancyWorkbench()
	{
		super(name, false);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		// TODO this crashes when the menu is opened for some reason
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote && tile != null && tile instanceof TileEntityFancyWorkbench)
		{
			TileEntityFancyWorkbench bench = (TileEntityFancyWorkbench)tile;
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			if (isBackOfBlock(bench.getAngle(), side) && hitY > 0.22F && hitY < 0.74F)
			{
				int booknum = isWhatBook(bench.getAngle(), hitX, hitZ) + 1;
				//System.out.println(booknum);					ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
				if (playerhand != ItemStack.EMPTY)
				{
					if (playerhand.getItem() instanceof ItemBook || playerhand.getItem() instanceof ItemRecipeBook && booknum != -1)
					{
						if (bench.addStackToInventoryFromWorld(playerhand, booknum, player))
						{
							return true;
						}
					}
				}
				else if (player.isSneaking())
				{
					if (bench.getStackInSlot(booknum) != ItemStack.EMPTY && booknum != -1)
					{
						bench.removeStackFromInventoryFromWorld(booknum, player, this);
					}
					return true;
				}
			}
			if (!bench.isTooManyPlayers())
			{
				player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ()); 
			}
		}
		return true;
	}
	
	public int isWhatBook(EnumFacing angle, float hitX, float hitZ)
	{
		int xt = (int) (hitX * 8);
		int zt = (int)( hitZ * 8);
		switch (angle)
		{
			case SOUTH:
			{ 
				return zt;
			}
			case WEST:
			{ 
				switch (xt)
				{
				case 0: return 7;
				case 1: return 6;
				case 2: return 5;
				case 3: return 4;
				case 4: return 3;
				case 5: return 2;
				case 6: return 1;
				case 7: return 0;
				default: break;
				}
			}
			case NORTH:
			{ 
				switch (zt)
				{
				case 0: return 7;
				case 1: return 6;
				case 2: return 5;
				case 3: return 4;
				case 4: return 3;
				case 5: return 2;
				case 6: return 1;
				case 7: return 0;
				default: break;
				}
			}
			case EAST:
			{ 
				return xt;
			}
			default: break;
		}
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityFancyWorkbench();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		//List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("bench");
		modelParts.add("top");
		modelParts.add("sides");
		if (tile instanceof TileEntityFancyWorkbench)
		{
			TileEntityFancyWorkbench bench = (TileEntityFancyWorkbench)tile;
			int[] books = bench.getBookArray();
			for (int i = 0; i < books.length; i++)
			{
				if (books[i] == 1)
				{
					int j = i + 1;
					String bookName = "book" + j;
					modelParts.add(bookName);
				}
			}
		}
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
