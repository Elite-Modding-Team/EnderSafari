package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class ModLootTablesEZ {
    public static final ResourceLocation CONCUSSION_CREEPER = new ResourceLocation(EnderSafari.MOD_ID, "entities/concussion_creeper");
    public static final ResourceLocation DIRE_CUBE = new ResourceLocation(EnderSafari.MOD_ID, "entities/dire_cube");
    public static final ResourceLocation DIRE_WOLF = new ResourceLocation(EnderSafari.MOD_ID, "entities/dire_wolf");
    public static final ResourceLocation ENDERIZED_ZOMBIE = new ResourceLocation(EnderSafari.MOD_ID, "entities/enderized_zombie");
    public static final ResourceLocation EPIC_SQUID = new ResourceLocation(EnderSafari.MOD_ID, "entities/epic_squid");
    public static final ResourceLocation OWL = new ResourceLocation(EnderSafari.MOD_ID, "entities/owl");
    public static final ResourceLocation VOID_CUBE = new ResourceLocation(EnderSafari.MOD_ID, "entities/void_cube");
    public static final ResourceLocation WITCH_CAT = new ResourceLocation(EnderSafari.MOD_ID, "entities/witch_cat");
    public static final ResourceLocation WITHER_WITCH = new ResourceLocation(EnderSafari.MOD_ID, "entities/wither_witch");

    public static void registerLootTables() {
        LootTableList.register(CONCUSSION_CREEPER);
        LootTableList.register(DIRE_CUBE);
        LootTableList.register(DIRE_WOLF);
        LootTableList.register(ENDERIZED_ZOMBIE);
        LootTableList.register(EPIC_SQUID);
        LootTableList.register(OWL);
        LootTableList.register(VOID_CUBE);
        LootTableList.register(WITCH_CAT);
        LootTableList.register(WITHER_WITCH);
    }
}
