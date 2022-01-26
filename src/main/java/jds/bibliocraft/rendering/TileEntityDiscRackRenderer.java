package jds.bibliocraft.rendering;

import jds.bibliocraft.Config;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityDiscRack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityDiscRackRenderer extends TileEntityBiblioRenderer
{
	//EnumVertPosition vert = EnumVertPosition.WALL;
	boolean rotated = false;
	boolean isBlock = false;
	
	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick)
	{
		TileEntityDiscRack rack = (TileEntityDiscRack)tile;
		rotated = rack.getWallRotation();
		for (int i = 0; i < tile.getSizeInventory(); i++)
		{
			ItemStack stack = tile.getStackInSlot(i);
			isBlock = Config.isBlock(stack);
			double adjustx = 0;
			double adjusty = 0;
			switch (getVertPosition())
			{
				case FLOOR: 
				{ 
					if (isBlock)
					{
						adjustx = -0.2;
						adjusty = 0.4;
					}
					if (getAngle() == EnumFacing.NORTH || getAngle() == EnumFacing.SOUTH)
					{
						renderSlotItem(stack, 0.5, 0.0 + adjusty, 0.62 - (0.105 * i) + adjustx, 1.0f); 
					}
					else
					{
						renderSlotItem(stack, 0.5, 0.0 + adjusty, -0.23 + (0.105 * i) + adjustx, 1.0f); 
					}
					break; 
				}
				case WALL: 
				{ 
					if (rotated)
					{
						renderSlotItem(stack, 0.09 + + (0.105 * i), 0.42, 0.35, 1.0f); 
					}
					else
					{
						renderSlotItem(stack, 0.5, 0.03 + (0.105 * i), 0.35, 1.0f); 
					}
					break; 
				}
				case CEILING: 
				{ 
					if (isBlock)
					{
						adjustx = -0.23;
						adjusty = 0.02;
					}
					if (getAngle() == EnumFacing.NORTH || getAngle() == EnumFacing.SOUTH)
					{
						renderSlotItem(stack, 0.50, 0.57 + adjusty, 0.09 + (0.105 * i) + adjustx, 1.0f); 
					}
					else
					{
						renderSlotItem(stack, 0.50, 0.57 + adjusty, 0.92 - (0.105 * i) + adjustx, 1.0f); 
					}
					break; 
					
				}
			}
		}
	}

	@Override
	public void additionalGLStuffForItemStack()
	{
		// need to get vert position
		// need to get rotation
		// test if block
		if (getVertPosition() == EnumVertPosition.CEILING)
		{
			if (isBlock)
			{
				GlStateManager.rotate(90, 1.0f, 0.0f, 0.0f);
			}
		}
		else
		{
			if (!isBlock)
			{
				GlStateManager.rotate(90, 1.0f, 0.0f, 0.0f);
			}
			else
			{
				GlStateManager.translate(0.0, 0.21, 0.0);
			}
			if (rotated && getVertPosition() == EnumVertPosition.WALL)
			{
				GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
				if (isBlock)
				{
					GlStateManager.rotate(90, 1.0f, 0.0f, 0.0f);
					GlStateManager.translate(0.0, 0.21, 0.17);
				}
			}
		}
		if (getVertPosition() == EnumVertPosition.FLOOR)
		{
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.0, -0.3, 0.3); 
		}
		
	}
}
