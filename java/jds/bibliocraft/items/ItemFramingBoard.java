package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import net.minecraft.item.Item;

public class ItemFramingBoard extends Item
{
	public static final String name = "FramingBoard";
	public static final ItemFramingBoard instance = new ItemFramingBoard();
	
	public ItemFramingBoard()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(64);
		setRegistryName(name);
	}
}
