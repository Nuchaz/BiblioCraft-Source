package jds.bibliocraft.states;

public class PanelState 
{
	protected String textureString = "none";
	
	public PanelState(String panelTextureString)
	{
		this.textureString = panelTextureString;
	}

	public String getTextureString()
	{
		return this.textureString;
	}
	
	@Override
	public String toString()
	{
		return this.textureString;
	}
}
