package jds.bibliocraft;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.HopperContainer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.MinecraftForgeClient;
import jds.bibliocraft.tileentities.TileEntityBookcase;
/*
import jds.bibliocraft.tileentities.BiblioLightTileEntity;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import jds.bibliocraft.tileentities.TileEntityBell;
import jds.bibliocraft.tileentities.TileEntityCase;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import jds.bibliocraft.tileentities.TileEntityClock;
import jds.bibliocraft.tileentities.TileEntityCookieJar;
import jds.bibliocraft.tileentities.TileEntityDesk;
import jds.bibliocraft.tileentities.TileEntityDinnerPlate;
import jds.bibliocraft.tileentities.TileEntityDiscRack;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFancyWorkbench;
import jds.bibliocraft.tileentities.TileEntityFramedChest;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import jds.bibliocraft.tileentities.TileEntityLabel;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import jds.bibliocraft.tileentities.TileEntityMarkerPole;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPaintingBorderless;
import jds.bibliocraft.tileentities.TileEntityPaintingFancy;
import jds.bibliocraft.tileentities.TileEntityPaintingFlat;
import jds.bibliocraft.tileentities.TileEntityPaintingMiddle;
import jds.bibliocraft.tileentities.TileEntityPaintingSimple;
import jds.bibliocraft.tileentities.TileEntityPotionShelf;
import jds.bibliocraft.tileentities.TileEntityPrintPress;
import jds.bibliocraft.tileentities.TileEntitySeat;
import jds.bibliocraft.tileentities.TileEntityShelf;
import jds.bibliocraft.tileentities.TileEntitySwordPedestal;
import jds.bibliocraft.tileentities.TileEntityTable;
import jds.bibliocraft.tileentities.TileEntityToolRack;
import jds.bibliocraft.tileentities.TileEntityTypeMachine;
import jds.bibliocraft.tileentities.TileEntityTypewriter;
import jds.bibliocraft.blocks.BiblioLightBlock;
import jds.bibliocraft.blocks.BlockArmorStand;
import jds.bibliocraft.blocks.BlockBell;
*/
import jds.bibliocraft.blocks.BlockBookcase;
/*
import jds.bibliocraft.blocks.BlockCase;
import jds.bibliocraft.blocks.BlockClipboard;
import jds.bibliocraft.blocks.BlockClock;
import jds.bibliocraft.blocks.BlockCookieJar;
import jds.bibliocraft.blocks.BlockDesk;
import jds.bibliocraft.blocks.BlockDinnerPlate;
import jds.bibliocraft.blocks.BlockDiscRack;
import jds.bibliocraft.blocks.BlockFancySign;
import jds.bibliocraft.blocks.BlockFancyWorkbench;
import jds.bibliocraft.blocks.BlockFramedChest;
import jds.bibliocraft.blocks.BlockFurniturePaneler;
import jds.bibliocraft.blocks.BlockLabel;
import jds.bibliocraft.blocks.BlockMapFrame;
import jds.bibliocraft.blocks.BlockMarkerPole;
import jds.bibliocraft.blocks.BlockPaintingFrameBorderless;
import jds.bibliocraft.blocks.BlockPaintingFrameFancy;
import jds.bibliocraft.blocks.BlockPaintingFrameFlat;
import jds.bibliocraft.blocks.BlockPaintingFrameMiddle;
import jds.bibliocraft.blocks.BlockPaintingFrameSimple;
import jds.bibliocraft.blocks.BlockPaintingPress;
import jds.bibliocraft.blocks.BlockPotionShelf;
import jds.bibliocraft.blocks.BlockPrintingPress;
import jds.bibliocraft.blocks.BlockSeat;
import jds.bibliocraft.blocks.BlockShelf;
import jds.bibliocraft.blocks.BlockSwordPedestal;
import jds.bibliocraft.blocks.BlockTable;
import jds.bibliocraft.blocks.BlockToolRack;
import jds.bibliocraft.blocks.BlockTypeWriter;
import jds.bibliocraft.blocks.BlockTypesettingTable;
import jds.bibliocraft.entity.EntitySeat;
*/
import jds.bibliocraft.containers.ContainerBookcase;

