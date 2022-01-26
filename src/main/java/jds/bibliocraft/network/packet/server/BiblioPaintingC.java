package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityPainting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioPaintingC implements IMessage {
    BlockPos pos;
    int aspectX;
    int aspectY;
    public BiblioPaintingC() {

    }
    public BiblioPaintingC(BlockPos pos, int aspectX, int aspectY) {
        this.pos = pos;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.aspectX = buf.readInt();
        this.aspectY = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.aspectX);
        buf.writeInt(this.aspectY);
    }
    public static class Handler implements IMessageHandler<BiblioPaintingC, IMessage> {

        @Override
        public IMessage onMessage(BiblioPaintingC message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    World world = player.world;
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityPainting) {
                        TileEntityPainting painting = (TileEntityPainting) tile;
                        painting.setPacketAspectsUpdate(message.aspectX, message.aspectY);
                    }   
                }
            });
            return null;
        }
        
    }
}
