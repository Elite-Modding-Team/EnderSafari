package mod.emt.endersafari.client.render;

import mod.emt.endersafari.entity.projectile.EntityFireball;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderFireball extends Render<EntityFireball> {
    protected RenderFireball(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@NotNull EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@NotNull EntityFireball entity) {
        return null;
    }

    public static class Factory implements IRenderFactory<EntityFireball> {
        @Override
        public Render<? super EntityFireball> createRenderFor(RenderManager manager) {
            return new RenderFireball(manager);
        }
    }
}