public class CommonProxy
{
	public static final ResourceLocation BOOKCASEGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/bookshelfgui.png");
	
	public static final ResourceLocation ARMORGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/armorstandgui.png");
	public static final ResourceLocation POTIONGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/potionshelfgui.png");
	public static final ResourceLocation TOOLRACKGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/toolrackgui.png");
	public static final ResourceLocation GENERICSHELFGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/genericshelfgui.png");
	public static final ResourceLocation WEAPONCASEGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/weaponcasegui.png");
	public static final ResourceLocation WOODLABELGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/woodlabelgui.png");
	public static final ResourceLocation WRITINGDESKGUI_PNG = new ResourceLocation("bibliocraft", "textures/gui/writingdeskgui.png");
	public static final ResourceLocation TYPEMACHINEgui_PNG = new ResourceLocation("bibliocraft", "textures/gui/typesettinggui.png");
	public static final ResourceLocation TYPEMACHINEGUI_L_PNG = new ResourceLocation("bibliocraft", "textures/gui/typesettinggui_l.png");
	public static final ResourceLocation TYPEMACHINEGUI_R_PNG = new ResourceLocation("bibliocraft", "textures/gui/typesettinggui_r.png");
	public static final ResourceLocation GUITYPEBACK_PNG = new ResourceLocation("bibliocraft", "textures/models/guitypebackg.png");
	public static final ResourceLocation GUICOOKIEJAR = new ResourceLocation("bibliocraft", "textures/gui/cookiejargui.png");
	
	public static final ResourceLocation DISCRACKgui = new ResourceLocation("bibliocraft", "textures/gui/discrackgui.png"); 
	public static final ResourceLocation MAPPINGUI = new ResourceLocation("bibliocraft", "textures/gui/mapwaypointgui.png");
	public static final ResourceLocation COMPASSGUI = new ResourceLocation("bibliocraft", "textures/gui/compasswaypointgui.png");
	public static final ResourceLocation ATLASGUI = new ResourceLocation("bibliocraft", "textures/gui/atlasgui.png");
	public static final ResourceLocation ATLASGUIBUTTONS = new ResourceLocation("bibliocraft", "textures/gui/atlasguibuttons.png");
	public static final ResourceLocation ATLASGUITRANSFER = new ResourceLocation("bibliocraft", "textures/gui/atlastransfergui.png");
	public static final ResourceLocation ATLASCOVER = new ResourceLocation("bibliocraft", "textures/gui/atlas_cover.png");
	public static final ResourceLocation SLOTTEDBOOKGUI = new ResourceLocation("bibliocraft", "textures/gui/slottedbookgui.png");
	public static final ResourceLocation BIGBOOKGUI = new ResourceLocation("bibliocraft", "textures/gui/bigbookgui.png");
	public static final ResourceLocation BIGBOOKGUIBUTTONS = new ResourceLocation("bibliocraft", "textures/gui/bigbookbuttons.png");
	public static final ResourceLocation FANCYSIGNGUI = new ResourceLocation("bibliocraft", "textures/gui/fancysigngui.png");
	public static final ResourceLocation FANCYSIGNGUIBUTTONS = new ResourceLocation("bibliocraft", "textures/gui/fancysignguibuttons.png");
	public static final ResourceLocation FANCYWORKBENCHGUI = new ResourceLocation("bibliocraft", "textures/gui/fancyworkbenchgui.png");
	public static final ResourceLocation FANCYWORKBENCHBOOKCASEGUI = new ResourceLocation("bibliocraft", "textures/gui/fancyworkbenchbookcasegui.png");
	public static final ResourceLocation RECIPEBOOKGUI = new ResourceLocation("bibliocraft", "textures/gui/recipebookgui.png");
	public static final ResourceLocation CLOCKGUI = new ResourceLocation("bibliocraft", "textures/gui/clockgui.png");

