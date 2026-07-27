package mod.emt.endersafari.client.model;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class ModelBlazeCow extends ModelQuadruped {
    private final ModelRenderer[] rod = new ModelRenderer[12];

    public ModelBlazeCow() {
        super(12, 0.0F);

        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
        this.head.setRotationPoint(0.0F, 4.0F, -8.0F);
        this.head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        this.head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);

        this.body = new ModelRenderer(this, 18, 4);
        this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.body.setTextureOffset(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4, 6, 1);

        for (int i = 0; i < this.rod.length; ++i) {
            this.rod[i] = new ModelRenderer(this, 0, 16);
            this.rod[i].addBox(-1.0F, 0.0F, 0.0F, 2, 6, 2);
        }
    }

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        if (this.isChild) {
            float f6 = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.body.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);

            for (ModelRenderer aRod : this.rod) {
                aRod.render(scale);
            }

            GlStateManager.popMatrix();

        } else {
            this.head.render(scale);
            this.body.render(scale);

            for (ModelRenderer aRod : this.rod) {
                aRod.render(scale);
            }
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @NotNull Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        this.head.rotateAngleX = headPitch / (180F / (float) Math.PI);
        this.head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.body.rotateAngleX = ((float) Math.PI / 2F);
        float f6 = ageInTicks * (float) Math.PI * -0.06F;

        for (int i = 0; i < 4; ++i) {
            this.rod[i].rotationPointY = 9.0F + MathHelper.cos(((float) (i * 2) + ageInTicks) * 0.25F);
            this.rod[i].rotationPointX = MathHelper.cos(f6) * 12.0F;
            this.rod[i].rotationPointZ = MathHelper.sin(f6) * 12.0F;
            ++f6;
        }

        f6 = ((float) Math.PI / 4F) + ageInTicks * (float) Math.PI * 0.03F;

        for (int j = 4; j < 8; ++j) {
            this.rod[j].rotationPointY = 14.0F + MathHelper.cos(((float) (j * 2) + ageInTicks) * 0.25F);
            this.rod[j].rotationPointX = MathHelper.cos(f6) * 7.0F;
            this.rod[j].rotationPointZ = MathHelper.sin(f6) * 7.0F;
            ++f6;
        }

        f6 = 0.4712389F + ageInTicks * (float) Math.PI * -0.05F;

        for (int k = 8; k < 12; ++k) {
            this.rod[k].rotationPointY = 18.0F + MathHelper.cos(((float) k * 1.5F + ageInTicks) * 0.5F);
            this.rod[k].rotationPointX = MathHelper.cos(f6) * 5.0F;
            this.rod[k].rotationPointZ = MathHelper.sin(f6) * 5.0F;
            ++f6;
        }
    }
}