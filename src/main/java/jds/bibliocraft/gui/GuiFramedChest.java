package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.containers.ContainerFramedChest;
import jds.bibliocraft.tileentities.TileEntityFramedChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiFramedChest extends GuiContainer
{
	private static final ResourceLocation chest_texture = new ResourceLocation("textures/gui/container/generic_54.png");
	private int inventoryRows = 3;
	
	TileEntityFramedChest mainChest;
	
	public GuiFramedChest(InventoryPlayer inventoryPlayer, TileEntityFramedChest tile, TileEntityFramedChest tile2)
	{
		super(new ContainerFramedChest(inventoryPlayer, tile, tile2));
		if (tile2 != null && tile2.getIsDouble())
		{
			this.inventoryRows = 6;
		}
		mainChest = tile;
		this.ySize = 114 + this.inventoryRows * 18;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString(I18n.translateToLocal(this.mainChest.getName()), 6, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 6, ySize - 94, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(chest_texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(k, l + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);

	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
