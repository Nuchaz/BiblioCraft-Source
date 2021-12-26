package jds.bibliocraft.helpers;

import jds.bibliocraft.blocks.BlockBookcase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;;

public class BiblioWoodHelperTab  //extends CreativeTabs
{
	private String[] textureString;
	private ItemStack icon;
	public BiblioWoodHelperTab(String name, String[] textures, ItemStack icon)
	{
		//super(name);
		this.textureString = textures;
		this.icon = icon;
	}
	/*
	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getTabIconItem() 
	{
		return this.icon;
		
	}
	*/
    @OnlyIn(Dist.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> list)
    {
    	for (int i = 0; i < textureString.length; i++)
    	{
        	RegisterCustomFramedBlocks reg = new RegisterCustomFramedBlocks(textureString[i]);
        	for (int j = 0; j < reg.getFramedBlockList().size(); j++)
        	{
        		if (reg.getEnableList()[j])
        			list.add(reg.getFramedBlockList().get(j));
        	}
    	}
    }
}
