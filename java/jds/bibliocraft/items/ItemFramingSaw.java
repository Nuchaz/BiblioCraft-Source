package jds.bibliocraft.items;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFramingSaw extends Item //implements IColorable
{
	public static final String name = "framingsaw";
	public static final ItemFramingSaw instance = new ItemFramingSaw();
	
	public ItemFramingSaw()
	{
		super(new Item.Properties().group(BiblioCraft.BiblioTab).maxStackSize(1)); // TODO ).containerItem(this)  figure out the container item deal
		//setContainerItem(this);
		//setCreativeTab(BlockLoader.biblioTab);
		//setUnlocalizedName(name);
		//setMaxStackSize(1);
		setRegistryName(BiblioCraft.MODID, name);
	}
/*
    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        /**
         * True if this Item has a container item (a.k.a. crafting result)
         *//*
        return true;
    }*/
}
