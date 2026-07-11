package mod.emt.enderzoo.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mod.emt.enderzoo.entity.navigator.PathNavigateGroundMounted;
import mod.emt.enderzoo.registry.ModItemsEZ;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
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

public class EntityFallenKnight extends EntitySkeleton implements IRangedAttackMob {
    private static final java.util.UUID RIDING_SPEED_BOOST_ID = java.util.UUID.fromString("67A102A0-8F91-4DDE-9951-8E6BA9C3241F");
    private static final AttributeModifier RIDING_SPEED_BOOST = new AttributeModifier(RIDING_SPEED_BOOST_ID, "Mount riding speed boost", 1.5D, 2).setSaved(false);

    private final EntityAIAttackRangedBow<EntityFallenKnight> aiArrowAttack = new EntityAIAttackRangedBow<>(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.25D, false) {
        @Override
        public void resetTask() {
            super.resetTask();
            EntityFallenKnight.this.setSwingingArms(false);
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            EntityFallenKnight.this.setSwingingArms(true);
        }
    };

    private final EntityAIBreakDoor breakDoorAI = new EntityAIBreakDoor(this);
    private boolean canBreakDoors = false;
    private boolean knightFirstUpdate = true;
    private boolean hasSpawned = false;
    private boolean isArcher = false;
    private ItemStack rangedMode = ItemStack.EMPTY;
    private ItemStack meleeMode = ItemStack.EMPTY;

    public EntityFallenKnight(World world) {
        super(world);
        this.setCombatTaskReal();
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    @Override
    protected @Nonnull PathNavigate createNavigator(@Nonnull World world) {
        return new PathNavigateGroundMounted(this, world);
    }

    @Override
    public void setCombatTask() {
    }

    public void setCombatTaskReal() {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getHeldItemMainhand();
            if (itemstack.getItem() instanceof ItemBow) {
                this.tasks.removeTask(this.aiAttackOnCollide);
                this.tasks.removeTask(this.aiArrowAttack);

                int cooldown = this.world.getDifficulty() == EnumDifficulty.HARD ? 20 : 40;
                this.aiArrowAttack.setAttackCooldown(cooldown);
                this.tasks.addTask(4, this.aiArrowAttack);
            } else {
                this.tasks.removeTask(this.aiArrowAttack);
                boolean hasMeleeTask = this.tasks.taskEntries.stream().anyMatch(entry -> entry.action == this.aiAttackOnCollide);

                if (!hasMeleeTask) {
                    this.tasks.addTask(4, this.aiAttackOnCollide);
                }
            }
        }
    }

    @Override
    public void setItemStackToSlot(@NotNull EntityEquipmentSlot slot, @NotNull ItemStack stack) {
        super.setItemStackToSlot(slot, stack);
        if (!this.world.isRemote && slot == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTaskReal();
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.world.isRemote) {
            IAttributeInstance speedAttribute = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

            if (this.isRiding()) {
                if (!speedAttribute.hasModifier(RIDING_SPEED_BOOST)) {
                    speedAttribute.applyModifier(RIDING_SPEED_BOOST);
                }

                if (this.getRidingEntity() instanceof EntityLiving) {
                    EntityLiving steed = (EntityLiving) this.getRidingEntity();

                    if (this.getAttackTarget() != steed.getAttackTarget()) {
                        steed.setAttackTarget(this.getAttackTarget());
                    }

                    EntityLivingBase knightRevenge = this.getRevengeTarget();
                    EntityLivingBase steedRevenge = steed.getRevengeTarget();
                    if (knightRevenge != null && knightRevenge.isEntityAlive()) {
                        if (steed.getAttackTarget() == null) steed.setAttackTarget(knightRevenge);
                        if (steed.getRevengeTarget() != knightRevenge) steed.setRevengeTarget(knightRevenge);
                    } else if (steedRevenge != null && steedRevenge.isEntityAlive()) {
                        if (this.getAttackTarget() == null) this.setAttackTarget(steedRevenge);
                        if (this.getRevengeTarget() != steedRevenge) this.setRevengeTarget(steedRevenge);
                    }
                }
            } else {
                if (speedAttribute.hasModifier(RIDING_SPEED_BOOST)) {
                    speedAttribute.removeModifier(RIDING_SPEED_BOOST);
                }
            }

            if (this.knightFirstUpdate) {
                this.isArcher = this.isRanged();
                if (this.isArcher) {
                    this.rangedMode = this.getHeldItem(EnumHand.MAIN_HAND).copy();
                    this.meleeMode = getSwordForLevel(getRandomEquipmentLevel());
                } else {
                    this.meleeMode = this.getHeldItem(EnumHand.MAIN_HAND).copy();
                    this.rangedMode = ItemStack.EMPTY;
                }
                setCombatTaskReal();
                this.knightFirstUpdate = false;
            }

            if (this.isArcher && !this.rangedMode.isEmpty() && !this.meleeMode.isEmpty()) {
                EntityLivingBase attackTarget = this.getAttackTarget();
                if (attackTarget != null) {
                    double dX = this.posX - attackTarget.posX;
                    double dZ = this.posZ - attackTarget.posZ;
                    double distanceSq2D = dX * dX + dZ * dZ;

                    if (distanceSq2D < 25.0D && this.isRanged()) {
                        swapWeapon(this.meleeMode);
                    } else if (distanceSq2D >= 36.0D && !this.isRanged()) {
                        swapWeapon(this.rangedMode);
                    }
                } else if (!this.isRanged()) {
                    swapWeapon(this.rangedMode);
                }
            }

            EntityLivingBase attackTarget = this.getAttackTarget();
            if (attackTarget != null && attackTarget.isEntityAlive()) {
                if (!this.isSwingingArms()) {
                    this.setSwingingArms(true);
                }
            } else {
                if (this.isSwingingArms()) {
                    this.setSwingingArms(false);
                }
            }
        }
    }

