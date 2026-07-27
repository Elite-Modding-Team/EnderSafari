package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelSheeper;
import mod.emt.endersafari.client.render.layer.LayerSheeperWool;
import mod.emt.endersafari.entity.EntitySheeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenderSheeper extends RenderLiving<EntitySheeper> {
    private static final ResourceLocation SHEARED_TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/sheeper/sheeper.png");

    public RenderSheeper(RenderManager renderManager) {
        super(renderManager, new ModelSheeper(), 0.7F);
        this.addLayer(new LayerSheeperWool(this));
    }

    @Override
    protected void preRenderCallback(EntitySheeper entity, float partialTickTime) {
        float f = entity.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    @Override
    protected int getColorMultiplier(EntitySheeper entity, float lightBrightness, float partialTickTime) {
        float f = entity.getCreeperFlashIntensity(partialTickTime);
        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    @Override
    protected @Nullable ResourceLocation getEntityTexture(@NotNull EntitySheeper entity) {
        return SHEARED_TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntitySheeper> {
        @Override
        public Render<? super EntitySheeper> createRenderFor(RenderManager manager) {
            return new RenderSheeper(manager);
        }
    }
}
