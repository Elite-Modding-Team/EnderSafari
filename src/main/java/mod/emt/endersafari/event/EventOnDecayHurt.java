package mod.emt.endersafari.event;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.registry.ModEnchantmentsES;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class EventOnDecayHurt {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDecayHurt(LivingHurtEvent event) {
        EntityLivingBase entity = event.getEntityLiving();

        if (event.getSource().getImmediateSource() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getSource().getImmediateSource();

            if (arrow.shootingEntity instanceof EntityLivingBase) {
                EntityLivingBase shooter = (EntityLivingBase) arrow.shootingEntity;
                ItemStack bow = shooter.getHeldItemMainhand();
                int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantmentsES.DECAY, bow);

                if (!bow.isEmpty() && level > 0) {
                    PotionEffect witherEffect = new PotionEffect(MobEffects.WITHER, 20 * (ESConfig.ENCHANTMENTS.DECAY.duration * level), 0);
                    if (entity.isPotionApplicable(witherEffect)) {
                        entity.addPotionEffect(witherEffect);
                    }
                }
            }
        }
    }
}
