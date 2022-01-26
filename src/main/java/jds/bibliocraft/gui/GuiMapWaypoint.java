package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioMapPin;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiMapWaypoint extends GuiScreen
{
	// gui image is at CommonProxy.MAPPINGUI
	private int guiImageWidth = 256;
	private int guiImageHeight = 128;
	private GuiBiblioTextField textField;
	private String text = " ";
	private int colorState = 1;
	private float xLoc;
	private float yLoc;
	private int xcoord;
	private int ycoord;
	private int zcoord;
	private boolean editing = false;
	private int waypointNumber;
	private String wayPointName = " ";
    private GuiButton buttonAccept;
    private GuiButton buttonCancel;
    private GuiButton buttonRemove;
	
	TileEntityMapFrame mapFrame;
	
	public GuiMapWaypoint(World world, EntityPlayer player, float xPin, float yPin, TileEntityMapFrame tile, int pinPoint)
	{
		this.xLoc = xPin;
		this.yLoc = yPin;
		this.mapFrame = tile;
		xcoord = tile.getPos().getX();
		ycoord = tile.getPos().getY();
		zcoord = tile.getPos().getZ();
		if (pinPoint == -1)
		{
			editing = false;
			waypointNumber = mapFrame.getPinXCoords().size()+1;
			wayPointName = I18n.translateToLocal("gui.mapWaypoint.waypoint")+" "+waypointNumber;
		}
		else
		{
			editing = true;
			waypointNumber = pinPoint;
			if (mapFrame.getPinNames().size() > 0)
			{
				System.out.println("attempting to get waypoint name");
				wayPointName = (String) mapFrame.getPinNames().get(waypointNumber);
				System.out.println(wayPointName);
			}
			if (mapFrame.getPinColors().size() > 0)
			{
				float cs = (Float) mapFrame.getPinColors().get(waypointNumber);
				colorState = (int)cs; // this line is crashing. Float cannot be case to Integer I get. getPinColors is a float?
			}
		}
	}
	
    @Override
	public void initGui()
	{
    	super.initGui();
    	buttonList.clear();
    	Keyboard.enableRepeatEvents(true);
    	int widthRender = (this.width - this.guiImageWidth) / 2;
    	int heightRender = (this.height - this.guiImageHeight) / 2;
    	buttonList.add(new GuiButtonClipboard(0, widthRender+120, heightRender+58, 16, 16, "", true));
    	buttonList.add(this.buttonAccept = new GuiButton(1, widthRender+166, heightRender+56, 52, 20, I18n.translateToLocal("gui.mapWaypoint.accept")));
    	if (editing)
    	{
    		buttonList.add(this.buttonRemove = new GuiButton(3, widthRender+38, heightRender+56, 52, 20, I18n.translateToLocal("gui.mapWaypoint.remove"))); //I18n.translateToLocal("gui.mapWaypointRemove")
    	}
    	else
    	{
    		buttonList.add(this.buttonCancel = new GuiButton(2, widthRender+38, heightRender+56, 52, 20, I18n.translateToLocal("gui.mapWaypoint.cancel"))); //I18n.translateToLocal("gui.mapWaypointCancel")
    	}
    	this.textField = new GuiBiblioTextField(this.fontRenderer, widthRender+17, heightRender+34, 222, 12);
    	this.textField.setEnableBackgroundDrawing(false);
    	this.textField.setTextColor(0x404040);
    	this.textField.setMaxStringLength(42);
    	this.textField.setText(wayPointName);
    	
    	
	}
    
    @Override
	public void drawScreen(int x, int y, float f)
    {
    	 //Minecraft mc =  Minecraft.getMinecraft();
    	 //this.buttonAccept.drawButton(mc, 10, 10);
    	 //this.buttonCancel.drawButton(mc, 50, 50);
    	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    	this. mc.getTextureManager().bindTexture(CommonProxy.MAPPINGUI);
    	 int widthRender = (this.width - this.guiImageWidth) / 2;
    	 int heightRender = (this.height - this.guiImageHeight) / 2;
    	 this.drawTexturedModalRect(widthRender, heightRender, 0, 0, this.guiImageWidth, this.guiImageHeight);
    	 this.textField.drawTextBox();
    	 super.drawScreen(x, y, f);
    	 //System.out.println(heightRender);
    	 switch (colorState)
    	 {
	 	 	case 0:
	 	 	{
	 	 		this.mc.getTextureManager().bindTexture(CommonProxy.BLACKWOOL);
	 	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
	 	 		break;
	 	 	}
    	 	case 1:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.REDWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 2:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.GREENWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 3:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.LIMEWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 4:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.BROWNWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 5:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.BLUEWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 6:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.CYANWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 7:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.LBLUEWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 8:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.PURPLEWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 9:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.MAGENTAWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 10:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.PINKWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 11:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.YELOOWWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 12:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.ORANGEWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 13:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.GRAYWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 14:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.LGRAYWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	case 15:
    	 	{
    	 		this.mc.getTextureManager().bindTexture(CommonProxy.WHITEWOOL);
    	 		this.drawTexturedModalRect(widthRender+120, heightRender+58, 0, 0, 16, 16);
    	 		break;
    	 	}
    	 	default: break;
    	 }
    }
    
    public void increaseColor()
    {
    	if (colorState >= 15)
    	{
    		colorState = 0;
    	}
    	else
    	{
    		colorState++;
    	}
    }
    public void decreaseColor()
    {
    	if (colorState <= 0)
    	{
    		colorState = 15;
    	}
    	else
    	{
    		colorState--;
    	}
    }
    
    @Override
	public void updateScreen()
    {
        super.updateScreen();
    }
    
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	if (click.id == 0)
    	{
    		//increaseColor();
    	}
    	if (click.id == 1)
    	{
    		//System.out.println("accept pin");
    		sendPacket(false);
    		Keyboard.enableRepeatEvents(false);
    		this.mc.displayGuiScreen((GuiScreen)null);
    	}
    	if (click.id == 2)
    	{
    		//System.out.println("cancel dialog");
    		Keyboard.enableRepeatEvents(false);
    		this.mc.displayGuiScreen((GuiScreen)null);
    		//this.onGuiClosed();
    	}
    	if (click.id == 3)
    	{
    		//System.out.println("remove pin");
    		sendPacket(true);
    		Keyboard.enableRepeatEvents(false);
    		this.mc.displayGuiScreen((GuiScreen)null);
    	}
    }
    
    public void sendPacket(boolean removePin)
    {
        // ByteBuf buffer = Unpooled.buffer();
        try
        {
			BiblioNetworking.INSTANCE.sendToServer(new BiblioMapPin(new BlockPos(xcoord, ycoord, zcoord), xLoc, yLoc, textField.getText(), colorState, waypointNumber, removePin, editing));
        	// buffer.writeInt(xcoord);
        	// buffer.writeInt(ycoord);
        	// buffer.writeInt(zcoord);
        	// buffer.writeFloat(xLoc);
        	// buffer.writeFloat(yLoc);
        	// ByteBufUtils.writeUTF8String(buffer, textField.getText());
        	// buffer.writeInt(colorState);
        	// buffer.writeInt(waypointNumber);
        	// buffer.writeBoolean(removePin);
        	// buffer.writeBoolean(editing);
        	
        	// BiblioCraft.ch_BiblioMapPin.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMapPin"));  
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onGuiClosed()
    {
    	
    }
    
    @Override
    protected void mouseClicked(int x, int y, int click)
    {
        try 
        {
			super.mouseClicked(x, y, click);
		}
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        this.textField.mouseClicked(x, y, click);
 		int w = (width - 256) / 2;
 		int h = (height - 128) / 2;
 		if (x >= w + 120 && x <= w + 135 && y >= h + 58 && y <= h + 73)
 		{
 			if (click == 0)
 			{
 				increaseColor();
 			}
 			else if (click == 1)
 			{
 				decreaseColor();
 			}
 		}
    }
    
    @Override
    protected void keyTyped(char par1, int par2)
    {
    	if (this.textField.textboxKeyTyped(par1, par2))
    	{
    		
    	}
    	else
    	{
    		try 
    		{
				super.keyTyped(par1, par2);
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    	}
    }
    
    private void updateButtons()
    {
    	
    }
}
