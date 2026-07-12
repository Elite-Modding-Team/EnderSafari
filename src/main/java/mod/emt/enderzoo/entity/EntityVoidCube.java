package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.client.particle.InfinityParticle;
import mod.emt.enderzoo.config.EZConfig;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityVoidCube extends EntityMagmaCube {
    public EntityVoidCube(World world) {
        super(world);
        this.setSlimeSize(1, false);
        this.experienceValue = 20;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EZConfig.ENTITIES.VOID_CUBE.maxHealth);
    }

    @Override
    protected @NotNull EntitySlime createInstance() {
        return new EntityVoidCube(this.world);
    }

    @Override
    protected @NotNull EnumParticleTypes getParticleType() {
        return EnumParticleTypes.BLOCK_CRACK;
    }

    @Override
    protected boolean spawnCustomParticles() {
        int i = this.getSlimeSize();
        for (int j = 0; j < i * 8; ++j) {
            float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
            float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
            float f2 = MathHelper.sin(f) * i * 0.5F * f1;
            float f3 = MathHelper.cos(f) * i * 0.5F * f1;
            EnumParticleTypes enumparticletypes = this.getParticleType();
            double d0 = this.posX + f2;
            double d1 = this.posZ + f3;
            world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, Block.getStateId(Blocks.BEDROCK.getDefaultState()));
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posZ);

        if (!world.isAirBlock(new BlockPos(i, 0, j))) {
            double d0 = (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * 0.66D;
            int k = MathHelper.floor(this.posY - getYOffset() + d0);
            return world.getCombinedLight(new BlockPos(i, k, j), 0);
        } else {
            return 0;
        }
    }

    @Override
    public float getBrightness() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posZ);

        if (!world.isAirBlock(new BlockPos(i, 0, j))) {
            double d0 = (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * 0.66D;
            int k = MathHelper.floor(this.posY - getYOffset() + d0);
            return world.getLightBrightness(new BlockPos(i, k, j));
        } else {
            return 0.0F;
        }
    }

    @Override
    protected int getAttackStrength() {
        return (int) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
    }

    @Override
    protected void setSize(float width, float height) {
        super.setSize(1, 1);
    }

    @Override
    public void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(1, resetHealth);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EZConfig.ENTITIES.VOID_CUBE.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EZConfig.ENTITIES.VOID_CUBE.maxHealth);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public void onCollideWithPlayer(@NotNull EntityPlayer player) {
        int i = getSlimeSize();
        if (canEntityBeSeen(player) && this.getDistanceSq(player) < (double) i * (double) i && player.attackEntityFrom(DamageSource.causeMobDamage(this), getAttackStrength())) {
            playSound(SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    @Override
    protected float applyArmorCalculations(DamageSource source, float damage) {
        if (!source.isUnblockable()) {
            return Math.min(Math.max(damage - 3 - this.getSlimeSize(), this.getSlimeSize()) / 2, damage);
        }
        return damage;
    }

    // Object because of sidedness
    private final @Nonnull List<Object> particles = new ArrayList<>();
    private int actionDelay = 0;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!dead) {
            if (world.isRemote) {
                onLivingUpdateClient();
            } else {
                onLivingUpdateServer();
            }
        }
    }

    private void onLivingUpdateServer() {
        if (actionDelay-- <= 0) {
            for (EntityPlayer player : getClosestPlayers(EZConfig.ENTITIES.VOID_CUBE.blindnessRange)) {
                player.addPotionEffect(new BlindEffect());
            }
            actionDelay = (int) (20 * (.5f + .5f * rand.nextFloat()));
        }
    }

    @SideOnly(Side.CLIENT)
    private void onLivingUpdateClient() {
        if (actionDelay-- <= 0 && EZConfig.ENTITIES.VOID_CUBE.enableDarknessParticles) {
            particles.removeIf(o -> !((InfinityParticle) o).isAlive());
            if (particles.size() < 10) {
                float offsetX = (-5f + 10f * rand.nextFloat());
                float offsetY = (-5f + 10f * rand.nextFloat());
                float offsetZ = (-5f + 10f * rand.nextFloat());
                float maxSize = rand.nextFloat() * (rand.nextBoolean() ? (2 * 8 + 0.1f) : 3.9f);
                float color = rand.nextBoolean() ? 0 : rand.nextFloat() / 10;
                final InfinityParticle particle = new InfinityParticle(world, this.getPosition(), new Vector4f(color, color, color, 1f), new Vector4f(offsetX, offsetY, offsetZ, maxSize));
                particle.setMaxAge(20 * 10);
                Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                particles.add(particle);
                actionDelay = (int) (20 * (.5f + .5f * rand.nextFloat()));
            }
        }
    }

    public List<EntityPlayer> getClosestPlayers(double distance) {
        List<EntityPlayer> players = null;
        final double distanceSq = distance * distance;

        for (EntityPlayer player : world.playerEntities) {
            if (EntitySelectors.NOT_SPECTATING.apply(player)) {
                if ((distance < 0.0D || player.getDistanceSq(posX, posY, posZ) < distanceSq)) {
                    if (players == null) {
                        players = new ArrayList<>();
                    }
                    players.add(player);
                }
            }
        }

        return players != null ? players : Collections.emptyList();
    }

    private class BlindEffect extends PotionEffect {
        private boolean combined = false;

        public BlindEffect() {
            super(MobEffects.BLINDNESS, 100, 0, true, false);
        }

        @Override
        public boolean onUpdate(@Nonnull EntityLivingBase entity) {
            if (combined || (!dead && entity.getDistanceSq(posX, posY, posZ) < 8 * 8)) {
                return super.onUpdate(entity);
            } else {
                return false;
            }
        }

        @Override
        public void combine(@Nonnull PotionEffect other) {
            if (!(other instanceof BlindEffect)) {
                // We got combined with a normal blindness effect. This means we should no longer vanish when the player gets out of range.
                combined = true;
            }
            super.combine(other);
        }
    }

    protected boolean isValidLightLevel() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.world.getLightFor(EnumSkyBlock.SKY, pos) > this.rand.nextInt(32)) {
            return false;
        } else {
            int i = this.world.getLightFromNeighbors(pos);

            if (this.world.isThundering()) {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(pos);
                this.world.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        IBlockState state = this.world.getBlockState((new BlockPos(this)).down());
        return EZConfig.ENTITIES.VOID_CUBE.spawnInDarkness ? state.canEntitySpawn(this) && this.isValidLightLevel() : state.canEntitySpawn(this);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    protected @NotNull SoundEvent getSquishSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    protected @NotNull SoundEvent getJumpSound() {
        return SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesEZ.VOID_CUBE;
    }
}
