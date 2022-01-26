package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class GuiRedstoneBook extends GuiScreen
{
	public static final ResourceLocation book_png = new ResourceLocation("textures/gui/book.png");
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    
    private static String titleText = I18n.translateToLocal("gui.redbook.title");
    private static String description =  I18n.translateToLocal("gui.redbook.description.p1");
    private static String description2 =  I18n.translateToLocal("gui.redbook.description.p2");
    private GuiButton buttonAccept;
    private GuiButton buttonCancel;
    private GuiBiblioTextField titleField;
    private ItemStack book;
    private String title = I18n.translateToLocal("item.BiblioRedBook.name");
    
    //private int width = 0;
    ///private int height = 0;
    
    public GuiRedstoneBook(ItemStack redstonebook)
    {
    	book = redstonebook;
    	getNBTTitle();
    }
    
    public void getNBTTitle()
    {
    	NBTTagCompound nbt = book.getTagCompound();
    	if (nbt != null)
    	{
    		NBTTagCompound display = nbt.getCompoundTag("display");
    		if (display != null)
    		{
    			title = display.getString("Name");
    			//System.out.println(title.length());
    			if (title.length() > 0)
    			{
    				return;
    			}
    		}
    	}
    }
    
    public void setNBTTitle()
    {
    	NBTTagCompound nbt = book.getTagCompound();
    	if (nbt != null)
    	{
			NBTTagCompound display = new NBTTagCompound();
			display.setString("Name", TextFormatting.WHITE+titleField.getText());
    		nbt.setTag("display", display);
    		book.setTagCompound(nbt);
    	}
    }
    
    @Override
    public void initGui()
    {
    	super.initGui();
    	buttonList.clear();
    	Keyboard.enableRepeatEvents(true);
    	int width = (this.width) / 2;
    	int height = (this.height) / 2;
    	buttonList.add(this.buttonAccept = new GuiButton(0, width+10, height+54, 42, 20, I18n.translateToLocal("book.save")));
    	buttonList.add(this.buttonCancel = new GuiButton(1, width-60, height+54, 42, 20, I18n.translateToLocal("book.cancel")));
    	
    	this.titleField = new GuiBiblioTextField(this.fontRenderer, width-64, height-64, 120, 12);
    	//this.titleField.setEnableBackgroundDrawing(false);
    	this.titleField.setTextColor(0xFFFFFF);
    	this.titleField.setMaxStringLength(42);
    	this.titleField.setText(this.title);
    }
    
	@Override
	public void drawScreen(int x, int y, float f)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    	int width = (this.width - this.bookImageWidth) / 2;
    	int height = (this.height - this.bookImageHeight) / 2;
		this. mc.getTextureManager().bindTexture(book_png);
		this.drawTexturedModalRect(width, height, 0, 0, this.bookImageWidth, this.bookImageHeight);
		this.titleField.drawTextBox();
		fontRenderer.drawString(titleText, width+35, height+22, 0x000000, false);
		//fontRenderer.drawString(this.description, width+35, height+52, 0x000000, false);
		super.drawScreen(x, y, f);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		fontRenderer.drawSplitString(this.description, (width*2)+70, (height*2)+104, 230, 0x000000);
		fontRenderer.drawSplitString(this.description2, (width*2)+70, (height*2)+174, 230, 0x000000);
		//mc.renderGlobal.
	}
	
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	if (click.id == 0)
    	{
    		setNBTTitle();
    		sendPacket();
    		this.mc.player.closeScreen();
    	}
    	if (click.id == 1)
    	{
    		this.mc.player.closeScreen();
    	}
    }
    
    public void sendPacket()
    {
		BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(book, false));
    	// ByteBuf buffer = Unpooled.buffer();
    	// ByteBufUtils.writeItemStack(buffer, book);
    	// BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
    }
    
	@Override
    protected void mouseClicked(int left, int top, int par3)
    {
		try 
		{
			super.mouseClicked(left, top, par3);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.titleField.mouseClicked(left, top, par3);
    }
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
        if (par2 == 1)
        {
            this.mc.player.closeScreen();
        }
        if (this.titleField.isFocused())
        {
        	this.titleField.textboxKeyTyped(par1, par2);
        }
        else if (par2 == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            this.mc.player.closeScreen();
        }
	}
	
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    }
	
	
}
