package mod.emt.endersafari.entity.navigator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateFly extends PathNavigateGround {
    private int totalTicksLocal;
    private int ticksAtLastPos;
    private @Nonnull Vec3d lastPosCheck = new Vec3d(0.0D, 0.0D, 0.0D);
    private boolean forceFlying = false;

    public PathNavigateFly(EntityLiving entity, World world) {
        super(entity, world);
    }

    public boolean isForceFlying() {
        return forceFlying && !noPath();
    }

    public void setForceFlying(boolean forceFlying) {
        this.forceFlying = forceFlying;
    }

    @Override
    protected @Nonnull PathFinder getPathFinder() {
        nodeProcessor = new FlyNodeProcessor();
        return new FlyingPathFinder(nodeProcessor);
    }

    @Override
    protected boolean canNavigate() {
        return true;
    }

    @Override
    protected @Nonnull Vec3d getEntityPosition() {
        int y = (int) (entity.getEntityBoundingBox().minY + 0.5D);
        return new Vec3d(entity.posX, y, entity.posZ);
    }

    public boolean tryFlyToXYZ(double x, double y, double z, double speed) {
        Path pathEntity = getPathToPos(new BlockPos(MathHelper.floor(x), (int) y, (double) MathHelper.floor(z)));
        return setPath(pathEntity, speed, true);
    }

    public boolean tryFlyToPos(double x, double y, double z, double speed) {
        Path pathEntity = getPathToXYZ(x, y, z);
        return setPath(pathEntity, speed, true);
    }

    public boolean tryFlyToEntityLiving(@Nonnull Entity entity, double speed) {
        Path pathEntity = getPathToEntityLiving(entity);
        return pathEntity != null && setPath(pathEntity, speed, true);
    }

    public boolean setPath(@Nullable Path path, double speed, boolean forceFlying) {
        if (super.setPath(path, speed)) {
            ticksAtLastPos = totalTicksLocal;
            lastPosCheck = getEntityPosition();
            this.forceFlying = forceFlying;
            return true;
        }
        return false;
    }

    @Override
    public boolean setPath(@Nullable Path path, double speed) {
        return setPath(path, speed, false);
    }

    @Override
    public void onUpdateNavigation() {
        ++totalTicksLocal;
        if (!noPath()) { // If we have a path
            pathFollow(); // Follow it
            if (!noPath() && currentPath != null) { // If we haven't finished, then set the new move point
                Vec3d targetPos = currentPath.getPosition(entity);
                double y = targetPos.y;
                if (forceFlying) {
                    double aboveBlock = y - (int) y;
                    if (aboveBlock < 0.10) {
                        y = (int) y + 0.10;
                    }
                }
                entity.getMoveHelper().setMoveTo(targetPos.x, y, targetPos.z, speed);
            }
        }

    }

    @Override
    protected void pathFollow() {
        Vec3d entPos = getEntityPosition();
        float entWidthSq = entity.width * entity.width;
        final Path currentPath2 = currentPath;
        if (currentPath2 == null) {
            return;
        }
        if (currentPath2.getCurrentPathIndex() == currentPath2.getCurrentPathLength() - 1 && entity.onGround) {
            entWidthSq = 0.01f; // We need to be right on top of the last point if on
            // the ground so we don't hang on ledges
        }

        Vec3d targetPos = currentPath2.getVectorFromIndex(entity, currentPath2.getCurrentPathIndex());

        double distToCurrTargSq = entPos.squareDistanceTo(targetPos);
        if (distToCurrTargSq < entWidthSq) {
            currentPath2.incrementPathIndex();
        }
        // Starting six points ahead (or the end point) see if we can go directly
        // there
        int i = 6;
        for (int j = Math.min(currentPath2.getCurrentPathIndex() + i, currentPath2.getCurrentPathLength() - 1); j > currentPath2.getCurrentPathIndex(); --j) {
            targetPos = currentPath2.getVectorFromIndex(entity, j);
            if (targetPos.squareDistanceTo(entPos) <= 36.0D && isDirectPathBetweenPoints(entPos, targetPos, 0, 0, 0)) {
                currentPath2.setCurrentPathIndex(j);
                break;
            }
        }
        checkForStuck(entPos);
    }

    @Override
    protected boolean isDirectPathBetweenPoints(@Nonnull Vec3d startPos, @Nonnull Vec3d endPos, int sizeX, int sizeY, int sizeZ) {
        Vec3d target = new Vec3d(endPos.x, endPos.y + entity.height * 0.5D, endPos.z);
        if (!isClear(startPos, target)) {
            return false;
        }
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        startPos = new Vec3d(bb.maxX, bb.maxY, bb.maxZ);
        if (!isClear(startPos, target)) {
            return false;
        }
        return true;

    }

    private boolean isClear(@Nonnull Vec3d startPos, @Nonnull Vec3d target) {
        RayTraceResult hit = world.rayTraceBlocks(startPos, target, true, true, false);
        return hit == null || hit.typeOfHit == RayTraceResult.Type.MISS;
    }

    @Override
    protected void checkForStuck(@Nonnull Vec3d positionVec3) {
        if (totalTicksLocal - ticksAtLastPos > 10 && positionVec3.squareDistanceTo(lastPosCheck) < 0.0625) {
            clearPath();
            ticksAtLastPos = totalTicksLocal;
            lastPosCheck = positionVec3;
            return;
        }

        if (totalTicksLocal - ticksAtLastPos > 50) {
            if (positionVec3.squareDistanceTo(lastPosCheck) < 2.25D) {
                clearPath();
            }

            ticksAtLastPos = totalTicksLocal;
            lastPosCheck = positionVec3;
        }
    }
}
