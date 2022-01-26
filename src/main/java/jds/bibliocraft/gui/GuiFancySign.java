package jds.bibliocraft.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.Config;
import jds.bibliocraft.containers.ContainerFancySign;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioSign;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class GuiFancySign extends GuiContainer {
	private TileEntityFancySign signTile;
	private RenderItem myItemRenderer;
	// private RenderManager renderManager;
	// private RenderBlocks renderBlocksRi = new RenderBlocks();

	private int editing = 0; // 0 = no editing, 1 = slot 1 editing menu, 2 = slot 2 editing menu, 3 = text
								// editing menu
	private int[] textScales = new int[15];
	private GuiBiblioTextField[] text = new GuiBiblioTextField[15];
	private String[] textLines = new String[15];
	private int slot1Scale = 1;
	private int slot2Scale = 1;
	private int slot1Rot = 0;
	private int slot2Rot = 0;
	private float[] scales = { 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f };
	private float[] antiScales = { (1.0f / scales[0]), (1.0f / scales[1]), (1.0f / scales[2]), (1.0f / scales[3]),
			(1.0f / scales[4]), (1.0f / scales[5]), (1.0f / scales[6]), (1.0f / scales[7]), (1.0f / scales[8]) };
	private String[] rots = { I18n.translateToLocal("gui.sign.left"), I18n.translateToLocal("gui.sign.center"),
			I18n.translateToLocal("gui.sign.right") };
	private String[] scaleItemsStrings = { "1x", "2x", "3x", "4x", "5x", "6x", "7x", "8x", "9x" };
	private String[] scaleTextStrings = { "1x", "1.5x", "2x", "3x", "4x", "5x" };
	private float[] scalesText = { 1.0f, 1.5f, 2.0f, 3.0f, 4.0f, 5.0f };
	private float[] antiScalesText = { (1.0f / scalesText[0]), (1.0f / scalesText[1]), (1.0f / scalesText[2]),
			(1.0f / scalesText[3]), (1.0f / scalesText[4]), (1.0f / scalesText[5]) };
	private int numOfLines = 15;
	private int currentLine = 0;
	private int currentCursorPos = 0;
	private float lineCounter = 0.0f;
	private int linespace = 0;
	private int scaledLinesNumber[] = { 16, 11, 7, 5, 4, 3 };

	private int xAdjust1 = 0;
	private int yAdjust1 = 0;
	private int xAdjust2 = 0;
	private int yAdjust2 = 0;

	private GuiButtonClipboard arrowUp;
	private int arrowUpCountdown = 0;
	private GuiButtonClipboard arrowDown;
	private int arrowDownCountdown = 0;
	private GuiButtonClipboard arrowLeft;
	private int arrowLeftCountdown = 0;
	private GuiButtonClipboard arrowRight;
	private int arrowRightCountdown = 0;
	private GuiButtonClipboard scalePlus;
	private int scalePlusCountdown = 0;
	private GuiButtonClipboard scaleNeg;
	private int scaleNegCountdown = 0;
	private GuiButtonClipboard rotPlus;
	private int rotPlusCountdown = 0;
	private GuiButtonClipboard rotNeg;
	private int rotNegCountdown = 0;
	private GuiButtonClipboard addFormat;
	private int formatCountdown;

	private GuiButtonClipboard selectSlot1;
	private GuiButtonClipboard selectSlot2;
	private GuiButtonClipboard selectText;

	private String[] tiptextcolors = { TextFormatting.BLACK + I18n.translateToLocal("gui.sign.black"),
			TextFormatting.DARK_BLUE + I18n.translateToLocal("gui.sign.darkblue"),
			TextFormatting.DARK_GREEN + I18n.translateToLocal("gui.sign.darkgreen"),
			TextFormatting.DARK_AQUA + I18n.translateToLocal("gui.sign.darkaqua"),
			TextFormatting.DARK_RED + I18n.translateToLocal("gui.sign.darkred"),
			TextFormatting.DARK_PURPLE + I18n.translateToLocal("gui.sign.darkpurple"),
			TextFormatting.GOLD + I18n.translateToLocal("gui.sign.gold"),
			TextFormatting.GRAY + I18n.translateToLocal("gui.sign.gray"),
			TextFormatting.DARK_GRAY + I18n.translateToLocal("gui.sign.darkgray"),
			TextFormatting.BLUE + I18n.translateToLocal("gui.sign.blue"),
			TextFormatting.GREEN + I18n.translateToLocal("gui.sign.green"),
			TextFormatting.AQUA + I18n.translateToLocal("gui.sign.aqua"),
			TextFormatting.RED + I18n.translateToLocal("gui.sign.red"),
			TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("gui.sign.lightpurple"),
			TextFormatting.YELLOW + I18n.translateToLocal("gui.sign.yellow"),
			TextFormatting.WHITE + I18n.translateToLocal("gui.sign.white") };
	private String[] tipcolorsymbols = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f" };

	public GuiFancySign(InventoryPlayer inventoryPlayer, TileEntityFancySign tile) {
		super(new ContainerFancySign(inventoryPlayer, tile));
		this.xSize = 256;
		this.ySize = 246;
		this.signTile = tile;

		this.textLines = tile.text;
		this.textScales = tile.textScale;
		// System.out.println(tile.textScale.length);
		this.slot1Scale = tile.slot1Scale;
		this.slot1Rot = tile.slot1Rot;
		this.xAdjust1 = tile.slot1X;
		this.yAdjust1 = tile.slot1Y;
		this.slot2Scale = tile.slot2Scale;
		this.slot2Rot = tile.slot2Rot;
		this.xAdjust2 = tile.slot2X;
		this.yAdjust2 = tile.slot2Y;
		// I think my tile entity is my go between on the container and gui ?

		if (myItemRenderer == null) {
			myItemRenderer = Minecraft.getMinecraft().getRenderItem();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		int w = (width - 256) / 2;
		int h = (height - 246) / 2;
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		buttonList.add(this.selectSlot1 = new GuiButtonClipboard(1, w + 198, h + 172, 8, 8, "", false));
		buttonList.add(this.selectSlot2 = new GuiButtonClipboard(2, w + 226, h + 172, 8, 8, "", false));
		buttonList.add(this.selectText = new GuiButtonClipboard(3, w + 212, h + 174, 8, 8, "", false));

		if (editing != 0) {
			buttonList.add(this.rotPlus = new GuiButtonClipboard(6, w + 226, h + 200, 12, 12, "", false));
			buttonList.add(this.rotNeg = new GuiButtonClipboard(7, w + 194, h + 200, 12, 12, "", false));

		}

		if (editing == 1 || editing == 2) {
			buttonList.add(this.scalePlus = new GuiButtonClipboard(4, w + 226, h + 187, 12, 12, "", false));
			buttonList.add(this.scaleNeg = new GuiButtonClipboard(5, w + 194, h + 187, 12, 12, "", false));

			buttonList.add(this.arrowUp = new GuiButtonClipboard(8, w + 210, h + 212, 12, 12, "", false));
			buttonList.add(this.arrowDown = new GuiButtonClipboard(9, w + 210, h + 225, 12, 12, "", false));
			buttonList.add(this.arrowRight = new GuiButtonClipboard(10, w + 223, h + 225, 12, 12, "", false));
			buttonList.add(this.arrowLeft = new GuiButtonClipboard(11, w + 197, h + 225, 12, 12, "", false));
		}

		if (editing == 3) {
			buttonList.add(this.addFormat = new GuiButtonClipboard(12, w + 194, h + 214, 12, 12, "", false));
		}

		numOfLines = 0;
		lineCounter = 0.0f;
		linespace = 0;
		for (int n = 0; n < 15; n++) {
			int textX = (int) ((w + 20) * antiScalesText[textScales[n]]);
			int textY = (int) (((h + 12) * antiScalesText[textScales[n]])
					+ (linespace * antiScalesText[textScales[n]]));
			this.text[n] = new GuiBiblioTextField(this.fontRenderer, textX, textY, 240, 8);
			this.text[n].setTextColor(0x000000);
			this.text[n].setEnableBackgroundDrawing(false);
			this.text[n].setMaxStringLength(200);
			if (this.textLines[n] != null) {
				this.text[n].setText(textLines[n]);
			}

			String textTest = this.text[n].getText();
			int formcount = 0;
			// System.out.println(textTest.length());

			for (int m = 0; m < textTest.length(); m++) {
				if (textTest.substring(m, m + 1).contains("\u00a7")) {
					formcount++;
				}
			}
			formcount = formcount * 2;
			this.text[n].setMaxStringLength((int) ((43) * antiScalesText[textScales[n]]) + formcount);
			linespace += (int) 8 * (16.0f / scaledLinesNumber[textScales[n]]);
			lineCounter += 1.0f / (scaledLinesNumber[textScales[n]]);
			if (lineCounter <= 1.0f) {
				numOfLines++;
			}
		}

	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		saveTextLines();
		sendPacket();

	}

	public void sendPacket()
    {
		String[] strings = new String[15];
		for (int n = 0; n<15; n++)
    	{
			strings[n] = this.text[n].getText();
    	}
		BiblioNetworking.INSTANCE.sendToServer(new BiblioSign(strings, textScales, numOfLines, this.slot1Scale, this.slot1Rot, this.slot2Scale, this.slot2Rot, this.xAdjust1, this.yAdjust1, this.xAdjust2, this.yAdjust2, this.signTile.getPos()));
    	// ByteBuf buffer = Unpooled.buffer();
    	// //ByteBufUtils.writeItemStack(buffer, book);
    	// //ByteBufUtils.writeTag(to, tag)
    	// for (int n = 0; n<15; n++)
    	// {
    	// 	ByteBufUtils.writeUTF8String(buffer, this.text[n].getText());
    	// 	buffer.writeInt(this.textScales[n]);
    	// }
    	// buffer.writeInt(this.numOfLines);
    	// buffer.writeInt(this.slot1Scale);
    	// buffer.writeInt(this.slot1Rot);
    	// buffer.writeInt(this.slot2Scale);
    	// buffer.writeInt(this.slot2Rot);
    	// buffer.writeInt(this.xAdjust1);
    	// buffer.writeInt(this.yAdjust1);
    	// buffer.writeInt(this.xAdjust2);
    	// buffer.writeInt(this.yAdjust2);
    	// buffer.writeInt(this.signTile.getPos().getX());
    	// buffer.writeInt(this.signTile.getPos().getY());
    	// buffer.writeInt(this.signTile.getPos().getZ());
    	// BiblioCraft.ch_BiblioSign.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioSign"));
    	
    }

	@Override
	protected void actionPerformed(GuiButton click) {
		// System.out.println(click.id);
		// System.out.println("Adjustment: "+this.xAdjust1);
		switch (click.id) {
			case 0: {
				break;
			}
			case 1: // select slot 1 editing
			{
				if (this.editing == 1) {
					this.editing = 0;
					initGui();
				} else {
					this.editing = 1;
					saveTextLines();
					initGui();
				}
				break;
			}
			case 2: // select slot 2 editing
			{
				if (this.editing == 2) {
					this.editing = 0;
					initGui();
				} else {
					this.editing = 2;
					saveTextLines();
					initGui();
				}
				break;
			}
			case 3: // select text editing
			{
				if (this.editing == 3) {
					this.editing = 0;
					saveTextLines();
					initGui();
				} else {
					this.editing = 3;
					// saveTextLines();
					initGui();
				}
				break;
			}
			case 4: {
				if (editing == 1) {
					if (slot1Scale < 8) {
						this.slot1Scale++;
					}
				} else {
					if (slot2Scale < 8) {
						this.slot2Scale++;
					}
				}
				this.scalePlusCountdown = 10;
				break;
			}
			case 5: {
				if (editing == 1) {
					if (slot1Scale > 0) {
						this.slot1Scale--;
					}
				} else {
					if (slot2Scale > 0) {
						this.slot2Scale--;
					}
				}
				this.scaleNegCountdown = 10;
				break;
			}
			case 6: {
				if (editing == 1) {
					if (this.slot1Rot < 2) {
						this.slot1Rot++;
					}
				} else if (editing == 2) {
					if (this.slot2Rot < 2) {
						this.slot2Rot++;
					}
				} else if (editing == 3) {
					if (this.textScales[this.currentLine] < 5) {
						this.textScales[this.currentLine]++;
					}
					saveTextLines();
					initGui();
				}

				this.rotPlusCountdown = 10;
				break;
			}
			case 7: {
				if (editing == 1) {
					if (this.slot1Rot > 0) {
						this.slot1Rot--;
					}
				} else if (editing == 2) {
					if (this.slot2Rot > 0) {
						this.slot2Rot--;
					}
				} else if (editing == 3) {
					if (this.textScales[this.currentLine] > 0) {
						this.textScales[this.currentLine]--;
					}
					saveTextLines();
					initGui();
				}
				this.rotNegCountdown = 10;
				break;
			}
			case 8: {
				if (editing == 1) {
					this.yAdjust1--;
				} else {
					this.yAdjust2--;
				}
				this.arrowUpCountdown = 10;
				break;
			}
			case 9: {
				if (editing == 1) {
					this.yAdjust1++;
				} else {
					this.yAdjust2++;
				}
				this.arrowDownCountdown = 10;
				break;
			}
			case 10: {
				if (editing == 1) {
					this.xAdjust1++;
				} else {
					this.xAdjust2++;
				}
				this.arrowRightCountdown = 10;
				break;
			}
			case 11: {
				if (editing == 1) {
					this.xAdjust1--;
				} else {
					this.xAdjust2--;
				}
				this.arrowLeftCountdown = 10;
				break;
			}
			case 12: {
				this.text[this.currentLine].setText(addTextMod(this.text[this.currentLine].getText()));
				this.formatCountdown = 10;
				// this.text[this.currentLine].setFocused(true);
				// this.text[this.currentLine].setCursorPosition(this.currentCursorPos+1);
				break;
			}
		}
	}

	private void saveTextLines() {
		for (int n = 0; n < 15; n++) {
			this.textLines[n] = this.text[n].getText();
		}
	}

	private String addTextMod(String line) {
		String partA = "";
		String partB = "";
		if (this.currentCursorPos > 0 && this.currentCursorPos <= line.length()) {
			partA = line.substring(0, this.currentCursorPos);
			partB = line.substring(this.currentCursorPos, line.length());
			return partA + "\u00a7" + partB; // the funky symbol == \u00a7
		} else {
			return "\u00a7" + line;
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int click) {
		try {
			super.mouseClicked(x, y, click);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int h = (3 * (this.height)) / 5;
		if (y < h) {
			// System.out.println("Mouser clicker: "+y+" h "+h);
			for (int n = 0; n < numOfLines; n++) {
				if (this.text[n].mouseClicked((int) (x * antiScalesText[textScales[n]]),
						(int) (y * antiScalesText[textScales[n]]), click)) {
					this.currentLine = n;
					this.currentCursorPos = this.text[n].getCursorPosition();
					this.editing = 3;
					saveTextLines();
					initGui();
					this.text[n].mouseClicked((int) (x * antiScalesText[textScales[n]]),
							(int) (y * antiScalesText[textScales[n]]), click);
				}
			}
		}

	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	protected void keyTyped(char par1, int key) {
		boolean keyedFocus = false;
		if (key == 1) {
			this.mc.player.closeScreen();
		}
		if (key == 15) {
			if (this.editing == 3) {
				this.editing = 1;
				saveTextLines();
			} else {
				this.editing++;
			}
			initGui();
		}

		for (int n = 0; n < numOfLines; n++) {
			if (this.text[n].isFocused()) {
				this.text[n].textboxKeyTyped(par1, key);
				this.currentCursorPos = this.text[n].getCursorPosition();
				keyedFocus = true;
				String textTest = this.text[n].getText();
				int formcount = 0;
				// System.out.println(textTest.length());

				for (int m = 0; m < textTest.length(); m++) {
					if (textTest.substring(m, m + 1).contains("\u00a7")) {
						formcount++;
					}
				}
				formcount = formcount * 2;
				// System.out.println(formcount);
				this.text[n].setMaxStringLength((int) ((43) * antiScalesText[textScales[n]]) + formcount);
			}
		}

		if (key == 56 || key == 184) {
			this.text[this.currentLine].setText(addTextMod(this.text[this.currentLine].getText()));
			this.formatCountdown = 10;
		}

		if (editing == 3) {
			if (key == 200) // up
			{
				for (int n = 1; n < 15; n++) {
					if (this.text[n].isFocused()) {
						this.text[n].setFocused(false);
						this.text[n - 1].setFocused(true);
						this.currentLine = n - 1;
						this.text[n - 1].setCursorPosition(this.currentCursorPos);
						break;
					}
				}
			}
			if (key == 208 || key == 28) // down
			{
				for (int n = 0; n < (this.numOfLines - 1); n++) {
					if (this.text[n].isFocused()) {
						this.text[n].setFocused(false);
						this.text[n + 1].setFocused(true);
						this.currentLine = n + 1;
						this.text[n + 1].setCursorPosition(this.currentCursorPos);
						break;
					}
				}
			}
		}

		if ((editing == 1 || editing == 2) && !keyedFocus) {
			// 200 == up
			// 203 == left
			// 205 == right
			// 208 == down
			if (key == 200 || key == 17) {
				if (editing == 1) {
					this.yAdjust1--;
				} else {
					this.yAdjust2--;
				}
				this.arrowUpCountdown = 10;
			} else if (key == 208 || key == 31) // down
			{
				if (editing == 1) {
					this.yAdjust1++;
				} else {
					this.yAdjust2++;
				}
				this.arrowDownCountdown = 10;
			} else if (key == 203 || key == 30) {
				if (editing == 1) {
					this.xAdjust1--;
				} else {
					this.xAdjust2--;
				}
				this.arrowLeftCountdown = 10;
			} else if (key == 205 || key == 32) {
				if (editing == 1) {
					this.xAdjust1++;
				} else {
					this.xAdjust2++;
				}
				this.arrowRightCountdown = 10;
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int x, int y) {
		// GlStateManager.color(1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(CommonProxy.FANCYSIGNGUI);
		int w = (width - 256) / 2;
		int h = (height - 256) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, 256, 246);
		// System.out.println(x+" "+y + " bg");
		this.mc.getTextureManager().bindTexture(CommonProxy.FANCYSIGNGUIBUTTONS);
		switch (this.editing) {
			case 0: {
				// this.fontRenderer.drawString("blah de blah blah", ((w+210)), ((h+196)),
				// 0x000000, false);
				this.drawTexturedModalRect(w + 197, h + 189, 0, 60, 9, 11); // slot 1 info button
				this.drawTexturedModalRect(w + 211, h + 189, 0, 60, 9, 11); // text editor info button
				this.drawTexturedModalRect(w + 225, h + 189, 0, 60, 9, 11); // slot 2 info button

				GL11.glScaled(0.6, 0.6, 0.6);
				this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip1"),
						(int) ((w + 192) * (1.0 / 0.6)), (int) ((h + 204) * (1.0 / 0.6)), 88, 0x000000);

				GL11.glScaled(1.0 / 0.6, 1.0 / 0.6, 1.0 / 0.6);
				if (x > (w + 197) && x <= (w + 206) && y > (h + 189) && y <= (h + 200)) // slot 1 info guide
				{
					this.mc.getTextureManager().bindTexture(CommonProxy.FANCYSIGNGUIBUTTONS);
					GlStateManager.color(1.0f, 1.0f, 1.0f);
					this.drawTexturedModalRect(x + 2, y - 7, 0, 75, 100, 48);
					GL11.glScaled(0.7, 0.7, 0.7);
					GlStateManager.color(0.0f, 0.0f, 0.0f);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip2"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 125, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}
				if (x > (w + 211) && x <= (w + 220) && y > (h + 189) && y <= (h + 200)) // text slot info guide
				{
					this.mc.getTextureManager().bindTexture(CommonProxy.FANCYSIGNGUIBUTTONS);
					GlStateManager.color(1.0f, 1.0f, 1.0f);
					this.drawTexturedModalRect(x + 2, y - 7, 0, 75, 100, 48);
					GL11.glScaled(0.7, 0.7, 0.7);
					GlStateManager.color(0.0f, 0.0f, 0.0f);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip3"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 125, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}
				// GlStateManager.color(1.0f, 1.0f, 1.0f);
				if (x > (w + 225) && x <= (w + 234) && y > (h + 189) && y <= (h + 200)) // slot 2 info guide
				{
					this.mc.getTextureManager().bindTexture(CommonProxy.FANCYSIGNGUIBUTTONS);
					GlStateManager.color(1.0f, 1.0f, 1.0f);
					this.drawTexturedModalRect(x + 2, y - 7, 0, 75, 100, 48);
					GL11.glScaled(0.7, 0.7, 0.7);
					GlStateManager.color(1.0f, 1.0f, 1.0f);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip4"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 125, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}
				GlStateManager.color(1.0f, 1.0f, 1.0f);
				break;
			}
			case 1: {
				this.drawTexturedModalRect(w + 198, h + 177, 0, 0, 9, 9); // check mark

				this.drawTexturedModalRect(w + 239, h + 193, 0, 60, 9, 11);
				this.drawTexturedModalRect(w + 239, h + 206, 0, 60, 9, 11);
				this.drawTexturedModalRect(w + 239, h + 231, 0, 60, 9, 11);

				this.drawTexturedModalRect(w + 194, h + 192, 12, 0, 12, 12); // top left button
				if (this.scaleNegCountdown > 0) {
					this.drawTexturedModalRect(w + 194, h + 192, 25, 0, 12, 12);
					this.scaleNegCountdown--;
				}
				this.drawTexturedModalRect(w + 197, h + 197, 0, 55, 6, 2); // top left button neg
				if (x >= (w + 194) && x < (w + 206) && y >= (h + 192) && y < (h + 204)) {
					this.drawTexturedModalRect(w + 197, h + 197, 10, 55, 6, 2);
				}

				this.drawTexturedModalRect(w + 226, h + 192, 12, 0, 12, 12); // top right button
				if (this.scalePlusCountdown > 0) {
					this.drawTexturedModalRect(w + 226, h + 192, 25, 0, 12, 12);
					this.scalePlusCountdown--;
				}
				this.drawTexturedModalRect(w + 229, h + 195, 0, 47, 6, 6); // top right button pos
				if (x >= (w + 226) && x < (w + 238) && y >= (h + 192) && y < (h + 204)) {
					this.drawTexturedModalRect(w + 229, h + 195, 10, 47, 6, 6);
				}

				this.drawTexturedModalRect(w + 194, h + 205, 12, 0, 12, 12); // mid left button
				if (this.rotNegCountdown > 0) {
					this.drawTexturedModalRect(w + 194, h + 205, 25, 0, 12, 12);
					this.rotNegCountdown--;
				}
				this.drawTexturedModalRect(w + 197, h + 210, 0, 55, 6, 2); // mid left button neg
				if (x >= (w + 194) && x < (w + 206) && y >= (h + 205) && y < (h + 217)) {
					this.drawTexturedModalRect(w + 197, h + 210, 10, 55, 6, 2);
				}

				this.drawTexturedModalRect(w + 226, h + 205, 12, 0, 12, 12); // mid right button
				if (this.rotPlusCountdown > 0) {
					this.drawTexturedModalRect(w + 226, h + 205, 25, 0, 12, 12);
					this.rotPlusCountdown--;
				}
				this.drawTexturedModalRect(w + 229, h + 208, 0, 47, 6, 6); // mid right button pos
				if (x >= (w + 226) && x < (w + 238) && y >= (h + 205) && y < (h + 217)) {
					this.drawTexturedModalRect(w + 229, h + 208, 10, 47, 6, 6);
				}

				this.drawTexturedModalRect(w + 210, h + 217, 12, 0, 12, 12); // up arrow button
				if (this.arrowUpCountdown > 0) {
					this.drawTexturedModalRect(w + 210, h + 217, 25, 0, 12, 12);
					this.arrowUpCountdown--;
				}
				this.drawTexturedModalRect(w + 212, h + 221, 0, 15, 8, 4);
				if (x >= (w + 210) && x < (w + 222) && y >= (h + 217) && y < (h + 229)) {
					this.drawTexturedModalRect(w + 212, h + 221, 10, 15, 8, 4);
				}

				this.drawTexturedModalRect(w + 210, h + 230, 12, 0, 12, 12); // down arrow button
				if (this.arrowDownCountdown > 0) {
					this.drawTexturedModalRect(w + 210, h + 230, 25, 0, 12, 12);
					this.arrowDownCountdown--;
				}
				this.drawTexturedModalRect(w + 212, h + 234, 0, 21, 8, 4);
				if (x >= (w + 210) && x < (w + 222) && y >= (h + 230) && y < (h + 242)) {
					this.drawTexturedModalRect(w + 212, h + 234, 10, 21, 8, 4);
				}

				this.drawTexturedModalRect(w + 223, h + 230, 12, 0, 12, 12); // right arrow button
				if (this.arrowRightCountdown > 0) {
					this.drawTexturedModalRect(w + 223, h + 230, 25, 0, 12, 12);
					this.arrowRightCountdown--;
				}
				this.drawTexturedModalRect(w + 227, h + 232, 0, 27, 4, 8);
				if (x >= (w + 223) && x < (w + 235) && y >= (h + 230) && y < (h + 242)) {
					this.drawTexturedModalRect(w + 227, h + 232, 10, 27, 4, 8);
				}

				this.drawTexturedModalRect(w + 197, h + 230, 12, 0, 12, 12); // left arrow button
				if (this.arrowLeftCountdown > 0) {
					this.drawTexturedModalRect(w + 197, h + 230, 25, 0, 12, 12);
					this.arrowLeftCountdown--;
				}
				this.drawTexturedModalRect(w + 200, h + 232, 0, 37, 4, 8);
				if (x >= (w + 197) && x < (w + 209) && y >= (h + 230) && y < (h + 242)) {
					this.drawTexturedModalRect(w + 200, h + 232, 10, 37, 4, 8);
				}

				if (x > (w + 239) && x <= (w + 248) && y > (h + 193) && y <= (h + 204)) {
					this.drawTexturedModalRect(x + 2, y - 7, 0, 178, 91, 17);
					GL11.glScaled(0.7, 0.7, 0.7);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip5"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 120, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}
				if (x > (w + 239) && x <= (w + 248) && y > (h + 206) && y <= (h + 217)) {
					this.drawTexturedModalRect(x + 2, y - 7, 0, 75, 100, 48);
					GL11.glScaled(0.7, 0.7, 0.7);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip6"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 130, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}
				if (x > (w + 239) && x <= (w + 248) && y > (h + 231) && y <= (h + 242)) {
					this.drawTexturedModalRect(x + 2, y - 28, 0, 126, 100, 48);
					GL11.glScaled(0.7, 0.7, 0.7);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip7"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 25) * (1.0 / 0.7)), 130, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}

				GL11.glPushMatrix();
				GL11.glScaled(0.55, 0.55, 0.55);
				this.fontRenderer.drawString(rots[this.slot1Rot],
						(int) ((w + 216 - (3 * (rots[this.slot1Rot].length() / 2))) * (1.0 / 0.55)),
						(int) ((h + 209) * (1.0 / 0.55)), 0x000000, false);
				GL11.glPopMatrix();

				this.fontRenderer.drawString(scaleItemsStrings[this.slot1Scale], ((w + 210)), ((h + 196)), 0x000000,
						false);
				break;
			}
			case 2: {
				this.drawTexturedModalRect(w + 226, h + 177, 0, 0, 9, 9); // check mark

				this.drawTexturedModalRect(w + 239, h + 193, 0, 60, 9, 11);
				this.drawTexturedModalRect(w + 239, h + 206, 0, 60, 9, 11);
				this.drawTexturedModalRect(w + 239, h + 231, 0, 60, 9, 11);

				this.drawTexturedModalRect(w + 194, h + 192, 12, 0, 12, 12); // top left button
				if (this.scaleNegCountdown > 0) {
					this.drawTexturedModalRect(w + 194, h + 192, 25, 0, 12, 12);
					this.scaleNegCountdown--;
				}
				this.drawTexturedModalRect(w + 197, h + 197, 0, 55, 6, 2); // top left button neg
				if (x >= (w + 194) && x < (w + 206) && y >= (h + 192) && y < (h + 204)) {
					this.drawTexturedModalRect(w + 197, h + 197, 10, 55, 6, 2);
				}

				this.drawTexturedModalRect(w + 226, h + 192, 12, 0, 12, 12); // top right button
				if (this.scalePlusCountdown > 0) {
					this.drawTexturedModalRect(w + 226, h + 192, 25, 0, 12, 12);
					this.scalePlusCountdown--;
				}
				this.drawTexturedModalRect(w + 229, h + 195, 0, 47, 6, 6); // top right button pos
				if (x >= (w + 226) && x < (w + 238) && y >= (h + 192) && y < (h + 204)) {
					this.drawTexturedModalRect(w + 229, h + 195, 10, 47, 6, 6);
				}

				this.drawTexturedModalRect(w + 194, h + 205, 12, 0, 12, 12); // mid left button
				if (this.rotNegCountdown > 0) {
					this.drawTexturedModalRect(w + 194, h + 205, 25, 0, 12, 12);
					this.rotNegCountdown--;
				}
				this.drawTexturedModalRect(w + 197, h + 210, 0, 55, 6, 2); // mid left button neg
				if (x >= (w + 194) && x < (w + 206) && y >= (h + 205) && y < (h + 217)) {
					this.drawTexturedModalRect(w + 197, h + 210, 10, 55, 6, 2);
				}

				this.drawTexturedModalRect(w + 226, h + 205, 12, 0, 12, 12); // mid right button
				if (this.rotPlusCountdown > 0) {
					this.drawTexturedModalRect(w + 226, h + 205, 25, 0, 12, 12);
					this.rotPlusCountdown--;
				}
				this.drawTexturedModalRect(w + 229, h + 208, 0, 47, 6, 6); // mid right button pos
				if (x >= (w + 226) && x < (w + 238) && y >= (h + 205) && y < (h + 217)) {
					this.drawTexturedModalRect(w + 229, h + 208, 10, 47, 6, 6);
				}

				this.drawTexturedModalRect(w + 210, h + 217, 12, 0, 12, 12); // up arrow button
				if (this.arrowUpCountdown > 0) {
					this.drawTexturedModalRect(w + 210, h + 217, 25, 0, 12, 12);
					this.arrowUpCountdown--;
				}
				this.drawTexturedModalRect(w + 212, h + 221, 0, 15, 8, 4);
				if (x >= (w + 210) && x < (w + 222) && y >= (h + 217) && y < (h + 229)) {
					this.drawTexturedModalRect(w + 212, h + 221, 10, 15, 8, 4);
				}

				this.drawTexturedModalRect(w + 210, h + 230, 12, 0, 12, 12); // down arrow button
				if (this.arrowDownCountdown > 0) {
					this.drawTexturedModalRect(w + 210, h + 230, 25, 0, 12, 12);
					this.arrowDownCountdown--;
				}
				this.drawTexturedModalRect(w + 212, h + 234, 0, 21, 8, 4);
				if (x >= (w + 210) && x < (w + 222) && y >= (h + 230) && y < (h + 242)) {
					this.drawTexturedModalRect(w + 212, h + 234, 10, 21, 8, 4);
				}

				this.drawTexturedModalRect(w + 223, h + 230, 12, 0, 12, 12); // right arrow button
				if (this.arrowRightCountdown > 0) {
					this.drawTexturedModalRect(w + 223, h + 230, 25, 0, 12, 12);
					this.arrowRightCountdown--;
				}
				this.drawTexturedModalRect(w + 227, h + 232, 0, 27, 4, 8);
				if (x >= (w + 223) && x < (w + 235) && y >= (h + 230) && y < (h + 242)) {
					this.drawTexturedModalRect(w + 227, h + 232, 10, 27, 4, 8);
				}

				this.drawTexturedModalRect(w + 197, h + 230, 12, 0, 12, 12); // left arrow button
				if (this.arrowLeftCountdown > 0) {
					this.drawTexturedModalRect(w + 197, h + 230, 25, 0, 12, 12);
					this.arrowLeftCountdown--;
				}
				this.drawTexturedModalRect(w + 200, h + 232, 0, 37, 4, 8);
				if (x >= (w + 197) && x < (w + 209) && y >= (h + 230) && y < (h + 242)) {
					this.drawTexturedModalRect(w + 200, h + 232, 10, 37, 4, 8);
				}

				if (x > (w + 239) && x <= (w + 248) && y > (h + 193) && y <= (h + 204)) {
					this.drawTexturedModalRect(x + 2, y - 7, 0, 178, 91, 17);
					GL11.glScaled(0.7, 0.7, 0.7);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip5"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 120, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}
				if (x > (w + 239) && x <= (w + 248) && y > (h + 206) && y <= (h + 217)) {
					this.drawTexturedModalRect(x + 2, y - 7, 0, 75, 100, 49);
					GL11.glScaled(0.7, 0.7, 0.7);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip6"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 130, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}
				if (x > (w + 239) && x <= (w + 248) && y > (h + 231) && y <= (h + 242)) {
					this.drawTexturedModalRect(x + 2, y - 28, 0, 126, 100, 49);
					GL11.glScaled(0.7, 0.7, 0.7);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip7"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 25) * (1.0 / 0.7)), 130, 0x000000);
					GL11.glScaled(1.0 / 0.7, 1.0 / 0.7, 1.0 / 0.7);
				}

				GL11.glPushMatrix();
				GL11.glScaled(0.55, 0.55, 1.0);
				this.fontRenderer.drawString(rots[this.slot2Rot],
						(int) ((w + 216 - (3 * (rots[this.slot2Rot].length() / 2))) * (1.0 / 0.55)),
						(int) ((h + 209) * (1.0 / 0.55)), 0x000000, false);
				GL11.glPopMatrix();

				this.fontRenderer.drawString(scaleItemsStrings[this.slot2Scale], ((w + 210)), ((h + 196)), 0x000000,
						false);

				break;
			}
			case 3: {
				this.drawTexturedModalRect(w + 212, h + 179, 0, 0, 9, 9); // check mark

				this.drawTexturedModalRect(w + 237, h + 219, 0, 60, 9, 11);
				this.drawTexturedModalRect(w + 184, h + 219, 0, 60, 9, 11);
				// this.drawTexturedModalRect(w+239, h+231, 0, 60, 9, 11);

				this.drawTexturedModalRect(w + 194, h + 205, 12, 0, 12, 12); // mid left button
				if (this.rotNegCountdown > 0) {
					this.drawTexturedModalRect(w + 194, h + 205, 25, 0, 12, 12);
					this.rotNegCountdown--;
				}
				this.drawTexturedModalRect(w + 197, h + 210, 0, 55, 6, 2); // mid left button neg
				if (x >= (w + 194) && x < (w + 206) && y >= (h + 205) && y < (h + 217)) {
					this.drawTexturedModalRect(w + 197, h + 210, 10, 55, 6, 2);
				}

				this.drawTexturedModalRect(w + 226, h + 205, 12, 0, 12, 12); // mid right button
				if (this.rotPlusCountdown > 0) {
					this.drawTexturedModalRect(w + 226, h + 205, 25, 0, 12, 12);
					this.rotPlusCountdown--;
				}
				this.drawTexturedModalRect(w + 229, h + 208, 0, 47, 6, 6); // mid right button pos
				if (x >= (w + 226) && x < (w + 238) && y >= (h + 205) && y < (h + 217)) {
					this.drawTexturedModalRect(w + 229, h + 208, 10, 47, 6, 6);
				}

				this.drawTexturedModalRect(w + 194, h + 218, 12, 0, 12, 12); // format button
				if (this.formatCountdown > 0) {
					this.drawTexturedModalRect(w + 194, h + 218, 25, 0, 12, 12);
					this.formatCountdown--;
				}

				if (x > (w + 237) && x <= (w + 246) && y > (h + 219) && y <= (h + 230)) {
					this.drawTexturedModalRect(x + 3, y - 148, 100, 0, 84, 175);
					GL11.glPushMatrix();
					GL11.glScaled(0.7, 0.7, 0.7);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip8"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 145) * (1.0 / 0.7)), 124, 0x000000);
					this.fontRenderer.drawSplitString("k = " + TextFormatting.OBFUSCATED + "Obfuscated",
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 114) * (1.0 / 0.7)), 120, 0x000000);
					this.fontRenderer.drawSplitString(
							"l = " + TextFormatting.BOLD + I18n.translateToLocal("gui.sign.bold"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 108) * (1.0 / 0.7)), 120, 0x000000);
					this.fontRenderer.drawSplitString(
							"m = " + TextFormatting.STRIKETHROUGH + I18n.translateToLocal("gui.sign.strikethrough"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 102) * (1.0 / 0.7)), 120, 0x000000);
					this.fontRenderer.drawSplitString(
							"n = " + TextFormatting.UNDERLINE + I18n.translateToLocal("gui.sign.underline"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 96) * (1.0 / 0.7)), 120, 0x000000);
					this.fontRenderer.drawSplitString(
							"o = " + TextFormatting.ITALIC + I18n.translateToLocal("gui.sign.italic"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 90) * (1.0 / 0.7)), 120, 0x000000);
					this.fontRenderer.drawSplitString("r = " + I18n.translateToLocal("book.reset"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 84) * (1.0 / 0.7)), 120, 0x000000);

					for (int n = 0; n < 16; n++) {
						this.fontRenderer.drawSplitString(this.tipcolorsymbols[n] + " = " + this.tiptextcolors[n],
								(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 72 + (n * 6)) * (1.0 / 0.7)), 120,
								0x000000);
					}
					GL11.glPopMatrix();
				}

				GL11.glPushMatrix();
				GL11.glScaled(1.25, 1.25, 1.25);
				this.fontRenderer.drawString("\u00a7", (int) ((w + 198) * (1.0 / 1.25)),
						(int) ((h + 219) * (1.0 / 1.25)), 0x000000, false);
				// GL11.glScaled(1.0/1.2, 1.0/1.2, 1.0/1.2);
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				GL11.glScaled(0.8, 0.8, 0.8);
				this.fontRenderer.drawString(I18n.translateToLocal("gui.sign.scale"), (int) ((w + 195) * (1.0 / 0.8)),
						(int) ((h + 196) * (1.0 / 0.8)), 0x000000, false);
				this.fontRenderer.drawString(I18n.translateToLocal("gui.sign.format"), (int) ((w + 208) * (1.0 / 0.8)),
						(int) ((h + 221) * (1.0 / 0.8)), 0x000000, false);
				this.fontRenderer.drawString(
						I18n.translateToLocal("gui.sign.linenum") + " = " + TextFormatting.GREEN
								+ (this.currentLine + 1),
						(int) ((w + 195) * (1.0 / 0.8)), (int) ((h + 233) * (1.0 / 0.8)), 0x000000, false);
				GL11.glPopMatrix();
				this.fontRenderer.drawString(scaleTextStrings[this.textScales[this.currentLine]],
						((w + 212) - (3 * (scaleTextStrings[this.textScales[this.currentLine]].length() / 2))),
						((h + 207)), 0x000000, false);

				if (x > (w + 184) && x <= (w + 193) && y > (h + 219) && y <= (h + 230)) {
					GL11.glPushMatrix();
					GlStateManager.color(1.0f, 1.0f, 1.0f);
					this.mc.getTextureManager().bindTexture(CommonProxy.FANCYSIGNGUIBUTTONS);
					this.drawTexturedModalRect(x + 2, y - 7, 0, 75, 100, 48);
					GL11.glScaled(0.7, 0.7, 1.0);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.sign.tooltip9"),
							(int) ((x + 10) * (1.0 / 0.7)), (int) ((y - 4) * (1.0 / 0.7)), 120, 0x000000);
					GL11.glPopMatrix();
				}

				break;
			}
		}

		// text boxes
		GL11.glPushMatrix();
		for (int n = 0; n < this.numOfLines; n++) {

			GL11.glScalef(scalesText[this.textScales[n]], scalesText[this.textScales[n]],
					scalesText[this.textScales[n]]);
			this.text[n].drawTextBox();
			GL11.glScalef(antiScalesText[this.textScales[n]], antiScalesText[this.textScales[n]],
					antiScalesText[this.textScales[n]]);

		}
		GL11.glPopMatrix();

		// RenderHelper.enableGUIStandardItemLighting();
		if (signTile.getStackInSlot(0) != ItemStack.EMPTY) {
			GL11.glPushMatrix();
			// GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_LIGHTING);
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			GL11.glTranslated(w + 128 + xAdjust1 - (6 * scales[this.slot1Scale]),
					h + 74 + yAdjust1 - (6 * scales[this.slot1Scale]), 4.0); // 160, 138
			GL11.glScalef(scales[this.slot1Scale], scales[this.slot1Scale], 1.0f);
			EntityItem item1 = new EntityItem(mc.world, 0, 0, 0, signTile.getStackInSlot(0));
			item1.hoverStart = 0f;

			GL11.glTranslated(8, 12, 2);
			GL11.glScaled(-32.0, -32.0, -1.0);
			// GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
			if (Config.isBlock(signTile.getStackInSlot(0))) {
				GL11.glTranslated(0.002, 0.084, 0);
				GL11.glScaled(1.23, 1.23, 1.23);
				if (this.slot1Rot == 0) {
					GL11.glRotatef(-25.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-225.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}
				if (this.slot1Rot == 1) {
					GL11.glRotatef(90.0F, 0.0f, 1.0f, 0.0f);
				}
				if (this.slot1Rot == 2) {
					GL11.glRotatef(-25.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(225.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}
			} else {
				if (this.slot1Rot == 2) {
					GL11.glRotatef(180, 0.0f, 1.0f, 0.0f);
				}
			}
			myItemRenderer.renderItem(signTile.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();

		}
		if (signTile.getStackInSlot(1) != ItemStack.EMPTY) {

			GL11.glPushMatrix();
			// GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_LIGHTING);
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			GL11.glTranslated(w + 128 + xAdjust2 - (6 * scales[this.slot2Scale]),
					h + 74 + yAdjust2 - (6 * scales[this.slot2Scale]), 1.0); // 160, 138
			GL11.glScalef(scales[this.slot2Scale], scales[this.slot2Scale], 1.0f);
			EntityItem item2 = new EntityItem(mc.world, 0, 0, 0, signTile.getStackInSlot(1));
			item2.hoverStart = 0f;

			GL11.glTranslated(8, 12, 5);
			GL11.glScaled(-32.0, -32.0, -1.0);
			// GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
			if (Config.isBlock(signTile.getStackInSlot(1))) {
				GL11.glTranslated(0.002, 0.084, 0);
				GL11.glScaled(1.23, 1.23, 1.23);
				if (this.slot2Rot == 0) {
					GL11.glRotatef(-25.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-225.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}
				if (this.slot2Rot == 1) {
					GL11.glRotatef(90.0F, 0.0f, 1.0f, 0.0f);
				}
				if (this.slot2Rot == 2) {
					GL11.glRotatef(-25.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(225.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}
			} else {
				if (this.slot2Rot == 2) {
					GL11.glRotatef(180, 0.0f, 1.0f, 0.0f);
				}
			}
			myItemRenderer.renderItem(signTile.getStackInSlot(1), ItemCameraTransforms.TransformType.FIXED);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();

		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) // x and y are the location of the mouse.
	{
		super.drawGuiContainerForegroundLayer(x, y);
		int w = (width - 256) / 2;
		int h = (height - 246) / 2;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
