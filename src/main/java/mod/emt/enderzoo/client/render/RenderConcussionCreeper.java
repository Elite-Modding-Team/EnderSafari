package mod.emt.enderzoo.client.render;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.entity.EntityConcussionCreeper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderConcussionCreeper extends RenderCreeper {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/concussion_creeper.png");

    public RenderConcussionCreeper(RenderManager render) {
        super(render);
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityCreeper entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityConcussionCreeper> {
        @Override
        public Render<? super EntityConcussionCreeper> createRenderFor(RenderManager manager) {
            return new RenderConcussionCreeper(manager);
        }
    }
}
