package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPlumbLine extends Item
{
	public static final String name = "PlumbLine";
	public static final ItemPlumbLine instance = new ItemPlumbLine();
	
	public ItemPlumbLine()
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
		if (world.isRemote)
		{
			float angle = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
	        ++angle;
	        angle %= 4; 
	        int yaw = (int)angle;
			if (yaw < 0.0f)
			{
				yaw = yaw + 360;
			}
			int initX = (int)player.posX;
			int initY = (int)player.posY;
			int initZ = (int)player.posZ;
			if (initX < 0)
			{
				initX--;
			}
			if (initZ < 0)
			{
				initZ--;
			}

			initY--;
			//initY--;
			switch (yaw)
			{
				case 0:
				{
					initX++;
					break;
				}
				case 1:
				{
					initZ++;
					break;
				}
				case 2:
				{
					initX--;
					break;
				}
				case 3:
				{
					initZ--;
					break;
				}
			}
			
			
			int lastY = initY;
			
			for (int i = 0; i < 255; i++)
			{
				lastY = initY - i;
				Block testBlock = world.getBlockState(new BlockPos(initX, lastY, initZ)).getBlock();
				if (testBlock instanceof BlockLiquid || world.isAirBlock(new BlockPos(initX, lastY, initZ)))
				{
					// continue
				}
				else
				{
					break;
				}
			}
			int depth = initY - lastY;
			int ypos = lastY + 1;
			player.sendMessage(new TextComponentString(I18n.translateToLocal("item.plumbline.depth")+" = "+depth+"m  @  y = "+ypos));
			
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
