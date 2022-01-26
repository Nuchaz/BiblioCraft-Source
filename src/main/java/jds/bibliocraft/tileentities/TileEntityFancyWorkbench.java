package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BlockFancyWorkbench;
import jds.bibliocraft.containers.ContainerFancyWorkbench;
import jds.bibliocraft.items.ItemRecipeBook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.util.Constants;

public class TileEntityFancyWorkbench extends BiblioTileEntity 
{
	private NonNullList<ItemStack> playerGrid;
	private NonNullList<ItemStack> bookGrid;
	private ContainerFancyWorkbench[] container = new ContainerFancyWorkbench[8];
	private int[] playerIDs = new int[8];
	
	public int[] bookCheck = new int[8];
	public int angle;
	public int showText = -1;
	
	public boolean islocked = false;
	public String lockee = "";
	
	public String customTex = "none";
	public ResourceLocation customTexture = null;
	
	public TileEntityFancyWorkbench()
	{
		super(9, true);
		playerGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
		bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
	}
	
	public void setShowText(int textnum)
	{
		showText = textnum;
	}
	public int getShowText()
	{
		return showText;
	}
	
	public int[] getBookArray()
	{
		return this.bookCheck;
	}
	
	public void setPlayerGrid(NonNullList<ItemStack> grid)
	{
		this.playerGrid = grid;
	}
	
	public boolean isTooManyPlayers()
	{
		if (this.playerIDs[7] != 0 & this.container[7] != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This saves the current players crafting grid layout to the recipe book
	 */
	public void setBookGrid(int id)
	{
		int arrayid = -1;
		for (int n = 0; n<8; n++)
		{
			if (playerIDs[n] == id)
			{
				arrayid = n;
				break;
			}
		}
		
		if (arrayid == -1 || container[arrayid] == null)
		{
			return;
		}
		if (getStackInSlot(0) != ItemStack.EMPTY && (getStackInSlot(0).getItem() == Items.BOOK || getStackInSlot(0).getItem() instanceof ItemRecipeBook))
		{
			setInventorySlotContents(0, ItemStack.EMPTY);
			ItemStack recipeBook = new ItemStack(ItemRecipeBook.instance, 1, 0);
			NBTTagCompound nbt = new NBTTagCompound();
			NBTTagList itemList = new NBTTagList();
	    	for (int i = 0; i < 9; i++)
	    	{
	    		ItemStack stack = this.playerGrid.get(i);
	    		//System.out.println(i+"   this: ");
	    		//if (stack != ItemStack.EMPTY)
	    		//{
	    			//System.out.println(stack.getDisplayName());
	    			NBTTagCompound tag = new NBTTagCompound();
	    			tag.setByte("Slot", (byte) i);
	    			stack.writeToNBT(tag);
	    			itemList.appendTag(tag);
	    		//}
	    	}
	    	nbt.setTag("grid", itemList);
	    	
	    	

	    	// here is where I want to add the new name.
	    	ItemStack result = container[arrayid].craftResult.getStackInSlot(0);
	    	if (result != ItemStack.EMPTY)
	    	{
	    		//System.out.println(result.getDisplayName());
				NBTTagCompound display = new NBTTagCompound();
				display.setString("Name", TextFormatting.WHITE + I18n.translateToLocal("book.setrecipename") + " " + result.getDisplayName());
	    		nbt.setTag("display", display);
	    		
	    		NBTTagCompound resultTag = new NBTTagCompound();
	    		result.writeToNBT(resultTag);
	    		nbt.setTag("result", resultTag);
	    	}
	    	
	    	
	    	recipeBook.setTagCompound(nbt);
	    	setInventorySlotContents(0, recipeBook);
	    	for (int i = 0; i < this.playerGrid.size(); i++)
	    	{
	    		this.bookGrid.set(i, this.playerGrid.get(i));
	    	}
		}
	}
	public NonNullList<ItemStack> getPlayerGrid()
	{
		return this.playerGrid;
	}
	public NonNullList<ItemStack> getBookGrid()
	{
		//this.bookGrid[1] = new ItemStack(Blocks.log, 1, 0);
		return this.bookGrid;
	}
	public void setContainer(ContainerFancyWorkbench bench, int id)
	{
		for (int n = 0; n < 8; n++)
		{
			if (this.container[n] == null)
			{
				this.container[n] = bench;
				this.playerIDs[n] = id;
				break;
			}
		}
	}
	
	public void clearContainer(int id)
	{
		for (int n = 0; n < 8; n++)
		{
			if (playerIDs[n] == id)
			{
				this.playerIDs[n] = 0;
				this.container[n] = null;
			}
		}
	}
	
	public void loadInvToGridForRecipe(int playerid)
	{
		if (this.container != null) 
		{
			if (this.getStackInSlot(0) != null)
			{
				if (this.getStackInSlot(0).getItem() instanceof ItemRecipeBook)
				{
					for (int n = 0; n < 8; n++)
					{
						if (playerIDs[n] == playerid && this.container[n] != null)
						{
							readBookMatrix(getStackInSlot(0));
							this.container[n].loadPlayerInventorytoRecipeBookGrid(this.bookGrid, playerid);
						}
					}
					
				}
			}
			//System.out.println("being call upon");
			
		}
	}
	
	/**
	 * This loads the current recipe / crafting matrix into the book craft matrix from a valid recipe book
	 * @param stack
	 */
	private void readBookMatrix(ItemStack stack)
	{
		
		if (stack.getItem() instanceof ItemRecipeBook)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null)
			{
				NBTTagList tagList = nbt.getTagList("grid", Constants.NBT.TAG_COMPOUND);
				//this.inventory = new ItemStack[this.getSizeInventory()];
				this.bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
				for (int i = 0; i < 9; i++)
				{
					NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
					byte slot = tag.getByte("Slot");
					//System.out.println(slot);
					if (slot >= 0 && slot < 9)
					{
						ItemStack nbtStack = new ItemStack(tag);
						if (nbtStack != ItemStack.EMPTY)
						{
							this.bookGrid.set(slot, nbtStack);//[slot] = nbtStack;
						}
					}
				}
			}
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		// for use with pipes and hoppers and such
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockFancyWorkbench.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		if (slot > 0 && slot < 10)
		{
			if (getStackInSlot(slot) != ItemStack.EMPTY && stack.getItem() != Items.AIR)
			{
				bookCheck[slot-1] = 1;
			}
			else
			{
				bookCheck[slot-1] = 0;
			}
		}
		
		if (this.container != null) 
		{
			if (slot == 0)
			{
				if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemRecipeBook)
				{
					readBookMatrix(stack);
				}
				else
				{
					for (int n = 0; n<9; n++)
					{
						this.bookGrid.set(n, ItemStack.EMPTY);//[n] = null;
					}
				}
			}
		}
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.bookCheck = nbt.getIntArray("books");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		nbt.setIntArray("books", bookCheck);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
