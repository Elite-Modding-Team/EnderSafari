package mod.emt.enderzoo.client.render;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.client.model.ModelWitchCat;
import mod.emt.enderzoo.entity.EntityWitchCat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderWitchCat extends RenderLiving<EntityWitchCat> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/witch_cat.png");
    private static final ResourceLocation TEXTURE_ANGRY = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/witch_cat_angry.png");
    private final float defaultShadowSize;

    public RenderWitchCat(RenderManager render) {
        super(render, new ModelWitchCat(), 0.4F);
        this.defaultShadowSize = 0.4F;
        this.addLayer(new AngryLayer());
    }

    @Override
    public void doRender(@NotNull EntityWitchCat entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = this.defaultShadowSize * entity.getScale();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(@NotNull EntityWitchCat entity, float partialTick) {
        float size = entity.getScale();

        if (entity.getGrowthMode() == EntityWitchCat.GrowthMode.GROW) {
            size = Math.min(entity.getAngryScale(), size + entity.getScaleInc() * partialTick);
        } else if (entity.getGrowthMode() == EntityWitchCat.GrowthMode.SHRINK) {
            size = Math.max(1.0F, size - entity.getScaleInc() * partialTick);
        }

        if (size > 1.0F) {
            float widthFactor = 1.0F - (entity.getAngryScale() - size);
            GlStateManager.scale(size + (0.25f * widthFactor), size, size - (0.1f * widthFactor));
        }
    }

    private class AngryLayer implements LayerRenderer<EntityWitchCat> {
        @Override
        public void doRenderLayer(EntityWitchCat cat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            float size = cat.getScale();

            if (cat.getGrowthMode() == EntityWitchCat.GrowthMode.GROW) {
                size = Math.min(cat.getAngryScale(), size + cat.getScaleInc() * partialTicks);
            } else if (cat.getGrowthMode() == EntityWitchCat.GrowthMode.SHRINK) {
                size = Math.max(1.0F, size - cat.getScaleInc() * partialTicks);
            }

            float blendFactor = 1.0F - (cat.getAngryScale() - size);
            if (blendFactor > 0.0F) {
                bindTexture(TEXTURE_ANGRY);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.color(1.0F, 1.0F, 1.0F, Math.min(1.0F, blendFactor));
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(-1.0F, -1.0F);
                char c0 = 61680;
                int j = c0 % 65536;
                int k = c0 / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
                GlStateManager.enableLighting();
                getMainModel().render(cat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                setLightmap(cat);
                GlStateManager.disablePolygonOffset();
                GlStateManager.disableBlend();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityWitchCat entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityWitchCat> {
        @Override
        public Render<? super EntityWitchCat> createRenderFor(RenderManager manager) {
            return new RenderWitchCat(manager);
        }
    }
}