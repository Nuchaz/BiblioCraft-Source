package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.blocks.BlockClipboard;
import jds.bibliocraft.gui.GuiClipboard;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClipboard extends Item
{
	public static final String name = "BiblioClipboard";
	public static final ItemClipboard instance = new ItemClipboard();
	public ItemStack clipboardstack;
	
	public ItemClipboard()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ)
	{
		if (player.isSneaking())
		{
			ItemStack stack = player.getHeldItemMainhand();
			if (stack != ItemStack.EMPTY)
			{
				switch (face)
				{
					case NORTH:{setClipboardBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1), EnumFacing.WEST, world, player, stack); break;}
					case SOUTH:{setClipboardBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1), EnumFacing.EAST, world, player, stack); break;}
					case WEST:{setClipboardBlock(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()), EnumFacing.SOUTH, world, player, stack); break;}
					case EAST:{setClipboardBlock(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()), EnumFacing.NORTH, world, player, stack); break;}
					default: break;
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}
	
	public void setClipboardBlock(BlockPos pos, EnumFacing angle, World world, EntityPlayer player, ItemStack stack)
	{
		Block testBlock = world.getBlockState(pos).getBlock();
		if (testBlock.isAir(world.getBlockState(pos), world, pos))
		{
			IBlockState state = BlockClipboard.instance.getDefaultState();
			world.setBlockState(pos, state);
			TileEntityClipboard clipboard = (TileEntityClipboard)world.getTileEntity(pos);
			if (clipboard != null)
			{
				clipboard.setAngle(angle);
				clipboard.setInventorySlotContents(0, stack);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY); 
				clipboard.getNBTData();
			}
		}
	}
    
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		NBTTagCompound test = player.getHeldItem(hand).getTagCompound();
		if (test == null)
		{
			NBTTagCompound clipboard = new NBTTagCompound();
			NBTTagCompound page = new NBTTagCompound();
			NBTTagCompound tasks = new NBTTagCompound();
			int[] taskstates = {0,0,0,0,0,0,0,0,0};
			page.setIntArray("taskStates", taskstates);
			tasks.setString("task1", "");
			tasks.setString("task2", "");
			tasks.setString("task3", "");
			tasks.setString("task4", "");
			tasks.setString("task5", "");
			tasks.setString("task6", "");
			tasks.setString("task7", "");
			tasks.setString("task8", "");
			tasks.setString("task9", "");
			page.setTag("tasks", tasks);
			page.setString("title", "");
			clipboard.setTag("page1",  page);
			clipboard.setInteger("currentPage", 1);
			clipboard.setInteger("totalPages", 1);
			player.getHeldItem(hand).setTagCompound(clipboard);
		}
		clipboardstack = player.getHeldItem(hand);
		if (!player.isSneaking() && world.isRemote  && hand == EnumHand.MAIN_HAND)
		{
			openWritingGUI(player.getHeldItem(hand),  true);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@SideOnly(Side.CLIENT)
    public void openWritingGUI(ItemStack stack, boolean inInv)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiClipboard(stack, inInv, 0, 0, 0));
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		String title = "";
		NBTTagCompound clipboard = stack.getTagCompound();
		if (clipboard != null)
		{
			int currpage = clipboard.getInteger("currentPage");
			if (currpage > 0)
			{
				String pagenum = "page"+currpage;
				NBTTagCompound page = clipboard.getCompoundTag(pagenum);
				if (page != null)
				{
					title = page.getString("title");
					title = title.trim();
					tooltip.add(title);
				}
			}
			
		}
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
}
