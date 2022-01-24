package jds.bibliocraft.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockMarkerPole;
import jds.bibliocraft.blocks.BlockTable;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.helpers.FileUtil;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemStockroomCatalog;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import jds.bibliocraft.tileentities.TileEntityClock;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFancyWorkbench;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import jds.bibliocraft.tileentities.TileEntityMarkerPole;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityTypeMachine;
import jds.bibliocraft.tileentities.TileEntityDesk;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class ServerPacketHandler {
	public ServerPacketHandler() {

	}

	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event) {
		EntityPlayerMP player = ((NetHandlerPlayServer) event.getHandler()).player;
		FMLProxyPacket packet = event.getPacket();

		if (packet != null) {
			if (packet.channel().equals("BiblioType")) {
				handleBookNameUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioTypeFlag")) {
				handleBookFlagUpdate(packet.payload());
			}
			if (packet.channel().equals("BiblioTypeDelete")) {
				handleBookDeletion(packet.payload());
			}
			if (packet.channel().equals("BiblioTypeUpdate")) {
				handleTypsetUpdate(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioMCBEdit")) {
				handleBookEdit(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioMCBPage")) {
				handleBookPageUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioUpdateInv")) {
				handleInventoryStackUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioMeasure")) {
				handleMarkerPoles(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioMapPin")) {
				handleMapWaypoints(packet.payload(), player);
				// packet.toS3FPacket()
			}
			if (packet.channel().equals("BiblioRBook")) {
				handleRecipeBook(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioRBookLoad")) {
				handleRecipeLoad(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioSign")) {
				handleFancySignUpdate(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioClock")) {
				handleClockUpdate(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioPaintPress")) {
				handlePaintPressUpdate(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioPainting")) {
				handlePaintingUpdate(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioPaintingC")) {
				handlePaintingCustomAspectsUpdate(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioAtlas")) {
				handleAtlasUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioAtlasSWP")) {
				handleAtlasSwapUpdate(packet.payload(), player);
			}
			// if (packet.channel().equals("BiblioAtlasWPT"))
			// {
			// handleAtlasTransferUpdate(packet.payload(), player);
			// }
			if (packet.channel().equals("BiblioPaneler")) {
				handlePanelerTextureStringUpdate(packet.payload(), player.world);
			}
			if (packet.channel().equals("BiblioRecipeCraft")) {
				handleRecipeBookRecipeCraft(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioStockTitle")) {
				// update the title of the stockroom catalog
				handleStockroomCatalogTitle(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioStockCompass")) {
				// update the compass on the stockroom catalog
				handleStockroomCatalogCompass(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioClipboard")) {
				handleClipboardBlockUpdate(packet.payload(), player);
			}
		}
	}

	private void handleClipboardBlockUpdate(ByteBuf packet, EntityPlayerMP player) {
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		int updatePos = packet.readInt();
		TileEntity tile = player.world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityClipboard) {
			TileEntityClipboard clipboard = (TileEntityClipboard) tile;
			clipboard.updateClipboardFromPlayerSelection(updatePos);
		}
	}

	private void handleStockroomCatalogTitle(ByteBuf packet, EntityPlayerMP player) {
		String title = ByteBufUtils.readUTF8String(packet);
		ItemStack stockroomcatalog = player.getHeldItem(EnumHand.MAIN_HAND);
		if (stockroomcatalog != ItemStack.EMPTY && stockroomcatalog.getItem() instanceof ItemStockroomCatalog) {
			NBTTagCompound tags = stockroomcatalog.getTagCompound();
			if (tags == null) {
				tags = new NBTTagCompound();
			}
			NBTTagCompound display = new NBTTagCompound();
			display.setString("Name", TextFormatting.WHITE + title);
			tags.setTag("display", display);
			stockroomcatalog.setTagCompound(tags);
			player.inventory.setInventorySlotContents(player.inventory.currentItem, stockroomcatalog);
		}
	}

	private void handleStockroomCatalogCompass(ByteBuf packet, EntityPlayerMP player) {
		int slotNumber = packet.readInt();
		String title = ByteBufUtils.readUTF8String(packet);
		int x = packet.readInt();
		int z = packet.readInt();
		if (slotNumber < player.inventory.getSizeInventory()) {
			ItemStack compass = player.inventory.getStackInSlot(slotNumber);
			if (compass != ItemStack.EMPTY && compass.getItem() instanceof ItemWaypointCompass) {
				NBTTagCompound tags = compass.getTagCompound();
				if (tags == null) {
					tags = new NBTTagCompound();
				}
				tags.setInteger("XCoord", x);
				tags.setInteger("ZCoord", z);
				tags.setString("WaypointName", title);
				compass.setTagCompound(tags);
				player.inventory.setInventorySlotContents(slotNumber, compass);
			}
		}
	}

	private void handleRecipeBookRecipeCraft(ByteBuf packet, EntityPlayerMP player) {
		ItemStack recipeBook = ByteBufUtils.readItemStack(packet);
		int inventorySlot = packet.readInt();
		if (Config.enableRecipeBookCrafting) {
			if (recipeBook != ItemStack.EMPTY && recipeBook.getItem() instanceof ItemRecipeBook) {
				NonNullList<ItemStack> bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);// new
																										// ItemStack[9];
				ItemStack resultStack = ItemStack.EMPTY;
				NBTTagCompound nbt = recipeBook.getTagCompound();
				if (nbt != null) {
					NBTTagList tagList = nbt.getTagList("grid", Constants.NBT.TAG_COMPOUND);
					bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
					for (int i = 0; i < 9; i++) {
						NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
						byte slot = tag.getByte("Slot");
						if (slot >= 0 && slot < 9) {
							ItemStack nbtStack = new ItemStack(tag);
							if (nbtStack != ItemStack.EMPTY) {
								bookGrid.set(slot, nbtStack);
							}
						}
					}
					NBTTagCompound resultTag = nbt.getCompoundTag("result");
					if (resultTag != null) {
						resultStack = new ItemStack(resultTag);
					}
				}

				if (resultStack != ItemStack.EMPTY) {
					if (checkForValidRecipeIngredients(bookGrid, player, false)) {
						Container contained = new Container() {
							@Override
							public boolean canInteractWith(EntityPlayer p_75145_1_) {
								return false;
							}
						};
						InventoryCrafting playerCraftMatrix = new InventoryCrafting(contained, 3, 3);
						for (int i = 0; i < bookGrid.size(); i++) {
							playerCraftMatrix.setInventorySlotContents(i, bookGrid.get(i));
						}

						ItemStack result = CraftingManager.findMatchingResult(playerCraftMatrix, player.world);
						if (result != ItemStack.EMPTY) {
							if (checkForValidRecipeIngredients(bookGrid, player, true)) // remove valid ingredients from
																						// inventory
							{
								if (!(player.inventory.addItemStackToInventory(result.copy()))) {
									EntityItem entityItem = new EntityItem(player.world, player.posX, player.posY,
											player.posZ,
											new ItemStack(result.getItem(), result.getCount(), result.getItemDamage()));
									if (result.hasTagCompound()) {
										entityItem.getItem()
												.setTagCompound((NBTTagCompound) result.getTagCompound().copy());
									}
									entityItem.motionX = 0;
									entityItem.motionY = 0;
									entityItem.motionZ = 0;
									player.world.spawnEntity(entityItem);
								}
								sendARecipeBookTextPacket(player,
										result.getDisplayName() + " " + I18n.translateToLocal("gui.recipe.crafted"),
										inventorySlot);
							} else {
								sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.failed"),
										inventorySlot);
							}
							return;
						} else {
							sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.invalid"),
									inventorySlot);
							return;
						}
					} else {
						sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.missing"), inventorySlot);
						return;
					}
				} else {
					sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.invalid"), inventorySlot);
					return;
				}
			}
			sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.wrong"), inventorySlot);
		} else {
			sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.disabled"), inventorySlot);
		}
	}

	private boolean checkForValidRecipeIngredients(NonNullList<ItemStack> ingredients, EntityPlayerMP player,
			boolean remove) {
		if (player.capabilities.isCreativeMode) {
			remove = false;
		}
		boolean[] passed = { false, false, false,
				false, false, false,
				false, false, false };
		NonNullList<ItemStack> inventory = player.inventory.mainInventory;
		NonNullList<ItemStack> playerInventory = inventory;
		NonNullList<ItemStack> playerIngredients = NonNullList.<ItemStack>withSize(ingredients.size(), ItemStack.EMPTY);
		for (int i = 0; i < ingredients.size(); i++) {
			playerIngredients.set(i, ingredients.get(i));
		}
		NonNullList<ItemStack> countedIngredients = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
		for (int i = 0; i < playerIngredients.size(); i++) {
			ItemStack thing = playerIngredients.get(i);
			if (thing != ItemStack.EMPTY) {
				int count = 0;
				for (int n = 0; n < playerIngredients.size(); n++) {
					ItemStack subThing = playerIngredients.get(n);
					if (subThing.getUnlocalizedName().equals(thing.getUnlocalizedName())) {
						count++;
						playerIngredients.set(n, ItemStack.EMPTY);
					}
				}
				thing.setCount(count);
				countedIngredients.set(i, thing);
			}
		}
		for (int i = 0; i < countedIngredients.size(); i++) {
			ItemStack ingredientItem = countedIngredients.get(i);
			if (ingredientItem != ItemStack.EMPTY
					&& !ingredientItem.getUnlocalizedName().contentEquals(ItemStack.EMPTY.getUnlocalizedName())) {
				for (int n = 0; n < playerInventory.size(); n++) {
					ItemStack inventoryItem = playerInventory.get(n);
					if (inventoryItem != ItemStack.EMPTY
							&& inventoryItem.getUnlocalizedName().equals(ingredientItem.getUnlocalizedName())) {
						if (inventoryItem.getCount() >= ingredientItem.getCount()) {
							if (remove) {
								inventoryItem.setCount(inventoryItem.getCount() - ingredientItem.getCount());
								if (inventoryItem.getCount() <= 0) {
									inventory.set(n, ItemStack.EMPTY);
								} else {
									inventory.set(n, inventoryItem);
								}
							}
							passed[i] = true;
							break;
						} else {
							inventoryItem.setCount(ingredientItem.getCount() - inventoryItem.getCount());
							countedIngredients.set(i, ingredientItem);// [i] = ingredientItem;
							if (remove) {
								inventory.set(n, ItemStack.EMPTY);
							}
						}
					}
				}
			} else {
				passed[i] = true;
			}
		}
		boolean hasIngredients = true;
		for (int m = 0; m < passed.length; m++) {
			if (!passed[m]) {
				hasIngredients = false;
			}
		}
		if (player.capabilities.isCreativeMode) {
			hasIngredients = true;
		}
		return hasIngredients;
	}

	private void sendARecipeBookTextPacket(EntityPlayerMP player, String text, int slot) {
		ItemStack currentBook = player.inventory.getStackInSlot(slot);
		if (currentBook != ItemStack.EMPTY) {
			if (currentBook.getItem() instanceof ItemRecipeBook) {
				ByteBuf buffer = Unpooled.buffer();
				ByteBufUtils.writeUTF8String(buffer, text);
				buffer.writeInt(slot);
				BiblioCraft.ch_BiblioRecipeText.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioRecipeText"),
						player);
			}
		}
	}

	private void handlePanelerTextureStringUpdate(ByteBuf packet, World world) {
		String texName = ByteBufUtils.readUTF8String(packet);
		int i = packet.readInt();
		int j = packet.readInt();
		int k = packet.readInt();
		TileEntity tile = world.getTileEntity(new BlockPos(i, j, k));
		if (tile != null && tile instanceof TileEntityFurniturePaneler) {
			TileEntityFurniturePaneler paneler = (TileEntityFurniturePaneler) tile;
			paneler.setCustomCraftingTex(texName);
		}
	}

	private void handlePaintingCustomAspectsUpdate(ByteBuf packet, World world) {
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		int aspectX = packet.readInt();
		int aspectY = packet.readInt();

		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityPainting) {
			TileEntityPainting painting = (TileEntityPainting) tile;
			painting.setPacketAspectsUpdate(aspectX, aspectY);
		}
	}

	private void handleAtlasSwapUpdate(ByteBuf packet, EntityPlayerMP player) {
		handleInventoryStackUpdate(packet, player);
		player.closeScreen();
		player.openGui(BiblioCraft.instance, 100, player.world, (int) player.posX, (int) player.posY,
				(int) player.posZ);
	}

	private void handleAtlasUpdate(ByteBuf packet, EntityPlayerMP playermp) {
		final boolean autoCenter = packet.readBoolean();
		final boolean autoCreate = packet.readBoolean();
		final int zoomLevel = packet.readInt();
		final int slot = packet.readInt();
		final boolean changeGUI = packet.readBoolean();
		final EntityPlayerMP player = playermp;
		ItemStack atlas = playermp.getHeldItem(EnumHand.MAIN_HAND); // I have to get the item and update it since the
																	// container also does an update.
		if (atlas != ItemStack.EMPTY && atlas.getItem() instanceof ItemAtlas) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ItemStack atlas = player.getHeldItem(EnumHand.MAIN_HAND);
					NBTTagCompound tags = atlas.getTagCompound();
					if (tags != null) {
						// int oldSlot = tags.getInteger("mapSlot");
						tags.setBoolean("autoCenter", autoCenter);
						tags.setBoolean("autoCreate", autoCreate);
						tags.setInteger("zoomLevel", zoomLevel);
						tags.setInteger("mapSlot", slot);
						tags.setInteger("lastGUImode", 0);
						atlas.setTagCompound(tags);
						atlas.setItemDamage(ItemAtlas.setAtlasDamage(atlas, slot));
						player.inventory.setInventorySlotContents(player.inventory.currentItem, atlas);
					}
					if (changeGUI) {
						ItemAtlas realAtlas = (ItemAtlas) atlas.getItem();
						realAtlas.getAtlasInventory(atlas, player.world, player, autoCreate, autoCenter, zoomLevel,
								slot);
						player.closeScreen();
						player.rotationPitch = 50.0f;
						ByteBuf buffer = Unpooled.buffer();
						ByteBufUtils.writeItemStack(buffer, player.getHeldItem(EnumHand.MAIN_HAND));
						BiblioCraft.ch_BiblioAtlasGUIswap
								.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlasSWP"), player);
					}
				}
			});

		}
	}

	private void handlePaintingUpdate(ByteBuf packet, World world) {
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();

		int corner = packet.readInt();
		int scale = packet.readInt();
		int res = packet.readInt();
		int aspect = packet.readInt();

		int rotation = packet.readInt();

		int customAspectX = packet.readInt();
		int customAspectY = packet.readInt();

		boolean hideFrame = packet.readBoolean();
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityPainting) {
			TileEntityPainting painting = (TileEntityPainting) tile;
			painting.setHideFrame(hideFrame);
			painting.setPacketUpdate(corner, scale, res, aspect, rotation, customAspectX, customAspectY);

		}
	}

	private void handlePaintPressUpdate(ByteBuf packet, World world) {
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();

		int artType = packet.readInt();
		String artName = ByteBufUtils.readUTF8String(packet);

		boolean applyToCanvas = packet.readBoolean();

		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityPaintPress) {
			TileEntityPaintPress press = (TileEntityPaintPress) tile;
			press.setSelectedPainting(artType, artName);
			if (applyToCanvas) {
				press.setCycle(true);
			}
		}
	}

	private void handleClockUpdate(ByteBuf packet, World world) {
		NBTTagCompound tags = ByteBufUtils.readTag(packet);
		if (tags != null) {
			int[] chimes = tags.getIntArray("chimes");
			int[] redstone = tags.getIntArray("redstone");

			boolean tick = packet.readBoolean();
			boolean chime = packet.readBoolean();
			boolean rsout = packet.readBoolean();
			boolean rspulse = packet.readBoolean();

			int i = packet.readInt();
			int j = packet.readInt();
			int k = packet.readInt();

			TileEntity tile = world.getTileEntity(new BlockPos(i, j, k));
			if (tile != null && tile instanceof TileEntityClock) {
				TileEntityClock clock = (TileEntityClock) tile;
				clock.setSettingFromGui(chimes, redstone, tick, chime, rsout, rspulse);
			}
		}
	}

	private void handleFancySignUpdate(ByteBuf packet, World world) {
		String[] text = new String[15];
		int[] textScales = new int[15];
		for (int n = 0; n < 15; n++) {
			text[n] = ByteBufUtils.readUTF8String(packet);
			textScales[n] = packet.readInt();
		}
		int numOfLines = packet.readInt();
		int s1Scale = packet.readInt();
		int s1Rot = packet.readInt();
		int s2Scale = packet.readInt();
		int s2Rot = packet.readInt();
		int s1x = packet.readInt();
		int s1y = packet.readInt();
		int s2x = packet.readInt();
		int s2y = packet.readInt();
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null) {
			if (tile instanceof TileEntityFancySign) {
				TileEntityFancySign sign = (TileEntityFancySign) tile;
				sign.updateFromPacket(text, textScales, numOfLines, s1Scale, s1Rot, s1x, s1y, s2Scale, s2Rot, s2x, s2y);
			}
		}
	}

	private void handleRecipeLoad(ByteBuf packet, World world) {
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		int id = packet.readInt();
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityFancyWorkbench) {
			TileEntityFancyWorkbench bench = (TileEntityFancyWorkbench) tile;
			// bench.setBookGrid();
			bench.loadInvToGridForRecipe(id);
		}
	}

	private void handleRecipeBook(ByteBuf packet, World world) {
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		int id = packet.readInt();
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityFancyWorkbench) {
			TileEntityFancyWorkbench bench = (TileEntityFancyWorkbench) tile;
			bench.setBookGrid(id);
		}
	}

	private void handleTypsetUpdate(ByteBuf packet, World world) {
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityTypeMachine) {
			TileEntityTypeMachine typeTile = (TileEntityTypeMachine) tile;
			typeTile.booklistset();
		}
	}

	private void handleBookNameUpdate(ByteBuf packet, EntityPlayerMP player) {
		String bookname;
		int x, y, z;
		bookname = ByteBufUtils.readUTF8String(packet);// inputStream.readUTF();
		x = packet.readInt();
		y = packet.readInt();
		z = packet.readInt();
		updateTypeMachine(x, y, z, bookname, player);
	}

	private void updateTypeMachine(int x, int y, int z, String book, EntityPlayerMP player) {
		World world = player.world;
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null) {
			TileEntityTypeMachine typetile = (TileEntityTypeMachine) tile;
			typetile.setBookname(book);
		}
	}

	private void handleBookFlagUpdate(ByteBuf packet) {
		String bookname = ByteBufUtils.readUTF8String(packet);
		boolean newFlag = packet.readBoolean();
		boolean isServer = packet.readBoolean();
		FileUtil util = new FileUtil();
		if (util.updatePublicFlag(!isServer, bookname, newFlag)) {
			String fl = "private";
			if (newFlag) {
				fl = "public";
			}
			// FMLLog.info(bookname+" is now a "+fl+" book.");
		} else {
			FMLLog.warning("Updating book flag for " + bookname + " failed");
		}
	}

	private void handleBookDeletion(ByteBuf packet) {
		String bookname = ByteBufUtils.readUTF8String(packet);
		boolean isServer = packet.readBoolean();
		FileUtil util = new FileUtil();
		if (util.deleteBook(!isServer, bookname)) {
			FMLLog.info(bookname + " has been deleted Forever!");
		} else {
			FMLLog.warning("Deletion of " + bookname + " failed");
			// FMLLog.warning
		}
	}

	private void handleBookEdit(ByteBuf packet, EntityPlayerMP player) {
		int x, y, z;
		int currentPage;
		ItemStack book;
		book = ByteBufUtils.readItemStack(packet);// Packet.readItemStack(inputStream);

		x = packet.readInt();
		y = packet.readInt();
		z = packet.readInt();
		currentPage = packet.readInt();

		if (book != ItemStack.EMPTY) {
			if (Config.testBookValidity(book)) {
				TileEntityDesk deskTile = (TileEntityDesk) player.world.getTileEntity(new BlockPos(x, y, z));
				if (deskTile != null) {
					deskTile.overwriteWrittenBook(book);
					deskTile.setCurrentPage(currentPage);
				}
			}
		}
	}

	private void handleBookPageUpdate(ByteBuf packet, EntityPlayerMP player) {
		// DataInputStream inputStream = new DataInputStream(new
		// ByteArrayInputStream(packet.func_149558_e()));
		int x, y, z;
		int currentPage;
		x = packet.readInt();
		y = packet.readInt();
		z = packet.readInt();
		currentPage = packet.readInt();

		TileEntityDesk deskTile = (TileEntityDesk) player.world.getTileEntity(new BlockPos(x, y, z));
		if (deskTile != null) {
			deskTile.setCurrentPage(currentPage);
		}
	}

	private void handleInventoryStackUpdate(ByteBuf packet, EntityPlayerMP player) {
		ItemStack stackostuff;
		stackostuff = ByteBufUtils.readItemStack(packet);
		if (stackostuff != ItemStack.EMPTY) {
			ItemStack currentPlayerSlot = player.getHeldItem(EnumHand.MAIN_HAND);
			if (currentPlayerSlot != ItemStack.EMPTY) {
				if (currentPlayerSlot.getUnlocalizedName().equals(stackostuff.getUnlocalizedName())
						&& checkIfValidPacketItem(currentPlayerSlot.getUnlocalizedName())) {
					NBTTagCompound currentTags = currentPlayerSlot.getTagCompound();
					NBTTagCompound newTags = stackostuff.getTagCompound();
					if (!currentPlayerSlot.getUnlocalizedName().contains("item.AtlasBook")) {
						if (currentTags != null && currentTags.hasKey("Inventory") && newTags != null) {
							NBTTagList tagList = currentTags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
							newTags.setTag("Inventory", tagList);
							stackostuff.setTagCompound(newTags);
						}
					} else if (currentTags.hasKey("atlasID") && newTags.hasKey("atlasID")
							&& currentTags.getInteger("atlasID") != newTags.getInteger("atlasID")) {
						return;
					}
					player.inventory.setInventorySlotContents(player.inventory.currentItem, stackostuff);
				}
			}
		}
	}

	private boolean checkIfValidPacketItem(String input) {
		// Make sure all this stuff can only open if in main hand. // TODO
		String validPacketItems[] = { "item.AtlasBook", "item.BigBook", "item.RecipeBook", "item.BiblioClipboard",
				"item.BiblioRedBook", "item.SlottedBook", "item.compass" };
		for (int i = 0; i < validPacketItems.length; i++) {
			if (validPacketItems[i].equals(input)) {
				return true;
			}
		}
		return false;
	}

	private void handleMarkerPoles(ByteBuf packet, EntityPlayerMP player) {
		int i;
		int j;
		int k;
		boolean newTest;
		int direction;
		i = packet.readInt();
		j = packet.readInt();
		k = packet.readInt();
		newTest = packet.readBoolean();
		direction = packet.readInt();
		EnumFacing facing = EnumFacing.getFront(direction);
		World world = player.world;
		int iadj = 0;
		int jadj = 0;
		int kadj = 0;
		switch (direction) {
			case 0:
				jadj = -1;
				break;
			case 1:
				jadj = 1;
				break;
			case 2:
				kadj = -1;
				break;
			case 3:
				kadj = 1;
				break;
			case 4:
				iadj = -1;
				break;
			case 5:
				iadj = 1;
				break;
			default:
				iadj = 1;
				break;
		}

		BlockPos pos = new BlockPos(i + iadj, j + jadj, k + kadj);
		if (newTest) {
			if (world.isAirBlock(pos)) {
				IBlockState st = BlockMarkerPole.instance.getDefaultState();
				world.setBlockState(pos, st);
				TileEntityMarkerPole poleTile = (TileEntityMarkerPole) world.getTileEntity(pos);
				if (poleTile != null) {
					poleTile.setAngle(EnumFacing.NORTH);
					if (facing == EnumFacing.UP) {
						poleTile.setVertPosition(EnumVertPosition.FLOOR);
					} else if (facing == EnumFacing.DOWN) {
						poleTile.setVertPosition(EnumVertPosition.CEILING);
					} else {
						switch (facing) {
							case NORTH: {
								facing = EnumFacing.WEST;
								break;
							}
							case WEST: {
								facing = EnumFacing.SOUTH;
								break;
							}
							case SOUTH: {
								facing = EnumFacing.EAST;
								break;
							}
							case EAST: {
								facing = EnumFacing.NORTH;
								break;
							}
							default:
								break;
						}
						poleTile.setAngle(facing);
						poleTile.setVertPosition(EnumVertPosition.WALL);
					}
					world.markBlockRangeForRenderUpdate(pos, pos);
				}
			}
		} else {
			// destroy block
			if (world.getBlockState(pos).getBlock() == BlockMarkerPole.instance) {
				world.destroyBlock(pos, false);
			}
		}
	}

	private void handleMapWaypoints(ByteBuf packet, EntityPlayerMP player) {
		World world = player.world;
		int i;
		int j;
		int k;
		float xPin;
		float yPin;
		String name;
		int color;
		int pinNum;
		boolean remove;
		boolean edit;
		i = packet.readInt();
		j = packet.readInt();
		k = packet.readInt();
		xPin = packet.readFloat();
		yPin = packet.readFloat();
		// name = inputStream.readUTF();
		name = ByteBufUtils.readUTF8String(packet);
		color = packet.readInt();
		pinNum = packet.readInt();
		remove = packet.readBoolean();
		edit = packet.readBoolean();

		TileEntity tile = world.getTileEntity(new BlockPos(i, j, k));
		if (tile != null && tile instanceof TileEntityMapFrame) {
			TileEntityMapFrame mapFrame = (TileEntityMapFrame) tile;
			if (!remove) {
				if (!edit) {
					mapFrame.addPinCoords(xPin, yPin, name, color);
				} else {
					mapFrame.editPinData(name, color, pinNum);
				}
			} else {
				mapFrame.removePin(pinNum);
			}
		}
	}

}
