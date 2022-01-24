package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerTypeMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPlate extends Slot
{
	final ContainerTypeMachine typemachine;
	
	public SlotPlate(ContainerTypeMachine typeContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.typemachine = typeContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//Item potionTest = stack.getItem();
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }

}
