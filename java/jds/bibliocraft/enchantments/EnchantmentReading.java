package jds.bibliocraft.enchantments;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.LanguageMap;


public class EnchantmentReading extends Enchantment
{
	
	public EnchantmentReading()
	{
		//protected Enchantment(Enchantment.Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots)
		super(Enchantment.Rarity.UNCOMMON, EnchantmentType.ARMOR_HEAD, new EquipmentSlotType[]{EquipmentSlotType.HEAD});
		this.setRegistryName(LanguageMap.getInstance().translateKey("enchant.reading"));
		this.setRegistryName("bibliocraft.readingench");
	}
	
	@Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 10 + 15 * (enchantmentLevel - 1);
    }
    /* this is gone I guess
	@Override
    public int getMaxEnchantability(int par1)
    {
		//System.out.println(par1);
        return super.getMinEnchantability(par1) + 50;
    }*/
    
	@Override
    public int getMaxLevel()
    {
        return 1;
    }
	
	@Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
		if (stack.getItem() instanceof BookItem)
		{
			return true;
		}
		else
		{
			return false;
		}
    }
	
	@Override
	public String getName()
	{
		return LanguageMap.getInstance().translateKey("enchant.reading"); 
	}

}
