package mod.emt.endersafari.client.render;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.EntityEnderizedZombie;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderEnderizedZombie extends RenderBiped<EntityEnderizedZombie> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnderSafari.MOD_ID, "textures/entity/enderized_zombie.png");

    public RenderEnderizedZombie(RenderManager render) {
        super(render, new ModelZombie(), 0.5F);
        LayerBipedArmor layerArmor = new LayerBipedArmor(this) {
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerArmor);
    }

    @Override
    protected @NotNull ResourceLocation getEntityTexture(@NotNull EntityEnderizedZombie entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityEnderizedZombie> {
        @Override
        public Render<? super EntityEnderizedZombie> createRenderFor(RenderManager manager) {
            return new RenderEnderizedZombie(manager);
        }
    }
}
