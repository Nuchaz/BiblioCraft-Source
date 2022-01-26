package jds.bibliocraft;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config 
{
	//public static int readingenchantID;
	//public static int deathcompenchantID;
	public static int enchantmentMultiplyer;
	public static boolean disablerenderers;
	public static String allowedBooks;
	public static String[] books;
	public static String additionalTools;
	public static String[] tools;
	public static String additionalPotions;
	public static String[] potions;
	//public static int color; //= 16777215;//553648127;
	//public static int color2 = 16711935;
	//public static boolean textshadow;
	public static int enchPlateMaxUses;
	public static boolean chairRedstone;
	public static int mapUpdateRate;
	public static boolean checkforupdate;
	public static String additionalDiscs;
	public static String[] discs;
	public static String[] witcheryPotions = {"ingredient.brew", "ingredient.clayJar", "ingredient.foulFume", "ingredient.diamondVapour", "ingredient.oilOfVitriol", "ingredient.exhaleOfTheHornedOne", "ingredient.breathOfTheGoddess", "ingredient.hintOfRebirth", "ingredient.whiffOfMagic", "ingredient.reekOfMisfortune", "ingredient.odourOfPurity", "ingredient.tearOfTheGoddess", "ingredient.dropOfLuck", "ingredient.redstoneSoup", "ingredient.flyingOintment", "ingredient.ghostOfTheLight", "ingredient.soulOfTheWorld", "ingredient.spiritOfOtherwhere", "ingredient.infernalAnimus", "ingredient.enderDew", "ingredient.infernalblood", "ingredient.mysticunguent"};
	public static String lastCheckedversion = "";
	public static int defaultBigBookTextScale = 0;
	public static double renderDistancePainting;
	public static Configuration bConfig;
	

	public static boolean emitLight;

	public static boolean enableBookcase;
	public static boolean enableArmorstand;
	public static boolean enablePotionshelf;
	public static boolean enableToolrack;
	public static boolean enableWeaponcase;
	public static boolean enableGenericshelf;
	public static boolean enableWoodLabel;
	public static boolean enableWritingdesk;
	public static boolean enableTable;
	public static boolean enablePrintpressTypeMachine; 
	public static boolean enableLamp;
	public static boolean enableLantern;
	public static boolean enableCookieJar;
	public static boolean enableDinnerPlate;
	public static boolean enableDiscRack;
	public static boolean enableMapFrame; 
	public static boolean enableSeat; 
	public static boolean enableRedstonebook;
	public static boolean enableReadingglasses;
	public static boolean enableTapemeasure; 
	public static boolean enableDrill;
	public static boolean enableLock;
	public static boolean enableClipboard;
	public static boolean enableWaypointCompass;
	public static boolean enableSwordPedestal;
	public static boolean enableFancyWorkbench;
	public static boolean enableFancySign;
	public static boolean enableBigBook;
	public static boolean enableSlottedBook;
	public static boolean enableDeskBell;
	public static boolean enableHandDrill;
	public static boolean enableTypewriter;
	public static boolean enableClock;
	public static boolean enablePainting; // painting will cover all the frames, the painting press, and the canvas
	public static boolean enableTesterItem;
	public static boolean enableAtlas;
	public static boolean enableDeathCompass;
	public static boolean enableFurniturePaneler;
	public static boolean enablePlumbLine;
	
	public static boolean enableFramedChest;
	public static boolean enableStockroomCatalog;
	
	public static boolean enableLockRecipe;
	public static boolean enableRecipeBookCrafting;
	public static boolean enablePublicTypesettingBooks;
	
	
	public static boolean forceFastRenderShelf;
	public static boolean forceFastRenderPotionShelf;
	public static boolean forceFastRenderFancySign;
	public static boolean forceFastRenderLabel;
	public static boolean forceFastRenderTabel;
	public static boolean forceFastRenderCase;
	public static boolean forceFastRenderToolrack;
	public static boolean forceFastRenderDinnerPlate;
	public static boolean forceFastRenderDiscRack;
	
	
	public static void init(FMLPreInitializationEvent event)
	{
		bConfig = new Configuration(event.getSuggestedConfigurationFile());
		bConfig.load();
		loadConfig();
		
		FMLCommonHandler.instance().bus().register(new ConfigUpdateListener());
	}
	
	public static void loadConfig()
	{
		bConfig.addCustomCategoryComment("Blocks Enabled", "Here you can disable or re-enable any blocks or items you choose. Change value to false to disable selected block or item.");
		enableBookcase = bConfig.get("Blocks Enabled", "Bookcase", true).getBoolean(true);
		enableArmorstand = bConfig.get("Blocks Enabled", "Armorstand", true).getBoolean(true);
		enablePotionshelf = bConfig.get("Blocks Enabled", "Potionshelf", true).getBoolean(true);
		enableToolrack = bConfig.get("Blocks Enabled", "Toolrack", true).getBoolean(true);
		enableWeaponcase = bConfig.get("Blocks Enabled", "Weaponcase", true).getBoolean(true);
		enableGenericshelf = bConfig.get("Blocks Enabled", "Shelf", true).getBoolean(true);
		enableWoodLabel = bConfig.get("Blocks Enabled", "WoodLabel", true).getBoolean(true);
		enableWritingdesk = bConfig.get("Blocks Enabled", "WoodDesk", true).getBoolean(true);
		enableTable = bConfig.get("Blocks Enabled", "Table", true).getBoolean(true);
		enablePrintpressTypeMachine = bConfig.get("Blocks Enabled", "PrintpressTypeMachine", true).getBoolean(true);
		enableLamp = bConfig.get("Blocks Enabled", "Lamp", true).getBoolean(true);
		enableLantern = bConfig.get("Blocks Enabled", "Lantern", true).getBoolean(true);
		enableCookieJar = bConfig.get("Blocks Enabled", "CookieJar", true).getBoolean(true);
		enableDinnerPlate = bConfig.get("Blocks Enabled", "DinnerPlate", true).getBoolean(true);
		enableDiscRack = bConfig.get("Blocks Enabled", "DiscRack", true).getBoolean(true);
		enableMapFrame = bConfig.get("Blocks Enabled", "MapFrame", true).getBoolean(true);
		enableSeat = bConfig.get("Blocks Enabled", "Seat", true).getBoolean(true);
		enableRedstonebook = bConfig.get("Blocks Enabled", "Redstonebook", true).getBoolean(true);
		enableReadingglasses = bConfig.get("Blocks Enabled", "Readingglasses", true).getBoolean(true);
		enableTapemeasure = bConfig.get("Blocks Enabled", "Tapemeasure", true).getBoolean(true);
		enableDrill = bConfig.get("Blocks Enabled", "ScrewGun", true).getBoolean(true);
		enableLock = bConfig.get("Blocks Enabled", "Lock", true).getBoolean(true);
		enableClipboard = bConfig.get("Blocks Enabled", "Clipboard", true).getBoolean(true);
		enableWaypointCompass = bConfig.get("Blocks Enabled", "WaypointCompass", true).getBoolean(true);
		enableSwordPedestal = bConfig.get("Blocks Enabled", "SwordPedestal", true).getBoolean(true);
		enableFancyWorkbench = bConfig.get("Blocks Enabled", "FancyWorkbench", true).getBoolean(true);
		enableFancySign = bConfig.get("Blocks Enabled", "FancySign", true).getBoolean(true);
		enableBigBook = bConfig.get("Blocks Enabled", "BigBook", true).getBoolean(true);
		enableSlottedBook = bConfig.get("Blocks Enabled", "SlottedBook", true).getBoolean(true);
		enableDeskBell = bConfig.get("Blocks Enabled", "DeskBell", true).getBoolean(true);
		enableHandDrill = bConfig.get("Blocks Enabled", "HandDrill", true).getBoolean(true);
		enableTypewriter = bConfig.get("Blocks Enabled", "Typewriter", true).getBoolean(true);
		enableClock = bConfig.get("Blocks Enabled", "Clock", true).getBoolean(true);
		enablePainting = bConfig.get("Blocks Enabled", "PaintingFrames", true).getBoolean(true);
		enableTesterItem = bConfig.get("Blocks Enabled", "TesterItem", true).getBoolean(true);
		enableAtlas = bConfig.get("Blocks Enabled", "AtlasBook", true).getBoolean(true);
		enableDeathCompass = bConfig.get("Blocks Enabled", "AtlasEternalCompass", true).getBoolean(true);
		enableFurniturePaneler = bConfig.get("Blocks Enabled",  "FurniturePaneler", true).getBoolean(true);
		enablePlumbLine = bConfig.get("Blocks Enabled",  "PlumbLine", true).getBoolean(true);
		enableLockRecipe = bConfig.get(Configuration.CATEGORY_GENERAL, "EnableLockAndKeyRecipe", false).getBoolean(false);
		enableFramedChest = bConfig.get("Blocks Enabled",  "FramedChest", true).getBoolean(true);
		enableStockroomCatalog = bConfig.get("Blocks Enabled",  "StockroomCatalog", true).getBoolean(true);
		
		enableRecipeBookCrafting = bConfig.get(Configuration.CATEGORY_GENERAL,  "EnableCraftingWithRecipieBooks", true, "This option enables crafting to be done inside a recipe book with no workbench.").getBoolean(true);
		enablePublicTypesettingBooks = bConfig.get(Configuration.CATEGORY_GENERAL,  "EnablePublicTypesettingBooks", false, "This option forces all new books added to the typesetting table to be public").getBoolean(false);
		
		
		
		forceFastRenderShelf = bConfig.get("Force Fast Render",  "Shelf", false).getBoolean(false);
		forceFastRenderPotionShelf = bConfig.get("Force Fast Render",  "PotionShelf", true).getBoolean(true);
		forceFastRenderFancySign = bConfig.get("Force Fast Render",  "FancySign", true).getBoolean(true);
		forceFastRenderLabel = bConfig.get("Force Fast Render",  "Label", true).getBoolean(true);
		forceFastRenderTabel = bConfig.get("Force Fast Render",  "Table", false).getBoolean(false);
		forceFastRenderCase = bConfig.get("Force Fast Render",  "Case", false).getBoolean(false);
		forceFastRenderToolrack = bConfig.get("Force Fast Render",  "ToolRack", false).getBoolean(false);
		forceFastRenderDinnerPlate = bConfig.get("Force Fast Render",  "DinnerPlate", false).getBoolean(false);
		forceFastRenderDiscRack = bConfig.get("Force Fast Render",  "DiscRack", false).getBoolean(false);
				
		//readingenchantID = bConfig.get("Custom Enchantments", "ReadingEnchant", 196).getInt();
		//deathcompenchantID = bConfig.get("Custom Enchantments", "DeathCompassEnchant", 197).getInt();

		//bConfig.addCustomCategoryComment("Text Colors", "This is were you can change the color of the text rendered on blocks when using the reading glasses. The value given is a combined RGB value. For ex. 16777215=white, 255=red, 65280=green, 16711680=blue, 16776960=aqua. A google search for these values can assit in finding a chart to show colors and codes.");
		//color = bConfig.get("Text Colors", "ReadingGlassesTextColor", 16777215).getInt();
		
		emitLight = bConfig.get(Configuration.CATEGORY_GENERAL, "EnableLightEmission", true, "Setting this to false will disable light emission from the Shelf, Label, Display Case and Table when a block that gives off light is placed on them.").getBoolean(true);
		//allowEmptyBooks = bConfig.get(Configuration.CATEGORY_GENERAL, "AllowEmptyBooks", false, "Should blank books be allowed on Bookcases? (true or false)").getBoolean(false);
		allowedBooks = bConfig.get(Configuration.CATEGORY_GENERAL, 
				"AllowedBooks","book, map, journal, plan, thaumonomicon, necronomicon, lexicon, print, notes, spell, library, tome, encyclopedia", 
				"These are the keywords that are compared against the item names to determine if the item can be placed on a Bookcase. Add more keywords if needed."
				).getString();//.value.split(",");
		books = allowedBooks.split(",");
		additionalTools = bConfig.get(Configuration.CATEGORY_GENERAL, 
				"AdditionalTools", "sprayer, wand, rod, scepter, wrench, screwdriver, meter, handsaw, gun, cutter, scoop, soldering, painter, reader, shovel, grafter, pickaxe, pipette, magnifying, sword, axe, hammer",
				"These are the names of additional tools that can be added to the Tool Rack. Added keywords will allow additional items to be placed on this block."
				).getString();//.value.split(",");
		tools = additionalTools.split(",");
		additionalPotions = bConfig.get(Configuration.CATEGORY_GENERAL,
				"AdditionalPotions","essence, mead, bottle, test, element, molecule, can, capsule, cell, catalyst, ambrosia, honey pot, dissipation, vial, juice",
				"These are keywords that add additional support for more types of potions and items. Add more keyworks if you wish to allow more types of items to be displayed."
				).getString(); //.value.split(",");
		potions = additionalPotions.split(",");
		additionalDiscs = bConfig.get(Configuration.CATEGORY_GENERAL, "AdditionalDiscs", 
				"disc, disk", 
				"These are keywords that add additional support for more types of discs and items that are allowed to be placed on the disc rack. Add more keywords if you wish to allow more types of items to be displayed"
				).getString();
		discs = additionalDiscs.split(",");
		//textshadow = bConfig.get(Configuration.CATEGORY_GENERAL,  "RenderTextShadow", false, "Setting to true renders a shadow behind the text seen with the reading glasses. This can improve visability quite a bit, but sometimes doesn't render correctly. Try at your own discretion.").getBoolean(false);
		disablerenderers = bConfig.get(Configuration.CATEGORY_GENERAL, "DisableRenderers", false, "Setting this to true will disable all renderers. This will allow a world to be loaded and a problem item removed from a BiblioCraft block in case of a rendering related crash.").getBoolean(false);
		enchPlateMaxUses = bConfig.get(Configuration.CATEGORY_GENERAL, "MaxEnchantedPlateUses", 3, "This will set the max number of uses an Enchanted Plate has before breaking. Default is 3.").getInt();
		enchantmentMultiplyer = bConfig.get(Configuration.CATEGORY_GENERAL, "EnchantmentCostMultiplyer", 10, "This will multiply the cost of copying enchanted books on the typesetting table. Please enter a positive integer value. Default is 10. Setting this to 1 would make the enchatment cost 1/10 the level default cost.").getInt();
		chairRedstone = bConfig.get(Configuration.CATEGORY_GENERAL, "ChairRedstone", true, "Setting this to false will deactivate the redstone signal output from seats when a player is sitting").getBoolean(true);
		mapUpdateRate = bConfig.get(Configuration.CATEGORY_GENERAL, "MapUpdateRate", 10, "Default is 1 update per 10 ticks, just like Item Frames. The number indicates how many ticks before an update packet is sent to clients. Lower numbers means more, faster updates.").getInt();
		checkforupdate = bConfig.get(Configuration.CATEGORY_GENERAL, "CheckForUpdates", true, "Setting this to false will permanently disable update checking").getBoolean(true);
		
		//useTextureSheet = bConfig.get(Configuration.CATEGORY_GENERAL, "UseTextureSheets", false, "If this is set to true, various models will use their own dedicated texture sheet, otherwise, models will use textures from vanilla planks. Supports Bookcase, Potion Shelf, Generic Shelf, Tool Rack, Display Case, and Label.").getBoolean(false);
		defaultBigBookTextScale = bConfig.getInt(Configuration.CATEGORY_GENERAL, "DefaultBigBookTextScale", 0, 0, 7, "This value will set the default text scale of text in the Big Book. Choose a positive integer between 0 and 7. 0 is the smallest scale and 7 is the largest scale.");
		if (checkforupdate)
		{
			bConfig.addCustomCategoryComment("Stored Variables", "These are the variables used by BiblioCraft to track if the player has already recieved an update message for a new version. These should not need to be edited.");
			lastCheckedversion = bConfig.get("Stored Variables", "lastVersionChecked", VersionCheck.currentversion).getString();
		}
		
		renderDistancePainting = bConfig.get(Configuration.CATEGORY_GENERAL, "PaintingRenderDistance", 64.0, "This will adjust the maximium render distance at which paintings can be seen. The default is 64.0 blocks.").getDouble();
		
		bConfig.save();
		
		
	}
	
	public static boolean testDiscValidity(String itemName)
	{
		boolean haveMatch = false;
		String testName = itemName.toLowerCase();
		for (int x=0; x < discs.length; x++)
		{
			if (testName.contains(discs[x].trim().toLowerCase()) || testName.contains("filmreel") || testName.contains("clapper") || testName.contains("dinnerplate"))
			{
				if (testName.contains("rack"))
				{
					break;
				}
				haveMatch = true;
				break;
			}
		}
		return haveMatch;
	}
	
	public static boolean testBookValidity(ItemStack stack)
	{
		// Current fix. Player can name any ItemStack `book` and have it accepted.
		ItemStack clone = stack.copy();
		String nameBefore = clone.getDisplayName();
		clone.clearCustomName();
		if (!nameBefore.equals(clone.getDisplayName())) {
			return false;
		}
		boolean haveMatch = false;
		if (stack != ItemStack.EMPTY)
		{
			//System.out.println(itemName); // turn this off before release
			String testName = stack.getUnlocalizedName().toLowerCase();
			String displayName = stack.getDisplayName().trim().toLowerCase();
			
			//System.out.println(testName);
			for (int x=0; x < books.length; x++)
			{
				if (testName.contains(books[x].trim().toLowerCase()) || displayName.contains(books[x].trim().toLowerCase()) || testName.contains("clipboard"))
				{
					if (testName.contains("shelf") || testName.contains("case") || testName.contains("stand") || testName.contains("planks") || testName.contains("tile.wood"))
					{
						break;
					} 
					else 
					{
						haveMatch = true;
						break;
					}
				}
			}
			
			if (testName.contains("ub@") || testName.contains("vv@") || testName.contains("vw@") || testName.contains("manual") || 
				testName.contains("catalogue") || testName.contains("railcraft.routing.table") || testName.contains("compendium") || 
				testName.contains("guide") || testName.contains("atlas") || testName.contains("item.itemeldritchobject.1") || testName.contains("diary") ||
				testName.contains("rites") || testName.contains("stockroomcatalog") || testName.contains("herbarium") || testName.contains("portfolio") || testName.contains("folder") || 
				testName.contains("on_the_dynamics_of_integration")) 
			{
				haveMatch = true; 
			}
		}
		return haveMatch;
	}
	
	public static boolean testToolValidity(String toolName, String toolcodeName)
	{
		toolcodeName = toolcodeName.toLowerCase();
		boolean haveMatch = false;
		String testName = toolName.trim().toLowerCase();
		
		for (int x=0; x < tools.length; x++)
		{
			if (testName.contains(tools[x].trim().toLowerCase()))
			{
				haveMatch = true;
				break;
			}
		}
		if ((toolcodeName.contains("@234d6d7d")))
		{
			haveMatch = false;
		}
		//System.out.println(toolcodeName);
		// this is where I can add a check for coded names and I will get TCon's stuff here. No problems. 
		if (toolcodeName.contains("tconstruct.items.tools") || toolcodeName.contains("item.tool") || toolcodeName.contains("hammer") || toolcodeName.contains("wrench") || 
			toolcodeName.contains("scythe") || toolcodeName.contains("staff") || toolcodeName.contains("chisel") ||  toolcodeName.contains("maptool") || toolcodeName.contains("handdrill") ||  
			toolcodeName.contains("dagger") ||  toolcodeName.contains("metagenerated_tool") ||  toolcodeName.contains("gt.metatool") || toolcodeName.contains("item.infitool") ||
			toolcodeName.contains("cleaver") || toolcodeName.contains("hatchet") || toolcodeName.contains("mattock") || toolcodeName.contains("excavator") || toolcodeName.contains("frypan") || toolcodeName.contains("battlesign") ||
			toolcodeName.contains("grapple") || toolcodeName.contains("chemthrower") || toolcodeName.contains("revolver") || toolcodeName.contains("rapier") || toolcodeName.contains("shortbow") || toolcodeName.contains("shuriken") ||
		    toolcodeName.contains("crossbow") || toolcodeName.contains("arrow") || toolcodeName.contains("tconstruct.bolt") || toolcodeName.contains("psi:cad") || toolcodeName.contains("vector_ruler") || toolcodeName.contains("saw"))
		{
			haveMatch = true;
		}
		
		return haveMatch;
	} 

	public static boolean testPotionValidity(String potName, String potDisplayName, Item potion)
	{
		boolean haveMatch = false;
			String testName = potName.trim().toLowerCase();
			String displayName = potDisplayName.trim().toLowerCase();
			for (int x=0; x < potions.length; x++)
			{
				if (testName.contains(potions[x].trim().toLowerCase()) || displayName.contains(potions[x].trim().toLowerCase()) || potion instanceof ItemPotion || testName.contains("potion") || testName.contains("jarfilled") || testName.contains("fillingagent") || 
					testName.contains("flask")  || displayName.contains("drink") || testName.contains("elixir")  || displayName.contains("elixir") || testName.contains("jellyitem") ||
					testName.contains("jellyitem") || testName.contains("milkshakeitem") || testName.contains("nutellaitem") || testName.contains("vegemiteitem") || testName.contains("sauceitem") ||
					testName.contains("juiceitem") || testName.contains("smoothieitem") || testName.contains("wateritem") || testName.contains("sodaitem") || testName.contains("brewitem") ||
					testName.contains("butteritem") || testName.contains("milkitem") || testName.contains("dressingitem") || testName.contains("coffee") || testName.contains("espressoitem") || 
					testName.contains("milkitem") || testName.contains("oilitem") || testName.contains("ketchup") || testName.contains("syrupitem") || testName.contains("chutneyitem") || 
					testName.contains("milkitem") || testName.contains("teaitem") || testName.contains("vinegaritem") || testName.contains("eggnog") || testName.contains("lemonaide") || 
					testName.contains("hotchocolate") || testName.contains("mustard") || testName.contains("yogurtitem") || testName.contains("creamitem"))
				{
					haveMatch = true;
					break;
				}
			}
			
			for (int x=0; x < witcheryPotions.length; x++)
			{
				if (potName.contains(witcheryPotions[x]))
				{
					haveMatch = true;
				}
			}
			
			if (testName.contains("shelf"))
			{
				haveMatch = false;
			}
		return haveMatch;
	}

	public static boolean isBlock(ItemStack stack)
	{
		if (stack != ItemStack.EMPTY)
		{
			String itemName = stack.getUnlocalizedName().toLowerCase();
			//System.out.println("Item Test Name: "+itemName);
			if (stack.getItem() instanceof ItemBlock || !(itemName.contains("item")) || !(stack.getItem() instanceof Item) || Block.getBlockFromItem(stack.getItem()) != Block.getBlockFromItem(ItemStack.EMPTY.getItem()))
			{
				return true;
			}
		}
		return false;
		
	}
	
	public static class ConfigUpdateListener
	{
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
		{
			if (eventArgs.getModID().equals(BiblioCraft.MODID))
			{
				loadConfig();
			}
		}
	}
}
