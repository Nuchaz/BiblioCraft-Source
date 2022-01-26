package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioClock;
import jds.bibliocraft.tileentities.TileEntityClock;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

public class GuiClock extends GuiScreen
{
	private int guiWidth = 176;
	private int guiHeight = 166;
	
	private String stringTick = I18n.translateToLocal("gui.clock.ticksound");//"Tick Sound";
	private String stringChimeSound = I18n.translateToLocal("gui.clock.chimesound");//"Chime Sound";
	private String stringRedstone = I18n.translateToLocal("gui.clock.redstone");//"Redstone";
	private String stringChime = I18n.translateToLocal("gui.clock.chime");//"Chime";
	private String stringPreset = I18n.translateToLocal("gui.clock.presets");//"Presets";
	
	private String stringPulse[] = {I18n.translateToLocal("gui.clock.pulse"), I18n.translateToLocal("gui.clock.solid")};
	private int pulse = 0;
	
	private EntityPlayer player;
	private TileEntityClock clock;
	
	private int[] chimeSettings = new int[48];
	private int[] redstoneSettings = new int[48];
	private boolean toggleTick = false;
	private boolean toggleChime = false;
	private boolean toggleRedstone = false;
	
	private GuiButton buttonCPreset1;
	private GuiButton buttonCPreset2;
	private GuiButton buttonCPreset3;
	private GuiButton buttonCPreset4;
	private GuiButton buttonCPreset5;
	
	private GuiButton buttonRPreset1;
	private GuiButton buttonRPreset2;
	private GuiButton buttonRPreset3;
	private GuiButton buttonRPreset4;
	private GuiButton buttonRPreset5;
	
	private GuiButton buttonTogglePulse;
	
	public GuiClock(TileEntityClock tile, EntityPlayer playa)
	{
		this.clock = tile;
		this.player = playa;
		this.chimeSettings = this.clock.chimeSettings;
		this.redstoneSettings = this.clock.redstoneSettings;
		this.toggleTick = this.clock.tickSound;
		this.toggleChime = this.clock.chimes;
		this.toggleRedstone = this.clock.redstone;
		if (this.clock.isRedstonePulse)
		{
			this.pulse = 0;
		}
		else
		{
			this.pulse = 1;
		}
		// get important data and save to
	}
	
	
    @Override
    public void initGui()
    {
    	super.initGui();
		int w = (this.width - this.guiWidth) / 2;
		int h = (this.height - this.guiHeight) / 2;
    	buttonList.clear();
    	buttonList.add(buttonCPreset1 = new GuiButton(1, w+3, h+20, 16, 20, "1"));
    	buttonList.add(buttonCPreset2 = new GuiButton(2, w+3, h+42, 16, 20, "2"));
    	buttonList.add(buttonCPreset3 = new GuiButton(3, w+3, h+64, 16, 20, "3"));
    	buttonList.add(buttonCPreset4 = new GuiButton(4, w+3, h+86, 16, 20, "4"));
    	buttonList.add(buttonCPreset5 = new GuiButton(5, w+3, h+108, 16, 20, "C"));
 
    	buttonList.add(buttonRPreset1 = new GuiButton(6, w+157, h+20, 16, 20, "1"));
    	buttonList.add(buttonRPreset2 = new GuiButton(7, w+157, h+42, 16, 20, "2"));
    	buttonList.add(buttonRPreset3 = new GuiButton(8, w+157, h+64, 16, 20, "3"));
    	buttonList.add(buttonRPreset4 = new GuiButton(9, w+157, h+86, 16, 20, "4"));
    	buttonList.add(buttonRPreset5 = new GuiButton(10, w+157, h+108, 16, 20, "C"));
    	
    	//buttonList.add(buttonTogglePulse = new GuiButton(11, w+40, h+162, 100, 20, "Pulse"));
    	
    }
    
	@Override
	public void drawScreen(int x, int y, float f)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int w = (this.width - this.guiWidth) / 2;
		int h = (this.height - this.guiHeight) / 2;
		this. mc.getTextureManager().bindTexture(CommonProxy.CLOCKGUI);
		
		this.drawTexturedModalRect(w, h, 0, 0, this.guiWidth, this.guiHeight);
		
