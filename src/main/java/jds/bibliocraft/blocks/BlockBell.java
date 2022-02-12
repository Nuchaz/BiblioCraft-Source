package jds.bibliocraft.blocks;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioSoundPlayer;
import jds.bibliocraft.network.packet.client.BiblioStockLog;
import jds.bibliocraft.tileentities.TileEntityBell;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class BlockBell extends BiblioSimpleBlock
{
	public static final BlockBell instance = new BlockBell();
	public static final String name = "Bell";
	public static final float range = 32.0F;
	
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
		if (!world.isRemote)
		{
			//world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), CommonProxy.SOUND_DING, SoundCategory.BLOCKS, 1.0F, 1.0F);
			TargetPoint target = new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
			BiblioNetworking.INSTANCE.sendToAllAround(new BiblioSoundPlayer(CommonProxy.SOUND_BELL_DING_TEXT, pos, 1.0F, 1.0F), target);
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
