package mod.emt.enderzoo.entity;

import javax.annotation.Nullable;
import java.util.List;

import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityFallenSteed extends EntityHorse {
    private boolean wasRidden = false;
    private final EntityAINearestAttackableTarget<EntityPlayer> findTargetAI;
    private final EntityAIAttackMelee attackAI;
    private ItemStack armor = ItemStack.EMPTY;

    public EntityFallenSteed(World world) {
        super(world);
        setGrowingAge(0);
        setHorseSaddled(true);
        tasks.taskEntries.clear();
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(6, new EntityAIWander(this, 1.2D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        findTargetAI = new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true);
        attackAI = new EntityAIAttackMelee(this, 2.0D, false);
        updateAttackAI();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    @Override
    protected boolean isMovementBlocked() {
        return isRearing();
    }

    @Override
    public boolean processInteract(@NotNull EntityPlayer player, @NotNull EnumHand hand) {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return true;
    }

    @Override
    public boolean canMateWith(@NotNull EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public boolean canBeLeashedTo(@NotNull EntityPlayer player) {
        return false;
    }

    @Override
    public boolean isBreedingItem(@NotNull ItemStack stack) {
        return false;
    }

    // Considered a hostile mob during spawning, not a passive mob
    @Override
    public boolean isCreatureType(@NotNull EnumCreatureType type, boolean forSpawnCount) {
        if (type == EnumCreatureType.MONSTER) {
            return true;
        }
        return super.isCreatureType(type, forSpawnCount);
    }

    @Override
    public IEntityLivingData onInitialSpawn(@NotNull DifficultyInstance di, @Nullable IEntityLivingData data) {
        setHorseArmorStack(ItemStack.EMPTY);
        setHorseSaddled(true);
        setGrowingAge(0);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.16D);
        final IAttributeInstance jumpStrength = getAttributeMap().getAttributeInstanceByName("horse.jumpStrength");
        if (jumpStrength != null) {
            jumpStrength.setBaseValue(0.5);
        }
        setHealth(getMaxHealth());

        // Random chance for the horse to be armored
        float chanceOfArmor = (world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.5F);
        if (rand.nextFloat() <= chanceOfArmor) {
            float occupiedDifficultyMultiplier = di.getClampedAdditionalDifficulty();
            occupiedDifficultyMultiplier /= 1.5f;

            float chanceImprovedArmor = (world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F);
            chanceImprovedArmor *= (1 + occupiedDifficultyMultiplier);

            int armorLevel = 0;
            for (int i = 0; i < 2; i++) {
                if (rand.nextFloat() <= chanceImprovedArmor) {
                    armorLevel++;
                }
            }

            Item armorItem = Items.IRON_HORSE_ARMOR;
            switch (armorLevel) {
                case 1:
                    armorItem = Items.GOLDEN_HORSE_ARMOR;
                    break;
                case 2:
                    armorItem = Items.DIAMOND_HORSE_ARMOR;
                    break;
            }
            armor = new ItemStack(armorItem);
            setHorseArmorStack(armor);
        } else {
            armor = ItemStack.EMPTY;
            setHorseArmorStack(armor);
        }
        return data;
    }

    // Despawn on Peaceful difficulty
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL && !this.hasCustomName()) {
            setDead();
        }
    }

    private boolean isValidTarget(EntityLivingBase target) {
        if (target == null || !target.isEntityAlive()) {
            return false;
        }
        if (target instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) target;
            return !player.capabilities.isCreativeMode && !player.isSpectator();
        }
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (world.isDaytime() && !world.isRemote) {
            if (burnInSun() && world.getTotalWorldTime() % 20 == 0) {
                if (getBrightness() > 0.5F && rand.nextFloat() * 30.0F < (getBrightness() - 0.4F) * 2.0F && world.canBlockSeeSky(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ)))) {
                    setFire(8);
                }
            }
        }
        setEatingHaystack(false);

        // Look for nearby Fallen Knights to mount
        if (!this.world.isRemote && !this.isRidden() && this.rand.nextInt(20) == 0) {
            List<Entity> nearbyEntities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D));
            for (Entity entity : nearbyEntities) {
                if (entity instanceof EntityFallenKnight && !entity.isRiding()) {
                    entity.startRiding(this, true);
                    break;
                }
            }
        }

        // Both the rider and mount will attack back if one of them is damaged
        if (!this.world.isRemote && this.isRidden()) {
            Entity rider = this.getPassengers().get(0);

            if (rider instanceof EntityLiving) {
                EntityLiving knight = (EntityLiving) rider;
                EntityLivingBase riderTarget = knight.getAttackTarget();

                if (isValidTarget(riderTarget)) {
                    this.setAttackTarget(riderTarget);
                } else if (isValidTarget(this.getAttackTarget())) {
                    knight.setAttackTarget(this.getAttackTarget());
                } else {
                    this.setAttackTarget(null);
                    knight.setAttackTarget(null);
                }

                EntityLivingBase steedRevenge = this.getRevengeTarget();
                EntityLivingBase knightRevenge = knight.getRevengeTarget();

                if (isValidTarget(steedRevenge)) {
                    if (knight.getRevengeTarget() != steedRevenge) knight.setRevengeTarget(steedRevenge);
                } else {
                    this.setRevengeTarget(null);
                }

                if (isValidTarget(knightRevenge)) {
                    if (this.getRevengeTarget() != knightRevenge) this.setRevengeTarget(knightRevenge);
                } else {
                    knight.setRevengeTarget(null);
                }
            }
        }

        if (wasRidden != isRidden()) {
            updateAttackAI();
            wasRidden = isRidden();
        }
    }

    @Override
    public void updatePassenger(@NotNull Entity passenger) {
        super.updatePassenger(passenger);
        if (passenger instanceof EntityPlayer) {
            EntityLivingBase living = (EntityLivingBase) passenger;
            this.rotationYaw = living.rotationYaw;
            this.prevRotationYaw = living.prevRotationYaw;
            this.rotationYawHead = living.rotationYawHead;
            this.renderYawOffset = living.renderYawOffset;
        }
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    private boolean burnInSun() {
        return getTotalArmorValue() == 0;
    }

    protected boolean isRidden() {
        return !getPassengers().isEmpty();
    }

    private void updateAttackAI() {
        this.targetTasks.removeTask(findTargetAI);
        this.tasks.removeTask(attackAI);

        if (this.isRidden()) {
            this.getNavigator().clearPath();
        } else {
            this.targetTasks.addTask(2, findTargetAI);
            this.tasks.addTask(4, attackAI);
        }
    }

    @Override
    public boolean attackEntityAsMob(@NotNull Entity target) {
        if (isRidden() || isDead) {
            return false;
        }
        super.attackEntityAsMob(target);
        if (!isRearing()) {
            makeMad();
        }
        float damage = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        return target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound root) {
        super.writeEntityToNBT(root);
        NBTTagCompound armTag = new NBTTagCompound();
        armor.writeToNBT(armTag);
        root.setTag("armor", armTag);
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound root) {
        super.readEntityFromNBT(root);
        setHorseSaddled(true);
        if (root.hasKey("armor", 10)) {
            armor = new ItemStack(root.getCompoundTag("armor"));
            setHorseArmorStack(armor);
        }
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HORSE_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_HORSE_DEATH;
    }

    @Override
    protected @NotNull ResourceLocation getLootTable() {
        return ModLootTablesEZ.FALLEN_STEED;
    }
}