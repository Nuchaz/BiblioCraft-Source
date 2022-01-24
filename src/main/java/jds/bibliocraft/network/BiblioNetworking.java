package jds.bibliocraft.network;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.network.packet.server.BiblioAtlas;
import jds.bibliocraft.network.packet.server.BiblioAtlasWPT;
import jds.bibliocraft.network.packet.server.BiblioClipboard;
import jds.bibliocraft.network.packet.server.BiblioClock;
import jds.bibliocraft.network.packet.server.BiblioMCBEdit;
import jds.bibliocraft.network.packet.server.BiblioMCBPage;
import jds.bibliocraft.network.packet.server.BiblioMapPin;
import jds.bibliocraft.network.packet.server.BiblioMeasure;
import jds.bibliocraft.network.packet.server.BiblioPaintPress;
import jds.bibliocraft.network.packet.server.BiblioPainting;
import jds.bibliocraft.network.packet.server.BiblioPaintingC;
import jds.bibliocraft.network.packet.server.BiblioPaneler;
import jds.bibliocraft.network.packet.server.BiblioRBook;
import jds.bibliocraft.network.packet.server.BiblioRBookLoad;
import jds.bibliocraft.network.packet.server.BiblioRecipeCraft;
import jds.bibliocraft.network.packet.server.BiblioSign;
import jds.bibliocraft.network.packet.server.BiblioStockCompass;
import jds.bibliocraft.network.packet.server.BiblioStockTitle;
import jds.bibliocraft.network.packet.server.BiblioType;
import jds.bibliocraft.network.packet.server.BiblioTypeDelete;
import jds.bibliocraft.network.packet.server.BiblioTypeFlag;
import jds.bibliocraft.network.packet.server.BiblioTypeUpdate;
import jds.bibliocraft.network.packet.server.BiblioUpdateInv;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Networking manager class
 * 
 * @author Exopteron
 */
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
        INSTANCE.registerMessage(BiblioStockTitle.Handler.class, BiblioStockTitle.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioStockCompass.Handler.class, BiblioStockCompass.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioClipboard.Handler.class, BiblioClipboard.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioUpdateInv.Handler.class, BiblioUpdateInv.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioAtlas.Handler.class, BiblioAtlas.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMeasure.Handler.class, BiblioMeasure.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMapPin.Handler.class, BiblioMapPin.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioRBook.Handler.class, BiblioRBook.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioRBookLoad.Handler.class, BiblioRBookLoad.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioSign.Handler.class, BiblioSign.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioClock.Handler.class, BiblioClock.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPaintPress.Handler.class, BiblioPaintPress.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPainting.Handler.class, BiblioPainting.class, packetID++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPaintingC.Handler.class, BiblioPaintingC.class, packetID++, Side.SERVER);
    }
}
