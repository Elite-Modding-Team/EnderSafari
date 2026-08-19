package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.client.model.ModelGolem;
import mod.emt.endersafari.client.render.layer.LayerGlow;
import mod.emt.endersafari.entity.EntityGolem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderGolem extends RenderLiving<EntityGolem> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_stone.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_andesite.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_diorite.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_granite.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_obsidian.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_netherrack.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_end_stone.png")
    };
    private static final ResourceLocation[] TEXTURES_GLOW = new ResourceLocation[] {
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_stone_eyes.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_andesite_eyes.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_diorite_eyes.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_granite_eyes.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_obsidian_eyes.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_netherrack_eyes.png"),
            new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/golem/golem_end_stone_eyes.png")
    };

    public RenderGolem(RenderManager renderManager) {
        super(renderManager, new ModelGolem(), 0.5F);
        this.addLayer(new LayerGlow<EntityGolem>(this, TEXTURES_GLOW) {
            @Override
            protected int getTextureIndex(EntityGolem entity) {
                return entity.getType();
            }
        });
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityGolem entity) {
        return TEXTURES[entity.getType()];
    }

    public static class Factory implements IRenderFactory<EntityGolem> {
        @Override
        public Render<? super EntityGolem> createRenderFor(RenderManager manager) {
            return new RenderGolem(manager);
        }
    }
}
