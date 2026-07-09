package mod.emt.enderzoo.entity;

import javax.annotation.Nullable;
import java.util.List;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class EntityFallenSteed extends EntityHorse {
    private boolean wasRidden = false;
    private final EntityAINearestAttackableTarget<EntityPlayer> findTargetAI;
    private final EntityAIAttackMelee attackAI;

    public EntityFallenSteed(World world) {
        super(world);
        setGrowingAge(0);
        setHorseSaddled(true);
        this.tasks.taskEntries.removeIf(entry ->
                entry.action instanceof EntityAIPanic || entry.action instanceof EntityAIFollowParent || entry.action instanceof EntityAIRunAroundLikeCrazy
        );
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
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.NAME_TAG || itemstack.getItem() == Items.LEAD)) {
            return itemstack.getItem().itemInteractionForEntity(itemstack, player, this, hand);
        }

        if (this.isTame()) {
            return super.processInteract(player, hand);
        }

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.GOLDEN_APPLE)) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            if (!this.world.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamedBy(player);
                    this.setAttackTarget(null);
                    this.setRevengeTarget(null);
                    ItemStack currentArmor = this.horseChest.getStackInSlot(1).copy();
                    this.initHorseChest();
                    this.horseChest.setInventorySlotContents(0, new ItemStack(Items.SADDLE));

                    if (!currentArmor.isEmpty()) {
                        this.setHorseArmorStack(currentArmor);
                    }

                    this.updateHorseSlots();
                    this.world.setEntityState(this, (byte) 7);
                    this.updateAttackAI();
                } else {
                    this.world.setEntityState(this, (byte) 6);
                }
            }
            return true;
        }

        // Deny mounting if hostile and untamed
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return !this.isTame();
    }

    @Override
    public boolean canMateWith(@NotNull EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public boolean canBeLeashedTo(@NotNull EntityPlayer player) {
        return this.isTame();
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
    public IEntityLivingData onInitialSpawn(@NotNull DifficultyInstance diff, @Nullable IEntityLivingData data) {
        this.initHorseChest();
        this.setHorseSaddled(true);
        this.setGrowingAge(0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.16D);
        final IAttributeInstance jumpStrength = this.getAttributeMap().getAttributeInstanceByName("horse.jumpStrength");
        if (jumpStrength != null) {
            jumpStrength.setBaseValue(1.0D);
        }
        this.setHealth(this.getMaxHealth());
        this.horseChest.setInventorySlotContents(0, new ItemStack(Items.SADDLE));
        this.horseChest.setInventorySlotContents(1, ItemStack.EMPTY);

        // Random chance for the horse to be armored
        float chanceOfArmor = (world.getDifficulty() == EnumDifficulty.HARD ? 0.5F : 0.2F);
        if (this.rand.nextFloat() <= chanceOfArmor) {
            float occupiedDifficultyMultiplier = diff.getClampedAdditionalDifficulty() / 1.5f;
            float chanceImprovedArmor = (world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.02F) * (1 + occupiedDifficultyMultiplier);

            int armorLevel = 0;
            for (int i = 0; i < 2; i++) {
                if (this.rand.nextFloat() <= chanceImprovedArmor) {
                    armorLevel++;
                }
            }

            Item armorItem = Items.IRON_HORSE_ARMOR;
            if (armorLevel == 1) armorItem = Items.GOLDEN_HORSE_ARMOR;
            if (armorLevel == 2) armorItem = Items.DIAMOND_HORSE_ARMOR;
            this.setHorseArmorStack(new ItemStack(armorItem));
        }

        this.updateHorseSlots();
        return data;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String @NotNull [] getVariantTexturePaths() {
        String[] texturePaths = super.getVariantTexturePaths();
        texturePaths[0] = EnderSafari.MOD_ID + ":textures/entity/fallen_steed.png";
        texturePaths[1] = null;
        return texturePaths;
    }

    // Despawn on Peaceful difficulty unless tamed
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            if (this.hasCustomName() || this.isTame()) {
                return;
            }
            setDead();
        }
    }

    private boolean isValidTarget(EntityLivingBase target) {
        if (target == null || !target.isEntityAlive()) {
            return false;
        }

        if (target instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) target;
            if (player.capabilities.isCreativeMode || player.isSpectator()) {
                return false;
            }

            if (this.isTame() && this.getOwnerUniqueId() != null) {
                if (player.getUniqueID().equals(this.getOwnerUniqueId())) {
                    return false;
                }

                EntityLivingBase horseAttacker = this.getRevengeTarget();
                EntityLivingBase ownerAttacker = null;
                EntityPlayer owner = this.world.getPlayerEntityByUUID(this.getOwnerUniqueId());

                if (owner != null) {
                    ownerAttacker = owner.getRevengeTarget();
                }

                return player.equals(horseAttacker) || player.equals(ownerAttacker);
            }
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

        // Look for nearby Fallen Knights to mount (Only if not tamed)
        if (!this.world.isRemote && !this.isTame() && !this.isRidden() && this.rand.nextInt(20) == 0) {
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

        if (!this.world.isRemote && this.isTame() && this.getAttackTarget() != null) {
            if (!isValidTarget(this.getAttackTarget())) {
                this.setAttackTarget(null);
            }
        }

        if (wasRidden != isRidden()) {
            updateAttackAI();
            wasRidden = isRidden();
        }
    }

    @Override
    public void onDeath(@NotNull DamageSource cause) {
        if (!this.world.isRemote && !this.isTame()) {
            ItemStack saddleStack = this.horseChest.getStackInSlot(0);
            if (!saddleStack.isEmpty() && saddleStack.getItem() == Items.SADDLE) {
                int lootingModifier = cause.getTrueSource() instanceof EntityLivingBase ? EnchantmentHelper.getLootingModifier((EntityLivingBase)cause.getTrueSource()) : 0;
                float saddleDropChance = 0.1F + (lootingModifier * 0.05F);
                if (this.rand.nextFloat() > saddleDropChance) {
                    this.horseChest.setInventorySlotContents(0, ItemStack.EMPTY);
                }
            }

            ItemStack armorStack = this.horseChest.getStackInSlot(1);
            if (!armorStack.isEmpty()) {
                int lootingModifier = cause.getTrueSource() instanceof EntityLivingBase ? EnchantmentHelper.getLootingModifier((EntityLivingBase)cause.getTrueSource()) : 0;
                float armorDropChance = 0.15F + (lootingModifier * 0.05F);
                if (this.rand.nextFloat() > armorDropChance) {
                    this.horseChest.setInventorySlotContents(1, ItemStack.EMPTY);
                }
            }
        }

        super.onDeath(cause);
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
        return (getTotalArmorValue() == 0 || !isTame());
    }

    protected boolean isRidden() {
        return !getPassengers().isEmpty();
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase target) {
        if (this.isTame() && target instanceof EntityPlayer) {
            if (target.getUniqueID().equals(this.getOwnerUniqueId())) {
                super.setAttackTarget(null);
                return;
            }
        }
        super.setAttackTarget(target);
    }

    private void updateAttackAI() {
        this.targetTasks.removeTask(findTargetAI);
        this.tasks.removeTask(attackAI);

        if (this.isRidden() || this.isTame()) {
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
        if (this.isTame() && target.getUniqueID().equals(this.getOwnerUniqueId())) {
            this.setAttackTarget(null);
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
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.initHorseChest();
        this.horseChest.setInventorySlotContents(0, new ItemStack(Items.SADDLE));
        this.setHorseSaddled(true);
        this.updateHorseSlots();
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