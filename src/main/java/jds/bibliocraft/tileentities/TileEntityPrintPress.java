package jds.bibliocraft.tileentities;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockPrintingPress;
import jds.bibliocraft.helpers.FileUtil;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemAtlasPlate;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemEnchantedPlate;
import jds.bibliocraft.items.ItemPlate;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemStockroomCatalog;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.oredict.OreDictionary;


public class TileEntityPrintPress extends BiblioTileEntity implements ITickable, ISidedInventory
{	
	public int furnaceCookTime = 0;
	public int furnaceBurnTime = 0;
	public int currentItemBurnTime = 0;
	public boolean animate = false;
	
	private boolean bed = false;
	private boolean arm = true;
	public float armAngle = -5.0f;
	public float bedAngle = 0.0f;
	public float prevArmAngle = -5.0f;
	private float armspeed = 10.0f;
	private float bedspeed = 3.0f;
	
	private boolean showPlateText = false;
	private boolean showInkText = false;
	private boolean showEmptyBookText = false;
	private boolean showNewBookText = false;
	
	
	public TileEntityPrintPress()
	{
		super(4, false);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public void setShowPlateText(boolean show)
	{
		showPlateText = show;
	}
	public void setShowInkText(boolean show)
	{
		showInkText = show;
	}
	public void setShowEmptyBookText(boolean show)
	{
		showEmptyBookText = show;
	}
	public void setShowNewBookText(boolean show)
	{
		showNewBookText = show;
	}
	
	public boolean getShowPlateText()
	{
		return showPlateText;
	}
	public boolean getShowInkText()
	{
		return showInkText;
	}
	public boolean getShowEmptyBookText()
	{
		return showEmptyBookText;
	}
	public boolean getShowNewBookText()
	{
		return showNewBookText;
	}
	
    public boolean hasPlate()
    {
    	ItemStack stack = getStackInSlot(1);
    	if (stack != ItemStack.EMPTY)
    	{
    		Item isplate = stack.getItem();
    		if (isplate instanceof ItemPlate || isplate instanceof ItemEnchantedPlate || isplate instanceof ItemAtlasPlate)  // Right here is where I need to check if the plate is an enchanted plate so that can also be placed on the printing press
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean hasEnchantedPlate()
    {
    	ItemStack stack = getStackInSlot(1);
    	if (stack != ItemStack.EMPTY)
    	{
    		Item isplate = stack.getItem();
    		if (isplate instanceof ItemEnchantedPlate)
    		{
    			//  check for enchantment and return false if none found.
       			NBTTagList taglist = this.getEnchantmentTagList(stack);
    			int tagscount = 0;
    			if (taglist != null)
    			{
    				tagscount = taglist.tagCount();
    				if (tagscount > 0)
    				{
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
    
    public boolean hasAtlasPlate()
    {
    	ItemStack stack = getStackInSlot(1);
    	if (stack != ItemStack.EMPTY)
    	{
    		Item test = stack.getItem();
    		if (test instanceof ItemAtlasPlate)
    		{
    			return true;
    		}
    	}
    	return false;
    }

    public boolean hasAtlasInput()
    {
    	ItemStack stack = getStackInSlot(2);
    	if (stack != ItemStack.EMPTY)
    	{
    		Item test = stack.getItem();
    		if (test instanceof ItemAtlas)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public String plateName()
    {
    	String bookname = "";
    	if (hasPlate())
    	{
    		ItemStack stack = getStackInSlot(1);
    		NBTTagCompound tag = stack.getTagCompound();
    		if (tag != null)
    		{
    			bookname = tag.getString("bookName");
    		}
    		if (hasEnchantedPlate())
    		{
    			NBTTagList taglist = this.getEnchantmentTagList(stack);
    			int tagscount = 0;
    			if (taglist != null)
    			{
    				tagscount = taglist.tagCount();
    			}
    			if (tagscount > 0)
    			{
    				short id = ((NBTTagCompound)taglist.getCompoundTagAt(0)).getShort("id");
    				short lvl = ((NBTTagCompound)taglist.getCompoundTagAt(0)).getShort("lvl");

    				if (Enchantment.getEnchantmentByID(id) != null)
    				{
    					bookname = Enchantment.getEnchantmentByID(id).getTranslatedName(lvl);
    				}
    				else
    				{
    					bookname = "Unregistered";
    				}
    			}
    		}
    		if (hasAtlasPlate())
    		{
    			bookname = "Atlas Copy";
    		}
    	}
    	else
    	{
    		bookname = "none";
    	}
    	return bookname;
    }
    
    public NBTTagList getEnchantmentTagList(ItemStack stack)
    {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey("StoredEnchantments") ? (NBTTagList)stack.getTagCompound().getTag("StoredEnchantments") : new NBTTagList();
    }
    
    public int getBurnTimeRemainingScaled(int par1)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * par1 / this.currentItemBurnTime;
    }
    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }
    
    @Override
    public void update()
    {
        boolean burnCheck = this.furnaceBurnTime > 0;
        boolean isDirty = false;
        
        if (this.furnaceBurnTime > 0)
        {
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote)
        {
            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.inventory.get(0));

                if (this.furnaceBurnTime > 0)
                {
                	isDirty = true;

                    if (this.inventory.get(0) != ItemStack.EMPTY)
                    {
                    	setPressAnimation(true);
                    	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
                        //--this.inventory[0].getCount();
                        this.inventory.get(0).setCount(this.inventory.get(0).getCount() - 1);

                        if (this.inventory.get(0).getCount() == 0)
                        {
                            this.inventory.set(0, this.inventory.get(0).getItem().getContainerItem(inventory.get(0)));
                            setInventorySlotContents(0, ItemStack.EMPTY);
                        }
                    }
                }
            }
            if (this.isBurning() && this.canSmelt())
            {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime == 200)
                {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    setPressAnimation(false);
                    isDirty = true;
                }
            }
            else
            {
                this.furnaceCookTime = 0;
            }

            if (burnCheck != this.furnaceBurnTime > 0)
            {
            	isDirty = true;
                //BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }
        
        if (world.isRemote)
        {
        	
        	//client side, handle animation here 
        	if (animate)
        	{
	    		 // range from -5.0f to -115.0f
	    		if (armAngle <= prevArmAngle && armAngle > -115.0f)
	    		{
	    			prevArmAngle = armAngle;
	    			armAngle -= armspeed; 
	    			if (bedAngle >= 0.0f)
	    			bedAngle -= bedspeed; // range is from 0.0f to 25.0f
	    		}
	    		else
	    		{
	    			prevArmAngle = armAngle;
	    			armAngle += armspeed;
	    			bedAngle += bedspeed;
	    		}
	    		if (armAngle == -5.0f && prevArmAngle == (-5.0f - armspeed))
	    		{
	    			prevArmAngle = armAngle;
	    		}
        	}
        	else
        	{
        		if (bedAngle > 0.0f)
        		{
        			bedAngle -= bedspeed;
        		}
        		if  (armAngle < -5.0f)
        		{
        			armAngle += armspeed;
        		}
        			
        	}
        }

        if (isDirty)
        {
        	this.markDirty();
        }
    }
    
    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (this.inventory.get(3) == ItemStack.EMPTY && hasPlate() && this.inventory.get(2) != ItemStack.EMPTY)
        {
        	if ((hasAtlasPlate() && !hasAtlasInput()) || (hasAtlasInput() && !hasAtlasPlate()))
        	{
        		return false;
        	}
        	//System.out.println("smeltican");
        	//setPressAnimation(false); // true
        	return true;
        }
        else
        {
        	//setPressAnimation(true); // false
            return false;
        }
    }
    
    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
    		FileUtil util = new FileUtil();
    		int booknum = util.getBookNumber(world, plateName());
    		if (booknum == -1 && !hasEnchantedPlate() && !hasAtlasPlate())
    		{
    			System.out.println("This is not a valid book name!");
    			setPressAnimation(false);
            	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    			return;
    		}
    		ItemStack newstack = ItemStack.EMPTY;
    		if (booknum == -1 && hasEnchantedPlate())
    		{
    			ItemStack plate = getStackInSlot(1);
    			if (plate.hasTagCompound())
				{
    				ItemStack blankenchbook = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
    				blankenchbook.setTagCompound((NBTTagCompound) plate.getTagCompound().copy());
    				newstack = blankenchbook.copy();
    				int damage = plate.getItemDamage();
    				plate.setItemDamage(damage + 1);
    				if (plate.getItemDamage() > (Config.enchPlateMaxUses - 1))
    				{
    					setInventorySlotContents(1, ItemStack.EMPTY);
    				}
				}
    			else
    			{
    				return;
    			}
    		}
    		if (booknum == -1 && hasAtlasPlate())
    		{
    			newstack = this.inventory.get(2).copy();
    			newstack.setTagCompound(this.inventory.get(1).getTagCompound());
    			this.inventory.set(1, ItemStack.EMPTY);
    		}
    		if (booknum != -1)
    		{
    			int checkBookType = util.getBookType(world, booknum);
    			if (checkBookType == 0)
    			{
    				//big book
    				ItemStack blankbook = new ItemStack(ItemBigBook.instance, 1, 0);
    				newstack = util.loadBookNBT(world, blankbook, booknum);
    			}
    			else if (checkBookType == 1)
    			{
    				//recipe book
    				ItemStack blankbook = new ItemStack(ItemRecipeBook.instance, 1, 0);
    				newstack = util.loadBookNBT(world, blankbook, booknum);
    			}
    			else if (checkBookType == 2)
    			{
    				//stockroom catalog
    				ItemStack blankbook = new ItemStack(ItemStockroomCatalog.instance, 1, 0);
    				newstack = util.loadBookNBT(world, blankbook, booknum);
    			}
    			else
    			{
    				//vanilla
    				ItemStack blankbook = new ItemStack(Items.BOOK, 1, 0);
        			newstack = util.loadBook(world, blankbook, booknum); 
    			}

    		}

        	if (this.inventory.get(3) == ItemStack.EMPTY)
        	{
            	this.inventory.set(3, newstack.copy());
        	}

        	//--this.inventory[2].getCount();
        	this.inventory.get(2).setCount(this.inventory.get(2).getCount() - 1);

        	if (this.inventory.get(2).getCount() <= 0)
        	{
            	this.inventory.set(2, ItemStack.EMPTY);
        	}
        	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
       }
    }
    
    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack par0ItemStack)
    {
    	if (par0ItemStack == ItemStack.EMPTY)
        {
            return 0;
        }
    	Item var2 = par0ItemStack.getItem();
    	if (var2 instanceof ItemDye) 
    	{
    		return 200;
    	}
    	else return 0;
    	
    }
    
    public void setPressAnimation(boolean ison)
    {
    	animate = ison;
    	this.armAngle = this.prevArmAngle = -5.0f;
    	this.bedAngle = 0.0f;
    	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
    public boolean getPressAntimation()
    {
    	return animate;
    }
    
    public boolean isInk(ItemStack stack)
	{
		boolean output = false;
		int[] oreIDs = OreDictionary.getOreIDs(stack);
		if (oreIDs.length > 0)
		{
			for (int i = 0; i < oreIDs.length; i++)
			{
				String oreName = OreDictionary.getOreName(oreIDs[i]);
				if (oreName.equals("dyeBlack"))
				{
					output = true;
				}
			}
		}
		return output;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		if (itemstack != ItemStack.EMPTY)
		{
			Item stackitem = itemstack.getItem();
			if (slot == 0 && isInk(itemstack))
			{
				int metacheck = itemstack.getItemDamage();
				if (metacheck == 0)
				{
					return true;
				}
			}
			if (slot == 1)
			{
				if (stackitem instanceof ItemPlate)
				{
					return true;
				}
			}
			if (slot == 2)
			{
				if (stackitem instanceof ItemBook)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) 
	{
		if (side == EnumFacing.DOWN)
		{
			int[] sides = new int[1];
			sides[0] = 3;
			return sides;
		}
		else
		{
			int[] sides = new int[3];
			sides[0] = 0;
			sides[1] = 1;
			sides[2] = 2;
			return sides;
		}
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, EnumFacing side) 
	{
		if (side != EnumFacing.DOWN)
		{
			if (itemstack != ItemStack.EMPTY)
			{
				Item stackItem = itemstack.getItem();
				if (stackItem instanceof ItemDye && itemstack.getItemDamage() == 0 && slot == 0)
				{
					return true;
				}
				if (stackItem instanceof ItemBook && slot == 2)
				{
					return true;
				}
				if ((stackItem instanceof ItemPlate || stackItem instanceof ItemEnchantedPlate) && slot == 1)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) 
	{
		if (side == EnumFacing.DOWN && slot == 3)
		{
			return true;
		}
		return false;
	}

	@Override
	public String getName() 
	{
		return BlockPrintingPress.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		if (slot >= 0 && slot <= 2 && stack == ItemStack.EMPTY && this.animate == true)
		{
			this.setPressAnimation(false);
		}
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		boolean prevAnimationState = this.animate;
        this.furnaceBurnTime = nbt.getShort("BurnTime");
        this.furnaceCookTime = nbt.getShort("CookTime");
        this.currentItemBurnTime = getItemBurnTime(this.inventory.get(0));
        this.animate = nbt.getBoolean("animate");
        if (this.animate != prevAnimationState)
        {
        	this.armAngle = this.prevArmAngle = -5.0f;
        	this.bedAngle = 0.0f;
        }
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
    	nbt.setShort("BurnTime", (short)this.furnaceBurnTime);
    	nbt.setShort("CookTime", (short)this.furnaceCookTime);
    	nbt.setBoolean("animate", animate);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
