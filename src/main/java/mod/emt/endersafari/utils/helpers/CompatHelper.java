package mod.emt.endersafari.utils.helpers;

import net.minecraftforge.fml.common.Loader;

public class CompatHelper {
    public static final boolean isEnderIOLoaded = Loader.isModLoaded("enderio");
    public static final boolean isJERLoaded = Loader.isModLoaded("jeresources");
    public static final boolean isThaumcraftLoaded = Loader.isModLoaded("thaumcraft");
}
