package mod.emt.endersafari.proxy;

import mod.emt.endersafari.compat.EnderIOIntegration;
import mod.emt.endersafari.compat.JERIntegration;
import mod.emt.endersafari.compat.ThaumcraftIntegration;
import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.event.EventOnBlockBreak;
import mod.emt.endersafari.event.EventOnDecayHurt;
import mod.emt.endersafari.network.ESPacketHandler;
import mod.emt.endersafari.registry.ModEntitiesES;
import mod.emt.endersafari.registry.ModLootTablesES;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import mod.emt.endersafari.utils.helpers.CompatHelper;

public class CommonProxy {
    public void preInit() {
        ESPacketHandler.registerMessages();
    }

    public void init() {
        ModEntitiesES.registerEntitySpawns();
        ModLootTablesES.registerLootTables();

        if (ESConfig.ENCHANTMENTS.DECAY.enableEnchantment) {
            MinecraftForge.EVENT_BUS.register(new EventOnDecayHurt());
        }

        if (ESConfig.ENTITIES.DIRE_CUBE.hardcoreSpawning) {
            MinecraftForge.EVENT_BUS.register(new EventOnBlockBreak());
        }

        if (CompatHelper.isEnderIOLoaded) {
            MinecraftForge.EVENT_BUS.register(new EnderIOIntegration());
        }

        if (CompatHelper.isJERLoaded) {
            JERIntegration.init();
        }

        if (CompatHelper.isThaumcraftLoaded) {
            MinecraftForge.EVENT_BUS.register(new ThaumcraftIntegration());
        }
    }

    public void postInit() {
    }

    /* Particles */
    public void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    }

    public void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
    }

    public void spawnParticleGlowBurst(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, boolean growth) {
    }
}
