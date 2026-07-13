package mod.emt.endersafari.enchantment;

import mod.emt.endersafari.config.ESConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentFireAspect;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class EnchantmentWitherAspect extends ESEnchantment {
    public EnchantmentWitherAspect(String name) {
        super(name, ESConfig.ENCHANTMENTS.WITHER_ASPECT.rarity, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return super.canApply(stack);
    }

    @Override
    public int getMaxLevel() {
        return ESConfig.ENCHANTMENTS.WITHER_ASPECT.maxLevel;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return ESConfig.ENCHANTMENTS.WITHER_ASPECT.minEnchantability + 20 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + ESConfig.ENCHANTMENTS.WITHER_ASPECT.maxEnchantability;
    }

    @Override
    public void onEntityDamaged(@NotNull EntityLivingBase entity, @NotNull Entity target, int level) {
        if (target instanceof EntityLivingBase && level > 0) {
            EntityLivingBase livingTarget = (EntityLivingBase) target;
            PotionEffect witherEffect = new PotionEffect(MobEffects.WITHER, 20 * (ESConfig.ENCHANTMENTS.WITHER_ASPECT.duration * level), 0);
            if (livingTarget.isPotionApplicable(witherEffect)) {
                livingTarget.addPotionEffect(witherEffect);
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
        return super.canApplyAtEnchantingTable(stack);
    }

    @Override
    protected boolean canApplyTogether(@Nonnull Enchantment ench) {
        return super.canApplyTogether(ench) && !(ench instanceof EnchantmentFireAspect);
    }
}