		int value = 0;
		for (float i = 0.0f; i < 360.0f; i = i+7.5f)
		{
			if (chimeSettings[value] == 1)
			{
				this.drawTexturedModalRect(w-2+(this.guiWidth/2)+(int)(64*Math.cos(i*(Math.PI/180))), h+68+(int)(64*Math.sin(i*(Math.PI/180))), 7, 170, 5, 5);
			}
			else
			{
				this.drawTexturedModalRect(w-2+(this.guiWidth/2)+(int)(64*Math.cos(i*(Math.PI/180))), h+68+(int)(64*Math.sin(i*(Math.PI/180))), 0, 170, 5, 5);
			}
			
			if(redstoneSettings[value] == 1)
			{
				this.drawTexturedModalRect(w-2+(this.guiWidth/2)+(int)(56*Math.cos(i*(Math.PI/180))), h+68+(int)(57*Math.sin(i*(Math.PI/180))), 14, 170, 5, 5);
			}
			else
			{
				this.drawTexturedModalRect(w-2+(this.guiWidth/2)+(int)(56*Math.cos(i*(Math.PI/180))), h+68+(int)(57*Math.sin(i*(Math.PI/180))), 0, 170, 5, 5);
			}
			value++;
		}
		
		if (this.toggleTick)
		{
			this.drawTexturedModalRect(w+7, h+149, 0, 180, 42, 10);
		}
		if (this.toggleChime)
		{
			this.drawTexturedModalRect(w+67, h+149, 0, 180, 42, 10);
			this.drawTexturedModalRect(w+86, h+146, 7, 170, 5, 5);
		}
		else
		{
			this.drawTexturedModalRect(w+86, h+146, 0, 170, 5, 5);
		}
		if (this.toggleRedstone)
		{
			this.drawTexturedModalRect(w+127, h+149, 0, 180, 42, 10);
			this.drawTexturedModalRect(w+146, h+146, 14, 170, 5, 5);
		}
		else
		{
			this.drawTexturedModalRect(w+146, h+146, 0, 170, 5, 5);
		}
		
		if (this.pulse == 1)
		{
			this.drawTexturedModalRect(w+127, h+133, 0, 180, 42, 10);
		}
		
