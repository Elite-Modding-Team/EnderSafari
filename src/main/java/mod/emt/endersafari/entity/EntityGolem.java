package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.entity.projectile.EffectDamage;
import mod.emt.endersafari.entity.projectile.EntityFireball;
import mod.emt.endersafari.registry.ModLootTablesES;
import mod.emt.endersafari.registry.ModSoundEventsES;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class EntityGolem extends EntityMob {
    public static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityGolem.class, DataSerializers.VARINT);

    public EntityGolem(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        this.experienceValue = 10;
        this.isImmuneToFire = true;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 0.46D, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.46D));
        this.tasks.addTask(7, new EntityAIWander(this, 0.46D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TYPE, 0);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.GOLEM.movementSpeed);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ESConfig.ENTITIES.GOLEM.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.GOLEM.maxHealth);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("type", this.dataManager.get(TYPE));
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(TYPE, compound.getInteger("type"));
    }

    public int getType() {
        return this.dataManager.get(TYPE);
    }

    public void setType(int skinType) {
        this.dataManager.set(TYPE, skinType);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        entityLivingData = super.onInitialSpawn(difficulty, entityLivingData);

        if (BiomeDictionary.hasType(this.getEntityWorld().getBiome(this.getPosition()), BiomeDictionary.Type.END)) {
            this.setType(6); // End Stone
        } else if (BiomeDictionary.hasType(this.getEntityWorld().getBiome(this.getPosition()), BiomeDictionary.Type.NETHER)) {
            this.setType(5); // Netherrack
        } else {
            int chance = this.rand.nextInt(100);
            if (chance < 20) { // 20% Chance
                this.setType(1 + this.rand.nextInt(3)); // Andesite, Diorite, Granite
            } else if (chance < 22) { // 2% Chance
                this.setType(4); // Obsidian
            } else {
                this.setType(0); // Stone
            }
        }

        return entityLivingData;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = this.rotationYawHead;
        if (!this.isDead && this.getHealth() > 0 && this.ticksExisted % 100 == 0 && this.getAttackTarget() != null) {
            if (!getEntityWorld().isRemote) {
                playSound(ModSoundEventsES.ENTITY_FIREBALL_LAUNCH.getSoundEvent(), 1.0F, 1.0F);
                EntityFireball proj = new EntityFireball(getEntityWorld());
                EffectDamage effect = new EffectDamage(8.0F, DamageSource.causeThrownDamage(proj, proj.getShooter()), 4, 1.0f);
                proj.initCustom(posX, posY + 1.6D, posZ, getLookVec().x * 0.5D, getLookVec().y * 0.5D, getLookVec().z * 0.5D, 4.0F, this);
                proj.setEffect(effect);
                if (this.getType() == 0) {
                    proj.setColor(2186938); // Lapis Lazuli
                } else if (this.getType() == 4) {
                    proj.setColor(4910545); // Diamond
                } else if (this.getType() == 5) {
                    proj.setColor(14404799); // Quartz
                } else if (this.getType() == 6) {
                    proj.setColor(9397391); // Purpur
                } else {
                    proj.setColor(4322180); // Emerald
                }
                getEntityWorld().spawnEntity(proj);
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(@NotNull Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            playSound(ModSoundEventsES.ENTITY_GOLEM_PUNCH.getSoundEvent(), 1.0F, 1.0F);
            return true;
        } else
            return false;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSoundEventsES.ENTITY_GOLEM_DEATH.getSoundEvent();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSoundEventsES.ENTITY_GOLEM_HURT.getSoundEvent();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull Block block) {
        super.playStepSound(pos, block);
        this.playSound(ModSoundEventsES.ENTITY_GOLEM_STEP.getSoundEvent(), 1.0F, 1.0F);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        if (this.getType() == 1) {
            return ModLootTablesES.ANDESITE_GOLEM;
        } else if (this.getType() == 2) {
            return ModLootTablesES.DIORITE_GOLEM;
        } else if (this.getType() == 3) {
            return ModLootTablesES.GRANITE_GOLEM;
        } else if (this.getType() == 4) {
            return ModLootTablesES.OBSIDIAN_GOLEM;
        } else if (this.getType() == 5) {
            return ModLootTablesES.NETHERRACK_GOLEM;
        } else if (this.getType() == 6) {
            return ModLootTablesES.END_STONE_GOLEM;
        }

        return ModLootTablesES.STONE_GOLEM;
    }
}
