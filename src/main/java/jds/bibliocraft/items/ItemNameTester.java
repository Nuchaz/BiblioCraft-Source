package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemNameTester extends Item
{
	public static final String name = "TesterItem";
	public static final ItemNameTester instance = new ItemNameTester();
	
	public ItemNameTester()
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
		if (!world.isRemote)
		{
			player.openGui(BiblioCraft.instance, 102, player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		tooltip.add("Dev item to get info");
		tooltip.add("on blocks and items");
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
