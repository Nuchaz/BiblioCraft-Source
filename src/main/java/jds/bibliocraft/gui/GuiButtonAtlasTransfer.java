package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;

public class GuiButtonAtlasTransfer extends GuiButton
{
	
	private boolean downArrow = false;
	public boolean hovered = false;
	public boolean pressed = false;
	private int pressCounter = 0;
	
	public GuiButtonAtlasTransfer(int buttonID, int xPos, int yPos, String text, boolean isArrowDown)
	{
		super(buttonID, xPos, yPos, 216, 30, text);
		downArrow = isArrowDown;
	}
	
	@Override
	public void drawButton(Minecraft mc, int par1, int par2, float thing)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(CommonProxy.ATLASGUITRANSFER);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = par1 >= this.x && par2 >= this.y && par1 < this.x + this.width && par2 < this.y + this.height;
            int k = this.getHoverState(this.hovered) - 1;
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            if (downArrow)
            {
            	this.drawTexturedModalRect(this.x, this.y, 0, 196, this.width, this.height);
            }
            else
            {
            	this.drawTexturedModalRect(this.x, this.y, 0, 164, this.width, this.height);
            }
            
            if (pressed)
            {
            	if (downArrow)
            	{
            		this.drawTexturedModalRect(this.x, this.y, 0, 132, this.width, this.height);
            	}
            	else
            	{
            		this.drawTexturedModalRect(this.x, this.y, 0, 100, this.width, this.height);
            	}
            	if (this.pressCounter >  10)
            	{
            		this.pressed = false;
            		this.pressCounter = 0;
            	}
            	else
            	{
            		this.pressCounter++;
            	}
            }

            if (k == 1)
            {
            	//System.out.println(this.type);
            	this.hovered = true;
            }
            else
            {
            	this.hovered = false;
            }
  
            this.mouseDragged(mc, par1, par2);
            int l = 14737632;

            if (packedFGColour != 0)
            {
                l = packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.hovered)
            {
                l = 16777120;
            }

            int offset = 0;
            if (downArrow)
            {
            	offset = -6;
            }
            else
            {
            	offset = 6;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + offset + (this.height - 8) / 2, l);
        }
    }
}
