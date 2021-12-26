package jds.bibliocraft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.blocks.BlockBookcaseCreative;
import jds.bibliocraft.blocks.TestBlock;
/*
import jds.bibliocraft.blocks.BlockArmorStand;
import jds.bibliocraft.blocks.BlockBell;
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
import jds.bibliocraft.blocks.BlockLampGold;
import jds.bibliocraft.blocks.BlockLampIron;
import jds.bibliocraft.blocks.BlockLanternGold;
import jds.bibliocraft.blocks.BlockLanternIron;
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
import jds.bibliocraft.blocks.blockitems.BlockItemArmorStand;

import jds.bibliocraft.blocks.blockitems.BlockItemCase;
import jds.bibliocraft.blocks.blockitems.BlockItemClock;
import jds.bibliocraft.blocks.blockitems.BlockItemDesk;
import jds.bibliocraft.blocks.blockitems.BlockItemFancySign;
import jds.bibliocraft.blocks.blockitems.BlockItemFancyWorkbench;
import jds.bibliocraft.blocks.blockitems.BlockItemFramedChest;
import jds.bibliocraft.blocks.blockitems.BlockItemFurniturePaneler;
import jds.bibliocraft.blocks.blockitems.BlockItemLabel;
import jds.bibliocraft.blocks.blockitems.BlockItemLamp;
import jds.bibliocraft.blocks.blockitems.BlockItemLantern;
import jds.bibliocraft.blocks.blockitems.BlockItemMapFrame;
import jds.bibliocraft.blocks.blockitems.BlockItemPaintingFrameBorderless;
import jds.bibliocraft.blocks.blockitems.BlockItemPaintingFrameFancy;
import jds.bibliocraft.blocks.blockitems.BlockItemPaintingFrameFlat;
import jds.bibliocraft.blocks.blockitems.BlockItemPaintingFrameMiddle;
import jds.bibliocraft.blocks.blockitems.BlockItemPaintingFrameSimple;
import jds.bibliocraft.blocks.blockitems.BlockItemPotionShelf;
import jds.bibliocraft.blocks.blockitems.BlockItemSeat;
import jds.bibliocraft.blocks.blockitems.BlockItemShelf;
import jds.bibliocraft.blocks.blockitems.BlockItemSwordPedestal;
import jds.bibliocraft.blocks.blockitems.BlockItemTable;
import jds.bibliocraft.blocks.blockitems.BlockItemToolRack;
import jds.bibliocraft.blocks.blockitems.BlockItemTypewriter;
*/
import jds.bibliocraft.blocks.blockitems.BlockItemBookcase;
import jds.bibliocraft.blocks.blockitems.BlockItemBookcaseCreative;
import jds.bibliocraft.blocks.blockitems.BlockItemTestBlock;
import jds.bibliocraft.helpers.EnumWoodsType;


public class BlockLoader 
{	
    public static String[] dyes =
    {
        "dyeWhite",
        "dyeLightGray",
        "dyeGray",
        "dyeBlack",
        "dyeRed",
        "dyeOrange",
        "dyeYellow",
        "dyeLime",
        "dyeGreen",
        "dyeCyan",
        "dyeLightBlue",
        "dyeBlue",
        "dyePurple",
        "dyeMagenta",
        "dyePink",
        "dyeBrown"
    };
	
    public static String[] dyes2 =
    {        
        "dyeBlack",
        "dyeRed",
        "dyeGreen",
        "dyeBrown",
        "dyeBlue",
        "dyePurple",
        "dyeCyan",
        "dyeLightGray",
        "dyeGray",
        "dyePink",
        "dyeLime",
        "dyeYellow",
        "dyeLightBlue",
        "dyeMagenta",
        "dyeOrange",
        "dyeWhite"
        
    };
	
	//public static int creativetabID = CreativeTabs.getNextID();
	
	//public static final ItemGroup biblioTab = new BiblioTab("BiblioCraft");
	//public static final ItemGroup biblioLightsTab = new BiblioLightsTab("BiblioCraftLights");
	//public static void initLightTab()
	//{
	//	biblioLightsTab = new BiblioLightsTab("BiblioCraftLights");
	//}
	
	/** The number of woods starting at 0 so easy use in loops.  */
	public final static int NUMBER_OF_WOODS = 6;
	
	
	
	/** BiblioBlocks */ 
	/*
	@ObjectHolder(BlockBookcase.name)
	public static final Block bookcase_block = BlockBookcase.instance;
	@ObjectHolder(BlockBookcase.name)
	public static final Item bookcase_item = BlockItemBookcase.instance;
	*/
	
	public static final BlockBookcase[] bookcases = {new BlockBookcase(EnumWoodsType.oak), 
													 new BlockBookcase(EnumWoodsType.spruce), 
													 new BlockBookcase(EnumWoodsType.birch), 
													 new BlockBookcase(EnumWoodsType.jungle), 
													 new BlockBookcase(EnumWoodsType.acacia), 
													 new BlockBookcase(EnumWoodsType.darkoak), 
													 new BlockBookcase(EnumWoodsType.framed)};
	
