package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.client.render.*;
import mod.emt.enderzoo.config.EZConfig;
import mod.emt.enderzoo.entity.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.*;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class ModEntitiesEZ {
    private static int entityID = 0;

    private static final @Nonnull EntityLiving.SpawnPlacementType ON_BEDROCK =
            Objects.requireNonNull(EnumHelper.addSpawnPlacementType("ON_BEDROCK", (world, pos) -> pos != null
                            && pos.getY() < 32
                            && world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK
                            && WorldEntitySpawner.isValidEmptySpawnBlock(world.getBlockState(pos))
                            && WorldEntitySpawner.isValidEmptySpawnBlock(world.getBlockState(pos.up()))
                            && !(world instanceof World && ((World) world).canBlockSeeSky(pos))));

    private static final @Nonnull EntityLiving.SpawnPlacementType IN_LAVA =
            Objects.requireNonNull(EnumHelper.addSpawnPlacementType("IN_LAVA", (world, pos) -> pos != null
                            && world.getBlockState(pos).getMaterial() == Material.LAVA
                            && world.getBlockState(pos.down()).getMaterial() == Material.LAVA
                            && !world.getBlockState(pos.up()).isNormalCube()));

    public static void registerEntity(String name, Class<? extends Entity> clazz, int eggColor1, int eggColor2) {
        EntityRegistry.registerModEntity(new ResourceLocation(EnderSafari.MOD_ID, name), clazz, EnderSafari.MOD_ID + "." + name, entityID++, EnderSafari.instance, 64, 1, true, eggColor1, eggColor2);
    }

    public static void registerEntity(String name, Class<? extends Entity> clazz, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(EnderSafari.MOD_ID, name), clazz, EnderSafari.MOD_ID + "." + name, entityID++, EnderSafari.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }

    @SubscribeEvent
    public static void registerEntities(@Nonnull final RegistryEvent.Register<EntityEntry> event) {
        if (EZConfig.ENTITIES.CONCUSSION_CREEPER.enableEntity) registerEntity("concussion_creeper", EntityConcussionCreeper.class, 4032112, 2897273);
        if (EZConfig.ENTITIES.DIRE_CUBE.enableEntity) registerEntity("dire_cube", EntityDireCube.class, 12158300, 5848361);
        if (EZConfig.ENTITIES.DIRE_WOLF.enableEntity) registerEntity("dire_wolf", EntityDireWolf.class, 6316128, 10526880);
        if (EZConfig.ENTITIES.ENDERIZED_ZOMBIE.enableEntity) registerEntity("enderized_zombie", EntityEnderizedZombie.class, 1257301, 2829596);
        if (EZConfig.ENTITIES.ENDERMINY.enableEntity) registerEntity("enderminy", EntityEnderminy.class, 2581069, 2171169);
        if (EZConfig.ENTITIES.EPIC_SQUID.enableEntity) registerEntity("epic_squid", EntityEpicSquid.class, 10223617, 15484494);
        if (EZConfig.ENTITIES.FALLEN_KNIGHT.enableEntity) registerEntity("fallen_knight", EntityFallenKnight.class, 3562021, 10526880);
        if (EZConfig.ENTITIES.FALLEN_STEED.enableEntity) registerEntity("fallen_steed", EntityFallenSteed.class, 3562021, 10526880);
        if (EZConfig.ENTITIES.OWL.enableEntity) registerEntity("owl", EntityOwl.class, 12679497, 16768454);
        if (EZConfig.ENTITIES.VOID_CUBE.enableEntity) registerEntity("void_cube", EntityVoidCube.class, 0, 11184810);
        if (EZConfig.ENTITIES.WITCH_CAT.enableEntity) registerEntity("witch_cat", EntityWitchCat.class, 3158064, 16777215);
        if (EZConfig.ENTITIES.WITHER_WITCH.enableEntity) registerEntity("wither_witch", EntityWitherWitch.class, 2511373, 9461315);

        registerEntity("primed_concussion_charge", EntityConcussionChargePrimed.class, 64, 1, true);
        registerEntity("primed_confusing_charge", EntityConfusingChargePrimed.class, 64, 1, true);
        registerEntity("primed_ender_charge", EntityEnderChargePrimed.class, 64, 1, true);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(@Nonnull final ModelRegistryEvent event) {
        if (EZConfig.ENTITIES.CONCUSSION_CREEPER.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityConcussionCreeper.class, new RenderConcussionCreeper.Factory());
        if (EZConfig.ENTITIES.DIRE_CUBE.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityDireCube.class, new RenderDireCube.Factory());
        if (EZConfig.ENTITIES.DIRE_WOLF.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityDireWolf.class, new RenderDireWolf.Factory());
        if (EZConfig.ENTITIES.ENDERIZED_ZOMBIE.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityEnderizedZombie.class, new RenderEnderizedZombie.Factory());
        if (EZConfig.ENTITIES.ENDERMINY.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityEnderminy.class, new RenderEnderminy.Factory());
        if (EZConfig.ENTITIES.EPIC_SQUID.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityEpicSquid.class, new RenderEpicSquid.Factory());
        if (EZConfig.ENTITIES.FALLEN_KNIGHT.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityFallenKnight.class, new RenderFallenKnight.Factory());
        if (EZConfig.ENTITIES.FALLEN_STEED.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityFallenSteed.class, new RenderFallenSteed.Factory());
        if (EZConfig.ENTITIES.OWL.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityOwl.class, new RenderOwl.Factory());
        if (EZConfig.ENTITIES.VOID_CUBE.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityVoidCube.class, new RenderVoidCube.Factory());
        if (EZConfig.ENTITIES.WITCH_CAT.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityWitchCat.class, new RenderWitchCat.Factory());
        if (EZConfig.ENTITIES.WITHER_WITCH.enableEntity) RenderingRegistry.registerEntityRenderingHandler(EntityWitherWitch.class, new RenderWitherWitch.Factory());

        RenderingRegistry.registerEntityRenderingHandler(EntityConcussionChargePrimed.class, new RenderChargePrimed.Factory(() -> ModBlocksEZ.CONCUSSION_CHARGE.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityConfusingChargePrimed.class, new RenderChargePrimed.Factory(() -> ModBlocksEZ.CONFUSING_CHARGE.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderChargePrimed.class, new RenderChargePrimed.Factory(() -> ModBlocksEZ.ENDER_CHARGE.getDefaultState()));
    }

    public static void registerEntitySpawns() {
        if (EZConfig.ENTITIES.CONCUSSION_CREEPER.spawnWeight > 0 && EZConfig.ENTITIES.CONCUSSION_CREEPER.enableEntity) {
            EntityRegistry.addSpawn(EntityConcussionCreeper.class, EZConfig.ENTITIES.CONCUSSION_CREEPER.spawnWeight, EZConfig.ENTITIES.CONCUSSION_CREEPER.spawnMin, EZConfig.ENTITIES.CONCUSSION_CREEPER.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityCreeper.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityConcussionCreeper.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.DIRE_CUBE.spawnWeight > 0 && EZConfig.ENTITIES.DIRE_CUBE.enableEntity) {
            EntityRegistry.addSpawn(EntityDireCube.class, EZConfig.ENTITIES.DIRE_CUBE.spawnWeight, EZConfig.ENTITIES.DIRE_CUBE.spawnMin, EZConfig.ENTITIES.DIRE_CUBE.spawnMax, EnumCreatureType.MONSTER, getBiomeTypes(BiomeDictionary.Type.MESA, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.WASTELAND));
            EntitySpawnPlacementRegistry.setPlacementType(EntityDireCube.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.DIRE_WOLF.spawnWeight > 0 && EZConfig.ENTITIES.DIRE_WOLF.enableEntity) {
            EntityRegistry.addSpawn(EntityDireWolf.class, EZConfig.ENTITIES.DIRE_WOLF.spawnWeight, EZConfig.ENTITIES.DIRE_WOLF.spawnMin, EZConfig.ENTITIES.DIRE_WOLF.spawnMax, EnumCreatureType.MONSTER, getBiomeTypes(BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST));
            EntitySpawnPlacementRegistry.setPlacementType(EntityDireWolf.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.ENDERIZED_ZOMBIE.spawnWeight > 0 && EZConfig.ENTITIES.ENDERIZED_ZOMBIE.enableEntity) {
            EntityRegistry.addSpawn(EntityEnderizedZombie.class, EZConfig.ENTITIES.ENDERIZED_ZOMBIE.spawnWeight, EZConfig.ENTITIES.ENDERIZED_ZOMBIE.spawnMin, EZConfig.ENTITIES.ENDERIZED_ZOMBIE.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityZombie.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityEnderizedZombie.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.ENDERMINY.spawnWeight > 0 && EZConfig.ENTITIES.ENDERMINY.enableEntity) {
            EntityRegistry.addSpawn(EntityEnderminy.class, EZConfig.ENTITIES.ENDERMINY.spawnWeight, EZConfig.ENTITIES.ENDERMINY.spawnMin, EZConfig.ENTITIES.ENDERMINY.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityEnderman.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityEnderminy.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.EPIC_SQUID.spawnWeight > 0 && EZConfig.ENTITIES.EPIC_SQUID.enableEntity) {
            EntityRegistry.addSpawn(EntityEpicSquid.class, EZConfig.ENTITIES.EPIC_SQUID.spawnWeight, EZConfig.ENTITIES.EPIC_SQUID.spawnMin, EZConfig.ENTITIES.EPIC_SQUID.spawnMax, EnumCreatureType.WATER_CREATURE, getBiomeTypes(BiomeDictionary.Type.NETHER));
            EntitySpawnPlacementRegistry.setPlacementType(EntityEpicSquid.class, IN_LAVA);
        }

        if (EZConfig.ENTITIES.FALLEN_KNIGHT.spawnWeight > 0 && EZConfig.ENTITIES.FALLEN_KNIGHT.enableEntity) {
            EntityRegistry.addSpawn(EntityFallenKnight.class, EZConfig.ENTITIES.FALLEN_KNIGHT.spawnWeight, EZConfig.ENTITIES.FALLEN_KNIGHT.spawnMin, EZConfig.ENTITIES.FALLEN_KNIGHT.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityZombie.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityFallenKnight.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.FALLEN_STEED.spawnWeight > 0 && EZConfig.ENTITIES.FALLEN_STEED.enableEntity) {
            EntityRegistry.addSpawn(EntityFallenSteed.class, EZConfig.ENTITIES.FALLEN_STEED.spawnWeight, EZConfig.ENTITIES.FALLEN_STEED.spawnMin, EZConfig.ENTITIES.FALLEN_STEED.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityZombie.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityFallenSteed.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.OWL.spawnWeight > 0 && EZConfig.ENTITIES.OWL.enableEntity) {
            EntityRegistry.addSpawn(EntityOwl.class, EZConfig.ENTITIES.OWL.spawnWeight, EZConfig.ENTITIES.OWL.spawnMin, EZConfig.ENTITIES.OWL.spawnMax, EnumCreatureType.CREATURE, getBiomeTypes(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.RIVER));
            EntitySpawnPlacementRegistry.setPlacementType(EntityOwl.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.VOID_CUBE.spawnWeight > 0 && EZConfig.ENTITIES.VOID_CUBE.enableEntity) {
            EntityRegistry.addSpawn(EntityVoidCube.class, EZConfig.ENTITIES.VOID_CUBE.spawnWeight, EZConfig.ENTITIES.VOID_CUBE.spawnMin, EZConfig.ENTITIES.VOID_CUBE.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityEnderman.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityVoidCube.class, ON_BEDROCK);
        }

        if (EZConfig.ENTITIES.WITCH_CAT.spawnWeight > 0 && EZConfig.ENTITIES.WITCH_CAT.enableEntity) {
            EntityRegistry.addSpawn(EntityWitchCat.class, EZConfig.ENTITIES.WITCH_CAT.spawnWeight, EZConfig.ENTITIES.WITCH_CAT.spawnMin, EZConfig.ENTITIES.WITCH_CAT.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityWitch.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityWitchCat.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }

        if (EZConfig.ENTITIES.WITHER_WITCH.spawnWeight > 0 && EZConfig.ENTITIES.WITHER_WITCH.enableEntity) {
            EntityRegistry.addSpawn(EntityWitherWitch.class, EZConfig.ENTITIES.WITHER_WITCH.spawnWeight, EZConfig.ENTITIES.WITHER_WITCH.spawnMin, EZConfig.ENTITIES.WITHER_WITCH.spawnMax, EnumCreatureType.MONSTER, getEntityBiomes(EntityWitch.class));
            EntitySpawnPlacementRegistry.setPlacementType(EntityWitherWitch.class, EntityLiving.SpawnPlacementType.ON_GROUND);
        }
    }

    // Gets biomes from selected entity.
    public static Biome[] getEntityBiomes(Class<? extends Entity> spawn) {
        List<Biome> biomes = new ArrayList<>();

        for (Biome biome : ForgeRegistries.BIOMES) {
            List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);

            for (Biome.SpawnListEntry list : spawnList) {
                if (list.entityClass == spawn) {
                    biomes.add(biome);
                    break;
                }
            }
        }

        return biomes.toArray(new Biome[0]);
    }

    // Get all biome types.
    public static Biome[] getBiomeTypes(BiomeDictionary.Type... types) {
        Set<Biome> biomes = new HashSet<>();

        for (BiomeDictionary.Type type : types) {
            biomes.addAll(BiomeDictionary.getBiomes(type));
        }
        return biomes.toArray(new Biome[0]);
    }
}
