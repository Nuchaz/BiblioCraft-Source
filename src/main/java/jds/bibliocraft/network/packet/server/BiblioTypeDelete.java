package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.helpers.FileUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioTypeDelete implements IMessage {
    String bookname;
    // TODO: Should not be from the client
    boolean isServer;
    public BiblioTypeDelete() {

    }
    public BiblioTypeDelete(String bookname, boolean isServer) {
        this.bookname = bookname;
        this.isServer = isServer;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.bookname = ByteBufUtils.readUTF8String(buf);
        this.isServer = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.bookname);
        buf.writeBoolean(this.isServer);
    }
    public static class Handler implements IMessageHandler<BiblioTypeDelete, IMessage> {

        @Override
        public IMessage onMessage(BiblioTypeDelete message, MessageContext ctx) {
            FileUtil util = new FileUtil();
            if (util.deleteBook(!message.isServer, message.bookname)) {
                BiblioCraft.LOGGER.info(message.bookname + " has been deleted FOREVER!");
            } else {
                BiblioCraft.LOGGER.warn("Deletion of " + message.bookname + " failed.");
                // FMLLog.warning
            }
            return null;
        }

    }
}
