package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButton32x14 extends GuiButton
{

    public boolean pressed;
    private int time = 0;

	public GuiButton32x14(int index, int posX, int posY)
	{
	    super(index, posX, posY, 32, 14, "");
	}
	
	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int par2, int par3, float thing)
	{
	    if (this.visible)
	    {
	        boolean var4 = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	       mc.getTextureManager().bindTexture(CommonProxy.STOCKROOMCATALOGGUI);
	        int var5 = 222;
	        int var6 = 110;
	
	        if (var4 && !this.pressed)
	        {
	        	var6 += 16;
	        }
	
	        if (this.pressed)
	        {
	            var6 += 32;
	            if (time > 10)
	            {
	            	time = 0;
	            	pressed = false;
	            }
	            else
	            {
	            	time++;
	            }
	        }
	
	        this.drawTexturedModalRect(this.x, this.y, var5, var6, 32, 14);
	    }
	}
}
