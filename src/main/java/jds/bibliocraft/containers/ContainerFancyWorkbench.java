package jds.bibliocraft.containers;

import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.slots.SlotFancyWorkbench;
import jds.bibliocraft.slots.SlotWorkbenchBook;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import jds.bibliocraft.tileentities.TileEntityFancyWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ContainerFancyWorkbench extends Container
{
	
    public InventoryCrafting playerCraftMatrix = new InventoryCrafting(this, 3, 3);
   // public InventoryCrafting bookCraftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    private World world;
    private int posX;
    private int posY;
    private int posZ;
    private TileEntityFancyWorkbench tileEntity;
    protected SlotFancyWorkbench slot;
    private InventoryPlayer playerInventory;
	private int[] ingredientCounts = new int[9];
	private String[] ingredientNames = new String[9];
	private NonNullList<ItemStack> newlyAddedMatrix = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);//new ItemStack[9];
	private int playerID = 0;
	
	private int numExtraBookcases = 0;
    
    public ContainerFancyWorkbench(InventoryPlayer inventoryPlayer, World worldObj, TileEntityFancyWorkbench tile, int playerid, TileEntityBookcase leftBookcase, TileEntityBookcase rightBookcase)
    {
    	world = worldObj;
    	tileEntity = tile;
    	posX = tile.getPos().getX();
    	posY = tile.getPos().getY();
    	posZ = tile.getPos().getZ();
    	tileEntity.setContainer(this, playerid); // I bet anything, this is where the problem is at with multipleyer
    	playerInventory = inventoryPlayer;
    	playerID = playerid;

    	this.addSlotToContainer(this.slot = new SlotFancyWorkbench(this, tileEntity, 0, 8+60, 35, true));  // recipe book slot
		for (int i = 0; i < 8; i++) 
		{
			this.addSlotToContainer(this.slot = new SlotFancyWorkbench(this, tileEntity, i+1, 17+60+i*18, 83, false));
		}
    	this.addSlotToContainer(new SlotCrafting(inventoryPlayer.player, this.playerCraftMatrix, this.craftResult, 10, 124+60, 35));
        for (int n = 0; n < 3; ++n)
        {
            for (int m = 0; m < 3; ++m)
            {
                this.addSlotToContainer(new Slot(this.playerCraftMatrix, m + n * 3, 30+60 + m * 18, 17 + n * 18));
            }
        }
		this.onCraftMatrixChanged(this.playerCraftMatrix);
		//this.onCraftMatrixChanged(this.bookCraftMatrix);
		
		if (leftBookcase != null)
		{
			for (int m = 0; m < 2; m++)
			{
				for (int n = 0; n < 8; n++)
				{
					this.addSlotToContainer(new SlotWorkbenchBook(this, leftBookcase, n+(m*8), -48+60+(m*21), 29+(n*18)));  //-27  -48
				}
			}
			numExtraBookcases++;
		}
		if (rightBookcase != null)
		{
			for (int m = 0; m < 2; m++)
			{
				for (int n = 0; n < 8; n++)
				{
					this.addSlotToContainer(new SlotWorkbenchBook(this, rightBookcase, n+(m*8), 187+60+(m*21), 29+(n*18)));  //-27  -48
				}
			}
			numExtraBookcases++;
		}
		
    	bindPlayerInventory(inventoryPlayer);
    }
    
    
    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
		this.craftResult.setInventorySlotContents(0, CraftingManager.findMatchingResult(this.playerCraftMatrix, this.world));
		NonNullList<ItemStack> grid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);//new ItemStack[9];
		for (int n=0; n<9; n++)
		{
			grid.set(n, this.playerCraftMatrix.getStackInSlot(n));
		}
		tileEntity.setPlayerGrid(grid);
    }
    
    /**
     * Looks at the players inventory and adds matching ItemStacks to the crafting grid intended for the recipe book
     * @param grid
     */
    public void loadPlayerInventorytoRecipeBookGrid(NonNullList<ItemStack> grid, int id)
    {
    	if (playerInventory.player.getEntityId() != id)
    	{
    		return;
    	}
    	ItemStack slotStack = ItemStack.EMPTY;
    	ItemStack invStack = ItemStack.EMPTY;
    	ItemStack matrixStack = ItemStack.EMPTY;
    	int[] matrixSizes = new int[9];
    	NonNullList<ItemStack> newMatrix = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
    	int stackcount;
    	int newStackCount = 0;
    	int totalStackCount;
    	int maxStackSize = 64;
    	compareingredients(grid);
    	
    	for (int n = 0; n < 9; n++)
    	{
    		slotStack = grid.get(n);
    		if (slotStack != ItemStack.EMPTY)
    		{
    			maxStackSize = slotStack.getMaxStackSize();
    			stackcount = 0;
    			for (int m = 0; m < playerInventory.mainInventory.size(); m++)
    			{
    				
    				invStack = playerInventory.mainInventory.get(m);	
    				if (invStack != ItemStack.EMPTY)
					{
    					if (slotStack.getUnlocalizedName().matches(invStack.getUnlocalizedName()))
    					{
    						stackcount += invStack.getCount();
    						slotStack = invStack.copy();
    					}
					}
    			}
    			for (int m = 0; m<9; m++)
    			{
    				if (slotStack.getUnlocalizedName().matches(this.ingredientNames[m]))
					{
    					totalStackCount = stackcount;
    					stackcount = stackcount / this.ingredientCounts[m];
    					if (stackcount > 0)
    					{
    						if (stackcount > maxStackSize)
    						{
    							stackcount = maxStackSize;
    						}
    						matrixStack = this.playerCraftMatrix.getStackInSlot(n);
    						
    						newStackCount = stackcount;
    						if (matrixStack != ItemStack.EMPTY)
    						{
    							matrixSizes[n] = matrixStack.getCount();
    							if (matrixStack.getUnlocalizedName().matches(slotStack.getUnlocalizedName()))
    							{
    								if (matrixSizes[n] + stackcount >= maxStackSize)
    								{
    									stackcount = maxStackSize;
    									newStackCount = maxStackSize - matrixSizes[n];
    								}
    								else
    								{
    									stackcount += matrixSizes[n];
    								}
    							}
    							else
    							{
    								stackcount = 0;
    								matrixSizes[n] = 0;
    							}
    						}
    						if (stackcount != 0)
    						{
    							slotStack.setCount(stackcount);
    							this.playerCraftMatrix.setInventorySlotContents(n, slotStack);
    							ItemStack stackcopy = slotStack.copy();
    							stackcopy.setCount(newStackCount);
    							newMatrix.set(n, stackcopy);// = stackcopy;
    						}
    					}
    					break;
					}
    			}
    		}
    	}
    	
    	slotStack = ItemStack.EMPTY;
    	invStack = ItemStack.EMPTY;
    	for (int n = 0; n<9; n++)
    	{
    		slotStack = newMatrix.get(n);
    		if (slotStack != ItemStack.EMPTY)
    		{
    			stackcount = slotStack.getCount();
    			if (stackcount > 0)
    			{
		    		for (int m = 0; m < this.playerInventory.mainInventory.size(); m++)
		    		{
		    			invStack = this.playerInventory.mainInventory.get(m);
		    			if (invStack != ItemStack.EMPTY)
		    			{
		    				if (invStack.getUnlocalizedName().matches(slotStack.getUnlocalizedName()))
		    				{
		    					// so I have a matching item in the player inventory as I added to my player crafting matrix.
		    					if (invStack.getCount() > stackcount)
		    					{
		    						this.playerInventory.mainInventory.get(m).setCount(this.playerInventory.mainInventory.get(m).getCount() - stackcount);// -= stackcount;
		    						stackcount = 0;
		    						break;
		    					}
		    					else if (invStack.getCount() == stackcount)
		    					{
		    						this.playerInventory.mainInventory.set(m, ItemStack.EMPTY);
		    						stackcount = 0;
		    						break;
		    					}
		    					else // if invStack.getCount() < stackcount
		    					{
		    						stackcount -= this.playerInventory.mainInventory.get(m).getCount();
		    						this.playerInventory.mainInventory.set(m, ItemStack.EMPTY);
		    					}
		    					
		    				}
		    			}
		    		}
    			}
    		}
    	}
    	
    }
    
    public void compareingredients(NonNullList<ItemStack> stacks)
	{
		ingredientCounts = new int[9];
		ingredientNames = new String[9];
		for (int i = 0; i < 9; i++)
		{
			ItemStack nbtStack = stacks.get(i);//[i];
			if (nbtStack != ItemStack.EMPTY && nbtStack != null)
			{
				int n = 0;
				boolean complete = false;
				boolean havematch = false;
				for (int m = 0; m < this.ingredientNames.length; m++)
				{
					if (this.ingredientNames[m] != null)
					{
						if (this.ingredientNames[m].matches(nbtStack.getUnlocalizedName()))
						{
							n = m;
							havematch = true;
						}
					}
				}
				
				if (havematch)
				{
					this.ingredientCounts[n] += 1;
				}
				else
				{
					while (!complete)
					{
							if (this.ingredientCounts[n] == 0)
							{
								this.ingredientCounts[n] += 1;
								this.ingredientNames[n] = nbtStack.getUnlocalizedName();
								complete = true;
							}
							else
							{
								if (n < 8)
								{
									n++;
								}
							}
					}
				}
			}	
		}
	}
    
    // this will test the players crafting slots and return true if there is any items in the crafting grid
    private boolean testPlayerCraftSlots()
    {
    	for (int n = 0; n < 9; n++)
    	{
    		if (this.playerCraftMatrix.getStackInSlot(n) != ItemStack.EMPTY)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.world.isRemote)
        {
            for (int i = 0; i < 9; ++i)
            {
                ItemStack itemstack = this.playerCraftMatrix.getStackInSlot(i);
                if (itemstack != ItemStack.EMPTY)
                {
                    par1EntityPlayer.dropItem(itemstack, false);
                }
            }
        }
        tileEntity.setPlayerGrid(NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY));
        tileEntity.clearContainer(playerID);
    }
    
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUsableByPlayer(player);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+60+j*18, 110+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+60+i*18,168));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		//System.out.println("teszer "+slot);
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		if (slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			Item toolTest = stack.getItem();


			if (slot == 0) // slot 0 is shift clickable
			{
				// recipe book slot.
				boolean bookcasefull = true;
				for (int n = 1; n<9; n++)
				{
					Slot slotObjecttest = (Slot) inventorySlots.get(n);
					if (!slotObjecttest.getHasStack())
					{
						bookcasefull = false;
						break;
					}
				}
				if (bookcasefull)
				{
					if (toolTest instanceof ItemBook)
					{
						if (!this.mergeItemStack(stackInSlot, 19+(this.numExtraBookcases*16), 55+(this.numExtraBookcases*16), false)) // 51, 87
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 19, 55+(this.numExtraBookcases*16), false)) 
						{
							return ItemStack.EMPTY;
						}
					}
				}
				else
				{
					if (!this.mergeItemStack(stackInSlot, 1, 9, false)) 
					{
						return ItemStack.EMPTY;
					}
				}


					
			}
			else if (slot > 8 && slot < 19 ) // craft grid and craft result is shift clickable
			{
				// craft result || craft grid
				if (!this.mergeItemStack(stackInSlot, 19+(this.numExtraBookcases*16), 55+(this.numExtraBookcases*16), false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (slot > 0 && slot < 9) // book slots are done
			{
				Slot slotObjecttest = (Slot) inventorySlots.get(0);
				if (slotObjecttest.getHasStack())
				{
					if (toolTest instanceof ItemBook)
					{
						if (!this.mergeItemStack(stackInSlot, 19+(this.numExtraBookcases*16), 55+(this.numExtraBookcases*16), false)) 
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 19, 55+(this.numExtraBookcases*16), false)) 
						{
							return ItemStack.EMPTY;
						}
					}
				}
				else
				{
					if (stackInSlot.getCount() > 1)
					{
						if (stackInSlot.getItem() instanceof ItemBook)
						{
							ItemStack stackCopy = stackInSlot.copy();
							stackCopy.setCount(1);
							stackInSlot.setCount(stackInSlot.getCount() - 1);
							if (!this.mergeItemStack(stackCopy, 0, 1, false) && !this.mergeItemStack(stackInSlot, 1, 9, false));
							{
								return ItemStack.EMPTY;
							}
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 0, 1, false))
						{
							return ItemStack.EMPTY;
						}
					}
				}
				// book slots
			}
			else if (slot > 18 && slot < 55+(this.numExtraBookcases*16)) // player inventory behavior is all that is left.
			{
				if (stackInSlot.getItem() instanceof ItemBook || stackInSlot.getItem() instanceof ItemRecipeBook)
				{
					if (stackInSlot.getItem() instanceof ItemBook)
					{
						if (!this.mergeItemStack(stackInSlot, 1, 9, false))
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 0, 9, false))
						{
							return ItemStack.EMPTY;
						}
					}
				}
				else
				{
					if (slot >= 46+(this.numExtraBookcases*16)) // hotbar
					{
						if (!this.mergeItemStack(stackInSlot, 19+(this.numExtraBookcases*16), 46+(this.numExtraBookcases*16), false))
						{
							return ItemStack.EMPTY;
						}
					}
					else
					{
						if (!this.mergeItemStack(stackInSlot, 46+(this.numExtraBookcases*16), 55+(this.numExtraBookcases*16), false))
						{
							return ItemStack.EMPTY;
						}
					}
				}
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
}
