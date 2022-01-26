package jds.bibliocraft;

import jds.bibliocraft.blocks.BlockLampGold;
import jds.bibliocraft.blocks.BlockLanternGold;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiblioLightsTab extends CreativeTabs
{
	public BiblioLightsTab(String name)
	{
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() 
	{
		if (Config.enableLantern)
		{
			return new ItemStack(BlockLanternGold.instance);
		}
		else if (Config.enableLamp)
		{
			return new ItemStack(BlockLampGold.instance);
		}
		else
		{
			return new ItemStack(Blocks.GLOWSTONE);
		}
		
	}

}
