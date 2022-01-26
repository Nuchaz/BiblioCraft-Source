package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.gui.GuiWaypointCompass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWaypointCompass extends Item
{
	public static final String name = "compass";
	public static final ItemWaypointCompass instance = new ItemWaypointCompass();
	
	public ItemWaypointCompass()
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
			if (player.isSneaking())
			{
				openWaypointGUI(world, player, player.getHeldItem(hand));
			}
			else
			{
				NBTTagCompound tags = player.getHeldItem(hand).getTagCompound();
				if (tags == null)
				{
					this.createNewNBT(player.getHeldItem(hand));
				}
				if (tags != null)
				{
					int sX = tags.getInteger("XCoord");
					int sZ = tags.getInteger("ZCoord");
					String waypoint = tags.getString("WaypointName");
					String tooltip = waypoint+"  @  X = "+sX+"   Z = "+sZ;
					player.sendMessage(new TextComponentString(tooltip));
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
	
	
	@SideOnly(Side.CLIENT)
    public void openWaypointGUI(World world, EntityPlayer player, ItemStack compass)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiWaypointCompass(world, player, compass));
    }
	
	public ItemStack writeNBT(ItemStack compass, int xset, int zset, String waypointName)
	{
		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("XCoord", xset);
		tags.setInteger("ZCoord", zset);
		tags.setString("WaypointName", waypointName);
		compass.setTagCompound(tags);
		return compass;
	}

	public void createNewNBT(ItemStack compass)
	{
		NBTTagCompound newTags = new NBTTagCompound();
		newTags.setInteger("XCoord", 0);
		newTags.setInteger("ZCoord", 0);
		newTags.setString("WaypointName", "World Origin");
		newTags.setFloat("needleAngle", 0.0F);
		newTags.setDouble("time", 5.25D);
		newTags.setDouble("prevAngle", 0.0D);
	}
	
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			int sX = tags.getInteger("XCoord");
			int sZ = tags.getInteger("ZCoord");
			String waypoint = tags.getString("WaypointName");
			String tip = waypoint+" @ X="+sX+" Z="+sZ;
			tooltip.add(tip);
		}
		else
		{
			this.createNewNBT(stack);
		}
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	public void updateTheta(float angle, double prevAngle, double time, ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			tags.setFloat("needleAngle", angle);
			tags.setDouble("prevAngle", prevAngle);
			tags.setDouble("time", time);
		}
		else
		{
			this.createNewNBT(stack);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		if (entity != null && entity instanceof EntityPlayer)
		{
			int sX = 0;
			int sZ = 0;
			double time = 0.0d;
			double prevAngle = 0.0d;
			NBTTagCompound tags = stack.getTagCompound();
			if (tags == null)
			{
				this.createNewNBT(stack);
			}
			if (tags != null)
			{
				 sX = tags.getInteger("XCoord");
				 sZ = tags.getInteger("ZCoord");
				 time = tags.getDouble("time");
				 prevAngle = tags.getDouble("prevAngle");
			}
			EntityPlayer player = (EntityPlayer)entity;
			double yaw = MathHelper.wrapDegrees(player.rotationYaw) + 90.0d;
			double dx = sX - player.posX;
			double dz = sZ - player.posZ;
			double newAngle = yaw - (Math.atan2(dz, dx)*(180.0d/Math.PI));
			double angleDelta = Math.abs(prevAngle - newAngle);
			prevAngle = newAngle;
			double delta = 0.0d;
			boolean runDelta = false;
			if (angleDelta > 8.0d)
			{
				time -= 0.25;
				if (angleDelta > 40.0d)
				{
					time -= 0.2;
				}
			}
			else
			{
				runDelta = true;
				if (time <= 5.25)
				{
					time += 0.06;
				}
				else
				{
					time = 5.25d;
				}
			}
			if (time > 5.25)
			{
				time = 5.25d;
			}
			if (time <= 1.309D)
			{
				time = 1.309D;
			}
			if (runDelta)
			{
				// removed this line since it addes the bounce feature which isn't really visable so much with the new method of rendering. 
				//delta = Math.exp(-time)*(64*Math.sin(3*time)-64*Math.cos(3*time));
			}
			float theta = (float) (newAngle+delta);
			updateTheta(theta, prevAngle, time, stack);
		}
	}
	
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return true;
    }
}
