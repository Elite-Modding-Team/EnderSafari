package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.EntityCrystalSkeleton;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderCrystalSkeleton extends RenderSkeleton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/crystal_skeleton/crystal_skeleton.png");

    public RenderCrystalSkeleton(RenderManager render) {
        super(render);
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull AbstractSkeleton entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityCrystalSkeleton> {
        @Override
        public Render<? super EntityCrystalSkeleton> createRenderFor(RenderManager manager) {
            return new RenderCrystalSkeleton(manager);
        }
    }
}
