package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelSpriteling;
import mod.emt.endersafari.entity.EntitySpriteling;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

public class RenderSpriteling extends RenderLiving<EntitySpriteling> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/sprite/sprite_green.png")
    };

    public RenderSpriteling(RenderManager render) {
        super(render, new ModelSpriteling(), 0);
    }

    @Override
    protected void preRenderCallback(EntitySpriteling entity, float partialTickTime) {
        if (entity.deathTime <= 0) {
            return;
        }
        float deathTime = entity.deathTime + partialTickTime;
        if (deathTime <= 0.0F) {
            return;
        }
        float deathProgress = (deathTime - 5.0F) / 15.0F;
        deathProgress = Math.max(0.0F, Math.min(1.0F, deathProgress));
        float shrinkProgress = deathProgress * deathProgress;
        float scale = 1.0F - shrinkProgress;
        GlStateManager.scale(scale, scale, scale);
        float spinProgress = deathProgress * deathProgress;
        float spin = spinProgress * 360.0F;
        GlStateManager.rotate(spin, 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected float getDeathMaxRotation(@NotNull EntitySpriteling entity) {
        return 0.0F;
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntitySpriteling entity) {
        return TEXTURES[entity.getType()];
    }

    public static class Factory implements IRenderFactory<EntitySpriteling> {
        @Override
        public Render<? super EntitySpriteling> createRenderFor(RenderManager manager) {
            return new RenderSpriteling(manager);
        }
    }
}
