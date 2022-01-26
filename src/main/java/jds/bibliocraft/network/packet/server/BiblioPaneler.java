package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioPaneler implements IMessage {
    String texName;
    BlockPos pos;
    public BiblioPaneler() {
        
    }
    public BiblioPaneler(String texName, BlockPos pos) {
        this.texName = texName;
        this.pos = pos;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.texName = ByteBufUtils.readUTF8String(buf);
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.texName);
        buf.writeLong(this.pos.toLong());
    }
    public static class Handler implements IMessageHandler<BiblioPaneler, IMessage> {

        @Override
        public IMessage onMessage(BiblioPaneler message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    TileEntity tile = player.world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityFurniturePaneler) {
                        TileEntityFurniturePaneler paneler = (TileEntityFurniturePaneler) tile;
                        paneler.setCustomCraftingTex(message.texName);
                    }   
                }
            });
            return null;
        }
        
    }
}
