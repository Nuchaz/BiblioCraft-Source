package jds.bibliocraft.helpers;

import net.minecraft.util.EnumFacing;

public class SeatHelper
{
	private boolean southConnect, westConnect, northConnect, eastConnect;
	public SeatHelper(boolean south, boolean west, boolean north, boolean east, EnumFacing seatAngle)
	{
		this.southConnect = south;
		this.westConnect = west;
		this.northConnect = north;
		this.eastConnect = east;
		adjustBooleanConnectsBasedOnAngle(seatAngle);
	}
	
	public boolean getSouthConnect()
	{
		return this.southConnect;
	}
	
	public boolean getWestConnect()
	{
		return this.westConnect;
	}
	
	public boolean getNorthConnect()
	{
		return this.northConnect;
	}
	
	public boolean getEastConnect()
	{
		return this.eastConnect;
	}
	
	private void adjustBooleanConnectsBasedOnAngle(EnumFacing angle)
	{
		// NORTH is default location
		boolean north = this.northConnect;
		boolean south = this.southConnect;
		boolean east = this.eastConnect;
		boolean west = this.westConnect;
		
		switch (angle)
		{
			case SOUTH:
			{
				this.southConnect = north;
				this.northConnect = south;
				this.eastConnect = west;
				this.westConnect = east;
				break;
			}
			case WEST:
			{
				this.southConnect = east;
				this.northConnect = west;
				this.eastConnect = north;
				this.westConnect = south;
				break;
			}
			case EAST:
			{
				this.southConnect = west;
				this.northConnect = east;
				this.eastConnect = south;
				this.westConnect = north;
				break;
			}
			default: break;
		}
	}
}
