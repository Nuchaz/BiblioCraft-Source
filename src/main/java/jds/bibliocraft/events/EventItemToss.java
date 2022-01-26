package jds.bibliocraft.events;

import jds.bibliocraft.containers.ContainerAtlas;
import jds.bibliocraft.containers.ContainerSlottedBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventItemToss 
{
	@SubscribeEvent
	public void DroppedItemEvent(ItemTossEvent event)
	{
		EntityPlayer player = event.getPlayer();
		
		if (player != null)
		{
			ItemStack thing = event.getEntityItem().getItem();
			if (thing != ItemStack.EMPTY)
			{
				if (checkIfValidPacketItem(thing.getUnlocalizedName()))
				{
					Container testContainer = player.openContainer;
					if (testContainer != null)
					{
						if (hasProperContainer(thing.getUnlocalizedName(), testContainer))
						{
							player.closeScreen();
						}
					}
					else
					{
						player.closeScreen();
					}
				}
			}
		}
	}
	
	public static boolean checkIfValidPacketItem(String input)
	{
		String validPacketItems[] = {"item.AtlasBook", "item.BigBook", "item.RecipeBook", "item.BiblioClipboard", "item.BiblioRedBook", "item.SlottedBook", "item.BiblioWayPointCompass"};
		for (int i = 0; i < validPacketItems.length; i++)
		{
			if (validPacketItems[i].equals(input))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasProperContainer(String input, Container contained)
	{
		if (input.equals("item.AtlasBook"))
		{
			if (contained instanceof ContainerAtlas)
			{
				return true;
			}
		}
		if (input.equals("item.SlottedBook"))
		{
			if (contained instanceof ContainerSlottedBook)
			{
				return true;
			}
		}
		return false;
	}
}
