package jds.bibliocraft.helpers;

import jds.bibliocraft.blocks.BlockMapFrame;

public class WoodRegistryEntry 
{

	private String slab;
	private String plank;
	private String mapFrame; 
	private String texture;
	private boolean exists;
	
	public WoodRegistryEntry(String slabString, String plankString, String textureString, boolean isReal)
	{
		this.slab = slabString;
		this.plank = plankString;
		this.mapFrame = BlockMapFrame.name;
		this.texture = textureString;
		this.exists = isReal;
	}
	
	public String getSlabString()
	{
		return this.slab;
	}
	
	public String getPlankString()
	{
		return this.plank;
	}
	
	public String getMapFrameString()
	{
		return this.mapFrame;
	}
	
	public String getTextureString()
	{
		return this.texture;
	}
	
	public boolean getIfReal()
	{
		return this.exists;
	}
	
	public boolean hasMatch(String input)
	{
		boolean result = false;
		if (input.contains(slab) || input.contains(plank) || input.contains(mapFrame))
			result = true;
		return result;
	}
}
