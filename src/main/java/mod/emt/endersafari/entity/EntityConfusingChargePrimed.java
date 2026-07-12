package mod.emt.endersafari.entity;

import mod.emt.endersafari.misc.ConfusingExplosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityConfusingChargePrimed extends EntityChargePrimed {
    public EntityConfusingChargePrimed(World world) {
        super(world);
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
    }

    public EntityConfusingChargePrimed(World world, double x, double y, double z, EntityLivingBase igniter) {
        super(world, x, y, z, igniter);
    }

    @Override
    public void explode() {
        ConfusingExplosion.create(this.world, this, this.posX, this.posY, this.posZ, 4.0F, false, false);
    }
}
