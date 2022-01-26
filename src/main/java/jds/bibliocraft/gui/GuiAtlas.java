package jds.bibliocraft.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerAtlas;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioAtlas;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class GuiAtlas extends GuiContainer
{
	private static int guiWidth = 256;
	private static int guiHeight = 241;
	
	private ItemStack atlasStack;
	private ItemStack selectedMapStack = ItemStack.EMPTY; 
	
	private GuiButtonAddSubtract bzoomPos;
	private GuiButtonAddSubtract bzoomNeg;
	private GuiButton btoggleAutoCenter;
	private GuiButton btoggleAutoCreate;
	
	private GuiButtonAtlasControls bAtlasMode;
	private GuiButtonAtlasControls bInventoryMode;
	
	private boolean autoCenter = false;
	private boolean autoCreate = false;
	private int zoomLevel = 0;
	private int selectedSlot = -1;
	private int hoveredSlot = -1;
	
	private int mapX = 0;
	private int mapZ = 0;
	private int mapZoom = 0;
	
	private boolean changeGui = false;
	
	// If I find a matching map, I'll set these to a positive integer. if this number is not -1 I'll render the guides
	private int slotMapNorth = -1;
	private int slotMapSouth = -1;
	private int slotMapEast = -1;
	private int slotMapWest = -1;

	private World world;
	private EntityPlayer player;
	
	private ContainerAtlas container;

	public GuiAtlas(InventoryPlayer inventoryPlayer, World worldy, EntityPlayer playa)
	{
		super(new ContainerAtlas(inventoryPlayer, worldy));
		this.world = worldy;
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		this.atlasStack = inventoryPlayer.getCurrentItem();
		this.player = playa;
		NBTTagCompound tags = this.atlasStack.getTagCompound();
		if (tags != null)
		{
			this.selectedSlot = tags.getInteger("mapSlot");
			this.autoCenter = tags.getBoolean("autoCenter");
			this.autoCreate = tags.getBoolean("autoCreate");
			this.zoomLevel = tags.getInteger("zoomLevel");
			setSelectedSlot(this.selectedSlot);
		}
	}
	
    @Override
    public void initGui()
    {
    	super.initGui();
    	Keyboard.enableRepeatEvents(true);
		int w = (width - this.guiWidth) / 2;
		int h = (height - this.guiHeight) / 2;
    	buttonList.clear();
    	buttonList.add(this.btoggleAutoCenter = new GuiButton(1, w+85, h+45, 30, 20, this.autoCenter ? I18n.translateToLocal("gui.atlas.yes") : I18n.translateToLocal("gui.atlas.no")));
    	buttonList.add(this.btoggleAutoCreate = new GuiButton(2, w+85, h+70, 30, 20, this.autoCreate ? I18n.translateToLocal("gui.atlas.yes") : I18n.translateToLocal("gui.atlas.no")));
    	buttonList.add(this.bzoomNeg = new GuiButtonAddSubtract(3, w+65, h+25, 1, 1.0f));
    	buttonList.add(this.bzoomPos = new GuiButtonAddSubtract(4, w+100, h+25, 0, 1.0f));
    	
    	buttonList.add(bAtlasMode = new GuiButtonAtlasControls(10, w+216, h+157, 5));
    	buttonList.add(bInventoryMode = new GuiButtonAtlasControls(11, w+216, h+177, 6));
    	bInventoryMode.mouseMode = 11;
    }
    
    @Override
    protected void actionPerformed(GuiButton click)
    {

    	switch (click.id)
    	{
    		case 0:{break;}
    		case 1:
    		{
    			this.autoCenter = !this.autoCenter; 
    			if (!this.autoCenter)
    			{
    				this.autoCreate = false;
    			} 
    			this.initGui(); break;
			}
    		case 2:
    		{
    			this.autoCreate = !this.autoCreate; 
    			if (this.autoCreate)
    			{
    				this.autoCenter = true;
				} 
    			this.initGui(); 
    			break;
			}
    		case 3:
    		{
    			if (this.zoomLevel > 0)
    			{
    				this.bzoomNeg.pressed = true;
    				this.zoomLevel--;
    			}
    			break;
    		}
    		case 4:
    		{
    			if (this.zoomLevel < 4)
    			{
    				this.bzoomPos.pressed = true;
    				this.zoomLevel++;
    			}
    			break;
    		}
    		case 10:
    		{
    			this.changeGui = true;
    			sendUpdatePacket();
    			
    			break;
    		}
    	}
    	
    	
    }
    
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    	if (!changeGui)
    	{
    		sendUpdatePacket();
    	}
    	
    }
    
    public void sendUpdatePacket()
    {
		BiblioNetworking.INSTANCE.sendToServer(new BiblioAtlas(autoCenter, autoCreate, zoomLevel, selectedSlot, changeGui));
    	// ByteBuf buffer = Unpooled.buffer();
    	// buffer.writeBoolean(autoCenter);
    	// buffer.writeBoolean(autoCreate);
    	// buffer.writeInt(zoomLevel);
    	// buffer.writeInt(selectedSlot);
    	// buffer.writeBoolean(changeGui);
    	// BiblioCraft.ch_BiblioAtlas.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlas"));
    }
	
    @Override
    protected void mouseClicked(int mousex, int mousey, int click)
    {
 		int w = (width - this.guiWidth) / 2;
 		int h = (height - this.guiHeight) / 2;
    	if (click == 2 || this.isCtrlKeyDown())
    	{
    		for (int i = 0; i<7; i++)
    		{
    			for (int j = 0; j<6; j++)
    			{
		    		if (mousex >= w+138+(j*18) && mousex<w+138+18+(j*18) && mousey >= h+15+(i*18) && mousey < h+15+18+(i*18))
		    		{
		    			this.selectedSlot = 6+(j+(i*6));
		    			setSelectedSlot(this.selectedSlot);
		    		}
    			}
    		}
    	}
    	else
    	{
    		int heldSlot = this.player.inventory.currentItem;
    		//System.out.println("hey hey hey "+heldSlot+"    "+mousex+"     "+(w+48+heldSlot*18));
    		 if (mousex >= w+47+heldSlot*18 && mousex <= w+49+(heldSlot+1)*18 && mousey >= h+216 && mousey <= h+218+18)
    		 {
    			return; 
    		 }
    		 else
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
    	}
    	
    
    }
    
    private void setSelectedSlot(int selSlot)
    {
    	//System.out.println("getting a fresh item proboly");
    	ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
    	if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemAtlas)
    	{
    		this.atlasStack = stack;
    	}
    			
    	NBTTagCompound tags = atlasStack.getTagCompound();
    	if (tags != null)
    	{
			InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", true, 48);
			NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < atlasInventory.getSizeInventory())
				{
					ItemStack invStack = new ItemStack(tag);
					atlasInventory.setInventorySlotContents(slot, invStack);
				}
			}
			this.mapX = 0;
			this.mapZ = 0;
			this.mapZoom = 0;
			this.slotMapNorth = -1;
			this.slotMapEast = -1;
			this.slotMapSouth = -1;
			this.slotMapWest = -1;
			ItemStack selectedStack = atlasInventory.getStackInSlot(selSlot);
			if (selectedStack != ItemStack.EMPTY)
			{
				if (tags.hasKey("maps"))
				{
					NBTTagList mapTags = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
					for (int i = 0; i<mapTags.tagCount(); i++)
					{
						NBTTagCompound newTags = mapTags.getCompoundTagAt(i);
						if (newTags.hasKey("mapName") && newTags.getString("mapName").contentEquals("Map_"+selectedStack.getItemDamage()))
						{
							this.mapX = newTags.getInteger("xCenter");
							this.mapZ = newTags.getInteger("zCenter");
							this.mapZoom = newTags.getInteger("mapScale");
						}
					}
					

					searchForNearbyMaps(0, mapTags, atlasInventory);
					searchForNearbyMaps(1, mapTags, atlasInventory);
					searchForNearbyMaps(2, mapTags, atlasInventory);
					searchForNearbyMaps(3, mapTags, atlasInventory);
				}
			}
    	}
    }
    /** directions: 0 = north, 1 = east, 2 = south, 3 = west
     * 
     * @param direction
     * @param maps
     */
	private void searchForNearbyMaps(int direction, NBTTagList maps, InventoryBasic inventory)
	{
		for (int i = 0; i<maps.tagCount(); i++)
		{
			NBTTagCompound newTags = maps.getCompoundTagAt(i);
			if (newTags.hasKey("mapName"))
			{
				int newZoom = newTags.getInteger("mapScale");
				if (newZoom == this.zoomLevel)
				{
					int mapSize = 128*(int)Math.pow(2, (this.zoomLevel));
					int newX = newTags.getInteger("xCenter");
					int newZ = newTags.getInteger("zCenter");
					//System.out.println(this.mapX+"     "+newX+"       "+this.mapZ+"       "+newY+"         "+this.mapZoom+"         "+newZoom);
					switch (direction)
					{
						case 0:
						{
							if ((this.mapZ-mapSize) == newZ && this.mapX == newX)
							{
								findMatchingMap(newTags.getString("mapName"), inventory, 0);
							}
							break;
						}
						case 1:
						{
							if ((this.mapX+mapSize) == newX && this.mapZ == newZ)
							{
								findMatchingMap(newTags.getString("mapName"), inventory, 1);
							}
							break;
						}
						case 2:
						{
							if ((this.mapZ+mapSize) == newZ && this.mapX == newX)
							{
								findMatchingMap(newTags.getString("mapName"), inventory, 2);
							}
							break;
						}
						case 3:
						{
							if ((this.mapX-mapSize) == newX && this.mapZ == newZ)
							{
								findMatchingMap(newTags.getString("mapName"), inventory, 3);
							}
							break;
						}
						
					}
				}
			}
		}
	}
	
	private void findMatchingMap(String mapName, InventoryBasic inventory, int direction)
	{
		///System.out.println(mapName);
		for (int i = 0; i<inventory.getSizeInventory(); i++)
		{
			ItemStack map = inventory.getStackInSlot(i);
			if (map != ItemStack.EMPTY && map.getItem() ==Items.FILLED_MAP)
			{
				if (mapName.contentEquals("Map_"+map.getItemDamage()))
				{
					switch (direction)
					{
						case 0: {this.slotMapNorth = i; return;}
						case 1: {this.slotMapEast = i; return;}
						case 2: {this.slotMapSouth = i; return;}
						case 3: {this.slotMapWest = i; return;}
					}
				}
			}
		}
	}
    
    @Override	
	public void updateScreen()
    {
        super.updateScreen();
        
    	ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
    	if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemAtlas)
    	{
    		this.atlasStack = stack;
    	}
    	
    	NBTTagCompound tags = atlasStack.getTagCompound();
    	if (tags != null)
    	{
    		boolean testContainerUpdate = tags.getBoolean("containerUpdate");
    		if (testContainerUpdate)
    		{
    			tags.setBoolean("containerUpdate", false);
    			this.atlasStack.setTagCompound(tags);
				this.setSelectedSlot(this.selectedSlot);
    		}
    	}
        
    }
    
    
    @Override
    protected void keyTyped(char par1, int key)
    {
    	if (key == 1)
    	{
    		 this.mc.player.closeScreen();
    	}
    	else if (key == 57) // space bar
    	{     
    		if (this.hoveredSlot != -1)
    		{
    			this.selectedSlot = this.hoveredSlot;    
    			this.setSelectedSlot(this.hoveredSlot);
    		}
    	}
    	//super.keyTyped(par1, key);
    }
    
    
    @Override
    public void drawDefaultBackground()
    {
        //this.drawWorldBackground(0);
    }
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) 
	{
		//this.mc.gameSettings.isKeyDown(p_100015_0_)
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.ATLASGUI);
		int w = (width - xSize) / 2;
		int h = (height - ySize) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, xSize, ySize);
		this.hoveredSlot = -1;
		this.mc.getTextureManager().bindTexture(CommonProxy.ATLASGUIBUTTONS);
		for (int i = 0; i<7; i++)
		{
			for (int j = 0; j<6; j++)
			{
	    		if (x >= w+138+(j*18) && x<w+138+18+(j*18) && y >= h+15+(i*18) && y < h+15+18+(i*18))
	    		{
	    			//System.out.println("map# "+(j+(i*6)));

	    			this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 20, 0, 18, 18);
	    			this.hoveredSlot = 6+(j+(i*6));
	    		}
	    		
    			if ((6+(j+(i*6))) == this.selectedSlot)
    			{
    				// render highlighted thing
    				this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 0, 0, 18, 18);
    				if (this.slotMapNorth != -1)
    				{
    					//System.out.println("woo");
    					this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 41, 20, 18, 18);
    				}
    				if (this.slotMapEast != -1)
    				{
    					//System.out.println("foo");
    					this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 21, 20, 18, 18);
    				}
    				if (this.slotMapSouth != -1)
    				{
    					//System.out.println("coo");
    					this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 1, 20, 18, 18);
    				}
    				if (this.slotMapWest != -1)
    				{
    					//System.out.println("hoo");
    					this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 61, 20, 18, 18);
    				}
    			}
    			
    			if (this.slotMapNorth-6 == (j+i*6))
    			{
    				this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 1, 20, 18, 18);
    			}
    			if (this.slotMapEast-6 == (j+i*6))
    			{
    				this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 61, 20, 18, 18);
    			}
    			if (this.slotMapSouth-6 == (j+i*6))
    			{
    				this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 41, 20, 18, 18);
    			}
    			if (this.slotMapWest-6 == (j+i*6))
    			{
    				this.drawTexturedModalRect(w+137+(j*18), h+14+(i*18), 21, 20, 18, 18);
    			}
			}
		}
		bAtlasMode.drawButton(this.mc, 0, 0, 0f);
		bInventoryMode.drawButton(this.mc, 0, 0, 0f);
		
	
		
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.atlas"), w+39, h+12, 0x000000, false); 
		
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.zoom"), w+12, h+28, 0x000000, false);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.center"), w+12, h+52, 0x000000, false);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.create"), w+12, h+78, 0x000000, false);
		this.fontRenderer.drawString((this.zoomLevel+1)+"", w+86, h+28, 0x0000BB, false);
		
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.mapXcenter"), w+12, h+94, 0x000000, false);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.mapZcenter"), w+12, h+103, 0x000000, false);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.atlas.mapZoom"), w+12, h+112, 0x000000, false);
		
		this.fontRenderer.drawString(""+this.mapX, w+84, h+94, 0x0000BB, false);
		this.fontRenderer.drawString(""+this.mapZ, w+84, h+103, 0x0000BB, false);
		this.fontRenderer.drawString(""+(this.mapZoom+1), w+90, h+112, 0x0000BB, false);
		
		//System.out.println(x+"    "+y);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		
		//System.out.println(x+"    "+y);

	}

	@Override
    public void drawScreen(int x, int y, float f)
    {
		super.drawScreen(x, y, f);
		
		if (isCtrlKeyDown())
    	{
			if (x >= this.btoggleAutoCenter.x && x < (this.btoggleAutoCenter.x+this.btoggleAutoCenter.width) && y >= this.btoggleAutoCenter.y && y < (this.btoggleAutoCenter.y+this.btoggleAutoCenter.height))
			{
				List lst = new ArrayList();
				lst.add(I18n.translateToLocal("gui.atlas.autocenter.tt1"));
				lst.add(I18n.translateToLocal("gui.atlas.autocenter.tt2"));
				lst.add(I18n.translateToLocal("gui.atlas.autocenter.tt3"));
				lst.add(I18n.translateToLocal("gui.atlas.autocenter.tt4"));
				lst.add(I18n.translateToLocal("gui.atlas.autocenter.tt5"));
				this.drawHoveringText(lst, x, y+15, this.fontRenderer);
			}
			
			if (x >= this.btoggleAutoCreate.x && x < (this.btoggleAutoCreate.x+this.btoggleAutoCreate.width) && y >= this.btoggleAutoCreate.y && y < (this.btoggleAutoCreate.y+this.btoggleAutoCreate.height))
			{
				List lst = new ArrayList();
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt1"));
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt2"));
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt3"));
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt4"));
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt5"));
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt6"));
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt7"));
				lst.add(" ");
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt8"));
				lst.add(I18n.translateToLocal("gui.atlas.autocreate.tt9"));
				this.drawHoveringText(lst, x, y+15, this.fontRenderer);
			}
			
			if (x >= this.bAtlasMode.x && x < (this.bAtlasMode.x+this.bAtlasMode.width) && y >= this.bAtlasMode.y && y < (this.bAtlasMode.y+this.bAtlasMode.height))
			{
				List lst = new ArrayList();
				lst.add(I18n.translateToLocal("gui.atlas.switchtoatlas"));
				this.drawHoveringText(lst, x, y+15, this.fontRenderer);
			}
    	}
		this.renderHoveredToolTip(x, y);
    }
}
