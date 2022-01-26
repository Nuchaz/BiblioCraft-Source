package jds.bibliocraft.helpers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiblioWoodHelperTab  extends CreativeTabs
{
	private String[] textureString;
	private ItemStack icon;
	public BiblioWoodHelperTab(String name, String[] textures, ItemStack icon)
	{
		super(name);
		this.textureString = textures;
		this.icon = icon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() 
	{
		return this.icon;
		
	}
	
    @SideOnly(Side.CLIENT)
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
