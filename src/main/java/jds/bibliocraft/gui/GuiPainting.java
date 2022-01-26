package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerPainting;
import jds.bibliocraft.helpers.BiblioEnums.EnumBiblioPaintings;
import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.helpers.PaintingUtil;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioPainting;
import jds.bibliocraft.tileentities.TileEntityPainting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiPainting extends GuiContainer
{
	private static int guiWidth = 176;
	private static int guiHeight = 192;
	TileEntityPainting painting;
	private EnumArt[] vanillaArtList = EnumArt.values();
	private EnumBiblioPaintings[] biblioArtList = EnumBiblioPaintings.values();
	
	private String sScale = I18n.translateToLocal("gui.painting.scale"); 
	private String sResolution = I18n.translateToLocal("gui.painting.resolution"); //"Pixel Size";
	private String sSizeTitle = I18n.translateToLocal("gui.painting.aspectRatio"); //"Aspect Ratio";
	private String sPaintingFinalSize = I18n.translateToLocal("gui.painting.size"); //"Painting Size";
	private String sPaintingRotation = I18n.translateToLocal("gui.painting.rotation"); //"Rotation";
	private String sCanvasCorner = I18n.translateToLocal("gui.painting.corner"); //"Master Corner";
	
	private int canvasScale = 1;
	private int canvasResolution = 0;
	private int canvasSize = 0;
	private int canvasCorner = 0; // 0 == bottom left corner, and  clockwise from there
	private int canvasRotation = 0;
	
	private GuiButtonAddSubtract bScalePos;
	private GuiButtonAddSubtract bScaleNeg;
	private GuiButtonAddSubtract bResPos;
	private GuiButtonAddSubtract bResNeg;
	private GuiButtonAddSubtract bSizePos;
	private GuiButtonAddSubtract bSizeNeg;
	private GuiButtonAddSubtract bRotPos;
	private GuiButtonAddSubtract bRotNeg;
	
	private GuiButtonAddSubtract bAspectXPos;
	private GuiButtonAddSubtract bAspectXNeg;
	private GuiButtonAddSubtract bAspectYPos;
	private GuiButtonAddSubtract bAspectYNeg;
	
	private GuiButton hideFrameButton;
	
	private String paintingTitle = "";
	private int paintingType = 0;
	private int paintingNumber = 0;
	
	private int resx = 0;
	private int resy = 0;
	
	private String rsScale = "";
	private String rsPixelSize = "";
	private String rsAspectRatio = "";
	private String rsFinalSize = "";
	
	private String[] customArtNames = null;
	private int[] customArtHeights = null;
	private int[] customArtWidths = null;
	private ResourceLocation[] customArtResources = null;
	private int customPaintingAspectX = 1;
	private int customPaintingAspectY = 1; 
	
	private boolean hideFrame = false;
	private EnumPaintingFrame frameType;
	private String hideFrameButtonText = I18n.translateToLocal("gui.painting.hideFrame"); 

	
	public GuiPainting(InventoryPlayer inventoryPlayer, TileEntityPainting tile)
	{
		super(new ContainerPainting(inventoryPlayer, tile));
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		this.painting = tile;
		this.frameType = tile.getFrameStyle(); 
		this.hideFrame = tile.getHideFrame();
		if (this.hideFrame)
		{
			this.hideFrameButtonText = I18n.translateToLocal("gui.painting.showFrame");
		}
		
		this.customArtNames = PaintingUtil.customArtNames;
		if (this.customArtNames != null)
		{
			this.customArtResources = PaintingUtil.customArtResources;
			this.customArtHeights = PaintingUtil.customArtHeights;
			this.customArtWidths = PaintingUtil.customArtWidths;
			//System.out.println(this.customArtResources.length);
		}
		
		this.initData();
	}
	
	public void initData()
	{
		if (this.painting.getStackInSlot(0) == ItemStack.EMPTY)
		{
			//System.out.println("null slot");
			this.paintingTitle = "blank";
			this.paintingType = 0;
			this.canvasCorner = 0;
			this.canvasResolution = 0;
			this.canvasSize = 0;
			this.canvasRotation = 0;
			this.customPaintingAspectX = 1;
			this.customPaintingAspectY = 1;
		}
		else
		{
			this.paintingTitle = this.painting.getPaintingTitle();
			this.paintingType = this.painting.getPaintingType();
			this.canvasCorner = this.painting.getPaintingCorner();
			this.canvasScale = this.painting.getPaintingScale();
			this.canvasResolution = this.painting.getPaintingRes();
			this.canvasSize = this.painting.getPaintingAspectRatio();
			this.canvasRotation = this.painting.getPaintingRotation();
			this.customPaintingAspectX = this.painting.getCustomPaintingAspectX();
			this.customPaintingAspectY = this.painting.getCustomPaintingAspectY();
			this.getPaintingNumber();
		}
		
	}
	
	private void resetData()
	{
		this.paintingTitle = "blank";
		this.paintingType = 0;
		this.canvasCorner = 0;
		this.canvasResolution = 0;
		this.canvasSize = 0;
		this.canvasRotation = 0;
		this.customPaintingAspectX = 1;
		this.customPaintingAspectY = 1;
	}
	
	public void getPaintingNumber()
	{
		if (paintingType == 0)
		{
			for (int i = 0; i<biblioArtList.length; i++)
			{
				if (this.paintingTitle.contentEquals(biblioArtList[i].title))
				{
					this.paintingNumber = i;
					break;
				}
			}
		}
		else if (paintingType == 1)
		{
			for (int i = 0; i<vanillaArtList.length; i++)
			{
				if (this.paintingTitle.contentEquals(vanillaArtList[i].title))
				{
					this.paintingNumber = i;
					break;
				}
			}
		}
		else if (paintingType == 2)
		{
			if (customArtNames != null && customArtNames.length > 0)
			{
				for (int i = 0; i < customArtNames.length; i++)
				{
					if (this.paintingTitle.contentEquals(this.customArtNames[i]))
					{
						this.paintingNumber = i;
					}
				}
			}
		}
	}
	
    @Override
    public void initGui()
    {
    	super.initGui();
		int w = (width - this.guiWidth) / 2;
		int h = (height - this.guiHeight) / 2;
    	buttonList.clear();
    	
    	buttonList.add(bScalePos = new GuiButtonAddSubtract(0, w+71, h+14, 0, 1.0f));
    	buttonList.add(bScaleNeg = new GuiButtonAddSubtract(1, w+6, h+14, 1, 1.0f));	
    	
    	buttonList.add(bResPos = new GuiButtonAddSubtract(2, w+71, h+40, 0, 1.0f));
    	buttonList.add(bResNeg = new GuiButtonAddSubtract(3, w+6, h+40, 1, 1.0f));	
    	
    	buttonList.add(bSizePos = new GuiButtonAddSubtract(4, w+71, h+66, 0, 1.0f));
    	buttonList.add(bSizeNeg = new GuiButtonAddSubtract(5, w+6, h+66, 1, 1.0f));	
    	
    	buttonList.add(bRotPos = new GuiButtonAddSubtract(6, w+144, h+92, 0, 1.0f));
    	buttonList.add(bRotNeg = new GuiButtonAddSubtract(7, w+114, h+92, 1, 1.0f));	
    	
    	buttonList.add(bAspectXPos = new GuiButtonAddSubtract(8, w+10, h+65, 0, 0.7f));
    	buttonList.add(bAspectXNeg = new GuiButtonAddSubtract(9, w+10, h+73, 1, 0.7f));
    	buttonList.add(bAspectYPos = new GuiButtonAddSubtract(10, w+71, h+65, 0, 0.75f));
    	buttonList.add(bAspectYNeg = new GuiButtonAddSubtract(11, w+71, h+73, 1, 0.75f));
    	
    	//bAspectXPos.scale = 0.75f;
    	//bAspectXPos.antiscale = 1.0f / 0.75f;
    	
    	buttonList.add(new GuiButtonClipboard(100, w+106, h+58, 17, 17, "", false));
    	buttonList.add(new GuiButtonClipboard(100, w+106, h+16, 17, 17, "", false));
    	buttonList.add(new GuiButtonClipboard(100, w+148, h+58, 17, 17, "", false));
    	buttonList.add(new GuiButtonClipboard(100, w+148, h+16, 17, 17, "", false));
    	
		if (this.frameType == EnumPaintingFrame.BORDERLESS)
		{
			buttonList.add(hideFrameButton = new GuiButton(12, w+45, h-20, 80, 20, hideFrameButtonText));
		}
    }
    
    @Override
    public void onGuiClosed()
    {
    	sendPacket();
    }
    
    public void sendPacket()
    {
		BiblioNetworking.INSTANCE.sendToServer(new BiblioPainting(this.painting.getPos(), this.canvasCorner, this.canvasScale, this.canvasResolution, this.canvasSize, this.canvasRotation, this.customPaintingAspectX, this.customPaintingAspectY, this.hideFrame));
    	// ByteBuf buffer = Unpooled.buffer();
    	// buffer.writeInt(this.painting.getPos().getX());
    	// buffer.writeInt(this.painting.getPos().getY());
    	// buffer.writeInt(this.painting.getPos().getZ());
    	
    	// buffer.writeInt(this.canvasCorner);
    	// buffer.writeInt(this.canvasScale);
    	// buffer.writeInt(this.canvasResolution);
    	// buffer.writeInt(this.canvasSize);
    	
    	// buffer.writeInt(this.canvasRotation);
    	
    	// buffer.writeInt(this.customPaintingAspectX);
    	// buffer.writeInt(this.customPaintingAspectY);
    	
    	// buffer.writeBoolean(this.hideFrame);
    	// BiblioCraft.ch_BiblioPainting.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioPainting"));
    }
    
    @Override
    protected void actionPerformed(GuiButton click)
    {
    	//System.out.println(click.id);
    	switch (click.id)
    	{
    		case 0:
    		{
    			bScalePos.pressed = true;
    			if (this.canvasScale < 10)
    			{
    				this.canvasScale++;
    			}
    			break;
    		}
    		case 1:
    		{
    			bScaleNeg.pressed = true;
    			if (this.canvasScale > 1)
    			{
    				this.canvasScale--;
    			}
    			break;
    		}
    		case 2:
    		{
    			bResPos.pressed = true;
    			if (this.paintingType == 0)
    			{
	    			if (this.canvasResolution < biblioArtList[this.paintingNumber].resolution.length-1)
	    			{
	    				this.canvasResolution++;
	    			}
    			}
    			else if (this.paintingType == 1)
    			{
    				
    			}
    			else if (this.paintingType == 2)
    			{
    				
    			}
    			break;
    		}
    		case 3:
    		{
    			bResNeg.pressed = true;
    			if (this.canvasResolution > 0)
    			{
    				this.canvasResolution--;
    			}
    			break;
    		}
    		case 4:
    		{
    			bSizePos.pressed = true;
    			if (this.paintingType == 0)
    			{
	    			if (this.canvasSize < biblioArtList[this.paintingNumber].sizeX.length-1)
	    			{
	    				this.canvasSize++;
	    			}
    			}
    			else if (this.paintingType == 1)
    			{
    				
    			}
    			else if (this.paintingType == 2)
    			{
    				
    			}
    			break;
    		}
    		case 5:
    		{
    			bSizeNeg.pressed = true;
    			if (this.canvasSize > 0)
    			{
    				this.canvasSize--;
    			}
    			break;
    		}
    		case 6:
    		{
    			bRotPos.pressed = true;
    			if (this.canvasRotation < 3)
    			{
    				this.canvasRotation++;
    			}
    			else
    			{
    				this.canvasRotation = 0;
    			}
    			break;
    		}
    		case 7:
    		{
    			bRotNeg.pressed = true;
    			if (this.canvasRotation > 0)
    			{
    				this.canvasRotation--;
    			}
    			else
    			{
    				this.canvasRotation = 3;
    			}
    			break;
    		}
    		case 8:
    		{
    			this.customPaintingAspectX++;
    			break;
    		}
    		case 9:
    		{
    			if (this.customPaintingAspectX > 1)
    			{
    				this.customPaintingAspectX--;
    			}
    			break;
    		}
    		case 10:
    		{
    			this.customPaintingAspectY++;
    			break;
    		}
    		case 11:
    		{
    			if (this.customPaintingAspectY > 1)
    			{
    				this.customPaintingAspectY--;
    			}
    			break;
    		}
    		case 12:
    		{
    			if (this.hideFrame)
    			{
    				this.hideFrame = false;
    				this.hideFrameButtonText = I18n.translateToLocal("gui.painting.hideFrame");
    			}
    			else
    			{
    				this.hideFrame = true;
    				this.hideFrameButtonText = I18n.translateToLocal("gui.painting.showFrame"); 
    			}
    			initGui();
    			break;
    		}
    	}
    	//System.out.println(click.id);
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
    	
 		int w = (width - this.guiWidth) / 2;
 		int h = (height - this.guiHeight) / 2;
 		
 		if (mousex > w+106 && mousex <= w+106+16 && mousey > h+59 && mousey <= h+59+16)
 		{
 			this.canvasCorner = 0;
 		}
 		if (mousex > w+106 && mousex <= w+106+16 && mousey > h+17 && mousey <= h+17+16)
 		{
 			this.canvasCorner = 1;
 		}
 		if (mousex > w+148 && mousex <= w+148+16 && mousey > h+17 && mousey <= h+17+16)
 		{
 			this.canvasCorner = 2;
 		}
 		if (mousex > w+148 && mousex <= w+148+16 && mousey > h+59 && mousey <= h+59+16)
 		{
 			this.canvasCorner = 3;
 		}
 		
 		if (mousex > w+78 && mousex <= w+80+16 && mousey > h+87 && mousey <= h+89+16)
 		{
 			resetData();
 			sendPacket();
 		}
    }
    
    @Override	
	public void updateScreen()
    {
        super.updateScreen();
    }
    
    @Override
    protected void keyTyped(char par1, int key)
    {
    	if (key == 1)
    	{
    		 this.mc.player.closeScreen();
    	}
    }
    
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		int x = (width - this.guiWidth) / 2;
		int y = (height - this.guiHeight) / 2;
		//draw text and stuff here
        //the parameters for drawString are: string, x, y, color
		//this.fontRenderer.drawString(I18n.translateToLocal("gui.cookiejar"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		//this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if (this.painting.getContainerUpdate())
		{
			//System.out.println("container has been updated");
			//System.out.println(this.paintingTitle);
			boolean before = this.paintingTitle.contentEquals("blank");
			this.initData();
			boolean after = this.paintingTitle.contentEquals("blank");
			if (before && !after && this.paintingType == 2)
			{
				this.resx = this.customArtWidths[this.paintingNumber];
				this.resy = this.customArtHeights[this.paintingNumber];
				if (resx <= resy)
				{
					this.customPaintingAspectX = roundNum(resx*1.0f / resx*1.0f);
					this.customPaintingAspectY = roundNum(resy*1.0f / resx*1.0f);
				}
				else
				{
					this.customPaintingAspectX = roundNum(resx*1.0f / resy*1.0f);
					this.customPaintingAspectY = roundNum(resy*1.0f/resy*1.0f);
				}
			}
			//System.out.println(this.paintingTitle);
			this.painting.setContainterUpdate(false);
		}

	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float floaty, int mousex, int mousey)
	{
		//draw your Gui here, only thing you need to change is the path
		//System.out.println("testy:  "+floaty+"   "+mousex+"    "+mousey);
		//int texture = mc.renderEngine.getTexture(CommonProxy.BOOKCASEGUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGGUI);
		int x = (width - this.guiWidth) / 2;
		int y = (height - this.guiHeight) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.guiWidth, this.guiHeight);
		//this.canvasCorner = 0;
		
		switch (this.canvasCorner)
		{
			case 0:{this.drawTexturedModalRect(x+106, y+59, 0, 211, 16, 16); this.drawTexturedModalRect(x+111, y+48, 20, 218, 22, 22); break;}
			case 1:{this.drawTexturedModalRect(x+106, y+17, 0, 211, 16, 16); this.drawTexturedModalRect(x+111, y+22, 20, 192, 22, 22); break;}
			case 2:{this.drawTexturedModalRect(x+148, y+17, 0, 211, 16, 16); this.drawTexturedModalRect(x+137, y+22, 46, 192, 22, 22); break;}
			case 3:{this.drawTexturedModalRect(x+148, y+59, 0, 211, 16, 16); this.drawTexturedModalRect(x+137, y+48, 46, 218, 22, 22); break;}
		}
		
		switch (this.canvasRotation)
		{
			case 0:{this.drawTexturedModalRect(x+130, y+93, 39, 240, 10, 10); break;}
			case 1:{this.drawTexturedModalRect(x+130, y+93, 49, 240, 10, 10); break;}
			case 2:{this.drawTexturedModalRect(x+130, y+93, 59, 240, 10, 10); break;}
			case 3:{this.drawTexturedModalRect(x+130, y+93, 69, 240, 10, 10); break;}
		}

		this.rsScale = this.canvasScale+"x";
		this.fontRenderer.drawString(this.sScale, x+28, y+5, 4210752);
		this.fontRenderer.drawString(this.rsScale, x+46-(this.rsScale.length()*8/2), y+16, 0xFFFFFF);
		this.fontRenderer.drawString(this.sResolution, x+19, y+31, 4210752);
		this.fontRenderer.drawString(this.sSizeTitle, x+12, y+57, 4210752);
		this.fontRenderer.drawString(this.sCanvasCorner, x+97, y+5, 4210752);
		this.fontRenderer.drawString(this.sPaintingFinalSize, x+12, y+82, 4210752);
		this.fontRenderer.drawString(this.sPaintingRotation, x+115, y+82, 4210752);
		
		//System.out.println((this.rsScale.length()*5/2));
		if (!(this.paintingTitle.contentEquals("blank")))
		{
			if (this.paintingType == 0)
			{
				this.bAspectXPos.enabled = false;
				this.bAspectXPos.visible = false;
				this.bAspectXNeg.enabled = false;
				this.bAspectXNeg.visible = false;
				this.bAspectYPos.enabled = false;
				this.bAspectYPos.visible = false;
				this.bAspectYNeg.enabled = false;
				this.bAspectYNeg.visible = false;
				
				if (this.biblioArtList[this.paintingNumber].resolution.length > 1)
				{
					this.bResPos.enabled = true;
					this.bResPos.visible = true;
					this.bResNeg.enabled = true;
					this.bResNeg.visible = true;
				}
				else
				{
					this.bResPos.enabled = false;
					this.bResPos.visible = false;
					this.bResNeg.enabled = false;
					this.bResNeg.visible = false;
				}
				
				if (this.biblioArtList[this.paintingNumber].sizeX.length > 1)
				{
					this.bSizePos.enabled = true;
					this.bSizePos.visible = true;
					this.bSizeNeg.enabled = true;
					this.bSizeNeg.visible = true;
				}
				else
				{
					this.bSizePos.enabled = false;
					this.bSizePos.visible = false;
					this.bSizeNeg.enabled = false;
					this.bSizeNeg.visible = false;
				}
				this.resx = biblioArtList[this.paintingNumber].resolution[this.canvasResolution]*biblioArtList[this.paintingNumber].sizeX[this.canvasSize];
				this.resy = biblioArtList[this.paintingNumber].resolution[this.canvasResolution]*biblioArtList[this.paintingNumber].sizeY[this.canvasSize];
				if (!(biblioArtList[this.paintingNumber].title.contentEquals("cornerexplody")) && resx != resy)
				{
					if (biblioArtList[this.paintingNumber].title.contentEquals("seaofportals") || biblioArtList[this.paintingNumber].title.contentEquals("boathouse") || biblioArtList[this.paintingNumber].title.contentEquals("raven"))
					{
						this.resx = this.resx / 4;
						this.resy = this.resy / 4;
					}
					else
					{
						this.resx = this.resx / 2;
						this.resy = this.resy / 2;
					}
				}

				this.rsAspectRatio = biblioArtList[this.paintingNumber].sizeX[this.canvasSize]+":"+biblioArtList[this.paintingNumber].sizeY[this.canvasSize];
				this.rsFinalSize = (this.canvasScale*biblioArtList[this.paintingNumber].sizeX[this.canvasSize])+" x "+(this.canvasScale*biblioArtList[this.paintingNumber].sizeY[this.canvasSize]);
			}
			else if (this.paintingType == 1)
			{
				this.bResPos.enabled = false;
				this.bResPos.visible = false;
				this.bResNeg.enabled = false;
				this.bResNeg.visible = false;
				this.bSizePos.enabled = false;
				this.bSizePos.visible = false;
				this.bSizeNeg.enabled = false;
				this.bSizeNeg.visible = false;
				this.bAspectXPos.enabled = false;
				this.bAspectXPos.visible = false;
				this.bAspectXNeg.enabled = false;
				this.bAspectXNeg.visible = false;
				this.bAspectYPos.enabled = false;
				this.bAspectYPos.visible = false;
				this.bAspectYNeg.enabled = false;
				this.bAspectYNeg.visible = false;

				this.resx = vanillaArtList[this.paintingNumber].sizeX;
				this.resy = vanillaArtList[this.paintingNumber].sizeY;
				
				
				this.rsAspectRatio = (int)((resx*1.0)/16.0)+":"+(int)((resy*1.0)/16.0);
				
				this.rsFinalSize = (this.canvasScale*(int)((resx*1.0)/16.0)+" x "+(this.canvasScale*(int)((resy*1.0)/16.0)));
				
				
				
			}
			else if (this.paintingType == 2)
			{
				this.bResPos.enabled = false;
				this.bResPos.visible = false;
				this.bResNeg.enabled = false;
				this.bResNeg.visible = false;
				this.bSizePos.enabled = false;
				this.bSizePos.visible = false;
				this.bSizeNeg.enabled = false;
				this.bSizeNeg.visible = false;
				this.bAspectXPos.enabled = true;
				this.bAspectXPos.visible = true;
				this.bAspectXNeg.enabled = true;
				this.bAspectXNeg.visible = true;
				this.bAspectYPos.enabled = true;
				this.bAspectYPos.visible = true;
				this.bAspectYNeg.enabled = true;
				this.bAspectYNeg.visible = true;
				
				//GL11.glScaled(0.5, 0.5, 0.5);
				//this.bAspectXPos.drawButton(mc, mousex, mousey);
				//GL11.glScaled(2, 2, 2);
				
				if (this.customArtNames != null)
				{
					this.resx = this.customArtWidths[this.paintingNumber];
					this.resy = this.customArtHeights[this.paintingNumber];
					this.rsAspectRatio = this.customPaintingAspectX+":"+this.customPaintingAspectY;
					this.rsFinalSize = (this.customPaintingAspectX*this.canvasScale)+" x "+(this.customPaintingAspectY*this.canvasScale);
				}
			}
			//System.out.println(this.rsFinalSize.length()*3 / 2);
			this.rsPixelSize = resx+"x"+resy;
			this.fontRenderer.drawString(this.rsPixelSize, x+53-(this.rsPixelSize.length()*8/2), y+42, 0xFFFFFF);
			this.fontRenderer.drawString(this.rsAspectRatio, x+50-(this.rsAspectRatio.length()*8/2), y+68, 0xFFFFFF);
			this.fontRenderer.drawString(this.rsFinalSize, x+46-(this.rsFinalSize.length()*7/2), y+92, 0X00FF00, true);
		}
		else
		{
			this.bResPos.enabled = false;
			this.bResPos.visible = false;
			this.bResNeg.enabled = false;
			this.bResNeg.visible = false;
			this.bSizePos.enabled = false;
			this.bSizePos.visible = false;
			this.bSizeNeg.enabled = false;
			this.bSizeNeg.visible = false;
			
			this.bAspectXPos.enabled = false;
			this.bAspectXPos.visible = false;
			this.bAspectXNeg.enabled = false;
			this.bAspectXNeg.visible = false;
			this.bAspectYPos.enabled = false;
			this.bAspectYPos.visible = false;
			this.bAspectYNeg.enabled = false;
			this.bAspectYNeg.visible = false;
		}
	}
	
	private int roundNum(float num)
	{
		int roundDown = (int)num;
		float roundTest = num - roundDown;
		if (roundTest >= 0.5f || roundDown == 0)
		{
			return roundDown+1;
		}
		else
		{
			return roundDown;
		}
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
}
