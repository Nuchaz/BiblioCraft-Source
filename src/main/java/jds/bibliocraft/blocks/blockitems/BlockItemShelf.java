package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockShelf;
import net.minecraft.block.Block;

public class BlockItemShelf extends BiblioWoodBlockItem
{
	public static final BlockItemShelf instance = new BlockItemShelf(BlockShelf.instance);
	
	public BlockItemShelf(Block block)
	{
		super(block, BlockShelf.name);
		setHasSubtypes(true);
		setRegistryName(BlockShelf.name);
	}
}
