package jds.bibliocraft.items;


import jds.bibliocraft.BlockLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemChase extends Item
{
	public static final String name = "BiblioChase";
	public static final ItemChase instance = new ItemChase();
	
	public ItemChase()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(64);
		setRegistryName(name);
	}
	
	@Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

}