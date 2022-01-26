package jds.bibliocraft.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.helpers.InventoryListItem;
import jds.bibliocraft.helpers.SortedListItem;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioStockCompass;
import jds.bibliocraft.network.packet.server.BiblioStockTitle;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class GuiStockCatalog extends GuiScreen
{
	ItemStack catalog;
	int[] compassSlots;
	ItemStack[] compassStacks;
    private int bookImageWidth = 220;
    private int bookImageHeight = 256;
    
    private int openSubMenu = -1;
    private ArrayList<SortedListItem> alphaList;
    private ArrayList<SortedListItem> quantaList;
    private ArrayList<SortedListItem> alphaReverseList;
    private ArrayList<SortedListItem> quantaReverseList;
    
    private ArrayList<SortedListItem> theList;
    
    private int listNum = 2; // 0 = alpha list, 1 = alpha list reversed, 2 = quantalist, 3 = quantalist reversed
    
    private GuiButtonGreenPageArrows bPrevPageMain;
    private GuiButtonGreenPageArrows bNextPageMain;
    private GuiButtonGreenPageArrows bPrevPageSub;
    private GuiButtonGreenPageArrows bNextPageSub;
    
    private GuiButtonAscendDescend bQuanSelector;
    private GuiButtonAscendDescend bAlphaSelector;
    
    private GuiButtonAddSubtract bAddToCompass1;
    private GuiButtonAddSubtract bAddToCompass2;
    private GuiButtonAddSubtract bAddToCompass3;
    private GuiButtonAddSubtract bAddToCompass4;
    private GuiButtonAddSubtract bAddToCompass5;
    
    private GuiButton32x14 bCloseButton;
    
    private GuiBiblioTextField titleText;
    
    private int pageNum = 0;
    private int totalPages = 1;
    private int invPageNum = 0;
    private int totalInvPages = 1;
    private int numEntries = 0;
    
    private int selectedCompass = 0;
    private boolean hasCompass = false;
    
    private String theTitle;
	
	public GuiStockCatalog(EntityPlayer player, ArrayList<SortedListItem> AlphaList, ArrayList<SortedListItem> QuantaList, ItemStack[] stacks, int[] compasses, String title)
	{
		this.allowUserInput = true;
		//this.catalog = stack;
		this.compassStacks = stacks;
		this.compassSlots = compasses;
		this.alphaList = AlphaList;
		this.quantaList = QuantaList;
		this.alphaReverseList = getReverseList(alphaList);
		this.quantaReverseList = getReverseList(quantaList);
		
		this.totalPages = alphaList.size() / 22;
		if (alphaList.size() % 22 != 0)
		{
			this.totalPages++;
		}
		if (this.totalPages == 0)
		{
			this.totalPages = 1;
		}
		theTitle = title.replace(TextFormatting.WHITE+"", "");
		
		if (this.alphaList.size() > 0 && this.alphaList.size() <= 22)
		{
			this.numEntries = this.alphaList.size();
		}
		else if (this.alphaList.size() > 22)
		{
			this.numEntries = 22;
		}
		
	}
	
	 private ArrayList<SortedListItem> getReverseList(ArrayList<SortedListItem> list)
	 {
		 ArrayList<SortedListItem> newList = new  ArrayList<SortedListItem>();
		 for (int i = list.size()-1; i >= 0; i--)
		 {
			 newList.add(list.get(i));
		 }
		 return newList;
	 }
	
    @Override
    public void initGui()
    {
    	super.initGui();
    	Keyboard.enableRepeatEvents(true);
    	int width = (this.width - this.bookImageWidth) / 2;
    	int height = (this.height - this.bookImageHeight) / 2;
    	buttonList.clear();
    	buttonList.add(this.bNextPageMain = new GuiButtonGreenPageArrows(1, width+150, height+235, true));
    	buttonList.add(this.bPrevPageMain = new GuiButtonGreenPageArrows(0, width+50, height+235, false));
    	buttonList.add(this.bNextPageSub = new GuiButtonGreenPageArrows(3, width+142, height+217, true));
    	buttonList.add(this.bPrevPageSub = new GuiButtonGreenPageArrows(2, width+42, height+217, false));


    	
    	buttonList.add(this.bQuanSelector = new GuiButtonAscendDescend(5, width+47, height+28, false));
    	buttonList.add(this.bAlphaSelector = new GuiButtonAscendDescend(6, width+177, height+28, false));
    	this.bQuanSelector.setIsSelected(true);

    	buttonList.add(this.bAddToCompass1 = new GuiButtonAddSubtract(10, width+174, height+74, 0, 0.75f));
    	buttonList.add(this.bAddToCompass2 = new GuiButtonAddSubtract(11, width+174, height+104, 0, 0.75f));
    	buttonList.add(this.bAddToCompass3 = new GuiButtonAddSubtract(12, width+174, height+134, 0, 0.75f));
    	buttonList.add(this.bAddToCompass4 = new GuiButtonAddSubtract(13, width+174, height+164, 0, 0.75f));
    	buttonList.add(this.bAddToCompass5 = new GuiButtonAddSubtract(14, width+174, height+194, 0, 0.75f));
    	
    	buttonList.add(this.bCloseButton = new GuiButton32x14(4, width+174, height+233)); 
    	
    	this.titleText = new GuiBiblioTextField(fontRenderer, width+41, height+14, 138, 10);
    	this.titleText.setEnableBackgroundDrawing(false);
    	this.titleText.setMaxStringLength(24);
    	this.titleText.setTextColor(0x000000);
    	this.titleText.setText(this.theTitle);

    	if (this.openSubMenu == -1)
    	{
			if (this.pageNum < this.totalPages-1)
			{
				this.bNextPageMain.enabled = true;
				this.bNextPageMain.visible = true;
			}
			else
			{
				this.bNextPageMain.enabled = false;
				this.bNextPageMain.visible = false;
			}
			
			if (this.pageNum != 0)
			{
    			this.bPrevPageMain.enabled = true;
    			this.bPrevPageMain.visible = true;
			}
			else
			{
				this.bPrevPageMain.enabled = false;
    			this.bPrevPageMain.visible = false;
			}
	    	this.bPrevPageSub.visible = false;
	    	this.bNextPageSub.enabled = false;
	    	this.bPrevPageSub.enabled = false;
	    	this.bNextPageSub.visible = false;
	    	
	    	this.bAddToCompass1.enabled = false;
	    	this.bAddToCompass1.visible = false;
	    	this.bAddToCompass2.enabled = false;
	    	this.bAddToCompass2.visible = false;
	    	this.bAddToCompass3.enabled = false;
	    	this.bAddToCompass3.visible = false;
	    	this.bAddToCompass4.enabled = false;
	    	this.bAddToCompass4.visible = false;
	    	this.bAddToCompass5.enabled = false;
	    	this.bAddToCompass5.visible = false;
    	}
    	else
    	{
	    	this.bPrevPageMain.enabled = false;
	    	this.bPrevPageMain.visible = false;
	    	this.bNextPageMain.enabled = false;
	    	this.bNextPageMain.visible = false;
	    	this.bQuanSelector.enabled = false;
	    	this.bQuanSelector.visible = false;
	    	this.bAlphaSelector.enabled = false;
	    	this.bAlphaSelector.visible = false;
	    	
			this.bNextPageSub.enabled = (this.invPageNum < this.totalInvPages-1);
			this.bNextPageSub.visible = (this.invPageNum < this.totalInvPages-1);
			
			this.bPrevPageSub.enabled = (this.invPageNum > 0);
			this.bPrevPageSub.visible = (this.invPageNum > 0);
    	}
    }
    
	@Override
	public void drawScreen(int x, int y, float f)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    	int width = (this.width - this.bookImageWidth) / 2;
    	int height = (this.height - this.bookImageHeight) / 2;
		this. mc.getTextureManager().bindTexture(CommonProxy.STOCKROOMCATALOGGUI);
		this.drawTexturedModalRect(width, height, 0, 0, this.bookImageWidth, this.bookImageHeight);
		
		if(this.openSubMenu != -1)
		{
			this. mc.getTextureManager().bindTexture(CommonProxy.STOCKROOMCATALOGSUBGUI);
			this.drawTexturedModalRect(width, height, 0, 0, this.bookImageWidth, this.bookImageHeight);
			if (hasCompass)
			{
				//
				this.drawTexturedModalRect((width+173), (height+52), 249, 0, 7, 150);
				
				if (this.selectedCompass < 7)
				{
					int compassLine = 7 - this.selectedCompass;
					for (int i = 0; i < compassLine; i++)
					{
						this.drawTexturedModalRect((width+153-(i*20)), (height+52), 222, 52, 20, 2);
					}
				}
				this.drawTexturedModalRect((width+21+(this.selectedCompass*20)), (height+31), 222, 57, 20, 23);
			}
		}
		
		this. mc.getTextureManager().bindTexture(CommonProxy.STOCKROOMCATALOGGUI);
		float scaler = 0.2f;
		float antiscaler = 1.0f/scaler;
		GL11.glScalef(scaler, scaler, scaler);
		for (int i = 0; i < numEntries; i++)
		{
			if (this.openSubMenu == i+(this.pageNum*22))
			{
				this.drawTexturedModalRect((int)((width+194)*antiscaler), (int)((height+35+(i*9))*antiscaler), 225, 215, 31, 41);
			}
			else
			{
				this.drawTexturedModalRect((int)((width+194)*antiscaler), (int)((height+37+(i*9))*antiscaler), 225, 180, 31, 32);
			}
		}
		GL11.glScalef(antiscaler, antiscaler, antiscaler);
		
		

		
		scaler = 0.8f;
		antiscaler = 1.0f/scaler;
		GL11.glScalef(scaler, scaler, scaler);
		switch (this.listNum)
		{
			case 0:{this.theList = this.alphaList; break;}
			case 1:{this.theList = this.alphaReverseList; break;}
			case 2:{this.theList = this.quantaList; break;}
			case 3:{this.theList = this.quantaReverseList; break;}
			default:{this.theList = this.quantaList; break;}
		}
		
		if (this.openSubMenu == -1)
		{
			
			// render list
			if (this.bQuanSelector.getIsSelected())
			{
				this.fontRenderer.drawString("\u00a72"+I18n.translateToLocal("gui.catalog.count"), (int)((width+18)*antiscaler), (int)((height+27)*antiscaler), 4210752);
				this.fontRenderer.drawString("\u00a77"+I18n.translateToLocal("gui.catalog.names"), (int)((width+60)*antiscaler), (int)((height+27)*antiscaler), 4210752);
			}
			else
			{
				this.fontRenderer.drawString("\u00a77"+I18n.translateToLocal("gui.catalog.count"), (int)((width+18)*antiscaler), (int)((height+27)*antiscaler), 4210752);
				this.fontRenderer.drawString("\u00a72"+I18n.translateToLocal("gui.catalog.names"), (int)((width+60)*antiscaler), (int)((height+27)*antiscaler), 4210752);
			}

			this.fontRenderer.drawString(I18n.translateToLocal("gui.paintpress.page")+" "+(this.pageNum+1)+" "+I18n.translateToLocal("gui.paintpress.of")+" "+this.totalPages, (int)((width+85)*antiscaler), (int)((height+237)*antiscaler), 4210752);
			
			for (int i = 0; i < quantaList.size(); i++)
			{
				if (i >= (this.pageNum*22) && i < 22+(this.pageNum*22))
				{
					this.fontRenderer.drawString(theList.get(i).itemQuantity+"", (int)((width+18)*antiscaler), (int)((height+37+((i-pageNum*22)*9))*antiscaler), 4210752);
					this.fontRenderer.drawString(I18n.translateToLocal(theList.get(i).itemName+".name"), (int)((width+60)*antiscaler), (int)((height+37+((i-pageNum*22)*9))*antiscaler), 4210752);
				}
			}
		}
		else
		{
			// render sub menu list
			ArrayList<InventoryListItem> invList = this.theList.get(this.openSubMenu).inventoryList;
			this.fontRenderer.drawString(I18n.translateToLocal("gui.paintpress.page")+" "+(this.invPageNum+1)+" "+I18n.translateToLocal("gui.paintpress.of")+" "+this.totalInvPages, (int)((width+77)*antiscaler), (int)((height+220)*antiscaler), 4210752);
			this.fontRenderer.drawString("\u00a72"+I18n.translateToLocal(this.theList.get(this.openSubMenu).itemName+".name"), (int)((width+33)*antiscaler), (int)((height+56)*antiscaler), 4210752);
			
	    	this.bAddToCompass1.enabled = false;
	    	this.bAddToCompass1.visible = false;
	    	this.bAddToCompass2.enabled = false;
	    	this.bAddToCompass2.visible = false;
	    	this.bAddToCompass3.enabled = false;
	    	this.bAddToCompass3.visible = false;
	    	this.bAddToCompass4.enabled = false;
	    	this.bAddToCompass4.visible = false;
	    	this.bAddToCompass5.enabled = false;
	    	this.bAddToCompass5.visible = false;
			for (int i = 0; i < invList.size(); i++)
			{
				if (i >= (this.invPageNum*5) && i < 5+(this.invPageNum*5))
				{
					this.fontRenderer.drawString(invList.get(i).itemQuantity+"", (int)((width+33)*antiscaler), (int)((height+71+((i-invPageNum*5)*30))*antiscaler), 4210752);
					this.fontRenderer.drawString(I18n.translateToLocal(invList.get(i).inventoryName), (int)((width+70)*antiscaler), (int)((height+71+((i-invPageNum*5)*30))*antiscaler), 4210752);
					this.fontRenderer.drawString("X: "+invList.get(i).tileX, (int)((width+33)*antiscaler), (int)((height+84+((i-invPageNum*5)*30))*antiscaler), 4210752);
					this.fontRenderer.drawString("Y: "+invList.get(i).tileY, (int)((width+81)*antiscaler), (int)((height+84+((i-invPageNum*5)*30))*antiscaler), 4210752);
					this.fontRenderer.drawString("Z: "+invList.get(i).tileZ, (int)((width+129)*antiscaler), (int)((height+84+((i-invPageNum*5)*30))*antiscaler), 4210752);
					if ((i-invPageNum*5) == 0 && hasCompass)
					{
						this.bAddToCompass1.enabled = true;
						this.bAddToCompass1.visible = true;
					}
					if ((i-invPageNum*5) == 1 && hasCompass)
					{
	    		    	this.bAddToCompass2.enabled = true;
	    		    	this.bAddToCompass2.visible = true;
					}
					if ((i-invPageNum*5) == 2 && hasCompass)
					{
	    		    	this.bAddToCompass3.enabled = true;
	    		    	this.bAddToCompass3.visible = true;
					}
					if ((i-invPageNum*5) == 3 && hasCompass)
					{
	    		    	this.bAddToCompass4.enabled = true;
	    		    	this.bAddToCompass4.visible = true;
					}
					if ((i-invPageNum*5) == 4 && hasCompass)
					{
	    		    	this.bAddToCompass5.enabled = true;
	    		    	this.bAddToCompass5.visible = true;
					}
				}
			}
		}
		
		GL11.glScalef(antiscaler, antiscaler, antiscaler);
		this.titleText.drawTextBox();
		//this.drawTexturedModalRect((int)((width+194)*antiscaler), (int)((height+26)*antiscaler), 225, 215, 31, 41);
		hasCompass = false;
		if (this.openSubMenu != -1)
		{
			for (int i = 0; i < this.compassSlots.length; i++)
			{
				if (this.compassSlots[i] != -1)
				{
					ItemStack stack = this.compassStacks[i];
					if (stack != ItemStack.EMPTY)
					{
						this.itemRender.renderItemAndEffectIntoGUI(stack, width+23+(20*i), height+33);
						hasCompass = true;
					}
				}
			}
			

		}

		
		super.drawScreen(x, y, f);
		
		if (this.hasCompass && this.openSubMenu != -1)
		{
			for (int j = 0; j < this.compassSlots.length; j++)
			{
				if (x > (width+21+(j*20)) && x < ((width+41)+(j*20)) && y > (height+32) &&  y < (height+50))
				{
					ItemStack stack = compassStacks[j];
					if (stack != ItemStack.EMPTY)
					{
						NBTTagCompound tags = stack.getTagCompound();
						if (tags != null)
						{
							int sX = tags.getInteger("XCoord");
							int sZ = tags.getInteger("ZCoord");
							String waypoint = tags.getString("WaypointName");
							String tooltip = "@  X = "+sX+"   Z = "+sZ;
							List lst = new ArrayList();
							lst.add(waypoint);
							lst.add(tooltip);
							this.drawHoveringText(lst, x, y, fontRenderer);
						}
					}
					
				}
			}

		}
		
		GL11.glScalef(scaler, scaler, scaler);
		if (this.openSubMenu == -1)
		{
			this.fontRenderer.drawString(I18n.translateToLocal("gui.typesetting.exit"), (int)((width+183)*antiscaler), (int)((height+237)*antiscaler), 4210752);
		}
		else
		{
			this.fontRenderer.drawString(I18n.translateToLocal("gui.catalog.close"), (int)((width+180)*antiscaler), (int)((height+237)*antiscaler), 4210752); 
		}
		GL11.glScalef(antiscaler, antiscaler, antiscaler);
	}
	
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	switch (click.id)
    	{
	    	case 0:
	    	{
	    		if (this.pageNum > 0)
	    		{
	    			this.pageNum--;
	    		}
	    		if (this.pageNum == 0)
	    		{
	    			this.bPrevPageMain.enabled = false;
	    			this.bPrevPageMain.visible = false;
	    		}
	    		if (this.pageNum < this.totalPages-1)
	    		{
	    			this.bNextPageMain.enabled = true;
	    			this.bNextPageMain.visible = true;
	    		}
	    		this.numEntries = 22;
	    		break;
	    	}
	    	case 1:
	    	{
	    		if (this.pageNum < this.totalPages-1)
	    		{
	    			this.pageNum++;
	    		}
	    		
	    		if (!(this.pageNum < this.totalPages-1))
	    		{
	    			this.bNextPageMain.enabled = false;
	    			this.bNextPageMain.visible = false;
	    		}
	    		
	    		if (this.pageNum > 0)
	    		{
	    			this.bPrevPageMain.enabled = true;
	    			this.bPrevPageMain.visible = true;
	    		}
	    		
	    		this.numEntries = 22;
	    		if (this.pageNum == this.totalPages-1)
	    		{
	    			numEntries = this.quantaList.size()-(this.pageNum*22);
	    		}
	    		break;
	    	}
	    	case 2:
	    	{
	    		if (this.invPageNum > 0)
	    		{
	    			this.invPageNum--;
	    		}
	    		if (this.invPageNum == 0)
	    		{
	    			this.bPrevPageSub.enabled = false;
	    			this.bPrevPageSub.visible = false;
	    		}
	    		if (this.invPageNum < this.totalInvPages-1)
	    		{
	    			this.bNextPageSub.enabled = true;
	    			this.bNextPageSub.visible = true;
	    		}
	    		break;
	    	}
	    	case 3:
	    	{
	    		if (this.invPageNum < this.totalInvPages-1)
	    		{
	    			this.invPageNum++;
	    		}
	    		
	    		if (!(this.invPageNum < this.totalInvPages-1))
	    		{
	    			this.bNextPageSub.enabled = false;
	    			this.bNextPageSub.visible = false;
	    		}
	    		if (this.invPageNum > 0)
	    		{
	    			this.bPrevPageSub.enabled = true;
	    			this.bPrevPageSub.visible = true;
	    		}
	    		
	    		break;
	    	}
	    	case 4:
	    	{
	    		if (this.openSubMenu != -1)
	        	{
	    			this.openSubMenu = -1;
	    			this.bAlphaSelector.enabled = true;
	    			this.bAlphaSelector.visible = true;
	    			this.bQuanSelector.enabled = true;
	    			this.bQuanSelector.visible = true;
	    			
	    			if (this.pageNum < this.totalPages-1)
	    			{
	    				this.bNextPageMain.enabled = true;
	    				this.bNextPageMain.visible = true;
	    			}
	    			
	    			if (this.pageNum != 0)
	    			{
		    			this.bPrevPageMain.enabled = true;
		    			this.bPrevPageMain.visible = true;
	    			}

	    			this.bNextPageSub.enabled = false;
	    			this.bPrevPageSub.enabled = false;
	    			this.bNextPageSub.visible = false;
	    			this.bPrevPageSub.visible = false;
	    			
	    			this.bAddToCompass1.enabled = false;
	    			this.bAddToCompass1.visible = false;
	    			this.bAddToCompass2.enabled = false;
	    			this.bAddToCompass2.visible = false;
	    			this.bAddToCompass3.enabled = false;
	    			this.bAddToCompass3.visible = false;
	    			this.bAddToCompass4.enabled = false;
	    			this.bAddToCompass4.visible = false;
	    			this.bAddToCompass5.enabled = false;
	    			this.bAddToCompass5.visible = false;
	        	}
	        	else
	        	{
	        		this.mc.player.closeScreen();
	        	}
	    		this.bCloseButton.pressed = true;
	    		break;
	    	}
	    	case 5:
	    	{
	    		if (this.bQuanSelector.getIsSelected())
	    		{
	    			this.bQuanSelector.setAscendOrDescend(!this.bQuanSelector.getIsAscend());
	    		}
	    		else
	    		{
	    			this.bQuanSelector.setIsSelected(true);
	    			this.bAlphaSelector.setIsSelected(false);
	    		}
	    		if (this.bQuanSelector.getIsAscend())
	    		{
	    			this.listNum = 3;
	    		}
	    		else
	    		{
	    			this.listNum = 2;
	    		}
	    		break;
	    	}
	    	case 6:
	    	{
	    		if (this.bAlphaSelector.getIsSelected())
	    		{
	    			this.bAlphaSelector.setAscendOrDescend(!this.bAlphaSelector.getIsAscend());
	    		}
	    		else
	    		{
	    			this.bAlphaSelector.setIsSelected(true);
	    			this.bQuanSelector.setIsSelected(false);
	    		}
	    		if (this.bAlphaSelector.getIsAscend())
	    		{
	    			this.listNum = 1;
	    		}
	    		else
	    		{
	    			this.listNum = 0;
	    		}
	    		break;
	    	}
	    	
	    	case 10:
	    	{
	    		if (hasCompass)
	    		{
	    			sendCompassUpdatePacket((0+invPageNum*5));
	    			this.bAddToCompass1.pressed = true;
	    		}
	    		break;
	    	}
	    	case 11:
	    	{
	    		if (hasCompass)
	    		{
	    			sendCompassUpdatePacket((1+invPageNum*5));
	    			this.bAddToCompass2.pressed = true;
	    		}
	    		break;
	    	}
	    	case 12:
	    	{
	    		if (hasCompass)
	    		{
	    			sendCompassUpdatePacket((2+invPageNum*5));
	    			this.bAddToCompass3.pressed = true;
	    		}
	    		break;
	    	}
	    	case 13:
	    	{
	    		if (hasCompass)
	    		{
	    			sendCompassUpdatePacket((3+invPageNum*5));
	    			this.bAddToCompass4.pressed = true;
	    		}
	    		break;
	    	}
	    	case 14:
	    	{
	    		if (hasCompass)
	    		{
	    			sendCompassUpdatePacket((4+invPageNum*5));
	    			this.bAddToCompass5.pressed = true;
	    		}
	    		break;
	    	}
    	}
    }
    
    private void sendCompassUpdatePacket(int inventoryIndex)
    {
    	if (this.openSubMenu != -1)
    	{
    		ItemStack currentCompass = this.compassStacks[this.selectedCompass];
    		if (currentCompass != ItemStack.EMPTY)
    		{
    			NBTTagCompound compTags = currentCompass.getTagCompound();
    			if (compTags == null)
    			{
    				compTags = new NBTTagCompound();
    			}
	    		InventoryListItem item = this.theList.get(this.openSubMenu).inventoryList.get(inventoryIndex);
	    		int slot = this.compassSlots[this.selectedCompass];
	    		String invName = I18n.translateToLocal(item.itemName + ".name") + " in " + I18n.translateToLocal(item.inventoryName);
	    		int x = item.tileX;
	    		int z = item.tileZ;
	    		compTags.setString("WaypointName", invName);
	    		compTags.setInteger("XCoord", x);
	    		compTags.setInteger("ZCoord", z);
	    		currentCompass.setTagCompound(compTags);
	    		this.compassStacks[this.selectedCompass] = currentCompass;
				BiblioNetworking.INSTANCE.sendToServer(new BiblioStockCompass(slot, invName, x, z));
	        	// ByteBuf buffer = Unpooled.buffer();
	        	// buffer.writeInt(slot);
	        	// ByteBufUtils.writeUTF8String(buffer, invName);
	        	// buffer.writeInt(x);
	        	// buffer.writeInt(z);
	        	// BiblioCraft.ch_BiblioStoCatCompass.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioStockCompass"));
    		}

    	}
    }
    
	@Override
    protected void mouseClicked(int left, int top, int click)
    {
		try
		{
			super.mouseClicked(left, top, click);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    	int width = (this.width - this.bookImageWidth) / 2;
    	int height = (this.height - this.bookImageHeight) / 2;
    	this.titleText.mouseClicked(left, top, click);
    	for (int i = 0; i < numEntries; i++)
    	{
	    	if (left >= (width+194) && left <= (width+194+8) && top >= (height+35+(i*9))&& top < (height+35+8+(i*9)))
	    	{
	    		if (i+(this.pageNum*22) == this.openSubMenu)
	    		{
	    			this.openSubMenu = -1;
	    			this.bAlphaSelector.enabled = true;
	    			this.bAlphaSelector.visible = true;
	    			this.bQuanSelector.enabled = true;
	    			this.bQuanSelector.visible = true;
	    			
	    			if (this.pageNum < this.totalPages-1)
	    			{
	    				this.bNextPageMain.enabled = true;
	    				this.bNextPageMain.visible = true;
	    			}
	    			
	    			if (this.pageNum != 0)
	    			{
		    			this.bPrevPageMain.enabled = true;
		    			this.bPrevPageMain.visible = true;
	    			}

	    			this.bNextPageSub.enabled = false;
	    			this.bPrevPageSub.enabled = false;
	    			this.bNextPageSub.visible = false;
	    			this.bPrevPageSub.visible = false;
	    			
    		    	this.bAddToCompass1.enabled = false;
    		    	this.bAddToCompass1.visible = false;
    		    	this.bAddToCompass2.enabled = false;
    		    	this.bAddToCompass2.visible = false;
    		    	this.bAddToCompass3.enabled = false;
    		    	this.bAddToCompass3.visible = false;
    		    	this.bAddToCompass4.enabled = false;
    		    	this.bAddToCompass4.visible = false;
    		    	this.bAddToCompass5.enabled = false;
    		    	this.bAddToCompass5.visible = false;
	    			
	    			
	    		}
	    		else
	    		{
	    			this.invPageNum = 0;
	    			this.openSubMenu = i+(this.pageNum*22); 
	    			this.bAlphaSelector.enabled = false;
	    			this.bAlphaSelector.visible = false;
	    			this.bQuanSelector.enabled = false;
	    			this.bQuanSelector.visible = false;
	    			
	    			this.bNextPageMain.enabled = false;
	    			this.bNextPageMain.visible = false;
	    			
	    			this.bPrevPageMain.enabled = false;
	    			this.bPrevPageMain.visible = false;
	    			
	    			this.bPrevPageSub.enabled = false;
	    			this.bPrevPageSub.visible = false;
	    			if (this.theList != null)
	    			{
	    				int invListSize = this.theList.get(i+(this.pageNum*22)).inventoryList.size();
	    				this.totalInvPages = invListSize / 5;
	    				if (invListSize % 5 != 0)
	    				{
	    					this.totalInvPages++;
	    				}
	    			}
	    			
    				this.bNextPageSub.enabled = (this.totalInvPages > 1);
    				this.bNextPageSub.visible = (this.totalInvPages > 1);

				}
	    		break;
	    	}
    	}
    	
		for (int j = 0; j < this.compassSlots.length; j++)
		{
			if (left > (width+21+(j*20)) && left < ((width+41)+(j*20)) && top > (height+32) &&  top < (height+50))
			{
				if (this.compassSlots[j] != -1)
				{
					this.selectedCompass = j;
				}
			}
		}
    }
	
	@Override
	protected void keyTyped(char par1, int key)
	{
        if (key == 1)
        {
        	if (this.openSubMenu != -1)
        	{
    			this.openSubMenu = -1;
    			this.bAlphaSelector.enabled = true;
    			this.bAlphaSelector.visible = true;
    			this.bQuanSelector.enabled = true;
    			this.bQuanSelector.visible = true;
    			
    			if (this.pageNum < this.totalPages-1)
    			{
    				this.bNextPageMain.enabled = true;
    				this.bNextPageMain.visible = true;
    			}
    			
    			if (this.pageNum != 0)
    			{
	    			this.bPrevPageMain.enabled = true;
	    			this.bPrevPageMain.visible = true;
    			}

    			this.bNextPageSub.enabled = false;
    			this.bPrevPageSub.enabled = false;
    			this.bNextPageSub.visible = false;
    			this.bPrevPageSub.visible = false;
    			
    			this.bAddToCompass1.enabled = false;
    			this.bAddToCompass1.visible = false;
    			this.bAddToCompass2.enabled = false;
    			this.bAddToCompass2.visible = false;
    			this.bAddToCompass3.enabled = false;
    			this.bAddToCompass3.visible = false;
    			this.bAddToCompass4.enabled = false;
    			this.bAddToCompass4.visible = false;
    			this.bAddToCompass5.enabled = false;
    			this.bAddToCompass5.visible = false;
    			return;
        	}
        	else
        	{
        		this.mc.player.closeScreen();
        	}
        }
        
        if (this.titleText.isFocused())
        {
        	this.titleText.textboxKeyTyped(par1, key);
        }
        try
		{
			super.keyTyped(par1, key);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    	String text = this.titleText.getText();
		BiblioNetworking.INSTANCE.sendToServer(new BiblioStockTitle(text));
    	// ByteBuf buffer = Unpooled.buffer();
    	// ByteBufUtils.writeUTF8String(buffer, text);
    	// BiblioCraft.ch_BiblioStoCatTitle.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioStockTitle"));
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
