package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.EntityRestlessHero;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderRestlessHero extends RenderBiped<EntityRestlessHero> {
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/restless_hero/restless_hero_steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/restless_hero/restless_hero_alex.png");

    private final ModelPlayer modelSteve;
    private final ModelPlayer modelAlex;

    protected RenderRestlessHero(RenderManager renderManager) {
        super(renderManager, new ModelPlayer(0.0F, false), 0.5F);
        this.modelSteve = new ModelPlayer(0.0F, false);
        this.modelAlex = new ModelPlayer(0.0F, true);
        this.addLayer(new LayerBipedArmor(this));
    }

    @Override
    protected void preRenderCallback(@NotNull EntityRestlessHero entity, float partialTickTime) {
        super.preRenderCallback(entity, partialTickTime);

        float spawnTime = (entity.ticksExisted + partialTickTime) / 15.0F;
        float fadeTime = MathHelper.clamp(MathHelper.sqrt(spawnTime), 0.0F, 1.0F);
        float pulseTime = (entity.ticksExisted + partialTickTime) * 0.05F;
        float pulse = (MathHelper.sin(pulseTime) + 1.0F) / 2.0F;
        float alpha = (0.2F + (pulse * 0.4F)) * fadeTime;
        float r = 0.6F + (pulse * 0.3F);
        float g = 0.7F + (pulse * 0.25F);
        float b = 0.8F + (pulse * 0.2F);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        if (fadeTime < 1.0F) {
            float glow = (1.0F - fadeTime) * 0.5F;
            GlStateManager.color(r + glow, g + glow, b + glow, alpha);
        } else {
            GlStateManager.color(r, g, b, alpha);
        }

        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.enableLighting();
    }

    @Override
    public void doRender(EntityRestlessHero entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.mainModel = entity.getType() == 1 ? modelAlex : modelSteve;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected float getDeathMaxRotation(@NotNull EntityRestlessHero entity) {
        return 0.0F;
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityRestlessHero entity) {
        return entity.getType() == 1 ? TEXTURE_ALEX : TEXTURE_STEVE;
    }

    public static class Factory implements IRenderFactory<EntityRestlessHero> {
        @Override
        public Render<? super EntityRestlessHero> createRenderFor(RenderManager manager) {
            return new RenderRestlessHero(manager);
        }
    }
}
