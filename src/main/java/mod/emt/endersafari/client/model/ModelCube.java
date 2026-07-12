package mod.emt.endersafari.client.model;

import javax.annotation.Nonnull;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ModelCube extends ModelBase {
    ModelRenderer[] sliceRenderers = new ModelRenderer[16];
    ModelRenderer[] coreRenderers = new ModelRenderer[24];
    ModelRenderer coreRendererClay;
    ModelRenderer coreRenderer;

    public ModelCube() {
        for (int i = 0; i < this.sliceRenderers.length; ++i) {
            this.sliceRenderers[i] = new ModelRenderer(this, 0, i);
            this.sliceRenderers[i].setTextureSize(64, 64);
            this.sliceRenderers[i].addBox(-8.0F, 8 + i, -8.0F, 16, 1, 16);
        }

        this.coreRendererClay = new ModelRenderer(this, 0, 32);
        this.coreRendererClay.setTextureSize(64, 64);
        this.coreRendererClay.addBox(-3.0F, 13.0F, -3.0F, 6, 6, 6);

        for (int i = 0; i < this.coreRenderers.length; ++i) {
            this.coreRenderers[i] = new ModelRenderer(this, 32, 32 + i);
            this.coreRenderers[i].setTextureSize(64, 64);
            this.coreRenderers[i].addBox(-3.0F, 13.0F, -3.0F, 6, 6, 6);
        }
    }

    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityMagmaCube cube = (EntityMagmaCube) entity;
        float f3 = cube.prevSquishFactor + (cube.squishFactor - cube.prevSquishFactor) * partialTickTime;
        int size = cube.getSlimeSize();

        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (size > 1) {
            float continuousTime = (float) entity.ticksExisted + partialTickTime;
            int i = (int) (continuousTime * 0.25F) % 8;
            coreRenderer = coreRenderers[i];
        } else {
            coreRenderer = coreRendererClay;
        }

        coreRenderer.rotationPointY = f3 * 1.7F;

        for (int i = 0; i < this.sliceRenderers.length; ++i) {
            this.sliceRenderers[i].rotationPointY = (-(4 - i)) * f3 * 1.7F;
        }
    }

    @Override
    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.coreRenderer.render(scale);

        for (ModelRenderer sliceRenderer : this.sliceRenderers) {
            sliceRenderer.render(scale);
        }
    }

    public void renderCore(float scale) {
        if (this.coreRenderer != null) {
            this.coreRenderer.render(scale);
        }
    }
}
