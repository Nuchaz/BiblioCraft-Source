package jds.bibliocraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.items.ItemRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioRecipeText implements IMessage {
    String text;
    int currentSlot;
    public BiblioRecipeText() {

    }
    public BiblioRecipeText(String text, int currentSlot) {
        this.text = text;
        this.currentSlot = currentSlot;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.text = ByteBufUtils.readUTF8String(buf);
        this.currentSlot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.text);
        buf.writeInt(this.currentSlot);
    }
    public static class Handler implements IMessageHandler<BiblioRecipeText, IMessage> {

        @Override
        public IMessage onMessage(BiblioRecipeText message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                ItemStack currentBook = player.inventory.getStackInSlot(message.currentSlot);
                if (currentBook != ItemStack.EMPTY) {
                    if (currentBook.getItem() instanceof ItemRecipeBook) {
                        ItemRecipeBook book = (ItemRecipeBook) currentBook.getItem();
                        book.updateFromPacket(message.text);
                    }
                }
            });
            return null;
        }
        
    }
}
