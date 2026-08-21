package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelFairy;
import mod.emt.endersafari.entity.EntityFairy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

public class RenderFairy extends RenderLiving<EntityFairy> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fairy/fairy_blue.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fairy/fairy_green.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fairy/fairy_orange.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fairy/fairy_pink.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fairy/fairy_purple.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fairy/fairy_red.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/fairy/fairy_yellow.png")
    };

    public RenderFairy(RenderManager render) {
        super(render, new ModelFairy(), 0);
    }

    @Override
    protected float getDeathMaxRotation(@NotNull EntityFairy entity) {
        return 0.0F;
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityFairy entity) {
        return TEXTURES[entity.getType()];
    }

    public static class Factory implements IRenderFactory<EntityFairy> {
        @Override
        public Render<? super EntityFairy> createRenderFor(RenderManager manager) {
            return new RenderFairy(manager);
        }
    }
}
