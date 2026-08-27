package mod.emt.endersafari.client.model;

import mod.emt.endersafari.entity.EntitySpriteling;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class ModelSpriteling extends ModelBase {
    ModelRenderer head1;
    ModelRenderer head2;
    ModelRenderer head3;
    ModelRenderer head4;
    ModelRenderer tail1;

    public ModelSpriteling() {
        textureWidth = 128;
        textureHeight = 128;
        head1 = new ModelRenderer(this, 0, 16);
        head1.addBox(-3F, -3F, -3F, 6, 6, 6);
        head1.setRotationPoint(0F, 0F, 0F);
        head1.setTextureSize(128, 128);
        head1.mirror = true;
        setRotation(head1, 0F, 0F, 0F);
        head2 = new ModelRenderer(this, 48, 32);
        head2.addBox(-1F, -1F, -1F, 2, 2, 2);
        head2.setRotationPoint(0F, 0F, 0F);
        head2.setTextureSize(128, 128);
        head2.mirror = true;
        setRotation(head2, 0F, 0F, 0F);
        head3 = new ModelRenderer(this, 48, 16);
        head3.addBox(0F, -1F, -2F, 4, 2, 4);
        head3.setRotationPoint(3F, 0F, 0F);
        head3.setTextureSize(128, 128);
        head3.mirror = true;
        setRotation(head3, 0F, -0.3926991F, 0F);
        head4 = new ModelRenderer(this, 48, 16);
        head4.addBox(-4F, -1F, -2F, 4, 2, 4);
        head4.setRotationPoint(-3F, 0F, 0F);
        head4.setTextureSize(128, 128);
        head4.mirror = true;
        setRotation(head4, 0F, 0.3926991F, 0F);
        tail1 = new ModelRenderer(this, 48, 0);
        tail1.addBox(-2F, 0F, -2F, 4, 6, 4);
        tail1.setRotationPoint(0F, 0F, 1F);
        tail1.setTextureSize(128, 128);
        tail1.mirror = true;
        setRotation(tail1, 1.308997F, 0F, 0F);
        head1.addChild(head3);
        head1.addChild(head4);
        head1.addChild(tail1);
    }

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GlStateManager.pushAttrib();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(0, 1.25, 0);
        setRotation(head2, 0, 0, 0);

        float animationTime = ageInTicks / 20.0F;
        float bobAngle = (float) (15.0F * Math.sin(animationTime * Math.PI * 2.0F));
        EntitySpriteling spriteling = (EntitySpriteling) entity;
        int twirlStart = spriteling.getDataManager().get(EntitySpriteling.TWIRL_START);
        float twirlAngle = 0.0F;
        if (twirlStart > 0) {
            float elapsed = ageInTicks - (float) twirlStart;
            float progress = MathHelper.clamp(elapsed / 15.0F, 0.0F, 1.0F);
            twirlAngle = progress * 360.0F;
        }

        GlStateManager.rotate(-0.5f * (float) Math.PI - (float) Math.toDegrees(entity.rotationYaw), 0, 1, 0);
        GlStateManager.rotate((float) Math.toDegrees(-entity.rotationPitch / 2.0f), 1, 0, 0);
        GlStateManager.rotate(twirlAngle, 0, 0, 1);
        GlStateManager.rotate(bobAngle, 0, 0, 1);
        head2.render(1.0f / 16.0f);
        GlStateManager.rotate(-bobAngle, 0, 0, 1);
        GlStateManager.rotate(-twirlAngle, 0, 0, 1);
        GlStateManager.rotate(-(float) Math.toDegrees(-entity.rotationPitch / 2.0f), 1, 0, 0);
        GlStateManager.rotate((float) Math.toDegrees(entity.rotationYaw) + 0.5f * (float) Math.PI, 0, 1, 0);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 0.6f);
        setRotation(head1, 0, 0, 0);
        setRotation(head1.childModels.get(2), (float) Math.toRadians(90.0), 0, 0);
        GlStateManager.rotate(-0.5f * (float) Math.PI - (float) Math.toDegrees(entity.rotationYaw), 0, 1, 0);
        GlStateManager.rotate((float) Math.toDegrees(-entity.rotationPitch / 2.0f), 1, 0, 0);
        GlStateManager.rotate(twirlAngle, 0, 0, 1);
        GlStateManager.rotate(bobAngle, 0, 0, 1);
        head1.render(1.0f / 16.0f);
        GlStateManager.rotate(-bobAngle, 0, 0, 1);
        GlStateManager.rotate(-twirlAngle, 0, 0, 1);
        GlStateManager.rotate(-(float) Math.toDegrees(-entity.rotationPitch / 2.0f), 1, 0, 0);
        GlStateManager.rotate((float) Math.toDegrees(entity.rotationYaw) + 0.5f * (float) Math.PI, 0, 1, 0);
        GlStateManager.translate(0, -(1.25), 0);
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popAttrib();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
