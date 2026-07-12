package mod.emt.endersafari.client.model;

import mod.emt.endersafari.entity.EntityDireWolf;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class ModelDireWolf extends ModelBase {
    public ModelRenderer wolfHeadMain;
    public ModelRenderer wolfBody;
    public ModelRenderer wolfLeg1;
    public ModelRenderer wolfLeg2;
    public ModelRenderer wolfLeg3;
    public ModelRenderer wolfLeg4;
    public ModelRenderer wolfTail;
    public ModelRenderer wolfMane;

    public ModelDireWolf() {
        float f = 0.0F;
        float f1 = 13.5F;

        this.wolfHeadMain = new ModelRenderer(this, 0, 0);
        this.wolfHeadMain.addBox(-3.0F, -3.0F, -4.0F, 6, 6, 6, f);
        this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);

        this.wolfBody = new ModelRenderer(this, 18, 17);
        this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
        this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);

        this.wolfMane = new ModelRenderer(this, 34, 0);
        this.wolfMane.addBox(-3.5F, -3.0F, -3.0F, 7, 6, 7, f);
        this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);

        this.wolfLeg1 = new ModelRenderer(this, 48, 22);
        this.wolfLeg1.addBox(-1.7F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg1.setTextureOffset(52, 14).addBox(-1.75F, -2.5F, -2.0F, 2, 4, 4, f);
        this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 6.0F);

        this.wolfLeg2 = new ModelRenderer(this, 48, 22);
        this.wolfLeg2.addBox(-1.8F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg2.setTextureOffset(52, 14).addBox(-1.75F, -2.5F, -2.0F, 2, 4, 4, f);
        this.wolfLeg2.setRotationPoint(2.0F, 16.0F, 6.0F);

        this.wolfLeg3 = new ModelRenderer(this, 0, 22);
        this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);

        this.wolfLeg4 = new ModelRenderer(this, 0, 22);
        this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);

        this.wolfTail = new ModelRenderer(this, 9, 22);
        this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);

        this.wolfHeadMain.setTextureOffset(16, 18).addBox(-2.5F, -5.0F, -1.5F, 1, 2, 2, f);
        this.wolfHeadMain.setTextureOffset(16, 18).addBox(1.5F, -5.0F, -1.5F, 1, 2, 2, f);
        this.wolfHeadMain.setTextureOffset(0, 14).addBox(-1.5F, 0.0F, -7.0F, 3, 3, 4, f);
    }

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        if (this.isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
            this.wolfHeadMain.renderWithRotation(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.wolfHeadMain.renderWithRotation(scale);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
        }
    }

    @Override
    public void setLivingAnimations(@NotNull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityDireWolf wolf = (EntityDireWolf) entity;

        if (wolf.isAngry()) {
            this.wolfTail.rotateAngleY = 0.0F;
        } else {
            this.wolfTail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.wolfBody.rotateAngleX = ((float) Math.PI / 2F);
        this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
        this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
        this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);

        this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 6.0F);
        this.wolfLeg2.setRotationPoint(2.0F, 16.0F, 6.0F);
        this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);

        this.wolfLeg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.wolfLeg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.wolfLeg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.wolfLeg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @NotNull Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.wolfHeadMain.rotateAngleX = headPitch * 0.017453292F;
        this.wolfHeadMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.wolfTail.rotateAngleX = 0.7853982F;
    }
}