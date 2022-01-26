package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.blocks.BlockMarkerPole;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioMeasure;
import jds.bibliocraft.tileentities.TileEntityMarkerPole;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
//import net.minecraft.network.packet.Packet;
//import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item
{
	public static final String name = "tapeMeasure";
	public static final ItemTapeMeasure instance = new ItemTapeMeasure();
	
	private int firstMeasurex = 0;
	private int firstMeasurey = 0;
	private int firstMeasurez = 0;
	private int oldx = 0;
	private int oldy = 0;
	private int oldz = 0;
	private EnumFacing oldface = EnumFacing.NORTH;
	
	private int mode = 1;
	
	private int ticktime = 0;
	
	public ItemTapeMeasure()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setRegistryName(name);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (firstMeasurex != 0 || firstMeasurey != 0 || firstMeasurez != 0)
			{

				int xdist = Math.abs(firstMeasurex - pos.getX());
				int ydist = Math.abs(firstMeasurey - pos.getY());
				int zdist = Math.abs(firstMeasurez - pos.getZ());
				xdist++;
				zdist++;
				if (side != EnumFacing.DOWN && side != EnumFacing.UP)
				{
					ydist = ydist + 1;
				}
				int measurmentxz = (int) Math.sqrt((xdist*xdist)+(zdist*zdist));
				int measurmentxy = (int) Math.sqrt((xdist*xdist)+(ydist*ydist));
				int measurmentyz = (int) Math.sqrt((ydist*ydist)+(zdist*zdist));
				
				if (mode == 1)
				{
						if (zdist != 1)
						{
							player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.measurenorthsouth")+zdist)); //Measurement North/South = 
						}
						if (xdist != 1)
						{
							player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.measureeastwest")+xdist)); //Measurement East/West = 
						}
						if (ydist != 0)
						{
							player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.measureheight")+ydist)); //Measurement Height = 
						}
						if (xdist == 1 && zdist == 1 && ydist == 0)
						{
							player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.nomeasure")));             //No Measurement
						}
					

				}
			   if (mode == 0)
				{
					if (ydist == 0)
					{
						player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.measure")+measurmentxz)); //Measurement = 
					}
					else if (xdist == 0)
					{
						player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.measure")+measurmentyz));  //Measurement = 
					}
					else if (zdist == 0)
					{
						player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.measure")+measurmentxy));  //Measurement = 
					}
					else
					{
						int euclideon = (int) Math.sqrt((xdist*xdist)+(ydist*ydist)+(zdist*zdist));
						player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.measure")+euclideon));  //Measurement = 
					}
				}
			    player.playSound(CommonProxy.SOUND_TAPE_CLOSE, 1.0F, 1.0F);
			    oldx = firstMeasurex;
			    oldy = firstMeasurey;
			    oldz = firstMeasurez;
				firstMeasurex = 0;
				firstMeasurey = 0;
				firstMeasurez = 0;
				sendPacket(false, oldx, oldy, oldz, oldface);
			}
			else
			{
				player.playSound(CommonProxy.SOUND_TAPE_OPEN, 1.0F, 1.0F); 
				player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.startmeasure")));  //Starting Measurement.
				firstMeasurex = pos.getX();
				firstMeasurey = pos.getY();
				firstMeasurez = pos.getZ();
				oldface = side;
				sendPacket(true, firstMeasurex, firstMeasurey, firstMeasurez, side);
				placeBlock(world, firstMeasurex, firstMeasurey, firstMeasurez, side);
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	public void sendPacket(boolean newOrOld, int i, int j, int k, EnumFacing direction)
	{
        // ByteBuf buffer = Unpooled.buffer();
        try
        {
			BiblioNetworking.INSTANCE.sendToServer(new BiblioMeasure(new BlockPos(i, j, k), newOrOld, direction.getIndex()));
        	// buffer.writeInt(i);
        	// buffer.writeInt(j);
        	// buffer.writeInt(k);
        	// buffer.writeBoolean(newOrOld);
        	// buffer.writeInt(direction.getIndex());
        	// BiblioCraft.ch_BiblioMeasure.sendToServer(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioMeasure"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
	}
	
	private void placeBlock(World world, int x, int y, int z, EnumFacing facing)
	{
		int xadj = 0;
		int yadj = 0;
		int zadj = 0;
		switch (facing)
		{
		case DOWN: yadj = -1; break;
		case UP: yadj = 1;  break;
		case NORTH: zadj = -1; break;
		case SOUTH: zadj = 1;  break;
		case WEST: xadj = -1; break;
		case EAST: xadj = 1;  break;
		default: xadj = 1; break;
		}
		BlockPos pos = new BlockPos(x+xadj, y+yadj, z+zadj);
		
		IBlockState st = BlockMarkerPole.instance.getDefaultState();
		world.setBlockState(pos, st);
		TileEntityMarkerPole poleTile = (TileEntityMarkerPole)world.getTileEntity(pos);
		if (poleTile != null)
		{
			poleTile.setAngle(EnumFacing.NORTH);
			if (facing == EnumFacing.UP)
			{
				poleTile.setVertPosition(EnumVertPosition.FLOOR);
			}
			else if (facing == EnumFacing.DOWN)
			{
				poleTile.setVertPosition(EnumVertPosition.CEILING);
			}
			else
			{
				switch (facing)
				{
					case NORTH: {facing = EnumFacing.WEST; break;}
					case WEST: {facing = EnumFacing.SOUTH; break;}
					case SOUTH: {facing = EnumFacing.EAST; break;}
					case EAST: {facing = EnumFacing.NORTH; break;}
					default: break;
				}
				poleTile.setAngle(facing);
				poleTile.setVertPosition(EnumVertPosition.WALL);
			}
			world.markBlockRangeForRenderUpdate(pos, pos);
		}
	}
	
	public void setMeasurments(boolean newOrOld, int i, int j, int k)
	{
		if (newOrOld)
		{
			firstMeasurex = i;
			firstMeasurey = j;
			firstMeasurez = k;
		}
		else
		{
			oldx = i;
			oldy = j;
			oldz = k;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote)
		{
			if (mode == 0)
			{
				player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.mode0")));  //Switching mode to North/South, East/West, and height.
				mode = 1;
			}
			else if (mode == 1)
			{
				player.sendMessage(new TextComponentString(I18n.translateToLocal("tape.mode1")));  //Switching mode to absolute measurment.
				mode = 0;
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	 public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) 
	 {
		if (ticktime > 19)
		{
			NBTTagCompound tapem = itemstack.getTagCompound();
			if (tapem == null)
			{
				tapem = new NBTTagCompound();
				tapem.setInteger("distance", 0);
			}
			if(firstMeasurex != 0 || firstMeasurey != 0 || firstMeasurez != 0)
			{
				int currentx = (int)entity.posX;
				int currenty = (int)entity.posY;
				int currentz = (int)entity.posZ;
				if (currentx <  0)
				{
					currentx = currentx - 1;
				}
				if (currentz <  0)
				{
					currentz = currentz - 1;
				}
				int xdist;
				int ydist;
				int zdist;
				if (firstMeasurex > currentx)
				{
					xdist = Math.abs(firstMeasurex - currentx);
				}
				else
				{
					xdist = Math.abs(currentx - firstMeasurex);
				}
				if (firstMeasurey > currenty)
				{
					ydist = Math.abs(firstMeasurey - currenty);
				}
				else
				{
					ydist = Math.abs(currenty - firstMeasurey);
				}
				if (firstMeasurez > currentz)
				{
					zdist = Math.abs(firstMeasurez - currentz);
				}
				else
				{
					zdist = Math.abs(currentz - firstMeasurez);
				}

				xdist++;
				zdist++;
				int euclideon = (int) Math.sqrt((xdist*xdist)+(ydist*ydist)+(zdist*zdist));
				tapem.setInteger("distance", euclideon);
				itemstack.setTagCompound(tapem);
			}
			else
			{
				tapem.setInteger("distance", 0);
				itemstack.setTagCompound(tapem);
			}
			ticktime = 0;
		}
		else
		{
			ticktime++;
		}
	 }
	
}
