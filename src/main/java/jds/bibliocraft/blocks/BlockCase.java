package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioSoundPlayer;
import jds.bibliocraft.states.TextureState;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityCase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class BlockCase extends BiblioWoodBlock
{
	public static final String name = "Case";
	public static final BlockCase instance = new BlockCase();
	public static final float range = 32.0F;
	
	public BlockCase()
	{
		super(name, true);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			TileEntityCase tile = (TileEntityCase)world.getTileEntity(pos);
			ItemStack playerHand = player.getHeldItem(EnumHand.MAIN_HAND);
			if (tile != null)
			{
				if (player.isSneaking())
				{
					tile.setOpenLid(!tile.getOpenLid());
					TargetPoint target = new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
					if (tile.getOpenLid())
					{
						//world.playSound(null, pos, CommonProxy.SOUND_CASE_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F); 
						BiblioNetworking.INSTANCE.sendToAllAround(new BiblioSoundPlayer(CommonProxy.SOUND_CASE_OPEN_TEXT, pos, 1.0F, 1.0F), target);
					}
					else
					{
						//world.playSound(null, pos, CommonProxy.SOUND_CASE_CLOSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
						BiblioNetworking.INSTANCE.sendToAllAround(new BiblioSoundPlayer(CommonProxy.SOUND_CASE_CLOSE_TEXT, pos, 1.0F, 1.0F), target);
					}
				}
				else if (tile.getOpenLid())
				{
					if (tile.isSlotFull())
					{
						//drop the item
						tile.removeStackFromInventoryFromWorld(0, player, this);
					}
					else if (playerHand != ItemStack.EMPTY)
					{
						//add
						if (Block.getBlockFromItem(playerHand.getItem()) instanceof BlockCarpet)
						{
							// add carpet
							tile.setInnerCover(playerHand, player);
						}
						else
						{
							tile.addStackToInventoryFromWorld(playerHand, 0, player);
						}
					}
					else
					{
						player.openGui(BiblioCraft.instance, 5, world, pos.getX(), pos.getY(), pos.getZ());
					}
				}
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityCase();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("case_inside");
		modelParts.add("case_bottom");
		if (tile instanceof TileEntityCase)
		{
			TileEntityCase casetile = (TileEntityCase)tile;
			if (casetile.getOpenLid())
			{
				modelParts.add("case_lid_glass_open");
				modelParts.add("case_lid_latch_open");
				modelParts.add("case_lid_wood_open");
			}
			else
			{
				modelParts.add("case_lid_glass");
				modelParts.add("case_lid_latch");
				modelParts.add("case_lid_wood");
			}
		}
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		 int pitch = MathHelper.floor(player.rotationPitch * 3.0F / 180.0F + 0.5D) & 3;
	     ++pitch;
	     pitch %= 2;
	     if (pitch == 1)
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.WALL);
	     }
	     else
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.FLOOR);
	     }
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		if (tile.getVertPosition() == EnumVertPosition.FLOOR)
		{
			transform = transform.compose(new TRSRTransformation(new Vector3f(1.0f, 0.0f, 1.0f), 
															     new Quat4f(0.0f, -1.0f, 0.0f, 1.0f), 
															     new Vector3f(1.0f, 1.0f, 1.0f), 
															     new Quat4f(0.0f, 0.0f, 1.0f, 1.0f)));
		}
		return transform;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tile = blockAccess.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity)
		{
			BiblioTileEntity caseTile = (BiblioTileEntity)tile;
			switch (caseTile.getAngle())
			{
				case SOUTH:
				{
					if (caseTile.getVertPosition() == EnumVertPosition.FLOOR)
					{
						output = this.getBlockBounds(0.06F, 0.0F, 0.0F, 0.94F, 0.5F, 1.0F);
					}
					else
					{
						output = this.getBlockBounds(0.5F, 0.0F, 0.06F, 1.0F, 1.0F, 0.94F);
					}
					break;
				}
				case WEST:
				{
					if (caseTile.getVertPosition() == EnumVertPosition.FLOOR)
					{
						output = this.getBlockBounds(0.0F, 0.0F, 0.06F, 1.0F, 0.5F, 0.94F);
					}
					else
					{
						output = this.getBlockBounds(0.06F, 0.0F, 0.5F, 0.94F, 1.0F, 1.0F);
					}
					break;
				}
				case NORTH:
				{
					if (caseTile.getVertPosition() == EnumVertPosition.FLOOR)
					{
						output = this.getBlockBounds(0.06F, 0.0F, 0.0F, 0.94F, 0.5F, 1.0F);
					}
					else
					{
						output = this.getBlockBounds(0.0F, 0.0F, 0.06F, 0.5F, 1.0F, 0.94F);
					}
					
					break;
				}
				case EAST:
				{
					if (caseTile.getVertPosition() == EnumVertPosition.FLOOR)
					{
						output = this.getBlockBounds(0.0F, 0.0F, 0.06F, 1.0F, 0.5F, 0.94F);
					}
					else
					{
						output = this.getBlockBounds(0.06F, 0.0F, 0.0F, 0.94F, 1.0F, 0.5F);
					}
					break;
				}
				default:break;
			}
		}
		return output;
	}
	
	@Override
    public TextureState addAdditionTextureStateInformation(BiblioTileEntity tile, TextureState state)
    {
    	ItemStack stack = tile.getStackInSlot(1);
    	if (stack != ItemStack.EMPTY)
    	{
        	state.setColorOne(EnumColor.getColorFromCarpetOrWool(stack));
    	}
    	else
    	{
    		state.setColorOne(EnumColor.WHITE);
    	}
    	return state;
    }
	
	@Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	@Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		int output = 0;
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityCase)
		{
			TileEntityCase tile = (TileEntityCase)te;
			ItemStack stack = tile.getStackInSlot(0);
			Item testItem = Item.getItemFromBlock(Blocks.REDSTONE_BLOCK);
			
			if (stack != ItemStack.EMPTY && stack.getUnlocalizedName().contains(testItem.getUnlocalizedName()))
			{
				output = 15;
			}
		}
		return output;
    }
	
	@Override
	public int getStrongPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
		
		return getWeakPower(state, worldIn, pos, side);
    }
}
