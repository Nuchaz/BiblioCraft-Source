package jds.bibliocraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiblioAtlasTGUI implements IMessage {
    ItemStack atlas;
    BlockPos pos;

    public BiblioAtlasTGUI() {

    }

    public BiblioAtlasTGUI(ItemStack atlas, BlockPos pos) {
        this.atlas = atlas;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.atlas = ByteBufUtils.readItemStack(buf);
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.atlas);
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler implements IMessageHandler<BiblioAtlasTGUI, IMessage> {

        @Override
        public IMessage onMessage(BiblioAtlasTGUI message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> 
            {
            	handleAtlas(message.atlas, message.pos);
            });
            return null;
        }

    }
    
    @SideOnly(Side.CLIENT)
    public static void handleAtlas(ItemStack atlas, BlockPos pos)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        final TileEntityMapFrame tile = (TileEntityMapFrame) player.world.getTileEntity(pos); 
        if (tile != null) 
        {
            Utils.openWaypointTransferGUI(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player, atlas, tile);
        }
    }
}
