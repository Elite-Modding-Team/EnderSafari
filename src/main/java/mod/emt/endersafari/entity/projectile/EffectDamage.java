package mod.emt.endersafari.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;
import java.util.function.Function;

public class EffectDamage implements IESProjectileEffect {
    private float damage;
    private Function<IESProjectilePreset, DamageSource> damageSourceFactory;
    private int fire;
    private double invincibilityMultiplier;

    public EffectDamage(float damage, DamageSource damageSource, int fire, double invincibilityMultiplier) {
        this(damage, projectile -> damageSource, fire, invincibilityMultiplier);
    }

    public EffectDamage(float damage, Function<IESProjectilePreset, DamageSource> damageSourceFactory, int fire, double invincibilityMultiplier) {
        this.damage = damage;
        this.damageSourceFactory = damageSourceFactory;
        this.fire = fire;
        this.invincibilityMultiplier = invincibilityMultiplier;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Function<IESProjectilePreset, DamageSource> getDamageSourceFactory() {
        return damageSourceFactory;
    }

    public void setDamageSourceFactory(Function<IESProjectilePreset, DamageSource> damageSourceFactory) {
        this.damageSourceFactory = damageSourceFactory;
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int seconds) {
        this.fire = seconds;
    }

    public double getInvincibilityMultiplier() {
        return invincibilityMultiplier;
    }

    public void setInvincibilityMultiplier(double multiplier) {
        this.invincibilityMultiplier = multiplier;
    }

    @Override
    public void onEntityImpact(Entity entity, @Nullable IESProjectilePreset projectile) {
        Entity shooter = projectile != null ? projectile.getShooter() : null;
        DamageSource damageSource = damageSourceFactory.apply(projectile);

        if (entity.attackEntityFrom(damageSource, damage)) {
            entity.setFire(fire);
        }

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase livingTarget = (EntityLivingBase) entity;
            livingTarget.setLastAttackedEntity(shooter);

            if (shooter instanceof EntityLivingBase) {
                livingTarget.setRevengeTarget((EntityLivingBase) shooter);
            }

            livingTarget.hurtResistantTime = (int) (livingTarget.hurtResistantTime * invincibilityMultiplier);
        }
    }
}
