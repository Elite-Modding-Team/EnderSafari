package mod.emt.endersafari.entity.navigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import mod.emt.endersafari.entity.SpawnUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class FlyingPathFinder extends PathFinder {
    private final @Nonnull PathHeap path = new PathHeap();
    private final @Nonnull PathPoint[] pathOptions = new PathPoint[32];
    private final @Nonnull NodeProcessor nodeProcessor;

    public FlyingPathFinder(@Nonnull NodeProcessor nodeProcessorIn) {
        super(nodeProcessorIn);
        this.nodeProcessor = nodeProcessorIn;
    }

    // createEntityPathTo
    @Override
    public Path findPath(@Nonnull IBlockAccess blockaccess, @Nonnull EntityLiving entityFrom, @Nonnull Entity entityTo, float dist) {
        return createEntityPathTo(blockaccess, entityFrom, entityTo.posX, entityTo.getEntityBoundingBox().minY, entityTo.posZ, dist);
    }

    // createEntityPathTo
    @Override
    public Path findPath(@Nonnull IBlockAccess blockaccess, @Nonnull EntityLiving entity, @Nonnull BlockPos targetPos, float dist) {
        return createEntityPathTo(blockaccess, entity, targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F, dist);
    }

    private Path createEntityPathTo(@Nonnull IBlockAccess blockaccess, @Nonnull Entity entity, double x, double y, double z, float distance) {
        path.clearPath();
        if (!(entity instanceof EntityLiving)) {
            return null;
        }
        EntityLiving entityIn = (EntityLiving) entity;
        nodeProcessor.init(blockaccess, entityIn);

        PathPoint startPoint = nodeProcessor.getStart();
        PathPoint endPoint = nodeProcessor.getPathPointToCoords(x, y, z);
        Vec3d targ = new Vec3d(x, y, z);
        Vec3d ePos = entityIn.getPositionVector();
        double yDelta = targ.y - ePos.y;
        double horizDist = new Vec3d(x, 0, z).distanceTo(new Vec3d(ePos.x, 0, ePos.z));

        int climbY = 0;
        if (horizDist > 4 && entityIn.onGround) {
            climbY = 1 * MathHelper.clamp((int) (horizDist / 8), 1, 3);
            if (yDelta >= 1) {
                climbY += (int) yDelta;
            } else {
                climbY++;
            }
        }

        if (climbY == 0) {
            return createDefault(blockaccess, entityIn, distance, x, y, z);
        }

        // Climb, then descend
        double climbDistance = Math.min(horizDist / 2.0, climbY);

        Vec3d horizDirVec = new Vec3d(targ.x, 0, targ.z);
        horizDirVec = horizDirVec.subtract(new Vec3d(ePos.x, 0, ePos.z));
        horizDirVec = horizDirVec.normalize();
        Vec3d offset = new Vec3d(horizDirVec.x * climbDistance, climbY, horizDirVec.z * climbDistance);

        PathPoint climbPoint = new PathPoint(rnd(startPoint.x + offset.x), rnd(startPoint.y + offset.y), rnd(startPoint.z + offset.z));
        if (!SpawnUtil.isSpaceAvailableForSpawn(entityIn.world, entityIn, false)) {
            return createDefault(blockaccess, entityIn, distance, x, y, z);
        }

        PathPoint[] points = addToPath(entityIn, startPoint, climbPoint, distance);
        nodeProcessor.postProcess();

        if (points == null) { // Failed to climb so go default
            return createDefault(blockaccess, entityIn, distance, x, y, z);
        }
        List<PathPoint> resPoints = new ArrayList<>(Arrays.asList(points));

        // then path from the climb point to destination
        path.clearPath();
        nodeProcessor.init(blockaccess, entityIn);
        climbPoint = new PathPoint(climbPoint.x, climbPoint.y, climbPoint.z);
        points = addToPath(entityIn, climbPoint, endPoint, distance);
        nodeProcessor.postProcess();

        if (points == null) {
            return createDefault(blockaccess, entityIn, distance, x, y, z);
        }
        resPoints.addAll(Arrays.asList(points));
        if (resPoints.isEmpty()) {
            return null;
        }
        return new Path(resPoints.toArray(new PathPoint[resPoints.size()]));
    }

    private PathPoint[] addToPath(@Nonnull Entity entity, @Nonnull PathPoint pointStart, @Nonnull PathPoint pointEnd, float maxDistance) {
        // Set start point values
        PPUtil.setTotalPathDistance(pointStart, 0f);
        float dist = pointStart.distanceToSquared(pointEnd);
        PPUtil.setDistanceToNext(pointStart, dist);
        PPUtil.setDistanceToTarget(pointStart, dist);
        PPUtil.setIndex(pointStart, -1);

        // Clear and add out start point to the path
        path.clearPath();
        path.addPoint(pointStart);
        PathPoint curPoint = pointStart;

        // while still points in the path
        while (!path.isPathEmpty()) {

            PathPoint dequeued = path.dequeue();

            // We are at the end
            if (dequeued.equals(pointEnd)) {
                return createEntityPath(pointStart, pointEnd);
            }

            // If the dequed point is closer to the ned that our current one, make it
            // the current point
            if (dequeued.distanceToSquared(pointEnd) < curPoint.distanceToSquared(pointEnd)) {
                curPoint = dequeued;
            }
            dequeued.visited = true;

            // Find options for the next point in the path

            int numPathOptions = nodeProcessor.findPathOptions(pathOptions, dequeued, pointEnd, maxDistance);
            for (int j = 0; j < numPathOptions; ++j) {
                PathPoint cadidatePoint = pathOptions[j];
                if (cadidatePoint != null) {
                    float newTotalDistance = PPUtil.getTotalPathDistance(dequeued) + dequeued.distanceToSquared(cadidatePoint);
                    if (newTotalDistance < maxDistance * 2.0F && (!cadidatePoint.isAssigned() || newTotalDistance < PPUtil.getTotalPathDistance(cadidatePoint))) {
                        PPUtil.setPrevious(cadidatePoint, dequeued);
                        PPUtil.setTotalPathDistance(cadidatePoint, newTotalDistance);
                        PPUtil.setDistanceToNext(cadidatePoint, cadidatePoint.distanceToSquared(pointEnd));
                        if (cadidatePoint.isAssigned()) {
                            path.changeDistance(cadidatePoint, PPUtil.getTotalPathDistance(cadidatePoint) + PPUtil.getDistanceToNext(cadidatePoint));
                        } else {
                            PPUtil.setDistanceToTarget(cadidatePoint, PPUtil.getTotalPathDistance(cadidatePoint) + PPUtil.getDistanceToNext(cadidatePoint));
                            path.addPoint(cadidatePoint);
                        }
                    }
                }
            }
        }

        if (curPoint == pointStart) {
            return null;
        } else {
            return createEntityPath(pointStart, curPoint);
        }
    }

    private int rnd(double d) {
        return (int) Math.round(d);
    }

    private Path createDefault(@Nonnull IBlockAccess blockaccess, @Nonnull EntityLiving entity, float distance, double x, double y, double z) {
        this.path.clearPath();
        this.nodeProcessor.init(blockaccess, entity);
        PathPoint pathpoint = nodeProcessor.getStart();
        PathPoint pathpoint1 = nodeProcessor.getPathPointToCoords(x, y, z);
        PathPoint[] p = addToPath(entity, pathpoint, pathpoint1, distance);
        Path res;
        if (p == null) {
            res = null;
        } else {
            res = new Path(p);
        }
        this.nodeProcessor.postProcess();
        return res;
    }

    private static PathPoint[] createEntityPath(PathPoint start, PathPoint end) {
        int i = 1;

        for (PathPoint pathpoint = end; pathpoint != null && PPUtil.getPrevious(pathpoint) != null; pathpoint = PPUtil.getPrevious(pathpoint)) {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint1 = end;
        --i;
        for (apathpoint[i] = end; pathpoint1 != null && PPUtil.getPrevious(pathpoint1) != null; apathpoint[i] = pathpoint1) {
            pathpoint1 = PPUtil.getPrevious(pathpoint1);
            --i;
        }

        return apathpoint;
    }
}
