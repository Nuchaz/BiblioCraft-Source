package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockDesk;
import net.minecraft.block.Block;

public class BlockItemDesk extends BiblioWoodBlockItem
{
	public static final BlockItemDesk instance = new BlockItemDesk(BlockDesk.instance);
	
	public BlockItemDesk(Block block)
	{
		super(block, BlockDesk.name);
		setRegistryName(BlockDesk.name);
	}
}
