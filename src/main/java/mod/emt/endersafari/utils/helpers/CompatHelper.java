package mod.emt.endersafari.utils.helpers;

import mod.emt.endersafari.config.ESConfig;
import net.minecraftforge.fml.common.Loader;

public class CompatHelper {
    public static final boolean isEnderIOLoaded = Loader.isModLoaded("enderio") && ESConfig.MOD_INTEGRATION.enderIOIntegration;
    public static final boolean isJERLoaded = Loader.isModLoaded("jeresources") && ESConfig.MOD_INTEGRATION.JERIntegration;
    public static final boolean isThaumcraftLoaded = Loader.isModLoaded("thaumcraft") && ESConfig.MOD_INTEGRATION.thaumcraftIntegration;
}
