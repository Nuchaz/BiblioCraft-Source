package jds.bibliocraft.tileentities;

import java.util.Iterator;
import java.util.List;

import jds.bibliocraft.blocks.BlockFramedChest;
import jds.bibliocraft.containers.ContainerFramedChest;
import jds.bibliocraft.helpers.BiblioSortingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityFramedChest extends BiblioTileEntity implements ITickable
{

    private boolean openChest = false;
	private ItemStack labelStack = null;
	private boolean isDouble = false;
	private boolean isLeft = true;
	
	private int ticksSinceSync = 0;
	private int numPlayersUsing = 0;
	private float prevLidAngle = 0.0f;
	private float lidAngle = 0.0f;
	
	public TileEntityFramedChest adjacentDoubleChest = null;
	//public ResourceLocation customChestFillerTexture = null;


	
	public TileEntityFramedChest()
	{
		super(27, true);
	}
	
	public float getPrevLidAngle()
	{
		return this.prevLidAngle;
	}
	
	public void addUsingPlayer(boolean add)
	{
		if (add)
		{
			this.numPlayersUsing++;
		}
		else
		{
			this.numPlayersUsing--;
			if (this.numPlayersUsing < 0)
			{
				this.numPlayersUsing = 0;
			}
		}
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public ItemStack getLabelStack()
	{
		return this.labelStack;
	}
	private void setLabelStack(ItemStack stack)
	{
		this.labelStack = stack;
	}
	
	public boolean getIsDouble()
	{
		return this.isDouble;
	}
	
	public boolean getIsLeft()
	{
		return this.isLeft;
	}
	
	public void setIsDouble(boolean dub, boolean left, TileEntityFramedChest secondChest)
	{
		this.isDouble = dub;
		this.isLeft = left;
		this.adjacentDoubleChest = secondChest;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setAdjacentChest(TileEntityFramedChest secondChest)
	{
		this.adjacentDoubleChest = secondChest;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

    public void setOpenChest(boolean chest)
    {
    	this.openChest = chest;
    	getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }
    public boolean getOpenChest()
    {
    	return this.openChest;
    }
	
	@Override
    public void update()
    {
        
        ++this.ticksSinceSync;
        float f;
        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            f = 5.0F;
            List list = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)this.pos.getX() - f), (double)((float)this.pos.getY() - f), (double)((float)this.pos.getZ() - f), (double)((float)(this.pos.getX() + 1) + f), (double)((float)(this.pos.getY() + 1) + f), (double)((float)(this.pos.getZ() + 1) + f)));
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
            	EntityPlayer player = (EntityPlayer)iterator.next();

                if (player.openContainer instanceof ContainerFramedChest)
                {
                    IInventory iinventory = ((ContainerFramedChest)player.openContainer).getMainTile();

                    if (iinventory == this || iinventory instanceof TileEntityFramedChest && checkIfIsAdjacentDoubleChest((TileEntityFramedChest)iinventory))
                    {
                    	this.numPlayersUsing++;
                    }
                }
            }
        }


        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d2;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && !this.world.isRemote)
        {
            double d1 = (double)this.pos.getX() + 0.5D;
            d2 = (double)this.pos.getZ() + 0.5D;
            if (this.getIsDouble() && this.adjacentDoubleChest != null)
            {
                if (this.adjacentDoubleChest.pos.getZ() == (this.pos.getZ()+1)) //zpos
                {
                    d2 += 0.5D;
                }
                if (this.adjacentDoubleChest.pos.getX() == (this.pos.getX()+1)) //xpos
                {
                    d1 += 0.5D;
                }
            }
            if ((this.getIsDouble() && this.getIsLeft()) || !this.getIsDouble())
            {
            	this.getWorld().playSound(null, d1, (double)this.pos.getY() + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }

        if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0F) || (numPlayersUsing > 0 && this.lidAngle < 1.0F))
        {
        	//first we deal with the lid angle
            float f1 = this.lidAngle;

            if (numPlayersUsing > 0)
            {
                this.lidAngle += f;
            }
            else
            {
                this.lidAngle -= f;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }
            
            float f2 = 0.5F;
            // here is where we deal with the lid closing sound
            if (this.lidAngle < f2 && f1 >= f2 && !this.world.isRemote)
            {
                d2 = (double)this.pos.getX() + 0.5D;
                double d0 = (double)this.pos.getZ() + 0.5D;

                if (this.getIsDouble() && this.adjacentDoubleChest != null)
                {
	                if (this.adjacentDoubleChest.pos.getZ() == (this.pos.getZ()+1)) //zpos
	                {
	                    d0 += 0.5D;
	                }
	                if (this.adjacentDoubleChest.pos.getX() == (this.pos.getX()+1)) //xpos
	                {
	                    d2 += 0.5D;
	                }
                }

                if ((this.getIsDouble() && this.getIsLeft()) || !this.getIsDouble())
                {
                	this.getWorld().playSound(null, d2, (double)this.pos.getY() + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                }
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }    
    }
	
	public void setLidAngle(float langle)
	{
		this.lidAngle = langle;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public float getLidAngle()
	{
		return this.lidAngle;
	}
	
	private boolean checkIfIsAdjacentDoubleChest(TileEntityFramedChest tile)
	{
		if (tile != null && this.adjacentDoubleChest != null)
		{
			if (tile.getIsDouble() && this.adjacentDoubleChest.getIsDouble() && this.getIsDouble() && this.getIsLeft())
			{
				if (tile.pos.getX() == this.adjacentDoubleChest.pos.getX() && tile.pos.getY() == this.adjacentDoubleChest.pos.getY() && tile.pos.getZ() == this.adjacentDoubleChest.pos.getZ())
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
		return BlockFramedChest.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		ItemStack bestStack = BiblioSortingHelper.getLargestStackInList(BiblioSortingHelper.getStackForBuiltinLabel(this));
		if (bestStack != ItemStack.EMPTY)
		{
			bestStack.setCount(1);
		}
		this.labelStack = bestStack;
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.openChest = nbt.getBoolean("openChest");
		this.isDouble = nbt.getBoolean("isDouble");
		this.isLeft = nbt.getBoolean("isLeft");
		this.numPlayersUsing = nbt.getInteger("numPlayersUsing");
		this.labelStack = new ItemStack(nbt.getCompoundTag("labelStack"));
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
    	nbt.setBoolean("openChest", this.openChest);
    	nbt.setBoolean("isDouble", this.isDouble);
    	nbt.setBoolean("isLeft", this.isLeft);
    	nbt.setInteger("numPlayersUsing", this.numPlayersUsing);
    	if (this.labelStack != null)
    	{
    		nbt.setTag("labelStack", this.labelStack.writeToNBT(new NBTTagCompound()));
    	}
    	else
    	{
    		nbt.setTag("labelStack", new NBTTagCompound());
    	}
		return nbt;
	}
    
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
