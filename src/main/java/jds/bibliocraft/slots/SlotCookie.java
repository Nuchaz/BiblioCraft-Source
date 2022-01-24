package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerCookieJar;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCookie extends Slot
{
	final ContainerCookieJar cookiejar;
	
	public SlotCookie(ContainerCookieJar cookieContainer, IInventory iInventory, int i, int j, int k)
	{
		super(iInventory, i, j, k);
		this.cookiejar = cookieContainer;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 64;
	}
}
