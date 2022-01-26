package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioMCBEdit;
import jds.bibliocraft.network.packet.server.BiblioRecipeCraft;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class GuiRecipeBook extends GuiScreen
{
	
	private int guiImageWidth = 256;
	private int guiImageHeight = 158;
	private ItemStack recipeBook;
	private NonNullList<ItemStack> bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
	private ItemStack resultStack = ItemStack.EMPTY;
	private int heightOffset = 0;
	private int widthOffset = 0;
	private int[] ingredientCounts = new int[9];
	private String[] ingredientNames = new String[9];
	private int ingredientsTest;
	private GuiButton buttonSave;
	private GuiButton buttonSign;
	private GuiButton buttonSigned;
	private GuiButton buttonCancel;
	private GuiButton buttonCreate;
	private GuiBiblioTextField[] text = new GuiBiblioTextField[10];
	private boolean edited = false;
	private boolean signed = false;
	private boolean signing = false;
	private boolean onDesk = false;
	private int xcoord = 0;
	private int ycoord = 0;
	private int zcoord = 0;
	private int inventorySlot = 0;
	private boolean canCraft = false;
	
	public GuiRecipeBook(ItemStack book, boolean isOnDesk, int x, int y, int z, int slot, boolean canCraft)
	{
		this.recipeBook = book;
		this.onDesk = isOnDesk;
		this.xcoord = x;
		this.ycoord = y;
		this.zcoord = z;
		this.inventorySlot = slot;
		this.canCraft = canCraft;
		// load the book grid from here
		loadBookGrid();
		compareingredients();
	}
	
	private void loadBookGrid()
	{
		NBTTagCompound nbt = this.recipeBook.getTagCompound();
		if (nbt != null)
		{
			NBTTagList tagList = nbt.getTagList("grid", Constants.NBT.TAG_COMPOUND);
			this.bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
			for (int i = 0; i < 9; i++)
			{
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < 9)
				{
					ItemStack nbtStack = new ItemStack(tag);
					if (nbtStack != null)
					{
						this.bookGrid.set(slot,  nbtStack);//[slot] = nbtStack;
					}
				}
			}
			
			NBTTagCompound resultTag = nbt.getCompoundTag("result");
			if (resultTag != null)
			{
				resultStack = new ItemStack(resultTag);
			}
			
			this.signed = nbt.getBoolean("signed");
			
		}
	}
	
	private void saveText()
	{
		NBTTagCompound nbt = this.recipeBook.getTagCompound();
		if (nbt != null)
		{
			NBTTagList lines = new NBTTagList();
			for (int n = 0; n<10; n++)
			{
				lines.appendTag(new NBTTagString(this.text[n].getText()));
			}
			nbt.setTag("text", lines);
			nbt.setBoolean("edited", true);
			this.recipeBook.setTagCompound(nbt);
		}
	}
	
	private void loadText()
	{
		NBTTagCompound nbt = this.recipeBook.getTagCompound();
		if (nbt != null)
		{
			NBTTagList lines = nbt.getTagList("text", Constants.NBT.TAG_STRING);
			if (lines != null)
			{
				for (int n = 0; n<10; n++)
				{
					this.text[n].setText(lines.getStringTagAt(n));
				}
			}
			this.edited = nbt.getBoolean("edited");
		}
	}
	
	public void compareingredients()
	{
		ingredientCounts = new int[9];
		ingredientNames = new String[9];
		NBTTagCompound nbt = this.recipeBook.getTagCompound();
		if (nbt != null)
		{
			NBTTagList tagList = nbt.getTagList("grid", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < 9; i++)
			{
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				//System.out.println(slot);
				if (slot >= 0 && slot < 9)
				{
					ItemStack nbtStack = new ItemStack(tag);
					if (nbtStack != ItemStack.EMPTY && !nbtStack.getUnlocalizedName().contentEquals(ItemStack.EMPTY.getUnlocalizedName()))
					{
						int n = 0;
						boolean complete = false;
						boolean havematch = false;
						for (int m = 0; m < this.ingredientNames.length; m++)
						{
							if (this.ingredientNames[m] != null)
							{
								if (this.ingredientNames[m].matches(nbtStack.getDisplayName()))
								{
									n = m;
									havematch = true;
								}
							}
						}
						
						if (havematch)
						{
							this.ingredientCounts[n] += 1;
						}
						else
						{
							while (!complete)
							{
								if (this.ingredientCounts[n] == 0)
								{
									this.ingredientCounts[n] += 1;
									this.ingredientNames[n] = nbtStack.getDisplayName();
									complete = true;
								}
								else
								{
									if (n < 8)
									{
										n++;
									}
								}
							}
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
    	buttonList.clear();
    	Keyboard.enableRepeatEvents(true);
    	int widthRender = (this.width - this.guiImageWidth) / 2;
    	int heightRender = (this.height - this.guiImageHeight) / 2;
    	if (this.signing)
    	{
    		buttonList.add(this.buttonCancel = new GuiButton(2, widthRender+70, heightRender+130, 40, 20, I18n.translateToLocal("gui.atlas.transfer.cancel")));
	    	buttonList.add(this.buttonSigned = new GuiButton(3, widthRender+20, heightRender+130, 40, 20, I18n.translateToLocal("gui.atlas.yes")));
    	}
    	else
    	{
    		if (!this.signed)
    		{
    			buttonList.add(this.buttonSave = new GuiButton(0, widthRender+70, heightRender+130, 40, 20, I18n.translateToLocal("book.save")));
    			buttonList.add(this.buttonSign = new GuiButton(1, widthRender+20, heightRender+130, 40, 20, I18n.translateToLocal("book.sign")));
    		}
	    	for (int n = 0; n<10; n++)
	    	{
	    		this.text[n] = new GuiBiblioTextField(fontRenderer, (int)((widthRender+12)*(1.0f/0.8f)), (int)((heightRender+22+(10*n))*(1.0f/0.8f)), 136, 10);
	    		this.text[n].setEnableBackgroundDrawing(false);
	    		this.text[n].setMaxStringLength(24);
	    		this.text[n].setTextColor(0x000000);
	    	}
	    	loadText();
    	}
    	if (canCraft && this.resultStack != ItemStack.EMPTY && this.inventorySlot != -1) 
    	{
    		buttonList.add(this.buttonCreate = new GuiButton(4, widthRender+212, heightRender+58, 40, 20, "\u00a76"+I18n.translateToLocal("gui.craft")));
    	}
	}
	
    @Override
	public void drawScreen(int x, int y, float f)
    {
    	
    	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    	this.mc.getTextureManager().bindTexture(CommonProxy.RECIPEBOOKGUI);
     	int widthRender = (this.width - this.guiImageWidth) / 2;
    	int heightRender = (this.height - this.guiImageHeight) / 2;
    	this.drawTexturedModalRect(widthRender, heightRender, 0, 0, this.guiImageWidth, this.guiImageHeight);
    	
    	super.drawScreen(x, y, f);
    	if (signing)
    	{
    		this.fontRenderer.drawString(I18n.translateToLocal("gui.recipe.areyousure"), (widthRender+20), (heightRender+48), 0x111111, false);
    		this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.recipe.finalize"), (widthRender+20), (heightRender+68), 90, 0x5A5A5A);
    	}
    	
    	
    	GL11.glPushMatrix();
		GL11.glScaled(0.8, 0.8, 0.8);
		for (int i = 0; i<this.ingredientCounts.length; i++)
		{
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			ingredientsTest = this.ingredientCounts[i];
			if (this.ingredientCounts[i] != 0)
			{
				this.fontRenderer.drawString(this.ingredientCounts[i]+"x "+this.ingredientNames[i], (int) ((widthRender+142)*(1.0f/0.8f)), (int)((heightRender+78+(7*i))*(1.0f/0.8f)), 0x111111, false);
			}
		}

		if (resultStack != ItemStack.EMPTY)
		{
			this.fontRenderer.drawString(this.resultStack.getDisplayName(), (int)((widthRender+14)*(1.0f/0.8f)), (int)((heightRender+10)*(1.0f/0.8f)), 0x000000, false);
			this.fontRenderer.drawString(this.resultStack.getDisplayName(), (int)((widthRender+142)*(1.0f/0.8f)), (int)((heightRender+10)*(1.0f/0.8f)), 0x000000, false);
		}
	
		
		if (!this.signing)
		{
			for (int n = 0; n<10; n++)
			{
				this.text[n].drawTextBox();
			}
		}
    	GL11.glPopMatrix();

		if (resultStack != ItemStack.EMPTY)
		{
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemAndEffectIntoGUI(resultStack, widthRender+224, heightRender+37);
		}
		

		for (int n = 0; n<9; n++)
		{
			if (bookGrid.get(n) != ItemStack.EMPTY)
			{
				switch (n)
				{
					case 0:{widthOffset = 0; heightOffset = 0; break;}
					case 1:{widthOffset = 18; heightOffset = 0; break;}
					case 2:{widthOffset = 36; heightOffset = 0; break;}
					case 3:{widthOffset = 0; heightOffset = 18; break;}
					case 4:{widthOffset = 18; heightOffset = 18; break;}
					case 5:{widthOffset = 36; heightOffset = 18; break;}
					case 6:{widthOffset = 0; heightOffset = 36; break;}
					case 7:{widthOffset = 18; heightOffset = 36; break;}
					case 8:{widthOffset = 36; heightOffset = 36; break;}
				}
				RenderHelper.enableGUIStandardItemLighting();
				this.itemRender.renderItemAndEffectIntoGUI(bookGrid.get(n), widthRender+142+widthOffset, heightRender+20+heightOffset); // render items from craft grid. 
			}
		}
		
    }
    
    @Override
	public void updateScreen()
    {
        super.updateScreen();
        //System.out.println("tick");
        if (!edited && !this.text[0].isFocused())
        {
        	this.text[0].setText(I18n.translateToLocal("gui.recipe.description")); 
        }
    }
    
    @Override
 	protected void actionPerformed(GuiButton click)
    {
     	if (click.id == 0)
     	{
     		saveText();
     		sendPacket();
     		this.mc.player.closeScreen();
     	}
     	if (click.id == 1)
     	{
     		saveText();
     		this.signing = true;
     		initGui();
     	}
     	if (click.id == 2)
     	{
     		this.signing = false;
     		initGui();
     	}
     	if (click.id == 3)
     	{
     		this.signed = true;
     		signBook();
     		sendPacket();
     		this.mc.player.closeScreen();
     	}
     	if (click.id == 4)
     	{
     		this.sendRecipeCraftRecipe();
     	}
    }
    
    private void sendRecipeCraftRecipe()
    {
    	// ByteBuf buffer = Unpooled.buffer();
    	// ByteBufUtils.writeItemStack(buffer, this.recipeBook);
    	// buffer.writeInt(this.inventorySlot);
    	if (!this.onDesk)
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioRecipeCraft(this.recipeBook, this.inventorySlot));
	    	// BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioRecipeCraft"));
    	}
    }
    
    private void signBook()
    {
    	NBTTagCompound nbt = this.recipeBook.getTagCompound();
    	if (nbt != null)
    	{
    		nbt.setBoolean("signed", this.signed);
    		this.recipeBook.setTagCompound(nbt);
    	}
    }
    
    private void sendPacket()
    {
    	ByteBuf buffer = Unpooled.buffer();
    	ByteBufUtils.writeItemStack(buffer, this.recipeBook);
    	if (!this.onDesk)
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioUpdateInv(this.recipeBook, false));
	    	//BiblioCraft.ch_BiblioInvStack.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioUpdateInv"));
    	}
    	else
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioMCBEdit(new BlockPos(this.xcoord, this.ycoord, this.zcoord), 0, this.recipeBook));
        	// buffer.writeInt(this.xcoord);
        	// buffer.writeInt(this.ycoord);
        	// buffer.writeInt(this.zcoord);
        	// buffer.writeInt(0);
        	// BiblioCraft.ch_BiblioMCBEdit.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMCBEdit"));
    	}
    }
    
    @Override
    protected void mouseClicked(int x, int y, int click)
    {
    	 try 
    	 {
			super.mouseClicked(x, y, click);
		 } 
    	 catch (IOException e) 
    	 {
			e.printStackTrace();
		 }
    	 if (!signed)
    	 {
	    	 for (int n = 0; n<10; n++)
	    	 {
	    		 if (this.text[n].mouseClicked((int)(x*(1.0f/0.8f)), (int)(y*(1.0f/0.8f)), click))
	    		 {
	    			 
	    		 }
	    	 }
	    	 if (this.text[0].isFocused() && !this.edited)
	    	 {
	    		 this.text[0].setCursorPosition(0);
	    		 this.text[0].setText("");
	    	 }
    	 }
    }
    
    @Override
    protected void keyTyped(char par1, int key)
    {
    	try 
    	{
			super.keyTyped(par1, key);
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	if (!signed)
    	{
	    	if (key == 28 || key == 208) // down
	    	{
				for (int n = 0; n<9; n++)
				{
					if (this.text[n].isFocused())
					{
						this.text[n].setFocused(false);
						this.text[n+1].setFocused(true);
						this.text[n+1].setCursorPosition(this.text[n].getCursorPosition());
						break;
					}
				}
	    	}
	    	else if (key == 200) // up
	    	{
				for (int n = 1; n<10; n++)
				{
					if (this.text[n].isFocused())
					{
						this.text[n].setFocused(false);
						this.text[n-1].setFocused(true);
						this.text[n-1].setCursorPosition(this.text[n].getCursorPosition());
						break;
					}
				}
	    	}
	    	else
	    	{
				for (int n = 0; n<10; n++)
				{
					if (this.text[n].isFocused())
					{
						if (this.text[n].textboxKeyTyped(par1, key))
						{
							if (n == 0)
							{
								this.edited = true;
							}
						}
					}
				}
	    	}
    	}
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    }
}
