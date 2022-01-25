package jds.bibliocraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioAtlasClient implements IMessage {
    ItemStack atlas;
    public BiblioAtlasClient() {

    }
    public BiblioAtlasClient(ItemStack atlas) {
        this.atlas = atlas;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.atlas = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.atlas);
    }
    public static class Handler implements IMessageHandler<BiblioAtlasClient, IMessage> {

        @Override
        public IMessage onMessage(BiblioAtlasClient message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                // TODO: current item might have changed (on server) before we set it? not sure.
                player.inventory.setInventorySlotContents(player.inventory.currentItem, message.atlas);
            });
            return null;
        }
        
    }
}
