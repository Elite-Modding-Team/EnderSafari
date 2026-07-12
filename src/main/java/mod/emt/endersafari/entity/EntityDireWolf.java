package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.registry.ModLootTablesES;
import mod.emt.endersafari.registry.ModSoundEventsES;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityDireWolf extends EntityMob {
    private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.createKey(EntityDireWolf.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityDireWolf.class, DataSerializers.VARINT);

    private EntityLivingBase previousAttackTarget;
    private static int packHowl = 0;
    private static long lastHowl = 0;

    public EntityDireWolf(World world) {
        super(world);
        this.setSize(0.8F, 1.2F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.2D, true));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_ANGRY, false);
        this.dataManager.register(TYPE, 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.DIRE_WOLF.armor);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ESConfig.ENTITIES.DIRE_WOLF.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.DIRE_WOLF.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.DIRE_WOLF.movementSpeed);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isAngry", this.dataManager.get(IS_ANGRY));
        compound.setInteger("type", this.dataManager.get(TYPE));
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(IS_ANGRY, compound.getBoolean("isAngry"));
        this.dataManager.set(TYPE, compound.getInteger("type"));
    }

    public boolean isAngry() {
        return this.dataManager.get(IS_ANGRY);
    }

    public void setAngry(boolean angry) {
        this.dataManager.set(IS_ANGRY, angry);
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
        if (BiomeDictionary.hasType(this.getEntityWorld().getBiome(this.getPosition()), BiomeDictionary.Type.SNOWY)) {
            this.setType(1);
        }

        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.world.isRemote) {
            EntityLivingBase currentTarget = this.getAttackTarget();
            if (currentTarget != this.previousAttackTarget) {
                if (currentTarget != null) {
                    this.alertPack(currentTarget);
                }
                this.previousAttackTarget = currentTarget;
                this.setAngry(currentTarget != null);
            }
        }
    }

    private void alertPack(EntityLivingBase target) {
        double range = 16.0D;
        AxisAlignedBB box = new AxisAlignedBB(this.posX - range, this.posY - range, this.posZ - range, this.posX + range, this.posY + range, this.posZ + range);
        List<EntityDireWolf> pack = this.world.getEntitiesWithinAABB(EntityDireWolf.class, box);
        for (EntityDireWolf wolf : pack) {
            if (wolf.getAttackTarget() == null) {
                wolf.setAttackTarget(target);
            }
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos pos = new BlockPos(i, j, k);
        // TODO: A config list of blocks it can spawn on would be very nice
        return this.world.getBlockState(pos.down()).getBlock() == Blocks.GRASS && super.getCanSpawnHere();
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 6;
    }

    public float getTailRotation() {
        if (this.isAngry()) {
            return (float) Math.PI / 2.0F;
        }
        return (float) Math.PI / 4.0F;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull Block blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return ModSoundEventsES.ENTITY_DIRE_WOLF_GROWL.getSoundEvent();
        }

        boolean howl = (packHowl > 0 || this.rand.nextFloat() <= ESConfig.ENTITIES.DIRE_WOLF.howlChance) && this.world.getTotalWorldTime() > (lastHowl + 20L);
        if (howl) {
            if (packHowl <= 0 && this.rand.nextFloat() <= 0.25F) {
                packHowl = 3 + this.rand.nextInt(3);
            }
            lastHowl = this.world.getTotalWorldTime();
            packHowl = Math.max(packHowl - 1, 0);
            return ModSoundEventsES.ENTITY_DIRE_WOLF_HOWL.getSoundEvent();
        } else {
            return ModSoundEventsES.ENTITY_DIRE_WOLF_GROWL.getSoundEvent();
        }
    }

    @Override
    public void playSound(@Nonnull SoundEvent sound, float volume, float pitch) {
        if (ModSoundEventsES.ENTITY_DIRE_WOLF_HOWL.getSoundEvent().equals(sound)) {
            volume *= (float) ESConfig.ENTITIES.DIRE_WOLF.howlVolume;
            pitch *= 0.8F;
        }

        super.playSound(sound, volume, pitch);
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSoundEventsES.ENTITY_DIRE_WOLF_HURT.getSoundEvent();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSoundEventsES.ENTITY_DIRE_WOLF_DEATH.getSoundEvent();
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.DIRE_WOLF;
    }
}