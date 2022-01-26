package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockBookcase;
import net.minecraft.block.Block;

public class BlockItemBookcase extends BiblioWoodBlockItem
{
	public static BlockItemBookcase instance = new BlockItemBookcase(BlockBookcase.instance);
	
	public BlockItemBookcase(Block block)
	{
		super(block, BlockBookcase.name);
		setRegistryName(BlockBookcase.name);
	}
}
