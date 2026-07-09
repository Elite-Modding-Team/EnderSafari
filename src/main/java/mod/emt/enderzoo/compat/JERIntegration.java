package mod.emt.enderzoo.compat;

import jeresources.api.IJERAPI;
import jeresources.api.IMobRegistry;
import jeresources.api.conditionals.LightLevel;
import jeresources.compatibility.JERAPI;
import mod.emt.enderzoo.entity.*;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.world.World;

public class JERIntegration {
    public static void init() {
        IJERAPI jerApi = JERAPI.getInstance();
        IMobRegistry jerMobRegistry = jerApi.getMobRegistry();
        World jerWorld = jerApi.getWorld();

        jerMobRegistry.register(new EntityConcussionCreeper(jerWorld), LightLevel.hostile, ModLootTablesEZ.CONCUSSION_CREEPER);
        jerMobRegistry.register(new EntityDireCube(jerWorld), LightLevel.any, ModLootTablesEZ.DIRE_CUBE);
        jerMobRegistry.register(new EntityDireWolf(jerWorld), LightLevel.any, ModLootTablesEZ.DIRE_WOLF);
        jerMobRegistry.register(new EntityEnderizedZombie(jerWorld), LightLevel.hostile, ModLootTablesEZ.ENDERIZED_ZOMBIE);
        jerMobRegistry.register(new EntityEnderminy(jerWorld), LightLevel.hostile, ModLootTablesEZ.ENDERMINY);
        jerMobRegistry.register(new EntityEpicSquid(jerWorld), LightLevel.any, ModLootTablesEZ.EPIC_SQUID);
        jerMobRegistry.register(new EntityFallenKnight(jerWorld), LightLevel.hostile, ModLootTablesEZ.FALLEN_KNIGHT);
        jerMobRegistry.register(new EntityFallenSteed(jerWorld), LightLevel.hostile, ModLootTablesEZ.FALLEN_STEED);
        jerMobRegistry.register(new EntityOwl(jerWorld), LightLevel.any, ModLootTablesEZ.OWL);
        jerMobRegistry.register(new EntityVoidCube(jerWorld), LightLevel.hostile, ModLootTablesEZ.VOID_CUBE);
        jerMobRegistry.register(new EntityWitchCat(jerWorld), LightLevel.any, ModLootTablesEZ.WITCH_CAT);
        jerMobRegistry.register(new EntityWitherWitch(jerWorld), LightLevel.hostile, ModLootTablesEZ.WITHER_WITCH);
    }
}
