package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockPaintingFrameFancy;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockItemPaintingFrameFancy extends BiblioWoodBlockItem
{
	public static final BlockItemPaintingFrameFancy instance = new BlockItemPaintingFrameFancy(BlockPaintingFrameFancy.instance);
	
	public BlockItemPaintingFrameFancy(Block block)
	{
		super(block, BlockPaintingFrameFancy.name);
		setRegistryName(BlockPaintingFrameFancy.name);
	}
	
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		tooltip.add(I18n.translateToLocal("paintingFrame.4tier"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}