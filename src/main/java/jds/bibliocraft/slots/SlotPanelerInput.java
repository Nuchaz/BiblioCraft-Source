package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerFurniturePaneler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPanelerInput extends Slot
{
	final ContainerFurniturePaneler container;
	
	public SlotPanelerInput(ContainerFurniturePaneler panelerContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.container = panelerContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if (this.container.tileEntity != null)
		{
			if (this.container.tileEntity.checkIfFramedBiblioCraftBlock(stack))
			{
				return true;
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
