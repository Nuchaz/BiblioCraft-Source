package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.item.ItemStack;

public class TileEntityLabelRenderer extends TileEntityBiblioRenderer
{

	@Override
	public void render(BiblioTileEntity tile, double x, double y, double z, float tick) 
	{
		ItemStack stackLeft = tile.getStackInSlot(1);
		ItemStack stackMiddle = tile.getStackInSlot(0);
		ItemStack stackRight = tile.getStackInSlot(2);
		
		if (stackLeft == ItemStack.EMPTY && stackRight == ItemStack.EMPTY)
		{
			renderSlotItem(stackMiddle, 0.5, 0.2, 0.04, 0.4f); 
		}
		else
		{
			renderSlotItem(stackLeft, 0.36, 0.24, 0.04, 0.22f); 
			renderSlotItem(stackMiddle, 0.5, 0.15, 0.05, 0.22f);
			renderSlotItem(stackRight, 0.64, 0.24, 0.04, 0.22f);
		}
	}

}
