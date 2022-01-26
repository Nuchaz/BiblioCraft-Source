package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityDesk;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioMCBPage implements IMessage {
    BlockPos pos;
    int currentPage;

    public BiblioMCBPage() {

    }

    public BiblioMCBPage(BlockPos pos, int currentPage) {
        this.pos = pos;
        this.currentPage = currentPage;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.currentPage = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.currentPage);
    }

    public static class Handler implements IMessageHandler<BiblioMCBPage, IMessage> {

        @Override
        public IMessage onMessage(BiblioMCBPage message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    TileEntityDesk deskTile = (TileEntityDesk) player.world.getTileEntity(message.pos);
                    if (deskTile != null) {
                        deskTile.setCurrentPage(message.currentPage);
                    }
                }
            });
            return null;
        }

    }
}
