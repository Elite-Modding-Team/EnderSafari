package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.config.EZConfig;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityWitherWitch extends EntityMob implements IRangedAttackMob {
    private static final UUID DRINKING_MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier DRINKING_MODIFIER = (new AttributeModifier(DRINKING_MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
    private static final DataParameter<Boolean> IS_DRINKING = EntityDataManager.createKey(EntityWitherWitch.class, DataSerializers.BOOLEAN);
    private int potionUseTimer;

    private final List<EntityWitchCat> cats = new ArrayList<>();
    private boolean initialCatsSpawn = EZConfig.ENTITIES.WITHER_WITCH.enableCatSpawning;
    private boolean hasSpawnedCats = false;

    public EntityWitherWitch(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(IS_DRINKING, Boolean.FALSE);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(EZConfig.ENTITIES.WITHER_WITCH.armor);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EZConfig.ENTITIES.WITHER_WITCH.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EZConfig.ENTITIES.WITHER_WITCH.movementSpeed);
    }

    public void setDrinkingPotion(boolean drinkingPotion) {
        this.getDataManager().set(IS_DRINKING, drinkingPotion);
    }

    public boolean isDrinkingPotion() {
        return this.getDataManager().get(IS_DRINKING);
    }

    @Override
    public void onLivingUpdate() {
        if (!this.world.isRemote) {
            if (this.initialCatsSpawn) {
                if (!this.hasSpawnedCats) {
                    this.spawnCats();
                }
                this.initialCatsSpawn = false;
            }

            if (this.isDrinkingPotion()) {
                if (this.potionUseTimer-- <= 0) {
                    this.setDrinkingPotion(false);
                    ItemStack heldItem = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);

                    if (heldItem.getItem() == Items.POTIONITEM) {
                        List<PotionEffect> effects = PotionUtils.getEffectsFromStack(heldItem);
                        for (PotionEffect effect : effects) {
                            this.addPotionEffect(new PotionEffect(effect));
                        }
                    }
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(DRINKING_MODIFIER);
                }
            } else {
                PotionType potiontype = null;

                if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING)) {
                    potiontype = PotionTypes.WATER_BREATHING;
                } else if (this.rand.nextFloat() < 0.15F && !this.isPotionActive(MobEffects.SPEED) && this.getAttackTarget() != null) {
                    potiontype = PotionTypes.SWIFTNESS;
                } else if (this.rand.nextFloat() < 0.15F && (this.isBurning() || (this.getLastDamageSource() != null && this.getLastDamageSource().isFireDamage())) && !this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    potiontype = PotionTypes.FIRE_RESISTANCE;
                } else if (this.rand.nextFloat() < 0.15F && !this.isPotionActive(MobEffects.REGENERATION) && this.getHealth() < this.getMaxHealth()) {
                    potiontype = PotionTypes.REGENERATION;
                } else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    potiontype = PotionTypes.HEALING;
                }

                if (potiontype != null) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
                    this.potionUseTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
                    this.setDrinkingPotion(true);
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
                    IAttributeInstance speedAttr = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    speedAttr.removeModifier(DRINKING_MODIFIER);
                    speedAttr.applyModifier(DRINKING_MODIFIER);
                }
            }

            EntityLivingBase target = this.getAttackTarget();
            for (EntityWitchCat cat : this.cats) {
                if (cat.getAttackTarget() != target) {
                    cat.setAttackTarget(target);
                }
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public void attackEntityWithRangedAttack(@NotNull EntityLivingBase target, float distanceFactor) {
        if (!this.isDrinkingPotion()) {
            double d1 = target.posX + target.motionX - this.posX;
            double d0 = target.posY + target.getEyeHeight() - 1.1D - this.posY;
            double d3 = target.posZ + target.motionZ - this.posZ;
            float groundDist = MathHelper.sqrt(d1 * d1 + d3 * d3);
            List<PotionEffect> effect = new ArrayList<>();
            PotionType baseType;

            // Done this way so the witches don't need to use the actual Decay and Rising potions, in case those are disabled in the config
            if (!target.isPotionActive(MobEffects.WITHER)) {
                baseType = PotionTypes.WATER;
                effect.add(new PotionEffect(MobEffects.WITHER, 900, 0));
            } else if (this.rand.nextFloat() < 0.2F) {
                baseType = PotionTypes.WATER;
                effect.add(new PotionEffect(MobEffects.LEVITATION, 200, 0));
            } else if (groundDist >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS)) {
                baseType = PotionTypes.WATER;
                effect.add(new PotionEffect(MobEffects.SLOWNESS, 900, 0));
            } else {
                baseType = PotionTypes.HARMING;
                effect.add(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 0));
            }

            ItemStack stack = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), baseType);
            PotionUtils.appendEffects(stack, effect);
            EntityPotion entitypotion = new EntityPotion(this.world, this, stack);
            entitypotion.rotationPitch -= -20.0F;
            entitypotion.shoot(d1, d0 + (double) (groundDist * 0.2F), d3, 0.75F, 8.0F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
            this.world.spawnEntity(entitypotion);
        }
    }

    public void registerCat(EntityWitchCat cat) {
        if (!this.cats.contains(cat)) {
            this.cats.add(cat);
        }
    }

    public void catDied(EntityWitchCat cat) {
        this.cats.remove(cat);
    }

    private void spawnCats() {
        this.hasSpawnedCats = true;
        int count = EZConfig.ENTITIES.WITHER_WITCH.catSpawnMin + this.rand.nextInt(EZConfig.ENTITIES.WITHER_WITCH.catSpawnMax);
        for (int i = 0; i < count; i++) {
            BlockPos spawnPos = this.getPosition().add(this.rand.nextInt(5) - 2, 0, this.rand.nextInt(5) - 2);
            EntityWitchCat cat = new EntityWitchCat(this.world);
            cat.setPositionAndRotation(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, this.rotationYaw, 0);
            cat.setOwner(this);
            this.cats.add(cat);
            this.world.spawnEntity(cat);
        }
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("HasSpawnedCats", this.hasSpawnedCats);
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.hasSpawnedCats = compound.getBoolean("HasSpawnedCats");
    }

    @Override
    protected float applyPotionDamageCalculations(@NotNull DamageSource source, float damage) {
        damage = super.applyPotionDamageCalculations(source, damage);
        if (source.getTrueSource() == this) damage = 0.0F;
        if (source.isMagicDamage()) damage = (float) ((double) damage * 0.15D);
        return damage;
    }

    @Override
    public boolean isPotionApplicable(@NotNull PotionEffect potion) {
        return potion.getPotion() != MobEffects.WITHER && potion.getPotion() != MobEffects.POISON && potion.getPotion() != MobEffects.LEVITATION
                && potion.getPotion() != MobEffects.SLOWNESS && super.isPotionApplicable(potion);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITCH_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ENTITY_WITCH_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITCH_DEATH;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesEZ.WITHER_WITCH;
    }

    @Override
    public float getEyeHeight() {
        return 1.62F;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }
}