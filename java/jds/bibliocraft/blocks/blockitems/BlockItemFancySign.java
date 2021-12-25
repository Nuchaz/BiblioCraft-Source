package jds.bibliocraft.blocks.blockitems;

import java.util.List;

import jds.bibliocraft.blocks.BlockFancySign;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockItemFancySign extends BiblioWoodBlockItem
{
	public static final BlockItemFancySign instance = new BlockItemFancySign(BlockFancySign.instance);
	
	public BlockItemFancySign(Block block)
	{
		super(block, BlockFancySign.name);
		setRegistryName(BlockFancySign.name);
	}
	
	@Override
    public List<String> addAdditionalInformation(ItemStack stack, World world, List<String> tooltip)
    {
        NBTTagCompound tags = stack.getTagCompound();
        if (tags != null && tags.hasKey("textscale"))
        {
        	tooltip.add("This sign contains user data."); 
        }
    	return tooltip;
    }
}
