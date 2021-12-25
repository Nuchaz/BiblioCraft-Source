package jds.bibliocraft.helpers;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

public class CustomBlockItemDataPack 
{
	// Fancy Sign Data. I could add other data types in the future?
	public boolean hasData = false;
	EnumCustomDataType datatype = EnumCustomDataType.NONE;
	
	//fancy sign data. I could delegate these bits out for each block if I really need, but for now, this is good.
	public String[] fs_text = new String[15];
	public int[] fs_textScale = new int[15];
	public int fs_numOfLines = 10;
	public int fs_slot1Scale = 1;
	public int fs_slot1Rot = 0;
	public int fs_slot1X = 0;
	public int fs_slot1Y = 0;
	public int fs_slot2Scale = 1;
	public int fs_slot2Rot = 0;
	public int fs_slot2X = 0;
	public int fs_slot2Y = 0;
	public NonNullList<ItemStack> inv;
	
	public CustomBlockItemDataPack()
	{
		
	}
	
	public void AddFancySignData(NonNullList<ItemStack> inventory, int slot1scale, int slot1rot, int slot1x, int slot1y, int slot2scale, int slot2rot, int slot2x, int slot2y, int numOfLines, int[] textscale, String[] text)
	{
		this.datatype = EnumCustomDataType.FANCY_SIGN;
		this.hasData = true;
		this.inv = inventory;
		this.fs_slot1Scale = slot1scale;
		this.fs_slot1Rot = slot1rot;
		this.fs_slot1X = slot1x;
		this.fs_slot1Y = slot1y;
		this.fs_slot2Scale = slot2scale;
		this.fs_slot2Rot = slot2rot;
		this.fs_slot2X = slot2x;
		this.fs_slot2Y = slot2y;
		this.fs_numOfLines = numOfLines;
		this.fs_textScale = textscale;
		this.fs_text = text;
	}
	
	public static void applyDataToBlock(NBTTagCompound tags, BiblioTileEntity tile)
	{
		if (tile instanceof TileEntityFancySign && tags != null)
		{
			TileEntityFancySign sign = (TileEntityFancySign)tile;
			String[] textlines = new String[15];
			for (int n = 0; n<15; n++)
			{
				textlines[n] = tags.getString("text"+n); 
			}
	    	NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < sign.inventory.size())
				{
					sign.inventory.set(slot, new ItemStack(tag));
				}
			}
			sign.updateFromPacket(textlines, tags.getIntArray("textscale"), tags.getInteger("numoflines"), tags.getInteger("s1scale"), tags.getInteger("s1rot"), tags.getInteger("s1x"), tags.getInteger("s1y"), 
					tags.getInteger("s2scale"), tags.getInteger("s2rot"), tags.getInteger("s2x"), tags.getInteger("s2y"));
		}
	}
	
	public NBTTagCompound applyDataToItemStack(NBTTagCompound tags)
	{
		// TODO also would be nice if some of the sign info was rendered on the mini sign item.
		switch (this.datatype)
		{
			case FANCY_SIGN: 
			{
				tags.setInteger("s1scale", this.fs_slot1Scale);
				tags.setInteger("s1rot", this.fs_slot1Rot);
				tags.setInteger("s1x", this.fs_slot1X);
				tags.setInteger("s1y", this.fs_slot1Y);
				tags.setInteger("s2scale", this.fs_slot2Scale);
				tags.setInteger("s2rot", this.fs_slot2Rot);
				tags.setInteger("s2x", this.fs_slot2X);
				tags.setInteger("s2y", this.fs_slot2Y);
				tags.setInteger("numoflines", this.fs_numOfLines);
				tags.setIntArray("textscale", this.fs_textScale);
		    	for (int n = 0; n<15; n++)
		    	{
		    		tags.setString("text"+n, this.fs_text[n]); 
		    	}
		    	NBTTagList itemList = new NBTTagList();
		    	for (int i = 0; i < this.inv.size(); i++)
		    	{
		    		ItemStack stack = this.inv.get(i);
		    		if (stack != ItemStack.EMPTY)
		    		{
		    			NBTTagCompound tag = new NBTTagCompound();
		    			tag.setByte("Slot", (byte) i);
		    			stack.writeToNBT(tag);
		    			itemList.appendTag(tag);
		    		}
		    	}
		    	tags.setTag("Inventory", itemList);
				break;
			}
			default: break;
		}
		return tags;
	}
	
}
