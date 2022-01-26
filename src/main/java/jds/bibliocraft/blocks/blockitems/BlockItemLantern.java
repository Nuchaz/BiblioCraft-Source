package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockLanternGold;
import jds.bibliocraft.blocks.BlockLanternIron;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockItemLantern extends ItemBlock
{
	
	private final static String[] lanternNames = 
		{
			"whiteLantern",
			"lightGrayLantern",
			"grayLantern",
			"blackLantern",
			"redLantern",
			"orangeLantern",
			"yellowLantern",
			"limeLantern",
			"greenLantern",
			"cyanLantern",
			"lightBlueLantern",
			"blueLantern",
			"purpleLantern",
			"magentaLantern",
			"pinkLantern",
			"brownLantern"
		};
	
	private final static String[] candleColors = 
	{
		I18n.translateToLocal("lantern.candle0"),
		I18n.translateToLocal("lantern.candle1"),
		I18n.translateToLocal("lantern.candle2"),
		I18n.translateToLocal("lantern.candle3"),
		I18n.translateToLocal("lantern.candle4"),
		I18n.translateToLocal("lantern.candle5"),
		I18n.translateToLocal("lantern.candle6"),
		I18n.translateToLocal("lantern.candle7"),
		I18n.translateToLocal("lantern.candle8"),
		I18n.translateToLocal("lantern.candle9"),
		I18n.translateToLocal("lantern.candle10"),
		I18n.translateToLocal("lantern.candle11"),
		I18n.translateToLocal("lantern.candle12"),
		I18n.translateToLocal("lantern.candle13"),
		I18n.translateToLocal("lantern.candle14"),
		I18n.translateToLocal("lantern.candle15")
	};
	
	public static final BlockItemLantern instanceGold = new BlockItemLantern(BlockLanternGold.instance, BlockLanternGold.name);
	public static final BlockItemLantern instanceIron = new BlockItemLantern(BlockLanternIron.instance, BlockLanternIron.name);
	
	public BlockItemLantern(Block block, String name)
	{
		super(block);
		setHasSubtypes(true);
		setRegistryName(name);
	}
	
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}
	
    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return lanternNames[itemstack.getItemDamage()];
    }
    
    @SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		boolean isIron = false;
		if (stack != ItemStack.EMPTY && stack.getItem() == Item.getItemFromBlock(BlockLanternIron.instance))
		{
			isIron = true;
		}
		int meta = stack.getItemDamage();
		tooltip.add(candleColors[meta]);
		if (isIron)
		{
			tooltip.add(I18n.translateToLocal("lighting.metalIron"));
		}
		else
		{
			tooltip.add(I18n.translateToLocal("lighting.metalGold")); 
		}
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}