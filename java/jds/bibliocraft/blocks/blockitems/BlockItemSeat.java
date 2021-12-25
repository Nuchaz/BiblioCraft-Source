package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockSeat;
import net.minecraft.block.Block;

public class BlockItemSeat extends BiblioWoodBlockItem
{
	public static final BlockItemSeat instance = new BlockItemSeat(BlockSeat.instance);
	
	public BlockItemSeat(Block block)
	{
		super(block, BlockSeat.name);
		setRegistryName(BlockSeat.name);
	}
}
