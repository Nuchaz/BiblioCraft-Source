package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerNameTester;
import jds.bibliocraft.items.ItemNameTester;
import jds.bibliocraft.items.ItemSlottedBook;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.util.Constants;

public class GuiNameTester extends GuiContainer
{
	private ItemStack testerStack;
	private ItemStack currentStack = ItemStack.EMPTY;
	private EntityPlayer player;
	
	 
	public GuiNameTester(InventoryPlayer inventoryPlayer, ItemStack handStack)
	{
		super(new ContainerNameTester(inventoryPlayer));
		if (inventoryPlayer.getCurrentItem() != ItemStack.EMPTY)
		{
			if (inventoryPlayer.getCurrentItem().getItem() instanceof ItemSlottedBook)
			{
				this.testerStack = inventoryPlayer.getCurrentItem();
				updateInventorySlotData();
			}
		}
		this.player = inventoryPlayer.player;
	}
	
    @Override
    public void initGui()
    {
    	super.initGui();
    	buttonList.clear();
    }

    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.SLOTTEDBOOKGUI);
		int w = (width - 172) / 2;
		int h = (height - 240) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, 174, 256);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.testeritem.name"), w+30, h+16, 0x000000, false); 
		this.fontRenderer.drawString(I18n.translateToLocal("gui.testeritem.display"), w+32, h+62, 0x6A6A6A, false); 
		this.fontRenderer.drawString(I18n.translateToLocal("gui.testeritem.unlocal"), w+32, h+82, 0x5A5A5A, false); 
		this.fontRenderer.drawString("Meta Value:", w+32, h+102, 0x5A5A5A, false); 
		if (this.currentStack != ItemStack.EMPTY)
		{
			
			this.fontRenderer.drawString(this.currentStack.getDisplayName(), w+42, h+72, 0x00AA00, false); 
			
			this.fontRenderer.drawString(this.currentStack.getUnlocalizedName(), w+42, h+92, 0x00AA00, false);
			
			this.fontRenderer.drawString(this.currentStack.getItemDamage()+"", w+92, h+102, 0x00AA00, false);
			
			//RegistryNamespaced registry = GameData.getBlockRegistry();
			//
			//this.fontRenderer.drawString(GameData.getBlockRegistry().getNameForObject(currentStack), w+59, h+82, 0x000000, false);
			//System.out.println(GameData.getItemRegistry().getNameForObject(currentStack));
		}
		
	}
	
    @Override
    public void onGuiClosed()
    {
    	//Minecraft.getMinecraft().setIngameNotInFocus();
    }
	
    @Override	
	public void updateScreen()
    {
        super.updateScreen();
        
        ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
    	if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemNameTester)
    	{
    		this.testerStack = stack;
    	}
    	
    	NBTTagCompound tags = testerStack.getTagCompound();
    	if (tags != null)
    	{
    		boolean testContainerUpdate = tags.getBoolean("containerUpdate");
    		if (testContainerUpdate)
    		{
    			tags.setBoolean("containerUpdate", false);
    			this.testerStack.setTagCompound(tags);
    			updateInventorySlotData();
    		}
    	}
    }
    
    private void updateInventorySlotData()
    {
    	if (this.testerStack != ItemStack.EMPTY)
    	{
    		NBTTagCompound tags = this.testerStack.getTagCompound();
    		if (tags != null)
    		{
    			NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
    			InventoryBasic inventory = new InventoryBasic("TestInventory", false, 1);
				for (int i = 0; i < tagList.tagCount(); i++)
				{
					
					NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
					byte slot = tag.getByte("Slot");
					if (slot >= 0 && slot < inventory.getSizeInventory())
					{
						ItemStack invStack = new ItemStack(tag);
						inventory.setInventorySlotContents(slot, invStack);
					}
				}
				
				this.currentStack = inventory.getStackInSlot(0);
    		}
    	}
    }
    
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}

