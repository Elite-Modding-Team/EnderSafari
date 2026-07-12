package mod.emt.endersafari.entity;

import mod.emt.endersafari.misc.ConcussionExplosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityConcussionChargePrimed extends EntityChargePrimed {
    public EntityConcussionChargePrimed(World world) {
        super(world);
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
    }

    public EntityConcussionChargePrimed(World world, double x, double y, double z, EntityLivingBase igniter) {
        super(world, x, y, z, igniter);
    }

    @Override
    public void explode() {
        ConcussionExplosion.create(this.world, this, this.posX, this.posY, this.posZ, 4.0F, false, false);
    }
}
