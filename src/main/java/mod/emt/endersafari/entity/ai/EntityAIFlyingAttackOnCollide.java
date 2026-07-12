package mod.emt.endersafari.entity.ai;

import mod.emt.endersafari.entity.IESFlyingMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIFlyingAttackOnCollide extends EntityAIBase {
    private final EntityCreature attacker;
    private int attackTick;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path entityPathEntity;
    private Class<? extends Entity> classTarget;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;
    private final IESFlyingMob flyingMob;

    public EntityAIFlyingAttackOnCollide(IESFlyingMob mob, Class<? extends Entity> targetClass, double speed, boolean useLongMemory) {
        this(mob, speed, useLongMemory);
        this.classTarget = targetClass;
    }

    public EntityAIFlyingAttackOnCollide(IESFlyingMob mob, double speed, boolean useLongMemory) {
        this.flyingMob = mob;
        this.attacker = mob.asEntityCreature();
        this.speedTowardsTarget = speed;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = attacker.getAttackTarget();
        if (target == null) {
            return false;
        } else if (!target.isEntityAlive()) {
            return false;
        } else if (classTarget != null && !classTarget.isAssignableFrom(target.getClass())) {
            return false;
        }

        if (canPenalize) {
            if (--delayCounter <= 0) {
                setPathTo(target);
                targetX = 4 + attacker.getRNG().nextInt(7);
                return entityPathEntity != null;
            } else {
                return true;
            }
        }
        setPathTo(target);
        return this.entityPathEntity != null;
    }

    private void setPathTo(EntityLivingBase target) {
        Vec3d targPos = target.getPositionVector();
        AxisAlignedBB targBB = target.getEntityBoundingBox();
        entityPathEntity = attacker.getNavigator().getPathToPos(new BlockPos(targPos.x, targBB.maxY + 1, targPos.z));
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase target = attacker.getAttackTarget();
        if (target == null || !target.isEntityAlive()) {
            return false;
        }
        return !longMemory ? !attacker.getNavigator().noPath() : attacker.isWithinHomeDistanceFromPosition(new BlockPos(target));
    }

    @Override
    public void startExecuting() {
        flyingMob.getFlyingNavigator().setPath(entityPathEntity, speedTowardsTarget, true);
        delayCounter = 0;
    }

    @Override
    public void resetTask() {
        attacker.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        if (target == null) {
            return;
        }
        attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        double distToTargSq = attacker.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRangSq = getAttackRangeSq(target);
        --delayCounter;

        if ((longMemory || attacker.getEntitySenses().canSee(target)) && delayCounter <= 0 && (targetX == 0.0D && targetY == 0.0D && targetZ == 0.0D || target.getDistanceSq(targetX, targetY, targetZ) >= 1.0D || attacker.getRNG().nextFloat() < 0.05F)) {
            targetX = target.posX;
            targetY = target.getEntityBoundingBox().minY;
            targetZ = target.posZ;
            delayCounter = 4 + attacker.getRNG().nextInt(7);

            if (canPenalize) {
                targetX += failedPathFindingPenalty;
                final Path path = attacker.getNavigator().getPath();
                if (path != null) {
                    PathPoint finalPathPoint = path.getFinalPathPoint();
                    if (finalPathPoint != null && target.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                        failedPathFindingPenalty = 0;
                    else
                        failedPathFindingPenalty += 10;
                } else {
                    failedPathFindingPenalty += 10;
                }
            }

            if (distToTargSq > 1024) {
                delayCounter += 10;
            } else if (distToTargSq > 256) {
                delayCounter += 5;
            }

            if (!flyToAttacker(target)) {
                delayCounter += 15;
            }
        }

        attackTick = Math.max(attackTick - 1, 0);

        if (distToTargSq <= attackRangSq && attackTick <= 0) {
            attackTick = 20;
            attacker.swingArm(EnumHand.MAIN_HAND);
            attacker.attackEntityAsMob(target);
        }
    }

    private boolean flyToAttacker(EntityLivingBase target) {
        AxisAlignedBB targBB = target.getEntityBoundingBox();
        return flyingMob.getFlyingNavigator().tryFlyToPos(target.posX, targBB.maxY + 0.5, target.posZ, speedTowardsTarget);
    }

    protected double getAttackRangeSq(EntityLivingBase target) {
        return (attacker.width * 2.0F * attacker.width * 2.0F + target.width);
    }
}
