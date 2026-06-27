package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.entity.navigator.PathNavigateFly;
import net.minecraft.entity.EntityCreature;

public interface IEZFlyingMob {
    float getMaxTurnRate();

    float getMaxClimbRate();

    PathNavigateFly getFlyingNavigator();

    EntityCreature asEntityCreature();
}
