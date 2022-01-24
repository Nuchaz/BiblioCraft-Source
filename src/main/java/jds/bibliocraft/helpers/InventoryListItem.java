package jds.bibliocraft.helpers;

public class InventoryListItem 
{

	public String itemName;
	public int itemQuantity;
	public String inventoryName;
	public int tileX;
	public int tileY;
	public int tileZ;
	
	public InventoryListItem(String ItemName, int ItemQuantity, String InventoryName, int tilex, int tiley, int tilez)
	{
		this.itemName = ItemName;
		this.itemQuantity = ItemQuantity;
		this.inventoryName = InventoryName;
		this.tileX = tilex;
		this.tileY = tiley;
		this.tileZ = tilez;
	}
}
