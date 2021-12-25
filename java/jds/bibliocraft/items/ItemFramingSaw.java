package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFramingSaw extends Item
{
	public static final String name = "FramingSaw";
	public static final ItemFramingSaw instance = new ItemFramingSaw();
	
	public ItemFramingSaw()
	{
		super();
		setContainerItem(this);
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        /**
         * True if this Item has a container item (a.k.a. crafting result)
         */
        return true;
    }
}
