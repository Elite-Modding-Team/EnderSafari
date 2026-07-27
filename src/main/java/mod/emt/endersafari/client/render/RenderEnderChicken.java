package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.render.layer.LayerGlow;
import mod.emt.endersafari.entity.EntityEnderChicken;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

public class RenderEnderChicken extends RenderChicken {
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/ender_chicken/ender_chicken_eyes.png");
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/ender_chicken/ender_chicken.png");

    public RenderEnderChicken(RenderManager renderManager) {
        super(renderManager);
        this.addLayer(new LayerGlow<>(this, TEXTURE_GLOW));
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityChicken entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityEnderChicken> {
        @Override
        public Render<? super EntityEnderChicken> createRenderFor(RenderManager manager) {
            return new RenderEnderChicken(manager);
        }
    }
}
