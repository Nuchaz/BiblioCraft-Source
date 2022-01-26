package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.Config;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityDesk;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioMCBEdit implements IMessage {
    BlockPos pos;
    int currentPage;
    ItemStack book;

    public BiblioMCBEdit() {

    }

    public BiblioMCBEdit(BlockPos pos, int currentPage, ItemStack book) {
        this.pos = pos;
        this.currentPage = currentPage;
        this.book = book;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.currentPage = buf.readInt();
        this.book = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.currentPage);
        ByteBufUtils.writeItemStack(buf, this.book);
    }

    public static class Handler implements IMessageHandler<BiblioMCBEdit, IMessage> {

        @Override
        public IMessage onMessage(BiblioMCBEdit message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (message.book != ItemStack.EMPTY) {
                    if (Config.testBookValidity(message.book)) {
                        // TODO: distance from player to position
                        if (Utils.hasPointLoaded(player, message.pos)) {
                            TileEntityDesk deskTile = (TileEntityDesk) player.world.getTileEntity(message.pos);
                            if (deskTile != null) {
                                deskTile.overwriteWrittenBook(message.book);
                                deskTile.setCurrentPage(message.currentPage);
                            }   
                        }
                    }
                }
            });
            return null;
        }

    }
}
