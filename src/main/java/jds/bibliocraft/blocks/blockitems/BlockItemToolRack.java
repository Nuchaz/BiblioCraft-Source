package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockToolRack;
import net.minecraft.block.Block;

public class BlockItemToolRack extends BiblioWoodBlockItem
{
	public static final BlockItemToolRack instance = new BlockItemToolRack(BlockToolRack.instance);
	
	public BlockItemToolRack(Block block)
	{
		super(block, BlockToolRack.name);
		setRegistryName(BlockToolRack.name);
	}
}
