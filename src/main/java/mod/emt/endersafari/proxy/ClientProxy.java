package mod.emt.endersafari.proxy;

import mod.emt.endersafari.client.particle.ParticleGlow;
import mod.emt.endersafari.client.particle.ParticleRenderer;
import mod.emt.endersafari.event.EventParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class ClientProxy extends CommonProxy {
    public static Random random = new Random();
    static int particleCounter;

    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(new EventParticles());
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    /* Particles */
    @Override
    public void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
        particleCounter += random.nextInt(3);
        if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
            ParticleRenderer.INSTANCE.addParticle(new ParticleGlow(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime));
        }
    }
}
