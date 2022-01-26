package jds.bibliocraft.helpers;

public enum EnumRelativeLocation 
{
	SINGLE(0),
	LEFT(1),
	RIGHT(2),
	UP(3),
	DOWN(4),
	CENTER(5);
	
	private int ID;
	
	private EnumRelativeLocation(int id)
	{
		this.ID = id;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public static EnumRelativeLocation getEnumFromID(int id)
	{
		EnumRelativeLocation loc = SINGLE;
		switch(id)
		{
			case 0: { loc = SINGLE; break; }
			case 1: { loc = LEFT; break; }
			case 2: { loc = RIGHT; break; }
			case 3: { loc = UP; break; }
			case 4: { loc = DOWN; break; }
			case 5: { loc = CENTER; break; }
		}
		return loc;
	}
}
