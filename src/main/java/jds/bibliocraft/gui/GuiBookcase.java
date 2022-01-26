package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerBookcase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.translation.I18n;

public class GuiBookcase extends GuiContainer
{
	
	public GuiBookcase (InventoryPlayer inventoryPlayer, TileEntity bookcase)
	{
		//the container is instantiated and passed to the superclass for handling
		super (new ContainerBookcase(inventoryPlayer, bookcase));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString(I18n.translateToLocal("gui.bookcase"), 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.BOOKCASEGUI_PNG);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
