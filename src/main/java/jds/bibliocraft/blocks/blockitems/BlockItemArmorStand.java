package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockArmorStand;
import net.minecraft.block.Block;

public class BlockItemArmorStand extends BiblioWoodBlockItem
{
	public static final BlockItemArmorStand instance = new BlockItemArmorStand(BlockArmorStand.instance);
	
	public BlockItemArmorStand(Block block)
	{
		super(block, BlockArmorStand.name);
		setRegistryName(BlockArmorStand.name);
	}
}
