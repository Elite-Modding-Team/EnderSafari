package mod.emt.endersafari.entity.ai;

import mod.emt.endersafari.entity.IESOwnable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIAttackOnCollideOwned extends EntityAIAttackMelee {
    private final IESOwnable<? extends EntityCreature, ? extends EntityLivingBase> ownable;
    private boolean retreating;
    private final EntityAIFollowOwnerES followTask;

    public EntityAIAttackOnCollideOwned(IESOwnable<? extends EntityCreature, ? extends EntityLivingBase> ownable, double speed, boolean useLongMemory, EntityAIFollowOwnerES followTask) {
        super(ownable.asEntity(), speed, useLongMemory);
        this.ownable = ownable;
        this.followTask = followTask;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting() || retreating;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        retreating = false;
        followTask.resetTask();
    }

    @Override
    public void updateTask() {
        if (retreating) {
            followTask.updateTask();
            if (followTask.isWithinTargetDistanceFromOwner()) {
                retreating = false;
                followTask.resetTask();
                if (this.attacker.getAttackTarget() != null) {
                    super.startExecuting();
                }
            }
            return;
        }

        if (isTooFarFromOwner()) {
            retreating = true;
            followTask.startExecuting();
            return;
        }

        if (this.attacker.getAttackTarget() == null) {
            return;
        }

        super.updateTask();
    }

    private boolean isTooFarFromOwner() {
        if (ownable.getOwner() == null) {
            return false;
        }

        double distance = getDistanceSqFromOwner();
        double maxRange = ownable.asEntity().getNavigator().getPathSearchRange();
        maxRange = maxRange * maxRange;
        return distance > maxRange;
    }

    private double getDistanceSqFromOwner() {
        final EntityLivingBase owner = ownable.getOwner();
        return owner == null ? 0 : ownable.asEntity().getDistanceSq(owner);
    }
}
