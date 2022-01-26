package jds.bibliocraft.tileentities;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.blocks.BlockClock;
import jds.bibliocraft.helpers.EnumVertPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityClock extends BiblioTileEntity implements ITickable 
{

	//private int clockType = 0; // 0 = small, 1 = large bottom, 2, = large top
	// replace clockType with the vertPosition. We can do wall = small clock, floor = bottom large, and ceiling = top large
	public int secondCount = 0;
	public int hourCount = 0;
	public float pendulumCount = 0.0f;
	public int activityCount = 0;
	public boolean chimePerformed = false;
	public int chimeLastPerformed = -1;
	public boolean redstonePerformed = false;
	public int redstoneLastPerformed = -1;
	public boolean redstoneActive = false;
	public int redstoneCount = 0;
	public boolean tickSound = true;
	public boolean chimes = false;
	public boolean redstone = false;
	public int[] chimeSettings = new int[48];
	public int[] redstoneSettings = new int[48];
	public boolean isRedstonePulse = true;
	
	public TileEntityClock()
	{
		super(0, true);
	}
	

	
	public void setSettingFromGui(int[] dings, int[] powers, boolean tickToggle, boolean chimeToggle, boolean redstoneToggle, boolean ispulsing)
	{
		this.chimeSettings = dings;
		this.redstoneSettings = powers;
		this.tickSound = tickToggle;
		this.chimes = chimeToggle;
		this.redstone = redstoneToggle;
		this.isRedstonePulse = ispulsing;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	
	@Override
	public void loadCustomNBTData(NBTTagCompound nbt)
	{
		//super.readFromNBT(nbt);
		this.chimeSettings = nbt.getIntArray("chimeSettings");
		this.redstoneSettings = nbt.getIntArray("redstoneSettings");
		this.tickSound = nbt.getBoolean("tickSound");
		this.chimes = nbt.getBoolean("chimeSound");
		this.redstone = nbt.getBoolean("toggleRedstone");
		this.isRedstonePulse = nbt.getBoolean("redstonePulse");
	}
	
	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt)
    {
    	//super.writeToNBT(nbt);  	
    	nbt.setIntArray("chimeSettings", this.chimeSettings);
    	nbt.setIntArray("redstoneSettings", this.redstoneSettings);
    	nbt.setBoolean("tickSound", this.tickSound);
    	nbt.setBoolean("chimeSound", this.chimes);
    	nbt.setBoolean("toggleRedstone", this.redstone);
    	nbt.setBoolean("redstonePulse", this.isRedstonePulse);
    	return nbt;
    }
	
	
    
    @Override
    public void update()
    {
    	if (this.getVertPosition() != EnumVertPosition.FLOOR)
    	{
    		long time = this.world.getWorldTime();
    		this.activityCount = (int) ((time%24000)/500)+24;
	    	if (activityCount >= 48)
	    	{
	    		this.activityCount -= 48;
	    	}
	    	
	    	if (this.world.isRemote)
	    	{
		    	this.hourCount = (int) ((time%24000)/1000)*15;
		    	this.secondCount = (int) ((time%24000)/16.667)*6;
		    	double coscount = (2*Math.PI*((time%40)/40.0));
				if (this.getVertPosition() == EnumVertPosition.WALL)
				{
					this.pendulumCount = (float) ((Math.PI/18)*Math.cos(coscount)*(360/Math.PI));
				}
				
				if (this.getVertPosition() == EnumVertPosition.CEILING)
				{	
					this.pendulumCount = (float) ((Math.PI/72)*Math.cos(coscount)*(360/Math.PI));
				}
				
				if (this.tickSound)
				{
					if (time%40 == 37)
					{
						this.getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_CLOCK_TOCK, SoundCategory.BLOCKS, 0.4F, 1.0F, false);
					}
					else if (time%40 == 17)
					{
						this.getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_CLOCK_TICK, SoundCategory.BLOCKS, 0.4F, 1.0F, false);
					}
				}
				
				if (this.chimes)
				{
					if (this.chimePerformed && this.chimeLastPerformed != this.activityCount)
					{
						this.chimePerformed = false;
						this.chimeLastPerformed = -1;
					}
					
					if (!this.chimePerformed && this.chimeSettings[activityCount] == 1)
					{
						this.chimePerformed = true;
						this.chimeLastPerformed = activityCount;
						this.getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_CLOCK_CHIME, SoundCategory.BLOCKS, 0.4F, 1.0F, false);
					}
				}
				
	    	}
	    	else
	    	{
				if (this.redstone)
				{
					this.redstoneAlgo();
				}
	    	}
    	}
    }
    
    // This only runs on the floor piece?
    // TODO it would be great if I should enable this to follow the minute hand instead of the hour hand
    public void redstoneAlgo()
    {
		if (this.redstonePerformed && this.redstoneLastPerformed != this.activityCount)
		{
			this.redstonePerformed = false;
			this.redstoneLastPerformed = -1;
			//this.setActivateRedstone(false);
		}
		
		if (!this.redstonePerformed && this.redstoneSettings[activityCount] == 1)
		{
			if (this.isRedstonePulse)
			{
				this.redstonePerformed = true;
				this.redstoneLastPerformed = this.activityCount;
			}
			
			if (!redstoneActive)
			{
				this.setActivateRedstone(true);
				if (this.getVertPosition() == EnumVertPosition.CEILING)
				{
					TileEntity tile = this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()));
					if (tile != null && tile instanceof TileEntityClock)
					{
						TileEntityClock bottomClock = (TileEntityClock)tile;
						bottomClock.setActivateRedstone(true);
					}
				}
			}
		}
		
		if (!this.redstonePerformed && this.redstoneSettings[activityCount] == 0 && !isRedstonePulse)
		{
			if (redstoneActive)
			{
				this.setActivateRedstone(false);
				if (this.getVertPosition() == EnumVertPosition.CEILING)
				{
					TileEntity tile = this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()));
					if (tile != null && tile instanceof TileEntityClock)
					{
						TileEntityClock bottomClock = (TileEntityClock)tile;
						bottomClock.setActivateRedstone(false);
					}
				}
			}
		}
		
		if (this.redstoneActive)
		{
			if (this.redstonePerformed && this.redstoneCount < 40) // this determines the length of the redstone pulse
			{
				this.redstoneCount++;
			}
			else if (this.redstonePerformed)
			{
				this.redstoneCount = 0;
				//this.redstoneActive = false;
				this.setActivateRedstone(false);
				if (this.getVertPosition() == EnumVertPosition.CEILING)
				{
					TileEntity tile = this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()));
					if (tile != null && tile instanceof TileEntityClock)
					{
						TileEntityClock bottomClock = (TileEntityClock)tile;
						bottomClock.setActivateRedstone(false);
					}
				}
			}
		}
    }
    
    public void setActivateRedstone(boolean toggle)
    {
    	//System.out.println("set activated redstone  "+toggle);
    	this.redstoneActive = toggle;
    	updateSurroundingBlocks(BlockClock.instance);
    }



	@Override
	public String getName() 
	{
		return BlockClock.name;
	}



	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) { }


	@Override
	public int getInventoryStackLimit() 
	{
		return 0;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
