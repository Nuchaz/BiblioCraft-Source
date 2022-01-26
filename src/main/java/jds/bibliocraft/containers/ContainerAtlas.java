package jds.bibliocraft.containers;


import java.util.ArrayList;

import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioAtlasClient;
import jds.bibliocraft.slots.SlotAtlas;
import jds.bibliocraft.slots.SlotAtlasMap;
import jds.bibliocraft.slots.SlotLocked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.util.Constants;

public class ContainerAtlas extends Container
{
	private InventoryBasic atlasInventory;
	private ItemStack atlasStack;
	private int mapSlot;
	protected SlotAtlas atlasSlot;
	protected SlotAtlasMap atlasMapSlot;
	private World world;
	public boolean updatedSlots = false;
	private boolean initalLoad = false;
	EntityPlayer player;
	private int mapsPage = 0;
	
	public ContainerAtlas(InventoryPlayer inventoryPlayer, World myworld)
	{
		atlasInventory = new InventoryBasic("AtlasInventory", true, 48); //  maybe temp, chaged from 48 to 216 to allow 5 pages
		atlasStack = inventoryPlayer.getCurrentItem();
		world = myworld;
		player = inventoryPlayer.player;
		updateInventory();
		resetSlots();

		bindPlayerInventory(inventoryPlayer);
	}
	
	private void resetSlots()
	{
		this.inventorySlots = new ArrayList();
		this.inventoryItemStacks = NonNullList.<ItemStack>create();//new ArrayList(); // TODO replaced the arraylist with NonNullList
		for (int i = 0; i<6; i++)
		{
			addSlotToContainer(this.atlasSlot = new SlotAtlas(this, atlasInventory, i, 11+(i*18), 123));
		}
		
		for (int i = 0; i<7; i++)
		{
			for (int j = 0; j<6; j++)
			{
				addSlotToContainer(this.atlasMapSlot = new SlotAtlasMap(this, atlasInventory, (j+i*6)+6+(42*mapsPage), 138+(j*18), 15+(i*18)));
			}
		}
	}
	
