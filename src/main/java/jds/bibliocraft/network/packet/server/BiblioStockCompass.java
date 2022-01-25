package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.items.ItemWaypointCompass;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioStockCompass implements IMessage {
    int slotNumber;
    String title;
    int x;
    int z;

    public BiblioStockCompass() {

    }

    public BiblioStockCompass(int slotNumber, String title, int x, int z) {
        this.slotNumber = slotNumber;
        this.title = title;
        this.x = x;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotNumber = buf.readInt();
        this.title = ByteBufUtils.readUTF8String(buf);
        this.x = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slotNumber);
        ByteBufUtils.writeUTF8String(buf, this.title);
        buf.writeInt(this.x);
        buf.writeInt(this.z);
    }

    public static class Handler implements IMessageHandler<BiblioStockCompass, IMessage> {

        @Override
        public IMessage onMessage(BiblioStockCompass message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (message.slotNumber < player.inventory.getSizeInventory()) {
                    ItemStack compass = player.inventory.getStackInSlot(message.slotNumber);
                    if (compass != ItemStack.EMPTY && compass.getItem() instanceof ItemWaypointCompass) {
                        NBTTagCompound tags = compass.getTagCompound();
                        if (tags == null) {
                            tags = new NBTTagCompound();
                        }
                        tags.setInteger("XCoord", message.x);
                        tags.setInteger("ZCoord", message.z);
                        tags.setString("WaypointName", message.title);
                        compass.setTagCompound(tags);
                        player.inventory.setInventorySlotContents(message.slotNumber, compass);
                    }
                }
            });
            return null;
        }

    }
}
