package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockLabel;
import net.minecraft.block.Block;

public class BlockItemLabel extends BiblioWoodBlockItem
{
	public static final BlockItemLabel instance = new BlockItemLabel(BlockLabel.instance);
	
	public BlockItemLabel(Block block)
	{
		super(block, BlockLabel.name);
		setRegistryName(BlockLabel.name);
	}
}
