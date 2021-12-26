package jds.bibliocraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DiscsConfig 
{

	public static String[] whitelist = {"filmreel", "clapper", "dinnerplate"};
	public static String[] blacklist = {"rack"};
	public static ForgeConfigSpec.ConfigValue<String> additionalDiscs;
	public static String[] discs;
	
	public static boolean testDiscValidity(String itemName)
	{
		boolean haveMatch = false;
		String testName = itemName.toLowerCase();
		for (int x=0; x < discs.length; x++)
		{
			if (testName.contains(discs[x].trim().toLowerCase()))
			{
				if (testName.contains(blacklist[0])) // should I just have a blacklist maybe?
				{
					break;
				}
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
		
		return haveMatch;
	}
}
