package mod.emt.enderzoo.client.render;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.client.model.ModelOwl;
import mod.emt.enderzoo.entity.EntityOwl;
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
public class RenderOwl extends RenderLiving<EntityOwl> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/owl.png");

    public RenderOwl(RenderManager render) {
        super(render, new ModelOwl(), 0.5F);
    }

    @Override
    public void doRender(@Nonnull EntityOwl entity, double x, double y, double z, float entityYaw, float partialTicks) {
        entity.calculateAngles(partialTicks);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityOwl entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityOwl> {
        @Override
        public Render<? super EntityOwl> createRenderFor(RenderManager manager) {
            return new RenderOwl(manager);
        }
    }
}
