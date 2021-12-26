package jds.bibliocraft.gui;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerBookcase;
import jds.bibliocraft.tileentities.TileEntityBookcase;

import org.lwjgl.opengl.GL11;

import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.HopperContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.LanguageMap;

public class GuiBookcase extends ContainerScreen<ContainerBookcase> implements IHasContainer<ContainerBookcase>
{
	//   public HopperScreen(HopperContainer p_i51085_1_, PlayerInventory p_i51085_2_, ITextComponent p_i51085_3_) {
	public GuiBookcase (ContainerBookcase container, PlayerInventory inv, ITextComponent name)//(PlayerInventory inventoryPlayer, TileEntity bookcase)
	{
		//the container is instantiated and passed to the superclass for handling
		super (container, inv, name);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.font.drawString(LanguageMap.getInstance().translateKey("gui.bookcase"), 8, 6, 4210752);
		this.font.drawString(LanguageMap.getInstance().translateKey("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(CommonProxy.BOOKCASEGUI_PNG);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(x, y, 0, 0, xSize, ySize);
		
	}
	
	@Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
