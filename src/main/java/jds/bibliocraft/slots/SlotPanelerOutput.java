package jds.bibliocraft.slots;

import jds.bibliocraft.containers.ContainerFurniturePaneler;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPanelerOutput extends Slot
{
	private ContainerFurniturePaneler container;
	private TileEntityFurniturePaneler tile;

	public SlotPanelerOutput(ContainerFurniturePaneler panelerContainer, TileEntityFurniturePaneler iInventory, int x, int y, int z)
	{
		super(iInventory, x, y, z);
		this.container = panelerContainer;
		this.tile = iInventory;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		//this.container.onCraftMatrixChanged(this.tile);
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 64;
    }
	
	@Override
    public ItemStack onTake(EntityPlayer playerIn, ItemStack stack)
    {
		this.tile.executeRecipe();
		this.container.onCraftMatrixChanged(this.tile);
        this.onSlotChanged();
        return stack;
    }
}
