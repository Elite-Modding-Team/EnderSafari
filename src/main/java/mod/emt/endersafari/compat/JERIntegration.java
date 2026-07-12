package mod.emt.endersafari.compat;

import jeresources.api.IJERAPI;
import jeresources.api.IMobRegistry;
import jeresources.api.conditionals.LightLevel;
import jeresources.compatibility.JERAPI;
import mod.emt.endersafari.entity.*;
import mod.emt.endersafari.registry.ModLootTablesES;
import net.minecraft.world.World;

public class JERIntegration {
    public static void init() {
        IJERAPI jerApi = JERAPI.getInstance();
        IMobRegistry jerMobRegistry = jerApi.getMobRegistry();
        World jerWorld = jerApi.getWorld();

        jerMobRegistry.register(new EntityConcussionCreeper(jerWorld), LightLevel.hostile, ModLootTablesES.CONCUSSION_CREEPER);
        jerMobRegistry.register(new EntityDireCube(jerWorld), LightLevel.any, ModLootTablesES.DIRE_CUBE);
        jerMobRegistry.register(new EntityDireWolf(jerWorld), LightLevel.any, ModLootTablesES.DIRE_WOLF);
        jerMobRegistry.register(new EntityEnderizedZombie(jerWorld), LightLevel.hostile, ModLootTablesES.ENDERIZED_ZOMBIE);
        jerMobRegistry.register(new EntityEnderminy(jerWorld), LightLevel.hostile, ModLootTablesES.ENDERMINY);
        jerMobRegistry.register(new EntityEpicSquid(jerWorld), LightLevel.any, ModLootTablesES.EPIC_SQUID);
        jerMobRegistry.register(new EntityFallenKnight(jerWorld), LightLevel.hostile, ModLootTablesES.FALLEN_KNIGHT);
        jerMobRegistry.register(new EntityFallenSteed(jerWorld), LightLevel.hostile, ModLootTablesES.FALLEN_STEED);
        jerMobRegistry.register(new EntityOwl(jerWorld), LightLevel.any, ModLootTablesES.OWL);
        jerMobRegistry.register(new EntityVoidCube(jerWorld), LightLevel.hostile, ModLootTablesES.VOID_CUBE);
        jerMobRegistry.register(new EntityWitchCat(jerWorld), LightLevel.any, ModLootTablesES.WITCH_CAT);
        jerMobRegistry.register(new EntityWitherWitch(jerWorld), LightLevel.hostile, ModLootTablesES.WITHER_WITCH);
    }
}