	public static final ResourceLocation PANELER_GUI = new ResourceLocation("bibliocraft", "textures/gui/panelergui.png");
	public static final ResourceLocation STOCKROOMCATALOGGUI = new ResourceLocation("bibliocraft", "textures/gui/stockcataloggui.png");
	public static final ResourceLocation STOCKROOMCATALOGSUBGUI = new ResourceLocation("bibliocraft", "textures/gui/stockcatalogsubgui.png");
	
	public static final ResourceLocation PAINTINGCANVAS = new ResourceLocation("bibliocraft", "textures/paintings/canvas.png");
	public static final ResourceLocation PAINTING01_128LONG = new ResourceLocation("bibliocraft", "textures/paintings/128painting01l.png");
	public static final ResourceLocation PAINTING01_64LONG = new ResourceLocation("bibliocraft", "textures/paintings/64painting01l.png");
	public static final ResourceLocation PAINTING01_128ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/128painting01z.png");
	public static final ResourceLocation PAINTING01_64ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/64painting01z.png");
	public static final ResourceLocation PAINTING02_128LONG = new ResourceLocation("bibliocraft", "textures/paintings/128painting02l.png");
	public static final ResourceLocation PAINTING02_64LONG = new ResourceLocation("bibliocraft", "textures/paintings/64painting02l.png");
	public static final ResourceLocation PAINTING02_32LONG = new ResourceLocation("bibliocraft", "textures/paintings/32painting02l.png");
	public static final ResourceLocation PAINTING02_128ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/128painting02z.png");
	public static final ResourceLocation PAINTING02_64ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/64painting02z.png");
	public static final ResourceLocation PAINTING02_32ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/32painting02z.png");
	public static final ResourceLocation PAINTING03_128LONG = new ResourceLocation("bibliocraft", "textures/paintings/128painting03l.png");
	public static final ResourceLocation PAINTING03_64LONG = new ResourceLocation("bibliocraft", "textures/paintings/64painting03l.png");
	public static final ResourceLocation PAINTING03_32LONG = new ResourceLocation("bibliocraft", "textures/paintings/32painting03l.png");
	public static final ResourceLocation PAINTING03_128ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/128painting03z.png");
	public static final ResourceLocation PAINTING03_64ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/64painting03z.png");
	public static final ResourceLocation PAINTING03_32ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/32painting03z.png");
	public static final ResourceLocation PAINTING04_128FULL = new ResourceLocation("bibliocraft", "textures/paintings/128painting04.png");
	public static final ResourceLocation PAINTING04_64FULL = new ResourceLocation("bibliocraft", "textures/paintings/64painting04.png");
	public static final ResourceLocation PAINTING04_128ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/128painting04z.png");
	public static final ResourceLocation PAINTING04_64ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/64painting04z.png");
	public static final ResourceLocation PAINTING05_256LONG = new ResourceLocation("bibliocraft", "textures/paintings/256painting05l.png");
	public static final ResourceLocation PAINTING05_128LONG = new ResourceLocation("bibliocraft", "textures/paintings/128painting05l.png");
	public static final ResourceLocation PAINTING05_128ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/128painting05z.png");
	public static final ResourceLocation PAINTING05_64ZOOM = new ResourceLocation("bibliocraft", "textures/paintings/64painting05z.png");
	public static final ResourceLocation PAINTINGCOLLAGE_128FULL = new ResourceLocation("bibliocraft", "textures/paintings/128collage.png");
	public static final ResourceLocation PAINTINGCOLLAGE_64FULL = new ResourceLocation("bibliocraft", "textures/paintings/64collage.png");
	public static final ResourceLocation PAINTINGCOLLAGE_32FULL = new ResourceLocation("bibliocraft", "textures/paintings/32collage.png");
	public static final ResourceLocation PAINTINGPIE_64FULL = new ResourceLocation("bibliocraft", "textures/paintings/64pie.png");
	public static final ResourceLocation PAINTINGPIE_32FULL = new ResourceLocation("bibliocraft", "textures/paintings/32pie.png");
	public static final ResourceLocation BOATHOUSE_128FULL = new ResourceLocation("bibliocraft", "textures/paintings/boathouse_128.png");
	public static final ResourceLocation BOATHOUSE_64FULL = new ResourceLocation("bibliocraft", "textures/paintings/boathouse_64.png");
	public static final ResourceLocation BOATHOUSE_128LONG = new ResourceLocation("bibliocraft", "textures/paintings/boathouse_128l.png");
	public static final ResourceLocation BOATHOUSE_64LONG = new ResourceLocation("bibliocraft", "textures/paintings/boathouse_64l.png");
	public static final ResourceLocation JIMI_128FULL = new ResourceLocation("bibliocraft", "textures/paintings/jimi_128.png");
	public static final ResourceLocation JIMI_64FULL = new ResourceLocation("bibliocraft", "textures/paintings/jimi_64.png");
	public static final ResourceLocation JIMI_32FULL = new ResourceLocation("bibliocraft", "textures/paintings/jimi_32.png");
	public static final ResourceLocation RAVEN_128LONG = new ResourceLocation("bibliocraft", "textures/paintings/raven_128l.png");
	public static final ResourceLocation RAVEN_64LONG = new ResourceLocation("bibliocraft", "textures/paintings/raven_64l.png");
	public static final ResourceLocation RAVEN_32LONG = new ResourceLocation("bibliocraft", "textures/paintings/raven_32l.png");
	public static final ResourceLocation RAVEN_128FULL = new ResourceLocation("bibliocraft", "textures/paintings/raven_128.png");
	public static final ResourceLocation RAVEN_64FULL = new ResourceLocation("bibliocraft", "textures/paintings/raven_64.png");
	public static final ResourceLocation RAVEN_32FULL = new ResourceLocation("bibliocraft", "textures/paintings/raven_32.png");
	public static final ResourceLocation PAINTINGNOTFOUND = new ResourceLocation("bibliocraft", "textures/paintings/paintingnotfound.png");
	public static final ResourceLocation PAINTINGGUI = new ResourceLocation("bibliocraft", "textures/gui/paintinggui.png");
	public static final ResourceLocation PAINTINGPRESSGUI = new ResourceLocation("bibliocraft", "textures/gui/paintpressgui.png");
	public static final ResourceLocation PAINTINGPRESSBUTTONS = new ResourceLocation("bibliocraft", "textures/gui/paintpressguibuttons.png");
	
