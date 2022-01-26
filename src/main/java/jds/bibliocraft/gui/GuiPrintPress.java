package jds.bibliocraft.gui;

import jds.bibliocraft.containers.ContainerPrintPress;
import jds.bibliocraft.tileentities.TileEntityPrintPress;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.translation.I18n;

public class GuiPrintPress extends GuiContainer
{
	
	
	public GuiPrintPress(InventoryPlayer inventoryPlayer, TileEntityPrintPress printpress)
	{
		super(new ContainerPrintPress(inventoryPlayer, printpress));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString("Printing Press", 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{

	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
