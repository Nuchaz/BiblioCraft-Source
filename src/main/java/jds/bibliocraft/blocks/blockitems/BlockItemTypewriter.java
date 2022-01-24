package jds.bibliocraft.blocks.blockitems;

import jds.bibliocraft.blocks.BlockTypeWriter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockItemTypewriter extends ItemBlock
{
	private final static String[] subName = 
	{
			"WhiteTypewriter", 
			"LightGrayTypewriter", 
			"GrayTypewriter", 
			"BlackTypewriter", 
			"RedTypewriter", 
			"OrangeTypewriter", 
			"YellowTypewriter", 
			"LimeTypewriter", 
			"GreenTypewriter", 
			"CyanTypewriter", 
			"LightBlueTypewriter", 
			"BlueTypewriter", 
			"PurpleTypewriter", 
			"MagentaTypewriter", 
			"PinkTypewriter", 
			"BrownTypewriter"
	};
	
	public static final BlockItemTypewriter instance = new BlockItemTypewriter(BlockTypeWriter.instance);
	
	public BlockItemTypewriter(Block block)
	{
		super(block);
		setHasSubtypes(true);
		setRegistryName(BlockTypeWriter.name);
	}
	
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}
	
    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return subName[itemstack.getItemDamage()];
    }
}
