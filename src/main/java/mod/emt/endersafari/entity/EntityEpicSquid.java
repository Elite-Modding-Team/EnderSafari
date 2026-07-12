package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.entity.ai.EntityAIAttackOnCollideAggressive;
import mod.emt.endersafari.registry.ModLootTablesES;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class EntityEpicSquid extends EntitySquid {
    private boolean inLava = false;

    public EntityEpicSquid(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityEpicSquid.AIMoveRandom(this));
        tasks.addTask(1, new EntityAIAttackOnCollideAggressive(this, 1.1D, true).setAttackFrequency(20));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.EPIC_SQUID.armor);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ESConfig.ENTITIES.EPIC_SQUID.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.EPIC_SQUID.maxHealth);
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        float f = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
    }

    @Override
    public void setRevengeTarget(EntityLivingBase entity) {
        super.setRevengeTarget(entity);
        if (getAttackTarget() == null) {
            setAttackTarget(entity);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 20.0D && this.posY < (double) this.world.getSeaLevel();
    }

    @Override
    public boolean handleWaterMovement() {
        super.handleWaterMovement();

        if (!this.inWater) {
            this.inLava = this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0D, -0.4D, 0.0D).shrink(0.001D), Material.LAVA, this);

            if (this.inLava) {
                this.fallDistance = 0.0F;
                this.inWater = true;
                this.extinguish();
            }
        } else {
            this.inLava = false;
        }

        return this.inWater;
    }

    static class AIMoveRandom extends EntityAIBase {
        private final EntityEpicSquid squid;

        public AIMoveRandom(EntityEpicSquid entity) {
            this.squid = entity;
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            int i = this.squid.getIdleTime();

            if (i > 100) {
                this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.hasMovementVector()) {
                float f = this.squid.getRNG().nextFloat() * ((float) Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;

                if (this.squid.inWater && this.squid.getRNG().nextBoolean()) {
                    f2 = Math.abs(f2);
                }

                if (this.squid.inLava && this.squid.world.getBlockState(this.squid.getPosition().up()).getMaterial() == Material.LAVA) {
                    f2 += 0.05F;
                }

                float f3 = MathHelper.sin(f) * 0.2F;
                this.squid.setMovementVector(f1, f2, f3);
            }
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.EPIC_SQUID;
    }
}
