package jds.bibliocraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class VersionCheck 
{
	public static String currentversion = BiblioCraft.VERSION;
	private static String lastCheckedversion = "";
	private static String latestWebVersion;
	
	private static String versionURL = "https://www.bibliocraftmod.com/vcheck/version.txt";
	private static String messageURL = "https://www.bibliocraftmod.com/vcheck/message.txt";
	private boolean runEvent = true;
	private EntityPlayer player;

	public VersionCheck(){}
		

	@SubscribeEvent
	public void onWorldLoad(EntityJoinWorldEvent event)
	{
		if (runEvent && Config.checkforupdate)
		{
			if (event.getEntity() instanceof EntityPlayer && event.getWorld().isRemote)
			{
				this.player = (EntityPlayer)event.getEntity();
				runEvent = false;
				new Thread(new Runnable() 
				{
					@Override
					public void run() {
						getNetVersion(player);
					}
				}).start();
			}
		}
	}
	
	public static void getNetVersion(EntityPlayer player)
	{
		try 
		{
			URL netVersion = new URL(versionURL);
			InputStream stream = netVersion.openStream();
			
			byte[] data = new byte[stream.available()];
			stream.read(data);
			String netVersionString = new String(data);
			latestWebVersion = netVersionString;
			if (latestWebVersion.length() > 8 || latestWebVersion.length() < 3)
			{
				return;
			}
			if (!(latestWebVersion.substring(1).startsWith(".")))
			{
				return;
			}

			lastCheckedversion = Config.bConfig.get("Stored Variables", "lastVersionChecked", VersionCheck.currentversion).getString();
			if (!latestWebVersion.contains(lastCheckedversion) && !latestWebVersion.contains(currentversion))
			{
				Config.bConfig.load();
				Config.bConfig.get("Stored Variables", "lastVersionChecked", VersionCheck.currentversion).set(netVersionString);  // set the last checked version in config to the web version
				setUpdateMessage(player);
				Config.bConfig.save();
			}
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public static void setUpdateMessage(EntityPlayer player)
	{
		try 
		{
			URL updateMsg = new URL(messageURL);
			InputStream stream = updateMsg.openStream();
			byte[] data = new byte[stream.available()];
			stream.read(data);
			player.sendMessage(new TextComponentString(new String(data)));
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
