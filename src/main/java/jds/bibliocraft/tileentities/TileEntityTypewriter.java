package jds.bibliocraft.tileentities;

import java.util.List;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.blocks.BlockTypeWriter;
import jds.bibliocraft.storygen.BookGenUtil;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityTypewriter extends BiblioTileEntity implements ITickable 
{
	private BookGenUtil bookgen;
	public int bookWriteCounts = 0;
	public boolean foundValidEntity = false;
	public String entityName = "";
	public int entityType = 0;
	public boolean soundPaperAdd = false;
	public boolean soundTyping = false;
	public boolean soundTypingHuman = false;
	public boolean soundEndBell = false;
	public boolean soundRemoveBook = false;
	public int soundCount = 0;
	
	public boolean showText = false;
	
	public TileEntityTypewriter()
	{
		super(2, false); // 1 slot for blank paper, 1 slot for a finished book, only 1 book at a time, machine stops working if slot is full.
	}
	
	public void setShowText(boolean setter)
	{
		showText = setter;
	}
	public boolean getShowText()
	{
		return showText;
	}
	
	
    public void setBookWriteCount(int cont, boolean human)
    {
    	this.bookWriteCounts = cont;
    	if (cont == 0)
    	{
    		this.soundRemoveBook = true;
    	}
    	else
    	{
    		if (human)
    		{
    			this.soundTypingHuman = true;
    		}
    		else
    		{
    			this.soundTyping = true;
    		}
    	}
    	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
    
    public int getBookWriteCount()
    {
    	return this.bookWriteCounts;
    }
    
    public int addPaper(ItemStack paper)
    {
    	ItemStack currentPaper = getStackInSlot(0);
    	int currentPaperStackSize = 0;
    	if (currentPaper != ItemStack.EMPTY)
    	{
    		currentPaperStackSize = currentPaper.getCount();
    		if (currentPaperStackSize == 64)
    		{
    			return -1;
    		}
    	}
    	
    	this.soundPaperAdd = true; 
    	ItemStack newpaper = paper.copy();
    	if (currentPaperStackSize+paper.getCount() <= 64)
    	{
    		newpaper.setCount(newpaper.getCount() + currentPaperStackSize);
    		setInventorySlotContents(0, paper);
    		return 0;
    	}
    	else
    	{
    		int returnSize = currentPaperStackSize + paper.getCount() - 64;
    		newpaper.setCount(64);
    		setInventorySlotContents(0, newpaper);
    		return returnSize;
    	}
    }
    
    public boolean getHasPaper()
    {
    	if (getStackInSlot(0) != ItemStack.EMPTY)
    	{
    		return true;
    	}
    	return false;
    }
    
    public boolean getHasEnoughPaper()
    {
    	ItemStack paper = getStackInSlot(0);
    	if (paper != ItemStack.EMPTY)
    	{
    		if (paper.getCount() >= 8)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean removePaperForBook()
    {
    	ItemStack paper = getStackInSlot(0);
    	if (paper.getCount() == 8)
    	{
    		this.setInventorySlotContents(0, ItemStack.EMPTY);
    		return true;
    	}
    	else if (paper.getCount() > 8)
    	{
        	ItemStack paperCopy = paper.copy();
        	paperCopy.setCount(paper.getCount() - 8);
        	this.setInventorySlotContents(0, paperCopy);
        	return true;
    	}
    	return false;
    }
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
    
    private int counter = 0;
    
    @Override
    public void update()
    {
    	if (counter == 100 && !this.world.isRemote)
    	{
    		counter = 0;
    		if (this.getStackInSlot(1) == ItemStack.EMPTY)
    		{
    			if (this.getHasEnoughPaper())
    			{
    				// write a book
	    			if (this.bookWriteCounts >= 15)
	    			{
	    				if (this.removePaperForBook())
	    				{
	    					if (!this.world.isRemote)
	    					{
	    						this.writeCustomBook();
	    					}
	    				}
	    			}
	    			else
	    			{
	    				this.scanForEntityes();
	    				if (this.foundValidEntity)
	    				{
	    					this.setBookWriteCount(this.bookWriteCounts+1, false);
	    				}
	    				
	    			}
    			}
    		}
    		else
    		{
    			this.addToDesk();
    		}
    	}
    	else
    	{
    		counter++;
    	}
    	
    	this.soundMonitor();
    }
    
    public void soundMonitor()
    {
    	if (this.soundEndBell)
    	{
    		if (this.soundCount > 10)
    		{
    			this.soundEndBell = false;
    			this.soundCount = 0;
    			
    		}
    		else
    		{
    			this.soundCount++;
    		}
    	}
    	
    	if (this.soundPaperAdd)
    	{
    		if (this.soundCount > 10)
    		{
    			this.soundPaperAdd = false;
    			this.soundCount = 0;
    			
    		}
    		else
    		{
    			this.soundCount++;
    		}
    	}
    	
    	if (this.soundRemoveBook)
    	{
    		if (this.soundCount > 10)
    		{
    			this.soundRemoveBook = false;
    			this.soundCount = 0;
    			
    		}
    		else
    		{
    			this.soundCount++;
    		}
    	}
    	
    	if (this.soundTyping)
    	{
    		if (this.soundCount > 10)
    		{
    			this.soundTyping = false;
    			this.soundCount = 0;
    			
    		}
    		else
    		{
    			this.soundCount++;
    		}
    	}
    	
    	if (this.soundTypingHuman)
    	{
    		if (this.soundCount > 10)
    		{
    			this.soundTypingHuman = false;
    			this.soundCount = 0;
    			
    		}
    		else
    		{
    			this.soundCount++;
    		}
    	}
    }
    
    public void writeCustomBook() 
    {
    	this.bookgen = new BookGenUtil(this.entityType, this.entityName);
		ItemStack newBook = new ItemStack(Items.WRITTEN_BOOK, 1, 0);
		NBTTagCompound bookTag = new NBTTagCompound();
		NBTTagList bookTagList = new NBTTagList();
		//stasis page
		NBTTagString nbtPage = new NBTTagString(bookgen.getStasis(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		//trigger page
		nbtPage = new NBTTagString(bookgen.getTrigger(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		//quest page
		nbtPage = new NBTTagString(bookgen.getQuest(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		//surprise page
		nbtPage = new NBTTagString(bookgen.getSurprise(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		//choice page
		nbtPage = new NBTTagString(bookgen.getChoice(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		//climax page
		nbtPage = new NBTTagString(bookgen.getClimax(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		//reversal page
		nbtPage = new NBTTagString(bookgen.getReversal(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		//resolution
		nbtPage = new NBTTagString(bookgen.getResolution(this.entityType, this.entityName));
		bookTagList.appendTag(nbtPage);
		
		bookTag.setTag("title", new NBTTagString(bookgen.getBookTitle()));
		bookTag.setTag("author", new NBTTagString(this.entityName));
		bookTag.setTag("pages", bookTagList);
		byte resolveByte = 1;
		NBTTagByte resolve = new NBTTagByte(resolveByte);
		bookTag.setTag("resolved", resolve);
		newBook.setTagCompound(bookTag);
		newBook.setTagCompound(bookTag);
		
		this.setInventorySlotContents(1, newBook);
		this.soundEndBell = true;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
    
    public void scanForEntityes()
    {
    	double xAdjust = 0.0;
    	double zAdjust = 0.0;
    	double xAdjust2 = 0.0;
    	double zAdjust2 = 0.0;
    	switch (this.getAngle())
    	{
    		case SOUTH:{xAdjust = -1.0; zAdjust =  0.0; xAdjust2 = 0.0; zAdjust2 = 1.0; break;}
    		case WEST:{xAdjust =  0.0; zAdjust = -1.0; xAdjust2 = 1.0; zAdjust2 = 0.0; break;}
    		case NORTH:{xAdjust =  1.0; zAdjust =  0.0; xAdjust2 = 2.0; zAdjust2 = 1.0; break;}
    		case EAST:{xAdjust =  0.0; zAdjust =  1.0; xAdjust2 = 1.0; zAdjust2 = 2.0; break;}
    		default: break;
    	}
    	
		AxisAlignedBB bb = new AxisAlignedBB(this.pos.getX()+xAdjust, this.pos.getY(), this.pos.getZ()+zAdjust, this.pos.getX()+xAdjust2, this.pos.getY(), this.pos.getZ()+zAdjust2);
		List checkEntities = this.world.getEntitiesWithinAABB(EntityCreature.class, bb);
		// need to make lists of all things that can write books. 
		
		this.foundValidEntity = false;
		this.entityName = "";
		this.entityType = 0;
		for (int x = 0; x<checkEntities.size(); x++)
		{
			EntityCreature guy = (EntityCreature)checkEntities.get(x);
			if (guy instanceof EntityVillager)
			{
				if (testFoundEntity(guy, 1)) {return;}	
			}
			else if (guy instanceof EntityPig || guy instanceof EntityPigZombie)
			{
				if (testFoundEntity(guy, 2)) {return;}
			}
			else if (guy instanceof EntityChicken)
			{
				if (testFoundEntity(guy, 3)) {return;}
			}
			else if (guy instanceof EntityCow || guy instanceof EntityMooshroom)
			{
				if (testFoundEntity(guy, 4)) {return;}
			}
			else if (guy instanceof EntitySheep)
			{
				if (testFoundEntity(guy, 5)) {return;}
			}
			else if (guy instanceof EntityWolf)
			{
				if (testFoundEntity(guy, 6)) {return;}
			}
			else if (guy instanceof EntityOcelot)
			{
				if (testFoundEntity(guy, 7)) {return;}
			}
			else if (guy instanceof EntityCreeper)
			{
				if (testFoundEntity(guy, 8)) {return;}
			}
			else if (guy instanceof EntityZombie)
			{
				if (testFoundEntity(guy, 9)) {return;}
			}
			else if (guy instanceof EntityEnderman)
			{
				if (testFoundEntity(guy, 10)) {return;}
			}
			else
			{
				if (testFoundEntity(guy, -1)) {return;}
			}


		}
    }
    
    private boolean testFoundEntity(EntityCreature guy, int type)
    {
		if (guy.getCustomNameTag().length() > 0)
		{
			this.foundValidEntity = true;
			this.entityName = guy.getCustomNameTag();
			this.entityType = type;
			return true;
		}
    	return false;
    }
    
    public void addToDesk()
    {
		TileEntity lowerTile = this.world.getTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
		if (lowerTile != null && lowerTile instanceof TileEntityDesk)
		{
			TileEntityDesk desk = (TileEntityDesk)lowerTile;
			int left = desk.getLeftBookEmptySlot();
			int right = desk.getRightBookEmptySlot();
			if (left >= 0)
			{
				desk.setInventorySlotContents(left, this.getStackInSlot(1));
				this.setInventorySlotContents(1, ItemStack.EMPTY);
				this.setBookWriteCount(0, false);
			}
			else if (right >= 0) 
			{
				desk.setInventorySlotContents(right, this.getStackInSlot(1));
				this.setInventorySlotContents(1, ItemStack.EMPTY);
				this.setBookWriteCount(0, false);
			}
		}
    }
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			if (stackitem == Items.PAPER)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockTypeWriter.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) {}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.bookWriteCounts = nbt.getInteger("bookWriteCount");
		this.soundEndBell = nbt.getBoolean("soundEndBell");
		this.soundPaperAdd = nbt.getBoolean("soundPaperAdd");
		this.soundRemoveBook = nbt.getBoolean("soundRemoveBook");
		this.soundTyping = nbt.getBoolean("soundTyping");
		this.soundTypingHuman = nbt.getBoolean("soundTypingHuman");
		if (this.soundPaperAdd)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_TYPEWRITER_ADDPAPER, SoundCategory.BLOCKS, 0.7F, 1.0F, false);
			//worldObj.playSound(x, y, z, soundIn, category, volume, pitch, distanceDelay);
			this.soundPaperAdd = false;
		}
		
		if (this.soundTyping)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_TYPEWRITER_TYPEING, SoundCategory.BLOCKS, 0.7F, 1.0F, false);
			this.soundTyping = false;
		}
		
		if (this.soundEndBell)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_TYPEWRITER_ENDBELL, SoundCategory.BLOCKS, 0.7F, 1.0F, false);
			this.soundEndBell = false;
		}
		
		if (this.soundRemoveBook)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_TYPEWRITER_REMOVEBOOK, SoundCategory.BLOCKS, 0.7F, 1.0F, false);
			this.soundRemoveBook = false;
		}
		
		if (this.soundTypingHuman)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_TYPEWRITER_TYPESINGLE, SoundCategory.BLOCKS, 0.7F, 1.0F, false);
			this.soundTypingHuman = false;
		}
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		nbt.setInteger("bookWriteCount", this.bookWriteCounts);
		nbt.setBoolean("soundEndBell", this.soundEndBell);
		nbt.setBoolean("soundPaperAdd", this.soundPaperAdd);
		nbt.setBoolean("soundRemoveBook", this.soundRemoveBook);
		nbt.setBoolean("soundTyping", this.soundTyping);
    	nbt.setBoolean("soundTypingHuman", this.soundTypingHuman);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
