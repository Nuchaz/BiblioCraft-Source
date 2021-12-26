package jds.bibliocraft;

import java.io.File;
import java.nio.file.Path;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.config.BiblioConfig;
import jds.bibliocraft.config.ServerConfig;
import jds.bibliocraft.containers.ContainerBookcase;
import jds.bibliocraft.enchantments.EnchantmentReading;
import jds.bibliocraft.events.TextureStichHandler;
import jds.bibliocraft.gui.GuiBookcase;
import jds.bibliocraft.helpers.EnumWoodsType;
import jds.bibliocraft.models.ModelBookcase;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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

//https://github.com/MinecraftForge/MinecraftForge/blob/1.13.x/mdk/src/main/resources/META-INF/mods.toml

// Minecraft v1.11.x+
@Mod(BiblioCraft.MODID)
public class BiblioCraft 
{
    public static final String MODID = "bibliocraft";
    public static final String VERSION = "3.0.0";

    public static final ItemGroup BiblioTab = new ItemGroup(MODID) 
    {	
		@Override
		public ItemStack createIcon() 
		{
			return new ItemStack(Blocks.BOOKSHELF);
		}
	};
	
    
    public static SimpleChannel network_BiblioCraft;
    
    public static SimpleChannel ch_BiblioType;
    public static SimpleChannel ch_BiblioTypeFlag;
    public static SimpleChannel ch_BiblioTypeDelete;
    public static SimpleChannel ch_BiblioMCBEdit;
    public static SimpleChannel ch_BiblioMCBPage;
    public static SimpleChannel ch_BiblioAStand;
    public static SimpleChannel ch_BiblioInvStack;
    public static SimpleChannel ch_BiblioMeasure;
    public static SimpleChannel ch_BiblioMapPin;
    public static SimpleChannel ch_BiblioTypeUpdate;
    public static SimpleChannel ch_BiblioRecipeBook;
    public static SimpleChannel ch_BiblioRecipeLoad;
    public static SimpleChannel ch_BiblioSign;
    public static SimpleChannel ch_BiblioClock;
    public static SimpleChannel ch_BiblioPaintPress;
    public static SimpleChannel ch_BiblioPainting;
    public static SimpleChannel ch_BiblioPaintingC;
    public static SimpleChannel ch_BiblioDrillText;
    public static SimpleChannel ch_BiblioAtlas;
    public static SimpleChannel ch_BiblioAtlasGUIswap;
    public static SimpleChannel ch_BiblioAtlasTranGUI;
    public static SimpleChannel ch_BiblioAtlasWPT;
    public static SimpleChannel ch_BiblioPaneler;
    public static SimpleChannel ch_BiblioRecipeCraft;
    public static SimpleChannel ch_BiblioRecipeText;
    public static SimpleChannel ch_BiblioStockCatalog;
    public static SimpleChannel ch_BiblioStoCatTitle;
    public static SimpleChannel ch_BiblioStoCatCompass;
    public static SimpleChannel ch_BiblioDeskGUIS;
    public static SimpleChannel ch_BiblioRenderUpdate;
    public static SimpleChannel ch_BiblioClipboard;
    public static SimpleChannel ch_BiblioOpenBook;
    
    
    //public static TileEntityType<TileEntityBookcase> TILE_BOOKCASE;
    
    //private static String test = "2.4";
    
	//@Mod.Instance(MODID)
	//public static BiblioCraft instance;

	//@SidedProxy(clientSide="jds.bibliocraft.ClientProxy", serverSide="jds.bibliocraft.CommonProxy")
	
	public static CommonProxy proxy;
	
	//TODO temp disabled
	//public static EventDeathDrop eDeathDrop;
	//public static EventSpawn eSpawn;
	//public static EventItemToss eItemToss;
	
	//public static EnchantmentDeathCompass deathCompassEnch;
	public static EnchantmentReading readingEnch;
	
