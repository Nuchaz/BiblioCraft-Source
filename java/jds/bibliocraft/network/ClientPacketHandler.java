package jds.bibliocraft.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.gui.GuiAtlasMap;
import jds.bibliocraft.gui.GuiAtlasWaypointTransfer;
import jds.bibliocraft.gui.GuiBigBook;
import jds.bibliocraft.gui.GuiClipboard;
import jds.bibliocraft.gui.GuiRecipeBook;
import jds.bibliocraft.gui.GuiScreenBookDesk;
import jds.bibliocraft.gui.GuiStockCatalog;
import jds.bibliocraft.helpers.BiblioRenderHelper;
import jds.bibliocraft.helpers.BiblioSortingHelper;
import jds.bibliocraft.helpers.SortedListItem;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemSlottedBook;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
/*
import jds.bibliocraft.BiblioSortingHelper;
import jds.bibliocraft.crafting.BiblioCraftingManager;
import jds.bibliocraft.gui.GuiAtlasMap;
import jds.bibliocraft.gui.GuiAtlasWaypointTransfer;
import jds.bibliocraft.gui.GuiStockCatalog;
import jds.bibliocraft.helpers.SortedListItem;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
*/
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ClientPacketHandler 
{
	public ClientPacketHandler(){}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event) 
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		FMLProxyPacket packet = event.getPacket();
		
		if (packet != null)
		{
			if (packet.channel().equals("BiblioAStand"))
			{
				handlePlayerArmorUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioDrillText"))
			{
				handPlayerDrillTextUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioAtlas"))
			{
				handPlayerAtlasUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioAtlasSWP"))
			{
				handleAtlasSwap(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioAtlasTGUI"))
			{
				handleAtlasTransferGUI(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioPaneler"))
			{
				handlePanelerTextureStringPacket(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioRecipeText"))
			{
				handleRecipeBookText(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioStockLog"))
			{
				handleBiblioStockroomCatalog(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioDeskOpenGUI"))
			{
				handleDeskOpenGUI(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioUpdateInv"))
			{
				handleInvStackUpdate(packet.payload(), player);
			}
			if (packet.channel().equals("BiblioOpenBook"))
			{
				handleOpenBook(packet.payload(), player);
			}
		}
	}
	
	private void handleOpenBook(ByteBuf packet, EntityPlayer player)
	{
		final boolean canCraft = packet.readBoolean();
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				EntityPlayer player = Minecraft.getMinecraft().player;
				ItemStack stackMain = player.getHeldItem(EnumHand.MAIN_HAND);
				ItemStack stackOff = player.getHeldItem(EnumHand.OFF_HAND);

				if (stackMain.getItem() == ItemRecipeBook.instance)
				{
					Minecraft.getMinecraft().displayGuiScreen(new GuiRecipeBook(stackMain, false, 0, 0, 0, player.inventory.currentItem, canCraft));
				} 
				else if (stackOff.getItem() == ItemRecipeBook.instance)
				{
					Minecraft.getMinecraft().displayGuiScreen(new GuiRecipeBook(stackOff, false, 0, 0, 0, player.inventory.currentItem, canCraft));
				}
			}
		});
	}
	
	private void handleInvStackUpdate(ByteBuf packet, EntityPlayer player)
	{
		ItemStack stack = ByteBufUtils.readItemStack(packet);
		ItemStack playerStack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (stack != ItemStack.EMPTY && playerStack != ItemStack.EMPTY && stack.getItem() == playerStack.getItem())
		{
			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
		}
	}
	
	private void handleDeskOpenGUI(ByteBuf packet, EntityPlayer player)
	{
		final int x = packet.readInt();
		final int y = packet.readInt();
		final int z = packet.readInt();
		final ItemStack book = ByteBufUtils.readItemStack(packet);
		final boolean canCraft = packet.readBoolean();
		if (book != ItemStack.EMPTY)
		{
			final Item signedtest = book.getItem();
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					EntityPlayer player = Minecraft.getMinecraft().player;
					if (signedtest instanceof ItemWrittenBook)
					{
				 		openWritingGUI(player, book, x, y, z, false);
						//signedtest.onItemRightClick(book, world, player); 
					}
					if (signedtest instanceof ItemWritableBook)
					{
						openWritingGUI(player, book, x, y, z, true);
					}
					if (signedtest instanceof ItemClipboard)
					{
						openClipboardGUI(book, false, x, y, z);
					}
					if (signedtest instanceof ItemBigBook)
					{
						openBigBookGUI(book, x, y, z, player.getDisplayNameString()); 
					}
					if (signedtest instanceof ItemRecipeBook)
					{
						openRecipeBookGUI(book, x, y, z, -1, canCraft);
					}
					if (Loader.isModLoaded("thaumcraft") && book.toString().contains("thaumonomicon"))
					{
						signedtest.onItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
					}
					if (Loader.isModLoaded("tailcraft") && book.toString().contains("railcraft.routing.table"))
					{
						signedtest.onItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
					}
					if (Loader.isModLoaded("craftguide") && book.toString().contains("craftguide"))
					{
						signedtest.onItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);  
					}
					if (Loader.isModLoaded("botania") && book.getUnlocalizedName().contentEquals("item.lexicon"))
					{
						//System.out.println(book.getUnlocalizedName());
						//signedtest.onItemRightClick(book, world, player);
						// doesnt work
					}
				}
			});
		}		
	}
	
    @SideOnly(Side.CLIENT)
    public void openWritingGUI(EntityPlayer player, ItemStack book, int x, int y, int z, boolean signed)
    {
    	Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBookDesk(player, book, signed, x, y, z));
    }
	@SideOnly(Side.CLIENT)
    public void openClipboardGUI(ItemStack stack, boolean inInv, int x, int y, int z)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiClipboard(stack, inInv, x, y, z));
    }
	
	@SideOnly(Side.CLIENT)
    public void openRecipeBookGUI(ItemStack stack, int x, int y, int z, int slot, boolean canCraft)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiRecipeBook(stack, true, x, y, z, slot, canCraft)); 
    }
	
	@SideOnly(Side.CLIENT)
    public void openBigBookGUI(ItemStack stack, int x, int y, int z, String author)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiBigBook(stack, false, x, y, z, author));
    }
	
	private void handleBiblioStockroomCatalog(ByteBuf packet, EntityPlayer player)
	{
		NBTTagCompound tags = ByteBufUtils.readTag(packet);
		if (tags != null)
		{
			NBTTagList comp = tags.getTagList("compasses", Constants.NBT.TAG_COMPOUND);
			int[] compasses = {-1,-1,-1,-1,-1,-1,-1,-1};
			ItemStack[] compassStacks = {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
			
			for (int i = 0; i < comp.tagCount(); i++)
			{
				NBTTagCompound compTag = comp.getCompoundTagAt(i);
				if (compTag != null)
				{
					String invName = "compass"+i;
					String invSlotName = "slot"+i;
					int compSlot = compTag.getInteger(invSlotName);
					compasses[i] = compSlot;
					if (compSlot != -1)
					{
						compassStacks[i] = new ItemStack(compTag);
					}
				}
			}
			
			NBTTagList alphaTagList = tags.getTagList("alphaList", Constants.NBT.TAG_COMPOUND);
			final ArrayList<SortedListItem> alphaList = BiblioSortingHelper.convertNBTTagListToArrayList(alphaTagList);
			NBTTagList quanaTagList = tags.getTagList("quantaList", Constants.NBT.TAG_COMPOUND);
			final ArrayList<SortedListItem> quantaList = BiblioSortingHelper.convertNBTTagListToArrayList(quanaTagList);
			final int[] finalCompasses = compasses;
			final ItemStack[] finalCompassStacks = compassStacks;
			final String title = tags.getString("title");
			//openCatalogGUI(player, alphaList, quantaList, compassStacks, compasses, tags.getString("title"));
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				
				@Override
				public void run()
				{
					openCatalogGUI(Minecraft.getMinecraft().player, alphaList, quantaList, finalCompassStacks, finalCompasses, title);
				}
			});
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void openCatalogGUI(EntityPlayer player, ArrayList<SortedListItem> AlphaList, ArrayList<SortedListItem> QuantaList, ItemStack[] stacks, int[] compasses, String title)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiStockCatalog(player, AlphaList, QuantaList, stacks, compasses, title));
		//Minecraft.getMinecraft()AlphaList.
    }
	
	private void handleRecipeBookText(ByteBuf packet, EntityPlayer player)
	{
		String text = ByteBufUtils.readUTF8String(packet);
		int currentSlot = packet.readInt();
		ItemStack currentBook = player.inventory.getStackInSlot(currentSlot);
		if (currentBook != ItemStack.EMPTY)
		{
			if (currentBook.getItem() instanceof ItemRecipeBook)
			{
				ItemRecipeBook book = (ItemRecipeBook)currentBook.getItem();
				book.updateFromPacket(text);
			}
		}
	}
	
	private void handlePanelerTextureStringPacket(ByteBuf packet, EntityPlayer player)
	{
		ItemStack panels = ByteBufUtils.readItemStack(packet);
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		String panelTextureName = "none";
		if (panels != ItemStack.EMPTY)
		{
			panelTextureName = BiblioRenderHelper.getBlockTextureString(panels);
		}	
		
		TileEntity tile = player.world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileEntityFurniturePaneler)
		{
			TileEntityFurniturePaneler paneler = (TileEntityFurniturePaneler)tile;
			paneler.setCustomCraftingTex(panelTextureName);
		}
		
		ByteBuf buffer = Unpooled.buffer();
    	ByteBufUtils.writeUTF8String(buffer, panelTextureName);
    	buffer.writeInt(x);
    	buffer.writeInt(y);
    	buffer.writeInt(z);
    	PacketBuffer payload = new PacketBuffer(buffer);
    	BiblioCraft.ch_BiblioPaneler.sendToServer(new FMLProxyPacket(payload, "BiblioPaneler"));
	}
	/*
	private void handlePanelerRecipePacket(ByteBuf packet, EntityPlayer player)
	{
		ItemStack panels = ByteBufUtils.readItemStack(packet);
		ItemStack input = ByteBufUtils.readItemStack(packet);
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		
		ItemStack output = BiblioCraftingManager.getInstance().findMatchingRecipeForPaneler(input, panels);
				
		ByteBuf buffer = Unpooled.buffer();
    	ByteBufUtils.writeItemStack(buffer, output);
    	buffer.writeInt(x);
    	buffer.writeInt(y);
    	buffer.writeInt(z);
    	BiblioCraft.ch_BiblioPaneler.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioPaneler"));  //(new FMLProxyPacket(buffer, "BiblioPaneler"), (EntityPlayerMP) player);
	}
	*/
	private void handleAtlasTransferGUI(ByteBuf packet, EntityPlayer player)
	{
		final ItemStack atlas = ByteBufUtils.readItemStack(packet);
		int x = packet.readInt();
		int y = packet.readInt();
		int z = packet.readInt();
		final TileEntityMapFrame tile = (TileEntityMapFrame)player.world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					openWaypointTransferGUI(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player, atlas, tile); 
				}
			});
			
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void openWaypointTransferGUI(World world, EntityPlayer player, ItemStack stack, TileEntityMapFrame tile)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiAtlasWaypointTransfer(world, player, stack, tile));
    }
	
	private void handleAtlasSwap(ByteBuf packet, EntityPlayer player)
	{
		player.rotationPitch = 50.0f;
		final ItemStack atlas = ByteBufUtils.readItemStack(packet);
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				openMapGUI(Minecraft.getMinecraft().player, atlas); 
			}
		});
	}
	
	@SideOnly(Side.CLIENT)
    public void openMapGUI(EntityPlayer player, ItemStack stack)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiAtlasMap(Minecraft.getMinecraft().world, player, stack));
    }
	
	private void handPlayerAtlasUpdate(ByteBuf packet, EntityPlayer player)
	{
		ItemStack atlas = ByteBufUtils.readItemStack(packet);
		player.inventory.setInventorySlotContents(player.inventory.currentItem, atlas);
	}
	
	private void handlePlayerArmorUpdate(ByteBuf packet, EntityPlayer player)
	{

		ItemStack armor = ItemStack.EMPTY;
		int armorslot = -1;
		armor = ByteBufUtils.readItemStack(packet);//Packet.readItemStack(inputStream);
		armorslot = packet.readInt();
		if (armorslot != -1)
		{
			//player.inventory.armorInventory[armorslot] = armor;
			player.inventory.armorInventory.set(armorslot, armor);
		}
	}
	
	private void handPlayerDrillTextUpdate(ByteBuf packet, EntityPlayer player)
	{
		String displayText = ByteBufUtils.readUTF8String(packet);
		
		ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
		if (playerhand != ItemStack.EMPTY && playerhand.getItem() instanceof ItemDrill)
		{
			ItemDrill drill = (ItemDrill)playerhand.getItem();
			drill.updateFromPacket(displayText); 
		}
	}
	
}
