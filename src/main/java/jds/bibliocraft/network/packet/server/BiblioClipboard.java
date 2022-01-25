package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioClipboard implements IMessage {
    BlockPos pos;
    int updatePos;

    public BiblioClipboard() {

    }

    public BiblioClipboard(BlockPos pos, int updatePos) {
        this.pos = pos;
        this.updatePos = updatePos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.updatePos = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.updatePos);
    }

    public static class Handler implements IMessageHandler<BiblioClipboard, IMessage> {

        @Override
        public IMessage onMessage(BiblioClipboard message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    TileEntity tile = player.world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityClipboard) {
                        TileEntityClipboard clipboard = (TileEntityClipboard) tile;
                        clipboard.updateClipboardFromPlayerSelection(message.updatePos);
                    }
                }
            });
            return null;
        }

    }
}
