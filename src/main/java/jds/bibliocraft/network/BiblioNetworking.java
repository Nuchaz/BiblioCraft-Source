package jds.bibliocraft.network;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.network.packet.server.BiblioAtlasWPT;
import jds.bibliocraft.network.packet.server.BiblioMCBEdit;
import jds.bibliocraft.network.packet.server.BiblioMCBPage;
import jds.bibliocraft.network.packet.server.BiblioPaneler;
import jds.bibliocraft.network.packet.server.BiblioRecipeCraft;
import jds.bibliocraft.network.packet.server.BiblioType;
import jds.bibliocraft.network.packet.server.BiblioTypeDelete;
import jds.bibliocraft.network.packet.server.BiblioTypeFlag;
import jds.bibliocraft.network.packet.server.BiblioTypeUpdate;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BiblioNetworking {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BiblioCraft.MODID);
    private static int packetID = 0;
    public static void setup() {
        INSTANCE.registerMessage(BiblioAtlasWPT.Handler.class, BiblioAtlasWPT.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioType.Handler.class, BiblioType.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioTypeFlag.Handler.class, BiblioTypeFlag.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioTypeDelete.Handler.class, BiblioTypeDelete.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioTypeUpdate.Handler.class, BiblioTypeUpdate.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMCBEdit.Handler.class, BiblioMCBEdit.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMCBPage.Handler.class, BiblioMCBPage.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPaneler.Handler.class, BiblioPaneler.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioRecipeCraft.Handler.class, BiblioRecipeCraft.class, packetID++, Side.SERVER);
    }
}
