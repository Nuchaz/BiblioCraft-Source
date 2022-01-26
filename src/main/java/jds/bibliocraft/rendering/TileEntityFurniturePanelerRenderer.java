package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFurniturePanelerRenderer extends TileEntitySpecialRenderer
{
	private TileEntityFurniturePaneler tile;
	private ItemStack inputStack;
	private ItemStack outputStack;
	private int degreeAngle;
	private RenderItem itemRenderer;

	@Override
	public void render(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float what) 
	{
	    if (this.itemRenderer == null)
	    {
	    	this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
	    }
	    
		this.tile = (TileEntityFurniturePaneler)tileEntity;
		if (tile != null)
		{
			this.inputStack = this.tile.getStackInSlot(1);
			this.outputStack = this.tile.getStackInSlot(2);
			
			switch (tile.getAngle())
			{
				case SOUTH:{degreeAngle = 270; break;}
				case WEST:{degreeAngle = 180; break;}
				case NORTH:{degreeAngle = 90; break;}
				case EAST:{degreeAngle = 0; break;}
				default:break;
			}
			
			switch (tile.getAngle())
			{
				case SOUTH:{
					renderSlotItem(inputStack, x+0.24, y+0.77, z+0.29);
					renderSlotItem(outputStack, x+0.24, y+0.77, z+0.72);
					break;}
				case WEST:{
					renderSlotItem(inputStack, x+0.71, y+0.77 , z+0.24);
					renderSlotItem(outputStack, x+0.28, y+0.77, z+0.24);
					break;}
				case NORTH:{
					renderSlotItem(inputStack, x+0.76, y+0.77, z+0.72);
					renderSlotItem(outputStack, x+0.76, y+0.77, z+0.29);
					break;}
				case EAST:{
					renderSlotItem(inputStack, x+0.29, y+0.77, z+0.76);
					renderSlotItem(outputStack, x+0.72, y+0.77, z+0.76);
					break;}
				default: break;
			}
		}
	}

	private void renderSlotItem(ItemStack stack, double xAdjust, double yAdjust, double zAdjust)
	{
		if (stack != ItemStack.EMPTY)
		{
			boolean fancyGraphics = Minecraft.isFancyGraphicsEnabled();
			GlStateManager.pushMatrix();
			GlStateManager.translate(xAdjust, yAdjust - 0.03, zAdjust);
			GlStateManager.rotate(degreeAngle + 180.0f, 0.0F, 1.0F, 0.0F);
			float scale = 0.45f;
			GlStateManager.scale(scale, scale, scale);
			this.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);	
			/*
			if (!fancyGraphics && !Config.isBlock(stack))
			{
				GL11.glRotatef(180, 0.0f, 1.0f, 0.0f);
				itemRenderer.doRender(slotEntity, 0, 0, 0, 0, 0);
			}
			
			itemRenderer.renderInFrame = false;
			*/
			GlStateManager.popMatrix();
		}
	}
}