    private void swapWeapon(@NotNull ItemStack stack) {
        if (!stack.isEmpty()) {
            this.resetActiveHand();
            super.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack.copy());
            this.setCombatTaskReal();
        }
    }

    private boolean isRanged() {
        return this.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow;
    }

    @Override
    public void attackEntityWithRangedAttack(@NotNull EntityLivingBase target, float distanceFactor) {
        EntityArrow entityarrow = this.getArrow(distanceFactor);

        if (this.getHeldItemMainhand().getItem() instanceof ItemBow) {
            entityarrow = ((ItemBow) this.getHeldItemMainhand().getItem()).customizeArrow(entityarrow);
        }

        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        entityarrow.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
    }

    protected @NotNull EntityArrow getArrow(float distanceFactor) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
        entitytippedarrow.setEnchantmentEffectsFromEntity(this, distanceFactor);
        return entitytippedarrow;
    }

    private void addRandomArmor() {
        int equipmentLevel = getRandomEquipmentLevel();

        // Skip gold armor
        if (equipmentLevel == 1) {
            equipmentLevel++;
        }

        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                ItemStack itemStack = this.getItemStackFromSlot(slot);
                if (itemStack.isEmpty() && this.rand.nextFloat() <= 0.20F) {
                    Item item = EntityLiving.getArmorByChance(slot, equipmentLevel);
                    if (item != null) {
                        ItemStack stack = new ItemStack(item);
                        if (equipmentLevel == 0 && item instanceof ItemArmor) {
                            ((ItemArmor) item).setColor(stack, 0);
                        }
                        this.setItemStackToSlot(slot, stack);
                    }
                }
            }
        }

        if (this.rand.nextFloat() >= 0.40F) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, getSwordForLevel(equipmentLevel));
            if (this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
            }
        } else {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItemsEZ.GUARDIAN_BOW));
        }
    }

    private int getRandomEquipmentLevel() {
        int armorLevel = this.rand.nextInt(2);
        for (int i = 0; i < 2; i++) {
            if (this.rand.nextFloat() <= 0.15F) {
                armorLevel++;
            }
        }
        return armorLevel;
    }

    private @NotNull ItemStack getSwordForLevel(int swordLevel) {
        if (swordLevel < 2) {
            swordLevel += this.rand.nextInt(2);
            swordLevel = Math.min(swordLevel, 2);
        }
        boolean isSword = this.rand.nextFloat() < 0.80F;
        switch (swordLevel) {
            case 0:
                return new ItemStack(isSword ? Items.WOODEN_SWORD : Items.WOODEN_AXE);
            case 1:
                return new ItemStack(isSword ? Items.STONE_SWORD : Items.STONE_AXE);
            case 2:
                return new ItemStack(isSword ? Items.IRON_SWORD : Items.IRON_AXE);
            default:
                return new ItemStack(isSword ? Items.DIAMOND_SWORD : Items.DIAMOND_AXE);
        }
    }

    private void spawnSteed() {
        if (this.isRiding() || !hasSpawned) {
            return;
        }

        EntityFallenSteed steed = new EntityFallenSteed(this.world);
        steed.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
        DifficultyInstance diff = this.world.getDifficultyForLocation(new BlockPos(steed));
        steed.onInitialSpawn(diff, null);
        this.setCanPickUpLoot(false);
        this.setCanBreakDoors(false);
        this.world.spawnEntity(steed);
        this.startRiding(steed);
    }

    @Override
    public IEntityLivingData onInitialSpawn(@NotNull DifficultyInstance diff, @Nullable IEntityLivingData livingData) {
        this.hasSpawned = true;

        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextGaussian() * 0.05D, 1));
        setCombatTaskReal();
        addRandomArmor();
        setEnchantmentBasedOnDifficulty(diff);

        float f = diff.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);
        setCanBreakDoors(this.rand.nextFloat() < f * 0.1F);

        if (!this.world.isRemote) {
            if (this.rand.nextFloat() <= 0.20F && this.world.canSeeSky(new BlockPos(this))) {
                this.spawnSteed();
            }
        }

        return livingData;
    }

    @Override
    protected void setEnchantmentBasedOnDifficulty(@NotNull DifficultyInstance diff) {
        float f = diff.getClampedAdditionalDifficulty();
        if (!this.getHeldItemMainhand().isEmpty() && this.rand.nextFloat() < 0.25F * f) {
            EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItemMainhand(), (int) (5.0F + f * (float) this.rand.nextInt(18)), false);
        }
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("canBreakDoors", this.canBreakDoors);
        compound.setBoolean("isArcher", this.isArcher);
        if (this.isArcher) {
            compound.setTag("rangedMode", this.rangedMode.writeToNBT(new NBTTagCompound()));
            compound.setTag("meleeMode", this.meleeMode.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setCanBreakDoors(compound.getBoolean("canBreakDoors"));
        this.isArcher = compound.getBoolean("isArcher");
        if (this.isArcher) {
            this.rangedMode = new ItemStack(compound.getCompoundTag("rangedMode"));
            this.meleeMode = new ItemStack(compound.getCompoundTag("meleeMode"));
        }
        this.setCombatTaskReal();
    }

    private void setCanBreakDoors(boolean val) {
        if (this.canBreakDoors != val) {
            this.canBreakDoors = val;
            if (this.canBreakDoors) {
                this.tasks.addTask(1, this.breakDoorAI);
            } else {
                this.tasks.removeTask(this.breakDoorAI);
            }
        }
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.ENTITY_HUSK_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HUSK_DEATH;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesEZ.FALLEN_KNIGHT;
    }
}