package jds.bibliocraft.entity;

import jds.bibliocraft.tileentities.TileEntitySeat;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySeat extends Entity
{
	int counter = 0;
	private TileEntitySeat seatTile;
	public static final String name = "SeatEntity";
	
	public EntitySeat(World world)
	{
		super(world);
		this.noClip = true;
		this.setSilent(true);
	}
	
	public EntitySeat(World world, double i, double j, double k, TileEntitySeat tile)
	{
		this(world);
		this.setPosition(i+0.5d, j, k+0.5d);
		seatTile = tile;
	}

	@Override
    public double getMountedYOffset()
    {
        return (double)this.height * 0.0D - 0.48D;
    }
	
	
	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }
	
	@Override
    public void onEntityUpdate()
    {
    	if (counter > 10)
    	{
    		
	    	if (this.getPassengers() != null && this.getPassengers().size() > 0)
	    	{
	    		counter = 0;
	    	}
	    	else
	    	{
	    		if (this.seatTile == null)
	    		{
	    			TileEntity tile = this.world.getTileEntity(new BlockPos((int)(this.lastTickPosX-0.5), (int)this.lastTickPosY-1, (int)(this.lastTickPosZ-0.5)));
	    			if (tile != null && tile instanceof TileEntitySeat)
	    			{
	    				this.seatTile = (TileEntitySeat)tile;
	    			}
	    		}
	    		
	    		if (seatTile != null)
	    		{
	    			seatTile.setSitter(false);
	    		}
	    		this.isDead = true;
	    	}
    	}
    	else
    	{
    		counter++;
    	}
    	//super.onEntityUpdate();
    }
    
	
	@Override
	protected void entityInit(){}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) { }

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) { }

}
