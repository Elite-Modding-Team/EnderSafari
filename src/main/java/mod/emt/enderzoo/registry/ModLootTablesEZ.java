package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class ModLootTablesEZ {
    public static final ResourceLocation CONCUSSION_CREEPER = new ResourceLocation(EnderSafari.MOD_ID, "entities/concussion_creeper");
    public static final ResourceLocation DIRE_CUBE = new ResourceLocation(EnderSafari.MOD_ID, "entities/dire_cube");
    public static final ResourceLocation ENDERIZED_ZOMBIE = new ResourceLocation(EnderSafari.MOD_ID, "entities/enderized_zombie");
    public static final ResourceLocation EPIC_SQUID = new ResourceLocation(EnderSafari.MOD_ID, "entities/epic_squid");
    public static final ResourceLocation VOID_CUBE = new ResourceLocation(EnderSafari.MOD_ID, "entities/void_cube");

    public static void registerLootTables() {
        LootTableList.register(CONCUSSION_CREEPER);
        LootTableList.register(DIRE_CUBE);
        LootTableList.register(ENDERIZED_ZOMBIE);
        LootTableList.register(EPIC_SQUID);
        LootTableList.register(VOID_CUBE);
    }
}
