package mod.emt.endersafari.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.potion.PotionConfusion;
import crazypants.enderio.base.potion.PotionFloating;
import crazypants.enderio.base.potion.PotionWithering;
import mod.emt.endersafari.registry.ModItemsES;
import mod.emt.endersafari.registry.ModLootTablesES;
import mod.emt.endersafari.registry.ModPotionsES;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EnderIOIntegration {
    public static void registerBrewingRecipes() {
        PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsES.OWL_EGG, PotionFloating.getFloating());
        PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsES.CONFUSING_DUST, PotionConfusion.getConfusion());
        PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsES.WITHERING_DUST, PotionWithering.getWithering());
        PotionHelper.addMix(ModPotionsES.LEVITATION_POTION, Items.REDSTONE, PotionFloating.getFloatinglong());
        PotionHelper.addMix(ModPotionsES.LEVITATION_POTION, Items.GLOWSTONE_DUST, PotionFloating.getFloatingstrong());
        PotionHelper.addMix(ModPotionsES.NAUSEA_POTION, Items.REDSTONE, PotionConfusion.getConfusionlong());
        PotionHelper.addMix(ModPotionsES.WITHER_POTION, Items.REDSTONE, PotionWithering.getWitheringlong());
    }

    // Adjust loot table drops if EnderIO is detected
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        if (name.equals(ModLootTablesES.ENDERIZED_ZOMBIE)) {
            LootPool rare = event.getTable().getPool("rare");

            if (rare != null) {
                Item nugget = getItem(EnderIO.MODID, "item_alloy_nugget");
                Item ingot = getItem(EnderIO.MODID, "item_alloy_ingot");
                Item food = getItem(EnderIO.MODID, "item_ender_food");
                Item skull = getItem(EnderIO.MODID, "block_enderman_skull");
                Item pearl = getItem("minecraft", "ender_pearl");
                LootFunction looting = new LootingEnchantBonus(new LootCondition[0], new RandomValueRange(0, 1), 0);
                LootFunction meta = new SetMetadata(new LootCondition[0], new RandomValueRange(6, 6));

                rare.removeEntry("carrot");
                rare.removeEntry("gold_ingot");
                rare.removeEntry("potato");

                if (nugget != null)
                    rare.addEntry(new LootEntryItem(nugget, 1, 0, new LootFunction[]{meta}, new LootCondition[0], "dark_steel_nugget_enderio"));
                if (ingot != null)
                    rare.addEntry(new LootEntryItem(ingot, 1, 0, new LootFunction[]{meta}, new LootCondition[0], "dark_steel_ingot_enderio"));
                if (food != null)
                    rare.addEntry(new LootEntryItem(food, 1, 0, new LootFunction[0], new LootCondition[0], "enderios_enderio"));
                if (skull != null)
                    rare.addEntry(new LootEntryItem(skull, 1, 0, new LootFunction[0], new LootCondition[0], "enderman_skull_enderio"));
                if (pearl != null) {
                    rare.addEntry(new LootEntryItem(pearl, 1, 0, new LootFunction[]{createEnchantRandomly(EnderIO.MODID + ":shimmer"), looting}, new LootCondition[0], "shimmer_ender_pearl_enderio"));
                    rare.addEntry(new LootEntryItem(pearl, 1, 0, new LootFunction[]{createEnchantRandomly(EnderIO.MODID + ":soulbound"), looting}, new LootCondition[0], "soulbound_ender_pearl_enderio"));
                }
            }
        }

        if (name.equals(ModLootTablesES.VOID_CUBE)) {
            LootPool main = event.getTable().getPool("main");
            Item air = getItem("minecraft", "air");

            if (main != null && air != null) {
                main.addEntry(new LootEntryItem(air, 1, 0, new LootFunction[]{createUseThings("dustBedrock")}, new LootCondition[0], "bedrock_dust_enderio"));
            }
        }
    }

    private static Item getItem(String modid, String name) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, name));
    }

    private static LootFunction createUseThings(String oreDictName) {
        JsonObject json = new JsonObject();
        json.addProperty("name", oreDictName);
        return LootFunctionManager.getSerializerForName(new ResourceLocation(EnderIO.MODID, "use_things")).deserialize(json, null, new LootCondition[0]);
    }

    private static LootFunction createEnchantRandomly(String enchantId) {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        array.add(enchantId);
        json.add("enchantments", array);
        return LootFunctionManager.getSerializerForName(new ResourceLocation("minecraft", "enchant_randomly")).deserialize(json, null, new LootCondition[0]);
    }
}
