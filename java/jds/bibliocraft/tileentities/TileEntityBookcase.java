package jds.bibliocraft.tileentities;

import java.util.Random;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.storygen.BookGenUtil;
import jds.bibliocraft.storygen.BookGenWordLists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.registries.ObjectHolder;


public class TileEntityBookcase extends BiblioTileEntity
{
	private int slotsFilled;
	private boolean hasredstonebook;  
	private int redstonebookslot;
	private int[] bookCheck = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 
	
	//public static ModelProperty<int[]> BOOKS = new ModelProperty<int[]>();
	public static ModelProperty<Boolean> BOOK1 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK2 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK3 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK4 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK5 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK6 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK7 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK8 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK9 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK10 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK11 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK12 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK13 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK14 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK15 = new ModelProperty<Boolean>();
	public static ModelProperty<Boolean> BOOK16 = new ModelProperty<Boolean>();
	
	
	@ObjectHolder("bibliocraft:bookcase")
	public static TileEntityType<TileEntityBookcase> tileType;
	
	public TileEntityBookcase() 
	{
		super(tileType, 16, true);
		//this.tileType = tileEntityTypeIn;
	}
	
	@Override
	public ModelDataMap.Builder getAdditionalModelData(ModelDataMap.Builder map)
	{
		return map.withInitial(BOOK1, bookCheck[0] == 1)
				  .withInitial(BOOK2, bookCheck[1] == 1)
				  .withInitial(BOOK3, bookCheck[2] == 1)
				  .withInitial(BOOK4, bookCheck[3] == 1)
				  .withInitial(BOOK5, bookCheck[4] == 1)
				  .withInitial(BOOK6, bookCheck[5] == 1)
				  .withInitial(BOOK7, bookCheck[6] == 1)
				  .withInitial(BOOK8, bookCheck[7] == 1)
				  .withInitial(BOOK9, bookCheck[8] == 1)
				  .withInitial(BOOK10, bookCheck[9] == 1)
				  .withInitial(BOOK11, bookCheck[10] == 1)
				  .withInitial(BOOK12, bookCheck[11] == 1)
				  .withInitial(BOOK13, bookCheck[12] == 1)
				  .withInitial(BOOK14, bookCheck[13] == 1)
				  .withInitial(BOOK15, bookCheck[14] == 1)
				  .withInitial(BOOK16, bookCheck[15] == 1);
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		boolean redtest = hasredstonebook;
		hasredstonebook = hasredstone();
		if (redtest != hasredstonebook)
		{
			updateSurroundingBlocks(BlockLoader.bookcases[0]);
			/*
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), BlockBookcase.instance);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()), BlockBookcase.instance);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()), BlockBookcase.instance);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1), BlockBookcase.instance);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1), BlockBookcase.instance);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), BlockBookcase.instance);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()), BlockBookcase.instance);
			*/
		}
		checkFilledSlots();
		checkBooks();
	}

	@Override
	public void loadCustomNBTData(CompoundNBT nbt)
	{
		this.slotsFilled = nbt.getInt("filledSlots");
		this.hasredstonebook = nbt.getBoolean("hasredstonebook");
		this.redstonebookslot = nbt.getInt("redstonebookslot");
		this.bookCheck = nbt.getIntArray("bookCheck");	
	}

	@Override
	public CompoundNBT writeCustomNBTData(CompoundNBT nbt) 
	{
		nbt.putInt("filledSlots", slotsFilled);
    	nbt.putBoolean("hasredstonebook", hasredstonebook);
    	nbt.putInt("redstonebookslot", redstonebookslot);
    	nbt.putIntArray("bookCheck", bookCheck);
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
				//if (item instanceof ItemRedstoneBook) TODO removed temp 
				//{
				//	redstonebookslot = x;
				//	return true;
				//}
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
		ItemStack newBook = new ItemStack(Items.WRITTEN_BOOK, 1);
		CompoundNBT bookTag = new CompoundNBT();
		ListNBT bookTagList = new ListNBT();
		//stasis page
		StringNBT nbtPage = new StringNBT(bookgen.getStasis(1, authorName));
		bookTagList.add(nbtPage);
		//trigger page
		nbtPage = new StringNBT(bookgen.getTrigger(1, authorName));
		bookTagList.add(nbtPage);
		//quest page
		nbtPage = new StringNBT(bookgen.getQuest(1, authorName));
		bookTagList.add(nbtPage);
		//surprise page
		nbtPage = new StringNBT(bookgen.getSurprise(1, authorName));
		bookTagList.add(nbtPage);
		//choice page
		nbtPage = new StringNBT(bookgen.getChoice(1, authorName));
		bookTagList.add(nbtPage);
		//climax page
		nbtPage = new StringNBT(bookgen.getClimax(1, authorName));
		bookTagList.add(nbtPage);
		//reversal page
		nbtPage = new StringNBT(bookgen.getReversal(1, authorName));
		bookTagList.add(nbtPage);
		//resolution
		nbtPage = new StringNBT(bookgen.getResolution(1, authorName));
		bookTagList.add(nbtPage);
		
		bookTag.put("title", new StringNBT(bookgen.getBookTitle()));
		bookTag.put("author", new StringNBT(authorName));
		bookTag.put("pages", bookTagList);
		byte resolveByte = 1;
		ByteNBT resolve = new ByteNBT(resolveByte);
		bookTag.put("resolved", resolve);
		newBook.setTag(bookTag);
		
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
/*
	@Override
	public ITextComponent getName() 
	{
		return new TextComponentString(BlockBookcase.name);
	}
    
	@Override
	public ITextComponent getDisplayName() 
	{
		//ITextComponent chat = new TextComponentString(getName());
		return getName();
	}

	@Override
	public ITextComponent getCustomName() 
	{
		return getName();
	}
	*/
	@Override
	public boolean isItemValid(int slot, ItemStack stack) 
	{
		// TODO Auto-generated method stub
		return false;
	}
}
