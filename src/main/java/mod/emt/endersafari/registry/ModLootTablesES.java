package mod.emt.endersafari.registry;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class ModLootTablesES {
    public static final ResourceLocation BLAZE_COW = new ResourceLocation(EnderSafari.MOD_ID, "entities/blaze_cow");
    public static final ResourceLocation CONCUSSION_CREEPER = new ResourceLocation(EnderSafari.MOD_ID, "entities/concussion_creeper");
    public static final ResourceLocation CRYSTAL_SKELETON = new ResourceLocation(EnderSafari.MOD_ID, "entities/crystal_skeleton");
    public static final ResourceLocation DIRE_CUBE = new ResourceLocation(EnderSafari.MOD_ID, "entities/dire_cube");
    public static final ResourceLocation DIRE_WOLF = new ResourceLocation(EnderSafari.MOD_ID, "entities/dire_wolf");
    public static final ResourceLocation ENDER_CHICKEN = new ResourceLocation(EnderSafari.MOD_ID, "entities/ender_chicken");
    public static final ResourceLocation ENDERIZED_ZOMBIE = new ResourceLocation(EnderSafari.MOD_ID, "entities/enderized_zombie");
    public static final ResourceLocation ENDERMINY = new ResourceLocation(EnderSafari.MOD_ID, "entities/enderminy");
    public static final ResourceLocation EPIC_SQUID = new ResourceLocation(EnderSafari.MOD_ID, "entities/epic_squid");
    public static final ResourceLocation FAIRY = new ResourceLocation(EnderSafari.MOD_ID, "entities/fairy");
    public static final ResourceLocation FALLEN_KNIGHT = new ResourceLocation(EnderSafari.MOD_ID, "entities/fallen_knight");
    public static final ResourceLocation FALLEN_STEED = new ResourceLocation(EnderSafari.MOD_ID, "entities/fallen_steed");
    public static final ResourceLocation OWL = new ResourceLocation(EnderSafari.MOD_ID, "entities/owl");
    public static final ResourceLocation SHEEPER = new ResourceLocation(EnderSafari.MOD_ID, "entities/sheeper");
    public static final ResourceLocation VOID_CUBE = new ResourceLocation(EnderSafari.MOD_ID, "entities/void_cube");
    public static final ResourceLocation WITCH_CAT = new ResourceLocation(EnderSafari.MOD_ID, "entities/witch_cat");
    public static final ResourceLocation WITHER_WITCH = new ResourceLocation(EnderSafari.MOD_ID, "entities/wither_witch");

    // Golems
    public static final ResourceLocation ANDESITE_GOLEM = new ResourceLocation(EnderSafari.MOD_ID, "entities/golem/andesite_golem");
    public static final ResourceLocation DIORITE_GOLEM = new ResourceLocation(EnderSafari.MOD_ID, "entities/golem/diorite_golem");
    public static final ResourceLocation END_STONE_GOLEM = new ResourceLocation(EnderSafari.MOD_ID, "entities/golem/end_stone_golem");
    public static final ResourceLocation GRANITE_GOLEM = new ResourceLocation(EnderSafari.MOD_ID, "entities/golem/granite_golem");
    public static final ResourceLocation NETHERRACK_GOLEM = new ResourceLocation(EnderSafari.MOD_ID, "entities/golem/netherrack_golem");
    public static final ResourceLocation OBSIDIAN_GOLEM = new ResourceLocation(EnderSafari.MOD_ID, "entities/golem/obsidian_golem");
    public static final ResourceLocation STONE_GOLEM = new ResourceLocation(EnderSafari.MOD_ID, "entities/golem/stone_golem");

    public static void registerLootTables() {
        LootTableList.register(ANDESITE_GOLEM);
        LootTableList.register(BLAZE_COW);
        LootTableList.register(CONCUSSION_CREEPER);
        LootTableList.register(DIORITE_GOLEM);
        LootTableList.register(DIRE_CUBE);
        LootTableList.register(DIRE_WOLF);
        LootTableList.register(ENDER_CHICKEN);
        LootTableList.register(ENDERIZED_ZOMBIE);
        LootTableList.register(ENDERMINY);
        LootTableList.register(END_STONE_GOLEM);
        LootTableList.register(EPIC_SQUID);
        LootTableList.register(FAIRY);
        LootTableList.register(FALLEN_KNIGHT);
        LootTableList.register(FALLEN_STEED);
        LootTableList.register(GRANITE_GOLEM);
        LootTableList.register(OWL);
        LootTableList.register(NETHERRACK_GOLEM);
        LootTableList.register(OBSIDIAN_GOLEM);
        LootTableList.register(SHEEPER);
        LootTableList.register(STONE_GOLEM);
        LootTableList.register(VOID_CUBE);
        LootTableList.register(WITCH_CAT);
        LootTableList.register(WITHER_WITCH);
    }
}
