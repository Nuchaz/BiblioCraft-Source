package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockBookcaseCreative;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockItemBookcaseCreative extends BiblioWoodBlockItem
{
	public static BlockItemBookcaseCreative instance = new BlockItemBookcaseCreative(BlockBookcaseCreative.instance);;
	
	public BlockItemBookcaseCreative(Block block)
	{
		super(block, BlockBookcaseCreative.name);
		setHasSubtypes(true);
		setRegistryName(BlockBookcaseCreative.name);
		//instance = new BlockItemBookcaseCreative(block);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(I18n.translateToLocal("item.creativebookcase.randombooks"));
		tooltip.add("\u00a7d" + I18n.translateToLocal("item.creativebookcase.mode"));
    	if (stack.getItemDamage() == 6)
    	{
    		NBTTagCompound nbt = stack.getTagCompound();
    		if (nbt != null)
    		{
    			tooltip.add(I18n.translateToLocal("item.paneler.panels")+" \u00a7o"+nbt.getString("renderTexture"));
    		}
    	}
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
