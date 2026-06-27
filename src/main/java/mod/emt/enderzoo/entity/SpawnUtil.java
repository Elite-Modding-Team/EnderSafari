package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.entity.navigator.PlanarBlockIterator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SpawnUtil {
    public static BlockPos findClearGround(@Nonnull World world, @Nonnull BlockPos startingLocation) {
        return findClearGround(world, startingLocation, 2, 10, false);
    }

    public static BlockPos findClearGround(@Nonnull World world, @Nonnull BlockPos startingLocation, int horizRange, int vertRange, boolean checkForLivingEntities) {
        for (PlanarBlockIterator itr = new PlanarBlockIterator(startingLocation, PlanarBlockIterator.Orientation.EAST_WEST, horizRange); itr.hasNext(); ) {
            BlockPos location = SpawnUtil.seachYForClearGround(itr.next(), world, vertRange, checkForLivingEntities);
            if (location != null) {
                return location;
            }
        }
        return null;
    }

    public static BlockPos seachYForClearGround(@Nonnull BlockPos target, @Nonnull World world) {
        return seachYForClearGround(target, world, 10, false);
    }

    public static BlockPos seachYForClearGround(@Nonnull BlockPos startingLocation, @Nonnull World world, int searchRange, boolean checkForLivingEntities) {
        MutableBlockPos pos = new MutableBlockPos(startingLocation);
        while (!world.isAirBlock(pos)) {
            pos.move(EnumFacing.UP);
            if (pos.getY() > 255 || pos.distanceSq(startingLocation) > searchRange * searchRange) {
                return null;
            }
        }
        while (world.isAirBlock(pos.down()) || isLiquid(world, pos.down())) {
            pos.move(EnumFacing.DOWN);
            if (pos.getY() < 0 || pos.distanceSq(startingLocation) > searchRange * searchRange) {
                return null;
            }
        }
        if (checkForLivingEntities && containsLiving(world, pos)) {
            return null;
        }
        return pos.toImmutable();

    }

    public static boolean containsLiving(@Nonnull World world, @Nonnull BlockPos pos) {
        return !world.checkNoEntityCollision(new AxisAlignedBB(pos));
    }

    public static boolean isLiquid(@Nonnull World world, @Nonnull BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isLiquid();
    }

    public static boolean isSpaceAvailableForSpawn(World world, EntityLiving entity, EntityCreature asCreature, boolean checkEntityCollisions, boolean canSpawnInLiquid) {
        if (asCreature != null && asCreature.getBlockPathWeight(entity.getPosition()) < 0) {
            return false;
        }
        if (checkEntityCollisions && !world.checkNoEntityCollision(entity.getEntityBoundingBox())) {
            return false;
        }
        if (!world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty()) {
            return false;
        }
        if (!canSpawnInLiquid && world.containsAnyLiquid(entity.getEntityBoundingBox())) {
            return false;
        }
        return true;
    }

    public static boolean isSpaceAvailableForSpawn(World world, EntityCreature entity, boolean checkEntityCollisions) {
        return isSpaceAvailableForSpawn(world, entity, entity, checkEntityCollisions, false);
    }

    public static boolean isSpaceAvailableForSpawn(World world, EntityLiving entity, boolean checkEntityCollisions, boolean canSpawnInLiquid) {
        return isSpaceAvailableForSpawn(world, entity, entity instanceof EntityCreature ? ((EntityCreature) entity) : null, checkEntityCollisions, canSpawnInLiquid);
    }

    public static boolean isSpaceAvailableForSpawn(World world, EntityLiving spawn, boolean checkEntityCollisions) {
        return isSpaceAvailableForSpawn(world, spawn, checkEntityCollisions, false);
    }

}