	public static final BlockItemBookcase[] bookcaseItems = {new BlockItemBookcase(bookcases[0], EnumWoodsType.oak),
															 new BlockItemBookcase(bookcases[1], EnumWoodsType.spruce),
															 new BlockItemBookcase(bookcases[2], EnumWoodsType.birch),
															 new BlockItemBookcase(bookcases[3], EnumWoodsType.jungle),
															 new BlockItemBookcase(bookcases[4], EnumWoodsType.acacia),
															 new BlockItemBookcase(bookcases[5], EnumWoodsType.darkoak),
															 new BlockItemBookcase(bookcases[6], EnumWoodsType.framed)};
	
	public static final TestBlock tblock = new TestBlock();
	public static final BlockItemTestBlock tblockitem = new BlockItemTestBlock(tblock, "testblock");
	
	public static void initBlocks(RegistryEvent.Register<Block> event)
	{
		if (Config.enableBookcase)
		{
			for (int i = 0; i < bookcases.length; i++)
			{
				event.getRegistry().register(bookcases[i]);
			}
			event.getRegistry().register(tblock);
			//event.getRegistry().register(bookcases[0]);
			//event.getRegistry().register(BlockBookcaseCreative.instance);
		}
		if (Config.enableGenericshelf)
		{	
			//event.getRegistry().register(BlockShelf.instance);
		}
		if (Config.enableTapemeasure)
		{
			//event.getRegistry().register(BlockMarkerPole.instance);
		}
		if (Config.enableClipboard)
		{
			//event.getRegistry().register(BlockClipboard.instance);
		}
		
		if (Config.enableLantern)
		{	
			//event.getRegistry().register(BlockLanternGold.instance);
			//event.getRegistry().register(BlockLanternIron.instance);
		}
		if (Config.enableLamp)
		{	
			//event.getRegistry().register(BlockLampGold.instance);
			//event.getRegistry().register(BlockLampIron.instance);
		}
		if (Config.enableFurniturePaneler)
		{
			//event.getRegistry().register(BlockFurniturePaneler.instance);
		}
		if (Config.enableFramedChest)
		{
			//event.getRegistry().register(BlockFramedChest.instance);
		}
		if (Config.enableFancySign)
		{
			//event.getRegistry().register(BlockFancySign.instance);
		}
		if (Config.enableFancyWorkbench)
		{
			//event.getRegistry().register(BlockFancyWorkbench.instance);
		}
		if (Config.enablePotionshelf)
		{
			//event.getRegistry().register(BlockPotionShelf.instance);
		}
		if (Config.enableToolrack)
		{	
			//event.getRegistry().register(BlockToolRack.instance);
		}
		if (Config.enableWoodLabel)
		{	
			//event.getRegistry().register(BlockLabel.instance);
		}
		if (Config.enableWritingdesk)
		{	
			//event.getRegistry().register(BlockDesk.instance);
		}
		if (Config.enableTable)
		{	
			//event.getRegistry().register(BlockTable.instance);
		}
		if (Config.enableSeat)
		{
			//event.getRegistry().register(BlockSeat.instance);
		}
		
		if (Config.enableClock)
		{
			//event.getRegistry().register(BlockClock.instance);
		}
		if (Config.enableWeaponcase)
		{	
			//event.getRegistry().register(BlockCase.instance);
		}
		if (Config.enableMapFrame)
		{
			//event.getRegistry().register(BlockMapFrame.instance);
		}
		
		if (Config.enablePainting)
		{
			//event.getRegistry().register(BlockPaintingFrameFlat.instance);
			//event.getRegistry().register(BlockPaintingFrameSimple.instance);
			//event.getRegistry().register(BlockPaintingFrameMiddle.instance);
			//event.getRegistry().register(BlockPaintingFrameFancy.instance);
			//event.getRegistry().register(BlockPaintingFrameBorderless.instance);

			//event.getRegistry().register(BlockPaintingPress.instance);
		}
		if (Config.enableTypewriter)
		{
			//event.getRegistry().register(BlockTypeWriter.instance);
		}
		if (Config.enableSwordPedestal)
		{
			//event.getRegistry().register(BlockSwordPedestal.instance);
		}
		if (Config.enableArmorstand)
		{	
			//event.getRegistry().register(BlockArmorStand.instance);
		}
		if (Config.enableDeskBell)
		{
			//event.getRegistry().register(BlockBell.instance); 
		}
		if (Config.enablePrintpressTypeMachine)
		{	
			//event.getRegistry().register(BlockTypesettingTable.instance);
			//event.getRegistry().register(BlockPrintingPress.instance); 
		}
		if (Config.enableCookieJar)
		{
			//event.getRegistry().register(BlockCookieJar.instance); 
		}
		if (Config.enableDinnerPlate)
		{
			//event.getRegistry().register(BlockDinnerPlate.instance); 
		}
		if (Config.enableDiscRack)
		{
			//event.getRegistry().register(BlockDiscRack.instance); 
		}
	}
	
