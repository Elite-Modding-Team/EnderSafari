package mod.emt.endersafari.entity;

import mod.emt.endersafari.registry.ModLootTablesES;
import mod.emt.endersafari.registry.ModSoundEventsES;
import mod.emt.endersafari.utils.ParticleUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class EntityFairy extends EntityFlying {
    public static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityFairy.class, DataSerializers.VARINT);
    public static final DataParameter<BlockPos> spawnPosition = EntityDataManager.createKey(EntityFairy.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<BlockPos> targetPosition = EntityDataManager.createKey(EntityFairy.class, DataSerializers.BLOCK_POS);

    public EntityFairy(World world) {
        super(world);
        this.setSize(0.45F, 0.6F);
        this.experienceValue = 10;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(spawnPosition, new BlockPos(0, -1, 0));
        this.getDataManager().register(targetPosition, new BlockPos(0, -1, 0));
        this.dataManager.register(TYPE, 0);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("spawnX", getDataManager().get(spawnPosition).getX());
        compound.setInteger("spawnY", getDataManager().get(spawnPosition).getY());
        compound.setInteger("spawnZ", getDataManager().get(spawnPosition).getZ());
        compound.setInteger("targetX", getDataManager().get(targetPosition).getX());
        compound.setInteger("targetY", getDataManager().get(targetPosition).getY());
        compound.setInteger("targetZ", getDataManager().get(targetPosition).getZ());
        compound.setInteger("type", this.dataManager.get(TYPE));
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        getDataManager().set(spawnPosition, new BlockPos(compound.getInteger("spawnX"), compound.getInteger("spawnY"), compound.getInteger("spawnZ")));
        getDataManager().set(targetPosition, new BlockPos(compound.getInteger("targetX"), compound.getInteger("targetY"), compound.getInteger("targetZ")));
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
        this.setType(this.rand.nextInt(7));
        return entityLivingData;
    }

    private int[] getParticleColor() {
        switch (this.getType()) {
            case 1: // Green
                return new int[]{177, 255, 117};
            case 2: // Orange
                return new int[]{255, 223, 163};
            case 3: // Pink
                return new int[]{255, 163, 255};
            case 4: // Purple
                return new int[]{219, 179, 255};
            case 5: // Red
                return new int[]{255, 98, 114};
            case 6: // Yellow
                return new int[]{255, 242, 179};
            default: // Blue
                return new int[]{163, 221, 255};
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            int[] color = getParticleColor();
            float x = (float) posX;
            float y = (float) posY + height / 2.0F;
            float z = (float) posZ;
            ParticleUtil.spawnParticleGlow(world, x, y, z, 0.0F, 0.0F, 0.0F, color[0], color[1], color[2], 0.05F, 2.5F + 2.5F * rand.nextFloat(), 40);
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.noClip = false;
        if (this.getDataManager().get(spawnPosition).getY() < 0) {
            this.getDataManager().set(spawnPosition, getPosition());
            this.getDataManager().set(targetPosition, getPosition());
        }
        if (getDataManager().get(targetPosition).compareTo(getDataManager().get(spawnPosition)) == 0 || this.rand.nextInt(30) == 0 || getDataManager().get(targetPosition).distanceSq((int) this.posX, (int) this.posY, (int) this.posZ) < 3.0D) {
            BlockPos newTarget = null;

            for (int i = 0; i < 8; i++) {
                BlockPos candidate = new BlockPos(
                        getDataManager().get(spawnPosition).getX() + this.rand.nextInt(15) - this.rand.nextInt(15),
                        getDataManager().get(spawnPosition).getY() + this.rand.nextInt(11) - 2,
                        getDataManager().get(spawnPosition).getZ() + this.rand.nextInt(15) - this.rand.nextInt(15)
                );

                if (canFlyTo(candidate)) {
                    newTarget = candidate;
                    break;
                }
            }

            if (newTarget != null) {
                this.getDataManager().set(targetPosition, newTarget);
            }
        }

        double dX = (double) this.getDataManager().get(targetPosition).getX() + 0.5D - this.posX;
        double dY = (double) this.getDataManager().get(targetPosition).getY() + 0.1D - this.posY;
        double dZ = (double) this.getDataManager().get(targetPosition).getZ() + 0.5D - this.posZ;
        this.motionX += (Math.signum(dX) * 0.5D - this.motionX) * 0.025D;
        this.motionY += (Math.signum(dY) * 0.7D - this.motionY) * 0.025D;
        this.motionZ += (Math.signum(dZ) * 0.5D - this.motionZ) * 0.025D;
        float f = (float) (MathHelper.atan2(this.motionZ, this.motionX) * (180D / Math.PI)) - 90.0F;
        float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
        this.moveForward = 0.5F;
        this.rotationYaw += f1;
    }

    private boolean canFlyTo(BlockPos target) {
        double dx = target.getX() + 0.5D - this.posX;
        double dy = target.getY() + 0.5D - this.posY;
        double dz = target.getZ() + 0.5D - this.posZ;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        int steps = Math.max(1, (int) Math.ceil(distance / 0.5D));

        for (int i = 1; i <= steps; i++) {
            double progress = (double) i / steps;
            double x = this.posX + dx * progress;
            double y = this.posY + dy * progress;
            double z = this.posZ + dz * progress;
            BlockPos pos = new BlockPos(x, y, z);
            if (!this.world.isAirBlock(pos)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTime;

        if (this.deathTime == 1) {
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
                    ParticleUtil.spawnParticleGlowBurst(this.world, (float) this.posX, (float) this.posY + this.height / 2.0F, (float) this.posZ, (float) motionX, (float) motionY, (float) motionZ,
                            color[0], color[1], color[2], 0.15F, 2.0F + 2.0F * this.rand.nextFloat(), 30, true);
                }
            }
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
    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    @Override
    public int getBrightnessForRender() {
        return 255;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEventsES.ENTITY_FAIRY_CHIME.getSoundEvent();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSoundEventsES.ENTITY_FAIRY_HURT.getSoundEvent();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEventsES.ENTITY_FAIRY_DEATH.getSoundEvent();
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.FAIRY;
    }
}
