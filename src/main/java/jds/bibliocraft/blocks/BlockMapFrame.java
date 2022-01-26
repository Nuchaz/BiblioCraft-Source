package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class BlockMapFrame extends BiblioWoodBlock
{
	public static final String name = "MapFrame";
	public static final BlockMapFrame instance = new BlockMapFrame();
	
	public BlockMapFrame()
	{
		super(name, false);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		TileEntityMapFrame frameTile = (TileEntityMapFrame)world.getTileEntity(pos);	
		ItemStack playerStack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (!world.isRemote)
		{
			if (frameTile != null)
			{
				//System.out.println("Angle = " + frameTile.getAngle() + "    Face = " + side);
				if (player.isSneaking())
				{

					frameTile.rotateMap();
					return true;
				}
				else
				{
					if (playerStack != ItemStack.EMPTY)
					{
						if (playerStack.getItem() ==Items.FILLED_MAP)
						{
							int stackSize = playerStack.getCount();
							if (frameTile.addMap(playerStack))
							{
								if (stackSize == 1)
								{
									player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
								}
								else
								{
									stackSize--;
									playerStack.setCount(stackSize);
									player.inventory.setInventorySlotContents(player.inventory.currentItem, playerStack);
								}
								return true;
							}
						}
						if (playerStack.getItem() instanceof ItemDrill)
						{
							frameTile.removeStackFromInventoryFromWorld(0, player, this);
							frameTile.removeMap();
						}
					}
				}
			}
		}
		if (world.isRemote)
		{
			if (player.isSneaking())
			{
				return true;
			}
			else
			{
				int faceCheck = frameTile.checkFace(frameTile.getAngle(), side, frameTile.getVertPosition());
				if (faceCheck != -1)
				{
					int pinPass = -1;
					switch (faceCheck)
					{
						case 0:
						{
							pinPass = frameTile.findPinCoords(hitX, hitY);
							break;
						}
						case 1:
						{
							pinPass = frameTile.findPinCoords(hitZ, hitY);
							break;
						}
						case 2:
						{
							pinPass = frameTile.findPinCoords(hitX, hitZ);
							break;
						}
					}
					if (pinPass != -1)
					{
						int[] coords = frameTile.getWorldCoordsFromPin(pinPass);
						if (coords != null)
						{
							String pinName = frameTile.getPinName(pinPass);
							player.sendMessage(new TextComponentString(pinName+"  @  X = "+coords[0]+"   Z = "+coords[1]));
							if (playerStack != ItemStack.EMPTY)
							{
								if (playerStack.getItem() instanceof ItemWaypointCompass)
								{
									ItemWaypointCompass comp = (ItemWaypointCompass)playerStack.getItem();
									ItemStack updatedCompass = comp.writeNBT(playerStack, coords[0], coords[1], pinName);
							        try
							        {
							        	ByteBuf buffer = Unpooled.buffer();
							        	ByteBufUtils.writeItemStack(buffer, updatedCompass);
										BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(updatedCompass, false));
										// BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
							        }
							        catch (Exception ex)
							        {
							            ex.printStackTrace();
							        }
								}
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityMapFrame();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		if (tile instanceof TileEntityMapFrame)
		{
			TileEntityMapFrame frame = (TileEntityMapFrame)tile;
			modelParts = new ArrayList<String>();
			modelParts.add("main");
			if (!frame.getTopFrame()) { modelParts.add("borderTop"); }
			if (!frame.getBottomFrame()) { modelParts.add("borderBottom"); }
			if (!frame.getLeftFrame()) { modelParts.add("borderLeft"); }
			if (!frame.getRightFrame()) { modelParts.add("borderRight"); }
			if (!frame.getTopFrame() || !frame.getLeftFrame()) { modelParts.add("cornerTL"); }
			if (!frame.getTopFrame() || !frame.getRightFrame()) { modelParts.add("cornerTR"); }
			if (!frame.getBottomFrame() || !frame.getLeftFrame()) { modelParts.add("cornerBL"); }
			if (!frame.getBottomFrame() || !frame.getRightFrame()) { modelParts.add("cornerBR"); }
		}
		return modelParts;
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
	    	 biblioTile.setAngle(EnumFacing.SOUTH);
	     }
	     else if (pitch == 1)
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.WALL);
	     }
	     else
	     {
	    	 biblioTile.setVertPosition(EnumVertPosition.FLOOR);
	    	 biblioTile.setAngle(EnumFacing.SOUTH);
	     }
	     if (biblioTile instanceof TileEntityMapFrame)
	     {
	    	 TileEntityMapFrame frame = (TileEntityMapFrame)biblioTile;
			 checkNeighborMapFrames(biblioTile.getWorld(), biblioTile.getPos().getX(), biblioTile.getPos().getY(), biblioTile.getPos().getZ(), frame);
			 biblioTile.getWorld().notifyBlockUpdate(biblioTile.getPos(), biblioTile.getWorld().getBlockState(biblioTile.getPos()), biblioTile.getWorld().getBlockState(biblioTile.getPos()), 3); //getWorld().markBlockForUpdate(biblioTile.getPos());
	     }
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		if (tile.getVertPosition() == EnumVertPosition.CEILING)
		{
			transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 1.0f, 0.0f), 
															     new Quat4f(0.0f, 0.0f, -1.0f, 1.0f), 
															     new Vector3f(1.0f, 1.0f, 1.0f), 
															     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		}
		else if (tile.getVertPosition() == EnumVertPosition.FLOOR)
		{
			transform = transform.compose(new TRSRTransformation(new Vector3f(1.0f, 0.0f, 0.0f), 
															     new Quat4f(0.0f, 0.0f, 1.0f, 1.0f), 
															     new Vector3f(1.0f, 1.0f, 1.0f), 
															     new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		}
		return transform;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity != null && tileEntity instanceof TileEntityMapFrame)
		{
			TileEntityMapFrame frameTile = (TileEntityMapFrame)tileEntity;
			EnumVertPosition vertAngleGet = frameTile.getVertPosition();
			output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 0.05F, 1.0F, 1.0F);
			switch (frameTile.getAngle())
			{
				case SOUTH:
				{
					switch (vertAngleGet)
					{
						case FLOOR:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.05F, 1.0F);break;}
						case WALL:{output = this.getBlockBounds(0.95F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);break;}
						case CEILING:{output = this.getBlockBounds(0.0F, 0.95F, 0.0F, 1.0F, 1.0F, 1.0F);break;}
					}
					break;
				}
				case WEST:
				{
					switch (vertAngleGet)
					{
						case FLOOR:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.05F, 1.0F);break;} 
						case WALL:{output = this.getBlockBounds(0.0F, 0.0F, 0.95F, 1.0F, 1.0F, 1.0F);break;}
						case CEILING:{output = this.getBlockBounds(0.0F, 0.95F, 0.0F, 1.0F, 1.0F, 1.0F);break;}
					}
					break;
				}
				case NORTH:
				{
					switch (vertAngleGet)
					{
						case FLOOR:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.05F, 1.0F);break;}
						case WALL:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 0.05F, 1.0F, 1.0F);break;}
						case CEILING:{output = this.getBlockBounds(0.0F, 0.95F, 0.0F, 1.0F, 1.0F, 1.0F);break;}
					}
					break;
				}
				case EAST:
				{
					switch (vertAngleGet)
					{
						case FLOOR:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.05F, 1.0F);break;}
						case WALL:{output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.05F);break;}
						case CEILING:{output = this.getBlockBounds(0.0F, 0.95F, 0.0F, 1.0F, 1.0F, 1.0F);break;}
					}
					break;
				}
				default: {output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 0.05F, 1.0F, 1.0F); break;}
			}
		}
		return output;
	}
	
	@Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		 TileEntity tile = world.getTileEntity(pos);
		 if (tile != null && tile instanceof TileEntityMapFrame)
		 {
			 TileEntityMapFrame frameTile = (TileEntityMapFrame)tile;
			 checkNeighborMapFrames(tile.getWorld(), pos.getX(), pos.getY(), pos.getZ(), frameTile);
			 frameTile.getWorld().notifyBlockUpdate(frameTile.getPos(), frameTile.getWorld().getBlockState(frameTile.getPos()), frameTile.getWorld().getBlockState(frameTile.getPos()), 3);
		 }
    }
	 
	 public void checkNeighborMapFrames(World world, int i, int j, int k, TileEntityMapFrame mapFrame)
    {
    	EnumVertPosition vertAngle = mapFrame.getVertPosition();
    	EnumFacing angle = mapFrame.getAngle();
    	if (vertAngle == EnumVertPosition.WALL)
    	{
    		int xLeftAdjust = 0;
    		int zLeftAdjust = 0;
    		int xRightAdjust = 0;
    		int zRightAdjust = 0;
    		switch (angle)
    		{
    			case SOUTH: { zLeftAdjust--; zRightAdjust++; break; }
    			case WEST: { xLeftAdjust++; xRightAdjust--; break; }
    			case NORTH: { zLeftAdjust++; zRightAdjust--; break; }
    			case EAST: { xLeftAdjust--; xRightAdjust++; break; }
    			default: break;
    		}
    		TileEntity blockUp = world.getTileEntity(new BlockPos(i, j+1, k));
    		TileEntity blockDown = world.getTileEntity(new BlockPos(i, j-1, k));
    		TileEntity blockLeft = world.getTileEntity(new BlockPos(i + xLeftAdjust, j, k + zLeftAdjust));
    		TileEntity blockRight = world.getTileEntity(new BlockPos(i + xRightAdjust, j, k + zRightAdjust));

    		if (isMapFrameBlock(blockUp)) { mapFrame.setTopFrame(true); } else { mapFrame.setTopFrame(false); }
    		if (isMapFrameBlock(blockDown)) { mapFrame.setBottomFrame(true); } else { mapFrame.setBottomFrame(false); }
    		if (isMapFrameBlock(blockLeft)) { mapFrame.setLeftFrame(true); } else { mapFrame.setLeftFrame(false); }
    		if (isMapFrameBlock(blockRight)) { mapFrame.setRightFrame(true); } else { mapFrame.setRightFrame(false); }
    		
    	}
    	else
    	{	
			int xTopAdjust = 0, xBottomAdjust = 0, xLeftAdjust = 0, xRightAdjust = 0;
			int zTopAdjust = 0, zBottomAdjust = 0, zLeftAdjust = 0, zRightAdjust = 0;
			if (vertAngle == EnumVertPosition.FLOOR)
			{
				xTopAdjust++;
				xBottomAdjust--;
				zLeftAdjust--;
				zRightAdjust++;
			}
			else
			{
				xTopAdjust--;
				xBottomAdjust++;
				zLeftAdjust--;
				zRightAdjust++;
				//ceiling
			}
			TileEntity blockFront = world.getTileEntity(new BlockPos(i + xTopAdjust, j, k + zTopAdjust));
			TileEntity blockBack = world.getTileEntity(new BlockPos(i + xBottomAdjust, j, k + zBottomAdjust));
			TileEntity blockLeft = world.getTileEntity(new BlockPos(i + xLeftAdjust, j, k + zLeftAdjust));
			TileEntity blockRight = world.getTileEntity(new BlockPos(i + xRightAdjust, j, k + zRightAdjust));
			
			if (isMapFrameBlock(blockFront)) { mapFrame.setTopFrame(true); } else { mapFrame.setTopFrame(false); }
    		if (isMapFrameBlock(blockBack)) { mapFrame.setBottomFrame(true); } else { mapFrame.setBottomFrame(false); }
    		if (isMapFrameBlock(blockLeft)) { mapFrame.setLeftFrame(true); } else { mapFrame.setLeftFrame(false); }
    		if (isMapFrameBlock(blockRight)) { mapFrame.setRightFrame(true); } else { mapFrame.setRightFrame(false); }
	    }
    }
	 
	public boolean isMapFrameBlock(TileEntity blockID)
	{
		if (blockID != null && blockID instanceof TileEntityMapFrame)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
