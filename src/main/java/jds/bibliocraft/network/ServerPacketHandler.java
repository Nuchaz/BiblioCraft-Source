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
			// if (packet.channel().equals("BiblioType")) {
			// 	handleBookNameUpdate(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioTypeFlag")) {
			// 	handleBookFlagUpdate(packet.payload());
			// }
			// if (packet.channel().equals("BiblioTypeDelete")) {
			// 	handleBookDeletion(packet.payload());
			// }
			// if (packet.channel().equals("BiblioTypeUpdate")) {
			// 	handleTypsetUpdate(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioMCBEdit")) {
			// 	handleBookEdit(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioMCBPage")) {
			// 	handleBookPageUpdate(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioUpdateInv")) {
			// 	handleInventoryStackUpdate(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioMeasure")) {
			// 	handleMarkerPoles(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioMapPin")) {
			// 	handleMapWaypoints(packet.payload(), player);
			// 	// packet.toS3FPacket()
			// }
			// if (packet.channel().equals("BiblioRBook")) {
			// 	handleRecipeBook(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioRBookLoad")) {
			// 	handleRecipeLoad(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioSign")) {
			// 	handleFancySignUpdate(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioClock")) {
			// 	handleClockUpdate(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioPaintPress")) {
			// 	handlePaintPressUpdate(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioPainting")) {
			// 	handlePaintingUpdate(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioPaintingC")) {
			// 	handlePaintingCustomAspectsUpdate(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioAtlas")) {
			// 	handleAtlasUpdate(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioAtlasSWP")) {
			// 	handleAtlasSwapUpdate(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioAtlasWPT"))
			// {
			// handleAtlasTransferUpdate(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioPaneler")) {
			// 	handlePanelerTextureStringUpdate(packet.payload(), player.world);
			// }
			// if (packet.channel().equals("BiblioRecipeCraft")) {
			// 	handleRecipeBookRecipeCraft(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioStockTitle")) {
			// 	// update the title of the stockroom catalog
			// 	handleStockroomCatalogTitle(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioStockCompass")) {
			// 	// update the compass on the stockroom catalog
			// 	handleStockroomCatalogCompass(packet.payload(), player);
			// }
			// if (packet.channel().equals("BiblioClipboard")) {
			// 	handleClipboardBlockUpdate(packet.payload(), player);
			// }
		}
	}
}
