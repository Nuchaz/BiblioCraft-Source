package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.helpers.EnumWoodsType;
import net.minecraft.block.Block;

public class BlockItemBookcase extends BiblioWoodBlockItem
{
	//public static BlockItemBookcase instance = new BlockItemBookcase(BlockBookcase.instance); Borked again
	
	public BlockItemBookcase(Block block, EnumWoodsType wood)
	{
		super(block, BlockBookcase.name + wood.name());
		setRegistryName(BlockBookcase.name + wood.name());
	}
}
