package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockMapFrame;
import net.minecraft.block.Block;

public class BlockItemMapFrame extends BiblioWoodBlockItem
{
	public static final BlockItemMapFrame instance = new BlockItemMapFrame(BlockMapFrame.instance);
	
	public BlockItemMapFrame(Block block)
	{
		super(block, BlockMapFrame.name);
		setRegistryName(BlockMapFrame.name);
	}
}
