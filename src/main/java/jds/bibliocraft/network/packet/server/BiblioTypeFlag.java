package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.helpers.FileUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// UNTESTED
public class BiblioTypeFlag implements IMessage {
    String bookname;
    boolean newFlag;
    // TODO: Should not be dictated by the client
    boolean isServer;

    public BiblioTypeFlag() {

    }
    public BiblioTypeFlag(String bookname, boolean newFlag, boolean isServer) {
        this.bookname = bookname;
        this.newFlag = newFlag;
        this.isServer = isServer;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.bookname = ByteBufUtils.readUTF8String(buf);
        this.newFlag = buf.readBoolean();
        this.isServer = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.bookname);
        buf.writeBoolean(this.newFlag);
        buf.writeBoolean(this.isServer);
    }
    public static class Handler implements IMessageHandler<BiblioTypeFlag, IMessage> {

        @Override
        public IMessage onMessage(BiblioTypeFlag message, MessageContext ctx) {
            FileUtil util = new FileUtil();
            if (!util.updatePublicFlag(!message.isServer, message.bookname, message.newFlag)) {
                BiblioCraft.LOGGER.warn("Updating book flag for " + message.bookname + " failed");
            }
            return null;
        }
        
    }
}
