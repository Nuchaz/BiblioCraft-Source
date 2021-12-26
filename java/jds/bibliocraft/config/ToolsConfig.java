package jds.bibliocraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ToolsConfig 
{

	public static String[] whitelist = {"tconstruct.items.tools", "item.tool", "hammer", "wrench", "scythe", "staff", "chisel", "maptool", "handdrill", 
			"dagger", "metagenerated_tool", "gt.metatool", "item.infitool", "cleaver", "hatchet", "mattock", "excavator", "frypan", "battlesign", 
			"grapple", "chemthrower", "revolver", "rapier", "shortbow", "shuriken", "crossbow", "arrow", "tconstruct.bolt", "psi:cad", "vector_ruler", 
			"saw"};
	public static ForgeConfigSpec.ConfigValue<String> additionalTools;
	public static String[] tools;
	
	
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
