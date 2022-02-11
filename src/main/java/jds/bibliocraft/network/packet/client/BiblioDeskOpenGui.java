package jds.bibliocraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.network.packet.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiblioDeskOpenGui implements IMessage {
    BlockPos pos;
    ItemStack book;
    boolean canCraft;

    public BiblioDeskOpenGui() {

    }

    public BiblioDeskOpenGui(BlockPos pos, ItemStack book, boolean canCraft) {
        this.pos = pos;
        this.book = book;
        this.canCraft = canCraft;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.book = ByteBufUtils.readItemStack(buf);
        this.canCraft = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        ByteBufUtils.writeItemStack(buf, this.book);
        buf.writeBoolean(this.canCraft);
    }

    public static class Handler implements IMessageHandler<BiblioDeskOpenGui, IMessage> {

        @Override
        public IMessage onMessage(BiblioDeskOpenGui message, MessageContext ctx) {
            int x = message.pos.getX();
            int y = message.pos.getY();
            int z = message.pos.getZ();
            ItemStack book = message.book;
            boolean canCraft = message.canCraft;
            if (book != ItemStack.EMPTY) {
                final Item signedtest = book.getItem();
                Minecraft.getMinecraft().addScheduledTask(() -> {
                	
                	handleBook(book, x, y, z, signedtest, canCraft);

                });
            }
            return null;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void handleBook(ItemStack book, int x, int y, int z, Item signedtest, boolean canCraft)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (signedtest instanceof ItemWrittenBook) {
            Utils.openWritingGUI(player, book, x, y, z, false);
            // signedtest.onItemRightClick(book, world, player);
        }
        if (signedtest instanceof ItemWritableBook) {
            Utils.openWritingGUI(player, book, x, y, z, true);
        }
        if (signedtest instanceof ItemClipboard) {
            Utils.openClipboardGUI(book, false, x, y, z);
        }
        if (signedtest instanceof ItemBigBook) {
            Utils.openBigBookGUI(book, x, y, z, player.getDisplayNameString());
        }
        if (signedtest instanceof ItemRecipeBook) {
            Utils.openRecipeBookGUI(book, x, y, z, -1, canCraft);
        }
        if (Loader.isModLoaded("thaumcraft") && book.toString().contains("thaumonomicon")) {
            signedtest.onItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
        }
        if (Loader.isModLoaded("tailcraft") && book.toString().contains("railcraft.routing.table")) {
            signedtest.onItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
        }
        if (Loader.isModLoaded("craftguide") && book.toString().contains("craftguide")) {
            signedtest.onItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
        }
        if (Loader.isModLoaded("botania") && book.getUnlocalizedName().contentEquals("item.lexicon")) {
            // System.out.println(book.getUnlocalizedName());
            // signedtest.onItemRightClick(book, world, player);
            // doesnt work
        }
    }
}
