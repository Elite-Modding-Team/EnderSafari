package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelCube;
import mod.emt.endersafari.entity.EntityVoidCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderVoidCube extends RenderLiving<EntityVoidCube> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/void_cube.png");

    public RenderVoidCube(RenderManager render) {
        super(render, new ModelCube(), 0.25F);
    }

    @Override
    protected void preRenderCallback(EntityVoidCube entity, float partialTick) {
        int i = entity.getSlimeSize();
        float f1 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * partialTick) / (i * 0.5F + 1.0F);
        float f2 = 1.0F / (f1 + 1.0F);
        GlStateManager.scale(f2 * (float) i, 1.0F / f2 * (float) i, f2 * (float) i);
    }

    @Override
    protected void renderModel(@NotNull EntityVoidCube entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (!entity.isInvisible()) {
            this.bindTexture(TEXTURE);
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            int i = 61680;
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            ((ModelCube) this.mainModel).renderCore(scaleFactor);
            int currentLight = entity.getBrightnessForRender();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) (currentLight % 65536), (float) (currentLight / 65536));
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityVoidCube entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityVoidCube> {
        @Override
        public Render<? super EntityVoidCube> createRenderFor(RenderManager manager) {
            return new RenderVoidCube(manager);
        }
    }
}
