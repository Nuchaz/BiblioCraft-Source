package jds.bibliocraft.helpers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import jds.bibliocraft.BiblioCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

public class PaintingUtil
{
	// so I guess I need to get a directory and then scan it for files
	public static int[] test;
	public static String[] customArtNames = null;
	public static int[] customArtHeights = null;
	public static int[] customArtWidths = null;
	public static ResourceLocation[] customArtResources = null;
	public static String[] customArtResourceStrings = null;
	
	/*
	public static void testingStuff() //throws URISyntaxException //throws IOException, IOException
	{
		String[] customPaintings = getListOfPaintings();
		if (customPaintings != null)
		{
			ResourceLocation[] resources = getPaintingsResourceLocations(customPaintings);
			int[] heights = getPaintingSetHeights(customPaintings, resources);
			int[] widths = getPaintingSetWidths(customPaintings, resources);
			for (int i = 0; i < customPaintings.length; i++)
			{
				System.out.println(customPaintings[i]);
				System.out.println(heights[i]+"x");
				System.out.println(widths[i]+"x");
			}
		}
	}
	*/
	public static void updateCustomArtDatas()
	{
		PaintingUtil.customArtNames = getListOfPaintings();
		if (PaintingUtil.customArtNames != null)
		{
			PaintingUtil.customArtResources = getPaintingsResourceLocations(customArtNames);
			PaintingUtil.customArtResourceStrings = getPaintingsStringLocations(customArtNames);
			PaintingUtil.customArtHeights = getPaintingSetHeights(customArtNames, customArtResources);
			PaintingUtil.customArtWidths = getPaintingSetWidths(customArtNames, customArtResources);
		}
	}
	
	private static ResourceLocation[] getPaintingsResourceLocations(String[] names)
	{
		ResourceLocation[] paintings = new ResourceLocation[names.length];
		for (int i = 0; i < names.length; i++)
		{
			paintings[i] = new ResourceLocation("bibliocraft","textures/custompaintings/"+names[i]);
		}
		return paintings;
	}
	
	private static String[] getPaintingsStringLocations(String[] names)
	{
		String[] paintings = new String[names.length];
		for (int i = 0; i < names.length; i++)
		{
			paintings[i] = "bibliocraft:custompaintings/"+names[i];
			paintings[i] = paintings[i].replace(".png", "");
		}
		return paintings;
	}
	
