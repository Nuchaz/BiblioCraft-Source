package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;

public class GuiButtonAddSubtract extends GuiButton
{
	private int displaySign = 0;
	private int pressCounter = 0;
	public boolean pressed = false;
	public float scale = 0.75f;
	public float antiscale = 1.0f / scale;
	/**
	 * symbol = 0 = pos, 1 = neg
	 * 
	 * @param buttonID
	 * @param xPos
	 * @param yPos
	 * @param symbol
	 */
	public GuiButtonAddSubtract(int buttonID, int xPos, int yPos, int symbol, float scaler)
	{
		super(buttonID, xPos, yPos, (int)(12.0f*scaler), (int)(12.0f*scaler), "");
		this.displaySign = symbol;
		this.scale = scaler;
		this.antiscale = 1.0f / scale;
	}
	
	@Override
    public void drawButton(Minecraft mc, int par1, int par2, float thing)
    {
        if (this.visible)
        {
        	
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(CommonProxy.PAINTINGGUI);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = par1 >= this.x && par2 >= this.y && par1 < this.x + this.width && par2 < this.y + this.height;
            int k = this.getHoverState(this.hovered) - 1;
            //System.out.println(k);
            // if k == 1, then standard position, if k == 2, mouse is hovering
            
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            GL11.glScalef(scale, scale, scale);
            if (pressed)
            {
            	this.drawTexturedModalRect((int)(this.x*antiscale), (int)(this.y*antiscale), 0, 229, (int)(this.width*antiscale)+1, (int)(this.height*antiscale)+1);
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
            else
            {
            	this.drawTexturedModalRect((int)(this.x*antiscale), (int)(this.y*antiscale), 0, 242, (int)(this.width*antiscale)+1, (int)(this.height*antiscale)+1);//
            }
            
            switch (this.displaySign)
            {
            	case 0:{this.drawTexturedModalRect((int)((this.x*antiscale))+3, (int)((this.y*antiscale))+3, 18+k*10, 243, 6, 6); break;}
            	case 1:{this.drawTexturedModalRect((int)((this.x*antiscale))+3, (int)((this.y*antiscale))+3, 18+k*10, 249, 6, 6); break;}
            }
            
            //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 229 + k * 13, this.width, this.height);
           // this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            
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
            GL11.glScalef(antiscale, antiscale, antiscale);
           // this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
        }
    }
}
