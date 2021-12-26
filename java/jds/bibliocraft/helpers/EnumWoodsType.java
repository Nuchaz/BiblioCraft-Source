package jds.bibliocraft.helpers;


public enum EnumWoodsType 
{
	oak(0, "oak", "minecraft:blocks/planks_oak"),
	spruce(1, "spruce", "minecraft:blocks/planks_spruce"),
	birch(2, "birch", "minecraft:blocks/planks_birch"),
	jungle(3, "jungle", "minecraft:blocks/planks_jungle"),
	acacia(4, "acacia", "minecraft:blocks/planks_acacia"),
	darkoak(5, "darkoak", "minecraft:blocks/planks_big_oak"),
	framed(6, "framed", "bibliocraft:blocks/frame");
	
	
	private int ID;
	private String name;
	private String textureString;
	private static final EnumWoodsType[] META_LOOKUP = new EnumWoodsType[values().length];
	
	private EnumWoodsType(int ID, String name, String texture)
	{
		this.ID = ID;
		this.name = name;
		this.textureString = texture;
	}

	public String getName() 
	{
		return name;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String getTextureString()
	{
		return this.textureString;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	public static EnumWoodsType getEnum(int meta)
	{
		EnumWoodsType thing = oak;
		switch (meta)
		{
			case 0:{thing = oak; break;}
			case 1:{thing = spruce; break;}
			case 2:{thing = birch; break;}
			case 3:{thing = jungle; break;}
			case 4:{thing = acacia; break;}
			case 5:{thing = darkoak; break;}
			case 6:{thing = framed; break;}
		}
		return thing;
	}
}
