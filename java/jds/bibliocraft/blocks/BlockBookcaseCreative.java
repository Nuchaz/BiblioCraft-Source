package jds.bibliocraft.blocks;

import jds.bibliocraft.helpers.EnumWoodsType;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraft.entity.LivingEntity;

public class BlockBookcaseCreative extends BlockBookcase
{
	public static final String name = "bookcasecreative";
	//public static final BlockBookcaseCreative instance = new BlockBookcaseCreative();
	
	public BlockBookcaseCreative(EnumWoodsType wood)
	{
		super(name, wood);
		//setRegistryName(name);
		//setUnlocalizedName(BlockBookcase.name);
		//setRegistryName(name);
		//setRegistryName("bibliocraft:" + name);
	}
	
	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, LivingEntity player)
	{
		if (biblioTile instanceof TileEntityBookcase)
		{
			TileEntityBookcase bookcase = (TileEntityBookcase)biblioTile;
			bookcase.addRandomBooksToShelf();
		}
	}
    
}
