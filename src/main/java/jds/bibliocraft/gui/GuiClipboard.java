package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioMCBEdit;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiClipboard extends GuiScreen
{
	public static final ResourceLocation book_png = new ResourceLocation("bibliocraft", "textures/gui/clipboardGUI.png");
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    
    private int button0state = 1;
    private int button1state = 0;
    private int button2state = 0;
    private int button3state = 0;
    private int button4state = 0;
    private int button5state = 0;
    private int button6state = 0;
    private int button7state = 0;
    private int button8state = 0;
    
    private String button0text = " ";
    private String button1text = " ";
    private String button2text = " ";
    private String button3text = " ";
    private String button4text = " ";
    private String button5text = " ";
    private String button6text = " ";
    private String button7text = " ";
    private String button8text = " ";
    private String titletext = " ";
    
    private GuiBiblioTextField textField0;
    private GuiBiblioTextField textField1;
    private GuiBiblioTextField textField2;
    private GuiBiblioTextField textField3;
    private GuiBiblioTextField textField4;
    private GuiBiblioTextField textField5;
    private GuiBiblioTextField textField6;
    private GuiBiblioTextField textField7;
    private GuiBiblioTextField textField8;
    private GuiBiblioTextField textFieldTitle;
    
    private GuiButtonNextPage buttonNextPage;
    private GuiButtonNextPage buttonPreviousPage;
    
    private ItemStack clipStack;
    private int totalPages = 1;
    private int currentPage = 1;
    
    private int tilex = 0;
    private int tiley = 0;
    private int tilez = 0;
    
    private int fieldCharLimit = 23;
    
    private boolean inInv;
	
   // private GuiTextField texty;
    public GuiClipboard(ItemStack stack, boolean eqippued, int tx, int ty, int tz) 
    {
    	clipStack = stack;
    	inInv = eqippued;
    	getNBTData();
    	
    	if (!inInv)
    	{
    		tilex = tx;
    		tiley = ty;
    		tilez = tz;
    	}
	}
   // fontRenderer fr;
    public void getNBTData()
    {
    	NBTTagCompound cliptags = clipStack.getTagCompound();
    	if (cliptags != null)
    	{
    		currentPage = cliptags.getInteger("currentPage");
    		totalPages = cliptags.getInteger("totalPages");
    		//System.out.println(currentPage);
    		String pagenum = "page"+currentPage;
    		NBTTagCompound pagetag = cliptags.getCompoundTag(pagenum);
    		if (pagetag != null)
    		{
    			int[] taskstat = pagetag.getIntArray("taskStates");
    			//System.out.println(taskstat.length);
    			if (taskstat.length > 0)
    			{
	    			button0state = taskstat[0];
	    			button1state = taskstat[1];
	    			button2state = taskstat[2];
	    			button3state = taskstat[3];
	    			button4state = taskstat[4];
	    			button5state = taskstat[5];
	    			button6state = taskstat[6];
	    			button7state = taskstat[7];
	    			button8state = taskstat[8];
    			}
    			NBTTagCompound tasks = pagetag.getCompoundTag("tasks");
    			button0text = tasks.getString("task1");
    			button1text = tasks.getString("task2");
    			button2text = tasks.getString("task3");
    			button3text = tasks.getString("task4");
    			button4text = tasks.getString("task5");
    			button5text = tasks.getString("task6");
    			button6text = tasks.getString("task7");
    			button7text = tasks.getString("task8");
    			button8text = tasks.getString("task9");
    			titletext = pagetag.getString("title");

    		}
    	}
    }
    
    @Override
	public void initGui()
	{
    	// this runs once, no problem
    	//System.out.println("passing the initGui method");
    	super.initGui();
    	buttonList.clear();
    	Keyboard.enableRepeatEvents(true);
    	int var5 = (this.width - this.bookImageWidth) / 2;
    	int sidex = (this.width/2) - 64;
    	int sidex2 = sidex + 12;
    	buttonList.add(new GuiButtonClipboard(0, sidex, 27, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(1, sidex, 42, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(2, sidex, 57, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(3, sidex, 72, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(4, sidex, 87, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(5, sidex, 102, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(6, sidex, 117, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(7, sidex, 132, 10, 10, "", true));
    	buttonList.add(new GuiButtonClipboard(8, sidex, 147, 10, 10, "", true));
    	
    	this.textField0 = new GuiBiblioTextField(this.fontRenderer, sidex2, 29, 115, 10);
    	this.textField1 = new GuiBiblioTextField(this.fontRenderer, sidex2, 44, 115, 10);
    	this.textField2 = new GuiBiblioTextField(this.fontRenderer, sidex2, 59, 115, 10);
    	this.textField3 = new GuiBiblioTextField(this.fontRenderer, sidex2, 74, 115, 10);
    	this.textField4 = new GuiBiblioTextField(this.fontRenderer, sidex2, 89, 115, 10);
    	this.textField5 = new GuiBiblioTextField(this.fontRenderer, sidex2, 104, 115, 10);
    	this.textField6 = new GuiBiblioTextField(this.fontRenderer, sidex2, 119, 115, 10);
    	this.textField7 = new GuiBiblioTextField(this.fontRenderer, sidex2, 134, 115, 10);
    	this.textField8 = new GuiBiblioTextField(this.fontRenderer, sidex2, 149, 115, 10);
    	this.textFieldTitle = new GuiBiblioTextField(this.fontRenderer, sidex2-10, 14, 125, 10);

    	this.textField0.setEnableBackgroundDrawing(false);
    	this.textField1.setEnableBackgroundDrawing(false);
    	this.textField2.setEnableBackgroundDrawing(false);
    	this.textField3.setEnableBackgroundDrawing(false);
    	this.textField4.setEnableBackgroundDrawing(false);
    	this.textField5.setEnableBackgroundDrawing(false);
    	this.textField6.setEnableBackgroundDrawing(false);
    	this.textField7.setEnableBackgroundDrawing(false);
    	this.textField8.setEnableBackgroundDrawing(false);
    	this.textFieldTitle.setEnableBackgroundDrawing(false);
    	this.textField0.setTextColor(0x404040);
    	this.textField1.setTextColor(0x404040);
    	this.textField2.setTextColor(0x404040);
    	this.textField3.setTextColor(0x404040);
    	this.textField4.setTextColor(0x404040);
    	this.textField5.setTextColor(0x404040);
    	this.textField6.setTextColor(0x404040);
    	this.textField7.setTextColor(0x404040);
    	this.textField8.setTextColor(0x404040);
    	this.textFieldTitle.setTextColor(0x404040);
    	
    	this.textField0.setText(button0text);
    	this.textField1.setText(button1text);
    	this.textField2.setText(button2text);
    	this.textField3.setText(button3text);
    	this.textField4.setText(button4text);
    	this.textField5.setText(button5text);
    	this.textField6.setText(button6text);
    	this.textField7.setText(button7text);
    	this.textField8.setText(button8text);
    	this.textFieldTitle.setText(titletext);
    	
    	this.textField0.setMaxStringLength(fieldCharLimit);
    	this.textField1.setMaxStringLength(fieldCharLimit);
    	this.textField2.setMaxStringLength(fieldCharLimit);
    	this.textField3.setMaxStringLength(fieldCharLimit);
    	this.textField4.setMaxStringLength(fieldCharLimit);
    	this.textField5.setMaxStringLength(fieldCharLimit);
    	this.textField6.setMaxStringLength(fieldCharLimit);
    	this.textField7.setMaxStringLength(fieldCharLimit);
    	this.textField8.setMaxStringLength(fieldCharLimit);
    	this.textFieldTitle.setMaxStringLength(26);
    	//buttonList.add(new GuiTextField(fontRenderer, sidex2, 27, 109, 10));
    	// I also need to render the current page number at the bottom in the center of the page
        int var1 = (this.width - this.bookImageWidth) / 2;
        byte var2 = 2;
        this.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(10, var1 + 120, var2 + 157, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(11, var1 + 38, var2 + 157, false));
    	
	}
    
    @Override
	public void drawScreen(int par1, int par2, float par3)
    {
        //int var4 = this.mc.renderEngine.getTexture("/gui/book.png");
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	// This runs each tich
    	
    	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(book_png);
        int var5 = (this.width - this.bookImageWidth) / 2;
        byte var6 = 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.bookImageWidth, this.bookImageHeight);
        //System.out.println("Attempting the draw screen method");
       // System.out.println(button0state);
        switch(button0state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 26, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 25, 72, 194, 16, 15); break;}
        }
        switch(button1state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 41, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 40, 72, 194, 16, 15); break;}
        }
        switch(button2state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 56, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 55, 72, 194, 16, 15); break;}
        }
        switch(button3state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 71, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 70, 72, 194, 16, 15); break;}
        }
        switch(button4state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 86, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 85, 72, 194, 16, 15); break;}
        }
        switch(button5state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 101, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 100, 72, 194, 16, 15); break;}
        }
        switch(button6state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 116, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 115, 72, 194, 16, 15); break;}
        }
        switch(button7state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 131, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 130, 72, 194, 16, 15); break;}
        }
        switch(button8state)
        {
	        case 1:{this.drawTexturedModalRect(var5+30, 146, 51, 195, 16, 12); break;}
	        case 2:{this.drawTexturedModalRect(var5+29, 145, 72, 194, 16, 15); break;}
        }
        String currpage = ""+currentPage;
        //currentPage = 1;
        //System.out.println(var5);
        int pageoffset = var5+90;
        if (currentPage > 9)
        {
        	pageoffset = var5+87;
        }
        fontRenderer.drawString(currpage, pageoffset, 162, 0x404040);
        if (currentPage > 1)
        {
        	this.buttonPreviousPage.enabled = true;
        }
        else
        {
        	this.buttonPreviousPage.enabled = false;
        }
        if (currentPage > 49)
        {
        	this.buttonNextPage.enabled = false;
        }
        else
        {
        	this.buttonNextPage.enabled = true;
        }
        super.drawScreen(par1, par2, par3);
        //GL11.glDisable(GL11.GL_LIGHTING);
        this.textField0.drawTextBox();
        this.textField1.drawTextBox();
        this.textField2.drawTextBox();
        this.textField3.drawTextBox();
        this.textField4.drawTextBox();
        this.textField5.drawTextBox();
        this.textField6.drawTextBox();
        this.textField7.drawTextBox();
        this.textField8.drawTextBox();
        this.textFieldTitle.drawTextBox();
        
        if (this.textField0.isFocused())
        {
        	//System.out.println("Field 0 is focused");
        }
       // GL11.glEnable(GL11.GL_LIGHTING);
    }
    
    @Override
	public void updateScreen()
    {
    	//System.out.println("test");
        super.updateScreen();
    }
    
    private void nextPage()
    {
    	// I should probly double check and save the current page of stuff before advancing as well..
    	saveNBT();
    	NBTTagCompound cliptags = clipStack.getTagCompound();
    	if (cliptags != null)
    	{
    		currentPage = cliptags.getInteger("currentPage");
    		int nextpage = currentPage + 1;
    		//System.out.println(nextpage);
    		String pagenum = "page"+nextpage;
    		NBTTagCompound pagetag = cliptags.getCompoundTag(pagenum);
    		int[] taskstat = pagetag.getIntArray("taskStates");
    		if (taskstat.length == 9)
    		{
    			//System.out.println("I guess we found some NBT ...");
    			currentPage++;
    			cliptags.setInteger("currentPage", currentPage);
    			clipStack.setTagCompound(cliptags);
    			getNBTData();
    			initGui();
    			//return true;
    		}
    		else
    		{
    			// here is where I need to add nbt to the next page.
    			//System.out.println("adding new NBT");
    			currentPage++;
				NBTTagCompound page = new NBTTagCompound();
				NBTTagCompound tasks = new NBTTagCompound();
				int[] taskstate = {0,0,0,0,0,0,0,0,0};
				page.setIntArray("taskStates", taskstate);
				tasks.setString("task1", "");
				tasks.setString("task2", "");
				tasks.setString("task3", "");
				tasks.setString("task4", "");
				tasks.setString("task5", "");
				tasks.setString("task6", "");
				tasks.setString("task7", "");
				tasks.setString("task8", "");
				tasks.setString("task9", "");
				page.setTag("tasks", tasks);
				page.setString("title", "");
				String pagename = "page"+currentPage;
				cliptags.setTag(pagename,  page);
				cliptags.setInteger("currentPage", currentPage);
				cliptags.setInteger("totalPages", (totalPages + 1));
				//System.out.println("updated NBT");
				clipStack.setTagCompound(cliptags);
				getNBTData();
				initGui();
    		}
    	}
    	//return false;
    }
    
    private void prevPage()
    {
    	saveNBT();
    	NBTTagCompound cliptags = clipStack.getTagCompound();
    	if (cliptags != null)
    	{
    		currentPage = cliptags.getInteger("currentPage");
    		int prevpage = currentPage - 1;
    		//System.out.println(currentPage);
    		String pagenum = "page"+prevpage;
    		NBTTagCompound pagetag = cliptags.getCompoundTag(pagenum);
    		if (pagetag != null)
    		{
    			currentPage = currentPage - 1;
    			cliptags.setInteger("currentPage", currentPage);
    			clipStack.setTagCompound(cliptags);
    			getNBTData();
    			initGui();
    			//return true;
    		}
    	}
    	//return false;
    }
    
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	//System.out.println("Action performed!  Clicked X: "+click.xPosition+"   Clicked Y: "+click.yPosition);
    	//System.out.println(click.id);
    	// This needs to be updated to send a packet to update the item.
    	// update the nbt of the item on the server side
    	if (click.id < 9)
    	{
	    	NBTTagCompound cliptags = clipStack.getTagCompound();
	    	if (cliptags != null)
	    	{
	    		String pagenum = "page"+cliptags.getInteger("currentPage");
	    		//System.out.println(pagenum+"   this is my pagenum");
	    		NBTTagCompound pagetag = cliptags.getCompoundTag(pagenum);
	    		if (pagetag != null)
	    		{
	    			int[] taskstat = pagetag.getIntArray("taskStates");
	    			if (taskstat.length == 9)
	    			{
		    			for(int x=0; x < 9; x++)
		    			{
		    				if (click.id == x)
		    				{
			    				if (taskstat[x] > 1)
			    				{
			    					taskstat[x] = 0;
			    				}
			    				else
			    				{
			    					//System.out.println("testaroo"+taskstat[x]);
			    					taskstat[x] = taskstat[x] + 1;
			    				}
		    				}
			    		}
		    			button0state = taskstat[0];
		    			button1state = taskstat[1];
		    			button2state = taskstat[2];
		    			button3state = taskstat[3];
		    			button4state = taskstat[4];
		    			button5state = taskstat[5];
		    			button6state = taskstat[6];
		    			button7state = taskstat[7];
		    			button8state = taskstat[8];
		    			pagetag.setIntArray("taskStates", taskstat);
		    			clipStack.setTagCompound(cliptags);
	    			}
	    		}
	    	}
    	}
    	
    	if(click.id == 10)
    	{
    		// this is the next page button
    		nextPage();
    	}
    	if(click.id == 11)
    	{
    		// this is the previous page button
    		prevPage();
    	}
    }
    
    public void saveNBT()
    {
    	// I will write this method to be called from onGuiClosed and from next pages/prev page functions
    	// this way I can be sure the data is full saved.
    	NBTTagCompound cliptags = this.clipStack.getTagCompound();
    	if (cliptags != null)
    	{
    		currentPage = cliptags.getInteger("currentPage");
    		//System.out.println(currentPage);
    		String pagenum = "page"+currentPage;
    		NBTTagCompound pagetag = cliptags.getCompoundTag(pagenum);
    		if (pagetag != null)
    		{
    			NBTTagCompound tasks = pagetag.getCompoundTag("tasks");
    			tasks.setString("task1", textField0.getText());
    			tasks.setString("task2", textField1.getText());
    			tasks.setString("task3", textField2.getText());
    			tasks.setString("task4", textField3.getText());
    			tasks.setString("task5", textField4.getText());
    			tasks.setString("task6", textField5.getText());
    			tasks.setString("task7", textField6.getText());
    			tasks.setString("task8", textField7.getText());
    			tasks.setString("task9", textField8.getText());
    			pagetag.setString("title", textFieldTitle.getText());
    			pagetag.setTag("tasks", tasks);
    			cliptags.setTag(pagenum, pagetag);
    			clipStack.setTagCompound(cliptags);
    		}
    		
    	}
    }
    
    @Override
    public void onGuiClosed()
    {
    	//System.out.println("I can haz closed GUI?");
    	Keyboard.enableRepeatEvents(false);
    	saveNBT();
        // ByteBuf buffer = Unpooled.buffer();
        try
        {
        	//ByteBufUtils.writeItemStack(buffer, this.clipStack);

            if (inInv)
            {
				BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(this.clipStack, false));
                //BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
            }
            else
            {
				BiblioNetworking.INSTANCE.sendToServer(new BiblioMCBEdit(new BlockPos(tilex, tiley, tilez), currentPage, this.clipStack));
            	// buffer.writeInt(tilex);
            	// buffer.writeInt(tiley);
            	// buffer.writeInt(tilez);
            	// buffer.writeInt(currentPage);
            	// BiblioCraft.ch_BiblioMCBEdit.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMCBEdit"));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
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
        //System.out.println(par1+"    "+par2+"    "+par3);
        this.textField0.mouseClicked(par1, par2, par3);
        this.textField1.mouseClicked(par1, par2, par3);
        this.textField2.mouseClicked(par1, par2, par3);
        this.textField3.mouseClicked(par1, par2, par3);
        this.textField4.mouseClicked(par1, par2, par3);
        this.textField5.mouseClicked(par1, par2, par3);
        this.textField6.mouseClicked(par1, par2, par3);
        this.textField7.mouseClicked(par1, par2, par3);
        this.textField8.mouseClicked(par1, par2, par3);
        this.textFieldTitle.mouseClicked(par1, par2, par3);
    }
    
    protected void keyTyped(char par1, int par2)
    {
    	//System.out.println(par1+"     "+par2);
    	if (par2 == 28 || par2 == 208)
    	{
    		if (this.textField0.isFocused())
    		{
    			this.textField0.setFocused(false);
    			this.textField1.setFocused(true);
    		}
    		else if (this.textField1.isFocused())
    		{
    			this.textField1.setFocused(false);
    			this.textField2.setFocused(true);
    		}
    		else if (this.textField2.isFocused())
    		{
    			this.textField2.setFocused(false);
    			this.textField3.setFocused(true);
    		}
    		else if (this.textField3.isFocused())
    		{
    			this.textField3.setFocused(false);
    			this.textField4.setFocused(true);
    		}
    		else if (this.textField4.isFocused())
    		{
    			this.textField4.setFocused(false);
    			this.textField5.setFocused(true);
    		}
    		else if (this.textField5.isFocused())
    		{
    			this.textField5.setFocused(false);
    			this.textField6.setFocused(true);
    		}
    		else if (this.textField6.isFocused())
    		{
    			this.textField6.setFocused(false);
    			this.textField7.setFocused(true);
    		}
    		else if (this.textField7.isFocused())
    		{
    			this.textField7.setFocused(false);
    			this.textField8.setFocused(true);
    		}
    	}
    	if (par2 == 200)
    	{
    		if (this.textField1.isFocused())
    		{
    			this.textField1.setFocused(false);
    			this.textField0.setFocused(true);
    		}
    		else if (this.textField2.isFocused())
    		{
    			this.textField2.setFocused(false);
    			this.textField1.setFocused(true);
    		}
    		else if (this.textField3.isFocused())
    		{
    			this.textField3.setFocused(false);
    			this.textField2.setFocused(true);
    		}
    		else if (this.textField4.isFocused())
    		{
    			this.textField4.setFocused(false);
    			this.textField3.setFocused(true);
    		}
    		else if (this.textField5.isFocused())
    		{
    			this.textField5.setFocused(false);
    			this.textField4.setFocused(true);
    		}
    		else if (this.textField6.isFocused())
    		{
    			this.textField6.setFocused(false);
    			this.textField5.setFocused(true);
    		}
    		else if (this.textField7.isFocused())
    		{
    			this.textField7.setFocused(false);
    			this.textField6.setFocused(true);
    		}
    		else if (this.textField8.isFocused())
    		{
    			this.textField8.setFocused(false);
    			this.textField7.setFocused(true);
    		}
    	}
        if (this.textField0.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField1.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField2.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField3.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField4.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField5.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField6.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField7.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textField8.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
        }
        else if (this.textFieldTitle.textboxKeyTyped(par1, par2))
        {
           // this.func_135015_g();
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
