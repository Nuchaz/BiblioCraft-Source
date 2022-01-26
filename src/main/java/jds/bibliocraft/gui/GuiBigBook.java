package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.Config;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioMCBEdit;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class GuiBigBook extends GuiScreen
{
	private int defaultTextScale = Config.defaultBigBookTextScale; //0; 
	
	private int bookImageWidth = 220;
	private int bookImageHeight = 256;
	private int spacing = 30;
	private ItemStack book;
	private boolean isinhand;
    private int tilex = 0;
    private int tiley = 0;
    private int tilez = 0;
  
    // signing stuff
    private GuiButton buttonSignAccept;
    private GuiButton buttonSignCancel;
    private GuiBiblioTextField signTextField;
    private String signingInstruct = ""+TextFormatting.ITALIC+I18n.translateToLocal("book.entertitle");
    private String signingNotice = ""+TextFormatting.ITALIC+I18n.translateToLocal("book.signnotice");
    private String signedBy = ""+TextFormatting.ITALIC+I18n.translateToLocal("book.by");
    private String signedAuthor = "";
    private boolean signing = false;
    private boolean signed = false;
    
    //
	private GuiButton buttonSave;
	private GuiButton buttonSign;
	private GuiButton buttonBold;
	private GuiButton buttonItalic;
	private GuiButton buttonUnderline;
	private GuiButton buttonStrike;
	private GuiButton buttonObfusticate;
	private GuiButton buttonReset;
	private GuiButtonClipboard colorMockButton;
	private GuiButtonClipboard buttonPageBack;
	private GuiButtonClipboard buttonPageForward;
	private GuiButtonClipboard buttonPageFirst;
	private GuiButtonClipboard buttonPageLast;
	
	private GuiButton buttonScalePlus;
	private GuiButton buttonScaleNeg;
	private int currentLine = 0;
	private int currentLineCursorPos = 1;
	private String textScale = I18n.translateToLocal("book.textscale");
	private String lineSelected = I18n.translateToLocal("book.lineselected");
	private String textScaling[] = {"0.5x", "0.65x", "0.8x", "1.0x", "1.2x", "1.4x", "1.75x", "2.0x"};
	private String mods[][] = new String[4][4];
	
	private GuiBiblioTextField lines[] = new GuiBiblioTextField[44];// I need to do some magic on lines if I am to support scales
	private int lineScales[] = new int[44];
	private String lineTexts[] = new String[44];

	private GuiBiblioTextField chapters[] = new GuiBiblioTextField[16];
	private boolean chapterBool[] = {false, false, false, false, false, false, false, false};
	private int chapterPageNums[] = new int[8];
	private String chapterNames[] = new String[16];
	//private int mcount = 0;
	
	
	private float scales[] = {0.5f, 0.65f, 0.8f, 1.0f, 1.2f, 1.4f, 1.75f, 2.0f};
	private float antiscales[] = {1/scales[0], 1/scales[1], 1/scales[2], 1/scales[3], 1/scales[4], 1/scales[5], 1/scales[6], 1/scales[7]};
	private int scaledLinesNumber[] = {44, 34, 27, 22, 18, 16, 12, 11};
	private float lineCounter = 0.0f;
	//private int scalenum = 2;
	private int numOfLines = 1; 
	private int linespace = 0;
	
	private int currentPage = 0;
	private int totalPages = 1;
	private String pagesTextLine = "";
	
	private int widthLocal;
	private int heightLocal;
	
	private boolean isInitialized = false;
	
	public GuiBigBook(ItemStack bigbook, boolean inhand, int tx, int ty, int tz, String author)
	{
		this.lineTexts = new String[44];
		this.lineScales = new int[44];
		for (int n=0; n<44; n++)
		{
			this.lineTexts[n] = "";
			this.lineScales[n] = defaultTextScale;
		}
		
		this.book = bigbook;
		this.isinhand = inhand;
    	mods[0][0] = ""+TextFormatting.BLACK;
    	mods[0][1] = ""+TextFormatting.DARK_BLUE;
    	mods[0][2] = ""+TextFormatting.DARK_GREEN;
    	mods[0][3] = ""+TextFormatting.DARK_AQUA;
    	mods[1][0] = ""+TextFormatting.DARK_RED;
    	mods[1][1] = ""+TextFormatting.DARK_PURPLE;
    	mods[1][2] = ""+TextFormatting.GOLD;
    	mods[1][3] = ""+TextFormatting.GRAY;
     	mods[2][0] = ""+TextFormatting.DARK_GRAY;
    	mods[2][1] = ""+TextFormatting.BLUE;
    	mods[2][2] = ""+TextFormatting.GREEN;
    	mods[2][3] = ""+TextFormatting.AQUA;
     	mods[3][0] = ""+TextFormatting.RED;
    	mods[3][1] = ""+TextFormatting.LIGHT_PURPLE;
    	mods[3][2] = ""+TextFormatting.YELLOW;
    	mods[3][3] = ""+TextFormatting.WHITE;
    	for (int n=0; n<44; n++)
    	{
    		//this.lineScales[n] = defaultTextScale;
    	}
    	NBTTagCompound tags = this.book.getTagCompound();
		if (tags != null)
		{
			this.totalPages = tags.getInteger("pagesTotal");
			this.currentPage = tags.getInteger("pagesCurrent");
			this.signed = tags.getBoolean("signed");
			if (this.totalPages == 0)
			{
				this.totalPages = 1;
			}
			if (tags.hasKey("author"))
			{
				this.signedAuthor = tags.getString("author");
			}
			else
			{
				this.signedAuthor = author;
			}
		}
		loadCurrentPageLinesFromNBT();
    	if (!inhand)
    	{
    		tilex = tx;
    		tiley = ty;
    		tilez = tz;
    	}
    	
	}
	
	@Override
    public void setWorldAndResolution(Minecraft p_146280_1_, int p_146280_2_, int p_146280_3_)
    {
    	if (this.isInitialized)
    	{
    		saveLinesToList();
    	}
    	else
    	{
    		this.isInitialized = true;
    	}
        this.mc = p_146280_1_;
        this.fontRenderer = p_146280_1_.fontRenderer;
        this.width = p_146280_2_;
        this.height = p_146280_3_;
        if (!MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Pre(this, this.buttonList)))
        {
            this.buttonList.clear();
            this.initGui();
        }
        MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Post(this, this.buttonList));
    }
	
	public void loadCurrentPageLinesFromNBT()
	{
		NBTTagCompound tags = this.book.getTagCompound();
		if (tags != null)
		{
			//this.totalPages = tags.getInteger("pagesTotal");
			//this.currentPage = tags.getInteger("pagesCurrent");
			NBTTagCompound pagesTag = tags.getCompoundTag("pages");
			if (pagesTag != null)
			{
				// page load logic
				NBTTagList nbtLines = pagesTag.getTagList("page"+this.currentPage, Constants.NBT.TAG_STRING);
				if (nbtLines != null)
				{
					//System.out.println("printing lines?");
					for (int n=0; n<44; n++)
					{
						this.lineTexts[n] = nbtLines.getStringTagAt(n);
					}
					this.lineScales = pagesTag.getIntArray("pageScale"+this.currentPage);
					if (this.lineScales.length != 44 || this.lineTexts.length != 44)
					{
						//System.out.println("nullified?"+lineScales.length);
						//System.out.println("new stuffs");
						this.lineTexts = new String[44];
						this.lineScales = new int[44];
						for (int n=0; n<44; n++)
						{
							this.lineTexts[n] = "";
							this.lineScales[n] = defaultTextScale;
						}
					}
				}
				else
				{
					//System.out.println("updating arrays");
					for (int n=0; n<44; n++)
					{
						this.lineTexts[n] = "";
						this.lineScales[n] = defaultTextScale;
					}
				}
			}
			// chapter load logic
			NBTTagCompound chapTag = tags.getCompoundTag("chapters");
			if (chapTag != null)
			{
				// first the chapter page nubmers
				this.chapterPageNums = chapTag.getIntArray("chapPages");
				if (this.chapterPageNums.length != 8)
				{
					this.chapterPageNums = new int[8];
				}
				// now the boolean list
				int[] bools = chapTag.getIntArray("chapBools");
				if (bools.length == 8)
				{
					for (int n=0; n<8; n++)
					{
						if (bools[n] == 1)
						{
							this.chapterBool[n] = true;
						}
						else
						{
							this.chapterBool[n] = false;
						}
					}
				}
				else
				{
					this.chapterBool = new boolean[8];
				}
				// finaly the text
				for (int n = 0; n<16; n++)
				{
					this.chapterNames[n] = chapTag.getString("chapline"+n);
				}
				
				if (this.chapterNames.length != 16)
				{
					this.chapterNames = new String[16];
				}
			}
		}
	}
	
	public void saveCurrentPageToNBT()
	{
		NBTTagCompound tags = this.book.getTagCompound();
		if (tags == null)
		{
			tags = new NBTTagCompound();
		}
		
		//Page Logic
		NBTTagCompound pagesTag = tags.getCompoundTag("pages");
		if (pagesTag == null)
		{
			pagesTag = new NBTTagCompound();
		}
				
		tags.setInteger("pagesTotal", totalPages);
		tags.setInteger("pagesCurrent", currentPage);
		
		NBTTagList nbtLines = new NBTTagList();
		for (int n = 0; n<44; n++)
		{
			nbtLines.appendTag(new NBTTagString(lines[n].getText()));
		}
		pagesTag.setIntArray("pageScale"+this.currentPage, this.lineScales);
		pagesTag.setTag("page"+this.currentPage, nbtLines);
		
		//Chapter Logic
		//chapterBool[] = new boolean[8];
		//chapterPageNums[] = new int[8];
		//chapterNames[] = new String[16];
		NBTTagCompound chapterNBT = new NBTTagCompound();
		int[] chapbools = new int[8];
		for (int n=0; n<8; n++)
		{
			if (this.chapterBool[n])
			{
				chapbools[n] = 1;
			}
			else
			{
				chapbools[n] = 0;
			}
		}
		for (int n=0; n<16; n++)
		{
			chapterNBT.setString("chapline"+n, chapters[n].getText());
		}
		chapterNBT.setIntArray("chapBools", chapbools);
		chapterNBT.setIntArray("chapPages", chapterPageNums);
		
		tags.setTag("pages", pagesTag);
		tags.setTag("chapters", chapterNBT);
		this.book.setTagCompound(tags);
	}
	
	public void saveSigningToNBT()
	{
		NBTTagCompound tags = this.book.getTagCompound();
		if (tags != null)
		{
			tags.setBoolean("signed", this.signed);
			//tags.setString(par1Str, par2Str)
			NBTTagCompound display = new NBTTagCompound();
			display.setString("Name",TextFormatting.WHITE+this.signTextField.getText());
			tags.setTag("display", display);
			tags.setString("author", this.signedAuthor);
			this.book.setTagCompound(tags);
		}
	}
	
    @Override
    public void initGui()
    {
    	super.initGui();
    	this.widthLocal = (this.width - this.bookImageWidth) / 2;
    	this.heightLocal = (this.height - this.bookImageHeight) / 2;

    	buttonList.clear();
    	Keyboard.enableRepeatEvents(true);
    	//signed = true;
    	if (!(signed || signing))
    	{
	    	buttonList.add(this.buttonBold = new GuiButton(10, widthLocal-60, heightLocal+25, 20, 20, TextFormatting.BOLD+I18n.translateToLocal("book.bold"))); 
	    	buttonList.add(this.buttonItalic = new GuiButton(11, widthLocal-40, heightLocal+25, 20, 20, TextFormatting.ITALIC+I18n.translateToLocal("book.italic"))); 
	    	buttonList.add(this.buttonUnderline = new GuiButton(12, widthLocal-20, heightLocal+25, 20, 20, TextFormatting.UNDERLINE+I18n.translateToLocal("book.underline"))); 
	    	buttonList.add(this.buttonStrike = new GuiButton(13, widthLocal-60, heightLocal+45, 40, 20, TextFormatting.STRIKETHROUGH+I18n.translateToLocal("book.strike"))); 
	    	buttonList.add(this.buttonObfusticate = new GuiButton(14, widthLocal-20, heightLocal+45, 20, 20, TextFormatting.OBFUSCATED+I18n.translateToLocal("book.obfuscated")));
	    	buttonList.add(this.buttonReset = new GuiButton(15, widthLocal-60, heightLocal+118, 60, 20, I18n.translateToLocal("book.reset"))); 
	    	buttonList.add(this.colorMockButton = new GuiButtonClipboard(9, widthLocal-60, heightLocal+65, 60, 53, "", false));
	    	
	    	buttonList.add(this.buttonScalePlus = new GuiButton(16, widthLocal-20, heightLocal+154, 20, 20, "+"));
	    	buttonList.add(this.buttonScaleNeg = new GuiButton(17, widthLocal-60, heightLocal+154, 20, 20, "-"));
	    	
	    	buttonList.add(this.buttonSave = new GuiButton(0, widthLocal-60, heightLocal+228, 60, 20, I18n.translateToLocal("book.save")));
	    	buttonList.add(this.buttonSign = new GuiButton(1, widthLocal-60, heightLocal+204, 60, 20, I18n.translateToLocal("book.sign")));
    	}
    	// these buttons are good for a signed book, but not a signing book.
    	buttonList.add(this.buttonPageBack = new GuiButtonClipboard(18, widthLocal+45, heightLocal+234, 20, 12, "", false)); 
    	buttonList.add(this.buttonPageForward = new GuiButtonClipboard(19, widthLocal+150, heightLocal+234, 20, 12, "", false));
    	buttonList.add(this.buttonPageFirst = new GuiButtonClipboard(20, widthLocal+15, heightLocal+234, 20, 12, "", false)); 
    	buttonList.add(this.buttonPageLast = new GuiButtonClipboard(21, widthLocal+178, heightLocal+234, 20, 12, "", false));
    	
    	//  This is good for a signed book, but not a signing book
    	numOfLines = 0;
    	lineCounter = 0.0f;
    	linespace = 0;

    	for (int n=0; n<44; n++)
    	{
    		this.lines[n] = new GuiBiblioTextField(fontRenderer, (int)((widthLocal+15)*antiscales[lineScales[n]]),(int)(((heightLocal+15)*antiscales[lineScales[n]])+(linespace*antiscales[lineScales[n]])), (int)(193*antiscales[lineScales[n]]), 8);
    		this.lines[n].setEnableBackgroundDrawing(false);
    		this.lines[n].setMaxStringLength(200);
    		this.lines[n].setTextColor(0x000000);
    		if (n < lineTexts.length)
    		{
    			if (lineTexts[n] != null)
    			{
    				this.lines[n].setText(lineTexts[n]); 
    			}
    		}
            String textTest = this.lines[n].getText();
            int formcount = 0;
            if (textTest.length() > 0)
            {
	            for (int m = 0; m<textTest.length(); m++)
	            {
	            	if (textTest.substring(m, m+1).contains("\u00a7")) 
	            	{
	            		formcount++;
	            	}
	            }
            }
            formcount = formcount*2;
          //  System.out.println(formcount);
    		this.lines[n].setMaxStringLength((int) ((40+formcount)*antiscales[lineScales[n]])); //  sometimes this isnt good enough, formatting symbols are burning space, kind of fixed though now I thiunk
    		lineCounter += 1.0f/(scaledLinesNumber[lineScales[n]]);
    		linespace += (int)5*(44.0f/scaledLinesNumber[lineScales[n]]);
    		//System.out.println(linespace);
    		if (lineCounter <= 1.0f)
    		{
    			numOfLines++;
    		}
    	}
    	// chapter init logic
    	for (int n=0; n<16; n++)
    	{
    		if (n%2 == 0)
    		{
    			this.chapters[n] = new GuiBiblioTextField(fontRenderer, (widthLocal+224), (heightLocal+11+(15*n)), 75, 8);
    		}
    		else
    		{
    			this.chapters[n] = new GuiBiblioTextField(fontRenderer, (widthLocal+224), (heightLocal+21+(15*(n-1))), 75, 8);
    		}
    		this.chapters[n].setEnableBackgroundDrawing(false);
    		this.chapters[n].setMaxStringLength(15);
    		this.chapters[n].setTextColor(0x000000);
    		if (this.chapterNames[n] != null)
    		{
    			this.chapters[n].setText(this.chapterNames[n]);
    		}
    	}
    	
    	if (this.currentPage == 0)
    	{
    		this.buttonPageBack.enabled = false;
    		this.buttonPageFirst.enabled = false;
    	}
    	else
    	{
    		this.buttonPageBack.enabled = true;
    		this.buttonPageFirst.enabled = true;
    	}
    	
    	if (this.currentPage == 255)
    	{
    		this.buttonPageForward.enabled = false;
    	}
    	else
    	{
    		this.buttonPageForward.enabled = true;
    	}
    	if (this.currentPage == (this.totalPages-1))
    	{
    		this.buttonPageLast.enabled = false;
    		if (signed)
    		{
    			this.buttonPageForward.enabled = false;
    		}
    	}
    	else
    	{
    		this.buttonPageLast.enabled = true;
    	}
    	
    	if (signing)
    	{
    		this.buttonPageBack.enabled = false;
    		this.buttonPageFirst.enabled = false;
    		this.buttonPageForward.enabled = false;
    		this.buttonPageLast.enabled = false;
    		buttonList.add(this.buttonSignCancel = new GuiButton(2, widthLocal+20, heightLocal+204, 80, 20, I18n.translateToLocal("book.cancel")));
    		buttonList.add(this.buttonSignAccept = new GuiButton(3, widthLocal+120, heightLocal+204, 80, 20, I18n.translateToLocal("book.sign")));
    		this.signTextField = new GuiBiblioTextField(fontRenderer, (widthLocal+22), (heightLocal+80), 180, 8);
    		this.signTextField.setEnableBackgroundDrawing(false);
    		this.signTextField.setMaxStringLength(34);
    		this.signTextField.setTextColor(0x000000);
    	}

    }
    
    public void saveLinesToList()
    {
   		//listLines.clear();
    	lineTexts = new String[44];
    	for (int n=0; n<44; n++)
    	{
    		lineTexts[n] = lines[n].getText();
    	}
    }
    
	@Override
	public void drawScreen(int x, int y, float f)
	{
		//System.out.println("x: "+x+"   y: "+y);
		// x and y are mouse positions
    	//int widthLocal = (this.width - this.bookImageWidth) / 2;
    	//int heightLocal = (this.height - this.bookImageHeight) / 2;
    	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(CommonProxy.BIGBOOKGUI);
		this.drawTexturedModalRect(widthLocal, heightLocal, 0, 0, this.bookImageWidth, this.bookImageHeight);
		this.mc.getTextureManager().bindTexture(CommonProxy.BIGBOOKGUIBUTTONS);
		
		//check for chapter, if chapter == true, have this big tab, else small tab
		
		// draw the chapter retract/expand buttons / backgrounds
		//signed = false;
		if (!signing)
		{
			for (int i = 0; i < 8; i++)
			{
				if (chapterBool[i])
				{
					if (signed)
					{
						this.drawTexturedModalRect(widthLocal+212, heightLocal+6+(spacing*i), 0, 171, 100, 28);
					}
					else
					{
						if (x > widthLocal+299 && x < widthLocal+311 && y > (heightLocal+16+(spacing*i)) && y < (heightLocal+21+(spacing*i)))
						{
							// expanded chapter on
							this.drawTexturedModalRect(widthLocal+212, heightLocal+6+(spacing*i), 0, 84, 100, 28);
						}
						else
						{
							// expanded chapter off
							this.drawTexturedModalRect(widthLocal+212, heightLocal+6+(spacing*i), 0, 55, 100, 28);
						}
					}
					//here is the best place for the chapter goto buttons
					this.drawTexturedModalRect(widthLocal+205, heightLocal+15+(spacing*i), 62, 13, 17, 10);
					if (this.currentPage == this.chapterPageNums[i])
					{
						this.drawTexturedModalRect(widthLocal+205, heightLocal+15+(spacing*i), 62, 35, 17, 10);
					}
					if (x > widthLocal+207 && x < widthLocal+221 && y > (heightLocal+10+(spacing*i)) && y < (heightLocal+29+(spacing*i)))
					{
						this.drawTexturedModalRect(widthLocal+205, heightLocal+15+(spacing*i), 62, 24, 17, 10);
					}
					//
				}
				else
				{
					if (!signed)
					{
						if (x > widthLocal+212 && x < widthLocal+222 && y > (heightLocal+14+(spacing*i)) && y < (heightLocal+25+(spacing*i)))
						{
							// retracted chapter on
							this.drawTexturedModalRect(widthLocal+212, heightLocal+6+(spacing*i), 0, 142, 95, 28);
						}
						else
						{
							// retracted chapter off
							this.drawTexturedModalRect(widthLocal+212, heightLocal+6+(spacing*i), 0, 113, 16, 28);
						}
					}
				}
			}
		}
		if (!(signed || signing))
		{
			this.drawTexturedModalRect(widthLocal-60, heightLocal+65, 0, 0, 60, 53);   // colors button
			this.drawTexturedModalRect(widthLocal-60, heightLocal+137, 0, 200, 60, 54); // solid gray scaling square
			// draw the highlights for the colors buttons
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 4; j++)
				{
					if (x > (widthLocal-60+(j*15)) && x < (widthLocal-45+(j*15)) && y > (heightLocal+66+(i*13)) && y < (heightLocal+79+(i*13)))
					{
						//System.out.println("true");
						this.drawTexturedModalRect(widthLocal-59+(j*15), heightLocal+66+(i*13), 62, 0, 13, 12);
					}
				}
			}
		}
		// drag the page forward button
		if (!signing)
		{
			if (this.buttonPageForward.enabled)
			{
				if (x > widthLocal+149 && x < widthLocal+169 && y > heightLocal+234 &&  y < heightLocal+246)
				{
					this.drawTexturedModalRect(widthLocal+150, heightLocal+235, 103, 0, 18, 10);
				}
				else
				{
					this.drawTexturedModalRect(widthLocal+150, heightLocal+235, 80, 0, 18, 10);
				}
			}
			
			// draw the page back button
			if (this.buttonPageBack.enabled)
			{
				if (x > widthLocal+44 && x < widthLocal+64 && y > heightLocal+234 &&  y < heightLocal+246)
				{
					this.drawTexturedModalRect(widthLocal+45, heightLocal+235, 103, 13, 18, 10);
				}
				else
				{
					this.drawTexturedModalRect(widthLocal+45, heightLocal+235, 80, 13, 18, 10);
				}
			}
			
			// first and last page buttons.
			if (this.buttonPageFirst.enabled)
			{
				if (x > widthLocal+14 && x < widthLocal+32 && y > heightLocal+234 &&  y < heightLocal+246)
				{
					this.drawTexturedModalRect(widthLocal+15, heightLocal+234, 80, 37, 18, 10);
				}
				else
				{
					this.drawTexturedModalRect(widthLocal+15, heightLocal+234, 80, 26, 18, 10);
				}
			}
			if (this.buttonPageLast.enabled)
			{
				if (x > widthLocal+178 && x < widthLocal+197 && y > heightLocal+234 &&  y < heightLocal+246)
				{
					this.drawTexturedModalRect(widthLocal+179, heightLocal+234, 103, 37, 18, 10);
				}
				else
				{
					this.drawTexturedModalRect(widthLocal+179, heightLocal+234, 103, 26, 18, 10);
				}
			}
	
			// draw the text boxes
	    	for (int n=0; n<numOfLines; n++)
	    	{
	    		//System.out.println("This test");
	    		//System.out.println(this.lines[n].getText());
	    		//System.out.println(this.lines[n].getText().length());
	    		//System.out.println(this.lines[n].getCursorPosition());
	    		if (this.lines[n].getCursorPosition() > this.lines[n].getText().length())
	    		{
	    			this.lines[n].setCursorPosition(this.lines[n].getText().length() - 1);
	    		}
	    		if (this.lines[n].getMaxStringLength() < this.lines[n].getText().length())
	    		{
	    			this.lines[n].setText(this.lines[n].getText().substring(0, this.lines[n].getMaxStringLength()));
	    		}
	    		if (this.lines[n].getText().length() > 0)
	    		{
		    		GL11.glScalef(scales[lineScales[n]], scales[lineScales[n]], scales[lineScales[n]]);
		    		this.lines[n].drawTextBox();
		    		GL11.glScalef(antiscales[lineScales[n]], antiscales[lineScales[n]], antiscales[lineScales[n]]);
	    		}
	    	}
	    	
	    	//mcount = 0;
	    	for (int n=0, m=0; n<16; n++)
	    	{
	    		if (chapterBool[m])
	    		{
	    			this.chapters[n].drawTextBox();
	    		}
	    		if (n%2 != 0)
	    		{
	    			m++;
	    		}
	    	}
		}
    	//
    	
    	
		super.drawScreen(x, y, f);
		
		if (!(signing || signed))
		{
			this.drawCenteredString(this.fontRenderer, this.textScale, widthLocal-31, heightLocal+142, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, this.textScaling[lineScales[this.currentLine]] , widthLocal-30, heightLocal+160, 0x00FF00);
			this.drawCenteredString(this.fontRenderer, I18n.translateToLocal("book.linenum") , widthLocal-39, heightLocal+178, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, this.currentLine+1+"", widthLocal-10, heightLocal+178, 0x00FF00);
		}
		if (!signing)
		{
			pagesTextLine = this.currentPage+1+"  of  "+this.totalPages;
			this.fontRenderer.drawString(pagesTextLine, widthLocal+103-((pagesTextLine.length()*4)/2), heightLocal+237, 0x000000, false);
		}
		
		if (signing)
		{
			///render "type title here" text and notice text
			this.signTextField.drawTextBox();
			this.fontRenderer.drawString(this.signingInstruct, widthLocal+22, heightLocal+65, 0x616161, false);
			this.fontRenderer.drawSplitString(this.signingNotice, widthLocal+22, heightLocal+150, 180, 0x616161);
			this.fontRenderer.drawString(this.signedBy, widthLocal+100, heightLocal+100, 0x000000, false);
			this.fontRenderer.drawString(this.signedAuthor, widthLocal+102-((this.signedAuthor.length()*5)/2), heightLocal+115, 0x000000, false);
		}
	}
	
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	//System.out.println(click.id);
    	
    	switch (click.id)
    	{
    		case(0):
    		{
    			saveCurrentPageToNBT();
    			sendPacket();
    			this.mc.player.closeScreen();
    			break;
    		}
    		case(1):
    		{
    			//System.out.println("ready to sign the book!");
    			this.signing = true;
    			saveCurrentPageToNBT();
    			loadCurrentPageLinesFromNBT();
    			initGui();
    			this.signTextField.setFocused(true);
    			break;
    		}
    		case(2):
    		{
    			this.signing = false;
    			this.signTextField.setFocused(false);
    			initGui();
    			break;
    		}
    		case(3):
    		{
    			this.signed = true;
    			this.signing = false;
    			this.signTextField.setFocused(false);
    			saveSigningToNBT();
    			initGui();
    			break;
    		}
    		case(10):
    		{
    			String moddedLine = addTextMod(lines[this.currentLine].getText(), TextFormatting.BOLD+"");
    			lines[this.currentLine].setText(moddedLine);
    			break;
    		}
    		case(11):
    		{
    			String moddedLine = addTextMod(lines[this.currentLine].getText(), TextFormatting.ITALIC+"");
    			lines[this.currentLine].setText(moddedLine);
    			break;
    		}
    		case(12):
    		{
    			String moddedLine = addTextMod(lines[this.currentLine].getText(), TextFormatting.UNDERLINE+"");
    			lines[this.currentLine].setText(moddedLine);
    			break;
    		}
    		case(13):
    		{
    			String moddedLine = addTextMod(lines[this.currentLine].getText(), TextFormatting.STRIKETHROUGH+"");
    			lines[this.currentLine].setText(moddedLine);
    			break;
    		}
    		case(14):
    		{
    			String moddedLine = addTextMod(lines[this.currentLine].getText(), TextFormatting.OBFUSCATED+"");
    			lines[this.currentLine].setText(moddedLine);
    			break;
    		}
    		case(15):
    		{
    			String moddedLine = addTextMod(lines[this.currentLine].getText(), TextFormatting.RESET+"");
    			lines[this.currentLine].setText(moddedLine);
    			break;
    		}
    		case(16):
    		{
    			if (lineScales[currentLine] < 7)
        		{
        			lineScales[currentLine]++;
        			saveCurrentPageToNBT();
        			loadCurrentPageLinesFromNBT();
        			initGui();
        		}
        		else
        		{
        			lineScales[currentLine] = 0;
        			saveCurrentPageToNBT();
        			loadCurrentPageLinesFromNBT();
        			initGui();
        		}
    			break;
    		}
    		case(17):
    		{
    			if (lineScales[currentLine] > 0)
        		{
        			lineScales[currentLine]--;
        			saveCurrentPageToNBT();
        			loadCurrentPageLinesFromNBT();
        			initGui();
        		}
        		else
        		{
        			lineScales[currentLine] = 7;
        			saveCurrentPageToNBT();
        			loadCurrentPageLinesFromNBT();
        			initGui();
        		}
    			break;
    		}
    		case(18):
    		{
    			//prev page
    			if (currentPage > 0)
    			{
    				saveCurrentPageToNBT();
    				currentPage--;
    				loadCurrentPageLinesFromNBT();
        			initGui();
    			}
    			break;
    		}
    		case(19):
    		{
				saveCurrentPageToNBT();
				currentPage++;
				if (this.totalPages == this.currentPage)
				{
					this.totalPages++;
				}
				loadCurrentPageLinesFromNBT();
    			initGui();
    			break;
    		}
    		case (20):
    		{
    			saveCurrentPageToNBT();
    			this.currentPage = 0;
    			loadCurrentPageLinesFromNBT();
    			initGui();
    			break;
    		}
    		case(21):
    		{
    			saveCurrentPageToNBT();
    			this.currentPage = this.totalPages-1;
    			loadCurrentPageLinesFromNBT();
    			initGui();
    			break;
    		}
    	}
    }
    
    public String addTextMod(String line, String mod)
    {
    	String partA = "";
    	String partB = "";
    	if (this.currentLineCursorPos > 0 && this.currentLineCursorPos <= line.length() && line.length() > 0)
    	{
    		partA = line.substring(0, this.currentLineCursorPos);  
    		partB = line.substring(this.currentLineCursorPos, line.length()); 
    		return partA+mod+partB;
    	}
    	else
    	{
    		return mod+line;
    	}
    }
    
    public void sendPacket()
    {
    	ByteBuf buffer = Unpooled.buffer();
    	ByteBufUtils.writeItemStack(buffer, book);
    	if (isinhand)
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(book, false));
	    	//BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
    	}
    	else
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioMCBEdit(new BlockPos(tilex, tiley, tilez), this.currentPage, book));
        	// buffer.writeInt(tilex);
        	// buffer.writeInt(tiley);
        	// buffer.writeInt(tilez);
        	// buffer.writeInt(this.currentPage);
        	// BiblioCraft.ch_BiblioMCBEdit.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMCBEdit"));
    	}
    }
    
	@Override
    protected void mouseClicked(int x, int y, int par3)
    {
		try 
		{
			super.mouseClicked(x, y, par3);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if (!(signed || signing))
		{
	    	for (int n=0; n<numOfLines; n++)
	    	{
	    		if (this.lines[n].mouseClicked((int)(x*antiscales[lineScales[n]]), (int)(y*antiscales[lineScales[n]]), par3))
	    		{
	    			this.currentLine = n;
	    			this.currentLineCursorPos = this.lines[n].getCursorPosition();
	    		}
	    	}
	    	
	    	for (int n=0, m=0; n<16; n++)
	    	{
	    		if (this.chapterBool[m])
	    		{
	    			if (this.chapters[n].mouseClicked(x, y, par3))
	    			{
	    				//System.out.println("selected chapter text line "+n);
	    			}
	    		}
	    		if (n%2 != 0)
	    		{
	    			m++;
	    		}
	    	}
	    	// catch clicks for the colors
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 4; j++)
				{
					if (x > (widthLocal-60+(j*15)) && x < (widthLocal-45+(j*15)) && y > (heightLocal+66+(i*13)) && y < (heightLocal+79+(i*13)))
					{
						String moddedLine = addTextMod(lines[this.currentLine].getText(), mods[i][j]);
		    			lines[this.currentLine].setText(moddedLine);
					}
				}
			}
		}
    	//System.out.println(x+"     "+y);
    	for (int i = 0; i<8; i++)
    	{
			if (chapterBool[i])
			{
				if (x > widthLocal+299 && x < widthLocal+311 && y > (heightLocal+16+(spacing*i)) && y < (heightLocal+21+(spacing*i)) && !(signed || signing))
				{
					chapterBool[i] = false;
				}
				if (x > widthLocal+207 && x < widthLocal+221 && y > (heightLocal+10+(spacing*i)) && y < (heightLocal+29+(spacing*i)))
				{
					//System.out.println("goto chapter");
					saveCurrentPageToNBT();
					this.currentPage = this.chapterPageNums[i];
        			loadCurrentPageLinesFromNBT();
        			initGui();
				}
			}
			else
			{
				if (x > widthLocal+212 && x < widthLocal+222 && y > (heightLocal+14+(spacing*i)) && y < (heightLocal+25+(spacing*i)) && !(signed || signing))
				{
					//this.drawTexturedModalRect(widthLocal+212, heightLocal+6+(spacing*i), 0, 142, 95, 28);
					chapterBool[i] = true;
					// I probly shouldnt change the page setting if the book is signed?
					chapterPageNums[i] = this.currentPage;
				}
			}
    	}
    	
    	


    	
    }
	
	@Override
	protected void keyTyped(char par1, int key)
	{
        if (key == 1)
        {
        	NBTTagCompound tags = this.book.getTagCompound();
        	if (tags != null)
        	{
        			saveCurrentPageToNBT();
        			sendPacket();
        	}
            this.mc.player.closeScreen();
        }
        
        if (signing)
        {
        	this.signTextField.textboxKeyTyped(par1, key);
        }
        //System.out.println(key);
        if (!(signed || signing))
        {
	        for (int n = 0; n<44; n++)
	        {
	        	if (lines[n].isFocused())
	        	{
	        		lines[n].textboxKeyTyped(par1, key);
	                String textTest = this.lines[n].getText();
	                int formcount = 0;
	                //System.out.println(textTest.length());
	                this.currentLineCursorPos = this.lines[n].getCursorPosition();
	                if (textTest.length() > 0)
	                {
		                for (int m = 0; m<textTest.length(); m++)
		                {
		                	if (textTest.substring(m, m+1).contains("\u00a7")) 
		                	{
		                		formcount++;
		                	}
		                }
	                }
	                formcount = formcount*2;
	                //System.out.println(this.text+" with extra space of"+formcount);
	               // this.maxStringLength += formcount;
	        		this.lines[n].setMaxStringLength((int) ((40+formcount)*antiscales[lineScales[n]]));
	        		
	        	}
	        }
	        
	        for (int n = 0; n<16; n++)
	        {
	        	if (chapters[n].isFocused())
	        	{
	        		chapters[n].textboxKeyTyped(par1, key);
	        	}
	        }
	        
	        //System.out.println(key);
	        if (key == 203 || key == 14)  // left arrow + backspace
	        {
	        	// left and right keys
	        	this.currentLineCursorPos = lines[this.currentLine].getCursorPosition();
	        	if (this.currentLineCursorPos == 0)
	        	{
	        		//System.out.println("true");
	        		if (this.currentLine > 0)
	        		{
	        			this.lines[this.currentLine].setFocused(false);
	        			this.currentLine--;
	        			this.lines[this.currentLine].setFocused(true);
	        			this.currentLineCursorPos = this.lines[this.currentLine].getCursorPosition();
	        		}
	        	}
	        }
	        if (key == 205) // right arrow
	        {
	        	this.currentLineCursorPos = lines[this.currentLine].getCursorPosition();
	        }
	        if (key == 200 && this.currentLine > 0)
	        {
	        	// 200 == up
	           	boolean isline = true;
	        	for (int n=0, m=0; n<15; n++)
	        	{
	        		if (chapterBool[m])
	        		{
		        		if (this.chapters[n].isFocused())
		        		{
		        			//System.out.println("foced on "+n);
		        			isline = false;
		        			if (n > 0)
		        			{
		        				chapters[n].setFocused(false);
		        				chapters[n-1].setFocused(true);
		        				break;
		        			}
		        		}
	        		}
	        		if (n%2 != 0)
	        		{
	        			m++;
	        		}
	        	}
	        	if (isline)
	        	{
		        	lines[this.currentLine].setFocused(false);
		        	this.currentLine--;
		        	lines[this.currentLine].setFocused(true);
	        	}
	        }
	        if ((key == 208 || key == 28) && this.currentLine < this.numOfLines-1)
	        {
	        	// 208 == down
	        	boolean isline = true;
	        	for (int n=0, m=0; n<15; n++)
	        	{
	        		if (chapterBool[m])
	        		{
		        		if (this.chapters[n].isFocused())
		        		{
		        			isline = false;
		        			if (n < 15)
		        			{
		        				chapters[n].setFocused(false);
		        				chapters[n+1].setFocused(true);
		        				break;
		        			}
		        		}
	        		}
	        		if (n%2 != 0)
	        		{
	        			m++;
	        		}
	        	}
	        	if (isline)
	        	{
		        	lines[this.currentLine].setFocused(false);
		        	this.currentLine++;
		        	lines[this.currentLine].setFocused(true);
	        	}
	        }
        }

	}
	
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    }
}
