package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockFurniturePaneler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
