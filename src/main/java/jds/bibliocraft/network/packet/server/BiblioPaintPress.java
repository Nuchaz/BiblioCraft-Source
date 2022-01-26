package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioPaintPress implements IMessage {
    BlockPos pos;
    int artType;
    String artName;
    boolean applyToCanvas;

    public BiblioPaintPress() {

    }

    public BiblioPaintPress(BlockPos pos, int artType, String artName, boolean applyToCanvas) {
        this.pos = pos;
        this.artType = artType;
        this.artName = artName;
        this.applyToCanvas = applyToCanvas;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.artType = buf.readInt();
        this.artName = ByteBufUtils.readUTF8String(buf);
        this.applyToCanvas = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.artType);
        ByteBufUtils.writeUTF8String(buf, this.artName);
        buf.writeBoolean(this.applyToCanvas);
    }

    public static class Handler implements IMessageHandler<BiblioPaintPress, IMessage> {

        @Override
        public IMessage onMessage(BiblioPaintPress message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (Utils.hasPointLoaded(player, message.pos)) {
                World world = player.world;
                TileEntity tile = world.getTileEntity(message.pos);
                if (tile != null && tile instanceof TileEntityPaintPress) {
                    TileEntityPaintPress press = (TileEntityPaintPress) tile;
                    press.setSelectedPainting(message.artType, message.artName);
                    if (message.applyToCanvas) {
                        press.setCycle(true);
                    }
                }   
            }
            return null;
        }

    }
}
