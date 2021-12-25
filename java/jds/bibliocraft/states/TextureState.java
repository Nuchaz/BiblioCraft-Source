package jds.bibliocraft.states;

import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.helpers.EnumColor;

public class TextureState
{
	protected String textureString = "none";
	private EnumColor colorOne = EnumColor.WHITE;
	private EnumColor colorTwo = EnumColor.WHITE;
	private EnumWoodType additionalWood = EnumWoodType.OAK;
	private String additionalTextureString = "none";
	private boolean flag = false;
	private boolean flag2 = false;
	
	public TextureState(String textureString)
	{
		this.textureString = textureString;
	}
	
	public String getTextureString()
	{
		return this.textureString;
	}
	
	public boolean getFlag()
	{
		return this.flag;
	}
	
	public void setFlag2(boolean flagged)
	{
		this.flag2 = flagged;
	}
	
	public boolean getFlag2()
	{
		return this.flag2;
	}
	
	public void setFlag(boolean flagged)
	{
		this.flag = flagged;
	}
	
	public void setAdditionalWood(EnumWoodType wood)
	{
		this.additionalWood = wood;
	}
	
	public void setAdditionalTextureString(String texture)
	{
		this.additionalTextureString = texture;
	}
	
	public EnumWoodType getAdditionalWood()
	{
		return this.additionalWood;
	}
	
	public String getAdditionalTexture()
	{
		return this.additionalTextureString;
	}
	
	public void setColorOne(EnumColor color)
	{
		this.colorOne = color;
	}
	
	public void setColorTwo(EnumColor color)
	{
		this.colorTwo = color;
	}
	
	public EnumColor getColorOne()
	{
		return this.colorOne;
	}

	public EnumColor getColorTwo()
	{
		return this.colorTwo;
	}
	
	@Override
	public String toString()
	{
		return this.textureString;
	}

}


