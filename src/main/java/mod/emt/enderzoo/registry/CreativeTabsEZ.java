package mod.emt.enderzoo.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

public class CreativeTabsEZ extends CreativeTabs {
    public CreativeTabsEZ(int length, String name) {
        super(length, name);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(ModItemsEZ.ENDER_FRAGMENT);
    }
}
