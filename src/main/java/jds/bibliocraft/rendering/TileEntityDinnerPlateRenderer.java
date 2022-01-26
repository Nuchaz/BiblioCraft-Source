package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class TileEntityDinnerPlateRenderer extends TileEntityBiblioRenderer
{

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick)
	{
		ItemStack centerStack = tile.getStackInSlot(0);
		ItemStack leftStack = tile.getStackInSlot(1);
		ItemStack rightStack = tile.getStackInSlot(2);
		
		if (leftStack == ItemStack.EMPTY && rightStack == ItemStack.EMPTY && centerStack != ItemStack.EMPTY)
		{
			int stackSize = centerStack.getCount();
			int testValue = (int)(stackSize / 20.0) + 1;
			//if (tick % 10 == 0)
			//System.out.println("stackSize = " + stackSize + "       testValue = " + testValue);
			if (stackSize >= 1)
			{
				renderSlotItem(centerStack, 0.5, 0.0, 0.5, 0.7f);
			}
			if (stackSize >= 2)
			{
				for (int i = 0; i < testValue; i++)
				{
					renderSlotItem(centerStack, 0.5, 0.05 + 0.05 * i, 0.5, 0.7f);
				}
			}
		}
		else
		{
			if (centerStack != ItemStack.EMPTY)
			{
				int stackSize = centerStack.getCount();
				int testValue = (int)(stackSize / 20.0) + 1;
				
				if (stackSize >= 1)
				{
					renderSlotItem(centerStack, 0.5, 0.02, 0.68, 0.5f);
				}
				if (stackSize >= 2)
				{
					for (int i = 0; i < testValue; i++)
					{
						renderSlotItem(centerStack, 0.5, 0.055 + 0.035 * i, 0.68, 0.5f);
					}
				}
			}
			if (leftStack != ItemStack.EMPTY)
			{
				int stackSize = leftStack.getCount();
				int testValue = (int)(stackSize / 20.0) + 1;
				
				if (stackSize >= 1)
				{
					renderSlotItem(leftStack, 0.35, 0.018, 0.4, 0.4f);
				}
				if (stackSize >= 2)
				{
					for (int i = 0; i < testValue; i++)
					{
						renderSlotItem(leftStack, 0.35, 0.048 + 0.03 * i, 0.4, 0.4f);
					}
				}
			}
			if (rightStack != ItemStack.EMPTY)
			{
				int stackSize = rightStack.getCount();
				int testValue = (int)(stackSize / 20.0) + 1;
				
				
				if (stackSize >= 1)
				{
					renderSlotItem(rightStack, 0.62, 0.016, 0.4, 0.4f);
				}
				if (stackSize >= 2)
				{
					for (int i = 0; i < testValue; i++)
					{
						renderSlotItem(rightStack, 0.62, 0.046 + 0.03 * i, 0.4, 0.4f);
					}
				}
			}
		}
	}

	@Override
	public void additionalGLStuffForItemStack()
	{
		GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
	}
}
