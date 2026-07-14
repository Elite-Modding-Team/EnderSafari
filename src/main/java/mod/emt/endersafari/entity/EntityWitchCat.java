package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.entity.ai.*;
import mod.emt.endersafari.registry.ModLootTablesES;
import mod.emt.endersafari.registry.ModSoundEventsES;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityWitchCat extends EntityMob implements IESOwnable<EntityWitchCat, EntityWitherWitch> {
    private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityWitchCat.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> GROWTH_MODE = EntityDataManager.createKey(EntityWitchCat.class, DataSerializers.VARINT);

    private static final float DEF_HEIGHT = 0.8F;
    private static final float DEF_WIDTH = 0.6F;
    private static final float ANGRY_SCALE = 2.0F;
    private static final float SCALE_INC = 0.05F;

    private static final UUID ATTACK_BOOST_MOD_UID = UUID.fromString("B9662B59-9566-4402-BC1F-2ED2B276D846");
    private static final UUID HEALTH_BOOST_MOD_UID = UUID.fromString("B9662B29-9467-3302-1D1A-2ED2B276D846");

    private GrowthMode lastGrowthMode = GrowthMode.NONE;
    private float lastScale = 1.0F;
    private int noTargetTicks = 0;
    private EntityWitherWitch owner;
    private UUID ownerUUID;

    public EntityWitchCat(World world) {
        super(world);
        this.setSize(DEF_WIDTH, DEF_HEIGHT);
    }

    @Override
    protected void initEntityAI() {
        EntityAIFollowOwnerES retreatTask = new EntityAIFollowOwnerES(this, 2.5D, 5.0D, 2.5D);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollideOwned(this, 1.0D, false, retreatTask));
        this.tasks.addTask(2, new EntityAIFollowOwnerES(this, 2.5D, 10.0D, 1.0D));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.setSize(DEF_WIDTH, DEF_HEIGHT);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.WITCH_CAT.armor);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ESConfig.ENTITIES.WITCH_CAT.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.WITCH_CAT.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.WITCH_CAT.movementSpeed);
    }

    public enum GrowthMode {
        NONE,
        GROW,
        SHRINK
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SCALE, 1.0F);
        dataManager.register(GROWTH_MODE, GrowthMode.NONE.ordinal());
    }

    @Override
    public EntityWitherWitch getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(EntityWitherWitch owner) {
        this.owner = owner;
        if (owner != null) {
            this.ownerUUID = owner.getUniqueID();
            owner.registerCat(this);
        }
    }

    @Override
    public @NotNull EntityWitchCat asEntity() {
        return this;
    }

    public void setScale(float scale) {
        dataManager.set(SCALE, scale);
    }

    public float getScale() {
        return dataManager.get(SCALE);
    }

    public void setGrowthMode(GrowthMode mode) {
        dataManager.set(GROWTH_MODE, mode.ordinal());
    }

    public GrowthMode getGrowthMode() {
        return GrowthMode.values()[dataManager.get(GROWTH_MODE)];
    }

    public float getAngryScale() {
        return ANGRY_SCALE;
    }

    public float getScaleInc() {
        return SCALE_INC;
    }

    public boolean isAngry() {
        return getScale() >= ANGRY_SCALE;
    }

    @Override
    public boolean isPotionApplicable(@NotNull PotionEffect potion) {
        return potion.getPotion() != MobEffects.WITHER && potion.getPotion() != MobEffects.POISON && potion.getPotion() != MobEffects.LEVITATION
                && potion.getPotion() != MobEffects.SLOWNESS && super.isPotionApplicable(potion);
    }

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
        if (owner != null && source.getTrueSource() == owner) {
            return false;
        }

        boolean res = super.attackEntityFrom(source, amount);
        if (!world.isRemote && res && source.getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
            if (owner != null) {
                if (owner.getRevengeTarget() == null) {
                    owner.setRevengeTarget(attacker);
                }
            } else {
                if (!(attacker instanceof EntityPlayer) || !((EntityPlayer) attacker).isCreative()) {
                    setAttackTarget(attacker);
                }
            }
        }
        return res;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (owner != null) {
            owner.catDied(this);
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (world.isRemote) {
            float scale = getScale();
            if (lastScale != scale) {
                spawnParticles();
                lastScale = scale;
                setSize(DEF_WIDTH * scale, DEF_HEIGHT * scale);
            }
            return;
        }

        if (this.owner == null && this.ownerUUID != null) {
            for (EntityWitherWitch witch : this.world.getEntitiesWithinAABB(EntityWitherWitch.class, this.getEntityBoundingBox().grow(16.0D))) {
                if (witch.getUniqueID().equals(this.ownerUUID)) {
                    this.setOwner(witch);
                    break;
                }
            }
        }

        if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
            this.setAttackTarget(null);
        }

        if (this.getAttackTarget() == null) {
            this.noTargetTicks++;
            if (this.noTargetTicks > 40 && isAngry() && getGrowthMode() != GrowthMode.SHRINK) {
                this.setGrowthMode(GrowthMode.SHRINK);
            }
        } else {
            this.noTargetTicks = 0;
            if (!isAngry() && getGrowthMode() != GrowthMode.GROW) {
                this.setGrowthMode(GrowthMode.GROW);
            }
        }

        if (this.getGrowthMode() != this.lastGrowthMode) {
            if (this.getGrowthMode() == GrowthMode.GROW || this.getGrowthMode() == GrowthMode.SHRINK) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, ModSoundEventsES.ENTITY_WITCH_CAT_GROW.getSoundEvent(), this.getSoundCategory(), 1.0F, 0.5F + (this.rand.nextFloat() * 0.5F));
            }
            this.lastGrowthMode = this.getGrowthMode();
        }

        updateScale();

        float scale = getScale();
        if (lastScale != scale) {
            lastScale = scale;
            setSize(DEF_WIDTH * scale, DEF_HEIGHT * scale);
            float growthRatio = (lastScale - 1.0F) / (ANGRY_SCALE - 1.0F);
            updateAttackDamage(growthRatio);
            updateHealth(growthRatio);
        }
    }

    public void updateScale() {
        GrowthMode curMode = getGrowthMode();
        if (curMode == GrowthMode.NONE) return;

        float scale = getScale();
        if (curMode == GrowthMode.GROW) {
            if (scale < ANGRY_SCALE) {
                setScale(Math.min(scale + SCALE_INC, ANGRY_SCALE));
            } else {
                setScale(ANGRY_SCALE);
                setGrowthMode(GrowthMode.NONE);
            }
        } else {
            if (scale > 1.0F) {
                setScale(Math.max(1.0F, scale - SCALE_INC));
            } else {
                setScale(1.0F);
                setGrowthMode(GrowthMode.NONE);
            }
        }
    }

    protected void updateAttackDamage(float growthRatio) {
        IAttributeInstance att = EntityUtil.removeModifier(this, SharedMonsterAttributes.ATTACK_DAMAGE, ATTACK_BOOST_MOD_UID);
        if (growthRatio == 0 || att == null) return;

        double attackDif = ESConfig.ENTITIES.WITCH_CAT.attackDamageExtra;
        double toAdd = attackDif * growthRatio;
        AttributeModifier mod = new AttributeModifier(ATTACK_BOOST_MOD_UID, "Transformed Attack Modifier", toAdd, 0);
        att.applyModifier(mod);
    }

    protected void updateHealth(float growthRatio) {
        IAttributeInstance att = EntityUtil.removeModifier(this, SharedMonsterAttributes.MAX_HEALTH, HEALTH_BOOST_MOD_UID);
        if (att == null) return;

        double oldMaxHealth = att.getAttributeValue();
        if (growthRatio > 0) {
            double healthDif = ESConfig.ENTITIES.WITCH_CAT.maxHealthExtra;
            double toAdd = healthDif * growthRatio;
            AttributeModifier mod = new AttributeModifier(HEALTH_BOOST_MOD_UID, "Transformed Health Modifier", toAdd, 0);
            att.applyModifier(mod);
        }

        double maxHealthDifference = att.getAttributeValue() - oldMaxHealth;
        if (maxHealthDifference != 0) {
            this.setHealth((float) (this.getHealth() + maxHealthDifference));
        }
    }

    private void spawnParticles() {
        double startX = posX;
        double startY = posY;
        double startZ = posZ;
        double offsetScale = 0.8 * getScale();
        for (int i = 0; i < 2; i++) {
            double xOffset = offsetScale - rand.nextFloat() * offsetScale * 2;
            double yOffset = offsetScale / 3 + rand.nextFloat() * offsetScale / 3 * 2F;
            double zOffset = offsetScale - rand.nextFloat() * offsetScale * 2;
            Particle fx = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.SPELL.getParticleID(), startX + xOffset, startY + yOffset, startZ + zOffset, 0.0D, 0.0D, 0.0D);
            if (fx != null) {
                fx.setRBGColorF(0.8f, 0.2f, 0.2f);
            }
        }
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        if (this.isAddedToWorld() && !this.world.isRemote) {
            this.setEntityBoundingBox(new AxisAlignedBB(x - (double) this.width / 2.0D, y, z - (double) this.width / 2.0D, x + (double) this.width / 2.0D, y + (double) this.height, z + (double) this.width / 2.0D));
        } else {
            super.setPosition(x, y, z);
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    protected void setSize(float width, float height) {
        if (width != this.width || height != this.height) {
            float f = this.width;
            this.width = width;
            this.height = height;
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) this.width, axisalignedbb.minY + (double) this.height, axisalignedbb.minZ + (double) this.width));
            if (this.width > f && !this.firstUpdate && !this.world.isRemote) {
                this.move(net.minecraft.entity.MoverType.SELF, f - this.width, 0.0D, f - this.width);
            }
        }
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("scale", getScale());
        compound.setByte("growthMode", (byte) getGrowthMode().ordinal());
        if (this.ownerUUID != null) {
            compound.setUniqueId("owner", this.ownerUUID);
        }
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("scale")) setScale(compound.getFloat("scale"));
        if (compound.hasKey("growthMode")) setGrowthMode(GrowthMode.values()[compound.getByte("growthMode")]);
        if (compound.hasUniqueId("owner")) {
            this.ownerUUID = compound.getUniqueId("owner");
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CAT_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.WITCH_CAT;
    }
}