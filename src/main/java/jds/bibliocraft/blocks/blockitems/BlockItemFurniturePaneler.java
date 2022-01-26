package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockFurniturePaneler;
import net.minecraft.block.Block;

public class BlockItemFurniturePaneler extends BiblioWoodBlockItem
{
	public static final BlockItemFurniturePaneler instance = new BlockItemFurniturePaneler(BlockFurniturePaneler.instance);
//private final static String[] subName = {"OakPaneler", "SprucePaneler", "BirchPaneler", "JunglePaneler", "AcaciaPaneler", "OldOakPaneler", "FramedPaneler"}; 
	
	public BlockItemFurniturePaneler(Block block)
	{
		super(block, BlockFurniturePaneler.name);
		setHasSubtypes(true);
		setRegistryName(BlockFurniturePaneler.name);
	}
}
