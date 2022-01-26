package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityFancyWorkbench;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// TODO: review. I removed the `id` part of the packet as it seems only to send the player ID, which we know on the server.
// Leading on, merge this and BiblioRBookLoad. I didn't notice earlier and it sounds like a pain to do now :P
public class BiblioRBook implements IMessage {
    BlockPos pos;

    public BiblioRBook() {

    }

    public BiblioRBook(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler implements IMessageHandler<BiblioRBook, IMessage> {

        @Override
        public IMessage onMessage(BiblioRBook message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    World world = player.world;
                    // TODO: check pos range to plr
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityFancyWorkbench) {
                        TileEntityFancyWorkbench bench = (TileEntityFancyWorkbench) tile;
                        bench.setBookGrid(player.getEntityId());
                    }   
                }
            });
            return null;
        }

    }
}
