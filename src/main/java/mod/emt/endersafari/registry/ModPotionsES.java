package mod.emt.endersafari.registry;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.effect.MobEffectDisplacement;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class ModPotionsES {
    // Potions
    public static PotionType DISPLACEMENT_POTION_STRONG;
    public static PotionType DISPLACEMENT_POTION;
    public static PotionType LEVITATION_POTION_LONG;
    public static PotionType LEVITATION_POTION_STRONG;
    public static PotionType LEVITATION_POTION;
    public static PotionType NAUSEA_POTION_LONG;
    public static PotionType NAUSEA_POTION;
    public static PotionType WITHER_POTION_LONG;
    public static PotionType WITHER_POTION_STRONG;
    public static PotionType WITHER_POTION;

    // Potion Effects
    public static Potion DISPLACEMENT;

    private static Potion registerPotion(String name, Potion potion) {
        return potion.setRegistryName(new ResourceLocation(EnderSafari.MOD_ID, name)).
                setPotionName("effect." + EnderSafari.MOD_ID + "." + name + ".name");
    }

    private static PotionType registerPotionType(String name, PotionType potionType) {
        potionType.setRegistryName(new ResourceLocation(EnderSafari.MOD_ID, name));
        return potionType;
    }

    @SubscribeEvent
    public static void registerPotionTypes(@Nonnull final RegistryEvent.Register<PotionType> event) {
        IForgeRegistry<PotionType> registry = event.getRegistry();

        DISPLACEMENT_POTION = registerPotionType("displacement", new PotionType("es_displacement",
                new PotionEffect(DISPLACEMENT, 1, 0)));
        DISPLACEMENT_POTION_STRONG = registerPotionType("displacement_strong", new PotionType("es_displacement",
                new PotionEffect(DISPLACEMENT, 1, 1)));
        LEVITATION_POTION = registerPotionType("levitation", new PotionType("es_levitation",
                new PotionEffect(MobEffects.LEVITATION, 900, 0)));
        LEVITATION_POTION_LONG = registerPotionType("levitation_long", new PotionType("es_levitation",
                new PotionEffect(MobEffects.LEVITATION, 1800, 0)));
        LEVITATION_POTION_STRONG = registerPotionType("levitation_strong", new PotionType("es_levitation",
                new PotionEffect(MobEffects.LEVITATION, 450, 1)));
        NAUSEA_POTION = registerPotionType("nausea", new PotionType("es_nausea",
                new PotionEffect(MobEffects.NAUSEA, 900, 0)));
        NAUSEA_POTION_LONG = registerPotionType("nausea_long", new PotionType("es_nausea",
                new PotionEffect(MobEffects.NAUSEA, 1800, 0)));
        WITHER_POTION = registerPotionType("wither", new PotionType("es_wither",
                new PotionEffect(MobEffects.WITHER, 900, 0)));
        WITHER_POTION_LONG = registerPotionType("wither_long", new PotionType("es_wither",
                new PotionEffect(MobEffects.WITHER, 1800, 0)));
        WITHER_POTION_STRONG = registerPotionType("wither_strong", new PotionType("es_wither",
                new PotionEffect(MobEffects.WITHER, 450, 1)));

        if (!ESConfig.POTIONS.disablePotionDisplacement) {
            registry.registerAll(
                    DISPLACEMENT_POTION,
                    DISPLACEMENT_POTION_STRONG
            );
        }

        if (!ESConfig.POTIONS.disablePotionDecay) {
            registry.registerAll(
                    WITHER_POTION,
                    WITHER_POTION_LONG,
                    WITHER_POTION_STRONG
            );
        }

        if (!ESConfig.POTIONS.disablePotionConfusion) {
            registry.registerAll(
                    NAUSEA_POTION,
                    NAUSEA_POTION_LONG
            );
        }

        if (!ESConfig.POTIONS.disablePotionLifting) {
            registry.registerAll(
                    LEVITATION_POTION,
                    LEVITATION_POTION_LONG,
                    LEVITATION_POTION_STRONG
            );
        }
    }

    @SubscribeEvent
    public static void registerPotions(@Nonnull final RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> registry = event.getRegistry();
        DISPLACEMENT = registerPotion("displacement", new MobEffectDisplacement("displacement", true, 5578058));
        registry.registerAll(
                DISPLACEMENT
        );
    }
}
