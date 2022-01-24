package jds.bibliocraft.states;

import net.minecraftforge.common.property.IUnlistedProperty;

public enum TextureProperty implements IUnlistedProperty<TextureState>
{
	instance;

	@Override
	public String getName() 
	{
		return "TextureProperty";
	}

	@Override
	public boolean isValid(TextureState value) 
	{
		return value instanceof TextureState;
	}

	@Override
	public Class<TextureState> getType() 
	{
		return TextureState.class;
	}

	@Override
	public String valueToString(TextureState value) 
	{
		return value.toString();
	}
	
}
