package jds.bibliocraft.helpers;

import net.minecraft.util.IStringSerializable;

public enum EnumCustomDataType implements IStringSerializable
{
	NONE(0, "None"),
	FANCY_SIGN(1, "FancySign");
	
	private int ID;
	private String name;
	private static final EnumCustomDataType[] META_LOOKUP = new EnumCustomDataType[values().length];

	private EnumCustomDataType(int id, String name)
	{
		this.ID = id;
		this.name = name;
	}
	
	@Override
	public String getName() 
	{
		return this.name;
	}
	
	public int getID()
	{
		return this.ID;
	}

	public static EnumCustomDataType getTypeFromID(int id)
	{
		EnumCustomDataType datatype = META_LOOKUP[id];
		return datatype;
	}
	
	static
	{
		for (EnumCustomDataType dt : values())
		{
			META_LOOKUP[dt.getID()] = dt;
		}
	}
}
