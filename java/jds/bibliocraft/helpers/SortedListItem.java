package jds.bibliocraft.helpers;

import java.util.ArrayList;

public class SortedListItem 
{
	public String itemName;
	public int itemQuantity;
	public ArrayList<InventoryListItem> inventoryList;
	
	public SortedListItem(String ItemName, int ItemQuantity, ArrayList<InventoryListItem> inventories)
	{
		this.itemName = ItemName;
		this.itemQuantity = ItemQuantity;
		this.inventoryList = inventories;
	}
}
