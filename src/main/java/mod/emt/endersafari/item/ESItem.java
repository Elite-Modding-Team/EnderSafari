package mod.emt.endersafari.item;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ESItem extends Item {
    private final EnumRarity rarity;

    public ESItem(String name, EnumRarity rarity) {
        super();
        this.setRegistryName(EnderSafari.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(EnderSafari.tabEZ);
        this.rarity = rarity;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }
}
