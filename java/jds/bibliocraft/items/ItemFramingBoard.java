package jds.bibliocraft.items;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import net.minecraft.item.Item;

public class ItemFramingBoard extends Item
{
	public static final String name = "framingboard";
	public static final ItemFramingBoard instance = new ItemFramingBoard();
	
	public ItemFramingBoard()
	{
		super(new Item.Properties().group(BiblioCraft.BiblioTab));
		//setCreativeTab(BlockLoader.biblioTab);
		//setUnlocalizedName(name);
		//setMaxStackSize(64);
		setRegistryName(BiblioCraft.MODID, name);
	}
}
