package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BlockLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLock extends Item
{
	public static final String name = "BiblioCreativeLock";
	public static final ItemLock instance = new ItemLock();
	
	public ItemLock()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			
		}
		return EnumActionResult.FAIL;
	}
	
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
		tooltip.add(I18n.translateToLocal("lock.creativeonly"));
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
