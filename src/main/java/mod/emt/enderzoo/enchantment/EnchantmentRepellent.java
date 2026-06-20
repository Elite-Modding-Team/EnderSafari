package mod.emt.enderzoo.enchantment;

import mod.emt.enderzoo.entity.TeleportHelper;
import mod.emt.enderzoo.registry.ModEnchantmentsEZ;
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

public class EnchantmentRepellent extends EZEnchantment {
    public EnchantmentRepellent(String name) {
        super(name, Rarity.VERY_RARE, EnumEnchantmentType.ARMOR, EntityEquipmentSlot.values());
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return super.canApply(stack);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + 20 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public void onUserHurt(@NotNull EntityLivingBase entity, @NotNull Entity attacker, int level) {
        ItemStack stack = EnchantmentHelper.getEnchantedItem(ModEnchantmentsEZ.REPELLENT, entity);

        if (shouldHit(level, entity.getRNG())) {
            if (attacker instanceof EntityLivingBase && attacker.isNonBoss() && !entity.world.isRemote) {
                EntityLivingBase livingAttacker = (EntityLivingBase) attacker;
                TeleportHelper.teleportRandomly(livingAttacker, 16 + level * 8);
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
            return rnd.nextFloat() < 0.15F * (float) level;
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