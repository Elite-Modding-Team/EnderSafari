package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.config.EZConfig;
import mod.emt.enderzoo.entity.ai.*;
import mod.emt.enderzoo.entity.navigator.FlyingMoveHelper;
import mod.emt.enderzoo.entity.navigator.PathNavigateFly;
import mod.emt.enderzoo.registry.ModItemsEZ;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import mod.emt.enderzoo.registry.ModSoundEventsEZ;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityOwl extends EntityAnimal implements IEZFlyingMob {
    private float wingRotation;
    private float prevWingRotation;
    private float wingRotDelta = 1.0F;
    private float destPos;
    private float prevDestPos;
    private float bodyAngle = 5;
    private float targetBodyAngle = 0;
    private float wingAngle;
    private double groundSpeedRatio = 0.25;
    private float climbRate = 0.25f;
    private float turnRate = 30;
    private int flySoundCooldown = 0;

    public int timeUntilNextEgg;

    public EntityOwl(World worldIn) {
        super(worldIn);
        setSize(0.4F, 0.85F);
        stepHeight = 1.0F;
        moveHelper = new FlyingMoveHelper(this);
        timeUntilNextEgg = getNextLayingTime();
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAIFlyingPanic(this, 2));
        tasks.addTask(1, new EntityAIFlyingAttackOnCollide(this, 2.5, false));
        tasks.addTask(2, new EntityAIMate(this, 1.0));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.SPIDER_EYE, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.5));
        tasks.addTask(5, new EntityAIFlyingLand(this, 2));
        tasks.addTask(6, new EntityAIFlyingFindPerch(this, 2, 80));
        tasks.addTask(7, new EntityAIFlyingShortWander(this, 2, 150));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(9, new EntityAILookIdle(this));

        EntityAINearestAttackableTargetBounded<EntitySpider> targetSpiders = new EntityAINearestAttackableTargetBounded<>(this, EntitySpider.class, true, true);
        targetSpiders.setMaxDistanceToTarget(12.0D);
        targetSpiders.setMaxVerticalDistanceToTarget(24.0D);
        targetTasks.addTask(0, targetSpiders);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(EZConfig.ENTITIES.OWL.armor);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EZConfig.ENTITIES.OWL.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EZConfig.ENTITIES.OWL.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EZConfig.ENTITIES.OWL.movementSpeed);
    }

    @Override
    protected @NotNull PathNavigate createNavigator(@NotNull World world) {
        return new PathNavigateFly(this, world);
    }

    public PathNavigateFly getFlyingNavigator() {
        return (PathNavigateFly) getNavigator();
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        IBlockState bs = world.getBlockState(pos.down());
        return bs.getMaterial() == Material.LEAVES ? 10.0F : 0;
    }

    @Override
    public boolean attackEntityAsMob(@NotNull Entity entity) {
        super.attackEntityAsMob(entity);
        float attackDamage = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (entity instanceof EntitySpider) {
            attackDamage *= 2.0f;
        }
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        prevWingRotation = wingRotation;
        prevDestPos = destPos;
        destPos = (float) (destPos + (onGround ? -1 : 4) * 0.3D);
        destPos = MathHelper.clamp(destPos, 0.0F, 1.0F);
        if (!onGround && wingRotDelta < 1.0F) {
            wingRotDelta = 1.0F;
        }
        wingRotDelta = (float) (wingRotDelta * 0.9D);
        float flapSpeed = 2f;
        float yDelta = (float) Math.abs(posY - prevPosY);
        if (yDelta != 0) {
            // Normalize between 0 and 0.02
            flapSpeed *= MathHelper.clamp(yDelta / 0.02f, 0.75f, 1f);
        }

        float oldRotation = wingRotation;
        wingRotation += wingRotDelta * flapSpeed;

        if (flySoundCooldown > 0) {
            flySoundCooldown--;
        }

        if (!onGround && !isSilent()) {
            double cycle = Math.PI * 2;
            if (flySoundCooldown <= 0 && (oldRotation % cycle) > (wingRotation % cycle)) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PARROT_FLY, this.getSoundCategory(), 0.15F, 0.85F + (this.rand.nextFloat() * 0.3F));
                this.flySoundCooldown = 5 + this.rand.nextInt(5);
            }
        }

        if (!world.isRemote && !isChild() && --timeUntilNextEgg <= 0) {
            if (isOnLeaves()) {
                playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                dropItem(ModItemsEZ.OWL_EGG, 1);
            }
            timeUntilNextEgg = getNextLayingTime();
        }

        if (onGround) {
            motionX *= groundSpeedRatio;
            motionZ *= groundSpeedRatio;
        }
    }

    private boolean isOnLeaves() {
        IBlockState bs = world.getBlockState(getPosition().down());
        return bs.getMaterial() == Material.LEAVES;
    }

    private void calculateWingAngle(float partialTicks) {
        float flapComletion = prevWingRotation + (wingRotation - prevWingRotation) * partialTicks;
        float onGroundTimerThing = prevDestPos + (destPos - prevDestPos) * partialTicks;
        wingAngle = (MathHelper.sin(flapComletion) + 1.0F) * onGroundTimerThing;

        if (onGround) {
            wingAngle = (float) Math.toRadians(3);
        }
    }

    private void calculateBodyAngle(float partialTicks) {
        if (onGround) {
            bodyAngle = 7;
            targetBodyAngle = 7;
            return;
        }

        // Ignore y as we want no tilt going straight up or down
        Vec3d motionVec = new Vec3d(motionX, 0, motionZ);
        double speed = motionVec.length();
        // Normalize between 0 - 0.1
        speed = Math.min(1, speed * 10);
        targetBodyAngle = 20 + ((float) speed * 30);

        if (targetBodyAngle == bodyAngle) {
            return;
        }
        if (targetBodyAngle > bodyAngle) {
            bodyAngle += (2 * partialTicks);
            if (bodyAngle > targetBodyAngle) {
                bodyAngle = targetBodyAngle;
            }
        } else {
            bodyAngle -= (1 * partialTicks);
            if (bodyAngle < targetBodyAngle) {
                bodyAngle = targetBodyAngle;
            }
        }
    }

    public void calculateAngles(float partialTicks) {
        calculateBodyAngle(partialTicks);
        calculateWingAngle(partialTicks);
    }

    public float getBodyAngle() {
        return (float) Math.toRadians(bodyAngle);
    }

    public float getWingAngle() {
        return wingAngle;
    }

    @Override
    public float getEyeHeight() {
        return height - 0.1F;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @NotNull IBlockState block, @NotNull BlockPos pos) {
    }

    @Override
    public int getTalkInterval() {
        return EZConfig.ENTITIES.OWL.hootInterval;
    }

    @Override
    public void playLivingSound() {
        if (world.isDaytime() || getAttackTarget() != null) {
            return;
        }
        playSound(getAmbientSound(), getSoundVolume() * (float) EZConfig.ENTITIES.OWL.hootVolume, 0.8f * getSoundPitch());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEventsEZ.ENTITY_OWL_HOOT.getSoundEvent();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSoundEventsEZ.ENTITY_OWL_HURT.getSoundEvent();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEventsEZ.ENTITY_OWL_DEATH.getSoundEvent();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull Block block) {
        playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    @Override
    public EntityOwl createChild(@NotNull EntityAgeable ageable) {
        return new EntityOwl(world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.SPIDER_EYE;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return ModLootTablesEZ.OWL;
    }

    @Override
    public float getMaxTurnRate() {
        return turnRate;
    }

    @Override
    public float getMaxClimbRate() {
        return climbRate;
    }

    @Override
    public EntityCreature asEntityCreature() {
        return this;
    }

    private int getNextLayingTime() {
        int dif = 24000 - 12000;
        return 12000 + rand.nextInt(dif);
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    @Override
    public boolean canBeLeashedTo(@NotNull EntityPlayer player) {
        return !this.getLeashed();
    }
}
