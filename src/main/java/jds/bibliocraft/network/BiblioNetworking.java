package jds.bibliocraft.network;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.network.packet.BiblioAtlasWPT;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BiblioNetworking {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BiblioCraft.MODID);
    private static int packetID = 0;
    public static void setup() {
        INSTANCE.registerMessage(BiblioAtlasWPT.BiblioAtlasWPTHandler.class, BiblioAtlasWPT.class, packetID++, Side.SERVER);
    }
}
