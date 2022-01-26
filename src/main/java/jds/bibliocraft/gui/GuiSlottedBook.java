package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerSlottedBook;
import jds.bibliocraft.items.ItemSlottedBook;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioMCBEdit;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class GuiSlottedBook extends GuiContainer
{
	private GuiBiblioTextField titleField;
	private GuiBiblioTextField line1Field;
	private GuiBiblioTextField line2Field;
	private GuiBiblioTextField line3Field;
	private GuiBiblioTextField line4Field;
	private GuiBiblioTextField line5Field;
    private GuiButton buttonAccept;
    private GuiButton buttonCancel;
    private ItemStack book = ItemStack.EMPTY;
    private String title = I18n.translateToLocal("book.title"); 
    private String line1 = I18n.translateToLocal("book.description"); 
    private String line2 = "";
    private String line3 = "";
    private String line4 = "";
    private String line5 = "";
    private static ContainerSlottedBook container;
    
    private boolean inHand = true;
    private int tilex = 0;
    private int tiley = 0;
    private int tilez = 0;
    private EntityPlayer player;
    
	public GuiSlottedBook(InventoryPlayer inventoryPlayer, ItemStack handStack, boolean inhand, int tx, int ty, int tz)
	{
		super(container = new ContainerSlottedBook(inventoryPlayer));
		if (inventoryPlayer.getCurrentItem() != ItemStack.EMPTY)
		{
			if (inventoryPlayer.getCurrentItem().getItem() instanceof ItemSlottedBook)
			{
				this.book = inventoryPlayer.getCurrentItem();
				getNBTTitleAndLines();
			}
		}
		this.player = inventoryPlayer.player;
		inHand = inhand;
		tilex = tx;
		tiley = ty;
		tilez = tz;
		// 
	}
	
    public void getNBTTitleAndLines()
    {
    	NBTTagCompound nbt = book.getTagCompound();
    	if (nbt != null)
    	{
    		boolean initilized = nbt.getBoolean("init");
    		//System.out.println(firstload);
    		if (initilized)
    		{
	    		NBTTagCompound display = nbt.getCompoundTag("display");
	    		if (display != null)
	    		{
	    			title = display.getString("Name");
	    			title = title.replace("\u00a7"+"f", "");
	    			//System.out.println("next");
	    		}
	    		NBTTagCompound lines = nbt.getCompoundTag("lines");
	    		if (lines != null)
	    		{
	    			line1 = lines.getString("line1");
	    			line2 = lines.getString("line2");
	    			line3 = lines.getString("line3");
	    			line4 = lines.getString("line4");
	    			line5 = lines.getString("line5");
	    		}
    		}
    		else
    		{
    		     title = I18n.translateToLocal("book.title");
    		     line1 = I18n.translateToLocal("book.description");
    		     nbt.setBoolean("init", true);
    		}
    	}
    }
    
    public void setNBTTitleAndLines()
    {
    	NBTTagCompound nbt = book.getTagCompound();
    	if (nbt == null)
    	{
    		nbt = new NBTTagCompound();
    	}
    	if (nbt != null)
    	{
			NBTTagCompound display = new NBTTagCompound();
			display.setString("Name", TextFormatting.WHITE+titleField.getText()); // TextFormatting.WHITE+
			NBTTagCompound lines = new NBTTagCompound();
    		lines.setString("line1", line1Field.getText());
    		lines.setString("line2", line2Field.getText());
    		lines.setString("line3", line3Field.getText());
    		lines.setString("line4", line4Field.getText());
    		lines.setString("line5", line5Field.getText());
    		
    		nbt.setTag("display", display);
    		nbt.setTag("lines", lines);
        	book.setTagCompound(nbt);
        	//System.out.println("saving NBT");
    	}
    }
    
    @Override
    public void initGui()
    {
    	super.initGui();
    	buttonList.clear();
    	Keyboard.enableRepeatEvents(true);
		int w = (width - 172) / 2;
		int h = (height - 240) / 2;
    	buttonList.add(this.buttonAccept = new GuiButton(0, w+100, h+104, 42, 20, I18n.translateToLocal("book.save"))); 
    	buttonList.add(this.buttonCancel = new GuiButton(1, w+30, h+104, 42, 20, I18n.translateToLocal("book.cancel"))); 
    	this.titleField = new GuiBiblioTextField(this.fontRenderer, w+31, h+15, 110, 12);
    	this.line1Field = new GuiBiblioTextField(this.fontRenderer, w+31, h+54, 110, 12);
    	this.line2Field = new GuiBiblioTextField(this.fontRenderer, w+31, h+64, 110, 12);
    	this.line3Field = new GuiBiblioTextField(this.fontRenderer, w+31, h+74, 110, 12);
    	this.line4Field = new GuiBiblioTextField(this.fontRenderer, w+31, h+84, 110, 12);
    	this.line5Field = new GuiBiblioTextField(this.fontRenderer, (w+31), (h+94), 110, 12); // I has to multiple w and h by 1/scale of new scale to get position correct
    	this.titleField.setTextColor(0x000000);
    	this.line1Field.setTextColor(0x000000);
    	this.line2Field.setTextColor(0x000000);
    	this.line3Field.setTextColor(0x000000);
    	this.line4Field.setTextColor(0x000000);
    	this.line5Field.setTextColor(0x000000);
    	this.titleField.setMaxStringLength(20);
    	this.line1Field.setMaxStringLength(20);
    	this.line2Field.setMaxStringLength(20);
    	this.line3Field.setMaxStringLength(20);
    	this.line4Field.setMaxStringLength(20);
    	this.line5Field.setMaxStringLength(20);
    	this.titleField.setText(this.title);
    	this.line1Field.setText(this.line1);
    	this.line2Field.setText(this.line2);
    	this.line3Field.setText(this.line3);
    	this.line4Field.setText(this.line4);
    	this.line5Field.setText(this.line5);
    	this.titleField.setEnableBackgroundDrawing(false);
    	this.line1Field.setEnableBackgroundDrawing(false);
    	this.line2Field.setEnableBackgroundDrawing(false);
    	this.line3Field.setEnableBackgroundDrawing(false);
    	this.line4Field.setEnableBackgroundDrawing(false);
    	this.line5Field.setEnableBackgroundDrawing(false);
    }
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.SLOTTEDBOOKGUI);
		int i = (width - 172) / 2;
		int j = (height - 240) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, 174, 256);
		this.titleField.drawTextBox();
		this.line1Field.drawTextBox();
		this.line2Field.drawTextBox();
		this.line3Field.drawTextBox();
		this.line4Field.drawTextBox();
		//GL11.glScalef(0.5f, 0.5f, 0.5f);
		//GL11.glTranslated(i*2, 0, j*2);
		this.line5Field.drawTextBox();
		//GL11.glScalef(2f, 2f, 2f);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		
	}
	
	
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	//System.out.println(click.id);
    	if (click.id == 0)
    	{
    		setNBTTitleAndLines();
    		sendPacket();
    		this.mc.player.closeScreen();
    		//System.out.println("called after?");
    	}
    	if (click.id == 1)
    	{
    		this.mc.player.closeScreen();
    	}
    }
    
    public void sendPacket()
    {
    	ByteBuf buffer = Unpooled.buffer();
    	ByteBufUtils.writeItemStack(buffer, book);
    	if (inHand)
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(book, false));
    		// BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
    	}
    	else
    	{
			// TODO: no `currentPage` is specified? 
			BiblioNetworking.INSTANCE.sendToServer(new BiblioMCBEdit(new BlockPos(tilex, tiley, tilez), 0, book));
    		// buffer.writeInt(tilex);
        	// buffer.writeInt(tiley);
        	// buffer.writeInt(tilez);
        	// BiblioCraft.ch_BiblioMCBEdit.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMCBEdit"));
    	}
    }
    
	@Override
    protected void mouseClicked(int left, int top, int click)
    {
		int w = (width - 172) / 2;
		int h = (height - 240) / 2;
		int heldSlot = this.player.inventory.currentItem;
		
		 if (left >= w+5+heldSlot*18 && left <= w+4+(heldSlot+1)*18 && top >= h+190 && top <= h+190+17)
		 {
			return; 
		 }
		 else
		 {
			 try
			{
				super.mouseClicked(left, top, click);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		 }

		this.titleField.mouseClicked(left, top, click);
		this.line1Field.mouseClicked(left, top, click);
		this.line2Field.mouseClicked(left, top, click);
		this.line3Field.mouseClicked(left, top, click);
		this.line4Field.mouseClicked(left, top, click);
		this.line5Field.mouseClicked(left, top, click);

    }
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		//System.out.println(this.line1Field.getCursorPosition());
        if (par2 == 1)
        {
            this.mc.player.closeScreen();
        }
        if (par2 == 200)
        {
        	// UP
        	if (this.line2Field.isFocused())
        	{
        		this.line1Field.setFocused(true);
        		this.line1Field.setCursorPosition(this.line2Field.getCursorPosition());
        		this.line2Field.setFocused(false);
        	}
        	else if (this.line3Field.isFocused())
        	{
        		this.line2Field.setFocused(true);
        		this.line2Field.setCursorPosition(this.line3Field.getCursorPosition());
        		this.line3Field.setFocused(false);
        	}
        	else if (this.line4Field.isFocused())
        	{
        		this.line3Field.setFocused(true);
        		this.line3Field.setCursorPosition(this.line4Field.getCursorPosition());
        		this.line4Field.setFocused(false);
        	}
        	else if (this.line5Field.isFocused())
        	{
        		this.line4Field.setFocused(true);
        		this.line4Field.setCursorPosition(this.line5Field.getCursorPosition());
        		this.line5Field.setFocused(false);
        	}
        }
        if (par2 == 208)
        {
        	// DOWN
        	if (this.line1Field.isFocused())
        	{
        		this.line2Field.setFocused(true);
        		this.line2Field.setCursorPosition(this.line1Field.getCursorPosition());
        		this.line1Field.setFocused(false);
        	}
        	else if (this.line2Field.isFocused())
        	{
        		this.line3Field.setFocused(true);
        		this.line3Field.setCursorPosition(this.line2Field.getCursorPosition());
        		this.line2Field.setFocused(false);
        	}
        	else if (this.line3Field.isFocused())
        	{
        		this.line4Field.setFocused(true);
        		this.line4Field.setCursorPosition(this.line3Field.getCursorPosition());
        		this.line3Field.setFocused(false);
        	}
        	else if (this.line4Field.isFocused())
        	{
        		this.line5Field.setFocused(true);
        		this.line5Field.setCursorPosition(this.line4Field.getCursorPosition());
        		this.line4Field.setFocused(false);
        	}
        }
        
        if (this.titleField.isFocused())
        {
        	if (par2 == 28)
        	{
        		line1Field.setFocused(true);
        		this.titleField.setFocused(false);
        	}
        	else
        	{
        		this.titleField.textboxKeyTyped(par1, par2);
        	}
        	
        }
        else if (this.line1Field.isFocused())
        {
        	if (par2 == 28)
        	{
        		this.line2Field.setFocused(true);
        		this.line1Field.setFocused(false);
        	}
        	else if (this.line1Field.getCursorPosition() >= this.line1Field.getMaxStringLength())
        	{
        		this.line2Field.setFocused(true);
        		this.line2Field.textboxKeyTyped(par1, par2);
        		this.line1Field.setFocused(false);
        	}
        	else if (this.line1Field.getCursorPosition() == 0 && par2 == 14)
        	{
        		
        	}
        	else
        	{
        		this.line1Field.textboxKeyTyped(par1, par2);
        	}
        }
        else if (this.line2Field.isFocused())
        {
        	if (par2 == 28)
        	{
        		this.line3Field.setFocused(true);
        		this.line2Field.setFocused(false);
        	}
        	else if (this.line2Field.getCursorPosition() >= this.line2Field.getMaxStringLength())
        	{
        		this.line3Field.setFocused(true);
        		this.line3Field.textboxKeyTyped(par1, par2);
        		this.line2Field.setFocused(false);
        	}
        	else if (this.line2Field.getCursorPosition() == 0 && par2 == 14)
        	{
        		this.line1Field.setFocused(true);
        		this.line1Field.textboxKeyTyped(par1, par2);
        		this.line2Field.setFocused(false);
        	}
        	else
        	{
        		this.line2Field.textboxKeyTyped(par1, par2);
        	}
        }
        else if (this.line3Field.isFocused())
        {
        	if (par2 == 28)
        	{
        		this.line4Field.setFocused(true);
        		this.line3Field.setFocused(false);
        	}
        	else if (this.line3Field.getCursorPosition() >= this.line3Field.getMaxStringLength())
        	{
        		this.line4Field.setFocused(true);
        		this.line4Field.textboxKeyTyped(par1, par2);
        		this.line3Field.setFocused(false);
        	}
        	else if (this.line3Field.getCursorPosition() == 0 && par2 == 14)
        	{
        		this.line2Field.setFocused(true);
        		this.line2Field.textboxKeyTyped(par1, par2);
        		this.line3Field.setFocused(false);
        	}
        	else
        	{
        		this.line3Field.textboxKeyTyped(par1, par2);
        	}
        }
        else if (this.line4Field.isFocused())
        {
        	if (par2 == 28)
        	{
        		this.line5Field.setFocused(true);
        		this.line4Field.setFocused(false);
        	}
        	else if (this.line4Field.getCursorPosition() >= this.line4Field.getMaxStringLength())
        	{
        		this.line5Field.setFocused(true);
        		this.line5Field.textboxKeyTyped(par1, par2);
        		this.line4Field.setFocused(false);
        	}
        	else if (this.line4Field.getCursorPosition() == 0 && par2 == 14)
        	{
        		this.line3Field.setFocused(true);
        		this.line3Field.textboxKeyTyped(par1, par2);
        		this.line4Field.setFocused(false);
        	}
        	else
        	{
        
        	this.line4Field.textboxKeyTyped(par1, par2);
        	}
        }
        else if (this.line5Field.isFocused())
        {
        	if (this.line5Field.getCursorPosition() >= this.line5Field.getMaxStringLength())
        	{
        		this.line5Field.textboxKeyTyped(par1, par2);
        	}
        	else if (this.line5Field.getCursorPosition() == 0 && par2 == 14)
        	{
        		this.line4Field.setFocused(true);
        		this.line4Field.textboxKeyTyped(par1, par2);
        		this.line5Field.setFocused(false);
        	}
        	else
        	{
        		this.line5Field.textboxKeyTyped(par1, par2);
        	}
        }
	}
	
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    }
}
