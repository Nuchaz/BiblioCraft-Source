package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;

public class GuiButtonAtlasControls extends GuiButton
{

	public boolean toggled = false;
	private int type = 1;
	public int mouseMode = 0;
	public boolean hovered = false;
	
	public GuiButtonAtlasControls(int buttonID, int xPos, int yPos, int symbol)
	{
		super(buttonID, xPos, yPos, 18, 18, "");
		this.type = symbol;
	}
	
	@Override
	public void drawButton(Minecraft mc, int par1, int par2, float thing)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(CommonProxy.ATLASGUIBUTTONS);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = par1 >= this.x && par2 >= this.y && par1 < this.x + this.width && par2 < this.y + this.height;
            int k = this.getHoverState(this.hovered) - 1;
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            if (mouseMode == this.id)
            {
            	this.drawTexturedModalRect(this.x, this.y, 20, 39, this.width, this.height);
            }
            else
            {
            	this.drawTexturedModalRect(this.x, this.y, 0, 39, this.width, this.height);
            }
            switch (type)
            {
            	case 1:{this.drawTexturedModalRect(this.x, this.y, 0, 60, this.width, this.height); break;}
            	case 2:{this.drawTexturedModalRect(this.x, this.y, 20, 60, this.width, this.height); break;}
            	case 3:{this.drawTexturedModalRect(this.x, this.y, 40, 60, this.width, this.height); break;}
            	case 4:{this.drawTexturedModalRect(this.x, this.y, 60, 60, this.width, this.height); break;}
            	case 5:{this.drawTexturedModalRect(this.x, this.y, 80, 60, this.width, this.height); break;}
            	case 6:{this.drawTexturedModalRect(this.x, this.y, 100, 60, this.width, this.height); break;}
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
        }
    }

}
