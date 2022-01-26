package jds.bibliocraft.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BiblioSortingHelper 
{
	public static ArrayList<ItemStack> getStackForBuiltinLabel(TileEntity tile)
	{
		IInventory inv = (IInventory)tile;
		ArrayList chestOfStuff = new ArrayList();
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack thing = inv.getStackInSlot(i);
			if (thing != ItemStack.EMPTY)
			{
				boolean alreadyInList = false;
				for (int n = 0; n < chestOfStuff.size(); n++)
				{
					ItemStack oldThing = (ItemStack)chestOfStuff.get(n);
					if (oldThing != ItemStack.EMPTY)
					{
						if (oldThing.isItemEqual(thing) && oldThing.areItemStackTagsEqual(thing, oldThing))
						{
							oldThing.setCount(oldThing.getCount() + thing.getCount());
							int extras = getExtraStackQuanityFromSpecialInventories(tile, oldThing.getCount(), i);
							if (extras != -1)
							{
								oldThing.setCount(extras);
								chestOfStuff.set(n, oldThing);
							}
							alreadyInList = true;
							break;
						}
					}
				}
				
				if (!alreadyInList)
				{
					ItemStack newThing = thing.copy();
					int extras = getExtraStackQuanityFromSpecialInventories(tile, newThing.getCount(), i);
					if (extras != -1)
					{
						newThing.setCount(extras);
						chestOfStuff.add(newThing);
					}
				}
			}
		}
		//if (inv.getSizeInventory() == 0)
			//chestOfStuff.addAll(getSpecialInventoryQuanities(tile)); // TODO disabled until I figure this one out
		return chestOfStuff;
	}
	
	public static ArrayList<ItemStack> getSpecialInventoryQuanities(TileEntity tile)
	{
		ArrayList chestOfStuff = new ArrayList();
		NBTTagCompound tags = new NBTTagCompound();
		tile.writeToNBT(tags);
		// TODO here figure out how to read the inventory in storage drawers mod
		if (tags != null)
		{
			// Storage Drawers
			if (tags.hasKey("id") && tags.getString("id").contentEquals("storagedrawers:basicdrawers") && tile instanceof ICapabilityProvider)
			{
				ICapabilityProvider cap = ((ICapabilityProvider)tile);
				IItemHandler handler = ((ICapabilityProvider)tile).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
				NBTTagList tagList = tags.getTagList("Slots", Constants.NBT.TAG_COMPOUND);
				//IItemHandler handle = (IItemHandler)tile;
				if (tagList != null)
				{
					//int tagCount = tagList.tagCount();
					for (int i = 0; i < tagList.tagCount(); i++)
					{
						NBTTagCompound slotInfo = tagList.getCompoundTagAt(i);
						System.out.println("Count = " + slotInfo.getInteger("Count"));
						ItemStack thing = handler.extractItem(i, 99999, true);
						thing.setCount(slotInfo.getInteger("Count"));
						System.out.println("extract test" + thing);
						
						boolean alreadyInList = false;
						for (int n = 0; n < chestOfStuff.size(); n++)
						{
							ItemStack oldThing = (ItemStack)chestOfStuff.get(n);
							if (oldThing != ItemStack.EMPTY)
							{
								if (oldThing.isItemEqual(thing) && oldThing.areItemStackTagsEqual(thing, oldThing))
								{
									chestOfStuff.set(n, oldThing);
									alreadyInList = true;
								}
							}
						}
						if (!alreadyInList)
						{
							ItemStack newThing = thing.copy();
							chestOfStuff.add(newThing);
						}
					}
				}
			}
		}
		return chestOfStuff;
	}
	
	public static int getExtraStackQuanityFromSpecialInventories(TileEntity tile, int oldValue, int slotNum)
	{
		if (tile != null)
		{
			NBTTagCompound tags = new NBTTagCompound();
			tile.writeToNBT(tags);
			if (tags != null)
			{
				// JABBA
				if (tags.hasKey("id") && tags.getString("id").contentEquals("TileEntityBarrel"))
				{
					if (tags.hasKey("storage"))
					{
						NBTTagCompound storage = tags.getCompoundTag("storage");
						if (storage != null && storage.hasKey("amount"))
						{
							return storage.getInteger("amount");
						}
					}
				}
				// MineFactoryReloaded Deep Storage Unit
				if (tags.hasKey("id") && tags.getString("id").contentEquals("factoryDeepStorageUnit"))
				{
					if (tags.hasKey("storedQuantity"))
					{
						return tags.getInteger("storedQuantity");
					}
				}
				//Quantime Storage Unit tilequantumdsu
				if (tags.hasKey("id") && tags.getString("id").contentEquals("tilequantumdsu"))
				{
					if (tags.hasKey("storedQuantity"))
					{
						int quan = tags.getInteger("storedQuantity");

						if (slotNum == 0)
						{
							return oldValue;
							
						}
						if (hasSecondSlotItem(tags))
						{
							oldValue--;
						}
						if (slotNum == 2)
						{
							return -1;
						}

						return oldValue + quan;
					}
				}


				// Thermal Expansion Cache
				if (tags.hasKey("id") && tags.getString("id").contentEquals("thermalexpansion.Cache"))
				{
					if (tags.hasKey("Item"))
					{
						NBTTagCompound thing = tags.getCompoundTag("Item");
						if (thing != null && thing.hasKey("Count"))
						{
							return oldValue+thing.getInteger("Count");
						}
					}
				}
				
				if (tags.hasKey("id") && tags.getString("id").contentEquals("StorageDrawers:tileDrawersComp"))
				{
					NBTTagList tagList = tags.getTagList("Slots", Constants.NBT.TAG_COMPOUND);
					if (tagList != null && tags.hasKey("Count"))
					{
						NBTTagCompound slot1 = tagList.getCompoundTagAt(0);
						int count = tags.getInteger("Count");
						/*
						if (slot3 != null && slotNum == 5)
						{
							return count;
						}
						if (slot2 != null  && slotNum == 4)
						{
							int mod = tags.getInteger("Conv1");
							int measuredQuantity = count / mod;
							return measuredQuantity;
						}
						*/
						if (slot1 != null  && slotNum == 3)
						{
							int mod = tags.getInteger("Conv0");
							int measuredQuantity = count / mod;
							return measuredQuantity;
						}
						else
						{
							return -1;
						}
					}
				}
				// This is where I would add more custom inventory types if I need to do any more custom quantity calculations
				
			}
		}
		return oldValue;
	}
	
	public static boolean hasSecondSlotItem(NBTTagCompound tags)
	{
		if (tags.hasKey("Items"))
		{
			NBTTagList list = tags.getTagList("Items", Constants.NBT.TAG_COMPOUND);
			if (list != null)
			{
				for (int i = 0; i < list.tagCount(); i++)
				{
					NBTTagCompound comp = list.getCompoundTagAt(i);
					
					if (comp.hasKey("Slot"))
					{
						
						if (comp.getShort("Slot") == 2)
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public static ItemStack getLargestStackInList(ArrayList list)
	{
		ItemStack bestStack = ItemStack.EMPTY;
		for (int i = 0; i < list.size(); i++)
		{
			ItemStack stack = (ItemStack)list.get(i);
			if (stack != ItemStack.EMPTY)
			{
				if (bestStack != ItemStack.EMPTY)
				{
					if (stack.getCount() > bestStack.getCount())
					{
						bestStack = stack;
					}
				}
				else
				{
					bestStack = stack;
				}
			}
		}
		return bestStack;
	}
	
	public static int getLargestStackSlotInList(ArrayList list)
	{
		int output = -1;
		int testStackSize = 0;
		for (int i = 0; i < list.size(); i++)
		{
			ItemStack stack = (ItemStack)list.get(i);
			if (stack != ItemStack.EMPTY)
			{
					if (stack.getCount() > testStackSize)
					{
						testStackSize = stack.getCount();
						output = i;
					}
			}
		}
		return output;
	}
	
	public static ArrayList<InventorySet> getUnsortedInventoryListFromStockroomCatalog(ItemStack catalog, World world)
	{
		ArrayList<InventorySet> unsortedList = new ArrayList<InventorySet>();
		NBTTagCompound tags = catalog.getTagCompound();
		if (tags != null)
		{
			NBTTagList invList = tags.getTagList("inventoryList", Constants.NBT.TAG_COMPOUND);
			if (invList != null && invList.tagCount() > 0)
			{
				for (int i = 0; i < invList.tagCount(); i++)
				{
					NBTTagCompound invTag = invList.getCompoundTagAt(i);
					if (invTag != null && invTag.hasKey("x") && invTag.hasKey("y") && invTag.hasKey("z"))
					{
						int x = invTag.getInteger("x");
						int y = invTag.getInteger("y"); 
						int z = invTag.getInteger("z");
						String name = invTag.getString("name");
						ArrayList<ItemStack> inventoryStacks = new ArrayList<ItemStack>();
						TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
						if (tile != null && tile instanceof IInventory)
						{
							inventoryStacks = BiblioSortingHelper.getStackForBuiltinLabel(tile);
						}
						
						if (inventoryStacks.size() > 0)
						{
							unsortedList.add(new InventorySet(name, x, y, z, inventoryStacks));
						}
					}
				}
			}
		}
		return unsortedList;
	}
	
	public static ArrayList<SortedListItem> buildUnsortedItemList(ArrayList<InventorySet> inventoryset)
	{
		ArrayList<SortedListItem> itemList = new ArrayList<SortedListItem>();
		for (int i = 0; i < inventoryset.size(); i++)
		{
			InventorySet invSet = inventoryset.get(i);
			if (invSet != null)
			{
				ArrayList<ItemStack> inv = invSet.inventoryList;
				for (int j = 0; j < inv.size(); j++)
				{
					ItemStack stack = inv.get(j);
					if (stack != ItemStack.EMPTY)
					{
						InventoryListItem newInvListItem = new InventoryListItem(stack.getUnlocalizedName(), stack.getCount(), invSet.inventoryName, invSet.tileX, invSet.tileY, invSet.tileZ);
						boolean listHasItem = false;
						for (int k = 0; k < itemList.size(); k++)
						{
							SortedListItem item = itemList.get(k);
							if (item.itemName.contentEquals(stack.getUnlocalizedName()))
							{
								ArrayList<InventoryListItem> listOfInventories = item.inventoryList;
								boolean alreadyHasCurrentInventory = false;
								for (int n = 0; n < listOfInventories.size(); n++)
								{
									InventoryListItem invListItem = listOfInventories.get(n);
									if (invListItem.inventoryName.contentEquals(invSet.inventoryName) && invListItem.tileX == invSet.tileX && invListItem.tileY == invSet.tileY && invListItem.tileZ == invSet.tileZ)
									{
										invListItem.itemQuantity += stack.getCount();
										alreadyHasCurrentInventory = true;
										listOfInventories.set(n, invListItem);
										break;
									}
								}
								if (!alreadyHasCurrentInventory)
								{
									listOfInventories.add(newInvListItem);
								}
								listOfInventories = sortListOfInventories(listOfInventories);
								item.inventoryList = listOfInventories;
								item.itemQuantity += stack.getCount();
								listHasItem = true;
								break;
							}
						}
						if (!listHasItem)
						{
							ArrayList<InventoryListItem> listOfInventories = new ArrayList<InventoryListItem>();
							listOfInventories.add(newInvListItem);
							itemList.add(new SortedListItem(stack.getUnlocalizedName(), stack.getCount(), listOfInventories));
						}
					}
				}
			}
		}
		
		return itemList;
	}
	
	public static ArrayList<InventoryListItem> sortListOfInventories(ArrayList<InventoryListItem> list)
	{
		ArrayList<InventoryListItem> sortedList = new ArrayList<InventoryListItem>();
		ArrayList<InventoryListItem> tempList = (ArrayList<InventoryListItem>) list.clone();
		for (int i = 0; i < list.size(); i++)
		{
			int highestQuanIndex = getHighestQuantityInventoryFromList(tempList);
			if (highestQuanIndex != -1)
			{
				sortedList.add(tempList.get(highestQuanIndex));
				tempList.remove(highestQuanIndex);
			}
		}
		
		return sortedList; 
	}
	
	public static int getHighestQuantityInventoryFromList(ArrayList<InventoryListItem> list)
	{
		int quantity = 0;
		int index = -1;
		
		for (int i = 0; i < list.size(); i++)
		{
			InventoryListItem item = list.get(i);
			if (item.itemQuantity > quantity)
			{
				quantity = item.itemQuantity;
				index = i;
			}
		}
		return index;
	}
	
	public static ArrayList<SortedListItem> getUnsortedList(ItemStack catalog, World world)
	{
		return buildUnsortedItemList(getUnsortedInventoryListFromStockroomCatalog(catalog, world));
	}
	
	public static ArrayList<SortedListItem> getSortedListByQuantity(ArrayList<SortedListItem> list)
	{
		ArrayList<SortedListItem> sortedList = new ArrayList<SortedListItem>();
		ArrayList<SortedListItem> tempList = (ArrayList<SortedListItem>) list.clone();
		for (int i = 0; i < list.size(); i++)
		{
			int highestQuanIndex = getHighestQuantityItemFromList(tempList);
			if (highestQuanIndex != -1)
			{
				sortedList.add(tempList.get(highestQuanIndex));
				tempList.remove(highestQuanIndex);
			}
		}
		return sortedList;
	}
	
	public static int getHighestQuantityItemFromList(ArrayList<SortedListItem> list)
	{
		int quantity = 0;
		int index = -1;
		
		for (int i = 0; i < list.size(); i++)
		{
			SortedListItem item = list.get(i);
			if (item.itemQuantity > quantity)
			{
				quantity = item.itemQuantity;
				index = i;
			}
		}
		return index;
	}
	
	public static ArrayList<SortedListItem> getSortedListByAlphabet(ArrayList<SortedListItem> list)
	{
		ArrayList<SortedListItem> sortedList = new ArrayList<SortedListItem>();
		List<String> localizedNameList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++)
		{
			SortedListItem item = list.get(i);
			if (item != null)
			{
				localizedNameList.add(I18n.translateToLocal(item.itemName+".name"));
			}
		}
		
		Collections.sort(localizedNameList);
		
		for (int i = 0; i < localizedNameList.size(); i++)
		{
			String localName = localizedNameList.get(i);
			for (int j = 0; j < list.size(); j++)
			{
				SortedListItem item = list.get(j);
				if (localName.contentEquals(I18n.translateToLocal(item.itemName+".name")))
				{
					sortedList.add(item);
					break;
				}
			}
		}
		
		return sortedList;
	}
	
	public static NBTTagList convertArrayListToNBTTagList(ArrayList<SortedListItem> list)
	{
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < list.size(); i++)
		{
			SortedListItem item = list.get(i);
			NBTTagCompound subTag = new NBTTagCompound();
			NBTTagList invTagList = new NBTTagList();
			subTag.setString("itemName", item.itemName);
			subTag.setInteger("itemQuantity", item.itemQuantity);
			ArrayList<InventoryListItem> invList = item.inventoryList;
			for (int j = 0; j < invList.size(); j++)
			{
				NBTTagCompound invTag = new NBTTagCompound();
				InventoryListItem invItem = invList.get(j);
				invTag.setString("itemName", invItem.itemName);
				invTag.setString("inventoryName", invItem.inventoryName);
				invTag.setInteger("itemQuantity", invItem.itemQuantity);
				invTag.setInteger("tileX", invItem.tileX);
				invTag.setInteger("tileY", invItem.tileY);
				invTag.setInteger("tileZ", invItem.tileZ);
				invTagList.appendTag(invTag);
			}
			subTag.setTag("inventories", invTagList);
			tagList.appendTag(subTag);
		}
		
		return tagList;
	}
	
	public static NBTTagCompound getFullyLoadedSortedListsInNBTTags(ItemStack catalog, World world)
	{
		NBTTagCompound tags = new NBTTagCompound();
		ArrayList<SortedListItem> unsortedList = BiblioSortingHelper.getUnsortedList(catalog, world);
		ArrayList<SortedListItem> alphaList = BiblioSortingHelper.getSortedListByAlphabet(unsortedList);
		ArrayList<SortedListItem> quantaList = BiblioSortingHelper.getSortedListByQuantity(unsortedList);
		tags.setTag("alphaList", convertArrayListToNBTTagList(alphaList));
		tags.setTag("quantaList", convertArrayListToNBTTagList(quantaList));
		return tags;
	}
	
	public static ArrayList<SortedListItem> convertNBTTagListToArrayList(NBTTagList tags)
	{
		ArrayList<SortedListItem> list = new ArrayList<SortedListItem>();
		
		for (int i = 0; i < tags.tagCount(); i++)
		{
			
			NBTTagCompound subTag = tags.getCompoundTagAt(i);
			NBTTagList invTags = subTag.getTagList("inventories", Constants.NBT.TAG_COMPOUND);
			ArrayList<InventoryListItem> invList = new ArrayList<InventoryListItem>();
			for (int j = 0; j < invTags.tagCount(); j++)
			{
				NBTTagCompound inv = invTags.getCompoundTagAt(j);
				invList.add(new InventoryListItem(inv.getString("itemName"), inv.getInteger("itemQuantity"), inv.getString("inventoryName"), inv.getInteger("tileX"), inv.getInteger("tileY"), inv.getInteger("tileZ")));
			}
			
			SortedListItem item = new SortedListItem(subTag.getString("itemName"), subTag.getInteger("itemQuantity"), invList);
			list.add(item);
		}
		return list;
	}
	
	public static void listTester(ArrayList<SortedListItem> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			SortedListItem item = list.get(i);
			if (item != null)
			{
				System.out.println("item name: "+I18n.translateToLocal(item.itemName+".name")+"      Quantity: "+item.itemQuantity+"      Inside x inventories: "+item.inventoryList.size());
			}
		}
	}
}
