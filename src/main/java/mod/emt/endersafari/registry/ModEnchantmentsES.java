package mod.emt.endersafari.registry;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.enchantment.ESEnchantment;
import mod.emt.endersafari.enchantment.EnchantmentDecay;
import mod.emt.endersafari.enchantment.EnchantmentRepellent;
import mod.emt.endersafari.enchantment.EnchantmentWitherAspect;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import mod.emt.endersafari.utils.helpers.CompatHelper;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class ModEnchantmentsES {
    public static final ESEnchantment DECAY = new EnchantmentDecay("decay");
    public static final ESEnchantment REPELLENT = new EnchantmentRepellent("repellent");
    public static final ESEnchantment WITHER_ASPECT = new EnchantmentWitherAspect("wither_aspect");

    @SubscribeEvent
    public static void registerEnchantments(@Nonnull final RegistryEvent.Register<Enchantment> event) {
        final IForgeRegistry<Enchantment> registry = event.getRegistry();
        if (ESConfig.ENCHANTMENTS.DECAY.enableEnchantment) registry.register(DECAY);
        if (ESConfig.ENCHANTMENTS.REPELLENT.enableEnchantment) registry.register(REPELLENT);
        if (ESConfig.ENCHANTMENTS.WITHER_ASPECT.enableEnchantment) registry.register(WITHER_ASPECT);
    }
}
