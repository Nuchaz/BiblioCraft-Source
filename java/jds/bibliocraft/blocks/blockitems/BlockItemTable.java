package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockTable;
import net.minecraft.block.Block;

public class BlockItemTable extends BiblioWoodBlockItem
{
	public static final BlockItemTable instance = new BlockItemTable(BlockTable.instance);
	
	public BlockItemTable(Block block)
	{
		super(block, BlockTable.name);
		setRegistryName(BlockTable.name);
	}
}
