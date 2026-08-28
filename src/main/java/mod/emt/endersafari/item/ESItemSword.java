package mod.emt.endersafari.item;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class ESItemSword extends ItemSword {
    public IRarity rarity;
    protected String tooltip = null;

    public ESItemSword(String name, ToolMaterial material, EnumRarity rarity) {
        this(name, material, rarity, false);
    }

    public ESItemSword(String unlocName, ToolMaterial material, EnumRarity rarity, boolean hasTooltip) {
        super(material);
        this.setRegistryName(EnderSafari.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(EnderSafari.tabEZ);
        this.maxStackSize = 1;
        this.rarity = rarity;

        if (hasTooltip) {
            this.tooltip = "item." + Objects.requireNonNull(this.getRegistryName()) + ".tooltip";
        }
    }

    @Override
    public @NotNull IRarity getForgeRarity(@Nonnull ItemStack stack) {
        return this.rarity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> list, @NotNull ITooltipFlag flag) {
        if (tooltip != null) {
            list.add(TextFormatting.GRAY + I18n.translateToLocal(tooltip));
        }
    }
}