	public static final ResourceLocation BLACKWOOL = new ResourceLocation("textures/blocks/wool_colored_black.png");
	public static final ResourceLocation BLUEWOOL = new ResourceLocation("textures/blocks/wool_colored_blue.png");
	public static final ResourceLocation BROWNWOOL = new ResourceLocation("textures/blocks/wool_colored_brown.png");
	public static final ResourceLocation CYANWOOL = new ResourceLocation("textures/blocks/wool_colored_cyan.png");
	public static final ResourceLocation GRAYWOOL = new ResourceLocation("textures/blocks/wool_colored_gray.png");
	public static final ResourceLocation GREENWOOL = new ResourceLocation("textures/blocks/wool_colored_green.png");
	public static final ResourceLocation LBLUEWOOL = new ResourceLocation("textures/blocks/wool_colored_light_blue.png");
	public static final ResourceLocation LIMEWOOL = new ResourceLocation("textures/blocks/wool_colored_lime.png");
	public static final ResourceLocation MAGENTAWOOL = new ResourceLocation("textures/blocks/wool_colored_magenta.png");
	public static final ResourceLocation ORANGEWOOL = new ResourceLocation("textures/blocks/wool_colored_orange.png");
	public static final ResourceLocation PINKWOOL = new ResourceLocation("textures/blocks/wool_colored_pink.png");
	public static final ResourceLocation PURPLEWOOL = new ResourceLocation("textures/blocks/wool_colored_purple.png");
	public static final ResourceLocation REDWOOL = new ResourceLocation("textures/blocks/wool_colored_red.png");
	public static final ResourceLocation LGRAYWOOL = new ResourceLocation("textures/blocks/wool_colored_silver.png");
	public static final ResourceLocation WHITEWOOL = new ResourceLocation("textures/blocks/wool_colored_white.png");
	public static final ResourceLocation YELOOWWOOL = new ResourceLocation("textures/blocks/wool_colored_yellow.png");
	public static final ResourceLocation GLINT_PNG = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	
	public static final ResourceLocation PLANKSOAK = new ResourceLocation("textures/blocks/planks_oak.png");
	public static final ResourceLocation PAINTINGSHEET = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
	
	
	
	
	
