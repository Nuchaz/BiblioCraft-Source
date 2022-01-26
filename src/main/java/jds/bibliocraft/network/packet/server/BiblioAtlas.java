package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioAtlasSWPClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioAtlas implements IMessage {
    boolean autoCenter;
    boolean autoCreate;
    int zoomLevel;
    int slot;
    boolean changeGUI;

    public BiblioAtlas() {

    }

    public BiblioAtlas(boolean autoCenter, boolean autoCreate, int zoomLevel, int slot, boolean changeGUI) {
        this.autoCenter = autoCenter;
        this.autoCreate = autoCreate;
        this.zoomLevel = zoomLevel;
        this.slot = slot;
        this.changeGUI = changeGUI;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.autoCenter = buf.readBoolean();
        this.autoCreate = buf.readBoolean();
        this.zoomLevel = buf.readInt();
        this.slot = buf.readInt();
        this.changeGUI = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.autoCenter);
        buf.writeBoolean(this.autoCreate);
        buf.writeInt(this.zoomLevel);
        buf.writeInt(this.slot);
        buf.writeBoolean(this.changeGUI);
    }

    public static class Handler implements IMessageHandler<BiblioAtlas, IMessage> {

        @Override
        public IMessage onMessage(BiblioAtlas message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                EntityPlayerMP playermp = player;
                ItemStack atlas = playermp.getHeldItem(EnumHand.MAIN_HAND); // I have to get the item and update it
                                                                            // since
                                                                            // the
                // container also does an update.
                if (atlas != ItemStack.EMPTY && atlas.getItem() instanceof ItemAtlas) {
                    FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                        @Override
                        public void run() {
                            ItemStack atlas = player.getHeldItem(EnumHand.MAIN_HAND);
                            NBTTagCompound tags = atlas.getTagCompound();
                            if (tags != null) {
                                // int oldSlot = tags.getInteger("mapSlot");
                                tags.setBoolean("autoCenter", message.autoCenter);
                                tags.setBoolean("autoCreate", message.autoCreate);
                                tags.setInteger("zoomLevel", message.zoomLevel);
                                tags.setInteger("mapSlot", message.slot);
                                tags.setInteger("lastGUImode", 0);
                                atlas.setTagCompound(tags);
                                atlas.setItemDamage(ItemAtlas.setAtlasDamage(atlas, message.slot));
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, atlas);
                            }
                            if (message.changeGUI) {
                                ItemAtlas realAtlas = (ItemAtlas) atlas.getItem();
                                realAtlas.getAtlasInventory(atlas, player.world, player, message.autoCreate,
                                        message.autoCenter, message.zoomLevel,
                                        message.slot);
                                player.closeScreen();
                                player.rotationPitch = 50.0f;
                                BiblioNetworking.INSTANCE.sendTo(
                                        new BiblioAtlasSWPClient(player.getHeldItem(EnumHand.MAIN_HAND)), player);
                                // ByteBuf buffer = Unpooled.buffer();
                                // ByteBufUtils.writeItemStack(buffer, player.getHeldItem(EnumHand.MAIN_HAND));
                                // BiblioCraft.ch_BiblioAtlasGUIswap
                                // .sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlasSWP"),
                                // player);
                            }
                        }
                    });

                }
            });
            return null;
        }

    }
}
