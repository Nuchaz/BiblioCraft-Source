package jds.bibliocraft.helpers;

public enum EnumMetalType 
{
	GOLD(0),
	IRON(1);
	
	private int ID;
	
	private EnumMetalType(int id)
	{
		this.ID = id;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public static EnumMetalType getEnumFromID(int id)
	{
		EnumMetalType type = GOLD;
		switch (id)
		{
		case 0: { type = GOLD; break; }
		case 1: { type = IRON; break; }
		}
		return type;
	}
}
