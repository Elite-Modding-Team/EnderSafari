package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.registry.ModSoundEventsES;
import mod.emt.endersafari.utils.ParticleUtil;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityRestlessHero extends EntityMob {
    public static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityRestlessHero.class, DataSerializers.VARINT);

    //private boolean spawned = false;

    public EntityRestlessHero(World world) {
        super(world);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 20;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntitySkeleton.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntitySpider.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVindicator.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntitySilverfish.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TYPE, 0);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.RESTLESS_HERO.armor);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.RESTLESS_HERO.movementSpeed);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ESConfig.ENTITIES.RESTLESS_HERO.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.RESTLESS_HERO.maxHealth);
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
    public IEntityLivingData onInitialSpawn(@NotNull DifficultyInstance diff, IEntityLivingData data) {
        int chance = this.rand.nextInt(100);
        if (chance < 50) { // 50% Chance
            this.setType(1); // Alex
        } else {
            this.setType(0); // Steve
        }

        if (rand.nextDouble() < 0.5) {
            setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
        }
        if (rand.nextDouble() < 0.5) {
            setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
        }
        if (rand.nextDouble() < 0.6) {
            setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
        }
        if (rand.nextDouble() < 0.5) {
            setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
        }
        if (rand.nextDouble() < 0.5) {
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        }
        if (rand.nextDouble() < 0.5) {
            setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        }
        if (rand.nextDouble() < 0.5) {
            setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.GOLDEN_AXE));
        }
        return super.onInitialSpawn(diff, data);
    }

    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            /*if (!spawned) {
                spawned = true;
                for (int i = 0; i < 40; i++) {
                    double angle = rand.nextDouble() * Math.PI * 2.0D;
                    double radius = 0.05D + rand.nextDouble() * 0.18D;
                    float x = (float) (posX + Math.cos(angle) * radius);
                    float y = (float) (posY + 0.9D + rand.nextDouble() * 0.5D);
                    float z = (float) (posZ + Math.sin(angle) * radius);
                    float vx = (float) (Math.cos(angle) * (0.02D + rand.nextDouble() * 0.025D));
                    float vy = (float) (0.015D + rand.nextDouble() * 0.035D);
                    float vz = (float) (Math.sin(angle) * (0.02D + rand.nextDouble() * 0.025D));
                    ParticleUtil.spawnParticleGlow(world, x, y, z, vx, vy, vz, 26, 38, 35, 0.5F, 8.0F + rand.nextFloat() * 5.0F, 40 + rand.nextInt(20));
                }
            }*/
            float xDest = (float) posX + 0.12F * (rand.nextFloat() - 0.5F);
            float yDest = (float) posY + 1.65F + 0.25F * rand.nextFloat();
            float zDest = (float) posZ + 0.12F * (rand.nextFloat() - 0.5F);
            float x = (float) posX + 0.18F * (rand.nextFloat() - 0.5F);
            float y = (float) posY + 1.0F;
            float z = (float) posZ + 0.18F * (rand.nextFloat() - 0.5F);
            ParticleUtil.spawnParticleGlow(world, x, y, z, (xDest - x) / 30.0F, (yDest - y) / 30.0F, (zDest - z) / 30.0F, 26, 38, 35, 0.5F, 10.0F, 100);
        }
    }

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
        if (source == DamageSource.CACTUS) return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public @NotNull EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSoundEventsES.ENTITY_RESTLESS_HERO_HURT.getSoundEvent();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSoundEventsES.ENTITY_RESTLESS_HERO_DEATH.getSoundEvent();
    }

    @Override
    protected @NotNull SoundEvent getFallSound(int heightIn) {
        return null;
    }

    @Override
    public boolean isPreventingPlayerRest(@NotNull EntityPlayer player) {
        return false;
    }
}