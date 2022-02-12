package jds.bibliocraft.network;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.network.packet.client.BiblioAtlasClient;
import jds.bibliocraft.network.packet.client.BiblioAtlasSWPClient;
import jds.bibliocraft.network.packet.client.BiblioAtlasTGUI;
import jds.bibliocraft.network.packet.client.BiblioDeskOpenGui;
import jds.bibliocraft.network.packet.client.BiblioDrillText;
import jds.bibliocraft.network.packet.client.BiblioOpenBook;
import jds.bibliocraft.network.packet.client.BiblioPanelerClient;
import jds.bibliocraft.network.packet.client.BiblioRecipeText;
import jds.bibliocraft.network.packet.client.BiblioSoundPlayer;
import jds.bibliocraft.network.packet.client.BiblioStockLog;
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
public class BiblioNetworking 
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BiblioCraft.MODID);
    private static int packetId = 0;
    public static void setup() 
    {
        INSTANCE.registerMessage(BiblioAtlasWPT.Handler.class, BiblioAtlasWPT.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioType.Handler.class, BiblioType.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioTypeFlag.Handler.class, BiblioTypeFlag.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioTypeDelete.Handler.class, BiblioTypeDelete.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioTypeUpdate.Handler.class, BiblioTypeUpdate.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMCBEdit.Handler.class, BiblioMCBEdit.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMCBPage.Handler.class, BiblioMCBPage.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPaneler.Handler.class, BiblioPaneler.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioRecipeCraft.Handler.class, BiblioRecipeCraft.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioStockTitle.Handler.class, BiblioStockTitle.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioStockCompass.Handler.class, BiblioStockCompass.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioClipboard.Handler.class, BiblioClipboard.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioUpdateInv.Handler.class, BiblioUpdateInv.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioAtlas.Handler.class, BiblioAtlas.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMeasure.Handler.class, BiblioMeasure.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioMapPin.Handler.class, BiblioMapPin.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioRBook.Handler.class, BiblioRBook.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioRBookLoad.Handler.class, BiblioRBookLoad.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioSign.Handler.class, BiblioSign.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioClock.Handler.class, BiblioClock.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPaintPress.Handler.class, BiblioPaintPress.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPainting.Handler.class, BiblioPainting.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(BiblioPaintingC.Handler.class, BiblioPaintingC.class, packetId++, Side.SERVER);
        
        INSTANCE.registerMessage(BiblioDrillText.Handler.class, BiblioDrillText.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioAtlasClient.Handler.class, BiblioAtlasClient.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioPanelerClient.Handler.class, BiblioPanelerClient.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioRecipeText.Handler.class, BiblioRecipeText.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioStockLog.Handler.class, BiblioStockLog.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioOpenBook.Handler.class, BiblioOpenBook.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioDeskOpenGui.Handler.class, BiblioDeskOpenGui.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioAtlasSWPClient.Handler.class, BiblioAtlasSWPClient.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(BiblioAtlasTGUI.Handler.class, BiblioAtlasTGUI.class, packetId++, Side.CLIENT);
        
        INSTANCE.registerMessage(BiblioSoundPlayer.Handler.class, BiblioSoundPlayer.class, packetId++, Side.CLIENT);
    }

}
