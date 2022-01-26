package jds.bibliocraft.items;

import jds.bibliocraft.BiblioTab;
import jds.bibliocraft.BlockLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemReadingGlasses extends ItemArmor //implements IArmorTextureProvider
{
	private final static String[] subName = {"readingglasses", "tintedglasses", "monocle"};
	public static final String name = "BiblioGlasses";
	public static ArmorMaterial BIBLIO_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("BiblioArmor", "monocle", 5, new int[]{1, 1, 1, 1}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);
	public static final ItemReadingGlasses instance = new ItemReadingGlasses();
	
	public ItemReadingGlasses()
	{
		super(BIBLIO_ARMOR_MATERIAL, 0, EntityEquipmentSlot.HEAD);
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setMaxDamage(0);
		setRegistryName(name);
	}
	
	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) 
	{
		return armorType == EntityEquipmentSlot.HEAD;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		switch (stack.getItemDamage())
		{
		case 1:{return "bibliocraft:textures/armor/glasses_darktint.png";}
		case 2:{return "bibliocraft:textures/armor/glasses_mono.png";}
		default:{return "bibliocraft:textures/armor/glasses.png";}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return getUnlocalizedName()+"."+subName[itemStack.getItemDamage()];
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
    	if (tab instanceof BiblioTab)
    	{
			for(int i = 0; i < subName.length; ++i)
	        {
	            list.add(new ItemStack(this, 1, i));
	        }
    	}
    }

}
