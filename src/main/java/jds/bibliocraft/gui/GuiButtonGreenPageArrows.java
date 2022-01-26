package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonGreenPageArrows extends GuiButton
{
    /**
     * True for pointing right (next page), false for pointing left (previous page).
     */
    private final boolean nextPage;

	public GuiButtonGreenPageArrows(int par1, int par2, int par3, boolean par4)
	{
	    super(par1, par2, par3, 18, 12, "");
	    this.nextPage = par4;
	}
	
	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3, float thing)
	{
	    if (this.visible)
	    {
	        boolean var4 = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        par1Minecraft.getTextureManager().bindTexture(CommonProxy.STOCKROOMCATALOGGUI);
	        int var5 = 222;
	        int var6 = 0;
	
	        if (var4)
	        {
	        	var6 += 26;
	        }
	
	        if (!this.nextPage)
	        {
	            var6 += 13;
	        }
	
	        this.drawTexturedModalRect(this.x, this.y, var5, var6, 23, 13);
	    }
	}
}