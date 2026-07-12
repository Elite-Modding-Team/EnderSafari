package mod.emt.endersafari.effect;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class MobEffectBaseES extends Potion {
    private final ResourceLocation iconTexture;

    protected MobEffectBaseES(String name, boolean isBadEffect, int liquidColor) {
        super(isBadEffect, liquidColor);
        this.setPotionName("mob_effect." + name);
        this.iconTexture = new ResourceLocation(EnderSafari.MOD_ID + ":textures/potions/" + name + ".png");
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, @NotNull PotionEffect effect, Minecraft mc) {
        if (mc.currentScreen != null) {
            mc.getTextureManager().bindTexture(this.iconTexture);
            Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, @NotNull PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(this.iconTexture);
        Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
    }
}