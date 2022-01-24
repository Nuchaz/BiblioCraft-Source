package jds.bibliocraft;

import java.io.File;
import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.ChannelHandler;
import jds.bibliocraft.blocks.BlockBell;
import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.blocks.blockitems.BlockItemCase;
import jds.bibliocraft.blocks.blockitems.BlockItemClock;
import jds.bibliocraft.blocks.blockitems.BlockItemTypewriter;
import jds.bibliocraft.enchantments.EnchantmentDeathCompass;
import jds.bibliocraft.enchantments.EnchantmentReading;
import jds.bibliocraft.entity.EntitySeat;
import jds.bibliocraft.events.BakeEventHandler;
import jds.bibliocraft.events.EventBlockMarkerHighlight;
import jds.bibliocraft.events.EventDeathDrop;
import jds.bibliocraft.events.EventItemToss;
import jds.bibliocraft.events.EventSpawn;
import jds.bibliocraft.events.GuiBiblioOverlay;
import jds.bibliocraft.events.TextureStichHandler;
import jds.bibliocraft.helpers.RecipeBiblioAtlas;
import jds.bibliocraft.helpers.RecipeBiblioFramedWood;
import jds.bibliocraft.helpers.RecipeShapelessFramedWood;
import jds.bibliocraft.items.ItemDeathCompass;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemHandDrill;
import jds.bibliocraft.items.ItemTape;
import jds.bibliocraft.items.ItemTapeMeasure;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.ClientPacketHandler;
import jds.bibliocraft.network.ServerPacketHandler;
import jds.bibliocraft.SoundLoader;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.RegistryEvent;

/**
 * BiblioCraft
 * 
 * Designed and written by Joseph "Nuchaz" Sinclair 
 * 
 * Special thanks goes to the MCP team and the Forge team for creating the resources
 * needed to make such a mod a much easier process than it could have been.
 * 
 * Special note on version numbers. 3 digits. 0.0.0
 * First digit represents major public releases. Each version number will include at least 1 new block/major feature
 * Second digit represents minor private releases. Each version will include at least 1 major feature or fundamental change (resets to 0 on public release day)
 * Third digit represents any small changes on a private or public release to fix bugs or errors (resets to 0 on public release, also resets on a private release)
 * 
 */

// Minecraft v1.11.x+
@Mod(modid=BiblioCraft.MODID, name="BiblioCraft", version=BiblioCraft.VERSION)

public class BiblioCraft 
{
    public static final String MODID = "bibliocraft";
    public static final String VERSION = "2.4.6";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static FMLEventChannel ch_BiblioType;
    public static FMLEventChannel ch_BiblioTypeFlag;
    public static FMLEventChannel ch_BiblioTypeDelete;
    public static FMLEventChannel ch_BiblioMCBEdit;
    public static FMLEventChannel ch_BiblioMCBPage;
    public static FMLEventChannel ch_BiblioAStand;
    public static FMLEventChannel ch_BiblioInvStack;
    public static FMLEventChannel ch_BiblioMeasure;
    public static FMLEventChannel ch_BiblioMapPin;
    public static FMLEventChannel ch_BiblioTypeUpdate;
    public static FMLEventChannel ch_BiblioRecipeBook;
    public static FMLEventChannel ch_BiblioRecipeLoad;
    public static FMLEventChannel ch_BiblioSign;
    public static FMLEventChannel ch_BiblioClock;
    public static FMLEventChannel ch_BiblioPaintPress;
    public static FMLEventChannel ch_BiblioPainting;
    public static FMLEventChannel ch_BiblioPaintingC;
    public static FMLEventChannel ch_BiblioDrillText;
    public static FMLEventChannel ch_BiblioAtlas;
    public static FMLEventChannel ch_BiblioAtlasGUIswap;
    public static FMLEventChannel ch_BiblioAtlasTranGUI;
    public static FMLEventChannel ch_BiblioAtlasWPT;
    public static FMLEventChannel ch_BiblioPaneler;
    public static FMLEventChannel ch_BiblioRecipeCraft;
    public static FMLEventChannel ch_BiblioRecipeText;
    public static FMLEventChannel ch_BiblioStockCatalog;
    public static FMLEventChannel ch_BiblioStoCatTitle;
    public static FMLEventChannel ch_BiblioStoCatCompass;
    public static FMLEventChannel ch_BiblioDeskGUIS;
    public static FMLEventChannel ch_BiblioRenderUpdate;
    public static FMLEventChannel ch_BiblioClipboard;
    public static FMLEventChannel ch_BiblioOpenBook;
    
