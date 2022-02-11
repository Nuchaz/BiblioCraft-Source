package jds.bibliocraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.network.packet.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiblioAtlasSWPClient implements IMessage {
    ItemStack atlas;

    public BiblioAtlasSWPClient() {

    }

    public BiblioAtlasSWPClient(ItemStack atlas) {
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

    public static class Handler implements IMessageHandler<BiblioAtlasSWPClient, IMessage> 
    {

        @Override
        public IMessage onMessage(BiblioAtlasSWPClient message, MessageContext ctx) 
        {
            Minecraft.getMinecraft().addScheduledTask(() -> 
            {
            	handleAtlas(message.atlas);
            });
            return null;
        }

    }
    
    @SideOnly(Side.CLIENT)
    public static void handleAtlas(ItemStack atlas)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        player.rotationPitch = 50.0f;
        Utils.openMapGUI(Minecraft.getMinecraft().player, atlas);
    }
}
