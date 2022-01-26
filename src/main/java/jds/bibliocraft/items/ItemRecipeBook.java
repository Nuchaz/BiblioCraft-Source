package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.Config;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioOpenBook;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRecipeBook extends Item
{
	public static final String name = "RecipeBook";
	public static final ItemRecipeBook instance = new ItemRecipeBook();
	
	private int[] ingredientCounts = new int[9];
	private String[] ingredientNames = new String[9];
	private int ingredientsTest;
	public boolean showText = false;
	public boolean showTextChanged = false;
	public String showTextString = "";
	
	public ItemRecipeBook()
	{
		super();
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote && hand == EnumHand.MAIN_HAND)
		{
			BiblioNetworking.INSTANCE.sendTo(new BiblioOpenBook(Config.enableRecipeBookCrafting), (EntityPlayerMP) player);
			// ByteBuf buffer = Unpooled.buffer();
			// buffer.writeBoolean(Config.enableRecipeBookCrafting);
			// BiblioCraft.ch_BiblioOpenBook.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioOpenBook"), (EntityPlayerMP) player);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null)
		{
			return nbt.getBoolean("signed");
		}
		return false;
	}
    /*
	@SideOnly(Side.CLIENT)
    public void openRecipeBookGUI(ItemStack stack, int slot)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiRecipeBook(stack, false, 0, 0, 0, slot)); 
    }
    */
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		compareingredients(stack);
		tooltip.add(I18n.translateToLocal("book.ingredients")); 
		for (int i = 0; i<this.ingredientCounts.length; i++)
		{
			ingredientsTest = this.ingredientCounts[i];
			if (this.ingredientCounts[i] != 0 && !this.ingredientNames[i].contains("Air"))
			{
				tooltip.add(this.ingredientCounts[i]+"x "+this.ingredientNames[i]);
			}
		}
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	public void updateFromPacket(String displayString)
	{
		this.showText = true;
		this.showTextChanged = true;
		this.showTextString = displayString;
	}
	
	public void compareingredients(ItemStack stack)
	{
		ingredientCounts = new int[9];
		ingredientNames = new String[9];
		if (stack.getItem() instanceof ItemRecipeBook)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null)
			{
				NBTTagList tagList = nbt.getTagList("grid", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < 9; i++)
				{
					NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
					byte slot = tag.getByte("Slot");
					//System.out.println(slot);
					if (slot >= 0 && slot < 9)
					{
						ItemStack nbtStack = new ItemStack(tag);
						if (nbtStack != ItemStack.EMPTY)
						{
							int n = 0;
							boolean complete = false;
							boolean havematch = false;
							for (int m = 0; m < this.ingredientNames.length; m++)
							{
								if (this.ingredientNames[m] != null)
								{
									if (this.ingredientNames[m].matches(nbtStack.getDisplayName()))
									{
										n = m;
										havematch = true;
									}
								}
							}
							
							if (havematch)
							{
								this.ingredientCounts[n] += 1;
							}
							else
							{
								while (!complete)
								{
									if (this.ingredientCounts[n] == 0)
									{
										this.ingredientCounts[n] += 1;
										this.ingredientNames[n] = nbtStack.getDisplayName();
										complete = true;
									}
									else
									{
										if (n < 8)
										{
											n++;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
