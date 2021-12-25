package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockCase;
import net.minecraft.block.Block;

public class BlockItemCase extends BiblioWoodBlockItem
{
	public static final BlockItemCase instance = new BlockItemCase(BlockCase.instance);
	
	public BlockItemCase(Block block)
	{
		super(block, BlockCase.name);
		setRegistryName(BlockCase.name);
	}
}
