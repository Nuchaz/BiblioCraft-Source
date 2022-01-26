package jds.bibliocraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VersionCheck {
	public static String currentversion = BiblioCraft.VERSION;
	private static String lastCheckedversion = "";
	private static String latestWebVersion;
	private static final String siteURL = "https://bibliocraftmod.com/";
	private static final String versionURL = siteURL + "vcheck/version.txt";
	private static final String messageURL = siteURL + "vcheck/message.txt";
	private boolean runEvent = true;
	private EntityPlayer player;

	public VersionCheck() {
	}

	@SubscribeEvent
	public void onWorldLoad(EntityJoinWorldEvent event) {
		if (runEvent && Config.checkforupdate) {
			if (event.getEntity() instanceof EntityPlayer && event.getWorld().isRemote) {
				this.player = (EntityPlayer) event.getEntity();
				runEvent = false;
				new Thread(() -> {
					getNetVersion(player);
				}).start();
			}
		}
	}
	private static String httpReadLine(URL url) throws IOException {
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("Host", url.getHost());
		http.setRequestProperty("Alt-Used", url.getHost());
		http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36"); // Server refuses us without a user agent string, so we use a generic one.
		int rCode = http.getResponseCode();
		if (rCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					http.getInputStream()));
			String netVersionString = in.readLine();
			in.close();
			return netVersionString;
		} else {
			BiblioCraft.LOGGER.error("GET request to " + url.getHost() + " failed with code " + rCode);
		}
		return null;
	}

	public static void getNetVersion(EntityPlayer player) {
		try {
			String netVersionString = httpReadLine(new URL(versionURL));
			if (netVersionString != null) {
				latestWebVersion = netVersionString;
				if (latestWebVersion.length() > 8 || latestWebVersion.length() < 3) {
					return;
				}
				if (!(latestWebVersion.substring(1).startsWith("."))) {
					return;
				}

				lastCheckedversion = Config.bConfig
						.get("Stored Variables", "lastVersionChecked", VersionCheck.currentversion).getString();
				if (!latestWebVersion.contains(lastCheckedversion) && !latestWebVersion.contains(currentversion)) {
					Config.bConfig.load();
					Config.bConfig.get("Stored Variables", "lastVersionChecked", VersionCheck.currentversion)
							.set(netVersionString); // set the last checked version in config to the web version
					setUpdateMessage(player);
					Config.bConfig.save();
				}
			} else {
				BiblioCraft.LOGGER.error("Version check responded with no version");
			}
		} catch (

		MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public static void getNetVersion(EntityPlayer player) {
	// try {
	// URL netVersion = new URL(versionURL);
	// InputStream stream = netVersion.openStream();

	// byte[] data = new byte[stream.available()];
	// stream.read(data);
	// String netVersionString = new String(data);
	// latestWebVersion = netVersionString;
	// if (latestWebVersion.length() > 8 || latestWebVersion.length() < 3) {
	// return;
	// }
	// if (!(latestWebVersion.substring(1).startsWith("."))) {
	// return;
	// }

	// lastCheckedversion = Config.bConfig
	// .get("Stored Variables", "lastVersionChecked",
	// VersionCheck.currentversion).getString();
	// if (!latestWebVersion.contains(lastCheckedversion) &&
	// !latestWebVersion.contains(currentversion)) {
	// Config.bConfig.load();
	// Config.bConfig.get("Stored Variables", "lastVersionChecked",
	// VersionCheck.currentversion)
	// .set(netVersionString); // set the last checked version in config to the web
	// version
	// setUpdateMessage(player);
	// Config.bConfig.save();
	// }
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }

	// }

	public static void setUpdateMessage(EntityPlayer player) {
		try {
			String data = httpReadLine(new URL(messageURL));
			// URL updateMsg = new URL(messageURL);
			// InputStream stream = updateMsg.openStream();
			// byte[] data = new byte[stream.available()];
			// stream.read(data);
			player.sendMessage(new TextComponentString(data));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
