package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelBlazeCow;
import mod.emt.endersafari.entity.EntityBlazeCow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderBlazeCow extends RenderLiving<EntityBlazeCow> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/blaze_cow.png");

    public RenderBlazeCow(RenderManager renderManager) {
        super(renderManager, new ModelBlazeCow(), 0.7F);
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityBlazeCow entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityBlazeCow> {
        @Override
        public Render<? super EntityBlazeCow> createRenderFor(RenderManager manager) {
            return new RenderBlazeCow(manager);
        }
    }
}
