package jds.bibliocraft.items;


import jds.bibliocraft.BlockLoader;
import net.minecraft.item.Item;

public class ItemFramingSheet extends Item
{
	public static final String name = "FramingSheet";
	public static final ItemFramingSheet instance = new ItemFramingSheet();
	
	public ItemFramingSheet()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(64);
		setRegistryName(name);
	}
}
