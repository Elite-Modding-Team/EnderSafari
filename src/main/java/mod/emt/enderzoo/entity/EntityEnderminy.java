package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.config.EZConfig;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class EntityEnderminy extends EntityMob {
    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.createKey(EntityEnderminy.class, DataSerializers.BOOLEAN);

    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291B0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST = (new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking Speed Boost", 0.15D, 0)).setSaved(false);

    private final boolean attackIfLookingAtPlayer = true;
    private final boolean attackCreepers = true;
    private final boolean groupAgroEnabled = true;

    public EntityEnderminy(World world) {
        super(world);
        this.setSize(0.3F, 0.725F);
        this.stepHeight = 1.0F;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        if (this.attackIfLookingAtPlayer) {
            this.targetTasks.addTask(2, new EntityEnderminy.AIFindPlayer(this));
        }
        if (this.attackCreepers) {
            this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityConcussionCreeper.class, true));
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(EZConfig.ENTITIES.ENDERMINY.armor);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EZConfig.ENTITIES.ENDERMINY.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EZConfig.ENTITIES.ENDERMINY.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EZConfig.ENTITIES.ENDERMINY.movementSpeed);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SCREAMING, false);
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entity) {
        super.setAttackTarget(entity);
        IAttributeInstance speedAttr = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entity == null) {
            this.setScreaming(false);
            speedAttr.removeModifier(ATTACKING_SPEED_BOOST);
        } else {
            this.setScreaming(true);
            if (!speedAttr.hasModifier(ATTACKING_SPEED_BOOST)) {
                speedAttr.applyModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ);
        boolean passedGrassCheck = this.world.getBlockState(pos).getBlock() == Blocks.GRASS;
        return passedGrassCheck && super.getCanSpawnHere();
    }

    private boolean shouldAttackPlayer(EntityPlayer player) {
        ItemStack helmet = player.inventory.armorInventory.get(3);
        if (!helmet.isEmpty() && helmet.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        }

        Vec3d relativeEyePos = new Vec3d(
                this.posX - player.posX,
                this.getEntityBoundingBox().minY + (double) (this.height / 2.0F) - (player.posY + (double) player.getEyeHeight()),
                this.posZ - player.posZ
        );

        double distance = relativeEyePos.length();
        relativeEyePos = relativeEyePos.normalize();

        Vec3d lookVec = this.getLook(1.0F).normalize();
        double dotTangent = -lookVec.dotProduct(relativeEyePos);

        return dotTangent > 1.0D - 0.025D / distance && player.canEntityBeSeen(this);
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL,
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
        this.isJumping = false;
        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        EntityLivingBase currentTarget = this.getAttackTarget();
        if (currentTarget != null && !currentTarget.isEntityAlive()) {
            this.setAttackTarget(null);
        }

        if (this.isScreaming() && this.getAttackTarget() == null && this.rand.nextInt(100) == 0) {
            this.setScreaming(false);
        }
        super.updateAITasks();
    }

    protected boolean teleportRandomly(int distance) {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * distance;
        double d1 = this.posY + (double) (this.rand.nextInt(distance + 1) - (distance / 2));
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * distance;
        return this.teleportTo(d0, d1, d2);
    }

    protected boolean teleportRandomly() {
        return this.teleportRandomly(32);
    }

    protected boolean teleportToEntity(Entity target) {
        Vec3d vec3d = new Vec3d(this.posX - target.posX, this.getEntityBoundingBox().minY + (double) (this.height / 2.0F) - target.posY + (double) target.getEyeHeight(), this.posZ - target.posZ).normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * d0;
        double d2 = this.posY + (double) (this.rand.nextInt(16) - 8) - vec3d.y * d0;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * d0;
        return this.teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
        if (MinecraftForge.EVENT_BUS.post(event)) return false;

        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }
        return flag;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }

    @Override
    protected float getSoundPitch() {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesEZ.ENDERMINY;
    }

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) return false;

        if (source instanceof EntityDamageSourceIndirect) {
            for (int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) return true;
            }
            return false;
        }

        boolean result = super.attackEntityFrom(source, amount);
        if (source.getTrueSource() instanceof EntityLivingBase && this.getHealth() > 0) {
            EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();

            if (attacker instanceof EntityPlayer && ((EntityPlayer) attacker).capabilities.isCreativeMode) {
                return result;
            }

            this.setScreaming(true);
            this.setAttackTarget(attacker);
            this.doGroupAggro(attacker);

            if (this.rand.nextInt(3) == 0) {
                for (int i = 0; i < 64; ++i) {
                    if (this.teleportRandomly(16)) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    private void doGroupAggro(EntityLivingBase target) {
        if (!this.groupAgroEnabled || target == null) return;

        int range = 16;
        AxisAlignedBB bb = new AxisAlignedBB(this.posX - range, this.posY - range, this.posZ - range, this.posX + range, this.posY + range, this.posZ + range);
        List<EntityEnderminy> minies = this.world.getEntitiesWithinAABB(EntityEnderminy.class, bb);

        for (EntityEnderminy miny : minies) {
            if (miny.getAttackTarget() == null) {
                miny.setScreaming(true);
                miny.setAttackTarget(target);
            }
        }
    }

    public boolean isScreaming() {
        return this.dataManager.get(SCREAMING);
    }

    public void setScreaming(boolean screaming) {
        this.dataManager.set(SCREAMING, screaming);
    }

    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {
        private final EntityEnderminy enderminy;
        private EntityPlayer player;
        private int stareTimer;
        private int teleportDelay;

        public AIFindPlayer(EntityEnderminy entity) {
            super(entity, EntityPlayer.class, false);
            this.enderminy = entity;
        }

        @Override
        public boolean shouldExecute() {
            double d0 = this.getTargetDistance();
            this.player = this.enderminy.world.getNearestAttackablePlayer(this.enderminy.posX, this.enderminy.posY, this.enderminy.posZ, d0, d0, null, p -> p != null && AIFindPlayer.this.enderminy.shouldAttackPlayer(p));
            return this.player != null;
        }

        @Override
        public void startExecuting() {
            this.stareTimer = 5;
            this.teleportDelay = 0;
        }

        @Override
        public void resetTask() {
            this.player = null;
            this.enderminy.setScreaming(false);
            super.resetTask();
        }

        @Override
        public boolean shouldContinueExecuting() {
            EntityLivingBase currentTarget = this.enderminy.getAttackTarget();
            if (currentTarget != null && currentTarget.isEntityAlive()) {
                return !(currentTarget instanceof EntityPlayer) || !((EntityPlayer) currentTarget).capabilities.isCreativeMode;
            }

            if (this.player != null) {
                if (!this.enderminy.shouldAttackPlayer(this.player)) {
                    return false;
                } else {
                    this.enderminy.faceEntity(this.player, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.targetEntity != null && this.targetEntity.isEntityAlive() || super.shouldContinueExecuting();
            }
        }

        @Override
        public void updateTask() {
            if (this.player != null) {
                if (--this.stareTimer <= 0) {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                    this.enderminy.playSound(SoundEvents.ENTITY_ENDERMEN_STARE, 1.0F, (this.enderminy.rand.nextFloat() - this.enderminy.rand.nextFloat()) * 0.2F + 1.5F);
                    this.enderminy.setAttackTarget(this.targetEntity);
                }
            } else {
                if (this.targetEntity != null) {
                    if (this.targetEntity instanceof EntityPlayer && this.enderminy.shouldAttackPlayer(this.targetEntity)) {
                        if (this.enderminy.rand.nextInt(3) == 0) {
                            if (this.targetEntity.getDistanceSq(this.enderminy) < 16.0D) {
                                this.enderminy.teleportRandomly();
                            }
                        }
                        this.teleportDelay = 0;
                    } else if (this.targetEntity.getDistanceSq(this.enderminy) > 256.0D && this.teleportDelay++ >= 30 && this.enderminy.teleportToEntity(this.targetEntity)) {
                        this.teleportDelay = 0;
                    }
                }
                super.updateTask();
            }
        }
    }
}