	@Mod.Instance(MODID)
	public static BiblioCraft instance;

	@SidedProxy(clientSide="jds.bibliocraft.ClientProxy", serverSide="jds.bibliocraft.CommonProxy")
	
	public static CommonProxy proxy;
	
	public static EventDeathDrop eDeathDrop;
	public static EventSpawn eSpawn;
	public static EventItemToss eItemToss;
	
	public static EnchantmentDeathCompass deathCompassEnch;
	public static EnchantmentReading readingEnch;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		Config.init(event);
		if (Config.enableLamp || Config.enableLantern)
		{
			BlockLoader.initLightTab();
		}
		if (deathCompassEnch == null)
			deathCompassEnch = new EnchantmentDeathCompass();
		if (readingEnch == null)
			readingEnch = new EnchantmentReading();


		//BlockLoader.addRecipies();
		//ItemLoader.addRecipies(); // still adding an enchantment
		
		EntityRegistry.registerModEntity(new ResourceLocation("bibliocraft:BiblioSeat"), EntitySeat.class, "SeatEntity", 0, this, 80, 1, false);
		eDeathDrop = new EventDeathDrop();
		MinecraftForge.EVENT_BUS.register(eDeathDrop);
		eSpawn = new EventSpawn();
		MinecraftForge.EVENT_BUS.register(eSpawn);
		eItemToss = new EventItemToss();
		MinecraftForge.EVENT_BUS.register(eItemToss);
		//proxy.registerRenderers();

