package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.blocks.BlockMarkerPole;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityMarkerPole;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioMeasure implements IMessage {
    BlockPos pos;
    boolean newTest;
    int direction;

    public BiblioMeasure() {

    }

    public BiblioMeasure(BlockPos pos, boolean newTest, int direction) {
        this.pos = pos;
        this.newTest = newTest;
        this.direction = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.newTest = buf.readBoolean();
        this.direction = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeBoolean(this.newTest);
        buf.writeInt(this.direction);
    }

    public static class Handler implements IMessageHandler<BiblioMeasure, IMessage> {

        @Override
        public IMessage onMessage(BiblioMeasure message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                EnumFacing facing = EnumFacing.getFront(message.direction);
                World world = player.world;
                int iadj = 0;
                int jadj = 0;
                int kadj = 0;
                switch (message.direction) {
                    case 0:
                        jadj = -1;
                        break;
                    case 1:
                        jadj = 1;
                        break;
                    case 2:
                        kadj = -1;
                        break;
                    case 3:
                        kadj = 1;
                        break;
                    case 4:
                        iadj = -1;
                        break;
                    case 5:
                        iadj = 1;
                        break;
                    default:
                        iadj = 1;
                        break;
                }

                BlockPos pos = new BlockPos(message.pos.getX() + iadj, message.pos.getY() + jadj,
                        message.pos.getZ() + kadj);
                if (Utils.hasPointLoaded(player, pos)) {
                    if (message.newTest) {
                        if (world.isAirBlock(pos)) {
                            IBlockState st = BlockMarkerPole.instance.getDefaultState();
                            world.setBlockState(pos, st);
                            TileEntityMarkerPole poleTile = (TileEntityMarkerPole) world.getTileEntity(pos);
                            if (poleTile != null) {
                                poleTile.setAngle(EnumFacing.NORTH);
                                if (facing == EnumFacing.UP) {
                                    poleTile.setVertPosition(EnumVertPosition.FLOOR);
                                } else if (facing == EnumFacing.DOWN) {
                                    poleTile.setVertPosition(EnumVertPosition.CEILING);
                                } else {
                                    switch (facing) {
                                        case NORTH: {
                                            facing = EnumFacing.WEST;
                                            break;
                                        }
                                        case WEST: {
                                            facing = EnumFacing.SOUTH;
                                            break;
                                        }
                                        case SOUTH: {
                                            facing = EnumFacing.EAST;
                                            break;
                                        }
                                        case EAST: {
                                            facing = EnumFacing.NORTH;
                                            break;
                                        }
                                        default:
                                            break;
                                    }
                                    poleTile.setAngle(facing);
                                    poleTile.setVertPosition(EnumVertPosition.WALL);
                                }
                                world.markBlockRangeForRenderUpdate(pos, pos);
                            }
                        }
                    } else {
                        // destroy block
                        if (world.getBlockState(pos).getBlock() == BlockMarkerPole.instance) {
                            world.destroyBlock(pos, false);
                        }
                    }
                }
            });
            return null;
        }

    }
}
