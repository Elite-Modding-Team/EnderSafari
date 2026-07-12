package mod.emt.enderzoo.client.render;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.config.EZConfig;
import mod.emt.enderzoo.entity.EntityEnderminy;
import net.minecraft.client.model.ModelEnderman;
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
public class RenderEnderminy extends RenderLiving<EntityEnderminy> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/enderminy.png");
    private static final ResourceLocation TEXTURE_ALT = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/alt/enderminy.png");
    private static final ResourceLocation EYE_TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/enderminy_eyes.png");
    private static final ResourceLocation EYE_TEXTURE_ALT = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/alt/enderminy_eyes.png");

    private final ModelEnderman endermanModel;

    public RenderEnderminy(RenderManager rm) {
        super(rm, new ModelEnderman(0.0F), 0.15F);
        this.endermanModel = (ModelEnderman) super.mainModel;
        this.addLayer(new EyesLayer());
    }

    @Override
    public void doRender(@NotNull EntityEnderminy entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.endermanModel.isAttacking = entity.isScreaming();

        if (entity.isScreaming()) {
            double jitterAmount = 0.02D;
            x += entity.getRNG().nextGaussian() * jitterAmount;
            z += entity.getRNG().nextGaussian() * jitterAmount;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@NotNull EntityEnderminy entity) {
        return EZConfig.ENTITIES.ENDERMINY.enableAlternateTexture ? TEXTURE_ALT : TEXTURE;
    }

    @Override
    protected void preRenderCallback(@NotNull EntityEnderminy entity, float partialTickTime) {
        GlStateManager.scale(0.5F, 0.25F, 0.5F);
    }

    private class EyesLayer implements LayerRenderer<EntityEnderminy> {

        @Override
        public void doRenderLayer(@NotNull EntityEnderminy entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ResourceLocation eyeTexture = EZConfig.ENTITIES.ENDERMINY.enableAlternateTexture ?  EYE_TEXTURE_ALT : EYE_TEXTURE;
            bindTexture(eyeTexture);
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(!entity.isInvisible());
            int i = 61680;
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            RenderEnderminy.this.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            RenderEnderminy.this.setLightmap(entity);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }

    public static class Factory implements IRenderFactory<EntityEnderminy> {
        @Override
        public Render<? super EntityEnderminy> createRenderFor(RenderManager manager) {
            return new RenderEnderminy(manager);
        }
    }
}
