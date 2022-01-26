package jds.bibliocraft.events;

import jds.bibliocraft.Config;
import jds.bibliocraft.items.ItemClipboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderClipboardText 
{
	// thanks to Vazkii and https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/client/core/handler/RenderLexicon.java for figuring this out
	
	@SubscribeEvent
	public void renderItem(RenderSpecificHandEvent event)
	{
		// this only ever runs in first person mode
		Minecraft mc = Minecraft.getMinecraft();
		boolean is1stperson = mc.gameSettings.thirdPersonView == 0;
		ItemStack stack = mc.player.getHeldItem(event.getHand());
		if (Config.enableClipboard && stack.getItem() == ItemClipboard.instance && is1stperson)
		{
			try
			{
				render(mc, event, stack);
			}
			catch (Throwable throwable) 
			{
				System.out.println("Failed to render text on clipboard");
			}
		}
	}
	
	private void render(Minecraft mc, RenderSpecificHandEvent event, ItemStack stack) throws Throwable
	{
		boolean isRightHand = event.getHand() == EnumHand.MAIN_HAND;
		NBTTagCompound cliptags = stack.getTagCompound();
    	if (cliptags != null)
    	{
    		int currentPage = cliptags.getInteger("currentPage");
    		String pagenum = "page"+currentPage;
    		NBTTagCompound pagetag = cliptags.getCompoundTag(pagenum);
    		if (pagetag != null)
    		{
    			NBTTagCompound tasks = pagetag.getCompoundTag("tasks");
    			if (tasks != null && event.getEquipProgress() == 0.0 && event.getSwingProgress() == 0.0)
    			{
    				renderText(mc, pagetag.getString("title"), isRightHand, 0);
    				renderText(mc, tasks.getString("task1"), isRightHand, 1);
    				renderText(mc, tasks.getString("task2"), isRightHand, 2);
    				renderText(mc, tasks.getString("task3"), isRightHand, 3);
    				renderText(mc, tasks.getString("task4"), isRightHand, 4);
    				renderText(mc, tasks.getString("task5"), isRightHand, 5);
    				renderText(mc, tasks.getString("task6"), isRightHand, 6);
    				renderText(mc, tasks.getString("task7"), isRightHand, 7);
    				renderText(mc, tasks.getString("task8"), isRightHand, 8);
    				renderText(mc, tasks.getString("task9"), isRightHand, 9);
    			}
    		}
    	}
	}
	
	private void renderText(Minecraft mc, String text, boolean isMainHand, int verticlepos)
	{
		GlStateManager.pushMatrix();
		double handOffsett = 0;
		if (!isMainHand)
		{
			handOffsett = -1.062;
		}
		double titleOffset = 0;
		if (verticlepos == 0)
		{
			titleOffset = 0.03;
		}
		GlStateManager.translate(0.397 - titleOffset + handOffsett, -0.105 - (0.0326 * verticlepos), -0.64);
		GlStateManager.rotate(180f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(180f, 0.0f, 0.0f, 1.0f);
		GlStateManager.scale(0.00225, 0.00225, 0.00225);
		mc.fontRenderer.drawString(text, 0, 0, 0x000000, false); 
		GlStateManager.popMatrix();
	}
}
