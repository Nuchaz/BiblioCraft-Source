package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockFancyWorkbench;
import net.minecraft.block.Block;

public class BlockItemFancyWorkbench extends BiblioWoodBlockItem
{
	
	public static final BlockItemFancyWorkbench instance = new BlockItemFancyWorkbench(BlockFancyWorkbench.instance);
	
	public BlockItemFancyWorkbench(Block block)
	{
		super(block, BlockFancyWorkbench.name);
		setRegistryName(BlockFancyWorkbench.name);
	}
}
