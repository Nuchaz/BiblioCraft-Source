package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityTypeMachine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// UNTESTED!!
public class BiblioType implements IMessage {
    String bookName;
    BlockPos pos;
    // dummy constructor for FML
    public BiblioType() {

    }
    public BiblioType(String bookName, BlockPos pos) {
        this.bookName = bookName;
        this.pos = pos;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.bookName = ByteBufUtils.readUTF8String(buf);
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.bookName);
        buf.writeLong(this.pos.toLong());
    }
    public static class Handler implements IMessageHandler<BiblioType, IMessage> {

        @Override
        public IMessage onMessage(BiblioType message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    World world = player.world;
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null) {
                        TileEntityTypeMachine typetile = (TileEntityTypeMachine) tile;
                        typetile.setBookname(message.bookName);
                    }   
                }
            });
            return null;
        }
        
    }
}
