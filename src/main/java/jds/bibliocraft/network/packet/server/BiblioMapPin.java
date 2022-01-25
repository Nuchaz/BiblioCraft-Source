package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioMapPin implements IMessage {
    BlockPos pos;
    float xPin;
    float yPin;
    String name;
    int colour;
    int pinNum;
    boolean remove;
    boolean edit;
    public BiblioMapPin() {

    }
    public BiblioMapPin(BlockPos pos, float xPin, float yPin, String name, int colour, int pinNum, boolean remove, boolean edit) {
        this.pos = pos;
        this.xPin = xPin;
        this.yPin = yPin;
        this.name = name;
        this.colour = colour;
        this.pinNum = pinNum;
        this.remove = remove;
        this.edit = edit;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.xPin = buf.readFloat();
        this.yPin = buf.readFloat();
        this.name = ByteBufUtils.readUTF8String(buf);
        this.colour = buf.readInt();
        this.pinNum = buf.readInt();
        this.remove = buf.readBoolean();
        this.edit = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeFloat(this.xPin);
        buf.writeFloat(this.yPin);
        ByteBufUtils.writeUTF8String(buf, this.name);
        buf.writeInt(this.colour);
        buf.writeInt(this.pinNum);
        buf.writeBoolean(this.remove);
        buf.writeBoolean(this.edit);
    }
    public static class Handler implements IMessageHandler<BiblioMapPin, IMessage> {

        @Override
        public IMessage onMessage(BiblioMapPin message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    World world = player.world;
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityMapFrame) {
                        TileEntityMapFrame mapFrame = (TileEntityMapFrame) tile;
                        if (!message.remove) {
                            if (!message.edit) {
                                mapFrame.addPinCoords(message.xPin, message.yPin, message.name, message.colour);
                            } else {
                                mapFrame.editPinData(message.name, message.colour, message.pinNum);
                            }
                        } else {
                            mapFrame.removePin(message.pinNum);
                        }
                    }   
                }
            });
            return null;
        }
        
    }
}
