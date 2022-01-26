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

public class BiblioPainting implements IMessage {
    BlockPos pos;
    int corner;
    int scale;
    int res;
    int aspect;
    int rotation;
    int customAspectX;
    int customAspectY;
    boolean hideFrame;
    public BiblioPainting() {

    }
    public BiblioPainting(BlockPos pos, int corner, int scale, int res, int aspect, int rotation, int customAspectX, int customAspectY, boolean hideFrame) {
        this.pos = pos;
        this.corner = corner;
        this.scale = scale;
        this.res = res;
        this.aspect = aspect;
        this.rotation = rotation;
        this.customAspectX = customAspectX;
        this.customAspectY = customAspectY;
        this.hideFrame = hideFrame;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.corner = buf.readInt();
        this.scale = buf.readInt();
        this.res = buf.readInt();
        this.aspect = buf.readInt();
        this.rotation = buf.readInt();
        this.customAspectX = buf.readInt();
        this.customAspectY = buf.readInt();
        this.hideFrame = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.corner);
        buf.writeInt(this.scale);
        buf.writeInt(this.res);
        buf.writeInt(this.aspect);
        buf.writeInt(this.rotation);
        buf.writeInt(this.customAspectX);
        buf.writeInt(this.customAspectY);
        buf.writeBoolean(this.hideFrame);
    }
    public static class Handler implements IMessageHandler<BiblioPainting, IMessage> {

        @Override
        public IMessage onMessage(BiblioPainting message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    World world = player.world;
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityPainting) {
                        TileEntityPainting painting = (TileEntityPainting) tile;
                        painting.setHideFrame(message.hideFrame);
                        painting.setPacketUpdate(message.corner, message.scale, message.res, message.aspect, message.rotation, message.customAspectX, message.customAspectY);
            
                    }   
                }
            });
            return null;
        }
        
    }
}