	public static final SoundEvent SOUND_DING = new SoundEvent(new ResourceLocation("bibliocraft:ding"));
	
	public static final SoundEvent SOUND_TYPEWRITER_ADDPAPER = new SoundEvent(new ResourceLocation("bibliocraft:addpaper"));
	public static final SoundEvent SOUND_TYPEWRITER_TYPEING = new SoundEvent(new ResourceLocation("bibliocraft:typing"));
	public static final SoundEvent SOUND_TYPEWRITER_ENDBELL = new SoundEvent(new ResourceLocation("bibliocraft:endbell"));
	public static final SoundEvent SOUND_TYPEWRITER_REMOVEBOOK = new SoundEvent(new ResourceLocation("bibliocraft:removebook"));
	public static final SoundEvent SOUND_TYPEWRITER_TYPESINGLE = new SoundEvent(new ResourceLocation("bibliocraft:typingsingle"));
	
	public static final SoundEvent SOUND_CLOCK_TICK = new SoundEvent(new ResourceLocation("bibliocraft:tick"));
	public static final SoundEvent SOUND_CLOCK_TOCK = new SoundEvent(new ResourceLocation("bibliocraft:tock"));
	public static final SoundEvent SOUND_CLOCK_CHIME = new SoundEvent(new ResourceLocation("bibliocraft:woundchime"));
	public static final SoundEvent SOUND_CLOCK_WIND = new SoundEvent(new ResourceLocation("bibliocraft:wind"));
	
	public static final SoundEvent SOUND_ITEM_HANDDRILL = new SoundEvent(new ResourceLocation("bibliocraft:drill"));
	public static final SoundEvent SOUND_ITEM_SCREWGUN = new SoundEvent(new ResourceLocation("bibliocraft:screw"));
	
	public static final SoundEvent SOUND_CASE_OPEN = new SoundEvent(new ResourceLocation("bibliocraft:copen"));
	public static final SoundEvent SOUND_CASE_CLOSE = new SoundEvent(new ResourceLocation("bibliocraft:cclose"));
	
	public static final SoundEvent SOUND_TAPE_OPEN = new SoundEvent(new ResourceLocation("bibliocraft:tapeopen"));
	public static final SoundEvent SOUND_TAPE_CLOSE = new SoundEvent(new ResourceLocation("bibliocraft:tapeclose"));
	
	
	
	
	//https://www.minecraftforge.net/forum/topic/71577-1142-containers-and-guis/
	//public static final ContainerType<HopperContainer> field_221522_p = func_221505_a("hopper", HopperContainer::new);
	//public static final ContainerType<ContainerBookcase> bookcaseContainer = null;//ContainerTypeBuilder("bookcase", ContainerBookcase::new);
	/*
    private static <T extends Container> ContainerType<T> ContainerTypeBuilder(String p_221505_0_, ContainerType.IFactory<T> p_221505_1_) 
    {
    	return Registry.register(Registry.???, p_221505_0_, new ContainerType<>(p_221505_1_));
    }
	*/
	public void registerRenderers()
	{
		
	}
	
