package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.gui.GuiRedstoneBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRedstoneBook extends Item
{
	public static final String name = "BiblioRedBook";
	public static final ItemRedstoneBook instance = new ItemRedstoneBook();
	public String playername;
	
	public ItemRedstoneBook()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(64);
		setRegistryName(name);
	}

	@Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) 
	{
		/*// TODO is so broken, cant access playername
	    List<EntityPlayer> p = player.playerEntities;
		if (p != null && p.size() > 0)
		{
		 playername = I18n.translateToLocal("redbook.by") + " " + p.get(0).getDisplayName().getFormattedText();
		}
		else
		{
			playername = "by James Maxwell";
		}
		*/
		playername = "by James Maxwell";
		NBTTagCompound bookTag = stack.getTagCompound();
		
		if (bookTag != null)
		{
			playername = bookTag.getString("redstonebook");
		}
		else
		{
			NBTTagCompound newbooktag = new NBTTagCompound();
			newbooktag.setString("redstonebook", playername);
			stack.setTagCompound(newbooktag);
		}
		
		tooltip.add(playername);
    	super.addInformation(stack, player, tooltip, advanced);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote && hand == EnumHand.MAIN_HAND)
		{
			openGui(player.getHeldItem(hand));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
	
	@SideOnly(Side.CLIENT)
	public void openGui(ItemStack book)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiRedstoneBook(book));
	}
}
