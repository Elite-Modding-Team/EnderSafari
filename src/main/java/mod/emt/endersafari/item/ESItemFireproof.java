package mod.emt.endersafari.item;

import com.invadermonky.futurefireproof.api.IFireproofItem;
import mod.emt.endersafari.EnderSafari;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

// This makes the item completely immune to lava if Future Fireproof is installed, otherwise it acts like a normal item
@SuppressWarnings("deprecation")
@Optional.Interface(modid = "futurefireproof", iface = "com.invadermonky.futurefireproof.api.IFireproofItem", striprefs = true)
public class ESItemFireproof extends Item implements IFireproofItem {
    private final EnumRarity rarity;
    protected String tooltip = null;

    public ESItemFireproof(String name, EnumRarity rarity) {
        this(name, rarity, false);
    }

    public ESItemFireproof(String name, EnumRarity rarity, boolean hasTooltip) {
        super();
        this.setRegistryName(EnderSafari.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(EnderSafari.tabEZ);
        this.rarity = rarity;

        if (hasTooltip) {
            this.tooltip = "item." + Objects.requireNonNull(this.getRegistryName()) + ".tooltip";
        }
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> list, @NotNull ITooltipFlag flag) {
        if (tooltip != null) {
            list.add(TextFormatting.GRAY + I18n.translateToLocal(tooltip));
        }
    }
}
