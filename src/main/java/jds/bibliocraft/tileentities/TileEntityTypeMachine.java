package jds.bibliocraft.tileentities;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockTypesettingTable;
import jds.bibliocraft.helpers.FileUtil;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemAtlasPlate;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemChase;
import jds.bibliocraft.items.ItemEnchantedPlate;
import jds.bibliocraft.items.ItemPlate;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemStockroomCatalog;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityTypeMachine extends BiblioTileEntity implements ITickable
{
	public String nameofbook = I18n.translateToLocal("typesetting.bookSelect"); 
	public boolean bookIsSaved;
	public String listofbooks = "";
	public String listofAuthors = "";
	public String listofPublicBooks = "";
	public boolean showBookname = false;
	public boolean showChaseText = false;
	public int requiredlevels = 0;
	public boolean showLevels = false;
	
	// these values are for live redstone data only, no nbt storage needed
	private int redstone = 0;
	private int counter = 0;
	
	public TileEntityTypeMachine()
	{
		super(3, false);
	}
	
	public int getLevels()
	{
		return requiredlevels;
	}
	
	@Override // Copied in from BiblioTileEntity becasue a crash came up claiming this was abstract.
	public ItemStack getStackInSlot(int slot)
	{
		ItemStack output = ItemStack.EMPTY;
		if (slot >= 0 && slot < this.inventory.size())
		{
			output = inventory.get(slot);
		}
		return output;
	}
	
	public void booklistset()
	{
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer())//MinecraftServer.getServer().isDedicatedServer()) 
		{
			FileUtil util = new FileUtil();
			String[] blist = util.scanBookDir(false);
			String[] alist = util.getAuthorList(blist, false);
			boolean[] plist = util.getPublistList(blist, false);
			String bookstring = "";
			String authorString = "";
			String publicsString = "";
		 	for (int g=0; g < blist.length; g++)
		 	{
		 		if (g != blist.length - 1)
		 		{
		     		bookstring = bookstring+blist[g]+"&_";
		     		authorString = authorString+alist[g]+"&_";
		     		if (plist[g])
		     		{
		     			publicsString=publicsString+"true&_";
		     		}
		     		else
		     		{
		     			publicsString=publicsString+"false&_";
		     		}
		 		}
		 		else
		 		{
		     		bookstring = bookstring+blist[g];
		     		authorString = authorString+alist[g];
		     		if (plist[g])
		     		{
		     			publicsString=publicsString+"true";
		     		}
		     		else
		     		{
		     			publicsString=publicsString+"false";
	         		}
	     		}
	     	}
			setBookList(bookstring, authorString, publicsString);
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	

	
	
    
	public void writePlateNBT(ItemStack stack, String bookName)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("bookName", bookName);
		stack.setTagCompound(tag);
	}
	
	public boolean signedBookCheck()
	{
		ItemStack stack = getStackInSlot(0);
		if (stack != ItemStack.EMPTY)
		{
			Item booktest = stack.getItem();
			if (booktest instanceof ItemWrittenBook || booktest instanceof ItemBigBook || booktest instanceof ItemRecipeBook || booktest instanceof ItemAtlas || booktest instanceof ItemStockroomCatalog)
			{
				return true;
			}
		}
			return false;
	}
	
	public boolean enchantedBookCheck()
	{
		ItemStack stack = getStackInSlot(0);
		if (stack != ItemStack.EMPTY)
		{
			Item booktest = stack.getItem();
			if (booktest instanceof ItemEnchantedBook)
			{
				return true;
			}
		}
			return false;
	}
	
	public boolean atlasBookCheck()
	{
		ItemStack stack = getStackInSlot(0);
		if (stack != ItemStack.EMPTY)
		{
			Item booktest = stack.getItem();
			if (booktest instanceof ItemAtlas)
			{
				return true;
			}
		}
			return false;
	}
	
	public boolean chaseCheck()
	{
		ItemStack stack = getStackInSlot(1);
		if (stack != ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int getChaseNum()
	{
		int stacksize = 0;
		if (chaseCheck())
		{
			ItemStack stack = getStackInSlot(1);
			int sizetest = stack.getCount();
			if (sizetest > 0 && sizetest < 17)
			{
				stacksize = 1;
			}
			if (sizetest > 16 && sizetest < 33)
			{
				stacksize = 2;
			}
			if (sizetest > 32 && sizetest < 49)
			{
				stacksize = 3;
			}
			if (sizetest > 48)
			{
				stacksize = 4;
			}
		}
		return stacksize;
	}
	
	public int addChase(ItemStack chaseStack)
	{
		ItemStack stack = getStackInSlot(1);
		if (stack != ItemStack.EMPTY)
		{
			int chaseSize = chaseStack.getCount();
			int sizetest = stack.getCount();
			int totaltest = sizetest + chaseSize;
			if (totaltest < 65)
			{
				stack.setCount(totaltest);
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				return 0;
			}
			else
			{
				stack.setCount(64);
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				return (totaltest - 64);
			}
		}
		else
		{
			setInventorySlotContents(1, chaseStack);
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			return 0;
		}
	}
	/**
	 * This method adds a book or a plate to the lower left hand slot on the typesetting table
	 * @param playerstack
	 * @param world
	 * @return
	 */
	public boolean addBookorPlate(ItemStack playerstack, World world)
	{
		ItemStack bookstack = getStackInSlot(0);
		if (bookstack == ItemStack.EMPTY)
		{
			//ItemStack stack  = getStackInSlot(0);
			if (playerstack != ItemStack.EMPTY)
			{
				Item itemtest = playerstack.getItem();
				boolean testForCustomBooks = false;
				if (itemtest instanceof ItemBigBook || itemtest instanceof ItemRecipeBook)
				{
					NBTTagCompound nbt = playerstack.getTagCompound();
					if (nbt != null)
					{
						if (nbt.getBoolean("signed"))
						{
							//System.out.println("signed book");
							testForCustomBooks = true;
						}
					}
				}
				
				if (itemtest instanceof ItemStockroomCatalog)
				{
					testForCustomBooks = true;
				}
				
				if (itemtest instanceof ItemPlate || itemtest instanceof ItemWrittenBook || itemtest instanceof ItemEnchantedBook || testForCustomBooks) 
				{
					FileUtil util = new FileUtil();
					
					if (util.isBookSaved(playerstack, world))
					{
						bookIsSaved = true;
					}
					else
					{
						bookIsSaved = false;
					}
					
					if (itemtest instanceof ItemEnchantedBook)
					{
						
						NBTTagList enchbookTagList = getEnchantmentTagList(playerstack);
						
						//System.out.println(enchbookTagList.tagCount());   // I think this is my number of enchantments
						int enchCount = enchbookTagList.tagCount();
						float slidingScale = 1.0F;
						float levelCalc = 0.0F;
						//int levelcost = 0;
						for (int x=0; x < enchbookTagList.tagCount(); x++)
						{
							int enchlvl = ((NBTTagCompound)enchbookTagList.getCompoundTagAt(x)).getShort("lvl");
							int enchid = ((NBTTagCompound)enchbookTagList.getCompoundTagAt(x)).getShort("id");
							int maxlvl = 1;
							if (Enchantment.getEnchantmentByID(enchid) != null)
							{
								maxlvl = Enchantment.getEnchantmentByID(enchid).getMaxLevel(); 
							}
							switch (maxlvl)
							{
							case 1:{enchlvl = enchlvl + 4; break;}
							case 2:{enchlvl = enchlvl + 3; break;}
							case 3:{enchlvl = enchlvl + 2; break;}
							case 4:{enchlvl = enchlvl + 1; break;}
							}
							if (x > 0)
							{
								slidingScale = (0.5F)*(1.0F/x);
							}
							levelCalc = levelCalc + ((enchlvl*enchlvl) + 15.0F)*slidingScale;
						}
						
						levelCalc = levelCalc*(Config.enchantmentMultiplyer/10.0F);
						int levelcost = (int)levelCalc;
						requiredlevels = levelcost;
					}
					setInventorySlotContents(0, playerstack);
					
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
				
				if (itemtest instanceof ItemAtlas)
				{
					// so I need to scan the book and cound the number of filled maps first
					NBTTagCompound tags = playerstack.getTagCompound();
					if (tags != null)
					{
						InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", false, 48);
						NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
						for (int i = 0; i < tagList.tagCount(); i++)
						{
							NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
							byte slot = tag.getByte("Slot");
							if (slot >= 0 && slot < atlasInventory.getSizeInventory())
							{
								ItemStack invStack = new ItemStack(tag);
								atlasInventory.setInventorySlotContents(slot, invStack);
							}
						}
						int mapCount = 0;
						for (int i = 6; i < atlasInventory.getSizeInventory(); i++)
						{
							ItemStack mapTest = atlasInventory.getStackInSlot(i);
							if (mapTest != ItemStack.EMPTY && mapTest.getItem() == Items.FILLED_MAP)
							{
								mapCount++;
							}
						}
						// lets do 32xp per map. 
						if (mapCount > 0)
						{
							// set enchantment stuff
							int enchant = getLevelFromXP(mapCount*32);
							//System.out.println("Calculated Cost = "+enchant);
							requiredlevels = enchant;
							setInventorySlotContents(0, playerstack);
							getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	private int getLevelFromXP(int xp)
	{
		int xpcounter = 0;
		int levelcounter = 1;
		for (int i = 0; i <= 30; i++)
		{
			if (i < 16)
			{
				xpcounter = xpcounter + 2*i+7;
			}
			else
			{
				xpcounter = xpcounter + 5*i-38;
			}
			if (xp <= xpcounter)
			{
				return i;
			}
			//System.out.println("total Level XP = "+xpcount+"   on level "+i);
		}
		
		return 0;
	}

	public boolean resetPlate()
	{
		if (hasOldPlate())
		{
			ItemStack newChase = new ItemStack(ItemChase.instance, 1, 0);
			ItemStack chaseStack = getStackInSlot(1);
			setInventorySlotContents(0, ItemStack.EMPTY);
			if (chaseStack != ItemStack.EMPTY)
			{
				if (chaseStack.getCount() < 64)
				{
					chaseStack.setCount(chaseStack.getCount() + 1);
				}
				else
				{
					setInventorySlotContents(0, newChase);
				}
			}
			else
			{
				setInventorySlotContents(1, newChase);
			}
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			return true;
			
		}
		return false;
	}
	
	public boolean saveBook(World world)
	{
			ItemStack booktosave = getStackInSlot(0);
			if (booktosave != ItemStack.EMPTY)
			{
				Item bookItem = booktosave.getItem();
				if (bookItem instanceof ItemWrittenBook)
				{
					FileUtil util = new FileUtil();
					util.saveBook(booktosave, world); 
					//System.out.println("Saved Book!");
					bookIsSaved = true;
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
				if (bookItem instanceof ItemBigBook)
				{
					FileUtil util = new FileUtil();
					util.saveNBTtoFile(booktosave, world, 0);
					bookIsSaved = true;
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
				if (bookItem instanceof ItemRecipeBook)
				{
					FileUtil util = new FileUtil();
					util.saveNBTtoFile(booktosave, world, 1);
					bookIsSaved = true;
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
				if (bookItem instanceof ItemStockroomCatalog) 
				{
					FileUtil util = new FileUtil();
					util.saveNBTtoFile(booktosave, world, 2);
					bookIsSaved = true;
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
			}
		return false;
	}
	
	public boolean enchantPlate(EntityPlayer player)
	{
		//System.out.println("executingEnchantPlate");
		ItemStack stack = getStackInSlot(0);
		if (stack != ItemStack.EMPTY && chaseCheck() && getStackInSlot(2) == ItemStack.EMPTY)
		{
			//System.out.println("to the next stage of enchantment");
			Item enchBook = stack.getItem();
			if (enchBook instanceof ItemEnchantedBook)
			{
				NBTTagList enchbookTagList = getEnchantmentTagList(stack);
				int enchCount = enchbookTagList.tagCount();
				//int levelcost = 0;
				float levelCalc = 0.0F;
				for (int x=0; x < enchbookTagList.tagCount(); x++)
				{
					int enchlvl = ((NBTTagCompound)enchbookTagList.getCompoundTagAt(x)).getShort("lvl");
					int enchid = ((NBTTagCompound)enchbookTagList.getCompoundTagAt(x)).getShort("id");
					int maxlvl = 1;
					if (Enchantment.getEnchantmentByID(enchid) != null)
					{
						maxlvl = Enchantment.getEnchantmentByID(enchid).getMaxLevel();
					}
					float slidingScale = 1.0F;
					switch (maxlvl)
					{
					case 1:{enchlvl = enchlvl + 4; break;}
					case 2:{enchlvl = enchlvl + 3; break;}
					case 3:{enchlvl = enchlvl + 2; break;}
					case 4:{enchlvl = enchlvl + 1; break;}
					}
					if (x > 0)
					{
						slidingScale = (0.5F)*(1.0F/x);
					}
					//System.out.println(slidingScale); 
					levelCalc = levelCalc + ((enchlvl*enchlvl) + 15.0F)*slidingScale;
					//levelcost = levelcost + ((enchlvl*enchlvl) + 15);
					//System.out.println(levelCalc);
				}
				levelCalc = levelCalc*(Config.enchantmentMultiplyer/10.0F);
				//System.out.println("Final Level Cost: "+(int)levelCalc);
				int levelcost = (int)levelCalc;
				if (player.experienceLevel < levelcost)
				{
					return false;
				}
				
				ItemStack enchplate = new ItemStack(ItemEnchantedPlate.instance, 1, 0);
				if (stack.hasTagCompound())
				{
					enchplate.setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
					setInventorySlotContents(2, enchplate);
					ItemStack chases = getStackInSlot(1);
					chases.setCount(chases.getCount() - 1);
					setInventorySlotContents(0, ItemStack.EMPTY);
				}
				
				 player.addExperienceLevel(-levelcost);
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				return true;
				// ok, now I need to read the nbt data and apply it to the plate.
			}
		}
		return false;
	}
	
	public boolean createAtlasPlate(EntityPlayer player)
	{
		ItemStack atlas = this.getStackInSlot(0);//.copy();
		if (atlas != ItemStack.EMPTY && atlas.getItem() instanceof ItemAtlas && chaseCheck() && getStackInSlot(2) == ItemStack.EMPTY)
		{
			atlas = atlas.copy();
			if (this.requiredlevels > player.experienceLevel)
			{
				return false;
			}
			
			NBTTagCompound tags = atlas.getTagCompound();
			// so I need to search the tags and delete everything from inventory that isnt a filled map, then copy the edited tag to the atlas plate
			InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", false, 48);
			NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < atlasInventory.getSizeInventory())
				{
					ItemStack invStack = new ItemStack(tag);
					atlasInventory.setInventorySlotContents(slot, invStack);
				}
			}
			for (int i = 0; i < atlasInventory.getSizeInventory(); i++)
			{
				ItemStack testStack = atlasInventory.getStackInSlot(i);
				if (testStack != ItemStack.EMPTY)
				{
					if (testStack.getItem() !=Items.FILLED_MAP)
					{
						atlasInventory.setInventorySlotContents(i, ItemStack.EMPTY);
					}
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
	    	ItemStack chases = getStackInSlot(1);
			ItemStack plate = new ItemStack(ItemAtlasPlate.instance, 1, 0);
			plate.setTagCompound(tags);
			player.addExperienceLevel(-requiredlevels);
			setInventorySlotContents(2, plate);
			if (chases.getCount() > 1)
			{
				chases.setCount(chases.getCount() - 1);
				this.setInventorySlotContents(1, chases);
				
			}
			else
			{
				this.setInventorySlotContents(1, ItemStack.EMPTY);
				
			}
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		}
		return false;
	}
	
	public ItemStack getEntchantedBook()
	{
		return getStackInSlot(0);
	}
	
    public NBTTagList getEnchantmentTagList(ItemStack stack)
    {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey("StoredEnchantments") ? (NBTTagList)stack.getTagCompound().getTag("StoredEnchantments") : new NBTTagList();
    }
	
	public void setPlate()
	{
		if (chaseCheck() && getStackInSlot(2) == ItemStack.EMPTY)
		{
			//System.out.println(getBookname());
			ItemStack stack = getStackInSlot(1);
			ItemStack newPlate = new ItemStack(ItemPlate.instance, 1, 0);
			writePlateNBT(newPlate, getBookname()); 
			inventory.set(2, newPlate);
			//inventory[2] = newPlate;
			
			if (stack.getCount() != 1)
			{
				stack.setCount(stack.getCount() - 1);
				inventory.set(1, stack);
				//inventory[1] = stack;
				
			}
			else
			{
				//inventory[1] = null;
				inventory.set(1, ItemStack.EMPTY);
				
			}
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);

		}
		
	}
    
    public String getBookname()
    {
    	return nameofbook;
    	
    }
    
    public void setBookname(String name)
    {
    	nameofbook = name;
    	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
    
    
    public boolean hasSavedBook()
    {
    	return bookIsSaved;
    }
    
    public boolean hasNewPlate()
    {
		ItemStack stack = getStackInSlot(2);
		if (stack != ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
    }
    public boolean hasEnchantedPlate()
    {
    	ItemStack stack = getStackInSlot(2);
    	if (stack != ItemStack.EMPTY)
    	{
    		Item item = stack.getItem();
    		if (item instanceof ItemEnchantedPlate)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean hasAtlasPlate()
    {
    	ItemStack stack = getStackInSlot(2);
    	if (stack != ItemStack.EMPTY)
    	{
    		Item item = stack.getItem();
    		if (item instanceof ItemAtlasPlate)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean hasOldPlate()
    {
		ItemStack stack = getStackInSlot(0);
		if (stack != ItemStack.EMPTY)
		{
			Item booktest = stack.getItem();
			if (booktest instanceof ItemPlate)
			{
				return true;
			}
		}
			return false;
    }
    
    public boolean hasLowerChase()
    {
    	ItemStack stack = getStackInSlot(0);
		if (stack != ItemStack.EMPTY)
		{
			Item booktest = stack.getItem();
			if (booktest instanceof ItemChase)
			{
				return true;
			}
		}
			return false;
    }
    
	public void setBookList(String books, String authors, String publics)
	{
		this.listofbooks = books;
		this.listofAuthors = authors;
		this.listofPublicBooks = publics;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	//worldObj.scheduleBlockUpdate(xCoord, yCoord, xCoord, 0, 0);
	}
	
	public String getbookListString()
	{
		//getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		return this.listofbooks;
	}
	public String getAuthorListString()
	{
		return this.listofAuthors;
	}
	public String getPublicListString()
	{
		return this.listofPublicBooks;
	}
	
	public String[] getbookList()
	{
		//getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		String[] booklist = listofbooks.split("&_");
		return booklist;
	}
	
	public String[] getAuthorList()
	{
		//getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		String[] authorlist = listofAuthors.split("&_");
		return authorlist;
	}
	public boolean[] getPublicList()
	{
		//getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		String[] publist = listofPublicBooks.split("&_");
		boolean[] publiclist = new boolean[publist.length];
		for (int h = 0; h < publist.length; h++)
		{
			if (publist[h].contains("true"))
			{
				publiclist[h] = true;
			}
			else
			{
				publiclist[h] = false;
			}
		}
		return publiclist;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			if (slot == 0)
			{
				if (stackitem instanceof ItemPlate || stackitem instanceof ItemWrittenBook)
				{
					return true;
				}
			
			}
			if (slot == 1)
			{
				if (stackitem instanceof ItemChase)
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockTypesettingTable.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.bookIsSaved = nbt.getBoolean("savedbook");
		this.nameofbook = nbt.getString("SavedBookName");
		this.listofbooks = nbt.getString("ListOfBooks");
		this.listofAuthors = nbt.getString("listofAuthors");
		this.listofPublicBooks = nbt.getString("listofPublicBooks");
		this.requiredlevels = nbt.getInteger("levelreq");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
    	nbt.setBoolean("savedbook", bookIsSaved);
    	nbt.setString("SavedBookName", nameofbook);
    	nbt.setString("ListOfBooks", listofbooks);
    	nbt.setString("listofAuthors", listofAuthors);
    	nbt.setString("listofPublicBooks", listofPublicBooks);
    	nbt.setInteger("levelreq", requiredlevels);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
	
	@Override
	public void update() 
	{
		if (!world.isRemote)
		{
			if (counter >= 2)
			{
				int power = getWorld().isBlockIndirectlyGettingPowered(getPos());
				if (power > redstone)
				{
					this.setPlate();
				}
				redstone = power;
				counter = 0;
			}
			else
			{
				counter++;
			}
		}
	}
}
