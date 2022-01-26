package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.Config;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.helpers.EnumRelativeLocation;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioDeskOpenGui;
import jds.bibliocraft.states.TextureState;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityDesk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockDesk extends BiblioWoodBlock
{
	public static final String name = "Desk";
	public static final BlockDesk instance = new BlockDesk();
	
	public BlockDesk()
	{
		super(name, false);
	}
	
    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
    	boolean output = false;
    	if (side == EnumFacing.UP)
    	{
    		output = true;
    	}
        return output;
    }
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityDesk)
			{
				TileEntityDesk desk = (TileEntityDesk)tile;
				EnumFacing angle = desk.getAngle();
				boolean dontOpenGui = false;
				if (player.isSneaking())
				{
					// drop stuff
					if (isLeftBookStack(hitX, hitZ, side, angle))
					{
						dontOpenGui = desk.removeStackFromInventoryFromWorld(desk.getLeftBookFullSlot(), player, this);
					}
					else if (isRightBookStack(hitX, hitZ, side, angle))
					{
						dontOpenGui = desk.removeStackFromInventoryFromWorld(desk.getRightBookFullSlot(), player, this);
					}
					else if (isWritingBook(hitX, hitZ, side, angle))
					{
						dontOpenGui = desk.removeStackFromInventoryFromWorld(0, player, this);
					}
					
					if (side != EnumFacing.UP)
					{
						dontOpenGui = desk.removeStackFromInventoryFromWorld(9, player, this);
					}
				}
				else
				{
					// not sneaking
					ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
					if (playerhand != ItemStack.EMPTY)
					{
						if (!Config.isBlock(playerhand) && Config.testBookValidity(playerhand))
						{
							if (isLeftBookStack(hitX, hitZ, side, angle))
							{
								if (desk.getLeftBookEmptySlot() != -1)
									dontOpenGui = desk.addStackToInventoryFromWorld(playerhand, desk.getLeftBookEmptySlot(), player);
							}
							else if (isRightBookStack(hitX, hitZ, side, angle))
							{
								if (desk.getLeftBookEmptySlot() != -1)
								dontOpenGui = desk.addStackToInventoryFromWorld(playerhand, desk.getRightBookEmptySlot(), player);
							}
							else if (isWritingBook(hitX, hitZ, side, angle))
							{
								dontOpenGui = desk.addStackToInventoryFromWorld(playerhand, 0, player);
							}
						}
						
						if (side != EnumFacing.UP && Block.getBlockFromItem(playerhand.getItem()) instanceof BlockCarpet)
						{
							dontOpenGui = desk.addStackToInventoryFromWorldSingleStackSize(playerhand, 9, player);
						}
					}
				}
				
				if (!dontOpenGui)
				{
					if (isWritingBook(hitX, hitZ, side, angle) && desk.getStackInSlot(0) != ItemStack.EMPTY)
					{
						// ByteBuf buffer = Unpooled.buffer();
						// buffer.writeInt(pos.getX());
						// buffer.writeInt(pos.getY());
						// buffer.writeInt(pos.getZ());
						// ByteBufUtils.writeItemStack(buffer, desk.getStackInSlot(0));
						// if (desk.getStackInSlot(0).getItem() == ItemRecipeBook.instance) // TODO added this if statment
						// {
						// 	buffer.writeBoolean(Config.enableRecipeBookCrafting);
						// }
						// else
						// {
						// 	buffer.writeBoolean(false);
						// }
						BiblioNetworking.INSTANCE.sendTo(new BiblioDeskOpenGui(pos, desk.getStackInSlot(0), desk.getStackInSlot(0).getItem() == ItemRecipeBook.instance ? Config.enableRecipeBookCrafting : false), (EntityPlayerMP) player);
						// BiblioCraft.ch_BiblioDeskGUIS.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioDeskOpenGUI"), (EntityPlayerMP) player);
					}
					else
					{
						player.openGui(BiblioCraft.instance, 7, world, pos.getX(), pos.getY(), pos.getZ()); 
					}
				}
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityDesk();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("candle");
		modelParts.add("pen");
		modelParts.add("deskTopEndRight");
		modelParts.add("deskTopEndLeft");
		modelParts.add("legLeft");
		modelParts.add("legRight");
		modelParts.add("shelfSingle");
		if (tile instanceof TileEntityDesk)
		{
			TileEntityDesk desk = (TileEntityDesk)tile;
			switch (desk.getSingleLeftRightCenter())
			{
				case LEFT:
				{
					modelParts = new ArrayList<String>();
					modelParts.add("deskTopRight");
					modelParts.add("deskTopEndLeft");
					modelParts.add("legLeft");
					modelParts.add("shelfLeft");
					modelParts.add("candle");
					modelParts.add("backRight");
					break;
				}
				case RIGHT:
				{
					modelParts = new ArrayList<String>();
					modelParts.add("deskTopEndRight");
					modelParts.add("deskTopLeft");
					modelParts.add("legRight");
					modelParts.add("shelfRight");
					modelParts.add("pen");
					modelParts.add("backLeft");
					break;
				}
				case CENTER:
				{
					modelParts = new ArrayList<String>();
					modelParts.add("deskTopRight");
					modelParts.add("deskTopLeft");
					modelParts.add("backBoth");
					break;
				}
				default: break;
			}
			if (desk.getOpenBook() != ItemStack.EMPTY && desk.getOpenBook().getItem() != Items.AIR && !desk.getHasMap())
			{
				modelParts.add("bookOpen");
			}
			if (desk.getLeftBookStack() > 0) { modelParts.add("lbook1"); }
			if (desk.getLeftBookStack() > 1) { modelParts.add("lbook2"); }
			if (desk.getLeftBookStack() > 2) { modelParts.add("lbook3"); }
			if (desk.getLeftBookStack() > 3) { modelParts.add("lbook4"); }
			
			if (desk.getRightBookStack() > 0) { modelParts.add("rbook1"); }
			if (desk.getRightBookStack() > 1) { modelParts.add("rbook2"); }
			if (desk.getRightBookStack() > 2) { modelParts.add("rbook3"); }
			if (desk.getRightBookStack() > 3) { modelParts.add("rbook4"); }
			
			if (desk.getStackInSlot(9) != ItemStack.EMPTY)
			{
				modelParts.add("carpet");
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
    
    public boolean isWritingBook(float hitx, float hitz, EnumFacing face, EnumFacing angle)
    {
    	boolean returnValue = false; 
    	if (face == EnumFacing.UP)
    	{
	    	switch (angle)
	    	{
	    		case SOUTH: { if (hitz > 0.2 && hitz < 0.8 && hitx < 0.5) { returnValue = true; } break; }
	    		case WEST: { if (hitx > 0.2 && hitx < 0.8 && hitz < 0.5) { returnValue = true; } break; }
	    		case NORTH: { if (hitz > 0.2 && hitz < 0.8 && hitx > 0.5) { returnValue = true; } break; }
	    		case EAST: { if (hitx > 0.2 && hitx < 0.8 && hitz > 0.5) { returnValue = true; } break; }
	    		default: break;
	    		
	    	}
    	}
    	return returnValue;
    }
    public boolean isLeftBookStack(float hitx, float hitz, EnumFacing face, EnumFacing angle)
    {
    	boolean returnValue = false;
    	if (face == EnumFacing.UP)
    	{
	    	switch (angle)
	    	{
	    		case SOUTH: { if (hitx > 0.5 && hitz < 0.5) { returnValue = true; } break; }
	    		case WEST: { if (hitx > 0.5 && hitz > 0.5) { returnValue = true; } break; }
	    		case NORTH: { if (hitx < 0.5 && hitz > 0.5) { returnValue = true; } break; } 
	    		case EAST: { if (hitx < 0.5 && hitz < 0.5) { returnValue = true; } break; }
	    		default: break;
	    		
	    	}
    	}
    	return returnValue;
    }
    public boolean isRightBookStack(float hitx, float hitz, EnumFacing face, EnumFacing angle)
    {
    	boolean returnValue = false;
    	if (face == EnumFacing.UP)
    	{
	    	switch (angle)
	    	{
	    		case SOUTH: { if (hitx > 0.5 && hitz > 0.5) { returnValue = true; } break; }
	    		case WEST: { if (hitx < 0.5 && hitz > 0.5) { returnValue = true; } break; }
	    		case NORTH: { if (hitx < 0.5 && hitz < 0.5) { returnValue = true; } break; }
	    		case EAST: { if (hitx > 0.5 && hitz < 0.5) { returnValue = true; } break; }
	    		default: break;
	    	}
    	}
    	return returnValue;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
    {
    	if (world.isAirBlock(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

	@Override
    public TextureState addAdditionTextureStateInformation(BiblioTileEntity tile, TextureState state)
    {
		ItemStack carpet = tile.getStackInSlot(9);
		if (carpet != ItemStack.EMPTY)
		{
			state.setColorOne(EnumColor.getColorFromCarpetOrWool(carpet));
		}
		return state;
    }
	
	@Override
	public void breakBlock(World world,BlockPos pos, IBlockState state)
	{
		dropItems(world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityDesk)
		{
			TileEntityDesk currDesk = (TileEntityDesk)tile;
			if (currDesk != null)
			{
				EnumFacing angle = currDesk.getAngle();
				switch (currDesk.getSingleLeftRightCenter())
				{
					case LEFT:
					{
						//adjust desl on right
						adjustRightDesk(world, currDesk.getPos(), angle);
						break;
					}
					case RIGHT:
					{
						//adjust desk on left
						adjustLeftDesk(world, currDesk.getPos(), angle);
						break;
					}
					case CENTER:
					{
						//adjust desk on left and right
						adjustLeftDesk(world, currDesk.getPos(), angle);
						adjustRightDesk(world, currDesk.getPos(), angle);
						break;
					}
					default: break;
				}
			}
		}
		super.breakBlock(world, pos, state);
	}
	
	private void adjustLeftDesk(World world, BlockPos pos, EnumFacing angle)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		switch (angle)
		{
			case SOUTH:
			{
				z--;
				break;
			}
			case WEST:
			{
				x++;
				break;
			}
			case NORTH:
			{
				z++;
				break;
			}
			case EAST:
			{
				x--;
				break;
			}
			default: break;
		}
		
		TileEntityDesk desk = (TileEntityDesk)world.getTileEntity(new BlockPos(x, y, z));
		if (desk != null)
		{
			switch (desk.getSingleLeftRightCenter())
			{
				case LEFT:
				{
					desk.setSingleLeftRightCenter(EnumRelativeLocation.SINGLE);
					break;
				}
				case CENTER:
				{
					desk.setSingleLeftRightCenter(EnumRelativeLocation.RIGHT);
					break;
				}
				default: break;
			}
		}
	}
	
	private void adjustRightDesk(World world, BlockPos pos, EnumFacing angle)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		switch (angle)
		{
			case SOUTH:
			{
				z++;
				break;
			}
			case WEST:
			{
				x--;
				break;
			}
			case NORTH:
			{
				z--;
				break;
			}
			case EAST:
			{
				x++;
				break;
			}
			default: break;
		}
		TileEntityDesk desk = (TileEntityDesk)world.getTileEntity(new BlockPos(x, y, z));
		if (desk != null)
		{
			switch (desk.getSingleLeftRightCenter())
			{
				case RIGHT:
				{
					desk.setSingleLeftRightCenter(EnumRelativeLocation.SINGLE);
					break;
				}
				case CENTER:
				{
					desk.setSingleLeftRightCenter(EnumRelativeLocation.LEFT);
					break;
				}
				default: break;
			}
		}
	}
}
