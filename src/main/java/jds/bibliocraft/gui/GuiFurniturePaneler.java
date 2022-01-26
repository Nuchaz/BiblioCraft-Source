package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerFurniturePaneler;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFurniturePaneler extends GuiContainer
{

	public GuiFurniturePaneler(InventoryPlayer inventoryPlayer, TileEntityFurniturePaneler paneler)
	{
		super(new ContainerFurniturePaneler(inventoryPlayer, paneler));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2){}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.PANELER_GUI);
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
