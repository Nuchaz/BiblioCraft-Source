package jds.bibliocraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig 
{

	// would be great if I could figure out how to actually do this. Do I need to?
	public static ForgeConfigSpec.BooleanValue forceFastRenderShelf;
	public static ForgeConfigSpec.BooleanValue forceFastRenderPotionShelf;
	public static ForgeConfigSpec.BooleanValue forceFastRenderFancySign;
	public static ForgeConfigSpec.BooleanValue forceFastRenderLabel;
	public static ForgeConfigSpec.BooleanValue forceFastRenderTabel;
	public static ForgeConfigSpec.BooleanValue forceFastRenderCase;
	public static ForgeConfigSpec.BooleanValue forceFastRenderToolrack;
	public static ForgeConfigSpec.BooleanValue forceFastRenderDinnerPlate;
	public static ForgeConfigSpec.BooleanValue forceFastRenderDiscRack;
	
	
	
	public static ForgeConfigSpec.BooleanValue emitLight;
	public static ForgeConfigSpec.BooleanValue disablerenderers;
	
	public static ForgeConfigSpec.DoubleValue renderDistancePainting;
	
	public static ForgeConfigSpec.IntValue mapUpdateRate; // this might not be relevent anymore. is this client or server thing? dont even know
	
	
	public ClientConfig()
	{
		
	}
	
	public static ForgeConfigSpec makeClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		// enable / disable blocks from here, so it can be done on a per-world basis
		//doFade = builder.define("testy test", true);
		return builder.build();
	}
	
}
