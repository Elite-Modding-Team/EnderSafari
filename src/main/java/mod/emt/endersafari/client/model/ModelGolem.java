package mod.emt.endersafari.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ModelGolem extends ModelBase {
    ModelRenderer legL;
    ModelRenderer legR;
    ModelRenderer body1;
    ModelRenderer body2;
    ModelRenderer armR;
    ModelRenderer fistR;
    ModelRenderer armL;
    ModelRenderer fistL;
    ModelRenderer head;

    public ModelGolem() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.legL = new ModelRenderer(this, 0, 0);
        this.legL.addBox(-2F, 0F, -2F, 4, 10, 4);
        this.legL.setRotationPoint(2F, 14F, 0F);
        this.legL.setTextureSize(64, 64);
        this.legL.mirror = true;
        this.setRotation(legL, 0F, 0F, 0F);
        this.legR = new ModelRenderer(this, 0, 0);
        this.legR.addBox(-2F, 0F, -2F, 4, 10, 4);
        this.legR.setRotationPoint(-2F, 14F, 0F);
        this.legR.setTextureSize(64, 64);
        this.legR.mirror = true;
        this.setRotation(legR, 0F, 0F, 0F);
        this.body1 = new ModelRenderer(this, 16, 16);
        this.body1.addBox(-4F, 0F, -2F, 8, 4, 4);
        this.body1.setRotationPoint(0F, 10F, 0F);
        this.body1.setTextureSize(64, 64);
        this.body1.mirror = true;
        this.setRotation(body1, 0F, 0F, 0F);
        this.body2 = new ModelRenderer(this, 16, 0);
        this.body2.addBox(-4.5F, 0F, -3F, 9, 8, 6);
        this.body2.setRotationPoint(0F, 2F, 0F);
        this.body2.setTextureSize(64, 64);
        this.body2.mirror = true;
        this.setRotation(body2, 0F, 0F, 0F);
        this.armR = new ModelRenderer(this, 48, 0);
        this.armR.addBox(-2F, 0F, -2F, 4, 12, 4);
        this.armR.setRotationPoint(-4.5F, 2.013333F, 0F);
        this.armR.setTextureSize(64, 64);
        this.armR.mirror = true;
        this.setRotation(armR, 0F, 0F, 0.3926991F);
        this.fistR = new ModelRenderer(this, 0, 32);
        this.fistR.addBox(-2F, 12F, -2.5F, 5, 5, 5);
        this.fistR.setRotationPoint(-4.5F, 2.013333F, 0F);
        this.fistR.setTextureSize(64, 64);
        this.fistR.mirror = true;
        this.setRotation(fistR, 0F, 0F, 0.3926991F);
        this.armL = new ModelRenderer(this, 48, 0);
        this.armL.addBox(-2F, 0F, -2F, 4, 12, 4);
        this.armL.setRotationPoint(4.5F, 2.013333F, 0F);
        this.armL.setTextureSize(64, 64);
        this.armL.mirror = true;
        this.setRotation(armL, 0F, 0F, -0.3926991F);
        this.fistL = new ModelRenderer(this, 0, 32);
        this.fistL.addBox(-3F, 12F, -2.5F, 5, 5, 5);
        this.fistL.setRotationPoint(4.5F, 2.013333F, 0F);
        this.fistL.setTextureSize(64, 64);
        this.fistL.mirror = false;
        this.setRotation(fistL, 0F, 0F, -0.3926991F);
        this.head = new ModelRenderer(this, 32, 32);
        this.head.addBox(-4F, -8F, -4F, 8, 8, 8);
        this.head.setRotationPoint(0F, 2F, 0F);
        this.head.setTextureSize(64, 64);
        this.head.mirror = true;
        this.setRotation(head, 0F, 0F, 0F);
    }

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.legL.render(scale);
        this.legR.render(scale);
        this.body1.render(scale);
        this.body2.render(scale);
        this.armR.render(scale);
        this.fistR.render(scale);
        this.armL.render(scale);
        this.fistL.render(scale);
        this.head.render(scale);
        GlStateManager.popMatrix();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @NotNull Entity entity) {
        this.legR.rotateAngleX = (float) Math.toRadians(180f * (float) Math.sin(limbSwing * 0.5) * limbSwingAmount * 0.5);
        this.legL.rotateAngleX = -(float) Math.toRadians(180f * (float) Math.sin(limbSwing * 0.5) * limbSwingAmount * 0.5);
        this.armL.rotateAngleX = (float) Math.toRadians(180f * (float) Math.sin(limbSwing * 0.5) * limbSwingAmount * 0.5);
        this.armR.rotateAngleX = -(float) Math.toRadians(180f * (float) Math.sin(limbSwing * 0.5) * limbSwingAmount * 0.5);
        this.fistL.rotateAngleX = (float) Math.toRadians(180f * (float) Math.sin(limbSwing * 0.5) * limbSwingAmount * 0.5);
        this.fistR.rotateAngleX = -(float) Math.toRadians(180f * (float) Math.sin(limbSwing * 0.5) * limbSwingAmount * 0.5);
    }
}
