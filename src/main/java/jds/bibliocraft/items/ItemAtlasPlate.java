package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class ItemAtlasPlate extends Item
{
	public static final String name = "AtlasPlate";
	public static final ItemAtlasPlate instance = new ItemAtlasPlate();
	
	public ItemAtlasPlate()
	{
		super();
		setMaxStackSize(1);
		setUnlocalizedName(name);
		setCreativeTab(BlockLoader.biblioTab);
		setRegistryName(name);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null)
		{
			tooltip.add("Not Valid");
		}
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
