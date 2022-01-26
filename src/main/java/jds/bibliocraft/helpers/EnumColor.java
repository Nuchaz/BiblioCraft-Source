package jds.bibliocraft.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public enum EnumColor implements IStringSerializable
{
	WHITE(0, "white"),
	LIGHT_GRAY(1, "silver"),
	GRAY(2, "gray"),
	BLACK(3, "black"),
	RED(4, "red"),
	ORANGE(5, "orange"),
	YELLOW(6, "yellow"),
	LIME(7, "lime"),
	GREEN(8, "green"),
	CYAN(9, "cyan"),
	LIGHT_BLUE(10, "light_blue"),
	BLUE(11, "blue"),
	PURPLE(12, "purple"),
	MAGENTA(13, "magenta"),
	PINK(14, "pink"),
	BROWN(15, "brown");
	
	
	

	private int ID;
	private String name;
	private String texturePath;
	private static final EnumColor[] META_LOOKUP = new EnumColor[values().length];
	
	private EnumColor(int id, String name)
	{
		this.ID = id;
		this.name = name;
		this.texturePath = "minecraft:blocks/wool_colored_" + name;
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
	
	public String getWoolTextureString()
	{
		return this.texturePath;
	}
	
	public static EnumColor getColorFromCarpetOrWool(ItemStack stack)
	{
		EnumColor color = EnumColor.WHITE;
		if (stack != null)
		{
			switch (stack.getItemDamage())
			{
				case 0:{ color = EnumColor.WHITE; break; }
				case 1:{ color = EnumColor.ORANGE; break; }
				case 2:{ color = EnumColor.MAGENTA; break; }
				case 3:{ color = EnumColor.LIGHT_BLUE; break; }
				case 4:{ color = EnumColor.YELLOW; break; }
				case 5:{ color = EnumColor.LIME; break; }
				case 6:{ color = EnumColor.PINK; break; }
				case 7:{ color = EnumColor.GRAY; break; }
				case 8:{ color = EnumColor.LIGHT_GRAY; break; }
				case 9:{ color = EnumColor.CYAN; break; }
				case 10:{ color = EnumColor.PURPLE; break; }
				case 11:{ color = EnumColor.BLUE; break; }
				case 12:{ color = EnumColor.BROWN; break; }
				case 13:{ color = EnumColor.GREEN; break; }
				case 14:{ color = EnumColor.RED; break; }
				case 15:{ color = EnumColor.BLACK; break; }
			}
		}
		return color;
	}
	
	public static EnumColor getColorEnumFromID(int id)
	{
		EnumColor color = META_LOOKUP[id];
		return color;
	}
	
	static
	{
		for (EnumColor color : values())
		{
			META_LOOKUP[color.getID()] = color;
		}
	}

}
