package jds.bibliocraft;

import jds.bibliocraft.blocks.BlockBookcase;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;


public class BiblioTab extends ItemGroup
{
	
	public BiblioTab(String name)
	{
		super(name);
	}
	/*
	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getTabIconItem() 
	{
		
		if (Config.enableBookcase)
		{
			return new ItemStack(BlockBookcase.instance);//Item.getItemFromBlock(BlockBookcase.instance);
		}
		/*
		else if (Config.enableArmorstand)
		{
			return Item.getItemFromBlock(BlockLoader.armorStand);
		}
		else if (Config.enableGenericshelf)
		{
			return Item.getItemFromBlock(BlockLoader.genericShelf);
		}
		else if (Config.enableToolrack)
		{
			return Item.getItemFromBlock(BlockLoader.toolRack);
		}
		*//*
		else
		{
			return new ItemStack(Blocks.BOOKSHELF);
		}
	}
*/

	@Override
	public ItemStack createIcon() 
	{
		return new ItemStack(BlockLoader.bookcases[0]);
	}
}
