package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiblioWoodBlockItem extends ItemBlock
{
	private String[] names;

	public BiblioWoodBlockItem(Block block, String blockName)
	{
		super(block);
		setNames(blockName);
		setHasSubtypes(true);
	}
	
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
    
    private void setNames(String blockName)
    {
    	names = new String[EnumWoodType.values().length];
    	for (int i = 0; i < names.length; i++)
    	{
    		names[i] = EnumWoodType.getEnum(i).getName() + blockName;
    	}
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
    {
    	if (stack.getItemDamage() == 6)
    	{
    		NBTTagCompound nbt = stack.getTagCompound();
    		if (nbt != null)
    		{
    			tooltip.add(I18n.translateToLocal("item.paneler.panels")+" \u00a7o"+nbt.getString("renderTexture"));
    		}
    	}
    	tooltip = addAdditionalInformation(stack, playerIn, tooltip);
    	super.addInformation(stack, playerIn, tooltip, advanced);
    }
   
    public List<String> addAdditionalInformation(ItemStack stack, World world, List<String> tooltip)
    {
    	return tooltip;
    }
}
