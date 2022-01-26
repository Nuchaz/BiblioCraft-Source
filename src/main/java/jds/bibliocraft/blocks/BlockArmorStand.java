package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockArmorStand extends BiblioWoodBlock {
	public static final BlockArmorStand instance = new BlockArmorStand();
	public static final String name = "ArmorStand";

	public BlockArmorStand() {
		super(name, false);
	}

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote && te != null && te instanceof TileEntityArmorStand) {
			TileEntityArmorStand tile = (TileEntityArmorStand) te;
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			boolean isPoweredBottom;
			boolean isPowerTop;
			int yCheck = (int) (hitY * 2);
			if (!tile.getIsBottomStand()) {
				tile = (TileEntityArmorStand) world.getTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
				if (tile == null)
					return false;
				isPowerTop = world.isBlockIndirectlyGettingPowered(pos) > 0;
				isPoweredBottom = world
						.isBlockIndirectlyGettingPowered(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())) > 0;
				yCheck += 2;
			} else {
				isPoweredBottom = world.isBlockIndirectlyGettingPowered(pos) > 0;
				isPowerTop = world
						.isBlockIndirectlyGettingPowered(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())) > 0;
			}

			if (player.isSneaking()) {
				if (isPoweredBottom || isPowerTop) {
					handleArmorTransation(player, tile, 0);
					handleArmorTransation(player, tile, 1);
					handleArmorTransation(player, tile, 2);
					handleArmorTransation(player, tile, 3);
					return true;
				}

				if (yCheck >= 0 && yCheck <= 3) {
					handleArmorTransation(player, tile, yCheck);
				}
				return true;
			}

			if (playerhand != ItemStack.EMPTY) {
				Item stackItem = playerhand.getItem();
				if (stackItem instanceof ItemArmor) {
					ItemArmor armorItem = (ItemArmor) stackItem;
					EntityEquipmentSlot armorType = armorItem.armorType;
					if ((yCheck == 0 && armorType == EntityEquipmentSlot.FEET) ||
							(yCheck == 1 && armorType == EntityEquipmentSlot.LEGS) ||
							(yCheck == 2 && armorType == EntityEquipmentSlot.CHEST) ||
							(yCheck == 3 && armorType == EntityEquipmentSlot.HEAD)) {
						if (tile.addArmor(playerhand, armorType)) {
							player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
							return true;
						}
					}
				}
			}

			player.openGui(BiblioCraft.instance, 1, world, tile.getPos().getX(), tile.getPos().getY(),
					tile.getPos().getZ());

		}
		return true;
	}

	/**
	 * For armor type, 0 = feet, 1 = legs, 2 = chest, 3 = head
	 * 
	 * @param player
	 * @param armorTile
	 * @param armortype
	 */
	private void handleArmorTransation(EntityPlayer player, TileEntityArmorStand armorTile, int armortype) {
		ItemStack playerArmor = player.inventory.armorInventory.get(armortype);
		int atilearmor = -1;
		switch (armortype) {
			case 0: {
				atilearmor = 3;
				break;
			}
			case 1: {
				atilearmor = 2;
				break;
			}
			case 2: {
				atilearmor = 1;
				break;
			}
			case 3: {
				atilearmor = 0;
				break;
			}
			default:
				break;
		}
		if (atilearmor != -1 && armortype >= 0 && armortype < 4) {
			ItemStack standArmor = armorTile.getStackInSlot(atilearmor);// getArmor(atilearmor);
			// ItemStack plegcopy = null;
			// ItemStack alegcopy = null;
			/*
			 * if (playerArmor != null)
			 * {
			 * plegcopy = playerArmor.copy();
			 * }
			 * if (standArmor != null)
			 * {
			 * alegcopy = standArmor.copy();
			 * }
			 */
			if (standArmor != ItemStack.EMPTY) {
				player.inventory.armorInventory.set(armortype, standArmor);
				// sendPlayerArmorPacket(player, alegcopy, armortype);
			} else {
				player.inventory.armorInventory.set(armortype, ItemStack.EMPTY);
			}

			if (playerArmor != ItemStack.EMPTY) {
				armorTile.setInventorySlotContents(atilearmor, playerArmor);
			} else {
				armorTile.setInventorySlotContents(atilearmor, ItemStack.EMPTY);
			}
			/*
			 * if (alegcopy == null)
			 * {
			 * player.inventory.armorInventory[armortype] = null;
			 * //sendPlayerArmorPacket(player, alegcopy, armortype); // I'm not sure I
			 * actually need these packets to the client anymore
			 * }
			 * if (playerArmor == null)
			 * {
			 * armorTile.setInventorySlotContents(atilearmor, null);
			 * }
			 */
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityArmorStand();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) {
		List<String> modelParts = new ArrayList<String>();
		if (tile != null && tile instanceof TileEntityArmorStand) {
			TileEntityArmorStand te = (TileEntityArmorStand) tile;
			if (te.getIsBottomStand()) {
				modelParts.add("bottomStand");
				modelParts.add("topStand");
			}
		}
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity tile, EntityLivingBase player) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos()); // TODO changed this and it worked on the meta
																			// data
		BlockPos pos = new BlockPos(tile.getPos().getX(), tile.getPos().getY() + 1, tile.getPos().getZ());
		tile.getWorld().setBlockState(pos, state);
		TileEntity te = tile.getWorld().getTileEntity(pos);
		if (te != null && te instanceof TileEntityArmorStand) {
			TileEntityArmorStand stand = (TileEntityArmorStand) te;
			stand.setCustomTexureString(tile.getCustomTextureString());
			stand.setIsBottomStand(false);
			stand.setAngle(tile.getAngle());
		}

	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) {
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f),
				new Quat4f(0.0f, 0.0f, 0.0f, 1.0f),
				new Vector3f(1.0f, 1.0f, 1.0f),
				new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
		return transform;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		if (world.isAirBlock(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity t = world.getTileEntity(pos);
		if (t != null && t instanceof TileEntityArmorStand) {
			TileEntityArmorStand tile = (TileEntityArmorStand) t;
			BlockPos newPos;
			if (tile.getIsBottomStand()) {
				newPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
			} else {
				newPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
				BiblioTileEntity top = (BiblioTileEntity) t;
				BiblioTileEntity bottom = (BiblioTileEntity) world.getTileEntity(newPos);
				top.setCustomTexureString(bottom.getCustomTextureString());
			}
			TileEntity sTile = world.getTileEntity(newPos);
			if (sTile != null && sTile instanceof TileEntityArmorStand) {

				dropItems(world, newPos);
				world.setBlockToAir(newPos);
			}
		}

		dropItems(world, pos);
		super.breakBlock(world, pos, state);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tile = blockAccess.getTileEntity(pos);
		if (tile != null && tile instanceof BiblioTileEntity) {
			BiblioTileEntity caseTile = (BiblioTileEntity) tile;
			if (caseTile.getAngle() == EnumFacing.SOUTH || caseTile.getAngle() == EnumFacing.NORTH) {
				output = this.getBlockBounds(0.3F, 0.0F, 0.0F, 0.7F, 1.0F, 1.0F);
			} else {
				output = this.getBlockBounds(0.0F, 0.0F, 0.3F, 1.0F, 1.0F, 0.7F);
			}
		}
		return output;
	}
}
