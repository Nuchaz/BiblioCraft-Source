package jds.bibliocraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.helpers.BiblioRenderHelper;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.server.BiblioPaneler;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioPanelerClient implements IMessage {
    ItemStack panels;
    BlockPos pos;

    public BiblioPanelerClient() {

    }

    public BiblioPanelerClient(ItemStack panels, BlockPos pos) {
        this.panels = panels;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.panels = ByteBufUtils.readItemStack(buf);
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.panels);
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler implements IMessageHandler<BiblioPanelerClient, IMessage> {

        @Override
        public IMessage onMessage(BiblioPanelerClient message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                String panelTextureName = "none";
                if (message.panels != ItemStack.EMPTY) {
                    panelTextureName = BiblioRenderHelper.getBlockTextureString(message.panels);
                }
    
                TileEntity tile = player.world.getTileEntity(message.pos);
                if (tile != null && tile instanceof TileEntityFurniturePaneler) {
                    TileEntityFurniturePaneler paneler = (TileEntityFurniturePaneler) tile;
                    paneler.setCustomCraftingTex(panelTextureName);
                }
                BiblioNetworking.INSTANCE.sendToServer(new BiblioPaneler(panelTextureName, message.pos)); 
            });
            return null;
        }

    }
}
