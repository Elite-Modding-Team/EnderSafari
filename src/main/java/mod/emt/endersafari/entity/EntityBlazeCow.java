package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.registry.ModItemsES;
import mod.emt.endersafari.registry.ModLootTablesES;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityBlazeCow extends EntityCow {
    private float heightOffset;
    private int heightOffsetUpdateTime;

    public EntityBlazeCow(World world) {
        super(world);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, ModItemsES.WITHERING_DUST, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.BLAZE_COW.armor);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.BLAZE_COW.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.BLAZE_COW.movementSpeed);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!source.isMagicDamage() && source.getImmediateSource() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) source.getImmediateSource();

            if (!source.isExplosion()) {
                entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this).setFireDamage(), 2.0F);
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public @NotNull EntityBlazeCow createChild(net.minecraft.entity.@NotNull EntityAgeable ageable) {
        return new EntityBlazeCow(this.world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == ModItemsES.WITHERING_DUST;
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
    public boolean processInteract(EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() == Items.BUCKET && !this.isChild()) {
            player.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
            stack.shrink(1);

            if (stack.isEmpty()) {
                player.setHeldItem(hand, new ItemStack(Items.LAVA_BUCKET));
            } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.LAVA_BUCKET))) {
                player.dropItem(new ItemStack(Items.LAVA_BUCKET), false);
            }

            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    @Override
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        if (this.world.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(this.isChild() ? EnumParticleTypes.SMOKE_NORMAL : EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
            }
        }

        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = this.getAttackTarget();

        if (entitylivingbase != null && entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset) {
            this.motionY += (0.3D - this.motionY) * 0.3D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.world.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this) && this.world.checkNoEntityCollision(this.getEntityBoundingBox()) && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull Block block) {
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.BLAZE_COW;
    }
}
