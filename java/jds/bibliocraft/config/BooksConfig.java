package jds.bibliocraft.config;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

public class BooksConfig 
{
	
	public BiblioConfig config;

	public static String[] whitelist = {"clipboard", "manual", "catalogue", "railcraft.routing.table", "compendium", "guide", "atlas", "item.itemeldritchobject.1", "diary", 
			"rites", "stockroomcatalog", "herbarium", "portfolio", "folder", "on_the_dynamics_of_integration"};
	public static String[] blacklist = {"shelf", "case", "stand", "planks", "tile.wood"};
	public static ForgeConfigSpec.ConfigValue<String> allowedBooks;
	public static String[] books;
	
	public static boolean testBookValidity(ItemStack stack)
	{
		boolean haveMatch = false;
		if (stack != ItemStack.EMPTY)
		{
			//System.out.println(itemName); // turn this off before release
			String testName = stack.getDisplayName().getUnformattedComponentText().toLowerCase();
			String displayName = stack.getDisplayName().getFormattedText().trim().toLowerCase();
			
			//System.out.println(testName);
			for (int x=0; x < books.length; x++)
			{
				if (testName.contains(books[x].trim().toLowerCase()) || displayName.contains(books[x].trim().toLowerCase()))
				{
					haveMatch = true;
					for (int j = 0; j < blacklist.length; j++)
					{
						if (testName.contains(blacklist[j]))
						{
							haveMatch = false;
							break;
						}
					}
					break;
				}
			}
			
			if (!haveMatch)
			for (int i = 0; i < whitelist.length; i++)
			{
				if (testName.contains(whitelist[i]))
				{
					haveMatch = true;
					for (int j = 0; j < blacklist.length; j++)
					{
						if (testName.contains(blacklist[j]))
						{
							haveMatch = false;
							break;
						}
					}
					break;
				}
			}
	
		}
		return haveMatch;
	}
}
