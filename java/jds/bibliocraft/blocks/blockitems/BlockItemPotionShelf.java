package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockPotionShelf;
import net.minecraft.block.Block;

public class BlockItemPotionShelf extends BiblioWoodBlockItem
{
	public static final BlockItemPotionShelf instance = new BlockItemPotionShelf(BlockPotionShelf.instance);
	
	public BlockItemPotionShelf(Block block)
	{
		super(block, BlockPotionShelf.name);
		setRegistryName(BlockPotionShelf.name);
	}
}
