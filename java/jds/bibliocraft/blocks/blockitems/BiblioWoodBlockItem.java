package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.blocks.BiblioWoodBlock;
import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.helpers.EnumWoodsType;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;;

public class BiblioWoodBlockItem extends BlockItem
{
	private String[] names;

	public BiblioWoodBlockItem(Block block, String blockName)
	{
		super(block, new Item.Properties().group(BiblioCraft.BiblioTab)); // TODO figure out this properties stuff for items
		//setNames(blockName);
		//setHasSubtypes(true);
	}
	/*
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}
	
    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return names[itemstack.getItemDamage()];
    }
    */
    private void setNames(String blockName)
    {
    	names = new String[EnumWoodType.values().length];
    	for (int i = 0; i < names.length; i++)
    	{
    		names[i] = EnumWoodType.getEnum(i).getName() + blockName;
    	}
    }
    

  //  @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced)   
    {
		Block theblock = Block.getBlockFromItem(stack.getItem());
    	if (theblock instanceof BiblioWoodBlock && ((BiblioWoodBlock)theblock).woodType == EnumWoodsType.framed)
    	{
    		CompoundNBT nbt = stack.getTag();
    		if (nbt != null)
    		{
    			tooltip.add(new StringTextComponent(LanguageMap.getInstance().translateKey("item.paneler.panels")+" \u00a7o"+nbt.getString("renderTexture")));
    		}
    	}
    	tooltip = addAdditionalInformation(stack, playerIn, tooltip);
    	super.addInformation(stack, playerIn, tooltip, advanced);
    }
   
    public List<ITextComponent> addAdditionalInformation(ItemStack stack, World world, List<ITextComponent> tooltip)
    {
    	return tooltip;
    }
}
