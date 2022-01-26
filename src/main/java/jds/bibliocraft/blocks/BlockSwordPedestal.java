package jds.bibliocraft.blocks;

import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntitySwordPedestal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockSwordPedestal extends BiblioColorBlock
{
	public static final String name = "SwordPedestal";
	public static final BlockSwordPedestal instance = new BlockSwordPedestal();
	
	public BlockSwordPedestal()
	{
		super(Material.ROCK, SoundType.STONE, name);
	}

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote && te instanceof BiblioTileEntity)
		{
			BiblioTileEntity tile = (BiblioTileEntity)te;
			ItemStack swordtest = player.getHeldItem(EnumHand.MAIN_HAND);
			if (tile.getStackInSlot(0) == ItemStack.EMPTY)
			{
				if (swordtest != ItemStack.EMPTY)
				{
	
					if (swordtest.getItem() instanceof ItemSword || swordtest.getUnlocalizedName().toLowerCase().contains("sword") || swordtest.getUnlocalizedName().toLowerCase().contains("gt.metatool.01.0"))
					{
						if (swordtest.getItem() == Item.getItemFromBlock(this.instance))
						{
							return true;
						}
						//System.out.println("I can haz sword?");
						tile.setInventorySlotContents(0, swordtest);
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
						return true;
					}
				}
			}
			else
			{
				tile.removeStackFromInventoryFromWorld(0, player, this);
				return true;
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntitySwordPedestal();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile)
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		return modelParts;
	}
	
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    	TileEntity pretile = blockAccess.getTileEntity(pos);
    	if (pretile != null && pretile instanceof TileEntitySwordPedestal)
    	{
    		TileEntitySwordPedestal tile = (TileEntitySwordPedestal)pretile;
    		EnumFacing angle = tile.getAngle();
    		float withsword = 0.0f;
    		if (tile.getStackInSlot(0) != ItemStack.EMPTY)
    		{
    			withsword = 0.76f;
    		}
    		switch (angle)
    		{
	    		case SOUTH:{output = this.getBlockBounds(0.3F, 0.0F, 0.1F, 0.7F, 0.24F+withsword, 0.9F);break;}
	    		case WEST:{output = this.getBlockBounds(0.1F, 0.0F, 0.3F, 0.9F, 0.24F+withsword, 0.7F);break;}
	    		case NORTH:{output = this.getBlockBounds(0.3F, 0.0F, 0.1F, 0.7F, 0.24F+withsword, 0.9F);break;}
	    		case EAST:{output = this.getBlockBounds(0.1F, 0.0F, 0.3F, 0.9F, 0.24F+withsword, 0.7F);break;}
	    		default: break;
    		}
    		
    	}
    	return output;
	}
    
	@Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess blocka, BlockPos pos, EnumFacing side)
    {
		TileEntitySwordPedestal tile = (TileEntitySwordPedestal)blocka.getTileEntity(pos);
		if (tile != null)
		{
			if (tile.getStackInSlot(0) != ItemStack.EMPTY)
			{
				return 15;
			}
		}
		return 0;
    }
	
	@Override
	public int getStrongPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
		return getWeakPower(state, worldIn, pos, side);
    }
}
