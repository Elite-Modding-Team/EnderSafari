package mod.emt.endersafari.enchantment;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ESEnchantment extends Enchantment {
    protected ESEnchantment(String name, Rarity rarity, EnumEnchantmentType type, EntityEquipmentSlot[] slots) {
        super(rarity, type, slots);
        this.setName(EnderSafari.MOD_ID + "." + name);
        this.setRegistryName(EnderSafari.MOD_ID, name);
    }
}
