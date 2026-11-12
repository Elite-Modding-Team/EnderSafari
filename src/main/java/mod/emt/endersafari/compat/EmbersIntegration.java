package mod.emt.endersafari.compat;

import mod.emt.endersafari.registry.ModLootTablesES;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

// Embers Unofficial Extended Life messed around with the registration apparently...
public class EmbersIntegration {
    // Add new loot table pools and drops if Embers is detected
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        if (name.equals(ModLootTablesES.ANDESITE_GOLEM) || name.equals(ModLootTablesES.DIORITE_GOLEM) || name.equals(ModLootTablesES.END_STONE_GOLEM) ||
                name.equals(ModLootTablesES.GRANITE_GOLEM) || name.equals(ModLootTablesES.NETHERRACK_GOLEM) || name.equals(ModLootTablesES.OBSIDIAN_GOLEM) ||
                name.equals(ModLootTablesES.STONE_GOLEM)) {
            LootPool main = new LootPool(new LootEntry[0], new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "embers_main");
            LootPool main2 = new LootPool(new LootEntry[0], new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "embers_main2");
            Item brick = getItem("embers", "archaic_brick");
            Item core = getItem("embers", "ancient_motive_core");

            if (brick != null) {
                main.addEntry(new LootEntryItem(brick, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(4, 11))}, new LootCondition[0], "embers_archaic_brick"));
            }

            if (core != null) {
                main2.addEntry(new LootEntryItem(core, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 1))}, new LootCondition[0], "embers_ancient_motive_core"));
            }

            event.getTable().addPool(main);
            event.getTable().addPool(main2);
        }
    }

    private static Item getItem(String modid, String name) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, name));
    }
}