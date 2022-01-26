package jds.bibliocraft.slots;

import jds.bibliocraft.Config;
import jds.bibliocraft.containers.ContainerFurniturePaneler;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPanalerPanels extends Slot
{
	final ContainerFurniturePaneler container;
	
	public SlotPanalerPanels(ContainerFurniturePaneler panelerContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.container = panelerContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//  only return true if block can be used as panel
		// check if block is rendered in full 3d so I only take full normal cubic blocks
		if (stack != ItemStack.EMPTY)
		{
			if (Config.isBlock(stack))
			{
				Block thing = Block.getBlockFromItem(stack.getItem());
				boolean thaumcraftException = stack.getUnlocalizedName().contains("tile.blockWoodenDevice");
				if ((thing.isOpaqueCube(thing.getDefaultState())) || thaumcraftException) 
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
}
