package mod.emt.enderzoo.compat;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.registry.ModBlocksEZ;
import mod.emt.enderzoo.registry.ModItemsEZ;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

@SuppressWarnings("deprecation")
public class ThaumcraftIntegration {
    @SubscribeEvent
    public void registerAspects(AspectRegistryEvent event) {
        AspectEventProxy proxy = event.register;

        // Entities
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "concussion_creeper", new AspectList().add(Aspect.PLANT, 10).add(Aspect.MIND, 10).add(Aspect.MOTION, 10));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "dire_cube", new AspectList().add(Aspect.LIFE, 10).add(Aspect.EARTH, 10).add(Aspect.ALCHEMY, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "dire_wolf", new AspectList().add(Aspect.BEAST, 15).add(Aspect.EARTH, 10).add(Aspect.AVERSION, 10));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "enderized_zombie", new AspectList().add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.ELDRITCH, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "enderminy", new AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.MOTION, 20).add(Aspect.AVERSION, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "epic_squid", new AspectList().add(Aspect.BEAST, 5).add(Aspect.AURA, 5).add(Aspect.FIRE, 10));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "fallen_knight", new AspectList().add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.PROTECT, 5).add(Aspect.AVERSION, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "fallen_steed", new AspectList().add(Aspect.BEAST, 10).add(Aspect.UNDEAD, 5).add(Aspect.PROTECT, 5).add(Aspect.AIR, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "owl", new AspectList().add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.SENSES, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "owl_egg", new AspectList().add(Aspect.LIFE, 5).add(Aspect.MOTION, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "primed_concussion_charge", new AspectList().add(Aspect.MIND, 10).add(Aspect.ELDRITCH, 10).add(Aspect.FIRE, 15).add(Aspect.MOTION, 10));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "primed_confusing_charge", new AspectList().add(Aspect.MIND, 10).add(Aspect.FIRE, 15).add(Aspect.MOTION, 10));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "primed_ender_charge", new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.FIRE, 15).add(Aspect.MOTION, 15));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "void_cube", new AspectList().add(Aspect.DARKNESS, 20).add(Aspect.ELDRITCH, 15).add(Aspect.TRAP, 10).add(Aspect.ALCHEMY, 5));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "witch_cat", new AspectList().add(Aspect.BEAST, 10).add(Aspect.ENTROPY, 10).add(Aspect.AVERSION, 15));
        ThaumcraftApi.registerEntityTag(EnderSafari.MOD_ID + "." + "wither_witch", new AspectList().add(Aspect.MAN, 15).add(Aspect.MAGIC, 5).add(Aspect.ENTROPY, 5).add(Aspect.ALCHEMY, 10));

        // Items
        proxy.registerObjectTag(new ItemStack(ModItemsEZ.CONFUSING_DUST), new AspectList().add(Aspect.ALCHEMY, 5).add(Aspect.MIND, 10).add(Aspect.MOTION, 10));
        proxy.registerObjectTag(new ItemStack(ModItemsEZ.ENDER_FRAGMENT), new AspectList().add(Aspect.ELDRITCH, 2).add(Aspect.MOTION, 3));
        proxy.registerObjectTag(new ItemStack(ModItemsEZ.EPIC_INK_SAC), new AspectList().add(Aspect.AURA, 2).add(Aspect.FIRE, 2).add(Aspect.SENSES, 5));
        proxy.registerObjectTag(new ItemStack(ModItemsEZ.GUARDIAN_BOW), new AspectList().add(Aspect.AVERSION, 20).add(Aspect.FLIGHT, 10).add(Aspect.PROTECT, 10));
        proxy.registerObjectTag(new ItemStack(ModItemsEZ.OWL_EGG), new AspectList().add(Aspect.FLIGHT, 5).add(Aspect.LIFE, 5));
        proxy.registerObjectTag(new ItemStack(ModItemsEZ.WITHERING_DUST), new AspectList().add(Aspect.ALCHEMY, 5).add(Aspect.DEATH, 10).add(Aspect.ENTROPY, 10));
    }
}
