package mod.emt.enderzoo.entity;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import javax.annotation.Nonnull;

public class TeleportHelper {
    private static final int DEFAULT_RND_TP_DISTANCE = 16;
    private static Random rand = new Random();

    public static boolean teleportRandomly(EntityLivingBase entity, int distance) {
        double d0 = entity.posX + (rand.nextDouble() - 0.5D) * distance;
        double d1 = entity.posY + rand.nextInt(distance + 1) - distance / 2;
        double d2 = entity.posZ + (rand.nextDouble() - 0.5D) * distance;
        return teleportTo(entity, d0, d1, d2, false);
    }

    public static boolean teleportRandomly(EntityLivingBase entity) {
        return teleportRandomly(entity, DEFAULT_RND_TP_DISTANCE);
    }

    public static boolean teleportToEntity(EntityLivingBase entity, Entity toEntity) {
        Vec3d vec3 = new Vec3d(entity.posX - toEntity.posX, entity.getEntityBoundingBox().minY + entity.height / 2.0F - toEntity.posY + toEntity.getEyeHeight(), entity.posZ - toEntity.posZ);
        vec3 = vec3.normalize();
        double d0 = 16.0D;
        double d1 = entity.posX + (rand.nextDouble() - 0.5D) * 8.0D - vec3.x * d0;
        double d2 = entity.posY + (rand.nextInt(16) - 8) - vec3.y * d0;
        double d3 = entity.posZ + (rand.nextDouble() - 0.5D) * 8.0D - vec3.z * d0;
        return teleportTo(entity, d1, d2, d3, false);
    }

    public static boolean teleportTo(EntityLivingBase entity, double x, double y, double z, boolean fireEndermanEvent) {
        EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);
        if (fireEndermanEvent) {
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            }
        }

        double origX = entity.posX;
        double origY = entity.posY;
        double origZ = entity.posZ;
        entity.posX = event.getTargetX();
        entity.posY = event.getTargetY();
        entity.posZ = event.getTargetZ();
        int xInt = MathHelper.floor(entity.posX);
        int yInt = Math.max(2, MathHelper.floor(entity.posY));
        int zInt = MathHelper.floor(entity.posZ);

        boolean doTeleport = false;
        World world = entity.getEntityWorld();
        if (world.isBlockLoaded(new BlockPos(xInt, yInt, zInt), true)) {
            boolean foundGround = false;
            while (!foundGround && yInt > 2) {
                IBlockState bs = world.getBlockState(new BlockPos(xInt, yInt - 1, zInt));
                if (bs != null && bs.getBlock() != null && bs.getMaterial().blocksMovement()) {
                    foundGround = true;
                } else {
                    --entity.posY;
                    --yInt;
                }
            }

            if (foundGround) {
                entity.setPosition(entity.posX, entity.posY, entity.posZ);
                if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty()
                        && !world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    doTeleport = true;
                } else if (yInt <= 0) {
                    doTeleport = false;
                }
            }
        }

        if (!doTeleport) {
            entity.setPosition(origX, origY, origZ);
            return false;
        }

        entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

        short short1 = 128;
        for (int l = 0; l < short1; ++l) {
            double d6 = l / (short1 - 1.0D);
            float f = (rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
            double d7 = origX + (entity.posX - origX) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            double d8 = origY + (entity.posY - origY) * d6 + rand.nextDouble() * entity.height;
            double d9 = origZ + (entity.posZ - origZ) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            world.spawnParticle(EnumParticleTypes.PORTAL, d7, d8, d9, f, f1, f2);
        }

        world.playSound(origX, origY, origZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
        entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        return true;
    }

    public static void teleportEntity(@Nonnull World world, @Nonnull Entity entity, boolean isItem, boolean dropToGround, float range) {
        if (entity instanceof FakePlayer) {
            return;
        }
        double origX = entity.posX, origY = MathHelper.clamp(entity.posY, 1, 255), origZ = entity.posZ;
        for (int i = 0; i < 15; i++) {
            double targetX = origX + rand.nextGaussian() * range;
            double targetY = -1;
            while (targetY < 1.1) {
                targetY = origY + rand.nextGaussian() * (range / 2);
            }
            double targetZ = origZ + rand.nextGaussian() * range;
            if (dropToGround) {
                targetY = MathHelper.floor(targetY) + .05;
                while (targetY >= 2f && !(hasGround(world, targetX, targetY, targetZ) && isClear(world, entity, targetX, targetY, targetZ))) {
                    targetY -= 1f;
                }
            }
            if (targetY >= 2f && isClear(world, entity, targetX, targetY, targetZ) && doTeleport(world, entity, targetX, targetY, targetZ)) {
                world.playSound(null, origX, origY, origZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1, 1);
                world.playSound(null, targetX, targetY, targetZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1, 1);
                entity.timeUntilPortal = 5;
                return;
            }
        }
    }

    private static boolean isClear(@Nonnull World world, @Nonnull Entity entity, double targetX, double targetY, double targetZ) {
        double origX = entity.posX, origY = entity.posY, origZ = entity.posZ;
        try {
            entity.setPosition(targetX, targetY, targetZ);
            boolean result = world.checkNoEntityCollision(entity.getEntityBoundingBox(), entity)
                    && world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox());
            return result;
        } finally {
            entity.setPosition(origX, origY, origZ);
        }
    }

    private static boolean hasGround(@Nonnull World world, double targetX, double targetY, double targetZ) {
        int xInt = MathHelper.floor(targetX);
        int yInt = MathHelper.floor(targetY);
        int zInt = MathHelper.floor(targetZ);
        return yInt > 1 && world.getBlockState(new BlockPos(xInt, yInt - 1, zInt)).getMaterial().blocksMovement();
    }

    private static boolean doTeleport(@Nonnull World world, @Nonnull Entity entity, double targetX, double targetY, double targetZ) {
        if (entity instanceof EntityLivingBase) {
            return doTeleport(world, (EntityLivingBase) entity, targetX, targetY, targetZ);
        }

        if (entity.isRiding()) {
            entity.dismountRidingEntity();
        }
        if (entity.isBeingRidden()) {
            entity.removePassengers();
        }

        entity.setPositionAndRotation(targetX, targetY, targetZ, entity.rotationYaw, entity.rotationPitch);
        return true;
    }

    private static boolean doTeleport(@Nonnull World world, @Nonnull EntityLivingBase entity, double targetX, double targetY, double targetZ) {
        float damage = 5f;
        if (entity.getMaxHealth() < 10f) {
            damage = 1f;
        }
        EnderTeleportEvent event = new EnderTeleportEvent(entity, targetX, targetY, targetZ, damage);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            if (rand.nextFloat() < 0.15F && world.getGameRules().getBoolean("doMobSpawning") && !(entity instanceof EntityEndermite)) {
                EntityEndermite mite = new EntityEndermite(world);
                mite.setSpawnedByPlayer(true);
                mite.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                world.spawnEntity(mite);
            }

            if (entity.isRiding()) {
                entity.dismountRidingEntity();
            }
            if (entity.isBeingRidden()) {
                entity.removePassengers();
            }

            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP) entity).connection.setPlayerLocation(event.getTargetX(), event.getTargetY(), event.getTargetZ(), entity.rotationYaw,
                        entity.rotationPitch);
            } else {
                entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            }
            //entity.fallDistance = 0.0F;
            //entity.attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
            return true;
        }
        return false;
    }
}
