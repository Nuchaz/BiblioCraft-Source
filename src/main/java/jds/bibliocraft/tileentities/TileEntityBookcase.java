package jds.bibliocraft.tileentities;

import java.util.Random;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.items.ItemRedstoneBook;
import jds.bibliocraft.storygen.BookGenUtil;
import jds.bibliocraft.storygen.BookGenWordLists;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;


public class TileEntityBookcase extends BiblioTileEntity
{
	private int slotsFilled;
	private boolean hasredstonebook;  
	private int redstonebookslot;
	private int[] bookCheck = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 
	
	public TileEntityBookcase() 
	{
		super(16, true);
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		boolean redtest = hasredstonebook;
		hasredstonebook = hasredstone();
		if (redtest != hasredstonebook)
		{
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), BlockBookcase.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()), BlockBookcase.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()), BlockBookcase.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1), BlockBookcase.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1), BlockBookcase.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), BlockBookcase.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()), BlockBookcase.instance, true);
		}
		checkFilledSlots();
		checkBooks();
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt)
	{
		this.slotsFilled = nbt.getInteger("filledSlots");
		this.hasredstonebook = nbt.getBoolean("hasredstonebook");
		this.redstonebookslot = nbt.getInteger("redstonebookslot");
		this.bookCheck = nbt.getIntArray("bookCheck");	
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		nbt.setInteger("filledSlots", slotsFilled);
    	nbt.setBoolean("hasredstonebook", hasredstonebook);
    	nbt.setInteger("redstonebookslot", redstonebookslot);
    	nbt.setIntArray("bookCheck", bookCheck);
		return nbt;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public boolean hasredstone()
	{
		for(int x=0; x < 16; x++)
		{
			ItemStack stack = inventory.get(x);
			if (stack != ItemStack.EMPTY)
			{
				Item item = stack.getItem();
				if (item instanceof ItemRedstoneBook)
				{
					redstonebookslot = x;
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void checkBooks()
	{
		if (bookCheck.length == 0)
		{
			bookCheck = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		}
		for(int x=0; x < bookCheck.length; x++)
		{
			ItemStack stack = inventory.get(x);
			if (stack != ItemStack.EMPTY && stack.getItem() != Items.AIR)
			{
				bookCheck[x] = 1;
			}
			else
			{
				bookCheck[x] = 0;
			}
		}
	}
	
	public int[] getCheckedBooks()
	{
		return bookCheck;
	}
	
	public boolean getredstone()
	{
		return hasredstonebook;
	}
	public int getRedstoneBookSlot()
	{
		return redstonebookslot;
	}
	
	public boolean setBook(int bookNumber, ItemStack bookStack)
	{
		if (bookStack != ItemStack.EMPTY && getStackInSlot(bookNumber) == ItemStack.EMPTY)
		{
			// adding the book
			setInventorySlotContents(bookNumber, bookStack);
			return true;
		}
		if (bookStack == ItemStack.EMPTY && getStackInSlot(bookNumber) != ItemStack.EMPTY)
		{
			// deleteing a book
			setInventorySlotContents(bookNumber, ItemStack.EMPTY);
		}
		return false;
	}

	public void checkFilledSlots()
	{
		slotsFilled = 0;
		for (int s = 0; s < 16; s++)
		{
			ItemStack stackTest = getStackInSlot(s);
			if (stackTest != ItemStack.EMPTY)
			{
				slotsFilled = slotsFilled + 1;
			}
		}
	}
	
	public boolean checkSlot(int slot)
    {
    	if (getStackInSlot(slot) != ItemStack.EMPTY)
    	{
    		return true;
    	}
    	return false;
    }
    
    public void addRandomBooksToShelf()
    {
    	Random rando = new Random();
    	for (int n = 0; n < 16; n++)
    	{
    		if (rando.nextInt(10) < 8)
    		{
    			
    			this.setInventorySlotContents(n, writeCustomBook());
    		}
    	}
    }
    
    public ItemStack writeCustomBook()
    {
    	BookGenWordLists bookgenwordlist = new BookGenWordLists();
    	String authorName = bookgenwordlist.getRandomName();
    	BookGenUtil bookgen = new BookGenUtil(1, authorName);
		ItemStack newBook = new ItemStack(Items.WRITTEN_BOOK, 1, 0);
		NBTTagCompound bookTag = new NBTTagCompound();
		NBTTagList bookTagList = new NBTTagList();
		//stasis page
		NBTTagString nbtPage = new NBTTagString(bookgen.getStasis(1, authorName));
		bookTagList.appendTag(nbtPage);
		//trigger page
		nbtPage = new NBTTagString(bookgen.getTrigger(1, authorName));
		bookTagList.appendTag(nbtPage);
		//quest page
		nbtPage = new NBTTagString(bookgen.getQuest(1, authorName));
		bookTagList.appendTag(nbtPage);
		//surprise page
		nbtPage = new NBTTagString(bookgen.getSurprise(1, authorName));
		bookTagList.appendTag(nbtPage);
		//choice page
		nbtPage = new NBTTagString(bookgen.getChoice(1, authorName));
		bookTagList.appendTag(nbtPage);
		//climax page
		nbtPage = new NBTTagString(bookgen.getClimax(1, authorName));
		bookTagList.appendTag(nbtPage);
		//reversal page
		nbtPage = new NBTTagString(bookgen.getReversal(1, authorName));
		bookTagList.appendTag(nbtPage);
		//resolution
		nbtPage = new NBTTagString(bookgen.getResolution(1, authorName));
		bookTagList.appendTag(nbtPage);
		
		bookTag.setTag("title", new NBTTagString(bookgen.getBookTitle()));
		bookTag.setTag("author", new NBTTagString(authorName));
		bookTag.setTag("pages", bookTagList);
		byte resolveByte = 1;
		NBTTagByte resolve = new NBTTagByte(resolveByte);
		bookTag.setTag("resolved", resolve);
		newBook.setTagCompound(bookTag);
		return newBook;
    }
    
    public int getFilledSlots()
    {
    	return slotsFilled;
    }

    public int getTopEmptySlot()
    {
    	for (int x = 0; x < 8; x++)
    	{
    		if (inventory.get(x) == ItemStack.EMPTY)
    		{
    			return x;
    		}
    	}
    	
    	return -1;
    }
    public int getBottomEmptySlot()
    {
    	for (int x = 8; x < 16; x++)
    	{
    		if (inventory.get(x) == ItemStack.EMPTY)
    		{
    			return x;
    		}
    	}
    	
    	return -1;
    }
    
    public int getTopFullSlot()
    {
    	for (int x = 7; x >= 0; x=x-1)
    	{
    		if (inventory.get(x) != ItemStack.EMPTY)
    		{
    			return x;
    		}
    	}
    	
    	return -1;
    }
    
    public int getBottomFullSlot()
    {
    	for (int x = 15; x >= 8; x=x-1)
    	{
    		if (inventory.get(x) != ItemStack.EMPTY)
    		{
    			return x;
    		}
    	}
    	
    	return -1;
    }
    
    public ItemStack getTopBook()
    {
    	int slot = getTopFullSlot();
    	if (slot != -1)
    	{
    		ItemStack topBook = inventory.get(slot);
    		//setInventorySlotContents(slot, null);
    		return topBook;
    	}
    	else
    	{
    		return ItemStack.EMPTY;
    	}
    }
    
    public ItemStack getBottomBook()
    {
    	int slot = getBottomFullSlot();
    	if (slot != -1)
    	{
    		ItemStack bottomBook = inventory.get(slot);
    		//setInventorySlotContents(slot, null);
    		return bottomBook;
    	}
    	else
    	{
    		return ItemStack.EMPTY;
    	}
    }
    
    public boolean removeTopBook()
    {
    	int slot = getTopFullSlot();
    	if (slot != -1)
    	{
    		setInventorySlotContents(slot, ItemStack.EMPTY);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public boolean removeBottomBook()
    {
    	int slot = getBottomFullSlot();
    	if (slot != -1)
    	{
    		setInventorySlotContents(slot, ItemStack.EMPTY);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public boolean addTopBook(ItemStack stack)
    {
    	//boolean hasStack = false;
    	if (getTopEmptySlot() != -1)
    	{
    		setInventorySlotContents(getTopEmptySlot(), stack);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public boolean addBottomBook(ItemStack stack)
    {
    	if (getBottomEmptySlot() != -1)
    	{
    		setInventorySlotContents(getBottomEmptySlot(), stack);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    @Override 
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			if (!Config.isBlock(itemstack) && Config.testBookValidity(itemstack))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockBookcase.name;
	}
    
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
