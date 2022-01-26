package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.helpers.BiblioSortingHelper;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioStockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ItemStockroomCatalog extends Item
{
	public static final String name = "StockroomCatalog";
	public static final ItemStockroomCatalog instance = new ItemStockroomCatalog();
	
	public ItemStockroomCatalog()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

	public NBTTagList getCompassList(EntityPlayer player)
	{
		NBTTagList tags = new NBTTagList();
		int[] compasses = {-1,-1,-1,-1,-1,-1,-1,-1};
		ItemStack[] compassStacks = {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
		for (int i = 0; i < player.inventory.getSizeInventory(); i++)
		{
			ItemStack invTest = player.inventory.getStackInSlot(i);
			if (invTest != ItemStack.EMPTY)
			{
				if (invTest.getItem() instanceof ItemWaypointCompass)
				{
					for (int n = 0; n < compasses.length; n++)
					{
						if (compasses[n] == -1)
						{
							compasses[n] = i;
							compassStacks[n] = invTest.copy();
							break;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < compasses.length; i++)
		{
			String invName = "compass"+i;
			String invSlotName = "slot"+i;
			NBTTagCompound tagComp = new NBTTagCompound();
			tagComp.setInteger(invSlotName, compasses[i]);
			if (compasses[i] != -1)
			{
				ItemStack stack = compassStacks[i];
				if (stack != ItemStack.EMPTY)
				{
					stack.writeToNBT(tagComp);
				}
			}
			tags.appendTag(tagComp);
		}
		return tags;
	}
    
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote && !player.isSneaking())
		{
			String title = I18n.translateToLocal("item.StockroomCatalog.name");
			NBTTagCompound tags = player.getHeldItem(hand).getTagCompound();
			if (tags != null && tags.hasKey("display"))
			{
				NBTTagCompound display = tags.getCompoundTag("display");
				title = display.getString("Name");
			}
			// ByteBuf buffer = Unpooled.buffer();
			NBTTagCompound loadedTags = BiblioSortingHelper.getFullyLoadedSortedListsInNBTTags(player.getHeldItem(hand), world);
			loadedTags.setTag("compasses", getCompassList(player));
			loadedTags.setString("title", title);
			BiblioNetworking.INSTANCE.sendTo(new BiblioStockLog(loadedTags), (EntityPlayerMP) player);
			// ByteBufUtils.writeTag(buffer, loadedTags);
			// BiblioCraft.ch_BiblioStockCatalog.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioStockLog"), (EntityPlayerMP) player);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if (!world.isRemote && player.isSneaking())
		{
			ItemStack stack = player.getHeldItemMainhand();
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof IInventory)
			{
				IInventory inventory = (IInventory)tile;
				int invSize = inventory.getSizeInventory();
				String invName = "stockcatalog.inventory.notfound";

				if (inventory.getDisplayName() != null)
				{
					invName = inventory.getDisplayName().getUnformattedText();
				} else if (inventory.getName() != null)
				{
					invName = inventory.getName();
				}
				
				NBTTagCompound tags = stack.getTagCompound();
				if (tags == null)
				{
					tags = new NBTTagCompound();
				}

				NBTTagList inventoryList = tags.getTagList("inventoryList", Constants.NBT.TAG_COMPOUND);
				if (inventoryList == null)
				{
					inventoryList = new NBTTagList();
				}

				if (inventoryList.tagCount() > 0)
				{
					for (int i = 0; i < inventoryList.tagCount(); i++)
					{
						NBTTagCompound invTag = inventoryList.getCompoundTagAt(i);
						if (invTag != null)
						{
							if (invTag.getString("name").equals(invName) && invTag.getInteger("x") == pos.getX() && invTag.getInteger("y") == pos.getY() && invTag.getInteger("z") == pos.getZ())
							{
								inventoryList.removeTag(i);
								tags.setTag("inventoryList", inventoryList);
								stack.setTagCompound(tags);
								return EnumActionResult.SUCCESS;
							}
						}
					}
				}
				NBTTagCompound newInvTag = new NBTTagCompound();
				newInvTag.setString("name", invName);
				newInvTag.setInteger("x", pos.getX());
				newInvTag.setInteger("y", pos.getY());
				newInvTag.setInteger("z", pos.getZ());
				inventoryList.appendTag(newInvTag);
				tags.setTag("inventoryList", inventoryList);
				stack.setTagCompound(tags);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
				return EnumActionResult.SUCCESS;
			}
		}
        return EnumActionResult.FAIL;
    }
}
