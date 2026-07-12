package mod.emt.endersafari.client.render;

import mod.emt.endersafari.entity.EntityFallenSteed;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFallenSteed extends RenderHorse {
    public RenderFallenSteed(RenderManager renderManager) {
        super(renderManager);
    }

    public static class Factory implements IRenderFactory<EntityFallenSteed> {
        @Override
        public Render<? super EntityFallenSteed> createRenderFor(RenderManager manager) {
            return new RenderFallenSteed(manager);
        }
    }
}