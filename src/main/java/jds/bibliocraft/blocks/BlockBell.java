package jds.bibliocraft.blocks;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.tileentities.TileEntityBell;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBell extends BiblioSimpleBlock
{
	public static final BlockBell instance = new BlockBell();
	public static final String name = "Bell";
	
	public BlockBell()
	{
		super(Material.IRON, SoundType.METAL, name);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return this.getBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.2F, 0.6F);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		System.out.println("remote? " + world.isRemote);
		// this doesn't run client side at all anymore. hmmm.
		if (!world.isRemote)
		{
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_DING, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityBell();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
}
