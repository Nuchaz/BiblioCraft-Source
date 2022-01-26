package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityTypewriter;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockTypeWriter extends BiblioColorBlock
{
	public static final String name = "Typewriter";
	public static final BlockTypeWriter instance = new BlockTypeWriter();
	
	public BlockTypeWriter()
	{
		super(Material.IRON, SoundType.METAL, name);
	}

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote && tile != null && tile instanceof TileEntityTypewriter)
		{
			TileEntityTypewriter typewriter = (TileEntityTypewriter)tile;
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			EnumFacing typeAngle = typewriter.getAngle();

			if (player.isSneaking())
			{
				if (typewriter.getStackInSlot(0) != ItemStack.EMPTY)
				{
					typewriter.removeStackFromInventoryFromWorld(0, player, this);
				}
			}
			else
			{
				if (playerhand != ItemStack.EMPTY)
				{
					if (playerhand.getItem() == Items.PAPER)
					{
						int returnsize = typewriter.addPaper(playerhand); 
						if (returnsize > 0)
						{
							playerhand.setCount(returnsize);
							player.inventory.setInventorySlotContents(player.inventory.currentItem, playerhand);
						}
						else if (returnsize == 0)
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
						}
					}
					return true;
				}
				
				if (isFrontFace(typeAngle, side) && typewriter.getStackInSlot(1) == ItemStack.EMPTY)
				{
					if (typewriter.getHasEnoughPaper())
					{
						
						if (typewriter.getBookWriteCount() < 15)
						{
							typewriter.entityName = player.getName();
							typewriter.entityType = 0;
							typewriter.setBookWriteCount(typewriter.getBookWriteCount()+1, true);
						}
						else
						{
							if (typewriter.removePaperForBook())
							{
								typewriter.foundValidEntity = true;
								typewriter.entityName = player.getName();
								typewriter.entityType = 0;
								typewriter.writeCustomBook();
							}
						}
					}
					return true;
				}
				
				if (typewriter.getStackInSlot(1) != ItemStack.EMPTY)
				{
					typewriter.foundValidEntity = false;
					typewriter.entityName = "";
					typewriter.entityType = 0;
					typewriter.setBookWriteCount(0, true);
					typewriter.removeStackFromInventoryFromWorld(1, player, this);
				}
			}
		}
		return true;
	}
	
	public boolean isFrontFace(EnumFacing typeAngle, EnumFacing face)
	{
		switch (typeAngle)
		{
			case SOUTH:{if (face == EnumFacing.WEST){return true;}break;}
			case WEST:{if (face == EnumFacing.NORTH){return true;}break;}
			case NORTH:{if (face == EnumFacing.EAST){return true;}break;}
			case EAST:{if (face == EnumFacing.SOUTH){return true;}break;}
			default: break;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityTypewriter();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile)
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("base");
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.09f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tile = blockAccess.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			BiblioTileEntity biblioTile = (BiblioTileEntity)tile;
			switch (biblioTile.getAngle())
			{
				case SOUTH:{output = this.getBlockBounds(0.05F, 0.0F, 0.25F, 0.55F, 0.3F, 0.75F); break;}
				case WEST:{output = this.getBlockBounds(0.25F, 0.0F, 0.05F, 0.75F, 0.3F, 0.55F); break;}
				case NORTH:{output = this.getBlockBounds(0.45F, 0.0F, 0.25F, 0.95F, 0.3F, 0.75F); break;}
				case EAST:{output = this.getBlockBounds(0.25F, 0.0F, 0.45F, 0.75F, 0.3F, 0.95F); break;}
				default:break;
			}
		}
		return output;
	}
}
