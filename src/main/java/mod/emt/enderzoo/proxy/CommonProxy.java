package mod.emt.enderzoo.proxy;

import mod.emt.enderzoo.compat.EnderIOIntegration;
import mod.emt.enderzoo.compat.JERIntegration;
import mod.emt.enderzoo.registry.ModEntitiesEZ;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraftforge.common.MinecraftForge;
import utils.helpers.CompatHelper;

public class CommonProxy {
    public void preInit() {
    }

    public void init() {
        ModEntitiesEZ.registerEntitySpawns();
        ModLootTablesEZ.registerLootTables();

        if (CompatHelper.isEnderIOLoaded) {
            MinecraftForge.EVENT_BUS.register(new EnderIOIntegration());
        }

        if (CompatHelper.isJERLoaded) {
            JERIntegration.init();
        }
    }

    public void postInit() {
    }
}
