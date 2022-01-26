package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityTypeMachine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioTypeUpdate implements IMessage {
    BlockPos pos;

    public BiblioTypeUpdate() {

    }

    public BiblioTypeUpdate(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler implements IMessageHandler<BiblioTypeUpdate, IMessage> {

        @Override
        public IMessage onMessage(BiblioTypeUpdate message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                // TODO: Check reach distance between block and player
                if (Utils.hasPointLoaded(player, message.pos)) {
                    TileEntity tile = player.world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityTypeMachine) {
                        TileEntityTypeMachine typeTile = (TileEntityTypeMachine) tile;
                        typeTile.booklistset();
                    }
                }
            });
            return null;
        }

    }
}
