package mod.emt.endersafari.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nonnull;

public interface IESOwnable<O extends EntityCreature, T extends EntityLivingBase> {
    T getOwner();

    void setOwner(T owner);

    @Nonnull
    O asEntity();
}
