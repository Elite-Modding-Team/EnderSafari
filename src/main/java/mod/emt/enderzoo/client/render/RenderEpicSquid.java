package mod.emt.enderzoo.client.render;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.entity.EntityEpicSquid;
import net.minecraft.client.model.ModelSquid;
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

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderEpicSquid extends RenderLiving<EntityEpicSquid> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/epic_squid.png");

    public RenderEpicSquid(RenderManager render) {
        super(render, new ModelSquid(), 0.7F);
    }

    @Override
    protected void applyRotations(@Nonnull EntityEpicSquid entity, float p_77043_2_, float rotationYaw, float partialTicks) {
        float f = entity.prevSquidPitch + (entity.squidPitch - entity.prevSquidPitch) * partialTicks;
        float f1 = entity.prevSquidYaw + (entity.squidYaw - entity.prevSquidYaw) * partialTicks;
        GlStateManager.translate(0.0F, 0.5F, 0.0F);
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -1.2F, 0.0F);
    }

    @Override
    protected float handleRotationFloat(@Nonnull EntityEpicSquid entity, float partialTicks) {
        return entity.lastTentacleAngle + (entity.tentacleAngle - entity.lastTentacleAngle) * partialTicks;
    }

    @Override
    protected boolean setBrightness(@Nonnull EntityEpicSquid entity, float partialTicks, boolean combineTextures) {
        if (super.setBrightness(entity, partialTicks, combineTextures)) {
            if (entity.hurtTime > 0 || entity.deathTime > 0) {
                float amount = (entity.hurtTime) / (float) entity.maxHurtTime;
                this.brightnessBuffer.position(0);
                this.brightnessBuffer.put(0.0F);
                this.brightnessBuffer.put(0.8f);
                this.brightnessBuffer.put(0.2F);
                this.brightnessBuffer.put(0.6F * amount);
                this.brightnessBuffer.flip();
                GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GlStateManager.glTexEnv(8960, 8705, this.brightnessBuffer);
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            }
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityEpicSquid entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityEpicSquid> {
        @Override
        public Render<? super EntityEpicSquid> createRenderFor(RenderManager manager) {
            return new RenderEpicSquid(manager);
        }
    }
}
