package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.gui.GuiBigBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBigBook extends Item
{
	public String playername;
	public static final String name = "BigBook";
	public static final ItemBigBook instance = new ItemBigBook();
	
	public ItemBigBook()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote && hand == EnumHand.MAIN_HAND)
		{
			openGui(player.getHeldItem(hand), player.getDisplayNameString());
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
    
	@SideOnly(Side.CLIENT)
	public void openGui(ItemStack book, String author)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiBigBook(book, true, 0, 0, 0, author));
	}
	
	@Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) 
	{
		/*// TODO is broken, cant get access to the player and playername in this CLIENT SIDE thing, wtf?
	    List<EntityPlayer> p = world.playerEntities;
		if (p != null && p.size() > 0)
		{
			playername = p.get(0).getDisplayName().getFormattedText();
		}
		else
		{
			playername = "Douglas Adams";
		}
		*/
		playername = "Douglas Adams";
		NBTTagCompound bookTag = stack.getTagCompound();
		if (bookTag != null)
		{
			playername = bookTag.getString("author");
		}
		/*
		else
		{
			NBTTagCompound newbooktag = new NBTTagCompound();
			newbooktag.setString("author", playername);
			stack.setTagCompound(newbooktag);
		}
		*/
		tooltip.add(I18n.translateToLocal("redbook.by")+" "+playername);
    	super.addInformation(stack, world, tooltip, advanced);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			if (tags.getBoolean("signed"))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
    public int getItemEnchantability()
    {
        return 1;
    }
}
