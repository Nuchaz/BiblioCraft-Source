package jds.bibliocraft.blocks;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.Config;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityDiscRack;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockDiscRack extends BiblioSimpleBlock
{
	public static final BlockDiscRack instance = new BlockDiscRack();
	public static final String name = "DiscRack";
	
	public BlockDiscRack()
	{
		super(Material.WOOD, SoundType.WOOD, name);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote && tile != null && tile instanceof TileEntityDiscRack)
		{
			ItemStack playerStack = player.getHeldItem(EnumHand.MAIN_HAND);
			TileEntityDiscRack rackTile = (TileEntityDiscRack)tile;
			if (rackTile != null)
			{
				EnumFacing angle = rackTile.getAngle();
				EnumVertPosition vertAngle = rackTile.getVertPosition();
				boolean isRotated = rackTile.getWallRotation();
				int discSlot = getDiscSlot(hitX, hitY, hitZ, angle, vertAngle, isRotated);
				if (playerStack != ItemStack.EMPTY)
				{
					String discName = playerStack.getUnlocalizedName().toLowerCase();
					if (playerStack.getItem() instanceof ItemRecord || Config.testDiscValidity(discName))
					{
						if (rackTile.addStackToInventoryFromWorld(playerStack, discSlot, player))
						{
							return true;
						}
					}
				}
				else
				{
					if (player.isSneaking())
					{
						if (rackTile.removeStackFromInventoryFromWorld(discSlot, player, this))
						{
							return true;
						}
					}
				}
			}
			player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityDiscRack();
	}
	
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		switch (tile.getVertPosition())
		{
			case FLOOR: { break; }
			case WALL:
			{
				boolean isRotated = false;
				if (tile instanceof TileEntityDiscRack)
				{
					TileEntityDiscRack discrack = (TileEntityDiscRack)tile;
					isRotated = discrack.getWallRotation();
				}
				if (isRotated)
				{
					transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
							   new Quat4f(1.0f, 0.0f, 0.0f, 1.0f), 
							   new Vector3f(1.0f, 1.0f, 1.0f), 
							   new Quat4f(0.0f, 0.0f, 1.0f, 1.0f)));
				}
				else
				{
					transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 1.0f, 0.0f), 
							   new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), 
							   new Vector3f(1.0f, 1.0f, 1.0f), 
							   new Quat4f(0.0f, 0.0f, 1.0f, 1.0f)));
				}
				break;
			}
			case CEILING:
			{
				transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 1.0f, -1.0f), 
						   new Quat4f(1.0f, 0.0f, 0.0f, 1.0f), 
						   new Vector3f(1.0f, 1.0f, 1.0f), 
						   new Quat4f(1.0f, 0.0f, 0.0f, 1.0f)));
				break;
			}
		}
		return transform;
	}
	
	public static int getDiscSlot(float hitX, float hitY, float hitZ, EnumFacing angle, EnumVertPosition vertAngle, boolean wallRotated)
	{
		int discSlot = 0;
		switch (vertAngle)
		{
			case FLOOR:
			{
				switch (angle)
				{
					case SOUTH:{discSlot = getDiscSlotPos(hitX);break;}
					case WEST:{discSlot = getDiscSlotNeg(hitZ);break;}
					case NORTH:{discSlot = getDiscSlotNeg(hitX);break;}
					case EAST:{discSlot = getDiscSlotPos(hitZ);break;}
					default: break;
				}
				break;
			}
			case WALL:
			{
				if (!wallRotated)
				{
					discSlot = getDiscSlotPos(hitY);
				}
				else
				{
					switch (angle)
					{
						case SOUTH:{discSlot = getDiscSlotPos(hitZ);break;}
						case WEST:{discSlot = getDiscSlotNeg(hitX);break;}
						case NORTH:{discSlot = getDiscSlotNeg(hitZ);break;}
						case EAST:{discSlot = getDiscSlotPos(hitX);break;}
						default: break;
					}
				}
				break;
			}
			case CEILING:
			{
				switch (angle)
				{
					case SOUTH:{discSlot = getDiscSlotNeg(hitX);break;}
					case WEST:{discSlot = getDiscSlotPos(hitZ);break;}
					case NORTH:{discSlot = getDiscSlotPos(hitX);break;}
					case EAST:{discSlot = getDiscSlotNeg(hitZ);break;}
					default: break;
				}
				break;
			}
		}
		return discSlot;
	}
	
	public static int getDiscSlotPos(float hit)
	{
		if (hit >= 0.87f) 					{ return 8; }
		if (hit < 0.87f && hit >= 0.766f) 	{ return 7; }
		if (hit < 0.766f && hit >= 0.662f) 	{ return 6; }
		if (hit < 0.662f && hit >= 0.558f) 	{ return 5; }
		if (hit < 0.558f && hit >= 0.454f) 	{ return 4; }
		if (hit < 0.454f && hit >= 0.35f) 	{ return 3; }
		if (hit < 0.35f && hit >= 0.246f) 	{ return 2; }
		if (hit < 0.246f && hit >= 0.142f) 	{ return 1; }
		if (hit < 0.142f) 					{ return 0; }
		return 0;
	}
	public static int getDiscSlotNeg(float hit)
	{
		if (hit >= 0.87f) 					{ return 0; }
		if (hit < 0.87f && hit >= 0.766f) 	{ return 1; }
		if (hit < 0.766f && hit >= 0.662f) 	{ return 2; }
		if (hit < 0.662f && hit >= 0.558f) 	{ return 3; }
		if (hit < 0.558f && hit >= 0.454f)	{ return 4; }
		if (hit < 0.454f && hit >= 0.35f) 	{ return 5; }
		if (hit < 0.35f && hit >= 0.246f) 	{ return 6; }
		if (hit < 0.246f && hit >= 0.142f) 	{ return 7; }
		if (hit < 0.142f) 					{ return 8; }
		return 0;
	}
	
	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
	     int pitch = MathHelper.floor(player.rotationPitch * 3.0F / 180.0F + 0.5D) & 3;
	     ++pitch;
	     pitch %= 4;
	     if (pitch == 0)
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.CEILING);
	     }
	     else if (pitch == 1)
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.WALL);
	     }
	     else
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.FLOOR);
	     }
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    	TileEntity tile = blockAccess.getTileEntity(pos);
    	if (tile != null && tile instanceof TileEntityDiscRack)
    	{
    		TileEntityDiscRack rackTile = (TileEntityDiscRack)tile;
	    	EnumFacing rackAngle = rackTile.getAngle();
			EnumVertPosition vertRackAngle = rackTile.getVertPosition();
			boolean rotated = rackTile.getWallRotation();
			switch (rackAngle)
			{
				case SOUTH:
				{
					switch (vertRackAngle)
					{
						case FLOOR:{output = this.getBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 0.35F, 0.75F); break;} 
						case WALL:
						{
							if (!rotated)
							{
								output = this.getBlockBounds(0.65F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
							}
							else
							{
								output = this.getBlockBounds(0.65F, 0.25F, 0.0F, 1.0F, 0.75F, 1.0F);
							}
							break;
						}
						case CEILING:{output = this.getBlockBounds(0.0F, 0.65F, 0.25F, 1.0F, 1.0F, 0.75F); break;}
					}
					break;
				}
				case WEST:
				{
					switch (vertRackAngle)
					{
						case FLOOR:{output = this.getBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 0.35F, 1.0F); break;}
						case WALL:
						{
							if (!rotated)
							{
								output = this.getBlockBounds(0.25F, 0.0F, 0.65F, 0.75F, 1.0F, 1.0F);
							}
							else
							{
								output = this.getBlockBounds(0.0F, 0.25F, 0.65F, 1.0F, 0.75F, 1.0F);
							}
							break;
						}
						case CEILING:{output = this.getBlockBounds(0.25F, 0.65F, 0.0F, 0.75F, 1.0F, 1.0F); break;}
					}
					break;
				}
				case NORTH:
				{
					switch (vertRackAngle)
					{
						case FLOOR:{output = this.getBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 0.35F, 0.75F); break;}
						case WALL:
						{
							if (!rotated)
							{
								output = this.getBlockBounds(0.0F, 0.0F, 0.25F, 0.35F, 1.0F, 0.75F);
							}
							else
							{
								output = this.getBlockBounds(0.0F, 0.25F, 0.0F, 0.35F, 0.75F, 1.0F);
							}
							break;
						}
						case CEILING:{output = this.getBlockBounds(0.0F, 0.65F, 0.25F, 1.0F, 1.0F, 0.75F); break;}
					}
					break;
				}
				case EAST:
				{
					switch (vertRackAngle)
					{
						case FLOOR:{output = this.getBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 0.35F, 1.0F); break;}
						case WALL:
						{
							if (!rotated)
							{
								output = this.getBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 0.35F);
							}
							else
							{
								output = this.getBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 0.75F, 0.35F);
							}
							break;
						}
						case CEILING:{output = this.getBlockBounds(0.25F, 0.65F, 0.0F, 0.75F, 1.0F, 1.0F); break;}
					}
					break;
				}
				default: break;
			}
			
    	}
    	return output;
	}
}
