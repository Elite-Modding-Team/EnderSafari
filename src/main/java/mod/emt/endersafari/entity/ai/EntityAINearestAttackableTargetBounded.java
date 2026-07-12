package mod.emt.endersafari.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAINearestAttackableTargetBounded<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
    private double distanceOverride = -1;
    private double vertDistOverride = -1;
    private final int targetChance;

    public EntityAINearestAttackableTargetBounded(EntityCreature creature, Class<T> classTarget, boolean checkSight) {
        this(creature, classTarget, checkSight, false);
    }

    public EntityAINearestAttackableTargetBounded(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        this(creature, classTarget, 10, checkSight, onlyNearby, null);
    }

    public EntityAINearestAttackableTargetBounded(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, final Predicate<? super T> targetSelector) {
        super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
        targetChance = chance;
    }

    public void setMaxDistanceToTarget(double distance) {
        this.distanceOverride = distance;
    }

    public void setMaxVerticalDistanceToTarget(double vertDist) {
        this.vertDistOverride = vertDist;
    }

    @Override
    protected double getTargetDistance() {
        if (distanceOverride >= 0) {
            return distanceOverride;
        }
        return super.getTargetDistance();
    }

    @Override
    public boolean shouldExecute() {
        if (getTargetDistance() > 0 && (targetChance <= 0 || taskOwner.getRNG().nextInt(targetChance) == 0)) {
            double horizDist = getTargetDistance();
            double vertDist = getVerticalDistance();

            AxisAlignedBB bb = taskOwner.getEntityBoundingBox().expand(horizDist, vertDist, horizDist);
            List<T> list = taskOwner.getEntityWorld().<T>getEntitiesWithinAABB(targetClass, bb, Predicates.<T>and(targetEntitySelector, EntitySelectors.NOT_SPECTATING));
            list.sort(sorter);

            if (!list.isEmpty()) {
                final T t = list.get(0);
                if (t != null) {
                    this.targetEntity = t;
                    return true;
                }
            }
        }
        return false;
    }

    private double getVerticalDistance() {
        if (vertDistOverride >= 0) {
            return vertDistOverride;
        }
        return 4.0D;
    }
}