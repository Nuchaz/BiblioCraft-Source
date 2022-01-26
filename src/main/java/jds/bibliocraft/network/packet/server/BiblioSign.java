package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// TODO check distance, claim plugins etc
public class BiblioSign implements IMessage {
    String[] text;
    int[] textScales;
    int numOfLines;
    int s1Scale;
    int s1Rot;
    int s2Scale;
    int s2Rot;
    int s1x;
    int s1y;
    int s2x;
    int s2y;
    BlockPos pos;
    public BiblioSign() {

    }
    // TODO: `text` and `textScales` length check
    public BiblioSign(String[] text, int[] textScales, int numOfLines, int s1Scale, int s1Rot, int s2Scale, int s2Rot, int s1x, int s1y, int s2x, int s2y, BlockPos pos) {
        this.text = text;
        this.textScales = textScales;
        this.numOfLines = numOfLines;
        this.s1Scale = s1Scale;
        this.s1Rot = s1Rot;
        this.s2Scale = s2Scale;
        this.s2Rot = s2Rot;
        this.s1x = s1x;
        this.s1y = s1y;
        this.s2x = s2x;
        this.s2y = s2y;
        this.pos = pos;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.text = new String[15];
        this.textScales = new int[15];
        for (int n = 0; n < 15; n++) {
			text[n] = ByteBufUtils.readUTF8String(buf);
			textScales[n] = buf.readInt();
		}
        numOfLines = buf.readInt();
		s1Scale = buf.readInt();
		s1Rot = buf.readInt();
		s2Scale = buf.readInt();
		s2Rot = buf.readInt();
		s1x = buf.readInt();
		s1y = buf.readInt();
		s2x = buf.readInt();
		s2y = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (int i = 0; i < 15; i++) {
            ByteBufUtils.writeUTF8String(buf, text[i]);
            buf.writeInt(textScales[i]);
        }
        buf.writeInt(numOfLines);
        buf.writeInt(s1Scale);
        buf.writeInt(s1Rot);
        buf.writeInt(s2Scale);
        buf.writeInt(s2Rot);
        buf.writeInt(s1x);
        buf.writeInt(s1y);
        buf.writeInt(s2x);
        buf.writeInt(s2y);
        buf.writeLong(this.pos.toLong());
    }
    public static class Handler implements IMessageHandler<BiblioSign, IMessage> {

        @Override
        public IMessage onMessage(BiblioSign message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    World world = player.world;
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null) {
                        if (tile instanceof TileEntityFancySign) {
                            TileEntityFancySign sign = (TileEntityFancySign) tile;
                            sign.updateFromPacket(message.text, message.textScales, message.numOfLines, message.s1Scale, message.s1Rot, message.s1x, message.s1y, message.s2Scale, message.s2Rot, message.s2x, message.s2y);
                        }
                    }   
                }
            });
            return null;
        }
        
    }
}