		// TODO comenting this stuff out temporarily.
		//RecipeSorter.register("bibliocraft:specialrecipes", RecipeBiblioAtlas.class, RecipeSorter.Category.SHAPED, ""); // TODO not sure about this
		//RecipeSorter.register("bibliocraft:shapedframedwood", RecipeBiblioFramedWood.class, RecipeSorter.Category.SHAPED, "");
		//RecipeSorter.register("bibliocraft:shapelessframedwood", RecipeShapelessFramedWood.class, RecipeSorter.Category.SHAPELESS, "");
	}
	
	@Mod.EventBusSubscriber(modid=MODID)
	public static class RegisterTheThings
	{
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event)
		{
			BlockLoader.initBlocks(event);
			proxy.initTileEntities();
		}
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event)
		{
			BlockLoader.initBlockItems(event);
			ItemLoader.initItems(event);
		}
		
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event)
		{
			proxy.registerRenderers();
		}
		
		@SubscribeEvent
		public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
		{
			ItemLoader.addRecipies(event);
		}
		
		
		@SubscribeEvent
		public static void registerEnchantments(RegistryEvent.Register<Enchantment> event)
		{
			if (deathCompassEnch == null)
				deathCompassEnch = new EnchantmentDeathCompass();
			if (readingEnch == null)
				readingEnch = new EnchantmentReading();
			if (Config.enableAtlas && Config.enableDeathCompass){
				event.getRegistry().register(deathCompassEnch);	
				}		
			if (Config.enableReadingglasses){
				event.getRegistry().register(readingEnch);	
				}		
		}
	}
	
	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event)
	{
		//MinecraftForge.EVENT_BUS.register(new HighlightHandler());
		MinecraftForge.EVENT_BUS.register(new EventBlockMarkerHighlight());
	}
	
	@Mod.EventHandler 
	public void load(FMLInitializationEvent event) 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiLoader());
		MinecraftForge.EVENT_BUS.register(proxy);
		BiblioNetworking.setup();
		ch_BiblioType = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioType");
		ch_BiblioTypeFlag = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioTypeFlag");
		ch_BiblioTypeDelete = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioTypeDelete");
		ch_BiblioMCBEdit = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioMCBEdit");
		ch_BiblioMCBPage = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioMCBPage");
		ch_BiblioAStand = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioAStand");
		ch_BiblioInvStack = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioUpdateInv");
		ch_BiblioMeasure = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioMeasure");
		ch_BiblioMapPin = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioMapPin");
		ch_BiblioTypeUpdate = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioTypeUpdate");
		ch_BiblioRecipeBook = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioRBook");
		ch_BiblioRecipeLoad = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioRBookLoad");
		ch_BiblioSign = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioSign");
		ch_BiblioClock = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioClock");
		ch_BiblioPaintPress = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioPaintPress");
		ch_BiblioPainting = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioPainting");
		ch_BiblioDrillText = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioDrillText");
		ch_BiblioAtlas = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioAtlas");
		ch_BiblioAtlasGUIswap = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioAtlasSWP");
		ch_BiblioAtlasTranGUI = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioAtlasTGUI");
		ch_BiblioAtlasWPT = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioAtlasWPT");
		ch_BiblioPaintingC = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioPaintingC");
		ch_BiblioPaneler = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioPaneler");
		ch_BiblioRecipeCraft  = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioRecipeCraft");
		ch_BiblioRecipeText  = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioRecipeText");
		ch_BiblioStockCatalog  = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioStockLog");
		ch_BiblioStoCatTitle = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioStockTitle");
		ch_BiblioStoCatCompass = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioStockCompass");
		ch_BiblioRenderUpdate = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioRenderUpdate");
		ch_BiblioDeskGUIS = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioDeskOpenGUI");
		ch_BiblioClipboard = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioClipboard");
		ch_BiblioOpenBook = NetworkRegistry.INSTANCE.newEventDrivenChannel("BiblioOpenBook");
		
		ServerPacketHandler masterServerHandler = new ServerPacketHandler();
		
		ch_BiblioType.register(masterServerHandler);
		ch_BiblioTypeFlag.register(masterServerHandler);
		ch_BiblioTypeDelete.register(masterServerHandler);
		ch_BiblioMCBEdit.register(masterServerHandler);
		ch_BiblioMCBPage.register(masterServerHandler);
		ch_BiblioAStand.register(masterServerHandler);
		ch_BiblioInvStack.register(masterServerHandler); 
		ch_BiblioMeasure.register(masterServerHandler);
		ch_BiblioMapPin.register(masterServerHandler);
		ch_BiblioTypeUpdate.register(masterServerHandler);
		ch_BiblioRecipeBook.register(masterServerHandler);
		ch_BiblioRecipeLoad.register(masterServerHandler);
		ch_BiblioSign.register(masterServerHandler);
		ch_BiblioClock.register(masterServerHandler);
		ch_BiblioPaintPress.register(masterServerHandler);
		ch_BiblioPainting.register(masterServerHandler);
		ch_BiblioAtlas.register(masterServerHandler);
		ch_BiblioAtlasGUIswap.register(masterServerHandler);
		ch_BiblioAtlasWPT.register(masterServerHandler);
		ch_BiblioPaintingC.register(masterServerHandler);
		ch_BiblioPaneler.register(masterServerHandler);
		ch_BiblioRecipeCraft.register(masterServerHandler);
		ch_BiblioStoCatTitle.register(masterServerHandler);
		ch_BiblioStoCatCompass.register(masterServerHandler);
		ch_BiblioClipboard.register(masterServerHandler);
		
		ClientPacketHandler masterClientHandler = new ClientPacketHandler();
		
		ch_BiblioAStand.register(masterClientHandler);
		ch_BiblioDrillText.register(masterClientHandler);
		ch_BiblioAtlas.register(masterClientHandler);
		ch_BiblioAtlasGUIswap.register(masterClientHandler);
		ch_BiblioAtlasTranGUI.register(masterClientHandler);
		ch_BiblioPaneler.register(masterClientHandler);
		ch_BiblioRecipeText.register(masterClientHandler);
		ch_BiblioStockCatalog.register(masterClientHandler);
		ch_BiblioRenderUpdate.register(masterClientHandler);
		ch_BiblioDeskGUIS.register(masterClientHandler);
		ch_BiblioInvStack.register(masterClientHandler);
		ch_BiblioOpenBook.register(masterClientHandler);
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	public void postInitClient(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new GuiBiblioOverlay(Minecraft.getMinecraft())); 
		
		boolean runningCurseClient = new File(Loader.instance().getConfigDir().getParentFile(), ".curseclient").exists();
		if (!runningCurseClient)
		{
			MinecraftForge.EVENT_BUS.register(new VersionCheck());
		}
	}
}
