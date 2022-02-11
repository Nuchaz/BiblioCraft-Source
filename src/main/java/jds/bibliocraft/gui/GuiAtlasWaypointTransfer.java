package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioAtlasWPT;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiAtlasWaypointTransfer extends GuiScreen
{
	private static int guiWidth = 256;
	private static int guiHeight = 96;
	private ItemStack atlasStack;
	private EntityPlayer player;
	private World world;
	private GuiButton bCancel;
	private GuiButtonAtlasTransfer bUpArrow;
	private GuiButtonAtlasTransfer bDownArrow;
	private TileEntityMapFrame frameTile;
	
	public GuiAtlasWaypointTransfer(World worldy, EntityPlayer playa, ItemStack atlas, TileEntityMapFrame tile)
	{
		this.atlasStack = atlas;
		this.world = worldy;
		this.player = playa;
		this.frameTile = tile;
		this.width = guiWidth;
		this.height = guiHeight;
	}
	
    @Override
    public void initGui()
    {
    	super.initGui();
		int w = (width - this.guiWidth) / 2;
		int h = (height - this.guiHeight) / 2;
		this.buttonList.clear();
		this.buttonList.add(bCancel = new GuiButton(0, w+220, h+80, 40, 20, I18n.translateToLocal("gui.atlas.transfer.cancel"))); 
		this.buttonList.add(bUpArrow = new GuiButtonAtlasTransfer(1, w+20, h+13, I18n.translateToLocal("gui.atlas.transfer.from"), false)); 
		this.buttonList.add(bDownArrow = new GuiButtonAtlasTransfer(2, w+20, h+52, I18n.translateToLocal("gui.atlas.transfer.to"), true)); 
    }
    
    @Override
    protected void actionPerformed(GuiButton click)
    {
    	switch (click.id)
    	{
    		case 1:
    		{
    			sendTransferPacket(true);
    			bUpArrow.pressed = true;
    			break;
    		}
    		case 2:
    		{
    			sendTransferPacket(false);
    			bDownArrow.pressed = true;
    			break;
    		}
    	}
    	this.mc.displayGuiScreen((GuiScreen)null);
    }
    
    private void sendTransferPacket(boolean toMapFrame)
    {
		System.out.println("balls");
		// something here isn't working right. 
    	// ByteBuf buffer = Unpooled.buffer();
    	// buffer.writeBoolean(toMapFrame);
    	// buffer.writeInt(frameTile.getPos().getX());
    	// buffer.writeInt(frameTile.getPos().getY());
    	// buffer.writeInt(frameTile.getPos().getZ());
    	// ByteBufUtils.writeItemStack(buffer, this.atlasStack);
		BiblioNetworking.INSTANCE.sendToServer(new BiblioAtlasWPT(toMapFrame, frameTile.getPos(), this.atlasStack));
    	// BiblioCraft.ch_BiblioAtlasWPT.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlasWPT"));
    }
   
    @Override
    protected void mouseClicked(int mousex, int mousey, int click)
    {
    	try 
    	{
			super.mouseClicked(mousex, mousey, click);
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    @Override
    public void onGuiClosed()
    {
    	
    }
    
    @Override
    public void drawDefaultBackground()
    {
        //this.drawWorldBackground(0);
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
	@Override
	public void drawScreen(int mousex, int mousey, float f)
	{
		int w = (this.width - this.guiWidth) / 2;
		int h = (this.height - this.guiHeight) / 2;
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(CommonProxy.ATLASGUITRANSFER); 
		this.drawTexturedModalRect(w, h, 0, 0, guiWidth, guiHeight);		
		this.bCancel.drawButton(mc, 0, 0, 0f);
		//this.bCancel.drawButtonForegroundLayer(mouseX, mouseY);
		super.drawScreen(mousex, mousey, f);
		
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.transfer"), w+80, h+43, 4210752, false);
		
	}
}
