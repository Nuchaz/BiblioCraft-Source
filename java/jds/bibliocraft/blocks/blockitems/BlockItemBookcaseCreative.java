package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BiblioWoodBlock;
import jds.bibliocraft.blocks.BlockBookcaseCreative;
import jds.bibliocraft.helpers.EnumWoodsType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.World;

public class BlockItemBookcaseCreative extends BiblioWoodBlockItem
{
	//public static BlockItemBookcaseCreative instance = new BlockItemBookcaseCreative(BlockBookcaseCreative.instance);; TODO so so borked
	
	public BlockItemBookcaseCreative(Block block, EnumWoodsType wood)
	{
		super(block, BlockBookcaseCreative.name + wood.name());
		//setHasSubtypes(true);
		setRegistryName(BlockBookcaseCreative.name + wood.name());
		//instance = new BlockItemBookcaseCreative(block);
	}
	
	//@OnlyIn(Dist.CLIENT)
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced)  
	{
		tooltip.add(new StringTextComponent(LanguageMap.getInstance().translateKey("item.creativebookcase.randombooks")));
		tooltip.add(new StringTextComponent("\u00a7d" + LanguageMap.getInstance().translateKey("item.creativebookcase.mode")));
		Block theblock = Block.getBlockFromItem(stack.getItem());
    	if (theblock instanceof BiblioWoodBlock && ((BiblioWoodBlock)theblock).woodType == EnumWoodsType.framed)
    	{
    		CompoundNBT nbt = stack.getTag();
    		if (nbt != null)
    		{
    			tooltip.add(new StringTextComponent(LanguageMap.getInstance().translateKey("item.paneler.panels")+" \u00a7o"+nbt.getString("renderTexture")));
    		}
    	}
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
