package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.helpers.FileUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

// UNTESTED
public class BiblioTypeFlag implements IMessage {
    String bookname;
    boolean newFlag;

    public BiblioTypeFlag() {

    }
    public BiblioTypeFlag(String bookname, boolean newFlag) {
        this.bookname = bookname;
        this.newFlag = newFlag;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.bookname = ByteBufUtils.readUTF8String(buf);
        this.newFlag = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.bookname);
        buf.writeBoolean(this.newFlag);
    }
    public static class Handler implements IMessageHandler<BiblioTypeFlag, IMessage> {

        @Override
        public IMessage onMessage(BiblioTypeFlag message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                boolean isServer = FMLCommonHandler.instance().getSide() == Side.SERVER;
                FileUtil util = new FileUtil();
                if (!util.updatePublicFlag(!isServer, message.bookname, message.newFlag)) {
                    BiblioCraft.LOGGER.warn("Updating book flag for " + message.bookname + " failed");
                }
            });
            return null;
        }
        
    }
}
