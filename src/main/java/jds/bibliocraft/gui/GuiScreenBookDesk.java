package jds.bibliocraft.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioMCBEdit;
import jds.bibliocraft.network.packet.server.BiblioMCBPage;
import jds.bibliocraft.tileentities.TileEntityDesk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
//import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiScreenBookDesk extends GuiScreen
{
    private static final Logger logger = LogManager.getLogger();
    public static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
    /**
     * The player editing the book
     */
    private final EntityPlayer editingPlayer;
    private final ItemStack bookObj;
    /**
     * Whether the book is signed or can still be edited
     */
    private final boolean bookIsUnsigned;
    private boolean bookIsModified;
    private boolean field_146480_s;
    /**
     * Update ticks since the gui was opened
     */
    private int updateCount;
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private String bookTitle = "";
    private GuiScreenBookDesk.NextPageButton buttonNextPage;
    private GuiScreenBookDesk.NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    /**
     * The GuiButton to sign this book.
     */
    private GuiButton buttonSign;
    private GuiButton buttonFinalize;
    private GuiButton buttonCancel;
    private static final String __OBFID = "CL_00000744";
    
    private int i;
    private int j;
    private int k;
	private TileEntityDesk deskTile;

    public GuiScreenBookDesk(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, boolean par3, int tileX, int tileY, int tileZ)
    {
        this.editingPlayer = par1EntityPlayer;
        this.bookObj = par2ItemStack;
        this.bookIsUnsigned = par3;
        
        this.i = tileX;
        this.j = tileY;
        this.k = tileZ;
        TileEntityDesk tile = (TileEntityDesk)editingPlayer.world.getTileEntity(new BlockPos(i, j, k));
        this.deskTile = tile;
        if (deskTile != null)
        {
        	//System.out.println("Getting current page");
        	this.currPage = deskTile.getCurrentPage();
        }
        
        if (par2ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par2ItemStack.getTagCompound();
            this.bookPages = nbttagcompound.getTagList("pages", 8);

            if (this.bookPages != null)
            {
                this.bookPages = (NBTTagList)this.bookPages.copy();
                this.bookTotalPages = this.bookPages.tagCount();

                if (this.bookTotalPages < 1)
                {
                    this.bookTotalPages = 1;
                }
            }
        }

        if (this.bookPages == null && par3)
        {
            this.bookPages = new NBTTagList();
            this.bookPages.appendTag(new NBTTagString(""));
            this.bookTotalPages = 1;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCount;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        if (this.bookIsUnsigned)
        {
            this.buttonList.add(this.buttonSign = new GuiButton(3, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.signButton", new Object[0])));
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0])));
            this.buttonList.add(this.buttonFinalize = new GuiButton(5, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.finalizeButton", new Object[0])));
            this.buttonList.add(this.buttonCancel = new GuiButton(4, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.cancel", new Object[0])));
        }
        else
        {
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done", new Object[0])));
        }

        int i = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.buttonList.add(this.buttonNextPage = new GuiScreenBookDesk.NextPageButton(1, i + 120, b0 + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiScreenBookDesk.NextPageButton(2, i + 38, b0 + 154, false));
        this.updateButtons();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons()
    {
        this.buttonNextPage.visible = !this.field_146480_s && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
        this.buttonPreviousPage.visible = !this.field_146480_s && this.currPage > 0;
        this.buttonDone.visible = !this.bookIsUnsigned || !this.field_146480_s;

        if (this.bookIsUnsigned)
        {
            this.buttonSign.visible = !this.field_146480_s;
            this.buttonCancel.visible = this.field_146480_s;
            this.buttonFinalize.visible = this.field_146480_s;
            this.buttonFinalize.enabled = this.bookTitle.trim().length() > 0;
        }
    }

    private void sendBookToServer(boolean publish)
    {
        if (this.bookIsUnsigned && this.bookIsModified)
        {
            if (this.bookPages != null)
            {
                String s;

                while (this.bookPages.tagCount() > 1)
                {
                    s = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);

                    if (s.length() != 0)
                    {
                        break;
                    }

                    this.bookPages.removeTag(this.bookPages.tagCount() - 1);
                }

                if (this.bookObj.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
                    nbttagcompound.setTag("pages", this.bookPages);
                }
                else
                {
                    this.bookObj.setTagInfo("pages", this.bookPages);
                }

                //s = "MC|BEdit";

                if (publish)
                {
                    //s = "MC|BSign";
                    this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
                    this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));
                    //this.bookObj.func_150996_a(Items.written_book);
                    //this.bookObj.setItem(Items.WRITTEN_BOOK); // TODO removed this line, verrify this latter
                }

                // ByteBuf buffer = Unpooled.buffer();

                try
                {
                    BiblioNetworking.INSTANCE.sendToServer(new BiblioMCBEdit(new BlockPos(i, j, k), currPage, this.bookObj));
                	// ByteBufUtils.writeItemStack(buffer, this.bookObj);
                	// buffer.writeInt(i);
                	// buffer.writeInt(j);
                	// buffer.writeInt(k);
                	// buffer.writeInt(currPage);
                	// BiblioCraft.ch_BiblioMCBEdit.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMCBEdit"));
                }
                catch (Exception exception)
                {
                    logger.error("Couldn\'t send book info", exception);
                }
                finally
                {
                	//buffer.release(); //  learn if I need to release() on all bytebuf stuff
                }
            }
        }
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            if (p_146284_1_.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.sendBookToServer(false);
            }
            else if (p_146284_1_.id == 3 && this.bookIsUnsigned)
            {
                this.field_146480_s = true;
            }
            else if (p_146284_1_.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
                else if (this.bookIsUnsigned)
                {
                    this.addNewPage();

                    if (this.currPage < this.bookTotalPages - 1)
                    {
                        ++this.currPage;
                    }
                }
            }
            else if (p_146284_1_.id == 2)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }
            else if (p_146284_1_.id == 5 && this.field_146480_s)
            {
                this.sendBookToServer(true);
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (p_146284_1_.id == 4 && this.field_146480_s)
            {
                this.field_146480_s = false;
            }

            this.updateButtons();
            
            if (this.deskTile != null)
            {
                try
                {
                	BiblioNetworking.INSTANCE.sendToServer(new BiblioMCBPage(new BlockPos(i, j, k), currPage));
                    // buffer.writeInt(i);
                	// buffer.writeInt(j);
                	// buffer.writeInt(k);
                	// buffer.writeInt(currPage);
                	// BiblioCraft.ch_BiblioMCBPage.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMCBPage"));
                }
                catch (Exception var6)
                {
                    var6.printStackTrace();
                }
            }
        }
    }

    private void addNewPage()
    {
        if (this.bookPages != null && this.bookPages.tagCount() < 50)
        {
            this.bookPages.appendTag(new NBTTagString(""));
            ++this.bookTotalPages;
            this.bookIsModified = true;
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        try
				{
						super.keyTyped(par1, par2);
				}
				catch (IOException e)
				{
						e.printStackTrace();
				}

        if (this.bookIsUnsigned)
        {
            if (this.field_146480_s)
            {
                this.func_146460_c(par1, par2);
            }
            else
            {
                this.keyTypedInBook(par1, par2);
            }
        }
    }

    /**
     * Processes keystrokes when editing the text of a book
     */
    private void keyTypedInBook(char p_146463_1_, int p_146463_2_)
    {
        switch (p_146463_1_)
        {
            case 22:
                this.func_146459_b(GuiScreen.getClipboardString());
                return;
            default:
                switch (p_146463_2_)
                {
                    case 14:
                        String s = this.func_146456_p();

                        if (s.length() > 0)
                        {
                            this.func_146457_a(s.substring(0, s.length() - 1));
                        }

                        return;
                    case 28:
                    case 156:
                        this.func_146459_b("\n");
                        return;
                    default:
                        if (ChatAllowedCharacters.isAllowedCharacter(p_146463_1_))
                        {
                            this.func_146459_b(Character.toString(p_146463_1_));
                        }
                }
        }
    }

    private void func_146460_c(char p_146460_1_, int p_146460_2_)
    {
        switch (p_146460_2_)
        {
            case 14:
                if (!this.bookTitle.isEmpty())
                {
                    this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
                    this.updateButtons();
                }

                return;
            case 28:
            case 156:
                if (!this.bookTitle.isEmpty())
                {
                    this.sendBookToServer(true);
                    this.mc.displayGuiScreen((GuiScreen)null);
                }

                return;
            default:
                if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_))
                {
                    this.bookTitle = this.bookTitle + Character.toString(p_146460_1_);
                    this.updateButtons();
                    this.bookIsModified = true;
                }
        }
    }

    private String func_146456_p() //pageGetCurrent
    {
        return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(this.currPage) : "";
    }

    private void func_146457_a(String p_146457_1_) // pageSetCurrent
    {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        {
            this.bookPages.set(this.currPage, new NBTTagString(p_146457_1_));
            this.bookIsModified = true;
        }
    }

    private void func_146459_b(String p_146459_1_) // pageInsertIntoCurrent
    {
        String s1 = this.func_146456_p();
        String s2 = s1 + p_146459_1_;
        int i = this.fontRenderer.getStringWidth(s2 + "" + TextFormatting.BLACK + "_"); // TODO this might be front, went from getStringSplitWidth to getStringWidth


        if (i <= 118 && s2.length() < 256)
        {
            this.func_146457_a(s2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookGuiTextures);
        int k = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        String s;
        String s1;
        int l;

        if (this.field_146480_s)
        {
            s = this.bookTitle;

            if (this.bookIsUnsigned)
            {
                if (this.updateCount / 6 % 2 == 0)
                {
                    s = s + "" + TextFormatting.BLACK + "_";
                }
                else
                {
                    s = s + "" + TextFormatting.GRAY + "_";
                }
            }

            s1 = I18n.format("book.editTitle", new Object[0]);
            l = this.fontRenderer.getStringWidth(s1);
            this.fontRenderer.drawString(s1, k + 36 + (116 - l) / 2, b0 + 16 + 16, 0);
            int i1 = this.fontRenderer.getStringWidth(s);
            this.fontRenderer.drawString(s, k + 36 + (116 - i1) / 2, b0 + 48, 0);
            String s2 = I18n.format("book.byAuthor", new Object[] {this.editingPlayer.getName()});
            int j1 = this.fontRenderer.getStringWidth(s2);
            this.fontRenderer.drawString(TextFormatting.DARK_GRAY + s2, k + 36 + (116 - j1) / 2, b0 + 48 + 10, 0);
            String s3 = I18n.format("book.finalizeWarning", new Object[0]);
            this.fontRenderer.drawSplitString(s3, k + 36, b0 + 80, 116, 0);
        }
        else
        {
            s = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
            s1 = "";

            if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
            {
                s1 = this.bookPages.getStringTagAt(this.currPage);
            }

            if (this.bookIsUnsigned)
            {
                if (this.fontRenderer.getBidiFlag())
                {
                    s1 = s1 + "_";
                }
                else if (this.updateCount / 6 % 2 == 0)
                {
                    s1 = s1 + "" + TextFormatting.BLACK + "_";
                }
                else
                {
                    s1 = s1 + "" + TextFormatting.GRAY + "_";
                }
            }

            l = this.fontRenderer.getStringWidth(s);
            this.fontRenderer.drawString(s, k - l + this.bookImageWidth - 44, b0 + 16, 0);
            this.fontRenderer.drawSplitString(s1, k + 36, b0 + 16 + 16, 116, 0);
        }

        super.drawScreen(par1, par2, par3);
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
        {
            private final boolean field_146151_o;
            private static final String __OBFID = "CL_00000745";

            public NextPageButton(int par1, int par2, int par3, boolean par4)
            {
                super(par1, par2, par3, 23, 13, "");
                this.field_146151_o = par4;
            }

            /**
             * Draws this button to the screen.
             */
            public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
            {
                if (this.visible)
                {
                    boolean flag = p_146112_2_ >= this.x && p_146112_3_ >= this.y && p_146112_2_ < this.x + this.width && p_146112_3_ < this.y + this.height;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    p_146112_1_.getTextureManager().bindTexture(GuiScreenBookDesk.bookGuiTextures);
                    int k = 0;
                    int l = 192;

                    if (flag)
                    {
                        k += 23;
                    }

                    if (!this.field_146151_o)
                    {
                        l += 13;
                    }

                    this.drawTexturedModalRect(this.x, this.y, k, l, 23, 13);
                }
            }
        }

}
