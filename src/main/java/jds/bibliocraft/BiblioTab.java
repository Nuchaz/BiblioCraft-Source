package jds.bibliocraft;


import jds.bibliocraft.blocks.BlockBookcase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiblioTab extends CreativeTabs
{
	
	public BiblioTab(String name)
	{
		super(name);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
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
		*/
		else
		{
			return new ItemStack(Blocks.BOOKSHELF);
		}
	}

}
