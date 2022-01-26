package jds.bibliocraft.blocks;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.Config;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPotionShelf;
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

public class BlockPotionShelf  extends BiblioWoodBlock
{
	public static final String name = "PotionShelf";
	public static final BlockPotionShelf instance = new BlockPotionShelf();
	
	public BlockPotionShelf()
	{
		super(name, true);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			BiblioTileEntity tile = (BiblioTileEntity)world.getTileEntity(pos);
			if (tile != null)
			{
				int potionSlot = getPotionShelfSlot(tile.getAngle(), side, hitX, hitY, hitZ);
				
				if (potionSlot >= 0 && player.isSneaking() && tile.getStackInSlot(potionSlot) != ItemStack.EMPTY)
				{
					if (tile.removeStackFromInventoryFromWorld(potionSlot, player, this))
					return true;
				}
				
				if (potionSlot >= 0 && playerhand != ItemStack.EMPTY && Config.testPotionValidity(playerhand.getUnlocalizedName(), playerhand.getDisplayName(), playerhand.getItem()))
				{
					// add item
					if (tile.addStackToInventoryFromWorld(playerhand, potionSlot, player))
					return true;
				}
			}
			player.openGui(BiblioCraft.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	public static int getPotionShelfSlot(EnumFacing angle, EnumFacing hitSide, float hitX, float hitY, float hitZ)
	{
		int output = -1;
		if (isFrontOfBlock(hitSide, angle))
		{
			output = isWhatShelf(hitY)+isWhatPot(hitSide, hitX, hitZ);
		}
		else if (isBackOfBlock(hitSide, angle))
		{
			EnumFacing invertedSide = hitSide;
			switch (hitSide)
			{
				case SOUTH: { invertedSide = EnumFacing.NORTH;  break; }
				case WEST: { invertedSide = EnumFacing.EAST;  break; }
				case NORTH: { invertedSide = EnumFacing.SOUTH;  break; }
				case EAST: { invertedSide = EnumFacing.WEST;  break; }
				default: break;
			}
			output = isWhatShelf(hitY) + isWhatPot(invertedSide, hitX, hitZ);
		}
		return output;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPotionShelf();
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
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.0f, 0.0f, -0.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
			     new Vector3f(1.0f, 1.0f, 1.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	private static int isWhatShelf(float hitY)
	{
		int yt = (int) (hitY * 3);
		switch (yt)
		{
		case 0: return 8;
		case 1: return 4;
		case 2: return 0;
		default: return 0;
		}
		

	}
	
	private static int isWhatPot(EnumFacing angle, float hitX, float hitZ)
	{
		int xt = (int) (hitX * 4);
		int zt = (int)( hitZ * 4);
		switch (angle)
		{
			case WEST:
			{ 
				return zt;
			}
			case NORTH:
			{ 
				switch (xt)
				{
				case 0: return 3;
				case 1: return 2;
				case 2: return 1;
				case 3: return 0;
				default: break;
				}
			}
			case EAST:
			{ 
				switch (zt)
				{
				case 0: return 3;
				case 1: return 2;
				case 2: return 1;
				case 3: return 0;
				default: break;
				}
			}
			case SOUTH:
			{ 
				return xt;
			}
			default:break;
		}
		return 0;
	}
}