		buttonCPreset1.drawButton(mc, 0, 0, 0f);
		buttonCPreset2.drawButton(mc, 0, 0, 0f);
		buttonCPreset3.drawButton(mc, 0, 0, 0f);
		buttonCPreset4.drawButton(mc, 0, 0, 0f);
		buttonCPreset5.drawButton(mc, 0, 0, 0f);
		buttonRPreset1.drawButton(mc, 0, 0, 0f);
		buttonRPreset2.drawButton(mc, 0, 0, 0f);
		buttonRPreset3.drawButton(mc, 0, 0, 0f);
		buttonRPreset4.drawButton(mc, 0, 0, 0f);
		buttonRPreset5.drawButton(mc, 0, 0, 0f);
		
	
		GL11.glScalef(0.6f, 0.6f, 0.6f);
		int wx = (int) ((1.0f/0.6f)*(w+29));
		int hy = (int) ((1.0f/0.6f)*(h+152));
		this.drawCenteredString(this.fontRenderer, this.stringChime, wx-22, hy-240, 0x00FF00);
		this.drawCenteredString(this.fontRenderer, this.stringPreset, wx-22, hy-230, 0x00FF00);
		this.drawCenteredString(this.fontRenderer, this.stringRedstone, wx+217, hy-240, 0xFF0000);
		this.drawCenteredString(this.fontRenderer, this.stringPreset, wx+217, hy-230, 0xff0000);
		this.drawCenteredString(this.fontRenderer, this.stringTick, wx, hy, 0xffffff);
		this.drawCenteredString(this.fontRenderer, this.stringChimeSound, wx+100, hy, 0xffffff);
		this.drawCenteredString(this.fontRenderer, this.stringRedstone, wx+200, hy, 0xffffff);
		this.drawCenteredString(this.fontRenderer, this.stringPulse[this.pulse], wx+199, hy-27, 0xffffff);
		GL11.glScalef((1.0f/0.6f), (1.0f/0.6f), (1.0f/0.6f));
		//this.draw
		super.drawScreen(x, y, f);
	}
	
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	switch (click.id)
    	{
    		case 0:
			{

				break;
			}
    		case 1:
			{
				this.chimeSettings = this.setPreset(this.chimeSettings, 4);
				break;
			}
    		case 2:
			{
				this.chimeSettings = this.setPreset(this.chimeSettings, 3);
				break;
			}
    		case 3:
			{
				this.chimeSettings = this.setPreset(this.chimeSettings, 2);
				break;
			}
    		case 4:
			{
				this.chimeSettings = this.setPreset(this.chimeSettings, 1);
				break;
			}
    		case 5:
			{
				this.chimeSettings = this.setPreset(this.chimeSettings, 0);
				break;
			}
    		case 6:
			{
				this.redstoneSettings = this.setPreset(this.redstoneSettings, 4);
				break;
			}
    		case 7:
			{
				this.redstoneSettings = this.setPreset(this.redstoneSettings, 3);
				break;
			}
    		case 8:
			{
				this.redstoneSettings = this.setPreset(this.redstoneSettings, 2);
				break;
			}
    		case 9:
			{
				this.redstoneSettings = this.setPreset(this.redstoneSettings, 1);
				break;
			}
    		case 10:
			{
				this.redstoneSettings = this.setPreset(this.redstoneSettings, 0);
				break;
			}
    	}
    }
    
    // type = chime or redstone
    //preset = 0 = clear, 1 = every 6hr, 2 = 3hr, 3 = 1hr, 4 = .5hr
    private int[] setPreset(int[] array, int preset)
    {
    	for (int i = 0; i < 48; i++)
    	{
    		switch (preset)
    		{
    			case 0:
    			{
    				array[i] = 0;
    				break;
    			}
    			case 1:
    			{
    				if (i%12 == 0)
    				{
    					array[i] = 1;
    				}
    				else
    				{
    					array[i] = 0;
    				}
    				break;
    			}
    			case 2:
    			{
    				if (i%4 == 0)
    				{
    					array[i] = 1;
    				}
    				else
    				{
    					array[i] = 0;
    				}
    				break;
    			}
    			case 3:
    			{
    				if (i%2 == 0)
    				{
    					array[i] = 1;
    				}
    				else
    				{
    					array[i] = 0;
    				}
    				break;
    			}
    			case 4:
    			{
    				array[i] = 1;
    				break;
    			}
    		}
    	}
    	
    	return array;
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
		int w = (this.width - this.guiWidth) / 2;
		int h = (this.height - this.guiHeight) / 2;
		
		int value = 0;
		// first check for
		for (float i = 0.0f; i<360.0f; i = i+7.5f)
		{
			int x = w-2+(this.guiWidth/2)+(int)(64*Math.cos(i*(Math.PI/180)));
			int y = h+68+(int)(64*Math.sin(i*(Math.PI/180)));
			
			if (left >= x && left < x+5 && top >= y && top < y+5)	
			{
				if (chimeSettings[value] == 1)
				{
					chimeSettings[value] = 0;
				}
				else
				{
					chimeSettings[value] = 1;
				}
				//chimeSettings[value] = !chimeSettings[value];
			}
			
			x = w-2+(this.guiWidth/2)+(int)(56*Math.cos(i*(Math.PI/180)));
			y = h+68+(int)(56*Math.sin(i*(Math.PI/180)));
			
			if (left >= x && left < x+5 && top >= y && top < y+5)	
			{
				if (redstoneSettings[value] == 1)
				{
					redstoneSettings[value] = 0;
				}
				else
				{
					redstoneSettings[value] = 1;
				}
				//redstoneSettings[value] = !redstoneSettings[value];
			}
			
			value++;
		}
		
		if (left >= w+7 && left < w+49 && top >= h+149 && top < h+159)
		{
			this.toggleTick = !this.toggleTick;
		}
		
		if (left >= w+67 && left < w+109 && top >= h+149 && top < h+159)
		{
			this.toggleChime = !this.toggleChime;
		}
		
		if (left >= w+127 && left < w+169 && top >= h+149 && top < h+159)
		{
			this.toggleRedstone = !this.toggleRedstone;
		}
		
		if (left >= w+127 && left < w+169 && top >= h+133 && top < h+143)
		{
			if (this.pulse == 0)
			{
				this.pulse = 1;
			}
			else
			{
				this.pulse = 0;
			}
		}
    }
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
        if (par2 == 1)
        {
            this.mc.player.closeScreen();
        }
        if (par2 == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            this.mc.player.closeScreen();
        }
	}
	
    @Override
    public void onGuiClosed()
    {
    	//send packet
    	sendPacket();
    	//player.playSound("bibliocraft:wind", 0.6f, 1.0f);
    	player.playSound(CommonProxy.SOUND_CLOCK_WIND,  0.6f, 1.0f);
    }
    
    public void sendPacket()
    {
    	// ByteBuf buffer = Unpooled.buffer();
    	NBTTagCompound tags = new NBTTagCompound();
    	tags.setIntArray("chimes", this.chimeSettings);
    	tags.setIntArray("redstone", this.redstoneSettings);
    	BiblioNetworking.INSTANCE.sendToServer(new BiblioClock(tags, this.toggleTick, this.toggleChime, this.toggleRedstone, (this.pulse < 1) ? true : false, this.clock.getPos()));
    	// ByteBufUtils.writeTag(buffer, tags);
    	
    	// buffer.writeBoolean(this.toggleTick);
    	// buffer.writeBoolean(this.toggleChime);
    	// buffer.writeBoolean(this.toggleRedstone);
    	// buffer.writeBoolean((this.pulse < 1) ? true : false);
    	
    	// buffer.writeInt(this.clock.getPos().getX());
    	// buffer.writeInt(this.clock.getPos().getY());
    	// buffer.writeInt(this.clock.getPos().getZ());
    	
    	// BiblioCraft.ch_BiblioClock.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioClock"));
    }
}
