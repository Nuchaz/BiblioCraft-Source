package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerDiscRack;
import jds.bibliocraft.tileentities.TileEntityDiscRack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.translation.I18n;

public class GuiDiscRack extends GuiContainer
{
	public GuiDiscRack(InventoryPlayer inventoryPlayer, TileEntityDiscRack discRack)
	{
		super(new ContainerDiscRack(inventoryPlayer, discRack));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString(I18n.translateToLocal("gui.discRack"), 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.DISCRACKGUI);
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
