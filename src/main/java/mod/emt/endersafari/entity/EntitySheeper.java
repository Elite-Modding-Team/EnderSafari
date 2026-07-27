package mod.emt.endersafari.entity;

import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.registry.ModItemsES;
import mod.emt.endersafari.registry.ModLootTablesES;
import mod.emt.endersafari.registry.ModSoundEventsES;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EntitySheeper extends EntityAnimal implements IShearable {
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.<Boolean>createKey(EntitySheeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(EntitySheeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntitySheeper.class, DataSerializers.VARINT);
    private EntityAIEatGrass entityAIEatGrass;
    public int explosionRadius = 3;
    public int fuseTime = 30;
    public int lastActiveTime;
    private int sheepTimer;
    public int timeSinceIgnited;

    public EntitySheeper(World world) {
        super(world);
        this.setSize(0.9F, 1.3F);
    }

    @Override
    protected void initEntityAI() {
        this.entityAIEatGrass = new EntityAIEatGrass(this);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1D, ModItemsES.CONFUSING_DUST, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SHEARED, Boolean.FALSE);
        this.dataManager.register(STATE, -1);
        this.dataManager.register(IGNITED, Boolean.FALSE);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.SHEEPER.armor);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.SHEEPER.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.SHEEPER.movementSpeed);
    }

    @Override
    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
        if (!super.attackEntityFrom(source, amount)) {
            return false;
        }

        // Explode when getting hit but only by other entities and non-creative players
        if (!this.world.isRemote) {
            Entity attacker = source.getTrueSource();

            if (attacker instanceof EntityLivingBase && attacker != this) {
                if (attacker instanceof EntityPlayer && ((EntityPlayer) attacker).isCreative()) {
                    return true;
                }

                this.setSheeperState(1);
            }
        }

        return true;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    @Override
    public @Nullable EntityAgeable createChild(@NotNull EntityAgeable ageable) {
        return new EntitySheeper(this.world);
    }

    @Override
    public boolean isShearable(@NotNull ItemStack item, IBlockAccess world, BlockPos pos) {
        return !this.getSheared() && !this.isChild();
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@NotNull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        if (!this.world.isRemote) {
            this.setSheared(true);
            this.setSheeperState(-1);
            this.timeSinceIgnited = 0;
            List<ItemStack> ret = new java.util.ArrayList<>();

            for (int j = 0; j < 1; ++j) {
                ret.add(new ItemStack(Items.GUNPOWDER, 1));
            }

            this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            return ret;
        }

        return Collections.emptyList();
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getHeadRotationPointY(float rotation) {
        if (this.sheepTimer <= 0) {
            return 0.0F;
        } else if (this.sheepTimer >= 4 && this.sheepTimer <= 36) {
            return 1.0F;
        } else {
            return this.sheepTimer < 4 ? ((float) this.sheepTimer - rotation) / 4.0F : -((float) (this.sheepTimer - 40) - rotation) / 4.0F;
        }
    }

    @SideOnly(Side.CLIENT)
    public float getHeadRotationAngleX(float rotation) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float f = ((float) (this.sheepTimer - 4) - rotation) / 32.0F;
            return ((float) Math.PI / 5F) + ((float) Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
        } else {
            return this.sheepTimer > 0 ? ((float) Math.PI / 5F) : this.rotationPitch * 0.017453292F;
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == ModItemsES.CONFUSING_DUST;
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setShort("Fuse", (short) this.fuseTime);
        compound.setByte("ExplosionRadius", (byte) this.explosionRadius);
        compound.setBoolean("ignited", this.hasIgnited());
        compound.setBoolean("Sheared", this.getSheared());
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.hasKey("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }

        if (compound.getBoolean("ignited")) {
            this.ignite();
        }

        this.setSheared(compound.getBoolean("Sheared"));
    }

    public int getSheeperState() {
        return this.dataManager.get(STATE);
    }

    public void setSheeperState(int state) {
        this.dataManager.set(STATE, state);
    }

    public boolean hasIgnited() {
        return this.dataManager.get(IGNITED);
    }

    public void ignite() {
        this.dataManager.set(IGNITED, Boolean.TRUE);
    }

    @SideOnly(Side.CLIENT)
    public float getCreeperFlashIntensity(float partialTicks) {
        return ((float) this.lastActiveTime + (float) (this.timeSinceIgnited - this.lastActiveTime) * partialTicks) / (float) (this.fuseTime - 2);
    }

    private void explode() {
        if (!this.world.isRemote) {
            boolean flag = ForgeEventFactory.getMobGriefingEvent(this.world, this);
            float explosionRadius = this.isChild() ? 1.5F : 3.0F;
            this.dead = true;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, explosionRadius, flag);
            this.setDead();
            this.spawnLingeringCloud();
        }
    }

    private void spawnLingeringCloud() {
        Collection<PotionEffect> collection = this.getActivePotionEffects();

        if (!collection.isEmpty()) {
            EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
            cloud.setRadius(2.5F);
            cloud.setRadiusOnUse(-0.5F);
            cloud.setWaitTime(10);
            cloud.setDuration(cloud.getDuration() / 2);
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());

            for (PotionEffect potioneffect : collection) {
                cloud.addEffect(new PotionEffect(potioneffect));
            }

            this.world.spawnEntity(cloud);
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() instanceof ItemFlintAndSteel) {
            this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            player.swingArm(hand);

            if (!this.world.isRemote) {
                this.ignite();
                stack.damageItem(1, player);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void onUpdate() {
        if (this.isEntityAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;

            if (this.hasIgnited()) {
                this.setSheeperState(1);
            }

            int i = this.getSheeperState();

            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;

            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }

        super.onUpdate();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSoundEventsES.ENTITY_SHEEPER_HURT.getSoundEvent();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull Block block) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    public boolean getSheared() {
        return this.dataManager.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.dataManager.set(SHEARED, sheared);
    }

    @Override
    public float getEyeHeight() {
        return 0.95F * this.height;
    }

    @Override
    public void eatGrassBonus() {
        this.setSheared(false);
        this.setSheeperState(-1);

        if (this.isChild()) {
            this.addGrowth(60);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.SHEEPER;
    }
}