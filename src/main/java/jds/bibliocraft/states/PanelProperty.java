package jds.bibliocraft.states;

import net.minecraftforge.common.property.IUnlistedProperty;

public enum PanelProperty implements IUnlistedProperty<PanelState>
{
	instance;
	
	@Override
	public String getName() 
	{
		return "TextureProperty";
	}

	@Override
	public boolean isValid(PanelState value) 
	{
		return value instanceof PanelState;
	}

	@Override
	public Class<PanelState> getType() 
	{
		return PanelState.class;
	}

	@Override
	public String valueToString(PanelState value) 
	{
		return value.toString();
	}
}
