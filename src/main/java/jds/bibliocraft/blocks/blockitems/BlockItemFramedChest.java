package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockFramedChest;
import net.minecraft.block.Block;

public class BlockItemFramedChest extends BiblioWoodBlockItem
{
	public static final BlockItemFramedChest instance = new BlockItemFramedChest(BlockFramedChest.instance);
	
	public BlockItemFramedChest(Block block)
	{
		super(block, BlockFramedChest.name);
		setRegistryName(BlockFramedChest.name);
	}
}
