package jds.bibliocraft.blocks;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraft.entity.EntityLivingBase;

public class BlockBookcaseCreative extends BlockBookcase
{
	public static final String name = "BookcaseCreative";
	public static final BlockBookcaseCreative instance = new BlockBookcaseCreative();
	
	public BlockBookcaseCreative()
	{
		super(name);
		//setRegistryName(name);
		//setUnlocalizedName(BlockBookcase.name);
		//setRegistryName(name);
		//setRegistryName("bibliocraft:" + name);
	}
	
	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player)
	{
		if (biblioTile instanceof TileEntityBookcase)
		{
			TileEntityBookcase bookcase = (TileEntityBookcase)biblioTile;
			bookcase.addRandomBooksToShelf();
		}
	}
    
}
