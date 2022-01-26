package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.blocks.BiblioBlock;
import jds.bibliocraft.blocks.BlockSeat;
import jds.bibliocraft.helpers.EnumRelativeLocation;
import jds.bibliocraft.helpers.EnumShiftPosition;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioDrillText;
import jds.bibliocraft.tileentities.BiblioLightTileEntity;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import jds.bibliocraft.tileentities.TileEntityCase;
import jds.bibliocraft.tileentities.TileEntityClock;
import jds.bibliocraft.tileentities.TileEntityDesk;
import jds.bibliocraft.tileentities.TileEntityDinnerPlate;
import jds.bibliocraft.tileentities.TileEntityDiscRack;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFancyWorkbench;
import jds.bibliocraft.tileentities.TileEntityFramedChest;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPotionShelf;
import jds.bibliocraft.tileentities.TileEntityPrintPress;
import jds.bibliocraft.tileentities.TileEntitySeat;
import jds.bibliocraft.tileentities.TileEntityShelf;
import jds.bibliocraft.tileentities.TileEntitySwordPedestal;
import jds.bibliocraft.tileentities.TileEntityTable;
import jds.bibliocraft.tileentities.TileEntityToolRack;
import jds.bibliocraft.tileentities.TileEntityTypeMachine;
import jds.bibliocraft.tileentities.TileEntityTypewriter;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemDrill extends Item
{
	public static final String name = "BiblioDrill";
	public static final ItemDrill instance = new ItemDrill(name);
	
	public int useMode = 0;
	private TileEntity tile1;
	private TileEntity tile2;
	
	private static String sSelectedPainting1 = I18n.translateToLocal("screwgun.selected.painting1");
	private static String sSelectedPainting2 = I18n.translateToLocal("screwgun.selected.painting2");
	
	private static String sSelectedClock1 = I18n.translateToLocal("screwgun.selected.clock1");
	private static String sSelectedClock2 = I18n.translateToLocal("screwgun.selected.clock2");
	
	private static String sSelectedSeat1 = I18n.translateToLocal("screwgun.firstSeat");
	private static String sSelectedSeat2 = I18n.translateToLocal("screwgun.secondSeat");
	
	private static String sSelectedDesk1 = I18n.translateToLocal("screwgun.selected.desk1");
	private static String sSelectedDesk2 = I18n.translateToLocal("screwgun.selected.desk2");
	
	private static String sSelectedChest1 = I18n.translateToLocal("screwgun.selected.chest1");
	private static String sSelectedChest2 = I18n.translateToLocal("screwgun.selected.chest2");
	
	//private static String sConnected = I18n.translateToLocal("drill.connected");
	private static String sFailed = I18n.translateToLocal("drill.failed");
	
	public boolean showText = false;
	public boolean showTextChanged = false;
	public String showTextString = "";
	
	public ItemDrill(String regName)
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		maxStackSize = 1;
		setRegistryName(regName);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			playSound(player);
		}
		boolean returnValue = false;
		if (!world.isRemote)
		{
			if (player.isSneaking())
			{
				// These all rotate vanilla blocks
				if (world.getBlockState(pos).getBlock() == Blocks.PISTON || 
					world.getBlockState(pos).getBlock() == Blocks.STICKY_PISTON ||
					world.getBlockState(pos).getBlock() == Blocks.LEVER ||
					world.getBlockState(pos).getBlock() == Blocks.UNPOWERED_REPEATER ||
					world.getBlockState(pos).getBlock() == Blocks.ANVIL ||
					world.getBlockState(pos).getBlock() == Blocks.IRON_DOOR ||
					world.getBlockState(pos).getBlock() == Blocks.ACACIA_DOOR ||
					world.getBlockState(pos).getBlock() == Blocks.DARK_OAK_DOOR ||
					world.getBlockState(pos).getBlock() == Blocks.OAK_DOOR ||
					world.getBlockState(pos).getBlock() == Blocks.BIRCH_DOOR ||
					world.getBlockState(pos).getBlock() == Blocks.JUNGLE_DOOR ||
					world.getBlockState(pos).getBlock() == Blocks.SPRUCE_DOOR ||
					world.getBlockState(pos).getBlock() == Blocks.TRAPDOOR || 
					world.getBlockState(pos).getBlock() == Blocks.IRON_TRAPDOOR)
				{
					returnValue = rotateBlockState(world, pos);
				}
			}
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null)
			{
				if (tile instanceof TileEntityDispenser ||
					tile instanceof TileEntityComparator ||
					tile instanceof TileEntityChest ||
					tile instanceof TileEntityFurnace ||
					tile instanceof TileEntityEnderChest)
				{
					returnValue = rotateBlockState(world, pos);
				}
				

				
				if (player.isSneaking())
				{
					// GOES in sneaking on top
					if (tile instanceof TileEntityDiscRack ||
						tile instanceof TileEntityFancySign ||
						tile instanceof TileEntityToolRack ||
						tile instanceof TileEntityCase ||
						tile instanceof TileEntityPotionShelf ||
						tile instanceof TileEntityMapFrame ||
						tile instanceof BiblioLightTileEntity ||
						tile instanceof TileEntityShelf ||
						tile instanceof TileEntityBookcase ||
						tile instanceof TileEntityMapFrame ||
						tile instanceof TileEntityDinnerPlate ||
						tile instanceof TileEntitySwordPedestal ||
						tile instanceof TileEntityTypewriter ||
						tile instanceof TileEntityFancyWorkbench ||
						tile instanceof TileEntityTypeMachine ||
						tile instanceof TileEntityPrintPress)
					{
						returnValue = rotateBiblioBlock(world, (BiblioTileEntity)tile);
					}
					
					if (!returnValue && tile instanceof TileEntitySeat)
					{
						if (face == EnumFacing.UP)
						{
							returnValue = connectChairs(player, world, pos);
						}
						else
						{
							returnValue = rotateBiblioBlock(world, (BiblioTileEntity)tile);
						}
					}
					if (!returnValue && tile instanceof TileEntityDesk)
					{
						returnValue = connectTwoDesks(world, tile, player);
					}
					if (!returnValue && tile instanceof TileEntityArmorStand)
					{
						returnValue = rotateArmorStand(world, (BiblioTileEntity)tile); 
					}
					if (!returnValue && tile instanceof TileEntityTable)
					{
						returnValue = removeTableCarpet(world, tile, pos);
					}
					if (!returnValue && tile instanceof TileEntityClock)
					{
						returnValue = connectTwoClocks(world, tile, player);
					}
					if (!returnValue && tile instanceof TileEntityPainting)
					{
						returnValue = connectTwoPaintings(world, tile, player);
					}
					if (!returnValue && tile instanceof TileEntityFramedChest)
					{
						returnValue = connectTwoChests(world, tile, player); 
					}
					// do the rotations for blocks here and do the vert/shifts in the lower bit
				}
				else
				{
					if (!returnValue && tile instanceof BiblioLightTileEntity)
					{
						returnValue = setVertPosition((BiblioTileEntity)tile, true, true, true);
					}
					if (!returnValue && tile instanceof TileEntityCase)
					{
						returnValue = setVertPosition((BiblioTileEntity)tile, true, false, true);
					}
					if (tile instanceof TileEntityFancySign ||
						tile instanceof TileEntityShelf ||
						tile instanceof TileEntityPotionShelf ||
						tile instanceof TileEntityToolRack ||
						tile instanceof TileEntityBookcase)
					{
						returnValue = setShiftPosition((BiblioTileEntity)tile, true);
					}

					if (!returnValue && tile instanceof TileEntityClock)
					{
						returnValue = setClockShift(world, (TileEntityClock)tile);
					}
					
					if (!returnValue && tile instanceof TileEntityFramedChest)
					{
						returnValue = rotateBiblioBlock(world, (BiblioTileEntity)tile);
					}
					
					if (!returnValue && tile instanceof TileEntityDiscRack)
					{
						returnValue = rotateDiscRackOnWall((TileEntityDiscRack)tile);
					}
					
					if (!returnValue && tile instanceof TileEntityPainting)
					{
						returnValue = rotatePaintingCanvas((TileEntityPainting)tile);
					}
					if (!returnValue && tile instanceof TileEntitySeat)
					{
						returnValue = removeStuffFromSeat((TileEntitySeat)tile, player, face);
					}
				}
				if (returnValue) // I dont think I ever used this really?
				{

				}
			}
		}
		EnumActionResult result = EnumActionResult.FAIL;
		if (returnValue)
		{
			result = EnumActionResult.SUCCESS;
		}
		return result; 
	}
	
	private boolean rotateBlockState(World world, BlockPos pos)
	{
		boolean output = false;
		Block block = world.getBlockState(pos).getBlock();
		if (block != null)
		{
			block.rotateBlock(world, pos, EnumFacing.UP);
			output = true;
		}
		//if (state != null && state.getValue(EnumFacing.))
		return output;
	}
	
	private boolean removeStuffFromSeat(TileEntitySeat tile, EntityPlayer player, EnumFacing face)
	{
		boolean output = false;
		if (face == EnumFacing.UP)
		{
			if (tile.getStackInSlot(0) != ItemStack.EMPTY)
			{
				tile.removeStackFromInventoryFromWorld(0, player, BlockSeat.instance);
				tile.removeCover();
				output = true;
			}
		}
		
		if (!output && tile.hasBack > 0)
		{
			tile.removeStackFromInventoryFromWorld(1, player, BlockSeat.instance);
			tile.removeBack();
			output = true;
		}
		
		if (!output && face != EnumFacing.UP)
		{
			if (tile.isCarpetFull())
			{
				//tile.setCarpet(null);
				tile.removeStackFromInventoryFromWorld(2, player, BlockSeat.instance);
				output = true;
			}
		}
		return output;
	}
	
	private boolean rotateBiblioBlock(World world, BiblioTileEntity tile)
	{
		if (tile != null)
		{
			switch (tile.getAngle())
			{
				case SOUTH:
				{
					tile.setAngle(EnumFacing.WEST);
					break;
				}
				case WEST:
				{
					tile.setAngle(EnumFacing.NORTH);
					break;
				}
				case NORTH:
				{
					tile.setAngle(EnumFacing.EAST);
					break;
				}
				case EAST:
				{
					tile.setAngle(EnumFacing.SOUTH);
					break;
				}
				default: break;
			}
			return true;
		}
		return false;
	}
	
	private boolean setVertPosition(BiblioTileEntity tile, boolean canWall, boolean canCeiling, boolean canFloor)
	{
		if (tile != null)
		{
			EnumVertPosition currAngle = tile.getVertPosition();
			switch (currAngle)
			{
				case FLOOR:
				{
					if (canWall)
					{
						tile.setVertPosition(EnumVertPosition.WALL);
					}
					else if (canCeiling)
					{
						tile.setVertPosition(EnumVertPosition.CEILING);
					}
					break;
				}
				case WALL:
				{
					if (canCeiling)
					{
						tile.setVertPosition(EnumVertPosition.CEILING);
					}
					else if (canFloor)
					{
						tile.setVertPosition(EnumVertPosition.FLOOR);
					}
					break;
				}
				case CEILING:
				{
					if (canFloor)
					{
						tile.setVertPosition(EnumVertPosition.FLOOR);
					}
					else if (canWall)
					{
						tile.setVertPosition(EnumVertPosition.WALL);
					}
					break;
				}
			}
			return true;
		}
		return false;
	}
	
	private boolean setShiftPosition(BiblioTileEntity tile, boolean canHalfShift)
	{
		if (tile != null)
		{
			switch (tile.getShiftPosition())
			{
				case NO_SHIFT:
				{
					if (canHalfShift)
					{
						tile.setShiftPosition(EnumShiftPosition.HALF_SHIFT);
					}
					else
					{
						tile.setShiftPosition(EnumShiftPosition.FULL_SHIFT);
					}
					break;
				}
				case HALF_SHIFT:
				{
					tile.setShiftPosition(EnumShiftPosition.FULL_SHIFT);
					break;
				}
				case FULL_SHIFT:
				{
					tile.setShiftPosition(EnumShiftPosition.NO_SHIFT);
					break;
				}
			}
			return true;
		}
		return false;
	}
	
	private boolean rotatePaintingCanvas(TileEntityPainting painting)
	{
		if (painting.getPaintingRotation() < 3)
		{
			painting.setPaintingRotation(painting.getPaintingRotation() + 1);
		}
		else
		{
			painting.setPaintingRotation(0);
		}
		return true;
	}
	
	private boolean rotateDiscRackOnWall(TileEntityDiscRack tile)
	{
		if (tile != null)
		{
			tile.setWallRotation();
			return true;
		}
		return false;
	}
	
	private boolean rotateArmorStand(World world, BiblioTileEntity tile)
	{
		if (tile != null)
		{
			TileEntity otherStand = null;
			if (tile.getVertPosition() == EnumVertPosition.FLOOR)
			{
				otherStand = world.getTileEntity(new BlockPos(tile.getPos().getX(), tile.getPos().getY() + 1, tile.getPos().getZ()));
			}
			else if (tile.getVertPosition() == EnumVertPosition.CEILING)
			{
				otherStand = world.getTileEntity(new BlockPos(tile.getPos().getX(), tile.getPos().getY() - 1, tile.getPos().getZ()));
			}
			if (otherStand != null && otherStand instanceof TileEntityArmorStand)
			{
				BiblioTileEntity otherTile = (BiblioTileEntity)otherStand;
				rotateBiblioBlock(world, tile);
				rotateBiblioBlock(world, otherTile);
				return true;
			}
		}
		return false;
	}
	
	private boolean connectTwoChests(World world, TileEntity tile, EntityPlayer player)
	{
		if (tile1 == null || !(tile1 instanceof TileEntityFramedChest))
		{
			// first desk selected
			tile1 = tile;
			this.sendPacketToClient(this.sSelectedChest1, (EntityPlayerMP)player);
			return true;
		}
		else if (tile2 == null || !(tile2 instanceof TileEntityFramedChest))
		{
			tile2 = tile;
			this.sendPacketToClient(this.sSelectedChest2, (EntityPlayerMP)player);
		}
		
		if (tile1 != null && tile1 instanceof TileEntityFramedChest && tile2 != null && tile2 instanceof TileEntityFramedChest)
		{
			TileEntityFramedChest chest1 = (TileEntityFramedChest)tile1;
			TileEntityFramedChest chest2 = (TileEntityFramedChest)tile2;
			if (chest1.getAngle() == chest2.getAngle())
			{
				int test = isValidChestConnect(chest1.getAngle(), chest1.getPos().getX(), chest1.getPos().getY(), chest1.getPos().getZ(), chest2.getPos().getX(), chest2.getPos().getY(), chest2.getPos().getZ());
				if (test == 1)
				{
					if (chest1.getIsDouble() && chest1.getIsLeft() && chest2.getIsDouble() && !chest2.getIsLeft())
					{
						// already connected, disconnect them
						chest1.setIsDouble(false, false, null);
						chest2.setIsDouble(false, false, null);
					}
					else if (!chest1.getIsDouble() && !chest2.getIsDouble())
					{
						// not connected, connect them
						chest1.setIsDouble(true, true, chest2);
						chest2.setIsDouble(true, false, chest1);
					}
					else
					{
						// not good enough
						this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
					}
				}
				else if (test == 2)
				{
					//System.out.println("Chest 1 is on the right and chest 2 is on the left");
					if (chest1.getIsDouble() && !chest1.getIsLeft() && chest2.getIsDouble() && chest2.getIsLeft())
					{
						// already connected, disconnect them
						chest1.setIsDouble(false, false, null);
						chest2.setIsDouble(false, false, null);
					}
					else if (!chest1.getIsDouble() && !chest2.getIsDouble())
					{
						// not connected, connect them
						chest1.setIsDouble(true, false, chest2);
						chest2.setIsDouble(true, true, chest1);
					}
					else
					{
						// not good enough
						this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
					}
				}
				else
				{
					this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
				}
			}
			else
			{
				this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
			}
			tile1 = null;
			tile2 = null;
			return true;
		}
		
		return false;
	}
	
	// 1 is left, 2 is right
	private int isValidChestConnect(EnumFacing angle, int x1, int y1, int z1, int x2, int y2, int z2)
	{
		if (y1 == y2)
		{
			switch (angle)
			{
				case SOUTH:
				{
					if (x1 == x2 && z1 == (z2+1))
					{
						return 2;
					}
					else if (x1 == x2 && z1 == (z2-1))
					{
						return 1;
					}
				}
				case WEST:
				{
					if (z1 == z2 && x1 == (x2-1))
					{
						return 2;
					}
					else if (z1 == z2 && x1 == (x2+1))
					{
						return 1;
					}
				}
				case NORTH:
				{
					if (x1 == x2 && z1 == (z2-1))
					{
						return 2;
					}
					else if (x1 == x2 && z1 == (z2+1))
					{
						return 1;
					}
				}
				case EAST:
				{
					if (z1 == z2 && x1 == (x2+1))
					{
						return 2;
					}
					else if (z1 == z2 && x1 == (x2-1))
					{
						return 1;
					}
				}
				default: break;
			}
		}
		return 0;
	}
	
	private boolean setClockShift(World world, BiblioTileEntity tile)
	{
		if (tile != null)
		{
			setShiftPosition(tile, true);
			TileEntity otherClock = null;
			if (tile.getVertPosition() == EnumVertPosition.CEILING)
			{
				//setShiftPosition(tile, true);
				otherClock = world.getTileEntity(new BlockPos(tile.getPos().getX(), tile.getPos().getY() - 1, tile.getPos().getZ()));
	
			}
			else if (tile.getVertPosition() == EnumVertPosition.FLOOR)
			{
				
				otherClock = world.getTileEntity(new BlockPos(tile.getPos().getX(), tile.getPos().getY() + 1, tile.getPos().getZ()));
			}
			
			if (otherClock != null && otherClock instanceof TileEntityClock)
			{
				BiblioTileEntity otherTile = (BiblioTileEntity)otherClock;
				setShiftPosition(otherTile, true);
			}
			return true;
		}
		return false;
	}
	

	private boolean connectTwoDesks(World world, TileEntity tile, EntityPlayer player)
	{
		if (tile1 == null || !(tile1 instanceof TileEntityDesk))
		{
			// first desk selected
			tile1 = tile;
			this.sendPacketToClient(this.sSelectedDesk1, (EntityPlayerMP)player);
			return true;
		}
		else if (tile2 == null || !(tile2 instanceof TileEntityDesk))
		{
			tile2 = tile;
			this.sendPacketToClient(this.sSelectedDesk2, (EntityPlayerMP)player);
		}
		
		if (tile1 != null && tile1 instanceof TileEntityDesk && tile2 != null && tile2 instanceof TileEntityDesk)
		{
			TileEntityDesk desk1 = (TileEntityDesk)tile1;
			TileEntityDesk desk2 = (TileEntityDesk)tile2;
			if (desk1.getAngle() == desk2.getAngle())
			{
				if (isDeskOnRight(desk1, desk2))
				{
					connectDeskToRight(desk1);
					connectDeskToLeft(desk2);
				}
				else if (isDeskOnLeft(desk1, desk2))
				{
					//System.out.println("desk 2 is on the left of desk1");
					connectDeskToLeft(desk1);
					connectDeskToRight(desk2);
				}
				else
				{
					this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
				}
			}
			else
			{
				this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
			}
			tile1 = null;
			tile2 = null;
			return true;
		}
		
		return false;
	}
	
	private void connectDeskToRight(TileEntityDesk desk)
	{
		switch (desk.getSingleLeftRightCenter())
		{
			case SINGLE:
			{
				desk.setSingleLeftRightCenter(EnumRelativeLocation.LEFT);
				break;
			}
			case LEFT:
			{
				desk.setSingleLeftRightCenter(EnumRelativeLocation.SINGLE);
				break;
			}
			case RIGHT:
			{
				desk.setSingleLeftRightCenter(EnumRelativeLocation.CENTER);
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
	
	private void connectDeskToLeft(TileEntityDesk desk)
	{
		switch (desk.getSingleLeftRightCenter())
		{
			case SINGLE:
			{
				desk.setSingleLeftRightCenter(EnumRelativeLocation.RIGHT);
				break;
			}
			case LEFT:
			{
				desk.setSingleLeftRightCenter(EnumRelativeLocation.CENTER);
				break;
			}
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
	
	private boolean isDeskOnRight(TileEntityDesk desk1, TileEntityDesk desk2)
	{
		switch (desk1.getAngle())
		{
			case SOUTH:
			{
				if (desk1.getPos().getZ() == (desk2.getPos().getZ()-1) && desk1.getPos().getX() == desk2.getPos().getX() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			case WEST:
			{
				if (desk1.getPos().getX() == (desk2.getPos().getX()+1) && desk1.getPos().getZ() == desk2.getPos().getZ() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			case NORTH:
			{
				if (desk1.getPos().getZ() == (desk2.getPos().getZ()+1) && desk1.getPos().getX() == desk2.getPos().getX() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			case EAST:
			{
				if (desk1.getPos().getX() == (desk2.getPos().getX()-1) && desk1.getPos().getZ() == desk2.getPos().getZ() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			default: break;
		}
		return false;
	}
	
	private boolean isDeskOnLeft(TileEntityDesk desk1, TileEntityDesk desk2)
	{
		//System.out.println(desk1.getAngle());
		switch (desk1.getAngle())
		{
			case SOUTH:
			{
				if (desk1.getPos().getZ() == (desk2.getPos().getZ()+1) && desk1.getPos().getX() == desk2.getPos().getX() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			case WEST:
			{
				if (desk1.getPos().getX() == (desk2.getPos().getX()-1) && desk1.getPos().getZ() == desk2.getPos().getZ() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			case NORTH:
			{
				if (desk1.getPos().getZ() == (desk2.getPos().getZ()-1) && desk1.getPos().getX() == desk2.getPos().getX() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			case EAST:
			{
				if (desk1.getPos().getX() == (desk2.getPos().getX()+1) && desk1.getPos().getZ() == desk2.getPos().getZ() && desk1.getPos().getY() == desk2.getPos().getY())
				{
					return true;
				}
				break;
			}
			default: break;
		}
		return false;
	}
	
	private boolean connectTwoPaintings(World world, TileEntity tile, EntityPlayer player)
	{
		if (tile1 == null || !(tile1 instanceof TileEntityPainting))
		{
			tile1 = tile;
			this.sendPacketToClient(this.sSelectedPainting1, (EntityPlayerMP)player);
			return true;
		}
		else if (tile2 == null || !(tile2 instanceof TileEntityPainting))
		{
			tile2 = tile;
			this.sendPacketToClient(this.sSelectedPainting2, (EntityPlayerMP)player);
		}
		
		if (tile1 != null && tile1 instanceof TileEntityPainting && tile2 != null && tile2 instanceof TileEntityPainting)
		{
			TileEntityPainting painting1 = (TileEntityPainting)tile1;
			TileEntityPainting painting2 = (TileEntityPainting)tile2;
			if (painting1.getAngle() == painting2.getAngle())
			{
				if (tile1.getPos().getY() == (tile2.getPos().getY()+1))
				{
					// check for painting below tile1 painting
					if (painting1.getConnectBottom() && painting2.getConnectTop())
					{
						painting1.setConnectBottom(false);
						painting2.setConnectTop(false);
					}
					else
					{
						painting1.setConnectBottom(true);
						painting2.setConnectTop(true);
					}
					completedPaintingConnect(player);
				}
				else if (tile1.getPos().getY() == (tile2.getPos().getY()-1))
				{
					//check for painting above tile1 painting
					if (painting1.getConnectTop() && painting2.getConnectBottom())
					{
						painting1.setConnectTop(false);
						painting2.setConnectBottom(false);
					}
					else
					{
						painting1.setConnectTop(true);
						painting2.setConnectBottom(true);
					}
					completedPaintingConnect(player);
				}
				else if (checkLeftSidePainting(painting1, painting2))
				{
					if (painting1.getConnectLeft() && painting2.getConnectRight())
					{
						painting1.setConnectLeft(false);
						painting2.setConnectRight(false);
					}
					else
					{
						painting1.setConnectLeft(true);
						painting2.setConnectRight(true);
					}
					completedPaintingConnect(player);
				}
				else if (checkRightSidePainting(painting1, painting2))
				{
					if (painting1.getConnectRight() && painting2.getConnectLeft())
					{
						painting1.setConnectRight(false);
						painting2.setConnectLeft(false);
					}
					else
					{
						painting1.setConnectRight(true);
						painting2.setConnectLeft(true);
					}
					completedPaintingConnect(player);
				}
				else
				{
					this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
					tile1 = null;
					tile2 = null;
				}
			}
			else
			{
				this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
				tile1 = null;
				tile2 = null;
			}
		}
		return false;
	}
	
	private void completedPaintingConnect(EntityPlayer player)
	{
		//player.sendMessage(new TextComponentString("Painting Connection Successful.")); 
		//this.sendPacketToClient(this.sConnected, (EntityPlayerMP)player);
		tile1 = null;
		tile2 = null;
	}
	
	private boolean checkLeftSidePainting(TileEntityPainting centerPainting, TileEntityPainting otherPainting)
	{
		switch (centerPainting.getAngle())
		{
			case SOUTH:
			{
				if (centerPainting.getPos().getZ() == otherPainting.getPos().getZ() + 1)
				{
					return true;
				}
				break;
			}
			case WEST:
			{
				if (centerPainting.getPos().getX() == otherPainting.getPos().getX() - 1)
				{
					return true;
				}
				break;
			}
			case NORTH:
			{
				if (centerPainting.getPos().getZ() == otherPainting.getPos().getZ() - 1)
				{
					return true;
				}
				break;
			}
			case EAST:
			{
				
				if (centerPainting.getPos().getX() == otherPainting.getPos().getX() + 1)
				{
					return true;
				}
				break;
			}
			default: break;
		}
	
		return false;
	}
	
	private boolean checkRightSidePainting(TileEntityPainting centerPainting, TileEntityPainting otherPainting)
	{
		switch (centerPainting.getAngle())
		{
			case SOUTH:
			{
				if (centerPainting.getPos().getZ() == otherPainting.getPos().getZ() - 1)
				{
					return true;
				}
				break;
			}
			case WEST:
			{
				if (centerPainting.getPos().getX() == otherPainting.getPos().getX() + 1)
				{
					return true;
				}
				break;
			}
			case NORTH:
			{
				if (centerPainting.getPos().getZ() == otherPainting.getPos().getZ() + 1)
				{
					return true;
				}
				break;
			}
			case EAST:
			{
				if (centerPainting.getPos().getX() == otherPainting.getPos().getX() - 1)
				{
					return true;
				}
				break;
			}
			default: break;
		}
		return false;
	}
	
	private boolean connectTwoClocks(World world, TileEntity tile, EntityPlayer player)
	{
		if (tile1 == null || !(tile1 instanceof TileEntityClock))
		{
			tile1 = tile;
			this.sendPacketToClient(this.sSelectedClock1, (EntityPlayerMP)player);
			return true;
		}
		else if (tile2 == null || !(tile2 instanceof TileEntityClock))
		{
			tile2 = tile;
			this.sendPacketToClient(this.sSelectedClock2, (EntityPlayerMP)player);
		}
		
		if (tile1 != null && tile1 instanceof TileEntityClock && tile2 != null && tile2 instanceof TileEntityClock)
		{
			// we have 2 clock tiles, time to try to connect and reset to null after attempt.
			if (tile1.getPos().getY() == (tile2.getPos().getY()+1))
			{
				// tile 1 is on top
				TileEntityClock clockTop = (TileEntityClock)tile1;
				TileEntityClock clockBottom = (TileEntityClock)tile2;
				if (configureClocks(clockTop, clockBottom))
				{
					//this.sendPacketToClient(this.sConnected, (EntityPlayerMP)player);
				}
				else
				{
					this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
				}
				tile1 = null;
				tile2 = null;
				return true;
			}
			else if (tile1.getPos().getY() == (tile2.getPos().getY()-1))
			{
				// tile 1 is on bottom
				TileEntityClock clockTop = (TileEntityClock)tile2;
				TileEntityClock clockBottom = (TileEntityClock)tile1;
				if (configureClocks(clockTop, clockBottom))
				{
					//this.sendPacketToClient(this.sConnected, (EntityPlayerMP)player);
				}
				else
				{
					this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
				}
				tile1 = null;
				tile2 = null;
				return true;
			}
			else
			{
				// failed the connect
				//player.sendMessage(new TextComponentString("Failed to connect"));
				this.sendPacketToClient(this.sFailed, (EntityPlayerMP)player);
				tile1 = null;
				tile2 = null;
			}

		}
		return false;
	}

	public boolean configureClocks(TileEntityClock top, TileEntityClock bottom)
	{
		if (top.getVertPosition() == EnumVertPosition.WALL && bottom.getVertPosition() == EnumVertPosition.WALL && top.getShiftPosition() == bottom.getShiftPosition())
		{
			// we have two single clocks we are to connect
			top.setVertPosition(EnumVertPosition.CEILING);
			bottom.setVertPosition(EnumVertPosition.FLOOR);
			return true;
		}
		else if (top.getVertPosition() == EnumVertPosition.CEILING && bottom.getVertPosition() == EnumVertPosition.FLOOR && top.getShiftPosition() == bottom.getShiftPosition())
		{
			//reset to single clocks
			top.setVertPosition(EnumVertPosition.WALL);
			bottom.setVertPosition(EnumVertPosition.WALL);
			return true;
		}
		else
		{
			return false;
		}
	}

	public void playSound(EntityPlayer player)
	{
		player.playSound(CommonProxy.SOUND_ITEM_SCREWGUN, 0.7F, 1.0F);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
		if (!player.isSneaking())
		{
			if (tile1 != null || tile2 != null)
			{
				tile1 = null;
				tile2 = null;
			}
		}
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
	
	public boolean removeTableCarpet(World world, TileEntity tile, BlockPos pos)
	{
		TileEntityTable tableTile = (TileEntityTable)world.getTileEntity(pos);
		if (tableTile != null)
		{
			EntityPlayer nullPlayer = null;
			tableTile.removeStackFromInventoryFromWorld(2, null, (BiblioBlock)world.getBlockState(pos).getBlock());
			//dropCarpet(world, i, j, k, 2);
			//tableTile.setCarpet(null);
			return true;
		}
		return false;
	}
	
	private void dropCarpet(World world, BlockPos pos, int slot)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(!(tileEntity instanceof IInventory))
		{
			return;
		}
		
		IInventory inventory = (IInventory) tileEntity;
		TileEntityTable tableTile = (TileEntityTable) tileEntity;
		ItemStack stuff;
		if (tableTile.isSlotFull() && slot == 0)
		{
			stuff = inventory.getStackInSlot(0);
		}
		else if (tableTile.isClothSlotFull() && slot == 1)
		{
			stuff = inventory.getStackInSlot(1);
		}
		else if (tableTile.isCarpetFull() && slot == 2)
		{
			stuff = inventory.getStackInSlot(2);
		}
		else
		{
			stuff = ItemStack.EMPTY;
		}
		
		if (stuff != ItemStack.EMPTY && stuff.getCount() > 0)
		{
			float iAdjust = 0;
			float kAdjust;
			//System.out.println(caseTile.getAngle());

			EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5F, pos.getY() + 1.4F, pos.getZ() + 0.5F, new ItemStack(stuff.getItem(), stuff.getCount(), stuff.getItemDamage()));
			
			if (stuff.hasTagCompound())
			{
				entityItem.getItem().setTagCompound((NBTTagCompound) stuff.getTagCompound().copy());
			}
			entityItem.motionX = 0;
			entityItem.motionY = 0;
			entityItem.motionZ = 0;
			world.spawnEntity(entityItem);
			stuff.setCount(0);
		}
	}
	
	public boolean connectChairs(EntityPlayer player, World world, BlockPos pos)
	{
		if (player.isSneaking())
		{
			if (tile1 == null)
			{
				
				tile1 = world.getTileEntity(pos);
				if (tile1 != null)
				{
					if (tile1 instanceof TileEntitySeat)
					{
						this.sendPacketToClient(this.sSelectedSeat1, (EntityPlayerMP)player);
						return true;
					}
					else
					{
						tile1 = null;
						tile2 = null;
						return false;
					}
				}
			}

			if (tile1 != null && tile1 instanceof TileEntitySeat)
			{
				//System.out.println("First tile test passed!");
				if (tile1.getPos().getX() != pos.getX() || tile1.getPos().getY() != pos.getY() || tile1.getPos().getZ() != pos.getZ())
				{
					
					tile2 = world.getTileEntity(pos);
					if (tile2 != null)
					{
						if (tile2 instanceof TileEntitySeat)
						{
							// should do this in the packet handler
							// send a packet here
							//player.sendMessage(new TextComponentString(I18n.translateToLocal("screwgun.secondSeat"))); 
							//this.showTextString = this.sSelectedSeat2;
							this.sendPacketToClient(this.sSelectedSeat2, (EntityPlayerMP)player);
							//System.out.println("we got the Second tile");
							int diffX = tile1.getPos().getX() - tile2.getPos().getX();
							int diffY = tile1.getPos().getY() - tile2.getPos().getY();
							int diffZ = tile1.getPos().getZ() - tile2.getPos().getZ();
							setChairConnects((TileEntitySeat)tile1, (TileEntitySeat)tile2, diffX, diffY, diffZ);
							tile1 = null;
							tile2 = null;
							return true;
						}
						else
						{
							//System.out.println("not a seat!, resetting");
							tile1 = null;
							tile2 = null;
							return false;
						}
					}
				}
				
			}
			else
			{
				tile1 = null;
				tile2 = null;
			}
		}
		return false;
	}

	
	public void setChairConnects(TileEntitySeat seatTile1, TileEntitySeat seatTile2, int diffX, int diffY, int diffZ)
	{
		if (diffY != 0)
		{
			//System.out.println("seats are not on same plane!");
			return;
		}
		if (diffX == 1 && diffZ == 0)
		{
			// tile 2 = east connect
			// tile 1 = west connect
			if (!seatTile1.getWestConnect())
			{
				seatTile1.setWestConnect(true);
			}
			else
			{
				seatTile1.setWestConnect(false);
			}
			if (!seatTile2.getEastConnect())
			{
				seatTile2.setEastConnect(true);
			}
			else
			{
				seatTile2.setEastConnect(false);
			}
		}
		else if (diffX == -1 && diffZ == 0)
		{
			// tile2 = west connect
			// tile1 = east connect
			if (!seatTile1.getEastConnect())
			{
				seatTile1.setEastConnect(true);
			}
			else
			{
				seatTile1.setEastConnect(false);
			}
			if (!seatTile2.getWestConnect())
			{
				seatTile2.setWestConnect(true);
			}
			else
			{
				seatTile2.setWestConnect(false);
			}
		}
		else if (diffZ == 1 && diffX == 0)
		{
			// tile2 = south connect
			// tile1 = north connect
			if (!seatTile1.getNorthConnect())
			{
				seatTile1.setNorthConnect(true);
			}
			else
			{
				seatTile1.setNorthConnect(false);
			}
			if (!seatTile2.getSouthConnect())
			{
				seatTile2.setSouthConnect(true);
			}
			else
			{
				seatTile2.setSouthConnect(false);
			}
		}
		else if (diffZ == -1 && diffX == 0)
		{
			// tile2 = north connect
			// tile1 = south connect
			if (!seatTile1.getSouthConnect())
			{
				seatTile1.setSouthConnect(true);
			}
			else
			{
				seatTile1.setSouthConnect(false);
			}
			if(!seatTile2.getNorthConnect())
			{
				seatTile2.setNorthConnect(true);
			}
			else
			{
				seatTile2.setNorthConnect(false);
			}
		}
	}
	
	public void setMode()
	{
		if (useMode >= 2)
		{
			useMode = 0;
		}
		else
		{
			useMode++;
		}
	}
	
	private void sendPacketToClient(String displayString, EntityPlayerMP player)
	{
		BiblioNetworking.INSTANCE.sendTo(new BiblioDrillText(displayString), player);
		// ByteBuf buffer = Unpooled.buffer();
		// ByteBufUtils.writeUTF8String(buffer, displayString);
		// BiblioCraft.ch_BiblioDrillText.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioDrillText"), player);
	}
	
	public void updateFromPacket(String displayString)
	{
		this.showText = true;
		this.showTextChanged = true;
		this.showTextString = displayString;
	}
	
}
