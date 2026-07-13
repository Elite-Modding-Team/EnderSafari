package mod.emt.endersafari.enchantment;

import mod.emt.endersafari.config.ESConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowFire;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class EnchantmentDecay extends ESEnchantment {
    public EnchantmentDecay(String name) {
        super(name, ESConfig.ENCHANTMENTS.DECAY.rarity, EnumEnchantmentType.BOW, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return super.canApply(stack);
    }

    @Override
    public int getMaxLevel() {
        return ESConfig.ENCHANTMENTS.DECAY.maxLevel;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return ESConfig.ENCHANTMENTS.DECAY.minEnchantability;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return ESConfig.ENCHANTMENTS.DECAY.maxEnchantability;
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