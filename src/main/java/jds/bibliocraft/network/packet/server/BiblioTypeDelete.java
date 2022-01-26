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

public class BiblioTypeDelete implements IMessage {
    String bookname;
    public BiblioTypeDelete() {

    }
    public BiblioTypeDelete(String bookname) {
        this.bookname = bookname;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.bookname = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.bookname);
    }
    public static class Handler implements IMessageHandler<BiblioTypeDelete, IMessage> {

        @Override
        public IMessage onMessage(BiblioTypeDelete message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                FileUtil util = new FileUtil();
                boolean isServer = FMLCommonHandler.instance().getSide() == Side.SERVER;
                if (util.deleteBook(!isServer, message.bookname)) {
                    BiblioCraft.LOGGER.info(message.bookname + " has been deleted FOREVER!");
                } else {
                    BiblioCraft.LOGGER.warn("Deletion of " + message.bookname + " failed.");
                    // FMLLog.warning
                }
            });
            return null;
        }

    }
}