	public BiblioCraft()
	{
		System.out.println("bibliocraft starting...");
		//instance = this;
		
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItems);
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerEnchantments);
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerTileEntities);
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBlocks);
		
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainers);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems); // so this is how its done

		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::bakeModels);
		//MinecraftForge.EVENT_BUS.addListener(this::bakeModels);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		
		
		// this creates a .toml file for config stuff
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BiblioConfig.makeServerConfig()); // this loads from the server and is synced to client and seems to be per-world config
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BiblioConfig.makeCommonConfig()); // this loads both server and client and is not synced, it is a global config
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BiblioConfig.makeClientConfig()); // this is a global config that loads on both server and client
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(BiblioConfig.ConfigUpdateListener::onConfigChanged); this is for when the config is edited in the gui, which isnt implemented

		//ServerConfig.loadConfig();
		
		MinecraftForge.EVENT_BUS.register(this);
		
		//System.out.println("Biblio register models from main thing? probly a bad idea, BUT HERE WE GO");
		//OBJLoader.INSTANCE.addDomain(MODID);
		MinecraftForge.EVENT_BUS.register(TextureStichHandler.instance);
		//MinecraftForge.EVENT_BUS.register(BakeEventHandler.instance);
		
		
		
	}

	
	private void setupClient(FMLClientSetupEvent event)
	{
		System.out.println("biblio setupClient n"); // this runs
		OBJLoader.INSTANCE.addDomain(MODID);
		ScreenManager.registerFactory(ContainerBookcase.bookcaseContainer, GuiBookcase::new);
	}
	

	private void setup(FMLCommonSetupEvent event) // This is the first of four commonly called events during mod initialization.
	{
		System.out.println("biblio setup"); // this also works
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverLoaded); // this never works
		 //clientSetup(event);
		// TODO move config to a seperate load config thing mayube. look at how direwolf did his thing.
		//Config.init(event);
		//if (Config.enableLamp || Config.enableLantern)
		//{
			//BlockLoader.initLightTab();
		//}
		//if (deathCompassEnch == null)
		//	deathCompassEnch = new EnchantmentDeathCompass();
		//if (readingEnch == null)
		//	readingEnch = new EnchantmentReading();
		
		
		//BlockLoader.addRecipies();
		//ItemLoader.addRecipies(); // still adding an enchantment
		
		//EntityRegistry.registerModEntity(new ResourceLocation("bibliocraft:BiblioSeat"), EntitySeat.class, "SeatEntity", 0, this, 80, 1, false); // TODO need to register seat entity
		/* TODO temp disabled
		eDeathDrop = new EventDeathDrop();
		MinecraftForge.EVENT_BUS.register(eDeathDrop);
		eSpawn = new EventSpawn();
		MinecraftForge.EVENT_BUS.register(eSpawn);
		eItemToss = new EventItemToss();
		MinecraftForge.EVENT_BUS.register(eItemToss);
		//proxy.registerRenderers();
		*/
		// TODO comenting this stuff out temporarily.
		//RecipeSorter.register("bibliocraft:specialrecipes", RecipeBiblioAtlas.class, RecipeSorter.Category.SHAPED, ""); // TODO not sure about this
		//RecipeSorter.register("bibliocraft:shapedframedwood", RecipeBiblioFramedWood.class, RecipeSorter.Category.SHAPED, "");
		//RecipeSorter.register("bibliocraft:shapelessframedwood", RecipeShapelessFramedWood.class, RecipeSorter.Category.SHAPELESS, "");
		
		//NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiLoader()); // TODO the guiloader thing is broke

		//MinecraftForge.EVENT_BUS.register(proxy);

		
		// TODO this
		//network_BiblioCraft = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(BiblioCraft.MODID, "BiblioNetwork")).simpleChannel();
		
		/* TODO disabled temporarily
		//server
		network_BiblioCraft.registerMessage(0, PacketTypemachine.class, PacketTypemachine::encode, PacketTypemachine::decode, PacketTypemachine.Handler::handle);
		network_BiblioCraft.registerMessage(1, PacketWorkbench.class, PacketWorkbench::encode, PacketWorkbench::decode, PacketWorkbench.Handler::handle);
		network_BiblioCraft.registerMessage(2, PacketMapframeWaypoint.class, PacketMapframeWaypoint::encode, PacketMapframeWaypoint::decode, PacketMapframeWaypoint.Handler::handle);
		network_BiblioCraft.registerMessage(3, PacketPainting.class, PacketPainting::encode, PacketPainting::decode, PacketPainting.Handler::handle);
		network_BiblioCraft.registerMessage(4, PacketPlayerHandStack.class, PacketPlayerHandStack::encode, PacketPlayerHandStack::decode, PacketPlayerHandStack.Handler::handle);
		network_BiblioCraft.registerMessage(5, PacketMarkerPole.class, PacketMarkerPole::encode, PacketMarkerPole::decode, PacketMarkerPole.Handler::handle);
		network_BiblioCraft.registerMessage(6, PacketBookFile.class, PacketBookFile::encode, PacketBookFile::decode, PacketBookFile.Handler::handle);
		network_BiblioCraft.registerMessage(7, PacketDesk.class, PacketDesk::encode, PacketDesk::decode, PacketDesk.Handler::handle);
		network_BiblioCraft.registerMessage(8, PacketFancySign.class, PacketFancySign::encode, PacketFancySign::decode, PacketFancySign.Handler::handle);
		network_BiblioCraft.registerMessage(9, PacketClock.class, PacketClock::encode, PacketClock::decode, PacketClock.Handler::handle);
		network_BiblioCraft.registerMessage(10, PacketPaintPress.class, PacketPaintPress::encode, PacketPaintPress::decode, PacketPaintPress.Handler::handle);
		network_BiblioCraft.registerMessage(11, PacketClipboard.class, PacketClipboard::encode, PacketClipboard::decode, PacketClipboard.Handler::handle);
		network_BiblioCraft.registerMessage(12, PacketAtlas.class, PacketAtlas::encode, PacketAtlas::decode, PacketAtlas.Handler::handle);
		network_BiblioCraft.registerMessage(13, PacketAtlasMapframeInteraction.class, PacketAtlasMapframeInteraction::encode, PacketAtlasMapframeInteraction::decode, PacketAtlasMapframeInteraction.Handler::handle);
		network_BiblioCraft.registerMessage(14, PacketPaneler.class, PacketPaneler::encode, PacketPaneler::decode, PacketPaneler.Handler::handle);
		network_BiblioCraft.registerMessage(15, PacketStockroomCatelog.class, PacketStockroomCatelog::encode, PacketStockroomCatelog::decode, PacketStockroomCatelog.Handler::handle);
		network_BiblioCraft.registerMessage(16, PacketCraftFromRecipeBook.class, PacketCraftFromRecipeBook::encode, PacketCraftFromRecipeBook::decode, PacketCraftFromRecipeBook.Handler::handle);
		
		//client
		network_BiblioCraft.registerMessage(17, PacketDrillText.class, PacketDrillText::encode, PacketDrillText::decode, PacketDrillText.Handler::handle);
		network_BiblioCraft.registerMessage(18, PacketAtlasGUISwap.class, PacketAtlasGUISwap::encode, PacketAtlasGUISwap::decode, PacketAtlasGUISwap.Handler::handle);
		network_BiblioCraft.registerMessage(19, PacketArmorStand.class, PacketArmorStand::encode, PacketArmorStand::decode, PacketArmorStand.Handler::handle);
		network_BiblioCraft.registerMessage(20, PacketRecipeBookOpen.class, PacketRecipeBookOpen::encode, PacketRecipeBookOpen::decode, PacketRecipeBookOpen.Handler::handle);
		network_BiblioCraft.registerMessage(21, PacketStackUpdate.class, PacketStackUpdate::encode, PacketStackUpdate::decode, PacketStackUpdate.Handler::handle);
		network_BiblioCraft.registerMessage(22, PacketStockCatelogClient.class, PacketStockCatelogClient::encode, PacketStockCatelogClient::decode, PacketStockCatelogClient.Handler::handle);
		network_BiblioCraft.registerMessage(23, PacketRecipeBookText.class, PacketRecipeBookText::encode, PacketRecipeBookText::decode, PacketRecipeBookText.Handler::handle);
		network_BiblioCraft.registerMessage(24, PacketPanelerClient.class, PacketPanelerClient::encode, PacketPanelerClient::decode, PacketPanelerClient.Handler::handle);
		network_BiblioCraft.registerMessage(25, PacketWaypointTransferGUI.class, PacketWaypointTransferGUI::encode, PacketWaypointTransferGUI::decode, PacketWaypointTransferGUI.Handler::handle);
		network_BiblioCraft.registerMessage(26, PacketDeskGUIClient.class, PacketDeskGUIClient::encode, PacketDeskGUIClient::decode, PacketDeskGUIClient.Handler::handle);
	*/
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		System.out.println("biblio clientSetup"); // never gets called
		//MinecraftForge.EVENT_BUS.register(new EventBlockMarkerHighlight()); TODO temp disabled
	}
	
	private void serverLoad(FMLServerStartingEvent event)
	{
		
	}
	
	public void serverLoaded(FMLServerStartedEvent event)
	{
		//clientPostMaload(event);
		System.out.println("The server is loaded.");  // never gets called
		// does this run on server and client?, just server? what?
		//event.
	}
	
	
	// the event says I shouldn't use this, so what should I use?
	@OnlyIn(Dist.CLIENT)
	private void clientPostMaload(FMLLoadCompleteEvent event)
	{
		//MinecraftForge.EVENT_BUS.register(new GuiBiblioOverlay(Minecraft.getInstance())); TODO temp disabled
		
		// TODO look into the curseclient thing
		//boolean runningCurseClient = new File(Loader.instance().getConfigDir().getParentFile(), ".curseclient").exists();
		//if (!runningCurseClient)
		//{
			//MinecraftForge.EVENT_BUS.register(new VersionCheck());
		//}
	}
	
	//public void load
	
	//@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event)
	{
		System.out.println("Biblio Block Registering");
		BlockLoader.initBlocks(event);
		//proxy.initTileEntities();
	}
	
	//@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event)
	{
		System.out.println("Biblio Item Registering");
		
		BlockLoader.initBlockItems(event);
		ItemLoader.initItems(event);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		System.out.println("Biblio register models");
		//OBJLoader.INSTANCE.addDomain(MODID);
		//MinecraftForge.EVENT_BUS.register(TextureStichHandler.instance);
		//MinecraftForge.EVENT_BUS.register(BakeEventHandler.instance);
		////proxy.registerRenderers();
		/* some example code from forge website
		final ModelResourceLocation itemLocation = new ModelResourceLocation(TEST_BLOCK.getRegistryName(), "normal");

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, TestBlock.name));
            ForgeHooksClient.registerTESRItemStack(item, 0, CustomTileEntity.class);
            ModelLoader.setCustomModelResourceLocation(item, 0, itemLocation);
            ClientRegistry.bindTileEntitySpecialRenderer(CustomTileEntity.class, TestTESR.instance);
		*/
	}
	
	/*
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		ItemLoader.addRecipies(event); // TODO dunno about this
	}*/
	
	
	@SubscribeEvent
	public static void registerEnchantments(Register<Enchantment> event)
	{
		System.out.println("Biblio Enchangment Registering");
		//if (deathCompassEnch == null)
		//	deathCompassEnch = new EnchantmentDeathCompass();
		//if (readingEnch == null)
		//	readingEnch = new EnchantmentReading();
		//if (Config.enableAtlas && Config.enableDeathCompass){
		//	event.getRegistry().register(deathCompassEnch);	
		//	}		
		//if (Config.enableReadingglasses){
		//	event.getRegistry().register(readingEnch);	
		//	}		
	}
	
	@SubscribeEvent
	public void registerTileEntities(Register<TileEntityType<?>> event)
	{
		System.out.println("Biblio Tile Registering");
		event.getRegistry().register(TileEntityType.Builder.create(TileEntityBookcase::new, BlockLoader.bookcases[0]).build(null).setRegistryName(MODID, "bookcase"));
	}
	
	@SubscribeEvent
	public void registerContainers(Register<ContainerType<?>> event)
	{
		System.out.println("Biblio container registrar");
		event.getRegistry().register(IForgeContainerType.create(ContainerBookcase::new).setRegistryName("bookcasecontainer"));
	}
    
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents 
    {
        @SubscribeEvent
        public static void onModelBakeEvent(ModelBakeEvent event) 
        {
        	 try 
        	 {
                 IUnbakedModel model = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("bibliocraft:bookcase.obj"));
                 System.out.println(model.toString());
                 if (model instanceof OBJModel) 
                 {
                     // If loading OBJ model succeeds, bake the model and replace stick's model with the baked model
                	 //System.out.println("biblio eh oh?");
                     IBakedModel bakedModel = model.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(model.getDefaultState(), false), DefaultVertexFormats.ITEM);
                     // instead of baking a model like that, I need to create a new version of my model with my model class. Look for examples
                     event.getModelRegistry().put(new ModelResourceLocation("bibliocraft:" + BlockBookcase.name + EnumWoodsType.oak.getName(), "inventory"), bakedModel); // new ModelBookcase(event, EnumWoodsType.oak, true)
                     event.getModelRegistry().put(new ModelResourceLocation("bibliocraft:" + BlockBookcase.name + EnumWoodsType.oak.getName()), new ModelBookcase(event, EnumWoodsType.oak, false));
                     
                   //event.getModelRegistry().put(new ModelResourceLocation("oak_planks", "inventory"), bakedModel);
                   //event.getModelRegistry().put(new ModelResourceLocation("oak_planks"), bakedModel);
                   
                   //event.getModelRegistry().put(new ModelResourceLocation("bibliocraft:testblock", "inventory"), bakedModel);
                   //event.getModelRegistry().put(new ModelResourceLocation("bibliocraft:testblock"), bakedModel);
                     //event.getModelRegistry().
                 }
             }
        	 catch (Exception e) 
        	 {
                 e.printStackTrace();
             }
        }
        
		@SubscribeEvent
		public static void onTextureStichEvent(TextureStitchEvent.Pre event)
		{
			event.addSprite(new ResourceLocation("bibliocraft:models/bookcase_books"));
			event.addSprite(new ResourceLocation("bibliocraft:gui/bookshelfgui"));


		}
		
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event)
		{
			System.out.println("biblio registerModels locale");
			//ModelLoader.
			//Item bookcase_item = Item.getItemFromBlock(BlockLoader.bookcases[0]);
			//ModelLoader.
			/* some example code from forge website
			final ModelResourceLocation itemLocation = new ModelResourceLocation(TEST_BLOCK.getRegistryName(), "normal");

	            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, TestBlock.name));
	            ForgeHooksClient.registerTESRItemStack(item, 0, CustomTileEntity.class);
	            ModelLoader.setCustomModelResourceLocation(item, 0, itemLocation);
	            ClientRegistry.bindTileEntitySpecialRenderer(CustomTileEntity.class, TestTESR.instance);
	            
	            
	            			Item bookcase_item = Item.getItemFromBlock(BlockBookcase.instance);
			Item bookcase_creative_item = Item.getItemFromBlock(BlockBookcaseCreative.instance); 
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(bookcase_item, i, ModelBookcase.modelResourceLocation);
				ModelLoader.setCustomModelResourceLocation(bookcase_creative_item, i, ModelBookcase.modelResourceLocationFilledBookcase);
			}
			ModelLoader.setCustomStateMapper(BlockBookcase.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockBookcaseCreative.instance, BiblioBlockStateMapper.instance);
			*/
		}
    }
    
	
}
