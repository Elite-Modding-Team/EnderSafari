package mod.emt.enderzoo.enchantment;

import mod.emt.enderzoo.EnderSafari;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EZEnchantment extends Enchantment {
    protected EZEnchantment(String name, Rarity rarity, EnumEnchantmentType type, EntityEquipmentSlot[] slots) {
        super(rarity, type, slots);
        this.setName(EnderSafari.MOD_ID + "." + name);
        this.setRegistryName(EnderSafari.MOD_ID, name);
    }
}
