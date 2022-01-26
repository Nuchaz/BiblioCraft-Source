package jds.bibliocraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerFancyWorkbench;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioRBook;
import jds.bibliocraft.network.packet.server.BiblioRBookLoad;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import jds.bibliocraft.tileentities.TileEntityFancyWorkbench;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiFancyWorkbench extends GuiContainer
{
	private TileEntityFancyWorkbench benchTile;
	private GuiButtonClipboard buttonWriteRecipe;
	private GuiButtonClipboard buttonLoadItemStack;
	private NonNullList<ItemStack> bookStacks = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
	private ItemStack book = new ItemStack(Items.BOOK, 1, 0);
	private int heightOffset = 0;
	private int widthOffset = 0;
	private int localWidth = (width - 176) / 2;
	private int localHeight = (height - 192) / 2;
	private int playerID = -1;
	private boolean isRecipeBook = false;
	private boolean checkSaveButton = false;
	private boolean checkLoadButton = false;
	private boolean hasLeftBookcase = false;
	private boolean hasRightBookcase = false;
	
	public GuiFancyWorkbench(InventoryPlayer inventoryPlayer, World world, TileEntityFancyWorkbench tile, int playerid, TileEntityBookcase leftBookcase, TileEntityBookcase rightBookcase)
	{
		super(new ContainerFancyWorkbench(inventoryPlayer, world, tile, playerid, leftBookcase, rightBookcase));
		this.xSize = 296; //176
		this.ySize = 192;
		this.benchTile = tile;
		this.playerID = playerid;
		if (leftBookcase != null)
		{
			hasLeftBookcase = true;
		}
		if (rightBookcase != null)
		{
			hasRightBookcase = true;
		}
	}
	
    @Override
    public void initGui()
    {
    	super.initGui();
    	buttonList.clear();
    	ItemStack book = benchTile.getStackInSlot(0);
    	buttonList.add(this.buttonWriteRecipe = new GuiButtonClipboard(0, (this.width/2)-82, (this.height/2)-43, 20, 12, "", true));
    	buttonList.add(this.buttonLoadItemStack = new GuiButtonClipboard(1, (this.width/2)-82, (this.height/2)-75, 20, 12, "", true));
    	this.buttonWriteRecipe.enabled = false;
    	this.buttonWriteRecipe.visible = false;
    	this.buttonLoadItemStack.enabled = false;
    	this.buttonLoadItemStack.visible = false;
    }
    
    @Override
    protected void actionPerformed(GuiButton click)
    {
    	if (click.id == 0)
    	{
    		benchTile.setBookGrid(this.playerID);
			BiblioNetworking.INSTANCE.sendToServer(new BiblioRBook(this.benchTile.getPos()));
			// ByteBuf buffer = Unpooled.buffer();
			// buffer.writeInt(this.benchTile.getPos().getX());
			// buffer.writeInt(this.benchTile.getPos().getY());
			// buffer.writeInt(this.benchTile.getPos().getZ());
			// buffer.writeInt(this.playerID);
		    //BiblioCraft.ch_BiblioTypeUpdate.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioRBook"));
    	}
    	if (click.id == 1)
    	{
    		//System.out.println("sending ... : "+this.playerID);
    		benchTile.loadInvToGridForRecipe(this.playerID);
			BiblioNetworking.INSTANCE.sendToServer(new BiblioRBookLoad(this.benchTile.getPos()));
			// ByteBuf buffer = Unpooled.buffer();
			// buffer.writeInt(this.benchTile.getPos().getX());
			// buffer.writeInt(this.benchTile.getPos().getY());
			// buffer.writeInt(this.benchTile.getPos().getZ());
			// buffer.writeInt(this.playerID);
		    // BiblioCraft.ch_BiblioTypeUpdate.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioRBookLoad"));
    	}
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int x, int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.localWidth = (width - 176) / 2;
		this.localHeight = (height - 192) / 2;
		this.mc.getTextureManager().bindTexture(CommonProxy.FANCYWORKBENCHGUI);
		this.drawTexturedModalRect(localWidth, localHeight, 0, 0, 176, 192);
		
		if (this.hasLeftBookcase)
		{
			this.mc.getTextureManager().bindTexture(CommonProxy.FANCYWORKBENCHBOOKCASEGUI);
			this.drawTexturedModalRect(localWidth-57, localHeight, 0, 0, 55, 180);
		}
		
		if (this.hasRightBookcase)
		{
			this.mc.getTextureManager().bindTexture(CommonProxy.FANCYWORKBENCHBOOKCASEGUI);
			this.drawTexturedModalRect(localWidth+178, localHeight, 0, 0, 55, 180);
		}
		
		this.checkSaveButton = testForShowButton();
		this.checkLoadButton = testForLoadButton();
		
		if (this.checkSaveButton)
		{
			this.mc.getTextureManager().bindTexture(CommonProxy.FANCYWORKBENCHGUI);
			this.drawTexturedModalRect(localWidth+10, localHeight+53, 17, 200, 15, 11); // save arrow off
			if (x > this.localWidth+10 && x < this.localWidth+25 && y > this.localHeight+53 && y < this.localHeight+64)
			{
				
				this.drawTexturedModalRect(localWidth+10, localHeight+53, 17, 211, 15, 11); // save arrow
			}
		}

		if (this.checkLoadButton)
		{
			this.mc.getTextureManager().bindTexture(CommonProxy.FANCYWORKBENCHGUI);
			this.drawTexturedModalRect(localWidth+10, localHeight+22, 0, 200, 15, 11);  // load arrow off
			if (x > this.localWidth+10 && x < this.localWidth+25 && y > this.localHeight+22 && y < this.localHeight+33)
			{
				this.drawTexturedModalRect(localWidth+10, localHeight+22, 0, 211, 15, 11);  // load arow on
			}
		}
		
		bookStacks = benchTile.getBookGrid();
		
		GlStateManager.pushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		
		for (int n = 0; n<9; n++)
		{
			if (bookStacks.get(n) != ItemStack.EMPTY)
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

				GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.6f);
				GlStateManager.colorMask(true, true, false, true);
				this.itemRender.renderItemAndEffectIntoGUI(bookStacks.get(n), localWidth + 30 + widthOffset, localHeight + 17 + heightOffset); 
				GlStateManager.colorMask(true, true, true, true);
				//
			}
		}
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 0.3f);
		GlStateManager.colorMask(true, true, false, true);
		this.itemRender.renderItemAndEffectIntoGUI(this.book, localWidth+8, localHeight+35);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.popMatrix();
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)  // x and y are the location of the mouse.
	{
	
		
		super.drawGuiContainerForegroundLayer(x, y);
		this.localWidth = (width - 176) / 2;
		this.localHeight = (height - 192) / 2;

		if (testForShowButton())
		{
			this.buttonWriteRecipe.enabled = true;
			this.buttonWriteRecipe.visible = true;
		}
		else
		{
			this.buttonWriteRecipe.enabled = false;
			this.buttonWriteRecipe.visible = false;
		}
		
		if (testForLoadButton())
		{
			this.buttonLoadItemStack.enabled = true;
			this.buttonLoadItemStack.visible = true;

		}
		else
		{
			this.buttonLoadItemStack.enabled = false;
			this.buttonLoadItemStack.visible = false;
			
		}

		this.fontRenderer.drawString(I18n.translateToLocal("gui.fancyworkbench"), 8+60, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8+60, ySize - 94 + 2, 4210752);
		
		if (this.hasLeftBookcase)
		{
			this.fontRenderer.drawString(I18n.translateToLocal(I18n.translateToLocal("gui.left")), -40+60, 6, 4210752);  
			this.fontRenderer.drawString(I18n.translateToLocal(I18n.translateToLocal("jds.tileentitybookcase")), -52+60, 15, 4210752); 
		}
		
		if (this.hasRightBookcase)
		{
			this.fontRenderer.drawString(I18n.translateToLocal(I18n.translateToLocal("gui.right")), 195+60, 6, 4210752); 
			this.fontRenderer.drawString(I18n.translateToLocal(I18n.translateToLocal("jds.tileentitybookcase")), 183+60, 15, 4210752); 
		}
		
		if (this.checkSaveButton)
		{
			if (x > this.localWidth+10 && x < this.localWidth+25 && y > this.localHeight+53 && y < this.localHeight+64)
			{
				if (this.isRecipeBook)
				{
					List lst = new ArrayList();
					lst.add(I18n.translateToLocal("book.overwriterecipe"));
					lst.add(I18n.translateToLocal("book.overwriterecipe2"));
					this.drawHoveringText(lst, x-localWidth-50, y-localHeight, fontRenderer);
				}
				else
				{
					List lst = new ArrayList();
					lst.add(I18n.translateToLocal("book.saverecipe"));
					lst.add(I18n.translateToLocal("book.saverecipe2"));
					this.drawHoveringText(lst, x-localWidth-50, y-localHeight, fontRenderer);
				}
			}
		}
		
		if (this.checkLoadButton)
		{
			if (x > this.localWidth+10 && x < this.localWidth+25 && y > this.localHeight+22 && y < this.localHeight+33)
			{
				List lst = new ArrayList();
				lst.add(I18n.translateToLocal("book.loadrecipe"));
				lst.add(I18n.translateToLocal("book.loadrecipe2"));
				this.drawHoveringText(lst, x-localWidth-44, y-localHeight, fontRenderer);
			}
		}
		
	}
	
	private boolean testForLoadButton()
	{
		if (this.benchTile.getStackInSlot(0) != ItemStack.EMPTY)
		{
			if (this.benchTile.getStackInSlot(0).getItem() instanceof ItemRecipeBook)
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean testForShowButton()
	{
		if (this.benchTile.getStackInSlot(0) != ItemStack.EMPTY)
		{
			if (this.benchTile.getStackInSlot(0).getItem() == Items.BOOK)
			{
				NonNullList<ItemStack> grid = this.benchTile.getPlayerGrid();
				for (int n = 0; n<9; n++)
				{
					if (grid.get(n) != ItemStack.EMPTY)		
					{
						return true;
					}
				}
			}
			else if (this.benchTile.getStackInSlot(0).getItem() instanceof ItemRecipeBook)
			{
				NBTTagCompound nbt = this.benchTile.getStackInSlot(0).getTagCompound();
				if (nbt != null)
				{
					this.isRecipeBook = true;
					return !(nbt.getBoolean("signed"));
				}
			}
		}
		return false;
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
