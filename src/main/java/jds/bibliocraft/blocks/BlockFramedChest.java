package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityFramedChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockFramedChest extends BiblioWoodBlock
{
	public static final String name = "FramedChest";
	public static final BlockFramedChest instance = new BlockFramedChest();
	
	public BlockFramedChest()
	{
		super(name, false);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityFramedChest)
			{
				TileEntityFramedChest chest = (TileEntityFramedChest)tile;
				if (chest.getIsDouble())
				{
					TileEntityFramedChest chest2 = getAdjacentChest(chest, world);
					if (chest2 != null)
					{
						chest.setAdjacentChest(chest2);
						chest2.setAdjacentChest(chest);
						chest2.addUsingPlayer(true);
					}
				}
				
				chest.addUsingPlayer(true);
				player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	public TileEntityFramedChest getAdjacentChest(TileEntityFramedChest chest, World world)
	{
		TileEntity tile = null;
		int x = chest.getPos().getX();
		int y = chest.getPos().getY();
		int z = chest.getPos().getZ();
		switch (chest.getAngle())
		{
			case SOUTH:
			{
				if (chest.getIsLeft())
				{
					z++;
				}
				else
				{
					z--;
				}
				break;
			}
			case WEST:
			{
				if (chest.getIsLeft())
				{
					x--;
				}
				else
				{
					x++;
				}
				break;
			}
			case NORTH:
			{
				if (chest.getIsLeft())
				{
					z--;
				}
				else
				{
					z++;
				}
				break;
			}
			case EAST:
			{
				if (chest.getIsLeft())
				{
					x++;
				}
				else
				{
					x--;
				}
				break;
			}
			default: break;
		}
		tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityFramedChest)
		{
			TileEntityFramedChest chest2 = (TileEntityFramedChest)tile;
			if (chest2.getIsDouble())
			{
				return chest2;
			}
		}
		
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityFramedChest();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("small_chest");
		if (tile instanceof TileEntityFramedChest)
		{
			TileEntityFramedChest chest = (TileEntityFramedChest)tile;
			if (chest.getIsDouble())
			{
				modelParts = new ArrayList<String>();
				if (chest.getIsLeft())
				{
					modelParts.add("large_chest_left");
				}
				else
				{
					modelParts.add("large_chest_right");
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
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.054F, 0.0F, 0.054F, 0.946F, 0.866F, 0.946F);
    	TileEntity tile = blockAccess.getTileEntity(pos);
    	if (tile != null && tile instanceof TileEntityFramedChest)
    	{
    		TileEntityFramedChest chest = (TileEntityFramedChest)tile;
    		if (chest.getIsDouble())
    		{
    			switch (chest.getAngle())
    			{
	    			case SOUTH:
	    			{
	    				if (chest.getIsLeft())
	    				{
	    					output = this.getBlockBounds(0.054F, 0.0F, 0.054F, 0.946F, 0.866F, 1.0F);
	    				}
	    				else
	    				{
	    					output = this.getBlockBounds(0.054F, 0.0F, 0.0F, 0.946F, 0.866F, 0.946F);
	    				}
	    				break;
	    			}
	    			case WEST:
	    			{
	    				if (chest.getIsLeft())
	    				{
	    					output = this.getBlockBounds(0.0F, 0.0F, 0.054F, 0.946F, 0.866F, 0.946F);
	    				}
	    				else
	    				{
	    					output = this.getBlockBounds(0.054F, 0.0F, 0.054F, 1.0F, 0.866F, 0.946F);
	    				}
	    				break;
	    			}
	    			case NORTH:
	    			{
	    				if (chest.getIsLeft())
	    				{
	    					output = this.getBlockBounds(0.054F, 0.0F, 0.0F, 0.946F, 0.866F, 0.946F);
	    				}
	    				else
	    				{
	    					output = this.getBlockBounds(0.054F, 0.0F, 0.054F, 0.946F, 0.866F, 1.0F);
	    				}
	    				break;
	    			}
	    			case EAST:
	    			{
	    				if (chest.getIsLeft())
	    				{
	    					output = this.getBlockBounds(0.054F, 0.0F, 0.054F, 1.0F, 0.866F, 0.946F);
	    				}
	    				else
	    				{
	    					output = this.getBlockBounds(0.0F, 0.0F, 0.054F, 0.946F, 0.866F, 0.946F);
	    				}
	    				break;
	    			}
	    			default: break;
    			}
    		}
    	}
    	return output;
	}
	
	@Override
	public void breakBlock(World world,BlockPos pos, IBlockState state)
	{
		dropItems(world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityFramedChest)
		{
			TileEntityFramedChest chest = (TileEntityFramedChest)tile;
			if (chest.getIsDouble())
			{
				TileEntityFramedChest chest2 = getAdjacentChest(chest, world);
				if (chest2 != null)
				{
					chest2.setIsDouble(false, true, null);
				}
			}
		}
		super.breakBlock(world, pos, state);
	}
}
