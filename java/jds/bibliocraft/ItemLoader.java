package jds.bibliocraft;

import java.util.Map; 

import jds.bibliocraft.enchantments.EnchantmentReading;
import jds.bibliocraft.helpers.RecipeBiblioAtlas;
import jds.bibliocraft.items.ItemFramingBoard;
import jds.bibliocraft.items.ItemFramingSaw;
import jds.bibliocraft.items.ItemFramingSheet;
/*
import jds.bibliocraft.blocks.BlockLabel;
import jds.bibliocraft.enchantments.EnchantmentDeathCompass;

import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemAtlasPlate;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemChase;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.items.ItemDeathCompass;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemEnchantedPlate;

import jds.bibliocraft.items.ItemHandDrill;
import jds.bibliocraft.items.ItemLock;
import jds.bibliocraft.items.ItemMapTool;
import jds.bibliocraft.items.ItemNameTester;
import jds.bibliocraft.items.ItemPaintingCanvas;
import jds.bibliocraft.items.ItemPlate;
import jds.bibliocraft.items.ItemPlumbLine;
import jds.bibliocraft.items.ItemReadingGlasses;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemRedstoneBook;
import jds.bibliocraft.items.ItemSeatBack;
import jds.bibliocraft.items.ItemSeatBack2;
import jds.bibliocraft.items.ItemSeatBack3;
import jds.bibliocraft.items.ItemSeatBack4;
import jds.bibliocraft.items.ItemSeatBack5;
import jds.bibliocraft.items.ItemSlottedBook;
import jds.bibliocraft.items.ItemStockroomCatalog;
import jds.bibliocraft.items.ItemTape;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.items.ItemTapeMeasure;
*/
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ItemLoader 
{
	//public static Enchantment readingChant;
	//public static Enchantment deathCompChant;
	
	
	
	
	public static void initItems(Register<Item> event)
	{
		/*
		// OBJ models
		if (Config.enableWaypointCompass)
		{
			//GameRegistry.registerItem(ItemWaypointCompass.instance, ItemWaypointCompass.name);
			event.getRegistry().register(ItemWaypointCompass.instance);
		}
		if (Config.enableMapFrame)
		{
			event.getRegistry().register(ItemMapTool.instance);
		}
		if (Config.enableSeat)
		{
			event.getRegistry().register(ItemSeatBack.instance);
			event.getRegistry().register(ItemSeatBack2.instance);
			event.getRegistry().register(ItemSeatBack3.instance);
			event.getRegistry().register(ItemSeatBack4.instance);
			event.getRegistry().register(ItemSeatBack5.instance);
		}
		
		// Vanilla models
		if (Config.enableStockroomCatalog)
		{
			event.getRegistry().register(ItemStockroomCatalog.instance);
		}
		
		if (Config.enablePlumbLine)
		{
			event.getRegistry().register(ItemPlumbLine.instance);
		}
		*/
		if (Config.enableFurniturePaneler)
		{
			event.getRegistry().register(ItemFramingSaw.instance.asItem());
			event.getRegistry().register(ItemFramingBoard.instance.asItem());
			event.getRegistry().register(ItemFramingSheet.instance.asItem());
		}
		/*
		if (Config.enableTesterItem)
		{
			event.getRegistry().register(ItemNameTester.instance);
		}
		
		if (Config.enableAtlas)
		{
			event.getRegistry().register(ItemAtlas.instance);
			event.getRegistry().register(ItemAtlasPlate.instance);
		}
		
		if (Config.enableAtlas && Config.enableDeathCompass)
		{
			event.getRegistry().register(ItemDeathCompass.instance);
			//deathCompChant = new EnchantmentDeathCompass();
		}
		
		if (Config.enablePainting)
		{
			event.getRegistry().register(ItemPaintingCanvas.instance);
		}
		if (Config.enableBigBook)
		{
			event.getRegistry().register(ItemBigBook.instance);
		}
		
		if (Config.enableFancyWorkbench)
		{
			event.getRegistry().register(ItemRecipeBook.instance);
		}
		
		if (Config.enableSlottedBook)
		{
			event.getRegistry().register(ItemSlottedBook.instance);
		}
		
		if (Config.enableHandDrill)
		{
			event.getRegistry().register(ItemHandDrill.instance);
		}

		if (Config.enableTapemeasure)
		{
			event.getRegistry().register(ItemTapeMeasure.instance);
			event.getRegistry().register(ItemTape.instance);
		}
		if (Config.enablePrintpressTypeMachine)
		{
			event.getRegistry().register(ItemChase.instance);
			event.getRegistry().register(ItemPlate.instance);
			event.getRegistry().register(ItemEnchantedPlate.instance);
		}
		if (Config.enableRedstonebook)
		{
			event.getRegistry().register(ItemRedstoneBook.instance);
		}
		if (Config.enableReadingglasses)
		{
			event.getRegistry().register(ItemReadingGlasses.instanceGlasses);
			event.getRegistry().register(ItemReadingGlasses.instanceTinted);
			event.getRegistry().register(ItemReadingGlasses.instanceMoncle);
		}
		if (Config.enableDrill)
		{
			event.getRegistry().register(ItemDrill.instance);
		}
		if (Config.enableLock)
		{
			event.getRegistry().register(ItemLock.instance);
		}
		if (Config.enableClipboard)
		{
			event.getRegistry().register(ItemClipboard.instance);
		}
		//readingChant = new EnchantmentReading();
		//Enchantment.addToBookList(readingChant); 
		
		*/
	}
/* TODO this is borked and needs work
	public static void addRecipies(RegistryEvent.Register<IRecipe> event)
	{
		
		ItemStack enchantedbook = new ItemStack(Items.ENCHANTED_BOOK, 1);
		ItemStack enchantedreadingbook = new ItemStack(Items.ENCHANTED_BOOK, 1);
		if (Config.enableAtlas)
		{
			if (Config.enableDeathCompass && Config.enableWaypointCompass)
			{			
				ResourceLocation regName = new ResourceLocation("bibliocraft:enchantedatlas");
				ResourceLocation regNameb = new ResourceLocation("bibliocraft:enchantedatlasalt");
				ItemStack waypointCompass = new ItemStack(ItemWaypointCompass.instance, 1);
				ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL, 1);
				CraftingHelper.register("bibliocraft:enchantedatlas", RecipeBiblioAtlas.class, RecipeSorter.Category.SHAPED, "");
				IRecipe recipea = RecipeBiblioAtlas.addAtlasEnchantRecipe(new ItemStack(ItemAtlas.instance, 1), "PBP", "CAC", "PBP", 'P', enderPearl, 'B', enchantedbook, 'C', waypointCompass, 'A', new ItemStack(ItemAtlas.instance));
				//recipea.setRegistryName(regName); // TODO setRegistryName is gone
				
				event.getRegistry().register(recipea);
				IRecipe recipeb =  RecipeBiblioAtlas.addAtlasEnchantRecipe(new ItemStack(ItemAtlas.instance, 1), "PCP", "BAB", "PCP", 'P', enderPearl, 'B', enchantedbook, 'C', waypointCompass, 'A', new ItemStack(ItemAtlas.instance));
				//recipeb.setRegistryName(regNameb);
				event.getRegistry().register(recipeb);
			}
		}
		
	}.*/
}
