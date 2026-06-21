package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class ModLootTablesEZ {
    public static final ResourceLocation CONCUSSION_CREEPER = new ResourceLocation(EnderSafari.MOD_ID, "entities/concussion_creeper");

    public static void registerLootTables() {
        LootTableList.register(CONCUSSION_CREEPER);
    }
}
