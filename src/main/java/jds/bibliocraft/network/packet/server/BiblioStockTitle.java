package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.items.ItemStockroomCatalog;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioStockTitle implements IMessage {
    String title;

    public BiblioStockTitle() {

    }

    public BiblioStockTitle(String title) {
        this.title = title;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.title = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.title);
    }

    public static class Handler implements IMessageHandler<BiblioStockTitle, IMessage> {

        @Override
        public IMessage onMessage(BiblioStockTitle message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                ItemStack stockroomcatalog = player.getHeldItem(EnumHand.MAIN_HAND);
                if (stockroomcatalog != ItemStack.EMPTY && stockroomcatalog.getItem() instanceof ItemStockroomCatalog) {
                    NBTTagCompound tags = stockroomcatalog.getTagCompound();
                    if (tags == null) {
                        tags = new NBTTagCompound();
                    }
                    NBTTagCompound display = new NBTTagCompound();
                    display.setString("Name", TextFormatting.WHITE + message.title);
                    tags.setTag("display", display);
                    stockroomcatalog.setTagCompound(tags);
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, stockroomcatalog);
                }
            });
            return null;
        }

    }
}
