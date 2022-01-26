package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerPaintPress;
import jds.bibliocraft.helpers.BiblioEnums.EnumBiblioPaintings;
import jds.bibliocraft.helpers.PaintingUtil;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioPaintPress;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiPaintPress extends GuiContainer
{
	private static int guiWidth = 256;
	private static int guiHeight = 241;
	private TileEntityPaintPress paintPress;
	private EnumArt[] vanillaArtList = EnumArt.values();
	private EnumBiblioPaintings[] biblioArtList = EnumBiblioPaintings.values();
	
	private String[] customArtNames = null;
	private int[] customArtHeights = null;
	private int[] customArtWidths = null;
	private ResourceLocation[] customArtResources = null;
	private int pagesTotal = 1;
	private int pagesCurrent = 0;
	
	private int tab = 0;
	
	private int showCount = 0;
	private int artCount = 0;
	private boolean drawCheck = false;
	
	private int selectedTypeArt = 0; // 0 = biblioart, 1 = vanillaart, 2 = customart
	private int selectedVanillaArt = -1;
	private int selectedBiblioArt = -1;
	private int selectedCustomArt = -1;
	private String selectedArtTitle = "blank";
	
	
	private String sBibliocraft = I18n.translateToLocal("itemGroup.BiblioCraft");
	private String sVanilla = I18n.translateToLocal("gui.paintpress.vanilla");
	private String sCustom = I18n.translateToLocal("gui.paintpress.custom");
	
	private GuiButton applyPainting;
	private GuiButtonNextPage pageNext;
	private GuiButtonNextPage pagePrev;
	
	public GuiPaintPress(InventoryPlayer inventoryPlayer, TileEntityPaintPress tile)
	{
		super(new ContainerPaintPress(inventoryPlayer, tile));
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		this.paintPress = tile;
		this.selectedTypeArt = this.paintPress.selectedPaintingType; //this.paintPress.getPaintingType();
		this.selectedArtTitle = this.paintPress.selectedPaintingTitle; //getPaintingTitle();
		this.tab = this.selectedTypeArt;
		
		this.customArtNames = PaintingUtil.customArtNames;
		if (this.customArtNames != null)
		{
			this.customArtResources = PaintingUtil.customArtResources;
			this.customArtHeights = PaintingUtil.customArtHeights;
			this.customArtWidths = PaintingUtil.customArtWidths;
			
			float baseNum = this.customArtNames.length / 32.0f;
			int roundDown =  this.customArtNames.length / 32;
			if ((baseNum-roundDown) > 0.0f)
			{
				this.pagesTotal = roundDown+1;
			}
			else if (roundDown == 0)
			{
				this.pagesTotal = 1;
			}
			else
			{
				this.pagesTotal = roundDown;
			}
			//System.out.println(this.pagesTotal);
		}
		
		//this.scanJarForArt();
		
		updateArtSelection(); 
	}
	
	
	private void updateArtSelection()
	{
		switch (this.selectedTypeArt)
		{
			case 0:
			{	
				for (int i = 0; i < biblioArtList.length; i++)
				{
					if (this.selectedArtTitle.contentEquals(this.biblioArtList[i].title))
					{
						this.selectedBiblioArt = i;
					}
				}
				break;
			}
			case 1:
			{	
				for (int i = 0; i < vanillaArtList.length; i++)
				{
					if (this.selectedArtTitle.contentEquals(this.vanillaArtList[i].title))
					{
						this.selectedVanillaArt = i;
					}
				}
				break;
			}
			case 2:
			{
				if (customArtNames != null && customArtNames.length > 0)
				{
					for (int i = 0; i < customArtNames.length; i++)
					{
						if (this.selectedArtTitle.contentEquals(this.customArtNames[i]))
						{
							this.selectedCustomArt = i;
							this.pagesCurrent = i / 32;
						}
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
    	buttonList.add(this.applyPainting = new GuiButton(0, w+52, h+137, 60, 20, I18n.translateToLocal("gui.paintpress.transfer"))); 
    	
    	buttonList.add(this.pagePrev = new GuiButtonNextPage(1, w+9, h+134, false));
    	buttonList.add(this.pageNext = new GuiButtonNextPage(2, w+222, h+134, true));
    	
    	if (this.pagesTotal > 1 && this.tab == 2 && this.pagesTotal != this.pagesCurrent+1)
    	{
    		this.pageNext.enabled = true;
    		this.pageNext.visible = true;
    	}
    	else
    	{
    		this.pageNext.enabled = false;
    		this.pageNext.visible = false;
    	}
    	if (this.pagesCurrent > 0)
    	{
    		this.pagePrev.enabled = true;
    		this.pagePrev.visible = true;
    	}
    	else
    	{
    		this.pagePrev.enabled = false;
    		this.pagePrev.visible = false;
    	}
    }
    
    @Override
    public void onGuiClosed()
    {
    	sendPacket(false);
    }
    
    @Override
    protected void actionPerformed(GuiButton click)
    {
    	if (click.id == 0)
    	{
    		// clicked the apply button.
    		// should probly send a packet to the tile entity and make the magic happen on the tile entity. 
    		sendPacket(true);
    	}
    	if (click.id == 1)
    	{
    		// click the prev page button
    		if (this.pagesCurrent > 0)
    		{
    			this.pagesCurrent--;
    			initGui();
    		}
    	}
    	if (click.id == 2)
    	{
    		// clicked the next page button
    		if (this.pagesCurrent < this.pagesTotal)
    		{
    			this.pagesCurrent++;
    			initGui();
    		}
    	}
    }
    
    private void sendPacket(boolean applyToCanvas)
    {
    	if (!applyToCanvas || (applyToCanvas && this.paintPress.getStackInSlot(0) != ItemStack.EMPTY))
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioPaintPress(this.paintPress.getPos(), this.selectedTypeArt, this.selectedArtTitle, applyToCanvas));
	    	// ByteBuf buffer = Unpooled.buffer();
	    	// buffer.writeInt(this.paintPress.getPos().getX());
	    	// buffer.writeInt(this.paintPress.getPos().getY());
	    	// buffer.writeInt(this.paintPress.getPos().getZ());
	    	
	    	// buffer.writeInt(this.selectedTypeArt);
	    	// ByteBufUtils.writeUTF8String(buffer, this.selectedArtTitle);
	    	
	    	// buffer.writeBoolean(applyToCanvas);
	    	
	    	// BiblioCraft.ch_BiblioPaintPress.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioPaintPress"));
    	}
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
 		
 		// check for tab clicks
 		if (mousey > h && mousey <= h+12)
 		{
	 		if (mousex > w+14 && mousex < w+89) // BiblioCraft tab
	 		{
	 			//System.out.println("tab?");
	 			if (this.tab != 0)
	 			{
	 				this.setTab(0);
	 				this.initGui();
	 			}
	 		}
	 		if (mousex > w+90 && mousex < w+165) // Vanilla tab
	 		{
	 			//System.out.println("tab?");
	 			if (this.tab != 1)
	 			{
	 				this.setTab(1);
	 				this.initGui();
	 			}
	 		}
	 		if (mousex > w+166 && mousex < w+241) // Custom tab
	 		{
	 			//System.out.println("tab?");
	 			if (this.tab != 2)
	 			{
	 				this.setTab(2);
	 				this.initGui();
	 			}
	 		}
 		}
 		
 		if (this.tab == 0)
 		{
 			
			for (int j = 0; j < 4; j++)
			{
				for (int i = 0; i < 8; i++)
				{ 	
					if (i+j*8 < this.biblioArtList.length)
					{
						if (mousex > (w+12+i*29) && mousex <= (w+40+i*29) && mousey > (h+19+j*29) && mousey < (h+47+j*29)) // returns true if mouse is hovering over square
						{
							if (click == 1)
							{
								this.selectedBiblioArt = -1;
								this.selectedArtTitle = "blank";
							}
							else
							{
								this.selectedBiblioArt = i+j*8;
								this.selectedVanillaArt = -1;
								this.selectedCustomArt = -1;
								this.selectedTypeArt = 0;
								this.selectedArtTitle = this.biblioArtList[i+j*8].title;
							}
						}
					}
				}
			}
 		}
 		// check for vanilla painting clicks
 		
		if (this.tab == 1)
		{
			this.artCount = 0;
			for (int j = 0; j < 4; j++)
			{
				for (int i = 0; i < 8; i++)
				{ 
					if (this.artCount < this.vanillaArtList.length)
					{
						if (mousex > (w+12+i*29) && mousex <= (w+40+i*29) && mousey > (h+19+j*29) && mousey < (h+47+j*29)) // returns true if mouse is hovering over square
						{
							if (click == 1)
							{
								this.selectedVanillaArt = -1;
								this.selectedArtTitle = "blank";
							}
							else
							{
								this.selectedVanillaArt = this.artCount;
								this.selectedBiblioArt = -1;
								this.selectedCustomArt = -1;
								this.selectedTypeArt = 1;
								this.selectedArtTitle = this.vanillaArtList[this.artCount].title;
							}
						}
						this.artCount++;
					}
				}
			}
		}
 		
		if (this.tab == 2)
		{
			// check for custom painting clicks
			if (this.customArtNames != null && this.customArtNames.length > 0)
			{
				for (int j = 0; j < 4; j++)
				{
					for (int i = 0; i < 8; i++)
					{ 
						if (i+j*8+(32*(this.pagesCurrent)) < this.customArtNames.length)
						{
							if (mousex > (w+12+i*29) && mousex <= (w+40+i*29) && mousey > (h+19+j*29) && mousey < (h+47+j*29)) // returns true if mouse is hovering over square
							{
								if (click == 1)
								{
									this.selectedCustomArt = -1;
									this.selectedArtTitle = "blank";
								}
								else
								{
									this.selectedCustomArt = i+j*8+(32*(this.pagesCurrent));
									this.selectedBiblioArt = -1;
									this.selectedTypeArt = 2;
									this.selectedArtTitle = this.customArtNames[this.selectedCustomArt];
								}
							}
						}
					}
				}
			}
		}
    	// System.out.println("click = "+click+"  "+mousex+"    "+mousey);
    }
    
    private void setTab(int tabe)
    {
    	this.tab = tabe;
    	this.initGui();
    }
    
    @Override	
	public void updateScreen()
    {
        super.updateScreen();
    }
    
    @Override
    protected void keyTyped(char par1, int key)
    {
    	//System.out.println("char = "+par1+"   key = "+key);
    	if (key == 1)
    	{
    		 this.mc.player.closeScreen();
    	}
    	if (key == 15) // tab key
    	{
    		if (this.tab < 2)
    		{
    			this.tab++;
    		}
    		else
    		{
    			this.tab = 0;
    		}
    	}
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		//draw text and stuff here
        //the parameters for drawString are: string, x, y, color
		//this.fontRenderer.drawString(I18n.translateToLocal("gui.cookiejar"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		//this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float floaty, int mousex, int mousey)
	{
		//draw your Gui here, only thing you need to change is the path
		//System.out.println("testy:  "+floaty+"   "+mousex+"    "+mousey);
		//int texture = mc.renderEngine.getTexture(CommonProxy.BOOKCASEGUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSGUI);
		int x = (width - this.guiWidth) / 2;
		int y = (height - this.guiHeight) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.guiWidth, this.guiHeight);
		
		if (this.tab == 0)
		{
			this.drawTexturedModalRect(x+14, y, 0, 244, 75, 12);
		}
		if (this.tab == 1)
		{
			this.drawTexturedModalRect(x+90, y, 0, 244, 75, 12);
		}
		if (this.tab == 2)
		{
			this.drawTexturedModalRect(x+166, y, 0, 244, 75, 12);
		}
		
		this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSBUTTONS);
		
		
		if (this.tab == 0)
		{
			// draw buttons for bibliocraft art
			//for (int i = 0; i < biblioArtList.length; i++)
			//{
			for (int j = 0; j < 4; j++)
			{
				for (int i = 0; i < 8; i++)
				{
					if (i+j*8 < this.biblioArtList.length)
					{
						this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSBUTTONS);
						this.drawTexturedModalRect(x+12+i*29, y+19+(j*29), 0, 0, 28, 28);
						
						
						if (mousex > (x+12+i*29) && mousex <= (x+40+i*29) && mousey > (y+19+(j*29)) && mousey < (y+47+(j*29))) // returns true if mouse is hovering over square
						{
							if (i+j*8 == this.selectedBiblioArt)
							{
								this.drawTexturedModalRect(x+12+i*29, y+19+(j*29), 90, 0, 28, 28);
								drawCheck = true;
							}
							else
							{
								this.drawTexturedModalRect(x+12+i*29, y+19+(j*29), 30, 0, 28, 28);
							}
						}
						else if (i+j*8 == this.selectedBiblioArt)
						{
							this.drawTexturedModalRect(x+12+i*29, y+19+(j*29), 60, 0, 28, 28);
							drawCheck = true;
							
						}
						else
						{
							this.drawTexturedModalRect(x+12+i*29, y+19+(j*29), 0, 0, 28, 28);
						}
						
						
						this.mc.getTextureManager().bindTexture(biblioArtList[i+j*8].paintingTextures[0][0]);
						float scaler = 24.0f / 256.0f;
						float antiScaler = 1.0f / scaler;
						GL11.glScalef(scaler, scaler, scaler);
						this.drawTexturedModalRect((int)((x+14+i*29)*antiScaler), (int)((y+21+(j*29))*antiScaler), 0, 0, 256, 256);
						GL11.glScalef(antiScaler, antiScaler, antiScaler);
						
						if (drawCheck)
						{
							this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSBUTTONS);
							this.drawTexturedModalRect(x+32+i*29, y+18+(j*29), 0, 30, 10, 8);
							drawCheck = false;
						}
					}
				}
			}
		}
		if (this.tab == 1)
		{
			this.artCount = 0;
			for (int j = 0; j < 4; j++)
			{
				for (int i = 0; i < 8; i++)
				{ 
					if (this.artCount < this.vanillaArtList.length)
					{
						
						this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSBUTTONS);
						if (mousex > (x+12+i*29) && mousex <= (x+40+i*29) && mousey > (y+19+j*29) && mousey < (y+47+j*29)) // returns true if mouse is hovering over square
						{
							if (this.artCount == this.selectedVanillaArt)
							{
								this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 90, 0, 28, 28);
								drawCheck = true;
							}
							else
							{
								this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 30, 0, 28, 28);
							}
						}
						else if (this.artCount == this.selectedVanillaArt)
						{
							this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 60, 0, 28, 28);
							drawCheck = true;
							
						}
						else
						{
							this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 0, 0, 28, 28);
						}
	 
						drawVanillaPainting(x+i*29, y+j*29, this.artCount);
						
						if (drawCheck)
						{
							this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSBUTTONS);
							this.drawTexturedModalRect(x+32+i*29, y+18+j*29, 0, 30, 10, 8);
							drawCheck = false;
						}
						this.artCount++;
					}
				}
			}
		}
		
		if (this.tab == 2)
		{
			if (this.customArtNames != null)
			{
				for (int j = 0; j < 4; j++)
				{
					for (int i = 0; i < 8; i++)
					{ 
						if (i+j*8+(32*(this.pagesCurrent)) < this.customArtNames.length)
						{
							this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSBUTTONS);
							this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 0, 0, 28, 28);
							
							
							if (mousex > (x+12+i*29) && mousex <= (x+40+i*29) && mousey > (y+19+j*29) && mousey < (y+47+j*29)) // returns true if mouse is hovering over square
							{
								if (i+j*8+(32*(this.pagesCurrent)) == this.selectedCustomArt)
								{
									this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 90, 0, 28, 28);
									drawCheck = true;
								}
								else
								{
									this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 30, 0, 28, 28);
								}
							}
							else if (i+j*8+(32*(this.pagesCurrent)) == this.selectedCustomArt)
							{
								this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 60, 0, 28, 28);
								drawCheck = true;
								
							}
							else
							{
								this.drawTexturedModalRect(x+12+i*29, y+19+j*29, 0, 0, 28, 28);
							}
							
							

								this.mc.getTextureManager().bindTexture(new ResourceLocation("bibliocraft","textures/custompaintings/"+customArtNames[i+j*8+(32*(this.pagesCurrent))]));
								float scaler = 24.0f / 256.0f;
								float antiScaler = 1.0f / scaler;
								GL11.glScalef(scaler, scaler, scaler);
								this.drawTexturedModalRect((int)((x+14+i*29)*antiScaler), (int)((y+21+j*29)*antiScaler), 0, 0, 256, 256);
								GL11.glScalef(antiScaler, antiScaler, antiScaler);
							
							
							if (drawCheck)
							{
								this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGPRESSBUTTONS);
								this.drawTexturedModalRect(x+32+i*29, y+18+j*29, 0, 30, 10, 8);
								drawCheck = false;
							}
						}
					}
				}
			}
		}
		
		this.fontRenderer.drawString(this.sBibliocraft, x+25, y+3, 0x000000, false); 
		this.fontRenderer.drawString(this.sVanilla, x+110, y+3, 0x000000, false);
		this.fontRenderer.drawString(this.sCustom, x+186, y+3, 0x000000, false);
		if (this.tab == 2)
		{
			this.fontRenderer.drawString(I18n.translateToLocal("gui.paintpress.page")+" "+(this.pagesCurrent+1)+" "+I18n.translateToLocal("gui.paintpress.of")+" "+this.pagesTotal, x+145, y+143, 0x000000, false);
		}
	}
	
	private void drawVanillaPainting(int x, int y, int paintingNum)
	{
		this.mc.getTextureManager().bindTexture(CommonProxy.PAINTINGSHEET);
		double scale = 24.0 / this.vanillaArtList[paintingNum].sizeX;
		if (this.vanillaArtList[paintingNum].sizeX < this.vanillaArtList[paintingNum].sizeY)
		{
			scale = 24.0 / this.vanillaArtList[paintingNum].sizeY;
		}
		
		double invertedScale = 1.0/scale;
		int adjustx = 0;
		int adjusty = 0;
		if (this.vanillaArtList[paintingNum].sizeY != this.vanillaArtList[paintingNum].sizeX)
		{
			if (this.vanillaArtList[paintingNum].sizeY > this.vanillaArtList[paintingNum].sizeX)
			{
				adjustx = 6;
			}
			else
			{
				adjusty = 6;
			}
			
			if (this.vanillaArtList[paintingNum].sizeY == 48)
			{
				adjusty = 2;
			}
		}
		GL11.glScaled(scale, scale, scale);
		//System.out.println(this.vanillaArtList[artCount].sizeX);
		this.drawTexturedModalRect((int)((x+14+adjustx)*invertedScale), (int)((y+22+adjusty)*invertedScale), (this.vanillaArtList[paintingNum].offsetX), (this.vanillaArtList[paintingNum].offsetY), (this.vanillaArtList[paintingNum].sizeX), (this.vanillaArtList[paintingNum].sizeY));
		GL11.glScaled(1.0/scale, 1.0/scale, 1.0/scale);
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
