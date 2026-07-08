package mod.emt.enderzoo.entity.navigator;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateGroundMounted extends PathNavigateGround {
    private int ticksStuck = 0;
    private Vec3d lastPosition = Vec3d.ZERO;

    public PathNavigateGroundMounted(EntityLiving entity, World world) {
        super(entity, world);
    }

    @Override
    protected void pathFollow() {
        if (!this.entity.isRiding()) {
            super.pathFollow();
            return;
        }

        Vec3d entityPos = this.getEntityPosition();
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            double distanceMoved = entityPos.squareDistanceTo(this.lastPosition);
            if (distanceMoved < 0.005D) {
                this.ticksStuck++;
            } else {
                this.ticksStuck = 0;
            }

            if (this.ticksStuck > 15) {
                this.clearPath();
                this.ticksStuck = 0;
                this.lastPosition = entityPos;
                return;
            }
        } else {
            this.ticksStuck = 0;
        }
        this.lastPosition = entityPos;

        if (this.currentPath != null && !this.currentPath.isFinished()) {
            Vec3d waypointPos = this.currentPath.getCurrentPos();
            float width = this.entity.getRidingEntity() != null ? this.entity.getRidingEntity().width : this.entity.width;
            float tolerance = width > 0.75F ? width / 2.0F : 0.75F - width / 2.0F;
            double maxHeightDifference = (this.entity.posY - waypointPos.y > 0) ? 2.0D : 1.0D;

            if (MathHelper.abs((float) (this.entity.posX - waypointPos.x)) < tolerance && MathHelper.abs((float) (this.entity.posZ - waypointPos.z)) < tolerance && Math.abs(this.entity.posY - waypointPos.y) < maxHeightDifference) {
                this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
            }
        }

        if (this.currentPath != null && !this.currentPath.isFinished()) {
            int totalLength = this.currentPath.getCurrentPathLength();
            float actualWidth = this.entity.getRidingEntity() != null ? this.entity.getRidingEntity().width : this.entity.width;
            float actualHeight = this.entity.getRidingEntity() != null ? this.entity.getRidingEntity().height : this.entity.height;
            int widthCeil = MathHelper.ceil(actualWidth);
            int heightCeil = MathHelper.ceil(actualHeight);

            for (int index = totalLength - 1; index >= this.currentPath.getCurrentPathIndex(); --index) {
                if (this.isDirectPathBetweenPoints(entityPos, this.currentPath.getVectorFromIndex(this.entity, index), widthCeil, heightCeil, widthCeil)) {
                    this.currentPath.setCurrentPathIndex(index);
                    break;
                }
            }
        }

        this.checkForStuck(entityPos);
    }
}