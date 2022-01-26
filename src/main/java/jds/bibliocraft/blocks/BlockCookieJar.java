package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityCookieJar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockCookieJar extends BiblioSimpleBlock
{
	public static final BlockCookieJar instance = new BlockCookieJar();
	public static final String name = "CookieJar";
	
	public BlockCookieJar()
	{
		super(Material.GLASS, SoundType.METAL, name);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return this.getBlockBounds(0.18F, 0.0F, 0.18F, 0.82F, 0.75F, 0.82F);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote && tile != null && tile instanceof TileEntityCookieJar)
		{
			TileEntityCookieJar cookiejar = (TileEntityCookieJar)world.getTileEntity(pos);
			if (cookiejar != null)
			{
				cookiejar.setIsOpen(true);
				//world.scheduleBlockUpdate(pos, this, 0, 0);
				
				//world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
			}
			player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCookieJar();
	}
	
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
	@Override
	public List<String> getModelParts(BiblioTileEntity tile)
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("jar");
		modelParts.add("lid");
		int numberOfCookies = 0;
		for (int i = 0; i < tile.getSizeInventory(); i++)
		{
			if (tile.getStackInSlot(i) != ItemStack.EMPTY)
			{
				numberOfCookies++;
			}
		}
		if (numberOfCookies >= 1) { modelParts.add("cookie001"); }
		if (numberOfCookies >= 2) { modelParts.add("cookie002"); }
		if (numberOfCookies >= 3) { modelParts.add("cookie003"); }
		if (numberOfCookies >= 4) { modelParts.add("cookie004"); }
		if (numberOfCookies >= 5) { modelParts.add("cookie005"); }
		if (numberOfCookies >= 6) { modelParts.add("cookie006"); }
		if (numberOfCookies >= 7) { modelParts.add("cookie007"); }
		if (numberOfCookies >= 8) { modelParts.add("cookie008"); }
		return modelParts;
	}
	
	@Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess blocka, BlockPos pos, EnumFacing side)
    {
		TileEntityCookieJar cookiejar = (TileEntityCookieJar)blocka.getTileEntity(pos);
		if (cookiejar != null)
		{
			boolean ison = cookiejar.getIsOpen();
			if (ison)
			{
				return 15;
			}
		}
		return 0;
    }
	
	@Override
	public int getStrongPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
		return getWeakPower(state, worldIn, pos, side);
    }
}