	private static int[] getPaintingSetHeights(String[] paintings, ResourceLocation[] resources)
	{
		int[] heights = new int[paintings.length];
		IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
		try
		{
			for (int i = 0; i < paintings.length; i++)
			{
				IResource theThing = rm.getResource(resources[i]);
				if (theThing != null)
				{
					Image img = ImageIO.read(theThing.getInputStream());
					if (img != null)
					{
						heights[i] = img.getHeight(null);
					}
				}
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return heights;
	}
	
	private static int[] getPaintingSetWidths(String[] paintings, ResourceLocation[] resources)
	{
		int[] widths = new int[paintings.length];
		IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
		try
		{
			for (int i = 0; i < paintings.length; i++)
			{
				IResource theThing = rm.getResource(resources[i]);
				if (theThing != null)
				{
					Image img = ImageIO.read(theThing.getInputStream());
					if (img != null)
					{
						widths[i] = img.getWidth(null);
					}
				}
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return widths;
	}
	
	private static String[] getListOfPaintings()
	{
		String[] resources = getJarPaintings(); 
		String[] resourcePacks = getResourcePackPaintings();
		
		if (resources != null && resources.length > 0 && resourcePacks != null && resourcePacks.length > 0)
		{
			String[] masterList = new String[resources.length+resourcePacks.length];
			int indexAdjuster = 0;
			for (int i = 0; i < masterList.length; i++)
			{
				if (i < resources.length)
				{
					masterList[i] = resources[i];
				}
				else
				{
					masterList[i] = resourcePacks[i-resources.length];
				}
			}
			return masterList;
		}
		else if ((resources == null || resources.length < 1) && resourcePacks != null && resourcePacks.length > 0)
		{
			return resourcePacks;
		}
		else if (resources != null && resources.length > 0 && (resourcePacks == null || resourcePacks.length < 1))
		{
			return resources;
		}
		return null;
	}
	
	@SuppressWarnings("resource")
	private static String[] getJarPaintings()
	{
		  String path = "assets/bibliocraft/textures/custompaintings/";
	      URL dirURL = BiblioCraft.instance.getClass().getResource("/assets/bibliocraft/textures/custompaintings/");
	      if (dirURL != null && dirURL.getProtocol().equals("file")) 
	      {
	    	  try
	    	  {
	    		  String[] newSet = new File(dirURL.toURI()).list();
	    		  int setSize = 0;
	    		  ArrayList<String> thePNGs = new ArrayList<String>();
	    		  for (int i = 0; i < newSet.length; i++)
	    		  {
	    			  if (newSet[i].contains(".png"))
	    			  {
	    				  thePNGs.add(newSet[i]);
	    			  }
	    		  }
	    		  String[] finalSet = new String[thePNGs.size()];
	    		  
	    		  for (int i = 0; i < thePNGs.size(); i++)
	    		  {
	    			  finalSet[i] = thePNGs.get(i);
	    		  }
	    		  return finalSet;
	    	  } 
	    	  catch (URISyntaxException e)
	    	  {
	    		  e.printStackTrace();
	    	  }
	      } 
	      if (dirURL != null && dirURL.getProtocol().equals("jar")) 
	      {
		        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); 
				JarFile jar;
				try
				{
					jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
					Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
				    Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
				    while(entries.hasMoreElements()) 
				    {
					    String name = entries.nextElement().getName();
			            if (name.startsWith(path)) // filter according to path
					    { 
			            	
			            	String entry = name.substring(path.length());
			            	//System.out.println(entry);
					        if (entry.contains(".png"))
					        {
					        	//System.out.println("what the?");
					        	result.add(entry);
					        }
			            }
			        }
			        return result.toArray(new String[result.size()]);

				} 
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
	      } 
	      
	      return null;
	}
	
	private static String[] getResourcePackPaintings()
	{
		ResourcePackRepository pack = Minecraft.getMinecraft().getResourcePackRepository();
		File[] packFileList = pack.getDirResourcepacks().listFiles();
		String[] currentlyUsedPacks = getCurrentResoucePackList(pack);
		if (packFileList != null && currentlyUsedPacks != null)
		{
			ArrayList<String> packPaintings = new ArrayList<String>();
			for (int i = 0; i < currentlyUsedPacks.length; i++)
			{
				for (int j = 0; j < packFileList.length; j++)
				{
					if (packFileList[j].getAbsolutePath().contains(currentlyUsedPacks[i]))
					{
						String[] paintingsInThisPack = getListFromResourcePack(packFileList[j]);
						if (paintingsInThisPack != null && paintingsInThisPack.length > 0)
						{
							for (int k = 0; k < paintingsInThisPack.length; k++)
							{
								packPaintings.add(paintingsInThisPack[k]);
							}
						}
						break;
					}
				}
			}
			if (!packPaintings.isEmpty() && packPaintings.size() > 0)
			{
				String[] fullPackPaintingList = new String[packPaintings.size()];
				for (int i = 0; i < packPaintings.size(); i++)
				{
					fullPackPaintingList[i] = packPaintings.get(i);
				}
				return fullPackPaintingList;
			}
		}
		return null;
	}
	
	private static String[] getCurrentResoucePackList(ResourcePackRepository pack)
	{
		List lst = pack.getRepositoryEntries();
		if (lst.size() > 0)
		{
			String[] currentPacks = new String[lst.size()];
			for (int i = 0; i < lst.size(); i++)
			{
				ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry)lst.get(i);
				IResourcePack packet = entry.getResourcePack();
				currentPacks[i] = packet.getPackName();
				//System.out.println(packet.getPackName());
			}
			return currentPacks;
		}
		return null;
	}
	
	private static String[] getListFromResourcePack(File resourceZipFile)
	{
		// open the zip file and get the list of paintings, all I need is the "painting.png" bit and Ill be good
		try
		{
			String ext = FilenameUtils.getExtension(resourceZipFile.getName());
			if (ext.equals("zip"))
			{ 
				// deal with zip file
				String path = "assets/bibliocraft/textures/custompaintings/";
				ZipFile zippy = new ZipFile(resourceZipFile);
				Enumeration packEntries = zippy.entries();
				String fileName = "";
				Set<String> result = new HashSet<String>();
				while (packEntries.hasMoreElements())
				{
					fileName = ((ZipEntry)packEntries.nextElement()).getName();
					if (fileName.startsWith(path)) // filter according to path
					{ 
						String entry = fileName.substring(path.length());
					    if (entry.contains(".png"))
					    {
					        result.add(entry);
					    }
			         }
				}
				zippy.close();
				if (!result.isEmpty() && result.size() > 0)
				{
					return result.toArray(new String[result.size()]);
				}
			}
			else if (ext.equals(""))
			{
				//  scan the folder
				File paintingsLoc = new File(resourceZipFile,"assets/bibliocraft/textures/custompaintings/");
				String[] finalSet = new String[0];
				if (paintingsLoc.exists())
				{
					String[] newSet = paintingsLoc.list();
					if (newSet.length > 0)
					{
						ArrayList<String> thePNGs = new ArrayList<String>();
						for (int i = 0; i < newSet.length; i++)
						{
							if (newSet[i].contains(".png"))
							{
								thePNGs.add(newSet[i]);
							}
						}
						finalSet = new String[thePNGs.size()];
						  
						for (int i = 0; i < thePNGs.size(); i++)
						{
							finalSet[i] = thePNGs.get(i);
						}
					}
				}
				return finalSet;
			      
			}
		} 
		catch (ZipException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
