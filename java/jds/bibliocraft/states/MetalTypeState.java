package jds.bibliocraft.states;

import jds.bibliocraft.helpers.EnumMetalType;

public class MetalTypeState 
{
	protected EnumMetalType metal = EnumMetalType.GOLD;
	
	public MetalTypeState(EnumMetalType setMetal)
	{
		this.metal = setMetal;
	}
	
	public EnumMetalType getMetalType()
	{
		return this.metal;
	}
	
	@Override
	public String toString()
	{
		return this.metal.name();
	}
}
