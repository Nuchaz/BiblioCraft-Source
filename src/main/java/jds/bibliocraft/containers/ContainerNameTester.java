package jds.bibliocraft.containers;

import jds.bibliocraft.items.ItemNameTester;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioAtlasClient;
import jds.bibliocraft.slots.SlotTesterSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ContainerNameTester extends Container
{
	
	private InventoryBasic inventory;
	public ItemStack testerStack;
	private SlotTesterSlot slot;
	private EntityPlayer playerPointer;
	private World world;
	public boolean updated = false;
	public ItemStack currentStack;
	
	public boolean updatedSlots = false;
	private boolean initalLoad = false;
	
	public ContainerNameTester(InventoryPlayer inventoryPlayer)
	{
		inventory = new InventoryBasic("TestInventory", false, 1);
		testerStack = inventoryPlayer.getCurrentItem();
		if (testerStack != ItemStack.EMPTY && testerStack.getItem() instanceof ItemNameTester)
		{
			NBTTagCompound tags = testerStack.getTagCompound();
			if (tags != null)
			{
				NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < tagList.tagCount(); i++)
				{
					
					NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
					byte slot = tag.getByte("Slot");
					if (slot >= 0 && slot < inventory.getSizeInventory())
					{
						ItemStack invStack = new ItemStack(tag);
						this.inventory.setInventorySlotContents(slot, invStack);
					}
				}
			}
		}
		
		addSlotToContainer(this.slot = new SlotTesterSlot(this, inventory, 0, 80, -3));
		bindPlayerInventory(inventoryPlayer);
		this.initalLoad = true;
		this.playerPointer = inventoryPlayer.player;
		this.world = inventoryPlayer.player.world;
	}
	
	
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 96+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18,154));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return true;
	}
	
    @Override
    public void onContainerClosed(EntityPlayer player) 
    {
    	// Save here
    	updateStackOnPlayer(player, false);
    }
    
    private void updateStackOnPlayer(EntityPlayer player, boolean guiUpdate)
    {
    	ItemStack newStack = player.getHeldItem(EnumHand.MAIN_HAND);
    	if (newStack != ItemStack.EMPTY && newStack.getItem() instanceof ItemNameTester)
    	{
    		NBTTagCompound tags = newStack.getTagCompound();
         	if (tags == null)
         	{
         		tags = new NBTTagCompound();
         	}
        	NBTTagList itemList = new NBTTagList();
        	for (int i = 0; i < inventory.getSizeInventory(); i++)
        	{
        		ItemStack stack = inventory.getStackInSlot(i);
        		if (stack != ItemStack.EMPTY)
        		{
        			NBTTagCompound tag = new NBTTagCompound();
        			tag.setByte("Slot", (byte) i);
        			stack.writeToNBT(tag);
        			itemList.appendTag(tag);
        		}
        	}
        	tags.setTag("Inventory", itemList);
        	tags.setBoolean("containerUpdate", guiUpdate);
        	newStack.setTagCompound(tags);
        	this.testerStack = newStack;
    	}

    	//player.inventory.setItemStack(atlasStack);
    	//System.out.println("saving container");
    	player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack); 
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


			if (slot < 1)
			{
				if (!this.mergeItemStack(stackInSlot, 1, 37, true))  
				{
					return ItemStack.EMPTY;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory

			else if (!this.mergeItemStack(stackInSlot, 0, 1, false))
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
    public void detectAndSendChanges()
    {
    	//System.out.println("testy");
    	
    	/*
    	this.currentStack = this.inventory.getStackInSlot(0);
    	if (this.currentStack != null)
    	{
    		System.out.println(this.currentStack.getUnlocalizedName());
    	}
    	*/
    	
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
                      if (i == 0)
                      {
                      	this.updatedSlots = true;
                      }
                  }
              }
          }
          
  		if (this.updatedSlots && this.initalLoad)
  		{
  			// time to update data in the NBT about the compass and maps locations and whatnots
  			updateStackOnPlayer(this.playerPointer, true);
			if (!(world.isRemote))
			{
				if (this.playerPointer instanceof EntityPlayerMP)
				{
					//ystem.out.println("MP player");
			    	// ByteBuf buffer = Unpooled.buffer();
			    	// ByteBufUtils.writeItemStack(buffer, this.testerStack);
			    	BiblioNetworking.INSTANCE.sendTo(new BiblioAtlasClient(this.testerStack), (EntityPlayerMP) this.playerPointer);
					// BiblioCraft.ch_BiblioAtlas.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlas"), (EntityPlayerMP) this.playerPointer);
			    	//System.out.println("sent the update");
				}
			}
  			this.updatedSlots = false;
  		}
    }
    
    //@SideOnly(Side.CLIENT)
    //public void updateProgressBar(int p_75137_1_, int p_75137_2_) {}
}