	public static void initBlockItems(RegistryEvent.Register<Item> event)
	{
		if (Config.enableBookcase)
		{
			for (int i = 0; i < bookcaseItems.length; i++)
			{
				event.getRegistry().register(bookcaseItems[i]);
			}
			event.getRegistry().register(tblockitem);
			//event.getRegistry().register(bookcaseItems[0]);
			
			//event.getRegistry().register(BlockItemBookcase.instance);

			//event.getRegistry().register(BlockItemBookcaseCreative.instance); 
		}
		/*
		if (Config.enableGenericshelf)
		{	
			event.getRegistry().register(BlockItemShelf.instance); 
		}
		if (Config.enableTapemeasure)
		{
			
			event.getRegistry().register(new ItemBlock(BlockMarkerPole.instance, new Item.Properties()).setRegistryName(BlockMarkerPole.name)); 
		}
		if (Config.enableClipboard)
		{
			event.getRegistry().register(new ItemBlock(BlockClipboard.instance, new Item.Properties()).setRegistryName(BlockClipboard.name)); 
		}
		
		if (Config.enableLantern)
		{	
			event.getRegistry().register(BlockItemLantern.instanceGold); 
			event.getRegistry().register(BlockItemLantern.instanceIron); 
		}
		if (Config.enableLamp)
		{	
			event.getRegistry().register(BlockItemLamp.instanceGold); 
			event.getRegistry().register(BlockItemLamp.instanceIron); 
		}
		if (Config.enableFurniturePaneler)
		{
			event.getRegistry().register(BlockItemFurniturePaneler.instance); 
		}
		if (Config.enableFramedChest)
		{
			event.getRegistry().register(BlockItemFramedChest.instance); 
		}
		if (Config.enableFancySign)
		{
			event.getRegistry().register(BlockItemFancySign.instance); 
		}
		if (Config.enableFancyWorkbench)
		{
			event.getRegistry().register(BlockItemFancyWorkbench.instance); 
		}
		if (Config.enablePotionshelf)
		{
			event.getRegistry().register(BlockItemPotionShelf.instance); 
		}
		if (Config.enableToolrack)
		{	
			event.getRegistry().register(BlockItemToolRack.instance); 
		}
		if (Config.enableWoodLabel)
		{	
			event.getRegistry().register(BlockItemLabel.instance); 
		}
		if (Config.enableWritingdesk)
		{	
			event.getRegistry().register(BlockItemDesk.instance); 
		}
		if (Config.enableTable)
		{	
			event.getRegistry().register(BlockItemTable.instance); 
		}
		if (Config.enableSeat)
		{
			event.getRegistry().register(BlockItemSeat.instance); 
		}
		
		if (Config.enableClock)
		{
			event.getRegistry().register(BlockItemClock.instance); 
		}
		if (Config.enableWeaponcase)
		{	
			event.getRegistry().register(BlockItemCase.instance); 
		}
		if (Config.enableMapFrame)
		{
			event.getRegistry().register(BlockItemMapFrame.instance); 
		}
		
		if (Config.enablePainting)
		{
			event.getRegistry().register(BlockItemPaintingFrameFlat.instance); 
			event.getRegistry().register(BlockItemPaintingFrameSimple.instance); 
			event.getRegistry().register(BlockItemPaintingFrameMiddle.instance); 
			event.getRegistry().register(BlockItemPaintingFrameFancy.instance); 
			event.getRegistry().register(BlockItemPaintingFrameBorderless.instance); 
			event.getRegistry().register(new ItemBlock(BlockPaintingPress.instance, new Item.Properties()).setRegistryName(BlockPaintingPress.name)); 
		}
		if (Config.enableTypewriter)
		{
			event.getRegistry().register(BlockItemTypewriter.instance); 
		}
		if (Config.enableSwordPedestal)
		{
			event.getRegistry().register(BlockItemSwordPedestal.instance); 
		}
		if (Config.enableArmorstand)
		{	
			event.getRegistry().register(BlockItemArmorStand.instance); 
		}
		if (Config.enableDeskBell)
		{
			event.getRegistry().register(new ItemBlock(BlockBell.instance, new Item.Properties()).setRegistryName(BlockBell.name)); 
		}
		if (Config.enablePrintpressTypeMachine)
		{	
			event.getRegistry().register(new ItemBlock(BlockTypesettingTable.instance, new Item.Properties()).setRegistryName(BlockTypesettingTable.name)); 
			event.getRegistry().register(new ItemBlock(BlockPrintingPress.instance, new Item.Properties()).setRegistryName(BlockPrintingPress.name)); 
		}
		if (Config.enableCookieJar)
		{
			event.getRegistry().register(new ItemBlock(BlockCookieJar.instance, new Item.Properties()).setRegistryName(BlockCookieJar.name)); 
		}
		if (Config.enableDinnerPlate)
		{
			event.getRegistry().register(new ItemBlock(BlockDinnerPlate.instance, new Item.Properties()).setRegistryName(BlockDinnerPlate.name)); 
		}
		if (Config.enableDiscRack)
		{
			event.getRegistry().register(new ItemBlock(BlockDiscRack.instance, new Item.Properties()).setRegistryName(BlockDiscRack.name)); 
		}
		*/
	}
}
