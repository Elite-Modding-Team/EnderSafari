package mod.emt.enderzoo.entity.ai;

import mod.emt.enderzoo.entity.IEZOwnable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

import javax.annotation.Nonnull;

public class EntityAIFollowOwnerEZ extends EntityAIBase {
    @Nonnull
    IEZOwnable<? extends EntityCreature, ? extends EntityLivingBase> owned;
    double followSpeed;
    private int pathingTimer;
    private final double minDistanceSq;
    private final double maxDistanceSq;

    public EntityAIFollowOwnerEZ(@Nonnull IEZOwnable<? extends EntityCreature, ? extends EntityLivingBase> owned, double minDist, double maxDist, double followSpeed) {
        this.owned = owned;
        minDistanceSq = minDist * minDist;
        maxDistanceSq = maxDist * maxDist;
        this.followSpeed = followSpeed;
    }

    @Override
    public boolean shouldExecute() {
        if (owned.getOwner() == null) {
            return false;
        }

        return getDistanceSqFromOwner() > maxDistanceSq;
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase owner = owned.getOwner();

        if (owner == null || !owner.isEntityAlive()) {
            return false;
        }

        return !owned.asEntity().getNavigator().noPath();
    }

    public boolean isWithinTargetDistanceFromOwner() {
        if (owned.getOwner() == null) {
            return true;
        }

        double distance = getDistanceSqFromOwner();
        return distance >= minDistanceSq && distance <= maxDistanceSq;
    }

    private double getDistanceSqFromOwner() {
        final EntityLivingBase owner = owned.getOwner();
        return owner == null ? 0 : owned.asEntity().getDistanceSq(owner);
    }

    @Override
    public void startExecuting() {
        pathingTimer = 0;
    }

    @Override
    public void updateTask() {
        EntityLivingBase owner = owned.getOwner();
        if (owner == null) {
            return;
        }

        double distance = getDistanceSqFromOwner();
        if (distance < minDistanceSq) {
            owned.asEntity().getNavigator().clearPath();
        }

        if (--pathingTimer <= 0) {
            pathingTimer = 10;
            owned.asEntity().getNavigator().tryMoveToEntityLiving(owner, followSpeed);
        }
    }
}
