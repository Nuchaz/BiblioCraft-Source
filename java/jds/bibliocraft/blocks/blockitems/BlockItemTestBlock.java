package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.BiblioCraft;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockItemTestBlock extends BlockItem
{

	public BlockItemTestBlock(Block block, String blockName) 
	{
		//block, new Item.Properties().group(BiblioCraft.BiblioTab)
		super(block, new Item.Properties().group(BiblioCraft.BiblioTab));
		setRegistryName(blockName);
	}

}
