package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.misc.EnderExplosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityEnderChargePrimed extends EntityChargePrimed {
    public EntityEnderChargePrimed(World world) {
        super(world);
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
    }

    public EntityEnderChargePrimed(World world, double x, double y, double z, EntityLivingBase igniter) {
        super(world, x, y, z, igniter);
    }

    @Override
    public void explode() {
        EnderExplosion.create(this.world, this, this.posX, this.posY, this.posZ, 4.0F, false, false);
    }
}
