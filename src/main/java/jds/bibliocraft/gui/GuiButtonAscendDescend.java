package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonAscendDescend extends GuiButton
{
    /**
     * True for pointing right (next page), false for pointing left (previous page).
     */
	private boolean ascend = true;
    private boolean isSelected = false;

	public GuiButtonAscendDescend(int index, int xPos, int yPos, boolean acend)
	{
	    super(index, xPos, yPos, 12, 8, "");
	    this.ascend = acend;
	}
	
	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int par2, int par3, float thing)
	{
	    if (this.visible)
	    {
	        boolean isMouseHover = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        mc.getTextureManager().bindTexture(CommonProxy.STOCKROOMCATALOGGUI);
	        int xTexSheet = 224;
	        int yTexSheet = 92;
	        
	        if (this.isSelected)
	        {
	        	 this.drawTexturedModalRect(this.x-2, this.y-2, 235, 83, 12, 8);
	        }
	        else
	        {
	        	 this.drawTexturedModalRect(this.x-2, this.y-2, 222, 83, 12, 8);
	        }
	        
	        if (isMouseHover)
	        {
	        	yTexSheet += 5;
	        }
	
	        if (this.ascend)
	        {
	        	xTexSheet += 13;
	        }
	
	        this.drawTexturedModalRect(this.x, this.y, xTexSheet, yTexSheet, 8, 4);
	        
	        

	    }
	}
	
	public void setAscendOrDescend(boolean asnd)
	{
		this.ascend = asnd;
	}
	
	public void setIsSelected(boolean selected)
	{
		this.isSelected = selected;
	}
	
	public boolean getIsAscend()
	{
		return this.ascend;
	}
	
	public boolean getIsSelected()
	{
		return this.isSelected;
	}
}
