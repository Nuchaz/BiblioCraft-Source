package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockClock;
import net.minecraft.block.Block;

public class BlockItemClock extends BiblioWoodBlockItem
{
	public static final BlockItemClock instance = new BlockItemClock(BlockClock.instance);
	
	public BlockItemClock(Block block)
	{
		super(block, BlockClock.name);
		setRegistryName(BlockClock.name);
	}
}
