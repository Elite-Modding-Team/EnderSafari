package mod.emt.endersafari.entity;

import mod.emt.endersafari.entity.navigator.PathNavigateFly;
import net.minecraft.entity.EntityCreature;

public interface IESFlyingMob {
    float getMaxTurnRate();

    float getMaxClimbRate();

    PathNavigateFly getFlyingNavigator();

    EntityCreature asEntityCreature();
}
