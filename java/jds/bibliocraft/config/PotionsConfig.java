package jds.bibliocraft.config;

import net.minecraft.item.Item;
import net.minecraft.item.PotionItem;
import net.minecraftforge.common.ForgeConfigSpec;

public class PotionsConfig 
{

	public static String[] whitelist = {"potion", "jarfilled", "fillingagent", "flask", "drink", "elixir", "jellyitem", "milkshakeitem", "nutellaitem", "vegemiteitem", "sauceitem", 
			"juiceitem", "smoothieitem", "wateritem", "sodaitem", "brewitem", "butteritem", "milkitem", "dressingitem", "coffee", "espressoitem", "milkitem", "oilitem", "ketchup",
			"syrupitem", "chutneyitem", "teaitem", "vinegaritem", "eggnog", "lemonaide", "hotchocolate", "mustard", "yogurtitem", "creamitem"};
	public static String[] witcheryPotions = {"ingredient.brew", "ingredient.clayJar", "ingredient.foulFume", "ingredient.diamondVapour", 
			"ingredient.oilOfVitriol", "ingredient.exhaleOfTheHornedOne", "ingredient.breathOfTheGoddess", "ingredient.hintOfRebirth", 
			"ingredient.whiffOfMagic", "ingredient.reekOfMisfortune", "ingredient.odourOfPurity", "ingredient.tearOfTheGoddess", "ingredient.dropOfLuck", 
			"ingredient.redstoneSoup", "ingredient.flyingOintment", "ingredient.ghostOfTheLight", "ingredient.soulOfTheWorld", "ingredient.spiritOfOtherwhere", 
			"ingredient.infernalAnimus", "ingredient.enderDew", "ingredient.infernalblood", "ingredient.mysticunguent"};
	
	public static ForgeConfigSpec.ConfigValue<String> additionalPotions;
	public static String[] potions;
	
	
	public static boolean testPotionValidity(String potName, String potDisplayName, Item potion)
	{
		boolean haveMatch = false;
			String testName = potName.trim().toLowerCase();
			String displayName = potDisplayName.trim().toLowerCase();
			
			if (potion instanceof PotionItem)
			{
				haveMatch = true;
			}
			
			if (!haveMatch)
			for (int x=0; x < potions.length; x++)
			{
				if (testName.contains(potions[x].trim().toLowerCase()) || displayName.contains(potions[x].trim().toLowerCase()))
				{
					haveMatch = true;
					break;
				}
			}
			
			if (!haveMatch)
			for (int x=0; x < witcheryPotions.length; x++)
			{
				if (potName.contains(witcheryPotions[x]))
				{
					haveMatch = true;
					break;
				}
			}
			
			if (!haveMatch)
			for (int i = 0; i < whitelist.length; i++)
			{
				if (testName.contains(whitelist[i]))
				{
					haveMatch = true;
					break;
				}
			}
			
			if (testName.contains("shelf"))
			{
				haveMatch = false;
			}
		return haveMatch;
	}
}
