package mod.emt.endersafari.utils;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.world.World;

public class ParticleUtil {
    public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
        EnderSafari.proxy.spawnParticleGlow(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime);
    }

    public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
        EnderSafari.proxy.spawnParticleGlow(world, x, y, z, vx, vy, vz, r, g, b, scale, lifetime);
    }
}
