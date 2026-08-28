package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.registry.ModLootTablesES;
import mod.emt.endersafari.registry.ModSoundEventsES;
import mod.emt.endersafari.utils.ParticleUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntitySpriteling extends EntityFlying implements IFaeMob {
    public static final DataParameter<Integer> DASH_TIMER = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.VARINT);
    public static final DataParameter<BlockPos> SPAWN_POSITION = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<Float> TARGET_DIRECTION_X = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> TARGET_DIRECTION_Y = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.FLOAT);
    public static final DataParameter<BlockPos> TARGET_POSITION = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<Integer> TWIRL_START = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> TWIRL_TIMER = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntitySpriteling.class, DataSerializers.VARINT);

    public Vec3d moveVec = new Vec3d(0, 0, 0);
    public Vec3d prevMoveVec = new Vec3d(0, 0, 0);
    public static float speedup = 1;

    public EntitySpriteling(World world) {
        super(world);
        setSize(0.5F, 0.5F);
        this.isAirBorne = true;
        this.noClip = true;
        this.experienceValue = 10;
        this.rotationYaw = rand.nextInt(240) + 60;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.SPRITELING.armor);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.SPRITELING.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.SPRITELING.movementSpeed);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(DASH_TIMER, 0);
        this.getDataManager().register(SPAWN_POSITION, new BlockPos(0, -1, 0));
        this.getDataManager().register(TARGET_DIRECTION_X, 0.0F);
        this.getDataManager().register(TARGET_DIRECTION_Y, 0.0F);
        this.getDataManager().register(TARGET_POSITION, new BlockPos(0, -1, 0));
        this.getDataManager().register(TWIRL_START, 0);
        this.getDataManager().register(TWIRL_TIMER, 0);
        this.getDataManager().register(TYPE, 0);
    }
    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("spawnX", this.getDataManager().get(SPAWN_POSITION).getX());
        compound.setInteger("spawnY", this.getDataManager().get(SPAWN_POSITION).getY());
        compound.setInteger("spawnZ", this.getDataManager().get(SPAWN_POSITION).getZ());
        compound.setInteger("targetX", this.getDataManager().get(TARGET_POSITION).getX());
        compound.setInteger("targetY", this.getDataManager().get(TARGET_POSITION).getY());
        compound.setInteger("targetZ", this.getDataManager().get(TARGET_POSITION).getZ());
        compound.setInteger("type", this.getDataManager().get(TYPE));
    }


    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.getDataManager().set(SPAWN_POSITION, new BlockPos(compound.getInteger("spawnX"), compound.getInteger("spawnY"), compound.getInteger("spawnZ")));
        this.getDataManager().set(TARGET_POSITION, new BlockPos(compound.getInteger("targetX"), compound.getInteger("targetY"), compound.getInteger("targetZ")));
        this.getDataManager().set(TYPE, compound.getInteger("type"));
    }

    public int getType() {
        return this.getDataManager().get(TYPE);
    }

    public void setType(int skinType) {
        this.getDataManager().set(TYPE, skinType);
    }

    private int[] getParticleColor() {
        switch (this.getType()) {
            default:
                return new int[]{139, 255, 82};
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        IBlockState state = this.world.getBlockState((new BlockPos(this)).down());
        return this.isValidLightLevel() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F && state.canEntitySpawn(this);
    }

    protected boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        } else {
            int i = this.world.getLightFromNeighbors(blockpos);

            if (this.world.isThundering()) {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }

    public float getBlockPathWeight(BlockPos pos) {
        return 0.0F;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();

        if (this.getDataManager().get(SPAWN_POSITION).getY() < 0) {
            this.getDataManager().set(SPAWN_POSITION, getPosition());
            this.getDataManager().set(TARGET_POSITION, getPosition());
        }

        if (this.getAttackTarget() == null) {
            if (this.getDataManager().get(TARGET_POSITION).distanceSq((int) this.posX, (int) this.posY, (int) this.posZ) < 3.0D || this.rand.nextInt(30) == 0) {
                BlockPos bestTarget = null;
                int bestScore = Integer.MIN_VALUE;

                for (int i = 0; i < 8; i++) {
                    BlockPos candidate = new BlockPos(
                            this.getDataManager().get(SPAWN_POSITION).getX() + this.rand.nextInt(15) - this.rand.nextInt(15),
                            this.getDataManager().get(SPAWN_POSITION).getY() + this.rand.nextInt(11) - 2,
                            this.getDataManager().get(SPAWN_POSITION).getZ() + this.rand.nextInt(15) - this.rand.nextInt(15)
                    );

                    int score = 0;
                    if (this.world.isAirBlock(candidate)) {
                        score += 10;
                    }

                    if (this.world.isAirBlock(candidate.up())) {
                        score += 5;
                    }

                    if (this.world.isAirBlock(candidate.down())) {
                        score += 2;
                    }

                    score += this.rand.nextInt(4);
                    if (score > bestScore) {
                        bestScore = score;
                        bestTarget = candidate;
                    }
                }

                if (bestTarget != null) {
                    this.getDataManager().set(TARGET_POSITION, bestTarget);
                }
            }

            BlockPos target = this.getDataManager().get(TARGET_POSITION);
            double targetX = target.getX() + 0.5D;
            double targetY = target.getY() + 0.5D;
            double targetZ = target.getZ() + 0.5D;

            this.getDataManager().set(TARGET_DIRECTION_X, (float) Math.toRadians(EntityUtil.yawDegreesBetweenPointsSafe(this.posX, this.posZ, targetX, targetZ, this.getDataManager().get(TARGET_DIRECTION_X))));
            this.getDataManager().set(TARGET_DIRECTION_Y, (float) Math.toRadians(EntityUtil.pitchDegreesBetweenPoints(this.posX, this.posY, this.posZ, targetX, targetY, targetZ)));
        } else {
            EntityLivingBase target = this.getAttackTarget();
            if (target != null && target.isEntityAlive()) {
                this.getDataManager().set(TARGET_DIRECTION_X, (float) Math.toRadians(EntityUtil.yawDegreesBetweenPointsSafe(posX, posZ, target.posX, target.posZ, this.getDataManager().get(TARGET_DIRECTION_X))));
                this.getDataManager().set(TARGET_DIRECTION_Y, (float) Math.toRadians(EntityUtil.pitchDegreesBetweenPoints(posX, posY, posZ, target.posX, target.posY + target.getEyeHeight() / 2.0D, target.posZ)));

                if (this.getDataManager().get(DASH_TIMER) <= 0 && this.ticksExisted % 40 == 0 && this.rand.nextInt(4) == 0) {
                    this.getDataManager().set(DASH_TIMER, 20);
                    this.getDataManager().set(TWIRL_TIMER, 20);
                    this.getDataManager().set(TWIRL_START, this.ticksExisted);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.world.isRemote) {
            int[] color = getParticleColor();
            ParticleUtil.spawnParticleGlowBurst(this.world, (float) posX + width * 0.5f * (this.rand.nextFloat() - 0.5f), (float) posY + height * 0.5f + height * (this.rand.nextFloat() - 0.5f), (float) posZ + width * 0.5f * (this.rand.nextFloat() - 0.5f),
                    0.0F, 0.0F, 0.0F, color[0], color[1], color[2], 0.5F, 1.5F + 1.5F * this.rand.nextFloat(), 30, true);

            if (getDataManager().get(DASH_TIMER) > 0) {
                ParticleUtil.spawnParticleSparkleBurst(this.world, (float) posX + ((world.rand.nextFloat()) - 0.5F) * 0.2F, (float) posY + 0.25F + ((world.rand.nextFloat()) - 0.5F) * 0.2F, (float) posZ + ((world.rand.nextFloat()) - 0.5F) * 0.2F,
                        -0.15F * (float) moveVec.x, -0.15F * (float) moveVec.y, -0.15F * (float) moveVec.z, color[0], color[1], color[2], 1.0F, 2.0F + 2.0F * this.rand.nextFloat(), 30, true);
            }
        }

        if (this.getHealth() <= 0.0F) {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.moveVec = Vec3d.ZERO;
            this.prevMoveVec = Vec3d.ZERO;
            return;
        }

        if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
            this.setAttackTarget(null);
        }

        if (this.getDataManager().get(TWIRL_TIMER) > 0) {
            this.getDataManager().set(TWIRL_TIMER, this.getDataManager().get(TWIRL_TIMER) - 1);
        } else if (this.getDataManager().get(TWIRL_START) != 0) {
            this.getDataManager().set(TWIRL_START, 0);
        }

        if (this.getDataManager().get(DASH_TIMER) > 0) {
            this.getDataManager().set(DASH_TIMER, this.getDataManager().get(DASH_TIMER) - 1);
        }

        if (this.ticksExisted % 5 == 0) {
            this.prevMoveVec = this.moveVec;
            double speed;
            if (this.getAttackTarget() != null) {
                if (this.getDataManager().get(DASH_TIMER) > 0) {
                    speed = 0.4D;
                } else {
                    speed = 0.225D;
                }
            } else {
                speed = 0.15D;
            }
            this.moveVec = EntityUtil.lookVector(this.getDataManager().get(TARGET_DIRECTION_X), this.getDataManager().get(TARGET_DIRECTION_Y)).scale(speed);
        }

        float motionInterp = ((float) (this.ticksExisted % 5)) / 5.0F;
        this.motionX = (1.0F - motionInterp) * this.prevMoveVec.x + motionInterp * this.moveVec.x;
        this.motionY = (1.0F - motionInterp) * this.prevMoveVec.y + motionInterp * this.moveVec.y;
        this.motionZ = (1.0F - motionInterp) * this.prevMoveVec.z + motionInterp * this.moveVec.z;

        if (Math.abs(this.motionX) > 0.001D || Math.abs(this.motionY) > 0.001D || Math.abs(this.motionZ) > 0.001D) {
            this.rotationYaw = (float) Math.toRadians(EntityUtil.yawDegreesBetweenPointsSafe(0, 0, this.motionX, this.motionZ, this.rotationYaw));
            this.rotationPitch = (float) Math.toRadians(EntityUtil.pitchDegreesBetweenPoints(0, 0, 0, this.motionX, this.motionY, this.motionZ));
        }
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        float modifier = speedup;

        if (this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX * modifier, this.motionY * modifier, this.motionZ * modifier);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.isInLava()) {
            this.moveRelative(strafe, vertical, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX * modifier, this.motionY * modifier, this.motionZ * modifier);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float f = 0.91F;

            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            this.moveRelative(strafe, vertical, forward, this.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX * modifier, this.motionY * modifier, this.motionZ * modifier);
            this.motionX *= f;
            this.motionY *= f;
            this.motionZ *= f;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof EntityLivingBase) {
            this.setAttackTarget((EntityLivingBase) source.getTrueSource());
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void collideWithEntity(@NotNull Entity entity) {
        if (this.getAttackTarget() != null && this.getHealth() > 0 && entity instanceof EntityLivingBase && entity.getUniqueID().equals(this.getAttackTarget().getUniqueID())) {
            EntityLivingBase living = (EntityLivingBase) entity;

            if (living instanceof EntityPlayer && ((EntityPlayer) living).isCreative()) {
                return;
            }

            living.attackEntityFrom(DamageSource.GENERIC, (float) ESConfig.ENTITIES.SPRITELING.attackDamage);
            float magnitude = (float) Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (magnitude > 0.0F) {
                living.knockBack(this, 2.0F * magnitude + 0.1F, -this.motionX / magnitude + 0.1D, -this.motionZ / magnitude + 0.1D);
            }

            living.setRevengeTarget(this);
        }
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean isAIDisabled() {
        return false;
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTime;

        if (this.deathTime == 20) {
            this.world.playSound(posX, posY, posZ, ModSoundEventsES.ENTITY_SPRITE_HURT.getSoundEvent(), SoundCategory.NEUTRAL, 1.0F, (world.rand.nextFloat() * 0.1F + 1.7F) / 2.0F, false);

            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }

            this.setDead();

            int[] color = getParticleColor();
            if (this.world.isRemote) {
                for (int i = 0; i < 30; i++) {
                    double motionX = this.rand.nextGaussian() * 0.045D;
                    double motionY = 0.02D + this.rand.nextGaussian() * 0.035D;
                    double motionZ = this.rand.nextGaussian() * 0.045D;
                    ParticleUtil.spawnParticleGlow(this.world, (float) this.posX, (float) this.posY + this.height / 2.0F, (float) this.posZ, (float) motionX, (float) motionY, (float) motionZ,
                            color[0], color[1], color[2], 0.15F, 2.0F + 2.0F * this.rand.nextFloat(), 30);
                }
            }
        }
    }

    @Override
    public int getBrightnessForRender() {
        return 255;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEventsES.ENTITY_SPRITELING_CHIME.getSoundEvent();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSoundEventsES.ENTITY_SPRITELING_HURT.getSoundEvent();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEventsES.ENTITY_SPRITELING_DEATH.getSoundEvent();
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.SPRITELING;
    }
}
