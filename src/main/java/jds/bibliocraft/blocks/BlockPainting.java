package jds.bibliocraft.blocks;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.helpers.PaintingUtil;
import jds.bibliocraft.items.ItemPaintingCanvas;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioPaintingC;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPainting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public abstract class BlockPainting extends BiblioWoodBlock
{
	private EnumPaintingFrame frameType = EnumPaintingFrame.BORDERLESS;
	
	public BlockPainting(String name, EnumPaintingFrame frameID)
	{
		super(name, false);
		this.frameType = frameID;
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
		if (!world.isRemote)
		{
			TileEntityPainting painting = (TileEntityPainting)world.getTileEntity(pos);
			if (playerhand != ItemStack.EMPTY)
			{
				if (playerhand.getItem() instanceof ItemPaintingCanvas)
				{
					painting.addStackToInventoryFromWorldSingleStackSize(playerhand, 0, player); 
					painting.resetPaintingData();
					return true;
				}
			}
			
			if (player.isSneaking())
			{
				if (painting.hasPainting())
				{
					painting.removeStackFromInventoryFromWorld(0, player, this);
					return true;
				}
			}
			player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());	
		}
		else
		{
			if (playerhand != ItemStack.EMPTY)
			{		
				if (playerhand.getItem() instanceof ItemPaintingCanvas)
				{
					//System.out.println("client side run of adding a painting");
					NBTTagCompound paintingTags = playerhand.getTagCompound();
					if (paintingTags != null && paintingTags.hasKey("paintingType"));
					{
						if (paintingTags.getInteger("paintingType") == 2 && PaintingUtil.customArtNames != null)
						{
							//System.out.println("custom painting");
							String name = paintingTags.getString("paintingTitle");
							int paintingNum = this.getCustomPaintingNum(name);
							if (paintingNum >= 0)
							{
								int resx = PaintingUtil.customArtWidths[paintingNum];
								int resy = PaintingUtil.customArtHeights[paintingNum];
								int aspectX = 1;
								int aspectY = 1;
								if (resx <= resy)
								{
									aspectX = roundNum(resx*1.0f / resx*1.0f);
									aspectY = roundNum(resy*1.0f / resx*1.0f);
								}
								else
								{
									aspectX = roundNum(resx*1.0f / resy*1.0f);
									aspectY = roundNum(resy*1.0f/resy*1.0f);
								}
								BiblioNetworking.INSTANCE.sendToServer(new BiblioPaintingC(pos, aspectX, aspectY));
								// ByteBuf buffer = Unpooled.buffer();
						    	// buffer.writeInt(pos.getX());
						    	// buffer.writeInt(pos.getY());
						    	// buffer.writeInt(pos.getZ());
						    	// buffer.writeInt(aspectX);
						    	// buffer.writeInt(aspectY);
								// BiblioCraft.ch_BiblioPaintingC.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioPaintingC"));
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	private int getCustomPaintingNum(String paintingName)
	{
		for (int i = 0; i < PaintingUtil.customArtNames.length; i++)
		{
			if (PaintingUtil.customArtNames[i].contentEquals(paintingName))
			{
				return i;
			}
		}
		return -1;
	}
	
	private int roundNum(float num)
	{
		int roundDown = (int)num;
		float roundTest = num - roundDown;
		if (roundTest >= 0.5f || roundDown == 0)
		{
			return roundDown+1;
		}
		else
		{
			return roundDown;
		}
	}
/*
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPainting();
	}

	/*
	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		//List<String> modelParts = new ArrayList<String>();
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		
		
	}
*/
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		return transform;
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    	TileEntity te = world.getTileEntity(pos);
    	if (te != null && te instanceof BiblioTileEntity)
    	{
    		BiblioTileEntity tile = (BiblioTileEntity)te;
    		switch (tile.getAngle())
			{
				case SOUTH:
				{
						output = this.getBlockBounds(0.92F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
						break;
				}
				case WEST:
				{
						output = this.getBlockBounds(0.0F, 0.0F, 0.92F, 1.0F, 1.0F, 1.0F);
						break;
				}
				case NORTH:
				{
						output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 0.08F, 1.0F, 1.0F);
						break;
				}
				case EAST:
				{
						output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.08F);
						break;
				}
				default: {output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 0.05F, 1.0F, 1.0F); break;}
			}
    	}
    	return output;
	}
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
    	TileEntity te = world.getTileEntity(pos);
    	if (te != null && te instanceof TileEntityPainting)
    	{
    		TileEntityPainting tile = (TileEntityPainting)te;
    		if (tile.getHideFrame())
    		{
    			return null;
    		}
    	}
    	return state.getBoundingBox(world, pos);
        //return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }
    
	@Override
	public void breakBlock(World world,BlockPos pos, IBlockState state)
	{
		dropItems(world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityPainting)
		{
			disconnectFrame(world, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
		}
		super.breakBlock(world, pos, state);
	}
	
    private void disconnectFrame(World world, int x, int y, int z)
	{
		TileEntityPainting tile = (TileEntityPainting)world.getTileEntity(new BlockPos(x, y, z));
		if (!world.isRemote && tile != null)
		{
			TileEntity tup = world.getTileEntity(new BlockPos(x, y + 1, z));
			TileEntity tdown = world.getTileEntity(new BlockPos(x, y - 1, z));
			TileEntity tleft = null;
			TileEntity tright = null;
			
			switch (tile.getAngle())
			{
				case SOUTH:{tleft = world.getTileEntity(new BlockPos(x, y, z - 1)); tright = world.getTileEntity(new BlockPos(x, y, z + 1)); break;}
				case WEST:{tleft = world.getTileEntity(new BlockPos(x + 1, y, z)); tright = world.getTileEntity(new BlockPos(x - 1, y, z)); break;}
				case NORTH:{tleft = world.getTileEntity(new BlockPos(x, y, z + 1)); tright = world.getTileEntity(new BlockPos(x, y, z - 1)); break;}
				case EAST:{tleft = world.getTileEntity(new BlockPos(x - 1, y, z)); tright = world.getTileEntity(new BlockPos(x + 1, y, z)); break;}
				default: return;
			}
			
			if (tup != null && tup instanceof TileEntityPainting)
			{
				TileEntityPainting pup = (TileEntityPainting)tup;
				if (tile.getAngle() == pup.getAngle())
				{
					pup.setConnectBottom(false);
				}
			}
			
			if (tdown != null && tdown instanceof TileEntityPainting)
			{
				TileEntityPainting pdown = (TileEntityPainting)tdown;
				if (tile.getAngle() == pdown.getAngle())
				{
					pdown.setConnectTop(false);
				}	
			}
			
			if (tleft != null && tleft instanceof TileEntityPainting)
			{
				TileEntityPainting pleft = (TileEntityPainting)tleft;
				if (tile.getAngle() == pleft.getAngle())
				{	
					pleft.setConnectRight(false);
				}
			}
			
			if (tright != null && tright instanceof TileEntityPainting)
			{
				TileEntityPainting pright = (TileEntityPainting)tright;
				if (tile.getAngle() == pright.getAngle())
				{
					pright.setConnectLeft(false);
				}
			}
			

			tile.setConnectBottom(false);
			tile.setConnectLeft(false);
			tile.setConnectRight(false);
			tile.setConnectTop(false);
			
			if (tile.getAngle() == EnumFacing.SOUTH || tile.getAngle() == EnumFacing.NORTH)
			{
				world.notifyNeighborsOfStateChange(new BlockPos(x, y + 1, z - 1), BlockPaintingFrameBorderless.instance, true);
				world.notifyNeighborsOfStateChange(new BlockPos(x, y + 1, z + 1), BlockPaintingFrameBorderless.instance, true);
				world.notifyNeighborsOfStateChange(new BlockPos(x, y - 1, z - 1), BlockPaintingFrameBorderless.instance, true);
				world.notifyNeighborsOfStateChange(new BlockPos(x, y - 1, z + 1), BlockPaintingFrameBorderless.instance, true);
			}
			else
			{
				world.notifyNeighborsOfStateChange(new BlockPos(x + 1, y + 1, z), BlockPaintingFrameBorderless.instance, true);
				world.notifyNeighborsOfStateChange(new BlockPos(x - 1, y + 1, z), BlockPaintingFrameBorderless.instance, true);
				world.notifyNeighborsOfStateChange(new BlockPos(x + 1, y - 1, z), BlockPaintingFrameBorderless.instance, true);
				world.notifyNeighborsOfStateChange(new BlockPos(x - 1, y - 1, z), BlockPaintingFrameBorderless.instance, true);
				//world.notifyBlockOfStateChange(new BlockPos(x - 1, y - 1, z), BlockPaintingFrameBorderless.instance); this is how they used to look
			}
		}
	}
    
    public void onBlockPlacedConnect(World world, int x, int y, int z, TileEntityPainting tile, boolean recurse)
    {
		if (!world.isRemote)
		{
			TileEntity tup = world.getTileEntity(new BlockPos(x, y + 1, z));
			TileEntity tdown = world.getTileEntity(new BlockPos(x, y - 1, z));
			TileEntity tleft = null;
			TileEntity tright = null;
			
			switch (tile.getAngle())
			{
				case SOUTH:{tleft = world.getTileEntity(new BlockPos(x, y, z - 1)); tright = world.getTileEntity(new BlockPos(x, y, z + 1)); break;}
				case WEST:{tleft = world.getTileEntity(new BlockPos(x + 1, y, z)); tright = world.getTileEntity(new BlockPos(x - 1, y, z)); break;}
				case NORTH:{tleft = world.getTileEntity(new BlockPos(x, y, z + 1)); tright = world.getTileEntity(new BlockPos(x, y, z - 1)); break;}
				case EAST:{tleft = world.getTileEntity(new BlockPos(x - 1, y, z)); tright = world.getTileEntity(new BlockPos(x + 1, y, z)); break;}
				default: return;
			}
			
			if (tup != null && tup instanceof TileEntityPainting)
			{
				TileEntityPainting pup = (TileEntityPainting)tup;
				if (tile.getAngle() == pup.getAngle())
				{
					pup.setConnectBottom(true);
					tile.setConnectTop(true);
					if (recurse)
					{
						this.onBlockPlacedConnect(world, x, y + 1, z, pup, false);
					}
 				}
			}
			
			if (tdown != null && tdown instanceof TileEntityPainting)
			{
				TileEntityPainting pdown = (TileEntityPainting)tdown;
				if (tile.getAngle() == pdown.getAngle())
				{
					pdown.setConnectTop(true);
					tile.setConnectBottom(true);
					if (recurse)
					{
						this.onBlockPlacedConnect(world, x, y - 1, z, pdown, false);
					}
				}	
			}
			
			if (tleft != null && tleft instanceof TileEntityPainting)
			{
				TileEntityPainting pleft = (TileEntityPainting)tleft;
				if (tile.getAngle() == pleft.getAngle())
				{	
					pleft.setConnectRight(true);
					tile.setConnectLeft(true);
					if (recurse)
					{
						switch (tile.getAngle())
						{
							case SOUTH:{this.onBlockPlacedConnect(world, x, y, z - 1, pleft, false); break;}
							case WEST:{this.onBlockPlacedConnect(world, x + 1, y, z, pleft, false); break;}
							case NORTH:{this.onBlockPlacedConnect(world, x, y, z + 1, pleft, false); break;}
							case EAST:{this.onBlockPlacedConnect(world, x - 1, y, z, pleft, false); break;}
							default: return;
						}
					}
				}
			}
			
			if (tright != null && tright instanceof TileEntityPainting)
			{
				TileEntityPainting pright = (TileEntityPainting)tright;
				if (tile.getAngle() == pright.getAngle())
				{
					pright.setConnectLeft(true);
					tile.setConnectRight(true);
					if (recurse)
					{
						switch (tile.getAngle())
						{
							case SOUTH:{this.onBlockPlacedConnect(world, x, y, z + 1, pright, false); break;}
							case WEST:{this.onBlockPlacedConnect(world, x - 1, y, z, pright, false); break;}
							case NORTH:{this.onBlockPlacedConnect(world, x, y, z - 1, pright, false); break;}
							case EAST:{this.onBlockPlacedConnect(world, x + 1, y, z, pright, false); break;}
							default: return;
						}
					}
				}
			}
		}
    }
    // */
}
