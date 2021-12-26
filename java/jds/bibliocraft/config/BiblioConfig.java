package jds.bibliocraft.config;

import jds.bibliocraft.BiblioCraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;

public class BiblioConfig 
{

	// common stuff here
	
	public static ServerConfig server = new ServerConfig();
	public static ClientConfig client = new ClientConfig();
	
	public static ForgeConfigSpec.ConfigValue<String> lastCheckedversion;
	public static ForgeConfigSpec.BooleanValue checkforupdate; 
	
	public static ForgeConfigSpec makeCommonConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		//doFade = builder.define("testy test", true);
		return builder.build();
	}
	
	public static ForgeConfigSpec makeServerConfig()
	{
		return ServerConfig.makeServerConfig();
	}
	
	public static ForgeConfigSpec makeClientConfig()
	{
		return ClientConfig.makeClientConfig();
	}
	
	
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class ConfigUpdateListener
	{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
		{
			if (eventArgs.getModID().equals(BiblioCraft.MODID))
			{
				System.out.println("biblio test config");
				//loadConfig();
			}
		}
	}
}
