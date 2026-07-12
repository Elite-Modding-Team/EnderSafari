package mod.emt.endersafari.entity;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.registry.ModLootTablesES;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class EntityEnderizedZombie extends EntityZombie {
    public EntityEnderizedZombie(World world) {
        super(world);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ESConfig.ENTITIES.ENDERIZED_ZOMBIE.armor);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ESConfig.ENTITIES.ENDERIZED_ZOMBIE.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ESConfig.ENTITIES.ENDERIZED_ZOMBIE.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ESConfig.ENTITIES.ENDERIZED_ZOMBIE.movementSpeed);
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D
                );
            }
        }

        super.onLivingUpdate();
    }

    @Override
    public boolean attackEntityAsMob(@NotNull Entity entity) {
        if (!super.attackEntityAsMob(entity)) {
            return false;
        }

        float localDifficulty = this.world.getDifficultyForLocation(this.getPosition()).getAdditionalDifficulty();
        if (this.isEntityAlive() && entity instanceof EntityLivingBase) {
            double teleportChance = ESConfig.ENTITIES.ENDERIZED_ZOMBIE.teleportChanceTarget + (localDifficulty / 10.0F);
            if (this.rand.nextFloat() <= (float) Math.min(teleportChance, 1.0F)) {
                TeleportHelper.teleportEntity(entity.world, entity, false, true, (float) ESConfig.ENTITIES.ENDERIZED_ZOMBIE.teleportRangeTarget);
            }
        }

        return true;
    }

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
        if (!isEntityInvulnerable(source) && super.attackEntityFrom(source, amount)) {
            if (rand.nextFloat() < Math.min((float) ESConfig.ENTITIES.ENDERIZED_ZOMBIE.teleportChanceSelf, 1.0F)) {
                TeleportHelper.teleportEntity(world, this, false, true, (float) ESConfig.ENTITIES.ENDERIZED_ZOMBIE.teleportRangeSelf);
            }
            return true;
        }

        return false;
    }

    @Override
    protected boolean shouldBurnInDay() {
        return false;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesES.ENDERIZED_ZOMBIE;
    }

    @SubscribeEvent
    public static void onSummonAid(ZombieEvent.SummonAidEvent event) {
        if (event.getSummoner() instanceof EntityEnderizedZombie && ESConfig.ENTITIES.ENDERIZED_ZOMBIE.enableReinforcements) {
            if (event.getResult() != Event.Result.DENY && event.getAttacker() != null && event.getWorld().getDifficulty() == EnumDifficulty.HARD &&
                    event.getWorld().rand.nextFloat() < event.getSummoner().getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue() &&
                    event.getWorld().getGameRules().getBoolean("doMobSpawning")) {
                event.setResult(Event.Result.ALLOW);
                event.setCustomSummonedAid(new EntityEnderizedZombie(event.getWorld()));
            } else {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
