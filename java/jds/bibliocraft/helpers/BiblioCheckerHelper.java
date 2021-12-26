package jds.bibliocraft.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BiblioCheckerHelper 
{
	public static Block carpets[] = {Blocks.WHITE_CARPET, Blocks.ORANGE_CARPET, Blocks.MAGENTA_CARPET, Blocks.LIGHT_BLUE_CARPET, Blocks.YELLOW_CARPET,
	Blocks.LIME_CARPET, Blocks.PINK_CARPET, Blocks.GRAY_CARPET, Blocks.LIGHT_GRAY_CARPET, Blocks.CYAN_CARPET, Blocks.PURPLE_CARPET,
	Blocks.BLUE_CARPET, Blocks.BROWN_CARPET, Blocks.GREEN_CARPET, Blocks.RED_CARPET, Blocks.BLACK_CARPET};
	
	public static boolean isCarpet(ItemStack stack)
	{
		boolean output = false;
		Block blocko = Block.getBlockFromItem(stack.getItem());
		for (int i = 0; i < carpets.length; i++)
		{
			if (blocko == carpets[i])
			{
				output = true;
				break;
			}
		}
		
		return output;
	}
}
