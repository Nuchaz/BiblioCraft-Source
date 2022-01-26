package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSlottedBook extends Item {
	public String playername;
	public static final String name = "SlottedBook";
	public static final ItemSlottedBook instance = new ItemSlottedBook();

	public ItemSlottedBook() {
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
			player.openGui(BiblioCraft.instance, 101, player.world, (int) player.posX, (int) player.posY,
					(int) player.posZ);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		/*
		 * // TODO so broken, cant access playername
		 * List<EntityPlayer> p = player.playerEntities;
		 * if (p != null && p.size() > 0)
		 * {
		 * playername = I18n.translateToLocal("redbook.by") +
		 * " "+p.get(0).getDisplayName().getFormattedText();
		 * }
		 * else
		 * {
		 * playername = "by Sir Hidington";
		 * }
		 */
		playername = "by Sir Hidington";
		NBTTagCompound bookTag = stack.getTagCompound();
		if (bookTag != null) {
			playername = bookTag.getString("authorName");
		} else {
			NBTTagCompound newbooktag = new NBTTagCompound();
			newbooktag.setString("authorName", playername);
			stack.setTagCompound(newbooktag);
		}
		tooltip.add(playername);
		super.addInformation(stack, player, tooltip, advanced);
	}
}
