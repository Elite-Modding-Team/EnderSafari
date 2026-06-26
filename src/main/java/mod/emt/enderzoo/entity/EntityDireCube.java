package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class EntityDireCube extends EntityMagmaCube {
    public EntityDireCube(World world) {
        super(world);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    @Override
    protected @NotNull EntitySlime createInstance() {
        return new EntityDireCube(this.world);
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
            world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, Block.getStateId(Blocks.DIRT.getDefaultState()));
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
        int i = this.getSlimeSize();
        super.setSize(i, i);
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(size * 2.0F);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(size);
    }

    @Override
    public void onCollideWithPlayer(@NotNull EntityPlayer player) {
        int i = getSlimeSize();
        if (canEntityBeSeen(player) && this.getDistanceSq(player) < (double) i * (double) i && player.attackEntityFrom(DamageSource.causeMobDamage(this), getAttackStrength())) {
            playSound(SoundEvents.BLOCK_GRAVEL_BREAK, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    @Override
    protected float applyArmorCalculations(DamageSource source, float damage) {
        if (!source.isUnblockable()) {
            return Math.min(Math.max(damage - 3 - this.getSlimeSize(), this.getSlimeSize()) / 2, damage);
        }
        return damage;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.BLOCK_GRAVEL_STEP;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_GRAVEL_BREAK;
    }

    @Override
    protected @NotNull SoundEvent getSquishSound() {
        return SoundEvents.BLOCK_GRAVEL_BREAK;
    }

    @Override
    protected @NotNull SoundEvent getJumpSound() {
        return SoundEvents.BLOCK_GRAVEL_STEP;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesEZ.DIRE_CUBE;
    }
}
