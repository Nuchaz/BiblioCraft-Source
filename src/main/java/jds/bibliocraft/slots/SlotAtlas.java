package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerAtlas;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemMapTool;
import jds.bibliocraft.items.ItemTapeMeasure;
import jds.bibliocraft.items.ItemWaypointCompass;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraftforge.fml.common.Loader;

public class SlotAtlas extends Slot
{
	private ContainerAtlas atlasContainer;
	public SlotAtlas(ContainerAtlas container, IInventory iInventory, int slotIndex, int xPos, int yPos)
	{
		super(iInventory, slotIndex, xPos, yPos);
		atlasContainer = container;
	}
	
	@Override
	public int getSlotStackLimit()
    {
		//System.out.println("testy");
        return 64;
    }
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//System.out.println("test");
		return isAtlasItemValid(stack);
	}
	
	public boolean isAtlasItemValid(ItemStack stack)
	{
		// this is where I need to test for valid items
		// items include maps of any kind, compasses of any kind, books, clipboard. 
		//atlasContainer.updatedSlots = true;
		if (stack != ItemStack.EMPTY)
		{
			Item stuff = stack.getItem();
			
			if (stuff instanceof ItemAtlas)
			{
				return false;
			}
			if (stuff instanceof ItemWaypointCompass || 
				stuff == Items.MAP || 
				stuff instanceof ItemWritableBook || 
				stuff instanceof ItemWrittenBook ||
				stuff instanceof ItemBigBook ||
				stuff instanceof ItemClipboard ||
				stuff instanceof ItemMapTool ||
				stuff instanceof ItemTapeMeasure ||
				stuff instanceof ItemDrill)
			{
				return true;
			}
			if (Loader.isModLoaded("terrafirmacraft") && Loader.isModLoaded("BiblioWoodsTFC") && stuff == Items.PAPER)
			{
				return true;
			}
		}
		return false;
	}
}
