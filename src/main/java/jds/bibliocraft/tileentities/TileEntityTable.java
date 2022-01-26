package jds.bibliocraft.tileentities;

import java.util.Iterator;
import java.util.List;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockTable;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class TileEntityTable extends BiblioTileEntity implements ITickable
{	
	public boolean leg1 = false;
	public boolean leg2 = false;
	public boolean leg3 = false;
	public boolean leg4 = false;
	public boolean top1 = true;
	public boolean top2 = true;
	public boolean top3 = true;
	public boolean top4 = true;
	public boolean exps1 = false;
	public boolean exps2 = false;
	public boolean exps3 = false;
	public boolean exps4 = false;
	public boolean monoleg = true;
	public int slotxangle = 0;
	public int slotyangle = 0;
	public boolean hasMap = false;
	private int counter = 1;
	public EntityItemFrame fauxFrame;// = new EntityItemFrame(this.worldObj);
	public int redstonePassthrough = 0;
	
	public boolean isVanilla = true;
	
	public TileEntityTable()
	{
		super(3, true);
	}
	
	public boolean getHasMap()
	{
		return hasMap;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public boolean setTableSlot(ItemStack stack)
	{
		boolean hasStack;
		if (stack == ItemStack.EMPTY)
		{
			if (isSlotFull())
			{
				setInventorySlotContents(0, ItemStack.EMPTY);
			}
			hasStack = false;
			return hasStack;
		}
		
		if (!isSlotFull())
		{
			setInventorySlotContents(0, stack);
			hasStack = true;
		}
		else
		{
			hasStack = false;
		}
		return hasStack;
	}
	
	public boolean isSlotFull()
	{
		if (getStackInSlot(0) != ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean isClothSlotFull()
	{
		if (getStackInSlot(1)!= ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isCarpetFull()
	{
		if (getStackInSlot(2) != ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ItemStack getCaseStack()
	{
		if (isSlotFull())
		{
			return getStackInSlot(0);
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}
	
	public int setTableCloth(ItemStack stack)
	{
		int stacksize = 0;
		if (stack == ItemStack.EMPTY)
		{
			if(isClothSlotFull())
			{
				setInventorySlotContents(1, ItemStack.EMPTY);
			}
			stacksize = -1;
			return stacksize;
		}
		
		if (!isClothSlotFull())
		{
			if (stack.getCount() > 1)
			{
				stacksize = stack.getCount() - 1;
			}
			else
			{
				stacksize = 0;
			}
			ItemStack carpetpiece = stack.copy();
			carpetpiece.setCount(1);
			setInventorySlotContents(1, carpetpiece);
		}
		else
		{
			stacksize = stack.getCount();
		}
		
		return stacksize;
	}
	public int getClothMetaData()
	{
		if(isClothSlotFull())
		{
			ItemStack carpet = getStackInSlot(1);
			return carpet.getItemDamage();
		}
		else
		{
			return -1;
		}
	}

	public int setCarpet(ItemStack stack)
	{
		int stacksize = 0;
		if (stack == ItemStack.EMPTY)
		{
			if(isCarpetFull())
			{
				setInventorySlotContents(2, ItemStack.EMPTY);
			}
			stacksize = -1;
			return stacksize;
		}
		
		if (!isCarpetFull())
		{
			if (stack.getCount() > 1)
			{
				stacksize = stack.getCount() - 1;
			}
			else
			{
				stacksize = 0;
			}
			ItemStack carpetpiece = stack.copy();
			carpetpiece.setCount(1);
			setInventorySlotContents(2, carpetpiece);
		}
		else
		{
			stacksize = stack.getCount();
		}
		return stacksize;
	}
	
	public int getCarpetMetaData()
	{
		if(isCarpetFull())
		{
			ItemStack carpet = getStackInSlot(2);
			return carpet.getItemDamage();
		}
		else
		{
			return -1;
		}
	}

	public void setLegs(boolean legg1, boolean legg2, boolean legg3, boolean legg4, boolean centerleg)
	{
		leg1 = legg1;
		leg2 = legg2;
		leg3 = legg3;
		leg4 = legg4;
		monoleg = centerleg;
	}
	public void setTops(boolean topp1, boolean topp2, boolean topp3, boolean topp4)
	{
		top1 = topp1;
		top2 = topp2;
		top3 = topp3;
		top4 = topp4;
	}
	public boolean getLeg1()
	{
		return leg1;
	}
	public boolean getLeg2()
	{
		return leg2;
	}
	public boolean getLeg3()
	{
		return leg3;
	}
	public boolean getLeg4()
	{
		return leg4;
	}
	public boolean getTop1()
	{
		return top1;
	}
	public boolean getTop2()
	{
		return top2;
	}
	public boolean getTop3()
	{
		return top3;
	}
	public boolean getTop4()
	{
		return top4;
	}
	public boolean getMonoleg()
	{
		return monoleg;
	}
	
	public void setSlotX(int angle)
	{
		slotxangle = angle;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	public void setSlotY(int angle)
	{
		slotyangle = angle;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	public int getSlotX()
	{
		return slotxangle;
	}
	public int getSlotY()
	{
		return slotyangle;
	}
	
	public void setExposeSides(boolean expside1, boolean expside2, boolean expside3, boolean expside4) 
	{
		exps1 = expside1;
		exps2 = expside2;
		exps3 = expside3;
		exps4 = expside4;
	}
	public boolean getExpSide1()
	{
		return exps1;
	}
	public boolean getExpSide2()
	{
		return exps2;
	}
	public boolean getExpSide3()
	{
		return exps3;
	}
	public boolean getExpSide4()
	{
		return exps4;
	}
	
	public void setFrame(World world)
	{
		fauxFrame = new EntityItemFrame(world);
	}
	
	@Override
	public void update() 
	{
		if (!this.world.isRemote)
		{
			if (counter >= Config.mapUpdateRate)
			{
				counter = 1;
				if (hasMap)
				{
					if (this.fauxFrame == null)
					{
						if (this.world != null)
						{
							this.setFrame(this.world);
						}
						return;
					}
					List players = this.world.playerEntities;
					ItemStack mapstack = getStackInSlot(0);
					
					mapstack.setItemFrame(fauxFrame);
					MapData mapdata =Items.FILLED_MAP.getMapData(mapstack, this.world);
					
		            Iterator iterator = players.iterator();
		            while (iterator.hasNext())
		            {
		                 EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
		                Items.FILLED_MAP.updateMapData(getWorld(), entityplayermp, mapdata);
	                     Packet packet = mapdata.getMapPacket(mapstack, getWorld(), entityplayermp);
	                     if (packet != null)
	                     {
	                         entityplayermp.connection.sendPacket(packet);
	                     }
		            }
				}
			}
			else
			{
				counter++;
			}
		}
		
	}

	@Override
	public String getName() 
	{
		return BlockTable.name;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		if(stack != ItemStack.EMPTY)
		{
			if (stack.getItem() ==Items.FILLED_MAP)
			{
				hasMap = true;
			}
			else
			{
				hasMap = false;
			}
		}
		else
		{
			hasMap = false;
		}
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.leg1 = nbt.getBoolean("leg1");
		this.leg2 = nbt.getBoolean("leg2");
		this.leg3 = nbt.getBoolean("leg3");
		this.leg4 = nbt.getBoolean("leg4");
		this.top1 = nbt.getBoolean("top1");
		this.top2 = nbt.getBoolean("top2");
		this.top3 = nbt.getBoolean("top3");
		this.top4 = nbt.getBoolean("top4");
		this.exps1 = nbt.getBoolean("exposedside1");
		this.exps2 = nbt.getBoolean("exposedside2");
		this.exps3 = nbt.getBoolean("exposedside3");
		this.exps4 = nbt.getBoolean("exposedside4");
		this.monoleg = nbt.getBoolean("monoleg");
		this.slotxangle = nbt.getInteger("slotxangle");
		this.slotyangle = nbt.getInteger("slotyangle");
		this.hasMap = nbt.getBoolean("hasMap");
		this.isVanilla = nbt.getBoolean("isVanilla");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
		nbt.setBoolean("leg1", leg1);
    	nbt.setBoolean("leg2", leg2);
    	nbt.setBoolean("leg3", leg3);
    	nbt.setBoolean("leg4", leg4);
    	nbt.setBoolean("top1", top1);
    	nbt.setBoolean("top2", top2);
    	nbt.setBoolean("top3", top3);
    	nbt.setBoolean("top4", top4);
    	nbt.setBoolean("exposedside1", exps1);
    	nbt.setBoolean("exposedside2", exps2);
    	nbt.setBoolean("exposedside3", exps3);
    	nbt.setBoolean("exposedside4", exps4);
    	nbt.setBoolean("monoleg", monoleg);
    	nbt.setInteger("slotxangle", slotxangle);
    	nbt.setInteger("slotyangle", slotyangle);
    	nbt.setBoolean("hasMap", hasMap);
    	nbt.setBoolean("isVanilla", this.isVanilla);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
