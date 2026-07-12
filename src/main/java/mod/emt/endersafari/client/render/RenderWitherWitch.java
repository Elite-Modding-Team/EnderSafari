package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.EntityWitherWitch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderWitherWitch extends RenderLiving<EntityWitherWitch> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/wither_witch.png");
    private final ModelWitch witchModel;

    public RenderWitherWitch(RenderManager renderManager) {
        super(renderManager, new ModelWitch(0.0F), 0.5F);
        this.witchModel = (ModelWitch) this.mainModel;
        this.addLayer(new LayerHeldItemWitherWitch());
    }

    @Override
    public void doRender(@NotNull EntityWitherWitch entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ItemStack stack = entity.getHeldItem(EnumHand.MAIN_HAND);
        this.witchModel.holdingItem = !stack.isEmpty();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(@NotNull EntityWitherWitch entity, float partialTick) {
        float scale = 0.9375F;
        GlStateManager.scale(scale, scale, scale);
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityWitherWitch entity) {
        return TEXTURE;
    }

    private class LayerHeldItemWitherWitch implements LayerRenderer<EntityWitherWitch> {
        @Override
        public void doRenderLayer(@NotNull EntityWitherWitch entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ItemStack stack = entity.getHeldItemMainhand();

            if (!stack.isEmpty()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F);
                GlStateManager.pushMatrix();

                if (witchModel.isChild) {
                    GlStateManager.translate(0.0F, 0.625F, 0.0F);
                    GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                }

                witchModel.villagerNose.postRender(0.0625F);
                GlStateManager.translate(-0.0625F, 0.53125F, 0.21875F);
                GlStateManager.translate(0.1875F, 0.1875F, 0.0F);
                GlStateManager.scale(0.875F, 0.875F, 0.875F);
                GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-60.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-15.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
                Minecraft.getMinecraft().getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
                GlStateManager.popMatrix();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }

    public static class Factory implements IRenderFactory<EntityWitherWitch> {
        @Override
        public Render<? super EntityWitherWitch> createRenderFor(RenderManager manager) {
            return new RenderWitherWitch(manager);
        }
    }
}
