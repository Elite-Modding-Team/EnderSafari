package mod.emt.endersafari.network;

import mod.emt.endersafari.EnderSafari;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ESPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(EnderSafari.MOD_ID);
    private static int id = 0;

    public static void registerMessages() {
        INSTANCE.registerMessage(MessageFireballImpactFX.MessageHolder.class, MessageFireballImpactFX.class, id++, Side.CLIENT);
    }
}
