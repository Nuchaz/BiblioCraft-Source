package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonClipboard extends GuiButton
{

	private boolean centered;
	
	public GuiButtonClipboard(int par1, int par2, int par3, int par4, int par5, String par6, boolean center) 
	{
		super(par1, par2, par3, par4, par5, par6);
		centered = center;
	}
	
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3, float thing)
    {
        if (this.enabled)
        {
            FontRenderer fontrenderer = par1Minecraft.fontRenderer;
            int l = 14737632;

            if (!this.enabled)
            {
                l = -6250336;
            }
            else if (this.hovered)
            {
                l = 16777120;
            }

            
            if (!centered)
            {
            	float scalediff = 0.8F;
            	float widthscaled = ((this.x + this.width / 2)-54)*(1.0F / scalediff);
            	int widthd = (int)widthscaled;
            	float heightscaled = ((this.y + (this.height - 8) / 2)+3)*(1.0F / scalediff);
            	int heightd = (int)heightscaled;
            	GL11.glPushMatrix();
            	GL11.glScalef(scalediff, scalediff, scalediff);
            	fontrenderer.drawString(this.displayString, widthd, heightd, 0x000000, false);
            	GL11.glPopMatrix();
            }
            else
            {
            	float scalediff = 0.8F;
            	float widthscaled = (((this.x + this.width / 2)-(this.displayString.length()/2)*(5*scalediff))-10)*(1.0F / scalediff);
            	int widthd = (int)widthscaled;
            	float heightscaled = ((this.y + (this.height - 8) / 2)+2)*(1.0F / scalediff);
            	int heightd = (int)heightscaled;
            	GL11.glPushMatrix();
            	GL11.glScalef(scalediff, scalediff, scalediff);
            	fontrenderer.drawString(this.displayString, widthd, heightd, 0x000000, false);
            	GL11.glPopMatrix();
            }
        }
        
    }

}
