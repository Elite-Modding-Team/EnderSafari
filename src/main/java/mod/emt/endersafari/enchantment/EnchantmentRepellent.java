package mod.emt.endersafari.enchantment;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.entity.TeleportHelper;
import mod.emt.endersafari.registry.ModEnchantmentsES;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowFire;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Random;

public class EnchantmentRepellent extends ESEnchantment {
    public EnchantmentRepellent(String name) {
        super(name, ESConfig.ENCHANTMENTS.REPELLENT.rarity, EnumEnchantmentType.ARMOR, EntityEquipmentSlot.values());
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return super.canApply(stack);
    }

    @Override
    public int getMaxLevel() {
        return ESConfig.ENCHANTMENTS.REPELLENT.maxLevel;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return ESConfig.ENCHANTMENTS.REPELLENT.minEnchantability + 20 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + ESConfig.ENCHANTMENTS.REPELLENT.maxEnchantability;
    }

    @Override
    public void onUserHurt(@NotNull EntityLivingBase entity, @NotNull Entity attacker, int level) {
        ItemStack stack = EnchantmentHelper.getEnchantedItem(ModEnchantmentsES.REPELLENT, entity);

        if (shouldHit(level, entity.getRNG())) {
            if (attacker instanceof EntityLivingBase && attacker.isNonBoss() && !entity.world.isRemote) {
                EntityLivingBase livingAttacker = (EntityLivingBase) attacker;
                TeleportHelper.teleportEntity(entity.world, livingAttacker, false, true, ESConfig.ENCHANTMENTS.REPELLENT.repellentTeleportRangeBase + level * ESConfig.ENCHANTMENTS.REPELLENT.repellentTeleportRangeScale);
            }

            if (!stack.isEmpty()) {
                damageArmor(stack, 2, entity);
            }
        }
    }

    public static boolean shouldHit(int level, Random rnd) {
        if (level <= 0) {
            return false;
        } else {
            return rnd.nextFloat() < (float) ESConfig.ENCHANTMENTS.REPELLENT.repellentChancePerLevel * (float) level;
        }
    }

    private void damageArmor(ItemStack stack, int amount, EntityLivingBase entity) {
        int slot = -1;
        int x = 0;

        for (ItemStack i : entity.getArmorInventoryList()) {
            if (i == stack) {
                slot = x;
                break;
            }
            x++;
        }

        if (slot == -1 || !(stack.getItem() instanceof ISpecialArmor)) {
            stack.damageItem(1, entity);
            return;
        }

        ISpecialArmor armor = (ISpecialArmor) stack.getItem();
        armor.damageArmor(entity, stack, DamageSource.causeThornsDamage(entity), amount, slot);
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
        return super.canApplyAtEnchantingTable(stack);
    }

    @Override
    protected boolean canApplyTogether(@Nonnull Enchantment ench) {
        return super.canApplyTogether(ench) && !(ench instanceof EnchantmentArrowFire);
    }
}