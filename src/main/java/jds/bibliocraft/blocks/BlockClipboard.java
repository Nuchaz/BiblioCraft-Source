package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioClipboard;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockClipboard extends BiblioBlock
{
	public static final String name = "Clipboard";
	public static final BlockClipboard instance = new BlockClipboard();
	
	public BlockClipboard()
	{
		super(Material.WOOD, SoundType.WOOD, null, name);
		//setCreativeTab(CreativeTabs.)
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        return new ArrayList<ItemStack>();
    }
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing face, float hitX, float hitY, float hitZ) 
	{
		if (player.isSneaking() && !world.isRemote)
		{
			dropStackInSlot(world, pos, 0, pos);
			world.setBlockToAir(pos);
			return true;
		}
		else if (!player.isSneaking() && world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityClipboard)
			{
				int updatePos = getSelectionPointFromFace(face, hitX, hitY, hitZ);
				BiblioNetworking.INSTANCE.sendToServer(new BiblioClipboard(pos, updatePos));
				// ByteBuf buffer = Unpooled.buffer();
				// buffer.writeInt(pos.getX());
				// buffer.writeInt(pos.getY());
				// buffer.writeInt(pos.getZ());
				// buffer.writeInt(updatePos);
				// BiblioCraft.ch_BiblioClipboard.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioClipboard"));
				return true;
			}
		}
		
		return false;
	}
	
	private int getSelectionPointFromFace(EnumFacing face, float hitx, float hity, float hitz)
	{
		switch (face)
		{
			case NORTH:{return getSelectionPoint(1.0f-hitx, 1.0f-hity);}
			case SOUTH:{return getSelectionPoint(hitx, 1.0f-hity);}
			case WEST:{return getSelectionPoint(hitz, 1.0f-hity);}
			case EAST:{return getSelectionPoint(1.0f-hitz, 1.0f-hity);}
			default: break;
		}
		return -1;
	}
	
	private int getSelectionPoint(float x, float y)
	{
		if (x > 0.21f && x < 0.272f)
		{
			float spacing = 0.0655f;
			for (int i = 0; i < 9; i++)
			{
				if (y > 0.23+(i*spacing) && y < 0.285f+(i*spacing))
				{
					
					return i;
				}
			}	
		}
		
		if (y > 0.83 && y < 0.868f)
		{
			if (x > 0.296f && x < 0.387f)
			{
				return 10;
			}
			if (x > 0.599f && x < 0.843f)
			{
				return 11;
			}
		}
		
		return -1;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityClipboard();
	}
	
	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("Clipboard");
		if (tile instanceof TileEntityClipboard)
		{
			TileEntityClipboard clipboard = (TileEntityClipboard)tile;
			clipboard.getNBTData();
			switch (clipboard.button0state)
			{
			case 1: { modelParts.add("box1c"); break; }
			case 2: { modelParts.add("box1x"); break; }
			}
			
			switch (clipboard.button1state)
			{
			case 1: { modelParts.add("box2c"); break; }
			case 2: { modelParts.add("box2x"); break; }
			}
			
			switch (clipboard.button2state)
			{
			case 1: { modelParts.add("box3c"); break; }
			case 2: { modelParts.add("box3x"); break; }
			}
			
			switch (clipboard.button3state)
			{
			case 1: { modelParts.add("box4c"); break; }
			case 2: { modelParts.add("box4x"); break; }
			}
			
			switch (clipboard.button4state)
			{
			case 1: { modelParts.add("box5c"); break; }
			case 2: { modelParts.add("box5x"); break; }
			}
			
			switch (clipboard.button5state)
			{
			case 1: { modelParts.add("box6c"); break; }
			case 2: { modelParts.add("box6x"); break; }
			}
			
			switch (clipboard.button6state)
			{
			case 1: { modelParts.add("box7c"); break; }
			case 2: { modelParts.add("box7x"); break; }
			}
			
			switch (clipboard.button7state)
			{
			case 1: { modelParts.add("box8c"); break; }
			case 2: { modelParts.add("box8x"); break; }
			}
			
			switch (clipboard.button8state)
			{
			case 1: { modelParts.add("box9c"); break; }
			case 2: { modelParts.add("box9x"); break; }
			}
		}
		return modelParts;
	}
	
	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		
	}
	
	@Override
	public ItemStack getPickBlockExtras(ItemStack stack, World world, BlockPos pos) 
	{
		return stack;
	}

	@Override
	public ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state) 
	{
		return state;
	}

	@Override
	public IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state) 
	{
		return state;
	}
	
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		return transform;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tile = blockAccess.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityClipboard)
		{
			TileEntityClipboard clipboard = (TileEntityClipboard)tile;
			switch (clipboard.getAngle())
			{
				case SOUTH:{output = this.getBlockBounds(0.97F, 0.08F, 0.15F, 1.0F, 0.92F, 0.85F); break;}
				case WEST:{output = this.getBlockBounds(0.15F, 0.08F, 0.97F, 0.85F, 0.92F, 1.0F); break;}
				case NORTH:{output = this.getBlockBounds(0.0F, 0.08F, 0.15F, 0.03F, 0.92F, 0.85F); break;}
				case EAST:{output = this.getBlockBounds(0.15F, 0.08F, 0.0F, 0.85F, 0.92F, 0.03F); break;}
				default: break;
			}
		}
	    return output;
	}

	@Override
	public IBlockState getFinalBlockstate(IBlockState state, IBlockState newState) 
	{
		return newState;
	}
}
