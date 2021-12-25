package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerTypeMachine;
import jds.bibliocraft.items.ItemChase;
import jds.bibliocraft.items.ItemPlate;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotChase extends Slot
{
	final ContainerTypeMachine typemachine;
	
	public SlotChase(ContainerTypeMachine typeContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.typemachine = typeContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//Item potionTest = stack.getItem();
		Item chaseTest = stack.getItem();
		//int itemID = stack.itemID;
		if(chaseTest instanceof ItemChase || chaseTest instanceof ItemPlate)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }


}
