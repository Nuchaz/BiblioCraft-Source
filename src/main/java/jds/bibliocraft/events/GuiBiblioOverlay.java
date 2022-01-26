package jds.bibliocraft.events;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemTapeMeasure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent ;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiBiblioOverlay extends Gui
{
	private Minecraft mc;
	public int mx = 0;
	private int tickcounter = 1;
	private int pretickcount = 1;
	final private FontRenderer fr;// = Minecraft.getMinecraft().fontRenderer;
	private int dheight = 0; //= mc.displayHeight;
	private int dwidth = 0;// = mc.displayWidth;
	private int distance = 0;
	
	private int textCounter = 0;
	private int fadeOutCount = 255;
	//private boolean showText = false;
	//private boolean showTextChanged = false;
	//private String showTextString = "";
	
	public GuiBiblioOverlay(Minecraft mc)
	{
		super();
		this.mc = mc;
		fr = this.mc.fontRenderer;
		//this.mc.currentScreen.height
	}
	
	public void setMeasX(int mex)
	{
		mx = mex;
	}
	
	//@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent.Post event)
	{
		
		if (event.getType() != event.getType().TEXT || event.isCanceled())
		{
			return;
		}
		
		ItemStack headArmor = this.mc.player.inventory.armorItemInSlot(3); 

		if (EventBlockMarkerHighlight.canHeadArmorRead(headArmor))
		{
			NBTTagCompound tags = headArmor.getTagCompound();
			if (tags != null && tags.hasKey("text"))
			{
				dheight = event.getResolution().getScaledHeight() / 2;
				dwidth = event.getResolution().getScaledWidth() / 2;
				NBTTagList names = tags.getTagList("text", Constants.NBT.TAG_STRING);
				for (int i = 0; i < names.tagCount(); i++)
				{
					if (event.getType() == event.getType().TEXT)
					{
						this.drawCenteredString(fr, names.getStringTagAt(i), dwidth+1, dheight + 20 + (i * 9), 16777215);
					}
				}
			}
		}
		
		ItemStack playerhand = this.mc.player.getHeldItem(EnumHand.MAIN_HAND);
		if (playerhand != ItemStack.EMPTY)
		{
			
			if (playerhand.getItem() instanceof ItemTapeMeasure)
			{
				if (tickcounter > 50)
				{
					tickcounter = 0;
					NBTTagCompound tape = playerhand.getTagCompound();
					if (tape != null)
					{
						distance = tape.getInteger("distance");
					}
					//}
				}
				else
				{
					tickcounter++;
				}
				if (distance > 0)
				{
					int guiscale = this.mc.gameSettings.guiScale;
					int guimulti = 0;
					switch(guiscale)
					{
					case 0: guimulti = 4; break;
					default: guimulti = guiscale; break;
					}
					dheight = event.getResolution().getScaledHeight() / 2;
					dwidth = event.getResolution().getScaledWidth() / 2;
					String dist = distance+"m";
					if (event.getType() == event.getType().TEXT)
					{
						this.drawCenteredString(fr, dist, dwidth+1, dheight+20, 16777215);
					}
				}
			}
			
			if (playerhand.getItem() instanceof ItemDrill)
			{
				ItemDrill drill  = (ItemDrill)playerhand.getItem();
				if (drill.showText)
				{
					if (textCounter < 300)
					{
						if (drill.showTextChanged)
						{
							this.textCounter = 0;
							drill.showTextChanged = false;
						}
						if (this.textCounter > 260)
						{
							this.fadeOutCount -= 6;
						}
						else
						{
							this.fadeOutCount = 255;
						}
						dheight = event.getResolution().getScaledHeight() / 2;
						dwidth = event.getResolution().getScaledWidth() / 2;
						GL11.glPushMatrix();
	                    GL11.glEnable(GL11.GL_BLEND);
	                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
						this.drawCenteredString(fr, drill.showTextString, dwidth+1, dheight+60, 16777215 + (fadeOutCount << 24));
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glPopMatrix();
						
						this.textCounter++;
					}
					else
					{
						drill.showText = false;
						this.textCounter = 0;
					}
				}

			}
			
			if (playerhand.getItem() instanceof ItemRecipeBook)
			{
				ItemRecipeBook book  = (ItemRecipeBook)playerhand.getItem();
				if (book.showText)
				{
					if (textCounter < 300)
					{
						if (book.showTextChanged)
						{
							this.textCounter = 0;
							book.showTextChanged = false;
						}
						if (this.textCounter > 260)
						{
							this.fadeOutCount -= 6;
						}
						else
						{
							this.fadeOutCount = 255;
						}
						dheight = event.getResolution().getScaledHeight() / 2;
						dwidth = event.getResolution().getScaledWidth() / 2;

						GL11.glPushMatrix();
	                    GL11.glEnable(GL11.GL_BLEND);
	                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
						this.drawCenteredString(fr, "\u00a76"+book.showTextString, dwidth+1, dheight+80, 16777215 + (fadeOutCount << 24)); 
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glPopMatrix();
						this.textCounter++;
					}
					else
					{
						book.showText = false;
						this.textCounter = 0;
					}
				}
				
			}
			 
			return;
		}
		
	}

}
