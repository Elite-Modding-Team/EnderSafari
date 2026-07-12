package mod.emt.endersafari;

import mod.emt.endersafari.proxy.CommonProxy;
import mod.emt.endersafari.registry.CreativeTabsES;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = EnderSafari.MOD_ID,
        name = EnderSafari.MOD_NAME,
        version = EnderSafari.MOD_VERSION
)
public class EnderSafari {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.VERSION;

    public static final String CLIENT_PROXY = "mod.emt.endersafari.proxy.ClientProxy";
    public static final String COMMON_PROXY = "mod.emt.endersafari.proxy.CommonProxy";

    public static final CreativeTabs tabEZ = new CreativeTabsES(CreativeTabs.CREATIVE_TAB_ARRAY.length, "EnderSafariTab");

    @Mod.Instance(MOD_ID)
    public static EnderSafari instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
