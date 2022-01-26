package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;

public class ItemHandDrill extends ItemDrill
{
	public static final String name = "HandDrill";
	public static final ItemHandDrill instance = new ItemHandDrill();
	
	public ItemHandDrill()
	{
		super(name);
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
	}
	
    @Override
    public void playSound(EntityPlayer player)
    {
    	player.playSound(CommonProxy.SOUND_ITEM_HANDDRILL, 0.8F, 1.0F);
    }
    
}
