package mod.emt.endersafari.client.render.layer;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelSheeperFurred;
import mod.emt.endersafari.client.render.RenderSheeper;
import mod.emt.endersafari.entity.EntitySheeper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class LayerSheeperWool implements LayerRenderer<EntitySheeper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/sheeper/sheeper_fur.png");
    private final RenderSheeper sheeperRenderer;
    private final ModelSheeperFurred sheeperModel = new ModelSheeperFurred();

    public LayerSheeperWool(RenderSheeper sheeperRendererIn) {
        this.sheeperRenderer = sheeperRendererIn;
    }

    @Override
    public void doRenderLayer(@NotNull EntitySheeper entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.getSheared() && !entity.isInvisible()) {
            this.sheeperRenderer.bindTexture(TEXTURE);
            this.sheeperModel.setModelAttributes(this.sheeperRenderer.getMainModel());
            this.sheeperModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            this.sheeperModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
