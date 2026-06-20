package mod.emt.enderzoo.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowFire;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class EnchantmentDecay extends EZEnchantment {
    public EnchantmentDecay(String name) {
        super(name, Rarity.RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return super.canApply(stack);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
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