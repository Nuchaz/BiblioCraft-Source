package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemPaintingCanvas extends Item
{
	public static final String name = "PaintingCanvas";
	public static final ItemPaintingCanvas instance = new ItemPaintingCanvas();
	
	public ItemPaintingCanvas()
	{
		super();
		setMaxStackSize(64);
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setRegistryName(name);
	}

	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			String name = tags.getString("paintingTitle");
			if (name.length() > 0)
			{
				if (name.contains(".png"))
				{
					name = name.replace(".png", "");
				}
				tooltip.add(name);
			}
		}
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int x, boolean b)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null)
		{
			tags = new NBTTagCompound();
			tags.setBoolean("blank", true);
			stack.setTagCompound(tags);
		}
		if (tags.getBoolean("blank"))
		{
			if (this.maxStackSize != 64)
			{
				this.setMaxStackSize(64);
			}
		}
		else
		{
			if (this.maxStackSize != 1)
			{
				this.setMaxStackSize(1);
			}
		}
	}
    
}
