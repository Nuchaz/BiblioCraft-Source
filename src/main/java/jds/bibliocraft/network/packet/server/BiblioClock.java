package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityClock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioClock implements IMessage {
    NBTTagCompound tag;
    boolean tick;
    boolean chime;
    boolean rsout;
    boolean rspulse;
    BlockPos pos;

    public BiblioClock() {

    }

    public BiblioClock(NBTTagCompound tag, boolean tick, boolean chime, boolean rsout, boolean rspulse, BlockPos pos) {
        this.tag = tag;
        this.tick = tick;
        this.chime = chime;
        this.rsout = rsout;
        this.rspulse = rspulse;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tag = ByteBufUtils.readTag(buf);
        this.tick = buf.readBoolean();
        this.chime = buf.readBoolean();
        this.rsout = buf.readBoolean();
        this.rspulse = buf.readBoolean();
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.tag);
        buf.writeBoolean(this.tick);
        buf.writeBoolean(this.chime);
        buf.writeBoolean(this.rsout);
        buf.writeBoolean(this.rspulse);
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler implements IMessageHandler<BiblioClock, IMessage> {

        @Override
        public IMessage onMessage(BiblioClock message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    World world = player.world;
                    int[] chimes = message.tag.getIntArray("chimes");
                    int[] redstone = message.tag.getIntArray("redstone");

                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityClock) {
                        TileEntityClock clock = (TileEntityClock) tile;
                        clock.setSettingFromGui(chimes, redstone, message.tick, message.chime, message.rsout,
                                message.rspulse);
                    }
                }
            });
            return null;
        }

    }
}
