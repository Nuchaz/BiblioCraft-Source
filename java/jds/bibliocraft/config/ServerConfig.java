package jds.bibliocraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig 
{

	// enable / disable blocks
	public static ForgeConfigSpec.BooleanValue enableBookcase;
	public static ForgeConfigSpec.BooleanValue enableArmorstand;
	public static ForgeConfigSpec.BooleanValue enablePotionshelf;
	public static ForgeConfigSpec.BooleanValue enableToolrack;
	public static ForgeConfigSpec.BooleanValue enableWeaponcase;
	public static ForgeConfigSpec.BooleanValue enableGenericshelf;
	public static ForgeConfigSpec.BooleanValue enableWoodLabel;
	public static ForgeConfigSpec.BooleanValue enableWritingdesk;
	public static ForgeConfigSpec.BooleanValue enableTable;
	public static ForgeConfigSpec.BooleanValue enablePrintpressTypeMachine; 
	public static ForgeConfigSpec.BooleanValue enableLamp;
	public static ForgeConfigSpec.BooleanValue enableLantern;
	public static ForgeConfigSpec.BooleanValue enableCookieJar;
	public static ForgeConfigSpec.BooleanValue enableDinnerPlate;
	public static ForgeConfigSpec.BooleanValue enableDiscRack;
	public static ForgeConfigSpec.BooleanValue enableMapFrame; 
	public static ForgeConfigSpec.BooleanValue enableSeat; 
	public static ForgeConfigSpec.BooleanValue enableRedstonebook;
	public static ForgeConfigSpec.BooleanValue enableReadingglasses;
	public static ForgeConfigSpec.BooleanValue enableTapemeasure; 
	public static ForgeConfigSpec.BooleanValue enableDrill;
	public static ForgeConfigSpec.BooleanValue enableLock;
	public static ForgeConfigSpec.BooleanValue enableClipboard;
	public static ForgeConfigSpec.BooleanValue enableWaypointCompass;
	public static ForgeConfigSpec.BooleanValue enableSwordPedestal;
	public static ForgeConfigSpec.BooleanValue enableFancyWorkbench;
	public static ForgeConfigSpec.BooleanValue enableFancySign;
	public static ForgeConfigSpec.BooleanValue enableBigBook;
	public static ForgeConfigSpec.BooleanValue enableSlottedBook;
	public static ForgeConfigSpec.BooleanValue enableDeskBell;
	public static ForgeConfigSpec.BooleanValue enableHandDrill;
	public static ForgeConfigSpec.BooleanValue enableTypewriter;
	public static ForgeConfigSpec.BooleanValue enableClock;
	public static ForgeConfigSpec.BooleanValue enablePainting; // painting will cover all the frames, the painting press, and the canvas
	public static ForgeConfigSpec.BooleanValue enableTesterItem;
	public static ForgeConfigSpec.BooleanValue enableAtlas;
	public static ForgeConfigSpec.BooleanValue enableDeathCompass;
	public static ForgeConfigSpec.BooleanValue enableFurniturePaneler;
	public static ForgeConfigSpec.BooleanValue enablePlumbLine;
	public static ForgeConfigSpec.BooleanValue enableFramedChest;
	public static ForgeConfigSpec.BooleanValue enableStockroomCatalog;
	
	
	public static ForgeConfigSpec.BooleanValue enableLockRecipe;
	public static ForgeConfigSpec.BooleanValue enableRecipeBookCrafting;
	public static ForgeConfigSpec.BooleanValue enablePublicTypesettingBooks;
	
	public static ForgeConfigSpec.IntValue enchantmentMultiplyer; 
	public static ForgeConfigSpec.IntValue enchPlateMaxUses;
	
	public static ForgeConfigSpec.BooleanValue chairRedstone;
	
	public static ForgeConfigSpec.IntValue defaultBigBookTextScale; // = 0 ?
	
	
	public ServerConfig()
	{
		
	}
	
	public static ForgeConfigSpec makeServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		
		builder.push("White Lists");
		BooksConfig.allowedBooks = builder
				.comment("stuff about it")
				.define("bookWhiteList", "book, map, journal, plan, thaumonomicon, necronomicon, lexicon, print, notes, spell, library, tome, encyclopedia");

		
		PotionsConfig.additionalPotions = builder
				.comment("stuff about it")
				.define("potionWhiteList", "default values");
		ToolsConfig.additionalTools = builder
				.comment("stuff about it")
				.define("toolsWhiteList", "default values");
		DiscsConfig.additionalDiscs = builder
				.comment("stuff about it")
				.define("discsWhiteList", "default values");
		builder.pop();
		
		builder.push("General");
		enableLockRecipe = builder
				.comment("stuff about it")
				.define("enableLockRecipe", false);
		enableRecipeBookCrafting = builder
				.comment("This option enables crafting to be done inside a recipe book with no workbench.")
				.define("enableRecipeBookCrafting", true);
		builder.pop();
		// should I make all the book and potion, tools and discs check from here?, than only the server would have to modifier their configs, that would be great.
		
		// enable / disable blocks from here, so it can be done on a per-world basis
		//doFade = builder.define("testy test", true);
		return builder.build();
	}
	
	public static void loadConfig() // TODO I need to run this after the world loads. or maybe 
	{
		BooksConfig.books = BooksConfig.allowedBooks.get().split(",");
		for (int i = 0; i < BooksConfig.books.length; i++)
		{
			System.out.println(BooksConfig.books[i]);
		}
	}
}