	public void updateInventory()
	{
		if (atlasStack != ItemStack.EMPTY && atlasStack.getItem() instanceof ItemAtlas)
		{
			NBTTagCompound tags = atlasStack.getTagCompound();
			if (tags != null)
			{
				this.mapSlot = tags.getInteger("mapSlot");
				NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < tagList.tagCount(); i++)
				{
					
					NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
					byte slot = tag.getByte("Slot");
					if (slot >= 0 && slot < atlasInventory.getSizeInventory())
					{
						ItemStack invStack = new ItemStack(tag);
						this.atlasInventory.setInventorySlotContents(slot, invStack);
					}
				}
			}
			this.initalLoad = true;
		}
	}
	
	@Override
    public void detectAndSendChanges()
    {

        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
            {
                itemstack1 = itemstack == ItemStack.EMPTY ? ItemStack.EMPTY : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                for (int j = 0; j < this.listeners.size(); ++j)
                {
                    ((IContainerListener)this.listeners.get(j)).sendSlotContents(this, i, itemstack1);
                    if (i < 48)
                    {
                    	this.updatedSlots = true;
                    }
                }
            }
        }
        
		if (this.updatedSlots && this.initalLoad)
		{
			// time to update data in the NBT about the compass and maps locations and whatnots
			updateClientInventoryAndData();
			this.updatedSlots = false;
		}
    }
	
	private void updateClientInventoryAndData()
	{
		NBTTagCompound tags = this.atlasStack.getTagCompound();
		boolean foundCompass = false;
		if (tags != null)
		{
			if (tags.hasKey("savedCompass"))
			{
				int comp = tags.getInteger("savedCompass");
				int xcoord = tags.getInteger("compassX");
				int zcoord = tags.getInteger("compassZ");
				// it might be better to run a loop through the first 6 slots and check all the compasses and update the number if it is different.
				for (int n=0; n<6; n++)
				{
					ItemStack compStack = (ItemStack)this.inventoryItemStacks.get(n);
					if (compStack != ItemStack.EMPTY && compStack.getItem() instanceof ItemWaypointCompass)
					{
						NBTTagCompound compassTags = compStack.getTagCompound();
						if (compassTags != null)
						{
							if (xcoord == compassTags.getInteger("XCoord") && zcoord == compassTags.getInteger("ZCoord"))
							{
								// we have a winner, no further action required for compass
								if (n != comp)
								{
									tags.setInteger("savedCompass", n);
									//this.atlasStack.setTagCompound(tags);
									
								}
								foundCompass = true;
							}
						}
					}
				}
				
				if (!foundCompass && comp >= 0)
				{
					tags.setInteger("savedCompass", -1);
				}
			}
			tags.setTag("maps", getUpdatedMapTagList(tags));
			NBTTagList itemList = new NBTTagList();
	    	for (int i = 0; i < atlasInventory.getSizeInventory(); i++) 
	    	{
	    		ItemStack stack = atlasInventory.getStackInSlot(i);
	    		if (stack != ItemStack.EMPTY)
	    		{
	    			NBTTagCompound tag = new NBTTagCompound();
	    			tag.setByte("Slot", (byte) i);
	    			stack.writeToNBT(tag);
	    			itemList.appendTag(tag);
	    		}
	    	}
	    	tags.setTag("Inventory", itemList);
	    	tags.setBoolean("containerUpdate", true);

			this.atlasStack.setTagCompound(tags);
			if (!(world.isRemote))
			{
				if (player instanceof EntityPlayerMP)
				{
					//ystem.out.println("MP player");
			    	BiblioNetworking.INSTANCE.sendTo(new BiblioAtlasClient(atlasStack), (EntityPlayerMP) player);
					// ByteBuf buffer = Unpooled.buffer();
			    	// ByteBufUtils.writeItemStack(buffer, atlasStack);
			    	// BiblioCraft.ch_BiblioAtlas.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlas"), (EntityPlayerMP) player);
				}
			}
			tags.setBoolean("containerUpdate", false);
			this.atlasStack.setTagCompound(tags);
		}
	}
	   
	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer) 
	{
		return true;
	}
	
    @Override
    public void onContainerClosed(EntityPlayer player) 
    {
    	updateStackOnPlayer(player);
    }
    
    private void updateStackOnPlayer(EntityPlayer player)
    {
    	ItemStack atlas = player.getHeldItem(EnumHand.MAIN_HAND);
    	if (atlas != ItemStack.EMPTY && atlas.getItem() instanceof ItemAtlas)
    	{
	    	NBTTagCompound tags = atlas.getTagCompound();
	    	if (tags == null)
	    	{
	    		tags = new NBTTagCompound();
	    	}
			if (tags.hasKey("savedCompass"))
			{
		    	int comp = tags.getInteger("savedCompass");
				int xcoord = tags.getInteger("compassX");
				int zcoord = tags.getInteger("compassZ");

				boolean foundCompass = false;
				for (int n=0; n<6; n++)
				{
					ItemStack compStack = (ItemStack)this.inventoryItemStacks.get(n);
					if (compStack != ItemStack.EMPTY && compStack.getItem() instanceof ItemWaypointCompass)
					{
						NBTTagCompound compassTags = compStack.getTagCompound();
						if (compassTags != null)
						{
							if (xcoord == compassTags.getInteger("XCoord") && zcoord == compassTags.getInteger("ZCoord"))
							{
								// we have a winner, no further action required for compass
								if (n != comp)
								{
									tags.setInteger("savedCompass", n);
								}
								foundCompass = true;
							}
						}
					}
				}
			
			if (!foundCompass && comp >= 0)
			{
				tags.setInteger("savedCompass", -1);
			}
		}
	    	NBTTagList itemList = new NBTTagList();
	    	for (int i = 0; i < atlasInventory.getSizeInventory(); i++) 
	    	{
	    		ItemStack stack = atlasInventory.getStackInSlot(i);
	    		if (stack != ItemStack.EMPTY)
	    		{
	    			NBTTagCompound tag = new NBTTagCompound();
	    			tag.setByte("Slot", (byte) i);
	    			stack.writeToNBT(tag);
	    			itemList.appendTag(tag);
	    		}
	    	}
	    	tags.setTag("Inventory", itemList);
	    	if (tags.hasKey("autoCenter") && tags.getBoolean("autoCenter"))
	    	{
	    		//  figure out a solution for this. If this is -1 when the GUI's swap, then the second gui doesn't get to load correct data.
	    		//tags.setInteger("mapSlot", -1);
	    	}
	    	
	    	tags.setTag("maps", getUpdatedMapTagList(tags));
	    	atlas.setTagCompound(tags);
	    	player.inventory.setInventorySlotContents(player.inventory.currentItem, atlas);
    	}
    }
    
    private NBTTagList getUpdatedMapTagList(NBTTagCompound tags)
    {
    	NBTTagList maps = null;
    	NBTTagList newMaps = new NBTTagList();
    	if (tags != null && tags.hasKey("maps"))
    	{
    		maps = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
    		newMaps = maps;
    	}
    	if (this.atlasInventory == null)
    	{
    		return newMaps;
    	}
    	for (int i = 6; i < this.atlasInventory.getSizeInventory(); i++)
    	{
    		ItemStack currentMap = this.atlasInventory.getStackInSlot(i);
    		if (currentMap != ItemStack.EMPTY)
    		{
    			String mapName = "Map_"+currentMap.getItemDamage();
	    		boolean foundMap = false;
	    		NBTTagCompound mapTag = new NBTTagCompound();
	    		
	    		if (maps != null)
	    		{
			    	for (int n = 0; n < maps.tagCount(); n++)
			    	{
			    		mapTag = maps.getCompoundTagAt(n);
			    		if (mapTag != null && mapTag.hasKey("mapName"))
			    		{
			    			if (mapTag.getString("mapName").contentEquals(mapName))
			    			{
			    				foundMap = true;
			    				break;
			    			}
			    		}
			    	}
	    		}
		    	if (!foundMap)
		    	{
		    		MapData mapdata = getMapData(currentMap);
		    		if (mapdata != null)
		    		{
		    			foundMap = true;
			    		mapTag = new NBTTagCompound();
			    		mapTag.setString("mapName", mapName);
			    		mapTag.setInteger("xCenter", mapdata.xCenter);
			    		mapTag.setInteger("zCenter", mapdata.zCenter);
			    		mapTag.setInteger("mapScale", mapdata.scale);
			    		newMaps.appendTag(mapTag);
		    		}
		    	}
    		}
    	}
    	return newMaps;
    }
    
    private MapData getMapData(ItemStack stack)
	{
    	if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemMap)
		{
			MapData mapdata =Items.FILLED_MAP.getMapData(stack, world);// ((ItemMap)(stack.getItem())).getMapData(stack, Minecraft.getMinecraft().theWorld);//ItemMap.getMapData(invStack, world);
			if (mapdata != null)
			{
				return mapdata;
			}
		}
		return null;
	}
    
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		int heldSlot = inventoryPlayer.currentItem;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 48+j*18, 159+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			if (i == heldSlot)
			{
				addSlotToContainer(new SlotLocked(inventoryPlayer, i, 48+i*18,217));
			}
			else
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 48+i*18,217));
			}
			
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = (Slot) inventorySlots.get(slot);
	//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			Item toolTest = stack.getItem();

			if (slot < 48)
			{
				if (!this.mergeItemStack(stackInSlot, 48, 84, true))  // changing 9 to 6
				{
					return ItemStack.EMPTY;
				}
			}
			else if (atlasSlot.isAtlasItemValid(stackInSlot) && !this.mergeItemStack(stackInSlot, 0, 6, false))
			{
				return ItemStack.EMPTY;
			}
			else if (stack.getCount() == 1 && atlasMapSlot.isAtlasMapItemValid(stack) && !this.mergeItemStack(stackInSlot, 6, 48, false))
			{
				return ItemStack.EMPTY;
			}

			
			if (stackInSlot.getCount() == 0)
			{
				slotObject.putStack(ItemStack.EMPTY);
			} else 
			{
				slotObject.onSlotChanged();
			}
			
			if (stackInSlot.getCount() == stack.getCount())
			{
				return ItemStack.EMPTY;
			}
			slotObject.onTake(player, stackInSlot);
		}
		return stack;
	}
	
    @Override
    public ItemStack slotClick(int slot, int dragType, ClickType modifier, EntityPlayer player)
    {
    	if (slot == -999)
    	{
    		return ItemStack.EMPTY;
    	}
    	return super.slotClick(slot, dragType, modifier, player);
    }
}
