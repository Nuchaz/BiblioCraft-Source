package jds.bibliocraft.containers.provider;

import jds.bibliocraft.containers.ContainerBookcase;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ContainerBookcaseProvider implements INamedContainerProvider
{
	TileEntityBookcase tile;
	
	public ContainerBookcaseProvider(TileEntityBookcase te)
	{
		this.tile = te;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) 
	{
		return new ContainerBookcase(id, inv, tile);
	}

	@Override
	public ITextComponent getDisplayName() 
	{
		return new StringTextComponent("bookcasecontainer");
	}

}
