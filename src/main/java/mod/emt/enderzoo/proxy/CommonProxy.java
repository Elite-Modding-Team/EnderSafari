package mod.emt.enderzoo.proxy;

import mod.emt.enderzoo.registry.ModLootTablesEZ;

public class CommonProxy {
    public void preInit() {
    }

    public void init() {
        ModLootTablesEZ.registerLootTables();
    }

    public void postInit() {
    }
}
