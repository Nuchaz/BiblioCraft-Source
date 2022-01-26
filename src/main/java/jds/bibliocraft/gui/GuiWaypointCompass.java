package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiWaypointCompass extends GuiScreen
{
	private int guiImageWidth = 256;
	private int guiImageHeight = 128;
	private GuiBiblioTextField textField;
	private GuiBiblioTextField textFieldX;
	private GuiBiblioTextField textFieldZ;
	private int xcoord;
	private int zcoord;
	private String wayPointName = " ";
	private GuiButton buttonAccept;
	private GuiButton buttonSetToCurrentLoc;
	private ItemStack compassStack;
	private ItemWaypointCompass compassItem;
	private int currxcoord;
	private int currzcoord;
	
	public GuiWaypointCompass(World world, EntityPlayer player, ItemStack compassstack)
	{
		this.compassStack = compassstack;
		this.compassItem = (ItemWaypointCompass)compassStack.getItem();
		NBTTagCompound tags = compassStack.getTagCompound();
		if (tags == null)
		{
			compassItem.createNewNBT(compassstack);
		}
		if (tags != null)
		{
			this.xcoord = tags.getInteger("XCoord");
			this.zcoord = tags.getInteger("ZCoord");
			this.wayPointName = tags.getString("WaypointName");
		}
		this.currxcoord = (int)player.posX;
		this.currzcoord = (int)player.posZ;
	}
	
	public void initNBTData()
	{
		
	}
	
	@Override
	public void initGui()
	{
    	super.initGui();
    	Keyboard.enableRepeatEvents(true);
    	buttonList.clear();
    	int widthRender = (this.width - this.guiImageWidth) / 2;
    	int heightRender = (this.height - this.guiImageHeight) / 2;
    	buttonList.add(this.buttonAccept = new GuiButton(0, widthRender+145, heightRender+68, 64, 20, I18n.translateToLocal("gui.mapWaypoint.accept"))); 
    	buttonList.add(this.buttonSetToCurrentLoc = new GuiButton(1, widthRender+47, heightRender+68, 64, 20, I18n.translateToLocal("gui.compass.currentlocation")));
    	this.textField = new GuiBiblioTextField(this.fontRenderer, widthRender+17, heightRender+28, 222, 12);
    	this.textFieldX = new GuiBiblioTextField(this.fontRenderer, widthRender+57, heightRender+52, 70, 12);
    	this.textFieldZ = new GuiBiblioTextField(this.fontRenderer, widthRender+155, heightRender+52, 70, 12);
    	this.textField.setEnableBackgroundDrawing(false);
    	this.textFieldX.setEnableBackgroundDrawing(false);
    	this.textFieldZ.setEnableBackgroundDrawing(false);
    	this.textField.setTextColor(0x404040);
    	this.textFieldX.setTextColor(0x404040);
    	this.textFieldZ.setTextColor(0x404040);
    	this.textField.setMaxStringLength(42);
    	this.textFieldX.setMaxStringLength(8);
    	this.textFieldZ.setMaxStringLength(8);
    	this.textField.setText(this.wayPointName);
    	String xText = ""+this.xcoord;
    	String zText = ""+this.zcoord;
    	this.textFieldX.setText(xText);
    	this.textFieldZ.setText(zText);
	}
	
    @Override
	public void drawScreen(int x, int y, float f)
    {
    	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    	this.mc.getTextureManager().bindTexture(CommonProxy.COMPASSGUI);
     	int widthRender = (this.width - this.guiImageWidth) / 2;
    	int heightRender = (this.height - this.guiImageHeight) / 2;
    	this.drawTexturedModalRect(widthRender, heightRender, 0, 0, this.guiImageWidth, this.guiImageHeight);
    	this.textField.drawTextBox();
    	this.textFieldX.drawTextBox();
    	this.textFieldZ.drawTextBox();
    	this.drawString(fontRenderer, I18n.translateToLocal("gui.compass.x"), widthRender+42, heightRender+52, -1);
    	this.drawString(fontRenderer, I18n.translateToLocal("gui.compass.z"), widthRender+140, heightRender+52, -1);
    	
    	//this.drawString(par1fontRenderer, par2Str, par3, par4, par5)
    	super.drawScreen(x, y, f);
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
    		//sendPacket(true);
    		String xtest = textFieldX.getText();
    		String ztest = textFieldZ.getText(); 
    		boolean packet = sendPacket(xtest, ztest, textField.getText());
    		if (packet)
    		{
    			Keyboard.enableRepeatEvents(false);
    			this.mc.displayGuiScreen((GuiScreen)null);
    		}
    	}
    	if (click.id == 1)
    	{
    		textFieldX.setText(""+this.currxcoord);
    		textFieldZ.setText(""+this.currzcoord);
    	}
    }
    
    private boolean sendPacket(String xcoord, String zcoord, String name)
    {
    	try
    	{
    		int newXcoord = Integer.parseInt(xcoord);
    		int newZcoord = Integer.parseInt(zcoord);
    		ItemStack updatedCompass = compassItem.writeNBT(compassStack, newXcoord, newZcoord, name);
    		if (updatedCompass != ItemStack.EMPTY)
    		{
		        try
		        {
					BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(updatedCompass, false));
			        // ByteBuf buffer = Unpooled.buffer();
			        // ByteBufUtils.writeItemStack(buffer, updatedCompass);
			        // BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
		            return true;
		        }
		        catch (Exception ex)
		        {
		            ex.printStackTrace();
		        }
    		}
    		
    	}
    	catch (NumberFormatException  ex)
    	{
    		//ex.printStackTrace();
    		System.out.println("X or Z value must be an Integer. Positive of Negitave. No letters or decimals accepted.");
    	}
    	return false;
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        try 
        {
			super.mouseClicked(par1, par2, par3);
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        this.textField.mouseClicked(par1, par2, par3);
        this.textFieldZ.mouseClicked(par1, par2, par3);
        this.textFieldX.mouseClicked(par1, par2, par3);
    }
    
    @Override
    protected void keyTyped(char par1, int par2)
    {
    	if (this.textField.textboxKeyTyped(par1, par2))
    	{
    		
    	}
    	else if (this.textFieldZ.textboxKeyTyped(par1, par2))
    	{
    		
    	}
    	else if (this.textFieldX.textboxKeyTyped(par1, par2))
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
}