	//public static TileEntityType<TileEntityBookcase> TILE_BOOKCASE;
	/*
	public static TileEntityType<TileEntityShelf> TILE_SHELF;
	public static TileEntityType<TileEntityMarkerPole> TILE_MARKERPOLE;
	public static TileEntityType<TileEntityClipboard> TILE_CLIPBOARD;
	public static TileEntityType<BiblioLightTileEntity> TILE_LIGHT;
	public static TileEntityType<TileEntityFurniturePaneler> TILE_PANELER;
	public static TileEntityType<TileEntityPotionShelf> TILE_POTIONSHELF;
	public static TileEntityType<TileEntityToolRack> TILE_TOOLRACK;
	public static TileEntityType<TileEntityLabel> TILE_LABEL;
	public static TileEntityType<TileEntityDesk> TILE_DESK;
	public static TileEntityType<TileEntityTable> TILE_TABLE;
	public static TileEntityType<TileEntitySeat> TILE_SEAT;
	public static TileEntityType<TileEntityFancySign> TILE_FANCYSIGN;
	public static TileEntityType<TileEntityFancyWorkbench> TILE_FANCYWORKBENCH;
	public static TileEntityType<TileEntityFramedChest> TILE_FRAMEDCHEST;
	public static TileEntityType<TileEntityMapFrame> TILE_MAPFRAME;
	public static TileEntityType<TileEntityCase> TILE_CASE;
	public static TileEntityType<TileEntityClock> TILE_CLOCK;
	public static TileEntityType<TileEntityPaintingBorderless> TILE_PAINTING_BORDERLESS;
	public static TileEntityType<TileEntityPaintingFancy> TILE_PAINTING_FANCY;
	public static TileEntityType<TileEntityPaintingFlat> TILE_PAINTING_FLAT;
	public static TileEntityType<TileEntityPaintingMiddle> TILE_PAINTING_MIDDLE;
	public static TileEntityType<TileEntityPaintingSimple> TILE_PAINTING_SIMPLE;
	public static TileEntityType<TileEntityPaintPress> TILE_PAINTPRESS;
	public static TileEntityType<TileEntityArmorStand> TILE_ARMORSTAND;
	public static TileEntityType<TileEntityTypeMachine> TILE_TYPEMACHINE;
	public static TileEntityType<TileEntityPrintPress> TILE_PRINTPRESS;
	public static TileEntityType<TileEntityCookieJar> TILE_COOKIEJAR;
	public static TileEntityType<TileEntityDinnerPlate> TILE_DINNERPLATE;
	public static TileEntityType<TileEntityDiscRack> TILE_DISCRACK;
	public static TileEntityType<TileEntitySwordPedestal> TILE_SWORDPEDESTAL;
	public static TileEntityType<TileEntityBell> TILE_BELL;
	public static TileEntityType<TileEntityTypewriter> TILE_TYPEWRITER;
	*/
	
	
	public void initTileEntities()
	{
		// TODO well this is broken now too I guess
		//TILE_BOOKCASE = TileEntityType.register(BlockBookcase.name, TileEntityType.Builder.func_223042_a(TileEntityBookcase::new)); //func_223042_a == create
		/*
		TILE_SHELF = TileEntityType.register(BlockShelf.name, TileEntityType.Builder.create(TileEntityShelf::new));
		TILE_MARKERPOLE = TileEntityType.register(BlockMarkerPole.name, TileEntityType.Builder.create(TileEntityMarkerPole::new));
		TILE_CLIPBOARD = TileEntityType.register(BlockClipboard.name, TileEntityType.Builder.create(TileEntityClipboard::new));
		TILE_LIGHT = TileEntityType.register(BiblioLightBlock.name, TileEntityType.Builder.create(BiblioLightTileEntity::new));
		TILE_PANELER = TileEntityType.register(BlockFurniturePaneler.name, TileEntityType.Builder.create(TileEntityFurniturePaneler::new));
		TILE_POTIONSHELF = TileEntityType.register(BlockPotionShelf.name, TileEntityType.Builder.create(TileEntityPotionShelf::new));
		TILE_TOOLRACK = TileEntityType.register(BlockToolRack.name, TileEntityType.Builder.create(TileEntityToolRack::new));
		TILE_LABEL = TileEntityType.register(BlockLabel.name, TileEntityType.Builder.create(TileEntityLabel::new));
		TILE_DESK = TileEntityType.register(BlockDesk.name, TileEntityType.Builder.create(TileEntityDesk::new));
		TILE_TABLE = TileEntityType.register(BlockTable.name, TileEntityType.Builder.create(TileEntityTable::new));
		TILE_SEAT = TileEntityType.register(BlockSeat.name, TileEntityType.Builder.create(TileEntitySeat::new));
		TILE_FANCYSIGN = TileEntityType.register(BlockFancySign.name, TileEntityType.Builder.create(TileEntityFancySign::new));
		TILE_FANCYWORKBENCH = TileEntityType.register(BlockFancyWorkbench.name, TileEntityType.Builder.create(TileEntityFancyWorkbench::new));
		TILE_FRAMEDCHEST = TileEntityType.register(BlockFramedChest.name, TileEntityType.Builder.create(TileEntityFramedChest::new));
		TILE_MAPFRAME = TileEntityType.register(BlockMapFrame.name, TileEntityType.Builder.create(TileEntityMapFrame::new));
		TILE_CASE = TileEntityType.register(BlockCase.name, TileEntityType.Builder.create(TileEntityCase::new));
		TILE_CLOCK = TileEntityType.register(BlockClock.name, TileEntityType.Builder.create(TileEntityClock::new));
		TILE_PAINTING_BORDERLESS = TileEntityType.register(BlockPaintingFrameBorderless.name, TileEntityType.Builder.create(TileEntityPaintingBorderless::new));
		TILE_PAINTING_FANCY = TileEntityType.register(BlockPaintingFrameFancy.name, TileEntityType.Builder.create(TileEntityPaintingFancy::new));
		TILE_PAINTING_FLAT = TileEntityType.register(BlockPaintingFrameFlat.name, TileEntityType.Builder.create(TileEntityPaintingFlat::new));
		TILE_PAINTING_MIDDLE = TileEntityType.register(BlockPaintingFrameMiddle.name, TileEntityType.Builder.create(TileEntityPaintingMiddle::new));
		TILE_PAINTING_SIMPLE = TileEntityType.register(BlockPaintingFrameSimple.name, TileEntityType.Builder.create(TileEntityPaintingSimple::new));
		TILE_PAINTPRESS = TileEntityType.register(BlockPaintingPress.name, TileEntityType.Builder.create(TileEntityPaintPress::new));
		TILE_ARMORSTAND = TileEntityType.register(BlockArmorStand.name, TileEntityType.Builder.create(TileEntityArmorStand::new));
		TILE_TYPEMACHINE = TileEntityType.register(BlockTypesettingTable.name, TileEntityType.Builder.create(TileEntityTypeMachine::new));
		TILE_PRINTPRESS = TileEntityType.register(BlockPrintingPress.name, TileEntityType.Builder.create(TileEntityPrintPress::new));
		TILE_COOKIEJAR = TileEntityType.register(BlockCookieJar.name, TileEntityType.Builder.create(TileEntityCookieJar::new));
		TILE_DINNERPLATE = TileEntityType.register(BlockDinnerPlate.name, TileEntityType.Builder.create(TileEntityDinnerPlate::new));
		TILE_DISCRACK = TileEntityType.register(BlockDiscRack.name, TileEntityType.Builder.create(TileEntityDiscRack::new));
		TILE_SWORDPEDESTAL = TileEntityType.register(BlockSwordPedestal.name, TileEntityType.Builder.create(TileEntitySwordPedestal::new));
		TILE_BELL = TileEntityType.register(BlockBell.name, TileEntityType.Builder.create(TileEntityBell::new));
		TILE_TYPEWRITER = TileEntityType.register(BlockTypeWriter.name, TileEntityType.Builder.create(TileEntityTypewriter::new));
		*/
		/* is this gone with?
		if (Config.enableBookcase){
			GameRegistry.registerTileEntity(TileEntityBookcase.class, BlockBookcase.name);}
		if (Config.enableGenericshelf){
			GameRegistry.registerTileEntity(TileEntityShelf.class, BlockShelf.name);}
		if (Config.enableTapemeasure){
			GameRegistry.registerTileEntity(TileEntityMarkerPole.class, BlockMarkerPole.name);}
		if (Config.enableClipboard){
			GameRegistry.registerTileEntity(TileEntityClipboard.class, BlockClipboard.name);}
		if (Config.enableLamp || Config.enableLantern){
			GameRegistry.registerTileEntity(BiblioLightTileEntity.class, BiblioLightBlock.name);}
		if (Config.enableFurniturePaneler){
			GameRegistry.registerTileEntity(TileEntityFurniturePaneler.class, BlockFurniturePaneler.name);}
		if (Config.enablePotionshelf){
			GameRegistry.registerTileEntity(TileEntityPotionShelf.class, BlockPotionShelf.name);}
		if (Config.enableToolrack){
			GameRegistry.registerTileEntity(TileEntityToolRack.class, BlockToolRack.name);}
		if (Config.enableWoodLabel){
			GameRegistry.registerTileEntity(TileEntityLabel.class, BlockLabel.name);}
		if (Config.enableWritingdesk){
			GameRegistry.registerTileEntity(TileEntityDesk.class, BlockDesk.name);}
		if (Config.enableTable){
			GameRegistry.registerTileEntity(TileEntityTable.class, BlockTable.name);}
		if (Config.enableSeat){
			GameRegistry.registerTileEntity(TileEntitySeat.class, BlockSeat.name);}
		if (Config.enableFancySign){
			GameRegistry.registerTileEntity(TileEntityFancySign.class, BlockFancySign.name);}
		if (Config.enableFancyWorkbench){
			GameRegistry.registerTileEntity(TileEntityFancyWorkbench.class, BlockFancyWorkbench.name);}
		if (Config.enableFramedChest){
			GameRegistry.registerTileEntity(TileEntityFramedChest.class, BlockFramedChest.name);}
		if (Config.enableMapFrame){
			GameRegistry.registerTileEntity(TileEntityMapFrame.class, BlockMapFrame.name);}
		if (Config.enableWeaponcase){
			GameRegistry.registerTileEntity(TileEntityCase.class, BlockCase.name);}
		if (Config.enableClock){
			GameRegistry.registerTileEntity(TileEntityClock.class, BlockClock.name);}
		if (Config.enablePainting){
			GameRegistry.registerTileEntity(TileEntityPaintingBorderless.class, BlockPaintingFrameBorderless.name);
			GameRegistry.registerTileEntity(TileEntityPaintingFancy.class, BlockPaintingFrameFancy.name);
			GameRegistry.registerTileEntity(TileEntityPaintingFlat.class, BlockPaintingFrameFlat.name);
			GameRegistry.registerTileEntity(TileEntityPaintingMiddle.class, BlockPaintingFrameMiddle.name);
			GameRegistry.registerTileEntity(TileEntityPaintingSimple.class, BlockPaintingFrameSimple.name);
			GameRegistry.registerTileEntity(TileEntityPaintPress.class, BlockPaintingPress.name);}
		if (Config.enableArmorstand){
			GameRegistry.registerTileEntity(TileEntityArmorStand.class, BlockArmorStand.name);}
		if (Config.enablePrintpressTypeMachine){
			GameRegistry.registerTileEntity(TileEntityTypeMachine.class, BlockTypesettingTable.name);
			GameRegistry.registerTileEntity(TileEntityPrintPress.class, BlockPrintingPress.name);}
		if (Config.enableCookieJar){
			GameRegistry.registerTileEntity(TileEntityCookieJar.class, BlockCookieJar.name);}
		if (Config.enableDinnerPlate){
			GameRegistry.registerTileEntity(TileEntityDinnerPlate.class, BlockDinnerPlate.name);}
		if (Config.enableDiscRack){
			GameRegistry.registerTileEntity(TileEntityDiscRack.class, BlockDiscRack.name);}
		if (Config.enableSwordPedestal){
			GameRegistry.registerTileEntity(TileEntitySwordPedestal.class, BlockSwordPedestal.name);}
		if (Config.enableDeskBell){
			GameRegistry.registerTileEntity(TileEntityBell.class, BlockBell.name);}
		if (Config.enableTypewriter){
			GameRegistry.registerTileEntity(TileEntityTypewriter.class, BlockTypeWriter.name);}
			*/
	}
	
	public void initNetwork()
	{
	}

}
