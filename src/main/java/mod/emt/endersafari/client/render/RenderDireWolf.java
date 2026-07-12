package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelDireWolf;
import mod.emt.endersafari.entity.EntityDireWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderDireWolf extends RenderLiving<EntityDireWolf> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/dire_wolf.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/snowy_dire_wolf.png")
    };

    public RenderDireWolf(RenderManager render) {
        super(render, new ModelDireWolf(), 0.5F);
    }

    @Override
    public void doRender(@NotNull EntityDireWolf entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected float handleRotationFloat(@NotNull EntityDireWolf wolf, float partialTicks) {
        return wolf.getTailRotation();
    }

    @Override
    protected void preRenderCallback(@NotNull EntityDireWolf entity, float partialTick) {
        float scale = 1.25F;
        GlStateManager.translate(0.1F, 0.0F, 0.0F);
        GlStateManager.scale(scale - 0.1F, scale, scale);
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityDireWolf entity) {
        return TEXTURES[entity.getType()];
    }

    public static class Factory implements IRenderFactory<EntityDireWolf> {
        @Override
        public Render<? super EntityDireWolf> createRenderFor(RenderManager manager) {
            return new RenderDireWolf(manager);
        }
    }
}
