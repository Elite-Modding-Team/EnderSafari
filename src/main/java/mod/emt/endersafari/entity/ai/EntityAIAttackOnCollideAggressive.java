package mod.emt.endersafari.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class EntityAIAttackOnCollideAggressive extends EntityAIBase {
    private final EntityLiving attacker;
    private int ticksToNextAttack;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path entityPathEntity;
    private Class<?> classTarget;
    private int ticksUntilNextPathingAttempt;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int attackFrequency = 20;

    public EntityAIAttackOnCollideAggressive(EntityLiving attacker, Class<?> targetClass, double attackSpeed, boolean longMemory) {
        this(attacker, attackSpeed, longMemory);
        this.classTarget = targetClass;
    }

    public EntityAIAttackOnCollideAggressive(EntityLiving attacker, double attackSpeed, boolean longMemory) {
        this.attacker = attacker;
        this.speedTowardsTarget = attackSpeed;
        this.longMemory = longMemory;
        this.setMutexBits(3);
    }

    public int getAttackFrequency() {
        return attackFrequency;
    }

    public @Nonnull EntityAIAttackOnCollideAggressive setAttackFrequency(int attackFrequency) {
        this.attackFrequency = attackFrequency;
        return this;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = attacker.getAttackTarget();

        if (entitylivingbase == null || !entitylivingbase.isEntityAlive()) {
            return false;
        } else if (classTarget != null && !classTarget.isAssignableFrom(entitylivingbase.getClass())) {
            return false;
        } else {
            if (--ticksUntilNextPathingAttempt <= 0) {
                entityPathEntity = attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
                ticksUntilNextPathingAttempt = 4 + attacker.getRNG().nextInt(7);

                if (entityPathEntity != null) {
                    return true;
                }

                return attacker.getDistanceSq(entitylivingbase) < 256.0D;
            }
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = attacker.getAttackTarget();
        if (entitylivingbase == null || !entitylivingbase.isEntityAlive()) {
            return false;
        }
        if (!longMemory) {
            return !attacker.getNavigator().noPath();
        }
        if (attacker instanceof EntityCreature) {
            return ((EntityCreature) attacker).isWithinHomeDistanceCurrentPosition();
        }
        return true;
    }

    @Override
    public void startExecuting() {
        if (entityPathEntity != null) {
            attacker.getNavigator().setPath(entityPathEntity, speedTowardsTarget);
        }
        ticksUntilNextPathingAttempt = 0;
    }

    @Override
    public void resetTask() {
        attacker.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        EntityLivingBase entitylivingbase = attacker.getAttackTarget();
        if (entitylivingbase == null) {
            return;
        }

        attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        double distToTargetSq = attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        double attackRange = attacker.width * 2.0F * attacker.width * 2.0F + entitylivingbase.width;
        --ticksUntilNextPathingAttempt;

        if ((longMemory || attacker.getEntitySenses().canSee(entitylivingbase)) && ticksUntilNextPathingAttempt <= 0 && (targetX == 0.0D && targetY == 0.0D && targetZ == 0.0D ||
                entitylivingbase.getDistanceSq(targetX, targetY, targetZ) >= 1.0D || attacker.getRNG().nextFloat() < 0.05F)) {

            targetX = entitylivingbase.posX;
            targetY = entitylivingbase.getEntityBoundingBox().minY;
            targetZ = entitylivingbase.posZ;

            if (distToTargetSq > 1024.0D) {
                ticksUntilNextPathingAttempt += 10;
            } else if (distToTargetSq > 256.0D) {
                ticksUntilNextPathingAttempt += 5;
            }

            if (!attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, speedTowardsTarget)) {
                ticksUntilNextPathingAttempt += 15;
                double dx = entitylivingbase.posX - attacker.posX;
                double dy = entitylivingbase.posY - attacker.posY;
                double dz = entitylivingbase.posZ - attacker.posZ;
                double r = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (r > 0) {
                    attacker.motionX += (dx / r) * 0.05D * speedTowardsTarget;
                    attacker.motionY += (dy / r) * 0.05D * speedTowardsTarget;
                    attacker.motionZ += (dz / r) * 0.05D * speedTowardsTarget;
                }
            }
        }

        ticksToNextAttack = Math.max(ticksToNextAttack - 1, 0);
        if (distToTargetSq <= attackRange && ticksToNextAttack <= 0) {
            ticksToNextAttack = attackFrequency;
            attacker.swingArm(EnumHand.MAIN_HAND);
            attacker.attackEntityAsMob(entitylivingbase);
        }
    }
}