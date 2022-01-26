package jds.bibliocraft.states;

import net.minecraftforge.common.property.IUnlistedProperty;

public enum MetalTypeProperty implements IUnlistedProperty<MetalTypeState>
{
	instance;
	
	@Override
	public String getName() 
	{
		return "TextureProperty";
	}
	
	@Override
	public boolean isValid(MetalTypeState value) 
	{
		return value instanceof MetalTypeState;
	}

	@Override
	public Class<MetalTypeState> getType() 
	{
		return MetalTypeState.class;
	}

	@Override
	public String valueToString(MetalTypeState value) 
	{
		return value.toString();
	}
}
