package jds.bibliocraft.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class GuiAtlasMap extends GuiScreen
{
	private static int guiWidth = 256;
	private static int guiHeight = 256;
	private ItemStack atlasStack;
	ScaledResolution scaledresolution;// = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
	
	private GuiButtonAtlasControls bSelect;
	private GuiButtonAtlasControls bEdit;
	private GuiButtonAtlasControls bAdd;
	private GuiButtonAtlasControls bAddToCompass1;
	private GuiButtonAtlasControls bAddToCompass2;
	private GuiButtonAtlasControls bAddToCompass3;
	private GuiButtonAtlasControls bAddToCompass4;
	private GuiButtonAtlasControls bAddToCompass5;
	private GuiButtonAtlasControls bAddToCompass6;
	
	private GuiButtonAtlasControls bAtlasMode;
	private GuiButtonAtlasControls bInventoryMode;
	
	private GuiButtonAtlasControls bModeSelect;
	private GuiButtonAtlasControls bModeCopyTo;
	private GuiButtonAtlasControls bModeCopyFrom;
	private GuiButtonAtlasControls bModeCopyPinsTo;
	private GuiButtonAtlasControls bModeCopyPinsFrom;
	

	

	//private String selectedPinName;
	
	private ArrayList xPin;
	private ArrayList zPin;
	private ArrayList pinStrings;
	private ArrayList pinColors;
	
	private int mapXCenter;
	private int mapZCenter;
	private int mapScale;
	
	private int mouseMode = 1;
	private int compassMode = 1;
	
	private EntityPlayer player;
	private World world;
	private InventoryBasic inventory = null;
	private ItemStack currentMapStack = ItemStack.EMPTY;
	
	private boolean hasSelectedPin = false;
	private float selectedPinX = -1.0f;
	private float selectedPinY = -1.0f;
	private float selectedCompasX = -1.0f;
	private float selectedCompasY = -1.0f;
	
	private int clickedPin = -1;
	private int currentPin = -1;
	private boolean hoveringPin = false;
	
	private Random rando;

	private ArrayList compasses;
	private ItemStack[] compassStacks;
	private int[] compassSlots = {0,0,0,0,0,0};
	private GuiBiblioTextField editPinName;
	private GuiButton editAccept;
	private GuiButton editDelete;
	private boolean editMenuToggle = false;
	private int editColor = 0;
	private GuiButtonAddSubtract editColorPos;
	private GuiButtonAddSubtract editColorNeg;
	
	private boolean swapGUI = false;
	
	private int animationTracker = 0;
	private int currentSelectedCompass = -1;
	
	public Tessellator tessellator;
	public BufferBuilder worldRenderer;
	
	public GuiAtlasMap(World worldy, EntityPlayer playa, ItemStack map)
	{
		//System.out.println("Constructor");
		this.world = worldy;
		this.atlasStack = map;
		this.player = playa;
		xPin = new ArrayList();
		zPin = new ArrayList();
		pinColors = new ArrayList();
		pinStrings = new ArrayList();
		compasses = new ArrayList();
		rando = new Random();
		NBTTagCompound tags = map.getTagCompound();
		if (tags != null)
		{
			tags.setInteger("lastGUImode", 1);
			this.atlasStack.setTagCompound(tags);
			if (tags.hasKey("selectedPinX"))
			{
				this.selectedPinX = tags.getFloat("selectedPinX");
				this.selectedPinY = tags.getFloat("selectedPinY");
			}
			int mapSlot = tags.getInteger("mapSlot");

			InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", true, 48); //  maybe temp, chaged from 48 to 216 to allow 5 pages
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
			this.inventory = atlasInventory;
			for (int i = 0; i < 6; i++)
			{
				//System.out.println("test"+i);
				ItemStack compTest = atlasInventory.getStackInSlot(i);
				if (compTest != ItemStack.EMPTY && compTest.getItem() instanceof ItemWaypointCompass)
				{
					this.compasses.add(compTest);
					this.compassSlots[i] = 1;
				}
			}
			//System.out.println("testo");
			this.compassStacks = new ItemStack[0];
			
			if (this.compasses.size() > 0)
			{
				//System.out.println("testenta");
				this.compassStacks = new ItemStack[this.compasses.size()];
				
				for (int i = 0; i<this.compasses.size(); i++)
				{
					//System.out.println("testuna"+i);
					this.compassStacks[i] = (ItemStack)this.compasses.get(i);
				}
			}
			
			if (tags.hasKey("savedCompass"))
			{
				boolean matchedCompass = false;
				this.currentSelectedCompass = -1;
				int savedX = tags.getInteger("compassX");
				int savedZ = tags.getInteger("compassZ");
				for (int i = 0; i<this.compassStacks.length; i++)
				{
					ItemStack testCompass = this.compassStacks[i];
					if (testCompass != ItemStack.EMPTY)
					{
						NBTTagCompound compassTags = testCompass.getTagCompound();
						if (compassTags != null)
						{
							if (compassTags.getInteger("XCoord") == savedX && compassTags.getInteger("ZCoord") == savedZ)
							{
								this.currentSelectedCompass = i;
							}
						}
					}
				}
			}
			if (mapSlot >= 0)
			{
				ItemStack invStack = atlasInventory.getStackInSlot(mapSlot);
				if (invStack != ItemStack.EMPTY)
				{
					this.currentMapStack = invStack;
					
					if (tags.hasKey("maps"))
					{
						NBTTagList mapTags = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
						for (int i = 0; i < mapTags.tagCount(); i++)
						{
							NBTTagCompound newTags = mapTags.getCompoundTagAt(i);
							if (newTags.hasKey("mapName") && newTags.getString("mapName").contentEquals("Map_"+invStack.getItemDamage()))
							{
								this.mapXCenter = newTags.getInteger("xCenter");
								this.mapZCenter = newTags.getInteger("zCenter");
								this.mapScale = newTags.getInteger("mapScale")+1;
								if (newTags.hasKey("xMapWaypoints") && newTags.hasKey("yMapWaypoints") && newTags.hasKey("MapWaypointNames") && newTags.hasKey("MapWaypointColors"))
								{
									NBTTagList mapXPins = newTags.getTagList("xMapWaypoints", Constants.NBT.TAG_FLOAT);
									this.xPin.clear();
									for (int n = 0; n < mapXPins.tagCount(); n++)
									{
										float xpindata = mapXPins.getFloatAt(n);
										this.xPin.add(xpindata);
									}
									
									NBTTagList mapYPins = newTags.getTagList("yMapWaypoints", Constants.NBT.TAG_FLOAT);
									this.zPin.clear();
									for (int n = 0; n < mapYPins.tagCount(); n++)
									{
										float ypindata = mapYPins.getFloatAt(n);
										this.zPin.add(ypindata);
									}
									
									NBTTagList mapPinNames = newTags.getTagList("MapWaypointNames", Constants.NBT.TAG_STRING);
									this.pinStrings.clear();
									for (int n = 0; n < mapPinNames.tagCount(); n++)
									{
										String name = mapPinNames.getStringTagAt(n);
										this.pinStrings.add(name);
									}
									
									NBTTagList mapPinColors = newTags.getTagList("MapWaypointColors", Constants.NBT.TAG_FLOAT); 
									this.pinColors.clear();
									for (int n = 0; n < mapPinColors.tagCount(); n++)
									{
										float color = mapPinColors.getFloatAt(n);
										this.pinColors.add(color);
									}
								}
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private void updateCompassInventory()
	{
		for (int n = 0, m = 0; n < 6; n++)
		{
			if (this.compassSlots[n] == 1)
			{
				ItemStack invStack = this.inventory.getStackInSlot(n);
				if (invStack != ItemStack.EMPTY && invStack.getItem() instanceof ItemWaypointCompass)
				{
					if (m < this.compassStacks.length)
					{
						this.inventory.setInventorySlotContents(n, this.compassStacks[m]);
						m++;
					}
				}
			}
		}
		
		NBTTagCompound tags = atlasStack.getTagCompound();
    	if (tags == null)
    	{
    		tags = new NBTTagCompound();
    	}
    	NBTTagList itemList = new NBTTagList();
    	for (int i = 0; i < inventory.getSizeInventory(); i++)
    	{
    		ItemStack stack = inventory.getStackInSlot(i);
    		if (stack != ItemStack.EMPTY)
    		{
    			NBTTagCompound tag = new NBTTagCompound();
    			tag.setByte("Slot", (byte) i);
    			stack.writeToNBT(tag);
    			itemList.appendTag(tag);
    		}
    	}
    	tags.setTag("Inventory", itemList);
    	this.atlasStack.setTagCompound(tags);
	}
	
	private void updateCompassCoords(boolean writeString, int compassNumber, float mapposx, float mapposz)
	{
		if (compassNumber < this.compassStacks.length)
		{
			NBTTagCompound tags = this.compassStacks[compassNumber].getTagCompound();
			
			if (tags == null)
			{
				tags = new NBTTagCompound();
				tags.setString("WaypointName", "");
			}
			if (tags != null)
			{
				int mapSize = 128*(int)Math.pow(2, (this.mapScale-1));
				
				float fx = 0.0f;
				float fz = 0.0f;
				
				if (writeString)
				{
					fx = (Float)this.xPin.get(this.clickedPin);
					fz = (Float)this.zPin.get(this.clickedPin);
				}
				else
				{
					fx = mapposx;
					fz = mapposz;
				}
				
				int xpos = (this.mapXCenter - (mapSize/2))+(int)(fx*mapSize);
				int ypos = (this.mapZCenter - (mapSize/2))+(int)(fz*mapSize);

				tags.setInteger("XCoord", xpos);
				tags.setInteger("ZCoord", ypos);
				if (writeString)
				{
					String txt = (String)this.pinStrings.get(this.clickedPin);
					tags.setString("WaypointName", txt);
				}
				this.compassStacks[compassNumber].setTagCompound(tags);
				//System.out.println("test3");
				updateCompassInventory();
			}
		}
	}
	
    @Override
    public void initGui()
    {
    	super.initGui();
    	this.tessellator = Tessellator.getInstance();
    	this.worldRenderer = tessellator.getBuffer();
    	Keyboard.enableRepeatEvents(true);
		int w = (width - this.guiWidth) / 2;
		int h = (height - this.guiHeight) / 2;
		double heighAdjust = ((8.0/89.0)*(this.height-20)); 
		double widthAdjust = (0.5)*(-0.75 + width)-(0.375*height);
		double scaler = 0.0058976*(this.mc.displayHeight);
		//double antiScaler = 1.0 / scaler;
		scaledresolution = new ScaledResolution(this.mc);
		
        int scale = scaledresolution.getScaleFactor(); // so this totally works.
		if (scale == 3)
		{
			scaler += 0.08;
		}
        double scaledMapWidth = (scaler*(128*(1.0/scale)));
		double widthAdjust2 = widthAdjust+scaledMapWidth;
		double heightAdjust2 = heighAdjust+scaledMapWidth;
    	buttonList.clear();
    	buttonList.add(bSelect = new GuiButtonAtlasControls(1, (int)widthAdjust-18, (int)heighAdjust+10, 1));
    	buttonList.add(bEdit = new GuiButtonAtlasControls(2, (int)widthAdjust-18, (int)heighAdjust+30, 2));
    	buttonList.add(bAdd = new GuiButtonAtlasControls(3, (int)widthAdjust-18, (int)heighAdjust+50, 3));
    	if (this.compassStacks.length > 0)
    	{
    		buttonList.add(bAddToCompass1 = new GuiButtonAtlasControls(4, (int)widthAdjust-18, (int)heighAdjust+82, 4));
    	}
    	if (this.compassStacks.length > 1)
    	{
    		buttonList.add(bAddToCompass2 = new GuiButtonAtlasControls(5, (int)widthAdjust-18, (int)heighAdjust+102, 4));
    	}
    	if (this.compassStacks.length > 2)
    	{
    		buttonList.add(bAddToCompass3 = new GuiButtonAtlasControls(6, (int)widthAdjust-18, (int)heighAdjust+122, 4));
    	}
    	if (this.compassStacks.length > 3)
    	{
    		buttonList.add(bAddToCompass4 = new GuiButtonAtlasControls(7, (int)widthAdjust-18, (int)heighAdjust+142, 4));
    	}
    	if (this.compassStacks.length > 4)
    	{
    		buttonList.add(bAddToCompass5 = new GuiButtonAtlasControls(8, (int)widthAdjust-18, (int)heighAdjust+162, 4));
    	}
    	if (this.compassStacks.length > 5)
    	{
    		buttonList.add(bAddToCompass6 = new GuiButtonAtlasControls(9, (int)widthAdjust-18, (int)heighAdjust+182, 4));
    	}
    	buttonList.add(editColorNeg = new GuiButtonAddSubtract(20, (this.width/2)-20, (this.height/2)+4, 1, 1.0f));
    	buttonList.add(editColorPos = new GuiButtonAddSubtract(21, (this.width/2)+8, (this.height/2)+4, 0, 1.0f));
    	buttonList.add(editAccept = new GuiButton(22, (this.width/2)+30, (this.height/2)+4, 40, 20, "Ok"));
    	buttonList.add(editDelete = new GuiButton(23, (this.width/2)-70, (this.height/2)+4, 40, 20, "Delete"));
    	this.editPinName = new GuiBiblioTextField(this.fontRenderer, (this.width/2)-84, (this.height/2)-16, 222, 12);
    	this.editPinName.setEnableBackgroundDrawing(false);
    	this.editPinName.setTextColor(0x404040);
    	this.editPinName.setMaxStringLength(42);
    	heighAdjust = ((8.0/89.0)*(this.height-600)); 
		widthAdjust = (0.5)*(-0.75 + width)-(0.375*height);
		widthAdjust2 = widthAdjust+scaledMapWidth;
		heightAdjust2 = heighAdjust+scaledMapWidth;
    	buttonList.add(bAtlasMode = new GuiButtonAtlasControls(10, (int)widthAdjust2+1, (int)heightAdjust2, 5));
    	bAtlasMode.mouseMode = 10;
    	buttonList.add(bInventoryMode = new GuiButtonAtlasControls(11, (int)widthAdjust2+1, (int)heightAdjust2+20, 6));
    	bSelect.mouseMode = 1;
    	bAtlasMode.enabled = false;
    }
    
    @Override
    protected void actionPerformed(GuiButton click)
    {
    	if (click.id >= 1 && click.id <= 9)
    	{
			this.mouseMode = click.id;
			bSelect.mouseMode = this.mouseMode;
			bEdit.mouseMode = this.mouseMode;
			bAdd.mouseMode = this.mouseMode;
    	
	    	if (click.id >= 4 && click.id <= 9)
	    	{
	    		setCompassHighlight(click.id-4);
			}
	    	else
	    	{
	    		this.selectedCompasX = -1.0f;
	    		this.selectedCompasY = -1.0f;
	    	}

    		if (this.compassStacks.length > 0)
        	{
    			bAddToCompass1.mouseMode = this.mouseMode;
        	}
        	if (this.compassStacks.length > 1)
        	{
        		bAddToCompass2.mouseMode = this.mouseMode;
        	}
        	if (this.compassStacks.length > 2)
        	{
        		bAddToCompass3.mouseMode = this.mouseMode;
        	}
        	if (this.compassStacks.length > 3)
        	{
        		bAddToCompass4.mouseMode = this.mouseMode;
        	}
        	if (this.compassStacks.length > 4)
        	{
        		bAddToCompass5.mouseMode = this.mouseMode;
        	}
        	if (this.compassStacks.length > 5)
        	{
        		bAddToCompass6.mouseMode = this.mouseMode;
        	}
    	}
    	
    	switch (click.id)
    	{
    		case 4:
    		{
    			if (mouseMode != 4)
    			{
    				//setCompassHighlight(click.id);
    			}
    			break;
    		}
    		case 11:
    		{
        		this.swapGUI = true;
        		sendPacket();
    			break;
    		}
    		case 20:
    		{
    			if (this.editColor > 0)
    			{
    				this.editColor--;
    			}
    			else
    			{
    				this.editColor = 15;
    			}
    			this.editColorNeg.pressed = true;
    			break;
    		}
    		case 21:
    		{
    			if (this.editColor < 15)
    			{
    				this.editColor++;
    			}
    			else
    			{
    				this.editColor = 0;
    			}
    			this.editColorPos.pressed = true;
    			break;
    		}
    		case 22:
    		{
    			this.pinColors.set(this.currentPin, (float)this.editColor);
    			this.pinStrings.set(this.currentPin, this.editPinName.getText());
    			updateNBTPinData();
    			this.editMenuToggle = false;
    			break;
    		}
    		case 23:
    		{
    			if (this.currentPin >= 0 && this.currentPin < this.xPin.size())
    			{
					this.xPin.remove(this.currentPin);
					this.zPin.remove(this.currentPin);
					this.pinColors.remove(this.currentPin);
					this.pinStrings.remove(this.currentPin);
					updateNBTPinData();
	    			this.editMenuToggle = false;
    			}
    			break;
    		}
    	}
    }
    
    private void setCompassHighlight(int compNum)
    {
    	if (compNum < this.compassStacks.length)
    	{
    		//System.out.println("passed 1st test");
    		NBTTagCompound compTags = this.compassStacks[compNum].getTagCompound();
    		if (compTags == null)
    		{
    			//compTags = new NBTTagCompound();
    		}
    		if (compTags != null)
    		{
    			//System.out.println("got some comp tags");
    			this.selectedCompasX = compTags.getInteger("XCoord");
    			this.selectedCompasY = compTags.getInteger("ZCoord");
    			int invNum = -1;
    			for (int i = 0; i<6; i++)
    			{
    				ItemStack compTester = this.inventory.getStackInSlot(i);
    				if (compTester != ItemStack.EMPTY && compTester.getItem() instanceof ItemWaypointCompass)
    				{
    					NBTTagCompound tagTest = compTester.getTagCompound();
    					if (tagTest != null && tagTest.getInteger("XCoord") == this.selectedCompasX && tagTest.getInteger("ZCoord") == this.selectedCompasY)
    					{
    						invNum = i;
    					}
    				}
    			}
    			this.currentSelectedCompass = compNum;
    			// I think I can save to my NBT for the stack here?
    			NBTTagCompound atlasTags = this.atlasStack.getTagCompound();
    			if (atlasTags != null)
    			{
    				atlasTags.setInteger("compassX", (int)this.selectedCompasX);
    				atlasTags.setInteger("compassZ", (int)this.selectedCompasY);
    				atlasTags.setInteger("savedCompass", invNum); 
    				this.atlasStack.setTagCompound(atlasTags);
    			}
    		}
    	}
    }
    
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    	if (!swapGUI)
    	{
    		sendPacket();
    	}
    }
    
    private void sendPacket()
    {
    	ByteBuf buffer = Unpooled.buffer();
    	ByteBufUtils.writeItemStack(buffer, this.atlasStack);
    	if (swapGUI)
    	{
    		BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(this.atlasStack, true));
			// BiblioCraft.ch_BiblioAtlasGUIswap.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlasSWP"));
    	}
    	else
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(this.atlasStack, false));
	    	//BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
    	}
    }
    
    @Override
    protected void mouseClicked(int mousex, int mousey, int click)
    {
    	//System.out.println(mousex+"    "+mousey+"     "+click);
 		int w = (width - this.guiWidth) / 2;
 		int h = (height - this.guiHeight) / 2;
		double heighAdjust = ((8.0/89.0)*(this.height-20)); 
		double widthAdjust = (0.5)*(-0.75 + width)-(0.375*height);
		double scaler = 0.0058976*(this.mc.displayHeight);
		//double antiScaler = 1.0 / scaler;
		scaledresolution = new ScaledResolution(this.mc);
        int scale = scaledresolution.getScaleFactor(); // so this totally works.
		if (scale == 3)
		{
			scaler += 0.08;
		}
        double scaledMapWidth = (scaler*(128*(1.0/scale)));
		double widthAdjust2 = widthAdjust+scaledMapWidth;
		double heightAdjust2 = heighAdjust+scaledMapWidth;
		float mapPosX = (float)((mousex - widthAdjust)*(1.0/scaledMapWidth));//*(128*(1.0/scale));
		float mapPosZ = (float)((mousey - heighAdjust)*(1.0/scaledMapWidth));
		int mapSize = 128*(int)Math.pow(2, (this.mapScale-1));
		int halfMapSize = mapSize / 2;
		//  mapPosX and mapPosZ are the actual mouse locations on the map that I need to save for the waypoint. 
		// It is the value between 0 and 1 that where 1x1 is the size of the map. Also render is done from these values

		boolean iswithinmap = mousex >= widthAdjust && mousex <= widthAdjust2 && mousey >= heighAdjust && mousey <= heightAdjust2;
		if (iswithinmap)
		{
			
			if (this.editMenuToggle)
			{
				// so do I need any mouse clicks here?, I guess not
			}
			else
			{
				if (mouseMode == 1) // selection mode
				{
					if (hoveringPin && this.currentPin != -1 && this.currentPin < this.xPin.size())
					{
						if (this.selectedPinX == (this.mapXCenter-halfMapSize+((Float)this.xPin.get(this.currentPin)*mapSize)) && this.selectedPinY == (this.mapZCenter-halfMapSize+((Float)this.zPin.get(this.currentPin)*mapSize)))
						{
							this.selectedPinX = -1.0f;
							this.selectedPinY = -1.0f;
						}
						else
						{
							this.selectedPinX = this.mapXCenter-halfMapSize+((Float)this.xPin.get(this.currentPin)*mapSize);
							this.selectedPinY = this.mapZCenter-halfMapSize+((Float)this.zPin.get(this.currentPin)*mapSize);
						}
						updateSelectedPinData();
					}
					else if (isCtrlKeyDown())
					{
						if (this.selectedPinX == (this.mapXCenter-halfMapSize+(mapPosX*mapSize)) && this.selectedPinY == (this.mapZCenter-halfMapSize+(mapPosZ*mapSize)))
						{
							this.selectedPinX = -1.0f;
							this.selectedPinY = -1.0f;
						}
						else
						{
							
							this.selectedPinX = this.mapXCenter-halfMapSize+(mapPosX*mapSize);
							this.selectedPinY = this.mapZCenter-halfMapSize+(mapPosZ*mapSize);
						}
						updateSelectedPinData();
					}
				}

				if (mouseMode == 2) // Add Pin Mode
				{
					//this.mc.gameSettings.isKeyDown(p_100015_0_)
					int randomColor = rando.nextInt(16);
					String newName = "Waypoint "+(this.xPin.size());
					
					
					//if (this.isShiftKeyDown())
					//{
						//System.out.println("Add pin with random color and name @ "+mapPosX+"     "+mapPosZ);
						xPin.add(mapPosX);
						zPin.add(mapPosZ);
						pinColors.add((float)randomColor);
						pinStrings.add(newName);
						updateNBTPinData();
						// then run a method that copies all the pins to the NBT of the current map.
					//}
					//else
					if (!(this.isShiftKeyDown()))
					{
						this.currentPin = this.xPin.size() - 1;
						this.editColor = randomColor;
						this.editPinName.setText(newName);
						//System.out.println("Add pin with popup for color and name @ "+mapPosX+"     "+mapPosZ);
						this.editMenuToggle = true;
					}
					
				}
				if (mouseMode == 3)
				{
					if (hoveringPin && this.currentPin != -1 && this.currentPin < this.xPin.size())
					{
						if (isShiftKeyDown())
						{
							this.xPin.remove(this.currentPin);
							this.zPin.remove(this.currentPin);
							this.pinColors.remove(this.currentPin);
							this.pinStrings.remove(this.currentPin);
							updateNBTPinData();
						}
						else
						{
							float currColor = (Float)this.pinColors.get(this.currentPin);
							this.editColor = (int)currColor;
							this.editPinName.setText((String)this.pinStrings.get(this.currentPin));
							this.editMenuToggle = true;
							// open the edit dialog box
							///(options include)
							
							//Name, Color
							//Delete, Cancel, Ok
						}
					}
				}
				if (mouseMode >= 4 && mouseMode <= 9)
				{
					if (hoveringPin && this.currentPin != -1 && this.currentPin < this.xPin.size())
					{
						this.clickedPin = this.currentPin;
						updateCompassCoords(true, this.mouseMode-4, mapPosX, mapPosZ);
						this.clickedPin = -1;
						setCompassHighlight(this.mouseMode-4);
					}
					else if (isCtrlKeyDown())
					{
						updateCompassCoords(false, this.mouseMode-4, mapPosX, mapPosZ);
						setCompassHighlight(this.mouseMode-4);
					}
				}
				
			}
			//System.out.println("on the map "+mapPosX+"    "+mapPosZ);
		}
 		try 
 		{
			super.mouseClicked(mousex, mousey, click);
		} 
 		catch (IOException e) 
 		{
			e.printStackTrace();
		}
 		if (this.editMenuToggle)
 		{
 			this.editPinName.mouseClicked(mousex, mousey, click);
 		}
    }
    
    private void updateSelectedPinData()
    {
    	NBTTagCompound tags = this.atlasStack.getTagCompound();
    	if (tags != null)
    	{
    		tags.setFloat("selectedPinX", this.selectedPinX);
    		tags.setFloat("selectedPinY", this.selectedPinY);
    		this.atlasStack.setTagCompound(tags);
    	}
    }
    
    public void updateNBTPinData()
    {
    	NBTTagCompound tags = this.atlasStack.getTagCompound();
    	if (tags != null && this.currentMapStack != ItemStack.EMPTY)
    	{
    		if (tags.hasKey("maps"))
    		{
    			NBTTagList mapTags = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
    			NBTTagList newTags = new NBTTagList();
    			for (int i = 0; i<mapTags.tagCount(); i++)
    			{
    				NBTTagCompound map = mapTags.getCompoundTagAt(i);
    				if (map.hasKey("mapName") && map.getString("mapName").contentEquals("Map_"+currentMapStack.getItemDamage()))
    				{
    					// Add pins to the map compound
    					// this is where I add data to the current "map" compound
    					NBTTagList mapXPins = new NBTTagList();
    			    	for (int n = 0; n < this.xPin.size(); n++)
    			    	{
    			    		mapXPins.appendTag(new NBTTagFloat((Float)this.xPin.get(n)));
    			    		//System.out.println(mapXPins.tagCount());
    			    	}
    			    	map.setTag("xMapWaypoints", mapXPins);
    			    	
    			    	NBTTagList mapYPins = new NBTTagList();
    			    	for (int n = 0; n < this.zPin.size(); n++)
    			    	{
    			    		mapYPins.appendTag(new NBTTagFloat((Float)this.zPin.get(n)));
    			    		//System.out.prnntln(mapYPins.tagCount());
    			    	}
    			    	map.setTag("yMapWaypoints", mapYPins);
    			    	
    			    	NBTTagList mapPinNames = new NBTTagList();
    			    	for (int n = 0; n < this.pinStrings.size(); n++)
    			    	{
    			    		mapPinNames.appendTag(new NBTTagString((String)this.pinStrings.get(n)));
    			    	}
    			    	map.setTag("MapWaypointNames", mapPinNames);
    			    	
    			    	NBTTagList mapPinColors = new NBTTagList();
    			    	//int[] colours = new int[this.pinColors.size()];
    			    	for (int n = 0; n < this.pinColors.size(); n++)
    			    	{
    			    		mapPinColors.appendTag(new NBTTagFloat((Float)this.pinColors.get(n)));
    			    	}
    			    	//mapPinColors.appendTag(new NBTTagIntArray(colours));
    			    	map.setTag("MapWaypointColors", mapPinColors);
    				}
    					newTags.appendTag(map);
    			}
    			tags.setTag("maps", newTags);
    			this.atlasStack.setTagCompound(tags);
    			
    			player.inventory.setInventorySlotContents(player.inventory.currentItem, atlasStack);
    			// get the inventory here?
    		}
    	}
    }
    
    @Override	
	public void updateScreen()
    {
    	
        super.updateScreen();
       // System.out.println(bSelect.hovered);
    }
    
    @Override
    public void drawDefaultBackground()
    {
        //this.drawWorldBackground(0);
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    @Override
    protected void keyTyped(char par1, int par2)
    {
    	if (this.editMenuToggle)
    	{
	    	if (this.editPinName.textboxKeyTyped(par1, par2)){}
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
    
	@Override
	public void drawScreen(int mousex, int mousey, float f)
	{
		//System.out.println(bSelect.hovered);
		
		int w = (this.width - this.guiWidth) / 2;
		int h = (this.height - this.guiHeight) / 2;
		bSelect.drawButton(this.mc, 0, 0, 0f);
		bEdit.drawButton(this.mc, 0, 0, 0f);
		bAdd.drawButton(this.mc, 0, 0, 0f);
 		if (this.compassStacks.length > 0)
    	{
 			bAddToCompass1.drawButton(this.mc, 0, 0, 0f);
    	}
    	if (this.compassStacks.length > 1)
    	{
    		bAddToCompass2.drawButton(this.mc, 0, 0, 0f);
    	}
    	if (this.compassStacks.length > 2)
    	{
    		bAddToCompass3.drawButton(this.mc, 0, 0, 0f);
    	}
    	if (this.compassStacks.length > 3)
    	{
    		bAddToCompass4.drawButton(this.mc, 0, 0, 0f);
    	}	
    	if (this.compassStacks.length > 4)
    	{
    		bAddToCompass5.drawButton(this.mc, 0, 0, 0f);
    	}
    	if (this.compassStacks.length > 5)
    	{
    		bAddToCompass6.drawButton(this.mc, 0, 0, 0f);
    	}

		bAtlasMode.drawButton(this.mc, 0, 0, 0f);
		bInventoryMode.drawButton(this.mc, 0, 0, 0f);
		scaledresolution = new ScaledResolution(this.mc);
        int scale = scaledresolution.getScaleFactor(); // so this totally works.
		double heighAdjust = ((8.0/89.0)*(this.height-20)); // width has a very slight adjustment on height (goddamnit jeb)
		double widthAdjust = (0.5)*(-0.75 + width)-(0.375*height);//0.0;	
		double scaler = 0.0058976*(this.mc.displayHeight);//0.00604833*(this.mc.displayHeight-8.106); // seems like the scaled height might be better
		if (scale == 3)
		{
			scaler += 0.08;
		}
		double antiscale = 1.0 / scaler;
		double scaledMapWidth = (scaler*(128*(1.0/scale)));
		double widthAdjust2 = widthAdjust+scaledMapWidth;
		double heightAdjust2 = heighAdjust+scaledMapWidth;
		int mapSize = 128*(int)Math.pow(2, (this.mapScale-1));
		int halfMapSize = mapSize / 2;
		
		if (this.editMenuToggle)
		{
			this.mc.getTextureManager().bindTexture(CommonProxy.ATLASGUIBUTTONS); 
			this.drawTexturedModalRect((this.width/2)-100, (this.height/2)+-42, 0, 100, 200, 71);
			this.mc.getTextureManager().bindTexture(getColorTexture(this.editColor)); 
			this.drawTexturedModalRect((this.width/2)-6, (this.height/2)+3, 0, 0, 12, 13);
			editAccept.enabled = true;
			editAccept.visible = true;
			editColorPos.visible = true;
			editColorPos.enabled = true;
			editColorNeg.visible = true;
			editColorNeg.enabled = true;
			editDelete.enabled = true;
			editDelete.visible = true;
			editAccept.drawButton(this.mc, 0, 0, 0f);
			editColorPos.drawButton(this.mc, 0, 0, 0f);
			editColorNeg.drawButton(this.mc, 0, 0, 0f);
			editDelete.drawButton(this.mc, 0, 0, 0f);
			editPinName.drawTextBox();
			
			this.bInventoryMode.enabled = false;
			this.bAdd.enabled = false;
			this.bSelect.enabled = false;
			this.bEdit.enabled = false;
			//this.bAddToCompass1.enabled = false;
		}
		else
		{
			editAccept.enabled = false;
			editAccept.visible = false;
			editDelete.enabled = false;
			editDelete.visible = false;
			editColorPos.visible = false;
			editColorPos.enabled = false;
			editColorNeg.visible = false;
			editColorNeg.enabled = false;
			
			this.bInventoryMode.enabled = true;
			this.bAdd.enabled = true;
			this.bSelect.enabled = true;
			this.bEdit.enabled = true;
		}
		super.drawScreen(mousex, mousey, f);
		this.mc.getTextureManager().bindTexture(CommonProxy.ATLASGUIBUTTONS); 
		/*
		// drawing all the map pins with colored X's in 2D since I can't render stuff with the map.
		for (int n = 0; n < xPin.size(); n++)
		{
			float Fcolor = ((Float)this.pinColors.get(n));
			int color = (int)Fcolor;
			float pinx = this.mapXCenter - halfMapSize + ((Float)this.xPin.get(n) * mapSize);
			float pinz = this.mapZCenter - halfMapSize + ((Float)this.zPin.get(n) * mapSize);
			double pinX = (pinx - this.mapXCenter) + (mapSize / 2.0);
			pinX = pinX / mapSize;
			pinX = pinX * scaledMapWidth + widthAdjust;
			double pinZ = (pinz - this.mapZCenter) + (mapSize / 2.0);
			pinZ = pinZ / mapSize;
			pinZ = pinZ * scaledMapWidth + heighAdjust;
			int uvx = 32 + (color * 11);
			int uvy = 80;
			GL11.glPushMatrix();
			GL11.glTranslated(pinX - 0.6, pinZ, 0.0);
			this.drawTexturedModalRect(-4, -4, uvx, uvy, 9, 8); 
			GL11.glPopMatrix();
		}
		*/
		
		
		if (animationTracker >= 180)
		{
			this.animationTracker = 0;
		}
		else
		{
			this.animationTracker++;
		}
		
		//System.out.println("ymm");
		if (this.selectedPinX != -1.0f && this.selectedPinY != -1.0f)
		{
			double pinHighlightX = (this.selectedPinX-this.mapXCenter)+(mapSize/2.0);
			pinHighlightX = pinHighlightX / mapSize;
			pinHighlightX = pinHighlightX*scaledMapWidth+widthAdjust;
			double pinHighlightY = (this.selectedPinY-this.mapZCenter)+(mapSize/2.0);
			pinHighlightY = pinHighlightY / mapSize;
			pinHighlightY = pinHighlightY*scaledMapWidth+heighAdjust;
				// shows up on top or bottom of map
			if (this.selectedPinY >= this.mapZCenter+halfMapSize)
			{
				pinHighlightY = scaledMapWidth+heighAdjust;
			}
			else if (this.selectedPinY <= this.mapZCenter-halfMapSize)
			{
				pinHighlightY = heighAdjust;
			}

			// shows up on left or right of map
			if (this.selectedPinX >= this.mapXCenter+halfMapSize)
			{
				pinHighlightX = scaledMapWidth+widthAdjust;
			}
			else if (this.selectedPinX <= this.mapXCenter-halfMapSize)
			{
				pinHighlightX = widthAdjust;
			}

			GL11.glPushMatrix();
			GL11.glTranslated(pinHighlightX, pinHighlightY, 0.0);
			GL11.glRotatef(2*this.animationTracker, 0.0f, 0.0f, 1.0f);
			this.drawTexturedModalRect(-4, -4, 10, 80, 8, 8); 
			GL11.glPopMatrix();
		}
		
		if (this.selectedCompasX != -1.0f && this.selectedCompasY != -1.0f)
		{
			double pinHighlightX = (this.selectedCompasX-this.mapXCenter)+(mapSize/2.0);
			pinHighlightX = pinHighlightX / mapSize;
			pinHighlightX = pinHighlightX*scaledMapWidth+widthAdjust;
			double pinHighlightY = (this.selectedCompasY-this.mapZCenter)+(mapSize/2.0);
			pinHighlightY = pinHighlightY / mapSize;
			pinHighlightY = pinHighlightY*scaledMapWidth+heighAdjust;
			if (this.selectedCompasY >= this.mapZCenter+halfMapSize)
			{
				pinHighlightY = scaledMapWidth+heighAdjust;
			}
			else if (this.selectedCompasY <= this.mapZCenter-halfMapSize)
			{
				pinHighlightY = heighAdjust;
			}

			if (this.selectedCompasX >= this.mapXCenter+halfMapSize)
			{
				pinHighlightX = scaledMapWidth+widthAdjust;
			}
			else if (this.selectedCompasX <= this.mapXCenter-halfMapSize)
			{
				pinHighlightX = widthAdjust;
			}
			
			GL11.glPushMatrix();
			GL11.glTranslated(pinHighlightX, pinHighlightY, 0.0);
			GL11.glRotatef(2*this.animationTracker, 0.0f, 0.0f, 1.0f);
			this.drawTexturedModalRect(-4, -4, 0, 80, 8, 8); 
			GL11.glPopMatrix();

		}
		
		if (this.currentSelectedCompass < this.compassStacks.length && this.currentSelectedCompass >= 0)
		{
			// render the green checkmark on selected compass. the height will be multiplied by the current selected compas int
			this.drawTexturedModalRect((int)widthAdjust-10, (int)heighAdjust+82+20*(this.currentSelectedCompass), 20, 80, 10, 8); 
		}
////////////////
		if (!this.editMenuToggle)
		{
			this.currentPin = -1;
		}
		
		this.hoveringPin = false;
		if (mousex >= widthAdjust && mousex <= widthAdjust2 && mousey >= heighAdjust && mousey <= heightAdjust2 && !editMenuToggle)
		{
			
			switch (this.mouseMode)
			{
				case 2:{this.drawTexturedModalRect(mousex-4, mousey-12, 20, 60, 18, 18); break;} // add pin
				case 3:{this.drawTexturedModalRect(mousex-1, mousey-15, 40, 60, 18, 18); break;} // edit pin
				//case 4:{this.drawTexturedModalRect(mousex, mousey, 60, 60, 18, 18); break;} // compass mode 
			}
			if (this.mouseMode >= 4 && this.mouseMode <= 9)
			{
				this.drawTexturedModalRect(mousex, mousey, 60, 60, 18, 18); 
			}
			
			boolean foundPin = false;
			float mapPosX = (float)((mousex - widthAdjust)*(1.0/scaledMapWidth));//*(128*(1.0/scale));
			float mapPosZ = (float)((mousey - heighAdjust)*(1.0/scaledMapWidth));
			
			for (int n = 0; n < xPin.size(); n++)
			{
				float pinx = (Float)xPin.get(n);
				float pinz = (Float)zPin.get(n);
				if ((mapPosX-0.01f) <= pinx && (mapPosX+0.01f) >= pinx && (mapPosZ-0.01f) <= pinz && (mapPosZ+0.01f) >= pinz)
				{
					//System.out.println("test");
					foundPin = true;
					String mapName = (String)pinStrings.get(n);
					//this.fontRenderer.drawString(mapName, mousex+10, mousey, 0x000000, false);
					
					int xpos = (this.mapXCenter - (mapSize/2))+(int)(pinx*mapSize);
					int ypos = (this.mapZCenter - (mapSize/2))+(int)(pinz*mapSize);
					List lst = new ArrayList();
					lst.add(mapName);
					lst.add("X = "+xpos);
					lst.add("Z = "+ypos);
					this.drawHoveringText(lst, mousex, mousey+15, this.fontRenderer);
					this.currentPin = n;
					this.hoveringPin = true;
				}
			}
			if (!foundPin && this.isCtrlKeyDown())
			{
				
				int xpos = (this.mapXCenter - (mapSize/2))+(int)(mapPosX*mapSize);
				int ypos = (this.mapZCenter - (mapSize/2))+(int)(mapPosZ*mapSize);
				List lst = new ArrayList();
				lst.add("X = "+xpos);
				lst.add("Z = "+ypos);
				this.drawHoveringText(lst, mousex, mousey+15, this.fontRenderer);
			}
			

		}
		//System.out.println(bSelect.hovered);
		if (isCtrlKeyDown())
		{
			if (mousex >= this.bSelect.x && mousex < (this.bSelect.x+this.bSelect.width) && mousey >= this.bSelect.y && mousey < (this.bSelect.y+this.bSelect.height))
			{
				List lst = new ArrayList();
				lst.add(TextFormatting.AQUA+""+TextFormatting.BOLD+I18n.translateToLocal("gui.atlas.select.tt1")); 
				lst.add(I18n.translateToLocal("gui.atlas.select.tt2")); 
				lst.add(I18n.translateToLocal("gui.atlas.select.tt3"));
				lst.add(I18n.translateToLocal("gui.atlas.select.tt4"));
				lst.add(" ");
				lst.add(I18n.translateToLocal("gui.atlas.select.tt5"));
				lst.add(I18n.translateToLocal("gui.atlas.select.tt6"));
				lst.add(I18n.translateToLocal("gui.atlas.select.tt7"));
				lst.add(I18n.translateToLocal("gui.atlas.select.tt8"));
				this.drawHoveringText(lst, mousex, mousey+15, this.fontRenderer);
			}
			if (mousex >= this.bAdd.x && mousex < (this.bAdd.x+this.bAdd.width) && mousey >= this.bAdd.y && mousey < (this.bAdd.y+this.bAdd.height))
			{
				List lst = new ArrayList();
				lst.add(TextFormatting.AQUA+""+TextFormatting.BOLD+I18n.translateToLocal("gui.atlas.edit.tt1"));
				lst.add(I18n.translateToLocal("gui.atlas.edit.tt2"));
				lst.add(I18n.translateToLocal("gui.atlas.edit.tt3"));
				lst.add(I18n.translateToLocal("gui.atlas.edit.tt4"));
				lst.add(" ");
				lst.add(I18n.translateToLocal("gui.atlas.edit.tt5"));
				lst.add(I18n.translateToLocal("gui.atlas.edit.tt6"));
				
				this.drawHoveringText(lst, mousex, mousey+15, this.fontRenderer);
			}
			if (mousex >= this.bEdit.x && mousex < (this.bEdit.x+this.bEdit.width) && mousey >= this.bEdit.y && mousey < (this.bEdit.y+this.bEdit.height))
			{

				
				List lst = new ArrayList();
				lst.add(TextFormatting.AQUA+""+TextFormatting.BOLD+I18n.translateToLocal("gui.atlas.add.tt1"));
				lst.add(I18n.translateToLocal("gui.atlas.add.tt2"));
				lst.add(I18n.translateToLocal("gui.atlas.add.tt3"));
				lst.add(I18n.translateToLocal("gui.atlas.add.tt4"));
				lst.add(I18n.translateToLocal("gui.atlas.add.tt5"));
				lst.add(" ");
				lst.add(I18n.translateToLocal("gui.atlas.add.tt6"));
				lst.add(I18n.translateToLocal("gui.atlas.add.tt7"));
				lst.add(I18n.translateToLocal("gui.atlas.add.tt8"));
				this.drawHoveringText(lst, mousex, mousey+15, this.fontRenderer);
			}
			
			if (mousex >= this.bInventoryMode.x && mousex < (this.bInventoryMode.x+this.bInventoryMode.width) && mousey >= this.bInventoryMode.y && mousey < (this.bInventoryMode.y+this.bInventoryMode.height))
			{
				List lst = new ArrayList();
				lst.add(I18n.translateToLocal("gui.atlas.switchtoinv"));
				this.drawHoveringText(lst, mousex, mousey+15, this.fontRenderer);
			}
		}
		
		if (this.compassStacks.length > 0)
    	{
			showCompassData(mousex, mousey, bAddToCompass1, 0);
    	}
    	if (this.compassStacks.length > 1)
    	{
    		showCompassData(mousex, mousey, bAddToCompass2, 1);
    	}
    	if (this.compassStacks.length > 2)
    	{
    		showCompassData(mousex, mousey, bAddToCompass3, 2);
    	}
    	if (this.compassStacks.length > 3)
    	{
    		showCompassData(mousex, mousey, bAddToCompass4, 3);
    	}
    	if (this.compassStacks.length > 4)
    	{
    		showCompassData(mousex, mousey, bAddToCompass5, 4);
    	}
    	if (this.compassStacks.length > 5)
    	{
    		showCompassData(mousex, mousey, bAddToCompass6, 5);
    	}
	}
	
	private boolean isPositionOnCurrentMap(int mapSize, int mapCenterX, int mapCenterZ)
	{
		
		return false;
	}
	
	private void showCompassData(int mousex, int mousey, GuiButtonAtlasControls control, int stacknum)
	{
		if (this.editMenuToggle)
		{
			control.enabled = false;
		}
		else
		{
			control.enabled = true;
			if (mousex >= control.x && mousex < (control.x+control.width) && mousey >= control.y && mousey < (control.y+control.height))
			{
				NBTTagCompound cTags = this.compassStacks[stacknum].getTagCompound();
				if (cTags != null)
				{
					List lst = new ArrayList();
					lst.add(""+cTags.getString("WaypointName"));
					lst.add("X = "+cTags.getInteger("XCoord"));
					lst.add("Z = "+cTags.getInteger("ZCoord"));
					this.drawHoveringText(lst, mousex, mousey+15, this.fontRenderer);
				}
				
			}
		}
	}
	
    
	public ResourceLocation getColorTexture(float color)
	{
		int colorint = (int)color;
		switch (colorint)
		{
		case 0:return CommonProxy.BLACKWOOL;
		case 1:return CommonProxy.REDWOOL;
		case 2:return CommonProxy.GREENWOOL;
		case 3:return CommonProxy.LIMEWOOL;
		case 4:return CommonProxy.BROWNWOOL;
		case 5:return CommonProxy.BLUEWOOL;
		case 6:return CommonProxy.CYANWOOL;
		case 7:return CommonProxy.LBLUEWOOL;
		case 8:return CommonProxy.PURPLEWOOL;
		case 9:return CommonProxy.MAGENTAWOOL;
		case 10:return CommonProxy.PINKWOOL;
		case 11:return CommonProxy.YELOOWWOOL;
		case 12:return CommonProxy.ORANGEWOOL;
		case 13:return CommonProxy.GRAYWOOL;
		case 14:return CommonProxy.LGRAYWOOL;
		case 15:return CommonProxy.WHITEWOOL;
		default:return CommonProxy.REDWOOL;
		}
	}
}
