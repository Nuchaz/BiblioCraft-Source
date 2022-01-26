package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPlate extends Item
{
	public static final String name = "PrintPlate";
	public static final ItemPlate instance = new ItemPlate();
	
	public ItemPlate()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		String book = I18n.translateToLocal("plate.notValid"); //"author, title";
		
		NBTTagCompound bookTag = stack.getTagCompound();
		
		if (bookTag != null)
		{
			book = bookTag.getString("bookName");
		}
		else
		{
			NBTTagCompound newbooktag = new NBTTagCompound();
			newbooktag.setString("bookName", book);
			stack.setTagCompound(newbooktag);
		}
		tooltip.add(book);
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
}