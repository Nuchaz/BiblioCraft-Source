package jds.bibliocraft.helpers;

public enum EnumPaintingFrame 
{
	BORDERLESS(0),
	FLAT(1),
	SIMPLE(2),
	MIDDLE(3),
	FANCY(4);
	
	private int ID;
	
	private EnumPaintingFrame(int id)
	{
		this.ID = id;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public static EnumPaintingFrame getEnumFromID(int id)
	{
		EnumPaintingFrame frame = BORDERLESS;
		switch (id)
		{
			case 0: { frame = BORDERLESS; break; }
			case 1: { frame = FLAT; break; }
			case 2: { frame = SIMPLE; break; }
			case 3: { frame = MIDDLE; break; }
			case 4: { frame = FANCY; break; }
		}
		return frame;
	}
}
