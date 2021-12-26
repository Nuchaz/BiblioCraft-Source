package jds.bibliocraft.items;


import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import net.minecraft.item.Item;

public class ItemFramingSheet extends Item
{
	public static final String name = "framingsheet";
	public static final ItemFramingSheet instance = new ItemFramingSheet();
	
	public ItemFramingSheet()
	{
		super(new Item.Properties().group(BiblioCraft.BiblioTab));
		//setCreativeTab(BlockLoader.biblioTab);
		//setUnlocalizedName(name);
		//setMaxStackSize(64);
		setRegistryName(BiblioCraft.MODID, name);
	}
}
