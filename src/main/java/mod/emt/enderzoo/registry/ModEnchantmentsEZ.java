package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.enchantment.EZEnchantment;
import mod.emt.enderzoo.enchantment.EnchantmentDecay;
import mod.emt.enderzoo.enchantment.EnchantmentRepellent;
import mod.emt.enderzoo.enchantment.EnchantmentWitherAspect;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import utils.helpers.CompatHelper;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class ModEnchantmentsEZ {
    public static final EZEnchantment DECAY = new EnchantmentDecay("decay");
    public static final EZEnchantment REPELLENT = new EnchantmentRepellent("repellent");
    public static final EZEnchantment WITHER_ASPECT = new EnchantmentWitherAspect("wither_aspect");

    @SubscribeEvent
    public static void registerEnchantments(@Nonnull final RegistryEvent.Register<Enchantment> event) {
        final IForgeRegistry<Enchantment> registry = event.getRegistry();
        // TODO: Make it where each enchantment can be disabled
        if (!CompatHelper.isEnderIOLoaded) {
            registry.register(DECAY);
            registry.register(REPELLENT);
            registry.register(WITHER_ASPECT);
        }
    }
}
