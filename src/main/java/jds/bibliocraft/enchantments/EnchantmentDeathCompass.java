package jds.bibliocraft.enchantments;

import jds.bibliocraft.items.ItemAtlas;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class EnchantmentDeathCompass extends Enchantment
{
	public EnchantmentDeathCompass()
	{
		super(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.ALL, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		this.setName("enchant.deathcompass");
		this.setRegistryName("bibliocraft.deathcompassench");
	}
	
	@Override
    public int getMinEnchantability(int par1)
    {
        return 10 + 20 * (par1 - 1);
    }
    
	@Override
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }
    
	@Override
    public int getMaxLevel()
    {
        return 1;
    }
	
	@Override
	public String getName()
	{
		return I18n.translateToLocal("enchant.deathcompass"); 
	}
	
	@Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
		if (stack.getItem() instanceof ItemAtlas)
		{
			return true;
		}
		else
		{
			return false;
		}
    }
	
	@Override
	public boolean canApply(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemAtlas)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
