package jds.bibliocraft.tileentities;

import jds.bibliocraft.blocks.BlockPaintingPress;
import jds.bibliocraft.items.ItemPaintingCanvas;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityPaintPress extends BiblioTileEntity implements ITickable 
{
	public int selectedPaintingType = 0; 
	public String selectedPaintingTitle = "blank";
	public float lidAngle = 0.0f;
	public boolean cycleLid = false;
	private int cycleCounter = 0;
	public boolean setPainting = false;
	
	public TileEntityPaintPress()
	{
		super(1, false); // single slot for the canvas, single canvas at a time operation
	}
	
	public void setSelectedPainting(int type, String name)
	{
		this.selectedPaintingType = type;
		this.selectedPaintingTitle = name;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setCycle(boolean cycle)
	{
		if (cycle)
		{
			if (this.cycleLid || this.setPainting)
			{
				return;
			}
		}
		this.cycleLid = cycle;
		this.setPainting = cycle;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setPainting(boolean setter)
	{
		this.setPainting = setter;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public int getPaintingType()
	{
		ItemStack canvas = getStackInSlot(0);
		if (canvas != ItemStack.EMPTY)
		{
			NBTTagCompound tags = canvas.getTagCompound();
			if (tags != null)
			{
				return tags.getInteger("paintingType");
			}
		}
		return 0;
	}
	
	public String getPaintingTitle()
	{
		ItemStack canvas = getStackInSlot(0);
		if (canvas != ItemStack.EMPTY)
		{
			NBTTagCompound tags = canvas.getTagCompound();
			if (tags != null)
			{
				return tags.getString("paintingTitle");
			}
		}
		return "blank";
	}
	
	public int addCanvas(ItemStack canvas)
	{
		if (canvas != ItemStack.EMPTY)
		{
			ItemStack currstack = this.getStackInSlot(0);
			if (currstack == ItemStack.EMPTY)
			{
				int returnvalue = -1;
				if (canvas.getCount() > 1)
				{
					returnvalue = canvas.getCount() - 1;
					ItemStack newCanvas = canvas.copy();
					newCanvas.setCount(1);
					this.setInventorySlotContents(0, newCanvas);
				}
				else
				{
					this.setInventorySlotContents(0, canvas);
					returnvalue = 0;
				}
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				return returnvalue;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			this.setInventorySlotContents(0, ItemStack.EMPTY);
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			return -1;
		}
	}
	
	public void setCanvasPainting()
	{
		//System.out.println(this.selectedPaintingTitle);
		//System.out.println("hmmm");
		
		ItemStack canvas = this.getStackInSlot(0);
		if (canvas != ItemStack.EMPTY)
		{
			//setCycle(true);
			NBTTagCompound tags = canvas.getTagCompound();
			if (tags == null)
			{
				tags = new NBTTagCompound();
			}
			tags.setInteger("paintingType", this.selectedPaintingType);
			tags.setString("paintingTitle", this.selectedPaintingTitle);
			
			ItemStack newCanvas = canvas.copy();
			canvas.setTagCompound(tags);
			setInventorySlotContents(0, newCanvas);
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
    @Override
    public void update()
    {

    		if (this.cycleLid)
    		{
    			if (this.cycleCounter > 20)
    			{
    				this.lidAngle = 0.0f;
    				
    				this.cycleCounter = 0;
    				this.setCycle(false);
    			}
    			else
    			{
    				if (this.cycleCounter > 10)
    				{

    						this.lidAngle -= 2.5f;
    					
    					if (this.setPainting)
    					{
    						if (!world.isRemote)
    						{
    							this.setPainting = false;
    							this.setCanvasPainting();
    						}
    					}
    				}
    				else
    				{
 
	    					if (this.lidAngle <= 22.5f)
	    					{
	    						this.lidAngle += 2.5f;
	    					}
	    					else
	    					{
	    						this.lidAngle = 25.0f;
	    					}
    					
    				}
    				this.cycleCounter++;
    			}
    		}
    	
    }
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			if (stackitem == ItemPaintingCanvas.instance)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{

		return BlockPaintingPress.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack)
	{

	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.selectedPaintingType = nbt.getInteger("paintingType");
		this.selectedPaintingTitle = nbt.getString("paintingTitle");
		this.cycleLid = nbt.getBoolean("cycle");
		this.setPainting = nbt.getBoolean("setPainting");
		this.lidAngle = nbt.getFloat("lidAngle");
		this.cycleCounter = nbt.getInteger("cycleCounter");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		nbt.setInteger("paintingType", this.selectedPaintingType);
    	nbt.setString("paintingTitle", this.selectedPaintingTitle);
    	nbt.setBoolean("cycle", this.cycleLid);
    	nbt.setBoolean("setPainting", this.setPainting);
    	nbt.setFloat("lidAngle", this.lidAngle);
    	nbt.setInteger("cycleCounter", this.cycleCounter);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
