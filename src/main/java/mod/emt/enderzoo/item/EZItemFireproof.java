package mod.emt.enderzoo.item;

import com.invadermonky.futurefireproof.api.IFireproofItem;
import mod.emt.enderzoo.EnderSafari;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// This makes the item completely immune to lava if Future Fireproof is installed, otherwise it acts like a normal item
@Optional.Interface(modid = "futurefireproof", iface = "com.invadermonky.futurefireproof.api.IFireproofItem", striprefs = true)
public class EZItemFireproof extends Item implements IFireproofItem {
    private final EnumRarity rarity;

    public EZItemFireproof(String name, EnumRarity rarity) {
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
