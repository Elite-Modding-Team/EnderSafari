package mod.emt.enderzoo.proxy;

import mod.emt.enderzoo.compat.JERIntegration;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import utils.helpers.CompatHelper;

public class CommonProxy {
    public void preInit() {
    }

    public void init() {
        ModLootTablesEZ.registerLootTables();

        if (CompatHelper.isJERLoaded) {
            JERIntegration.init();
        }
    }

    public void postInit() {
    }
}
