package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockSwordPedestal;
import jds.bibliocraft.helpers.EnumColor;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockItemSwordPedestal extends ItemBlock
{
	public static final BlockItemSwordPedestal instance = new BlockItemSwordPedestal(BlockSwordPedestal.instance);
	//private final static String[] colors = {"white", "orange", "magenta", "lightblue", "yellow", "lime", "pink", "gray", "lightgray", "cyan", "purple", "blue", "brown", "green", "red", "black"}; 
	
	public BlockItemSwordPedestal(Block block)
	{
		super(block);
		setHasSubtypes(true);
		setRegistryName(BlockSwordPedestal.name);
	}
	
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}
	
    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return "swordpedestal";//_"+colors[itemstack.getItemDamage()];
    }
    
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		tooltip.add(I18n.translateToLocal("swordpedestal."+EnumColor.values()[stack.getItemDamage()].getName()));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}