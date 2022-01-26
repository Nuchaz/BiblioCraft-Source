package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockPaintingFrameBorderless;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockItemPaintingFrameBorderless extends BiblioWoodBlockItem
{
	public static final BlockItemPaintingFrameBorderless instance = new BlockItemPaintingFrameBorderless(BlockPaintingFrameBorderless.instance);
	
	public BlockItemPaintingFrameBorderless(Block block)
	{
		super(block, BlockPaintingFrameBorderless.name); 
		setRegistryName(BlockPaintingFrameBorderless.name);
	}
	
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		tooltip.add(I18n.translateToLocal("paintingFrame.0tier"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}