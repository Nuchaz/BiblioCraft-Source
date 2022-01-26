package jds.bibliocraft.gui;

import java.io.IOException;
import java.util.ArrayList;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.helpers.FileUtil;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioType;
import jds.bibliocraft.network.packet.server.BiblioTypeDelete;
import jds.bibliocraft.network.packet.server.BiblioTypeFlag;
import jds.bibliocraft.network.packet.server.BiblioTypeUpdate;
import jds.bibliocraft.tileentities.TileEntityTypeMachine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiTypesetting extends GuiScreen //GuiContainer
{
	//protected Minecraft mc;
	//protected fontRenderer fontRenderer;

	public TileEntityTypeMachine typetile;
	//protected GuiSlotBooklist list;
	//public ContainerTypeMachine typecont;
	//private ContainerTypeMachine typecont;
	public String bookName = "Select a book";// = typemachine.getBookname();
	public String savedname = "Loading";
	//public EntityPlayer play;
	public int i;
	public int j;
	public int k;
	public String[] booklist;
	public boolean[] isPublic;	
	public String[] authorList;
	public boolean[] bookButtonRender = {false, false, false, false, false, false, false, false};
	
	private String title = I18n.translateToLocal("gui.typesetting.guiTitle"); 
	private int pageCurr = 1;
	private int pageTotal = 0;
	private GuiButton[] select;
	private GuiButton[] publicToggle;
	private GuiButton[] delete;
	private GuiButton nextPage;
	private GuiButton prevPage;
	private GuiButton exit;
	private GuiButton deleteToggle;
	private boolean deleteable = false;
	//private Minecraft mc = Minecraft.getMinecraft();
	private String playerName;
	private boolean creativeMode;
	private int[] heights;
	private int listStart = 0;
	private int listEnd = 8;
	private boolean isServerSide = false;
	
	public GuiTypesetting(EntityPlayer player, TileEntityTypeMachine tile) /// I might try to stick in a tile entity here
	{
		super();
		this.typetile = tile;
		this.i = typetile.getPos().getX();
		this.j = typetile.getPos().getY();
		this.k = typetile.getPos().getZ();
		this.playerName = player.getName();
		if(player.capabilities.isCreativeMode)// || MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(this.playerName)) 
		{
			this.creativeMode = true; 
		}
		else
		{
			this.creativeMode = false;
		}
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		World world = typetile.getWorld();
		int widthRender = (this.width) / 2;
    	int heightRender = (this.height) / 2;
    	heights = new int[]{heightRender-94,heightRender-69,heightRender-44,heightRender-19,heightRender+6,heightRender+31,heightRender+56,heightRender+81};
    	this.select = new GuiButton[]
    		{
	    	new GuiButton(1, widthRender-160, heightRender-100, 40, 20, I18n.translateToLocal("gui.typesetting.select")),  // id, spce from top, space from side, width, heigh 
	    	new GuiButton(2, widthRender-160, heightRender-75, 40, 20, I18n.translateToLocal("gui.typesetting.select")),   
	    	new GuiButton(3, widthRender-160, heightRender-50, 40, 20, I18n.translateToLocal("gui.typesetting.select")),
	    	new GuiButton(4, widthRender-160, heightRender-25, 40, 20, I18n.translateToLocal("gui.typesetting.select")),
	    	new GuiButton(5, widthRender-160, heightRender, 40, 20, I18n.translateToLocal("gui.typesetting.select")),
	    	new GuiButton(6, widthRender-160, heightRender+25, 40, 20, I18n.translateToLocal("gui.typesetting.select")),
	    	new GuiButton(7, widthRender-160, heightRender+50, 40, 20, I18n.translateToLocal("gui.typesetting.select")),
	    	new GuiButton(8, widthRender-160, heightRender+75, 40, 20, I18n.translateToLocal("gui.typesetting.select"))
    		};
    	this.publicToggle = new GuiButton[]
			{
			new GuiButton(9, widthRender+75, heightRender-100, 42, 20, I18n.translateToLocal("gui.typesetting.public")),
			new GuiButton(10, widthRender+75, heightRender-75, 42, 20, I18n.translateToLocal("gui.typesetting.public")),
			new GuiButton(11, widthRender+75, heightRender-50, 42, 20, I18n.translateToLocal("gui.typesetting.public")),
			new GuiButton(12, widthRender+75, heightRender-25, 42, 20, I18n.translateToLocal("gui.typesetting.public")),
			new GuiButton(13, widthRender+75, heightRender, 42, 20, I18n.translateToLocal("gui.typesetting.public")),
			new GuiButton(14, widthRender+75, heightRender+25, 42, 20, I18n.translateToLocal("gui.typesetting.public")),
			new GuiButton(15, widthRender+75, heightRender+50, 42, 20, I18n.translateToLocal("gui.typesetting.public")),
			new GuiButton(16, widthRender+75, heightRender+75, 42, 20, I18n.translateToLocal("gui.typesetting.public"))
			};
    	this.delete = new GuiButton[]
			{
	    	new GuiButton(17, widthRender+120, heightRender-100, 40, 20, I18n.translateToLocal("gui.typesetting.delete")),
	    	new GuiButton(18, widthRender+120, heightRender-75, 40, 20, I18n.translateToLocal("gui.typesetting.delete")),
	    	new GuiButton(19, widthRender+120, heightRender-50, 40, 20, I18n.translateToLocal("gui.typesetting.delete")),
	    	new GuiButton(20, widthRender+120, heightRender-25, 40, 20, I18n.translateToLocal("gui.typesetting.delete")),
	    	new GuiButton(21, widthRender+120, heightRender, 40, 20, I18n.translateToLocal("gui.typesetting.delete")),
	    	new GuiButton(22, widthRender+120, heightRender+25, 40, 20, I18n.translateToLocal("gui.typesetting.delete")),
	    	new GuiButton(23, widthRender+120, heightRender+50, 40, 20, I18n.translateToLocal("gui.typesetting.delete")),
	    	new GuiButton(24, widthRender+120, heightRender+75, 40, 20, I18n.translateToLocal("gui.typesetting.delete"))
			};
    	this.nextPage = new GuiButton(25, widthRender+10, heightRender+100, 60, 20, I18n.translateToLocal("gui.typesetting.nextPage")); 
    	this.prevPage = new GuiButton(26, widthRender-115, heightRender+100, 60, 20, I18n.translateToLocal("gui.typesetting.prevPage"));
    	this.exit = new GuiButton(0, widthRender+95, heightRender+100, 40, 20, I18n.translateToLocal("gui.typesetting.exit"));
    	this.deleteToggle = new GuiButton(27, widthRender+114, heightRender-120, 46, 20, I18n.translateToLocal("gui.typesetting.enableDiscard")); 

    	for (int i = 0; i < 8; i++)
    	{
    		buttonList.add(select[i]);
    		buttonList.add(publicToggle[i]);
    		buttonList.add(delete[i]);
    	}
    	buttonList.add(nextPage);
    	buttonList.add(prevPage);
    	buttonList.add(exit);
    	buttonList.add(deleteToggle);
    	
		boolean issp = this.mc.isSingleplayer();
		FileUtil util = new FileUtil();
		if (!issp)
		{
			this.isServerSide = true;
			booklist = typetile.getbookList();
			if (booklist != null)
			{
				authorList = typetile.getAuthorList();//util.getAuthorList(booklist, false);
				isPublic = typetile.getPublicList();//util.getPublistList(booklist, false);
			}
			
		}
		else
		{
			this.isServerSide = false;
			//System.out.println("We are on a cleint");
			booklist = util.scanBookDir(world);
			if (booklist != null)
			{
				authorList = util.getAuthorList(booklist, true);
				isPublic = util.getPublistList(booklist, true);
				//this.list = new GuiSlotBooklist(this, i, j, k, world, booklist);
			}
			//this.mc.displayGuiScreen(new GuiBooklist(this, i, j, k, world, false, null));
		}
		if (booklist != null)
		{
			removePrivatesFromList();
		}
		if (booklist != null)
		{
			pageTotal = (booklist.length / 8)+1;
			pageCurr = 1;
			//System.out.println(pageTotal);
			setBookListEnds();
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float f)
	{
		drawDefaultBackground();
		int widthRender = (this.width) / 2;
		int heightRender = (this.height) / 2;
		this. mc.getTextureManager().bindTexture(CommonProxy.TYPEMACHINEGUI_L_PNG);
		this.drawTexturedModalRect(widthRender-170, heightRender-128, 0, 0, 256, 256);
		this. mc.getTextureManager().bindTexture(CommonProxy.TYPEMACHINEGUI_R_PNG);
		this.drawTexturedModalRect(widthRender+86, heightRender-128, 172, 0, 84, 256);
		fontRenderer.drawString(title, widthRender-(title.length()*3), heightRender-119, 0x404040);
		String pages = pageCurr+" "+I18n.translateToLocal("gui.typesetting.pageOfpages")+" "+pageTotal;
		fontRenderer.drawString(pages, widthRender-19-(pages.length()*3), heightRender+106, 0x404040);
		//fontRenderer.drawString("Enable Deleting:", widthRender+30, heightRender-119, 0x404040);
		if (booklist != null)
		{
			for (int h = 0; h < 8; h++)
			{
				if (bookButtonRender[h])
				{
					select[h].visible = true;
					publicToggle[h].visible = true;
				}
				else
				{
					this.select[h].visible = false;
					this.publicToggle[h].visible = false;
				}
				this.delete[h].visible = false;
			}
			for (int b = this.listStart, h = 0; b < this.listEnd; b++, h++)
			{
				fontRenderer.drawString(TextFormatting.RESET + booklist[b], widthRender-115, heights[h], 0x404040);
				if (isPublic[b])
				{
					publicToggle[h].displayString = I18n.translateToLocal("gui.typesetting.public");
				}
				else
				{
					publicToggle[h].displayString = I18n.translateToLocal("gui.typesetting.private");
				}
				if (this.creativeMode || this.authorList[b].contains(this.playerName))
				{
					if (deleteable)
					{
						delete[h].visible = true;
					}
				}
			}
			if (pageCurr == 1)
			{
				if (this.prevPage.visible == true)
				{
					this.prevPage.visible = false;
				}
			}
			else
			{
				if (this.prevPage.visible == false)
				{
					this.prevPage.visible = true;
				}
			}
			if (pageCurr == pageTotal)
			{
				if (this.nextPage.visible == true)
				{
					this.nextPage.visible = false;
				}
			}
			else
			{
				if (this.nextPage.visible == false)
				{
					this.nextPage.visible = true;
				}
			}
		}
		else
		{
			this.prevPage.visible = false;
			this.nextPage.visible = false;
			for (int h = 0; h < 8; h++)
			{
				this.select[h].visible = false;
				this.publicToggle[h].visible = false;
				delete[h].visible = false;
			}
		}
		super.drawScreen(x, y, f);
	}
	
	private void setBookListEnds()
	{
		this.listStart = pageCurr*8-8;
		if (pageCurr == pageTotal)
		{
			this.listEnd = booklist.length;
		}
		else
		{
			this.listEnd = pageCurr*8;
		}
		for (int i = 0; i < 8; i++)
		{
			this.bookButtonRender[i] = false;
		}
		for (int i = 0; i < (this.listEnd - this.listStart); i++)
		{
			this.bookButtonRender[i] = true;
		}
	}
	
    @Override
	protected void actionPerformed(GuiButton click)
    {
    	boolean setExit = false;
    	boolean sendBookname = false;
    	//System.out.println(click.id);
    	String bookFlagTitle = "";
    	boolean setFlagUpdate = false;
    	String deleteBookTitle = "";
    	int flag = 0;
    	boolean setDelete = false;
    	switch (click.id)
    	{
    	case 0:{setExit = true;break;} // Exit
    	case 1:{bookName = booklist[this.listStart]; setExit = true; break;}
    	case 2:{bookName = booklist[this.listStart+1]; setExit = true; break;}
    	case 3:{bookName = booklist[this.listStart+2]; setExit = true; break;}
    	case 4:{bookName = booklist[this.listStart+3]; setExit = true; break;}
    	case 5:{bookName = booklist[this.listStart+4]; setExit = true; break;}
    	case 6:{bookName = booklist[this.listStart+5]; setExit = true; break;}
    	case 7:{bookName = booklist[this.listStart+6]; setExit = true; break;}
    	case 8:{bookName = booklist[this.listStart+7]; setExit = true; break;}
    	case 9:{bookFlagTitle = booklist[this.listStart]; setFlagUpdate = true; flag = this.listStart; break;}
    	case 10:{bookFlagTitle = booklist[this.listStart+1]; setFlagUpdate = true; flag = this.listStart+1; break;}
    	case 11:{bookFlagTitle = booklist[this.listStart+2]; setFlagUpdate = true; flag = this.listStart+2; break;}
    	case 12:{bookFlagTitle = booklist[this.listStart+3]; setFlagUpdate = true; flag = this.listStart+3; break;}
    	case 13:{bookFlagTitle = booklist[this.listStart+4]; setFlagUpdate = true; flag = this.listStart+4; break;}
    	case 14:{bookFlagTitle = booklist[this.listStart+5]; setFlagUpdate = true; flag = this.listStart+5; break;}
    	case 15:{bookFlagTitle = booklist[this.listStart+6]; setFlagUpdate = true; flag = this.listStart+6; break;}
    	case 16:{bookFlagTitle = booklist[this.listStart+7]; setFlagUpdate = true; flag = this.listStart+7; break;}
    	case 17:{deleteBookTitle = booklist[this.listStart]; setDelete = true; flag = this.listStart; break;}
    	case 18:{deleteBookTitle = booklist[this.listStart+1]; setDelete = true; flag = this.listStart+1; break;}
    	case 19:{deleteBookTitle = booklist[this.listStart+2]; setDelete = true; flag = this.listStart+2; break;}
    	case 20:{deleteBookTitle = booklist[this.listStart+3]; setDelete = true; flag = this.listStart+3; break;}
    	case 21:{deleteBookTitle = booklist[this.listStart+4]; setDelete = true; flag = this.listStart+4; break;}
    	case 22:{deleteBookTitle = booklist[this.listStart+5]; setDelete = true; flag = this.listStart+5; break;}
    	case 23:{deleteBookTitle = booklist[this.listStart+6]; setDelete = true; flag = this.listStart+6; break;}
    	case 24:{deleteBookTitle = booklist[this.listStart+7]; setDelete = true; flag = this.listStart+7; break;}
    	case 27:{deleteable = !deleteable; break;}
    	}
    	if (click.id == 25) // Next page
    	{
    		if (pageCurr < pageTotal)
    		{
    			this.pageCurr++;
    			setBookListEnds();
    		}
    	}
    	if (click.id == 26) // Prev page
    	{
    		if (pageCurr > 1)
    		{
    			this.pageCurr--;
    			setBookListEnds();
    		}
    	}

    	if (click.id >= 1 && click.id <= 8)
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioType(bookName, new BlockPos(i, j, k)));
    		// ByteBuf buffer = Unpooled.buffer();
		    // ByteBufUtils.writeUTF8String(buffer, bookName);
		    // buffer.writeInt(i);
		    // buffer.writeInt(j);
		    // buffer.writeInt(k);
		    // BiblioCraft.ch_BiblioType.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioType")); 
    	}
    	
    	if (setFlagUpdate)
    	{
    		if (authorList[flag].contains(playerName) || creativeMode)
    		{
    			//System.out.println("ready to update flag for book "+bookFlagTitle);
    			// ByteBuf buffer = Unpooled.buffer();
    			// ByteBufUtils.writeUTF8String(buffer, bookFlagTitle);
    			// buffer.writeBoolean(!(isPublic[flag]));
    			// buffer.writeBoolean(this.isServerSide);
    			//buffer.writeInt(i);
    		    //buffer.writeInt(j);
    		    //buffer.writeInt(k);
				BiblioNetworking.INSTANCE.sendToServer(new BiblioTypeFlag(bookFlagTitle, !(isPublic[flag])));
    		    // BiblioCraft.ch_BiblioTypeFlag.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioTypeFlag"));
    		    isPublic[flag] = !isPublic[flag];
    		}
    	}
    	
    	if (setDelete)
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioTypeDelete(deleteBookTitle));
    		//System.out.println("ready to delete book  "+deleteBookTitle);
    		// ByteBuf buffer = Unpooled.buffer();
    		// ByteBufUtils.writeUTF8String(buffer, deleteBookTitle);
    		// buffer.writeBoolean(this.isServerSide);
		    // BiblioCraft.ch_BiblioTypeDelete.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioTypeDelete"));
		    deleteBookFromLists(flag);
    	}
    	
    	if (setExit)
    	{
    		this.mc.player.closeScreen();
    	}
    }
    
    private void deleteBookFromLists(int flag)
    {
    	String[] nBooks = new String[booklist.length - 1];
    	String[] nAuthors = new String[authorList.length - 1];
    	boolean[] nPublics = new boolean[isPublic.length - 1];
    	int r = 0;
    	for (int b = 0; b < booklist.length; b++)
    	{
    		if (b != flag)
    		{
    			nBooks[r] = booklist[b];
    			nAuthors[r] = authorList[b];
    			nPublics[r] = isPublic[b];
    			r++;
    		}
    	}
    	booklist = nBooks;
    	authorList = nAuthors;
    	isPublic = nPublics;
		pageTotal = (booklist.length / 8)+1;
		if (pageCurr > pageTotal && pageTotal > 1)
		{
			pageCurr--;
		}
		//System.out.println(pageTotal);
		setBookListEnds();
    }
    
	@Override
    protected void mouseClicked(int left, int top, int par3)
    {
		//System.out.println(left);
		//System.out.println(top);
		try
		{
			super.mouseClicked(left, top, par3);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//System.out.println(par3);
		/*
		if (top > 30 && left > 120 && left < 330)
		{
			//this.mc.thePlayer.closeScreen();   
		}
		*/
    }
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
	         if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.getKeyCode())
	         {
	                 this.mc.player.closeScreen();
	         }
	}
	
    @Override
    public void onGuiClosed()
    {
    	if (isServerSide)
    	{
			BiblioNetworking.INSTANCE.sendToServer(new BiblioTypeUpdate(new BlockPos(this.i, this.j, this.k)));
			// ByteBuf buffer = Unpooled.buffer();
			// buffer.writeInt(this.i);
			// buffer.writeInt(this.j);
			// buffer.writeInt(this.k);
		    // BiblioCraft.ch_BiblioTypeUpdate.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioTypeUpdate"));
    	}
    }
	
	public void setBookList(String[] books)
	{
		booklist = books;
	}
	
	private void removePrivatesFromList()
	{

		if (!this.creativeMode)
		{
			ArrayList books = new ArrayList();
			ArrayList authors = new ArrayList();
			ArrayList publics = new ArrayList();
			//System.out.println("test1");
			for (int b = 0; b < booklist.length; b++)
			{
				if (authorList[b].contains(playerName) || isPublic[b])
				{
					books.add(booklist[b]);
					authors.add(authorList[b]);
					if (isPublic[b])
					{
						publics.add(1);
					}
					else
					{
						publics.add(0);
					}
					
				}
			}
			//System.out.println("test2");
			if (books.size() > 0)
			{
			this.booklist = new String[books.size()];
			this.authorList = new String[authors.size()];
			this.isPublic = new boolean[publics.size()];
			for (int b = 0; b < books.size(); b++)
			{
				System.out.println("test4");
				this.booklist[b] = (String)books.get(b);
				this.authorList[b] = (String)authors.get(b);
				if ((Integer)publics.get(b) == 1)
				{
					this.isPublic[b] = true;
				}
				else
				{
					this.isPublic[b] = false;
				}
			}
			}
			else
			{
				this.booklist = null;
				this.pageTotal = 1;
				this.bookButtonRender = new boolean[]{false, false, false, false, false, false, false, false};
			}
		}
	}
	
}
