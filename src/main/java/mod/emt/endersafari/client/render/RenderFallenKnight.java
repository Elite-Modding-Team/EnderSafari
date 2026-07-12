package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.EntityFallenKnight;
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
public class RenderFallenKnight extends RenderSkeleton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fallen_knight.png");

    public RenderFallenKnight(RenderManager render) {
        super(render);
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull AbstractSkeleton entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityFallenKnight> {
        @Override
        public Render<? super EntityFallenKnight> createRenderFor(RenderManager manager) {
            return new RenderFallenKnight(manager);
        }
    }
}
